package m6t3.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import m6t3.server.ServerMain;

class Connection {

	static final String DEFAULT_SERVER_HOST = "localhost";
	static final int DEFAULT_TIMEOUT = 3000;
	static final int RETRIES_COUNT = 3;
	
	final ClientMain client;
	String serverHost;
	int serverPort;
	Socket socket;
	final ClientReciever reciever;
	final ClientTransmitter transmitter;
	final Synchronizer synchronizer;
	
//	final Queue<Student> inQueue = new LinkedList<>();
	final BlockingQueue<Object> outQueue = new LinkedBlockingQueue<>();

	public Connection(ClientMain client) {
		this.serverHost = DEFAULT_SERVER_HOST;
		this.serverPort = ServerMain.DEFAULT_IP_PORT;
		this.client = client;
		reciever = new ClientReciever(this);
		reciever.start();
		transmitter = new ClientTransmitter(this);
		transmitter.start();
		synchronizer = new Synchronizer(client);
		synchronizer.start();
//		outQueue.add(SYNC_REQUEST);
	}

	void reconnect() {
		System.out.print("Reconnection ");
		while (!client.shell.isDisposed()) {
			int retries = RETRIES_COUNT;
			while (retries-- > 0) {
				System.out.print(retries + " ");
				try {
					socket.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				socket = new Socket();
				try {
					socket.connect(new InetSocketAddress(serverHost, serverPort), DEFAULT_TIMEOUT);
					reciever.in = socket.getInputStream();
					transmitter.out = socket.getOutputStream();
					System.out.println("success");
					return;
				} catch (IOException e) {
					//Здесь ничего не надо делать
				}
			}
			System.out.println();
			client.shell.getDisplay().syncExec(() -> showReconnDlg());
		}
	}
	
	void showReconnDlg() {
		if (!(boolean) new ConnectionDialog(client).open()) {
			client.shell.getDisplay().syncExec(() -> client.shell.close());
		}
	}
	
//	public void sendStudent(Student student) {
//		System.out.println("Передача студента в очередь отправки " + outQueue.add(student));
//		;
//	}

	public void disconnect() {
		synchronizer.interrupt();
		if (outQueue.isEmpty()) {
			transmitter.interrupt();
		}
		try {
			transmitter.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package m6t3.client;

import static m6t3.common.Const.INVALID_ID;
import static m6t3.common.Tranceiver.AUTH_ACKNOWLEDGEMENT;
import static m6t3.common.Tranceiver.AUTH_CHALLENGE;
import static m6t3.common.Tranceiver.receiveInt;
import static m6t3.server.ServerMain.DEFAULT_IP_PORT;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import m6t3.common.AuthAcknowledgement;
import m6t3.common.AuthChallenge;
import m6t3.common.LoginRequest;

class Connection {

	static final String DEFAULT_SERVER_HOST = "localhost";
	static final int DEFAULT_TIMEOUT = 3000;
	static final int RETRIES_COUNT = 3;
	
	final ClientMain client;
	String serverHost;
	int serverPort;
	Socket socket;
	final ClientReceiver receiver;
	final ClientTransmitter transmitter;
	final Synchronizer synchronizer;
	
	String login = "";
	char[] password;
	private AuthChallenge lastChallenge;
	boolean needAskLogin = true;
	
//	final Queue<Student> inQueue = new LinkedList<>();
	final BlockingQueue<Object> outQueue = new LinkedBlockingQueue<>();

	public Connection(ClientMain client) {
		this.serverHost = DEFAULT_SERVER_HOST;
		this.serverPort = DEFAULT_IP_PORT;
		this.client = client;
		receiver = new ClientReceiver(this);
		receiver.start();
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
				} catch (Exception e) {
					//Nothing to do here
				}
				socket = new Socket();
				try {
					socket.connect(new InetSocketAddress(serverHost, serverPort), DEFAULT_TIMEOUT);
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();
					System.out.print("success. Now authenticating...");
					if (authenticate(in, out)) {
						System.out.println("Ok.");
						receiver.in = in;
						transmitter.out = out;
						return;						
					}
				} catch (IOException e) {
					//Nothing to do here
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					System.err.println("Cryptographic error");
					e.printStackTrace();
				}
			}
			System.out.println("failed.");
			client.shell.getDisplay().syncExec(() -> showReconnDlg());
		}
	}
	
	private boolean authenticate(InputStream in, OutputStream out) 
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		if (needAskLogin) {
			client.shell.getDisplay().syncExec(() -> showLoginDialog());
		}
		new LoginRequest(login).transmit(out);
		if (receiveInt(in) != AUTH_CHALLENGE) return false;
		AuthChallenge challenge = AuthChallenge.receive(in);
		if (null != password) {
			challenge.createResponse(password).transmit(out);
			Arrays.fill(password, (char) 0);
			password = null;
		} else {
			challenge.createResponse(lastChallenge).transmit(out);
		}
		if (receiveInt(in) != AUTH_ACKNOWLEDGEMENT) return false;
		AuthAcknowledgement acknowledgement = AuthAcknowledgement.receive(in);
		if (INVALID_ID == acknowledgement.userId) return false;
		lastChallenge = challenge;
		needAskLogin = false;
		return true;
	}

	private void showLoginDialog() {
		if (!(boolean) new LoginDialog(client).open()) {
			client.shell.getDisplay().syncExec(() -> client.shell.close());
		}
	}

	private void showReconnDlg() {
		if (!(boolean) new ConnectionDialog(client).open()) {
			client.shell.getDisplay().syncExec(() -> client.shell.close());
		}
	}

	public void disconnect() {
		synchronizer.interrupt();
		if (outQueue.isEmpty()) {
			transmitter.interrupt();
		}
		try {
			transmitter.join();
		} catch (InterruptedException e) {
			//Nothing to do here
		}
	}
}

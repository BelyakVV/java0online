package m6t3.client;

import static m6t3.common.Tranciever.SEND_ALL;
import static m6t3.common.Tranciever.SEND_STUDENT;
import static m6t3.common.Tranciever.SYNC_INTERVAL;
import static m6t3.common.Tranciever.recieveInt;
import static m6t3.common.Tranciever.recieveStudent;
import static m6t3.common.Tranciever.recieveStudentsList;
import static m6t3.common.Tranciever.transmitInt;
import static m6t3.common.Tranciever.transmitStudent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import m6t3.common.Student;
import m6t3.server.ServerMain;

class Connection extends Thread {

	static final String DEFAULT_SERVER_HOST = "localhost";
	static final int DEFAULT_TIMEOUT = 3000;
	static final int RETRIES_COUNT = 3;
	
	ClientMain client;
	String serverHost;
	int serverPort;
	Socket socket;
	InputStream in;
	OutputStream out;
	
	Queue<Student> outStudents = new LinkedList<>();

	public Connection(ClientMain client) {
		this.serverHost = DEFAULT_SERVER_HOST;
		this.serverPort = ServerMain.DEFAULT_IP_PORT;
		this.client = client;
	}

	@Override
	public void run() {
		while(client.running) {
			try {
				if (in.available() >= 4) {
					int signature = recieveInt(in);
					if (SEND_ALL == signature) {
						LinkedList<Student> students = recieveStudentsList(in);
						if (client.table.getItemCount() < 1) {
							client.shell.getDisplay().asyncExec(() -> client.replaceStudents(students));
						} else {
							client.shell.getDisplay().asyncExec(() -> client.mergeStudents(students));						
						}
					} else if (SEND_STUDENT == signature) {
						Student student = recieveStudent(in);
						client.shell.getDisplay().asyncExec(() -> client.mergeStudent(student));
					} else {
						in.skipNBytes(in.available());
					}
				} else if (!outStudents.isEmpty()) {
					transmitInt(SEND_STUDENT, out);
					transmitStudent(outStudents.poll(), out);
				} else {
					Thread.sleep(SYNC_INTERVAL);
				}
			} catch (IOException | NullPointerException e) {
				reconnect();
			} catch (InterruptedException e) {
				//Ничего не надо делать
			}
		}
	}

	private void reconnect() {
		int retries = RETRIES_COUNT;
		while (!client.shell.isDisposed()) {
			while (retries-- > 0) {
				try {
					socket.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				socket = new Socket();
				try {
					socket.connect(new InetSocketAddress(serverHost, serverPort), DEFAULT_TIMEOUT);
					in = socket.getInputStream();
					out = socket.getOutputStream();
					return;
				} catch (IOException e) {
					//Здесь ничего не надо делать
				}
			}
			client.shell.getDisplay().syncExec(() -> showReconnDlg());
		}
	}
	
	void showReconnDlg() {
		if (!(boolean) new ConnectionDialog(client).open()) {
			client.shell.getDisplay().syncExec(() -> client.shell.close());
		}
	}
	
	public void sendStudent(Student student) {
		outStudents.add(student);
	}
}

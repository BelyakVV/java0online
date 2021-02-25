package m6t3.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import m6t3.server.ServerMain;
import m6t3.server.Student;

class Connection extends Thread {

	static final String DEFAULT_SERVER_HOST = "localhost";
	static final int DEFAULT_TIMEOUT = 3000;
	static final int RETRIES_COUNT = 3;
	static final long SYNC_INTERVAL = 500;
	
	static final int SEND_ALL = signatureToInt("BULK");
	static final int SEND_STUDENT = signatureToInt("STUD");
	static final int STOP = signatureToInt("STOP");
	
	ClientMain client;
	String serverHost;
	int serverPort;
	Socket socket;
	InputStream in;
	OutputStream out;

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
						if (client.students.isEmpty()) {
							client.replaceStudents(recieveStudentsList(in));
						} else {
							client.mergeStudents(recieveStudentsList(in));							
						}
					} else if (SEND_STUDENT == signature) {
						client.mergeStudent(recieveStudent(in));
					} else {
						in.close();
					}
				}
				if (client.running) {
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
		while (true) {
			while (retries-- > 0) {
				socket = new Socket();
				try {
					socket.connect(new InetSocketAddress(serverHost, serverPort), DEFAULT_TIMEOUT);
					return;
				} catch (IOException e) {
					//Здесь ничего не надо делать
				}
			}
			if (!client.showConnDlg()) {
				client.running = false;
				return;
			}
		}
	}
	
	static int signatureToInt(String str) {
		return ByteBuffer.wrap(str.getBytes()).getInt();
	}
	
	static int recieveInt(InputStream in) throws IOException {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) + in.read();
		}
		return result;
	}
	
	static List<Student> recieveStudentsList(InputStream in) throws IOException {
		List<Student> result = new LinkedList<>();
		int signature = recieveInt(in);
		while (signature == SEND_STUDENT) {
			result.add(recieveStudent(in));
			signature = recieveInt(in);
		}
		if (signature != STOP) {
			in.close();
		}
		return result;
	}
	
	static Student recieveStudent(InputStream in) throws IOException {
		int id = recieveInt(in);
		int serial = recieveInt(in);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String number = br.readLine();
		String surname = br.readLine();
		String name = br.readLine();
		String patronymic = br.readLine();
		return new Student(id, serial, number, surname, name, patronymic);
	}
}

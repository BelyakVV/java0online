package m6t3.server;

import static m6t3.common.Tranciever.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import m6t3.common.Student;

class SocketThread extends Thread {
	final Socket socket;
	final ServerThread server;
	
	static final long STOP_TIMEOUT = 1000;

	SocketThread(ServerThread server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}

	@Override
	public void run() {
		Student student = null;
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			while (server.running && !socket.isClosed()) {
				if (in.available() >= 4) {
					System.out.println("Серверу что-то прилетело");
					int signature = recieveInt(in);
					if (SEND_STUDENT == signature) {
						System.out.println("Это студент");
						student = recieveStudent(in);
						System.out.println("Сервер принял студента");
					} else {
						System.out.println("Сервер не опознал передачу");
						in.skipNBytes(in.available());
					}
				} else if (student != null) {
					System.out.println("Сервер передаёт студента");
					transmitInt(SEND_STUDENT, out);
					transmitStudent(student, out);
					student = null;
					out.flush();
					System.out.println("Сервер передал студента");
				} else {
					Thread.sleep(100);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
//			
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Сервер закрыл подключение");
	}
	
	
}

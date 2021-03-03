package m6t3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import m6t3.common.Student;

class ServerThread extends Thread {
	List<Student> students = new LinkedList<>();
	Queue<Student> changed = new LinkedList<>();
	int nextId = 0;
	final ServerSocket listener;
	final List<SocketThread> threads = new LinkedList<>();
	volatile boolean running = true;
	
	ServerThread(int port) throws IOException {
		listener = new ServerSocket(port);
	}

	@Override
	public void run() {
		while (running) {
			try {
				Socket socket = listener.accept();
				SocketThread thread = new SocketThread(this, socket);
				if (threads.add(thread)) {
					thread.start();
				} else {
					socket.close();
				}
			} catch (IOException e) {
				if (!running) {
					break;
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		closeSocket();
	}
	
	public void syncStop() throws InterruptedException {
		running = false;
		interrupt();
		while (!threads.isEmpty()) {
			SocketThread thread = threads.remove(0);
			thread.interrupt();
		}
		closeSocket();		
	}

	private void closeSocket() {
		try {
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateStudent(Student upd) {
		synchronized (students) {
			if (upd.id < 0) {
				if (upd.getSerial() >= 0) {
					Student student = new Student(nextId++, upd);
					if (students.add(student)) {
						changed.add(student);
					}
				}
				return;
			}
			for (var student: students) {
				if (student.id == upd.id) {
					if (upd.getSerial() < 0) {
						students.remove(student);
						changed.add(upd);
					} else {
						upd.incSerial();
						if (student.update(upd)) {
							changed.add(student);
						}
					}
					return;
				}
			}
		}
	}
}

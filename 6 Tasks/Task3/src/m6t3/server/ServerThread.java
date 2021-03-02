package m6t3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

class ServerThread extends Thread {
//	int port;
	final ServerSocket listener;
	final List<SocketThread> threads = new LinkedList<>();
//	final ThreadGroup threadGroup;
	volatile boolean running = true;
	
	ServerThread(int port) throws IOException {
//		this.port = port;
		listener = new ServerSocket(port);
//		threadGroup = new ThreadGroup("Port " + Integer.toString(port));
	}

	@Override
	public void run() {
		while (running) {
			try {
				System.out.println("Сервер ждёт подключения");
				Socket socket = listener.accept();
				System.out.println("Сервер установил подключение");
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
//				if (!running) return;
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
}

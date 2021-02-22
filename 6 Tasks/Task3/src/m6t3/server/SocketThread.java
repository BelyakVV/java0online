package m6t3.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			while (server.running && !socket.isClosed()) {
				out.write(in.read());
			}
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}

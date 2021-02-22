package m6t3.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

class Synchronizator extends Thread {
	ClientMain client;
	String host;
	int port;
	Socket socket = new Socket();
	boolean running = true;
	static final long SYNC_PERIOD = 1000;
	
	Synchronizator(ClientMain client, String host, int port) {
		this.client = client;
		this.host = host;
		this.port = port;
		
	}

	@Override
	public void run() {
		while (running) {
			
			try {
				sleep(SYNC_PERIOD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	boolean connect() {
		if (socket.isConnected()) return true;
		try {
			socket.connect(new InetSocketAddress(host, port));
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}

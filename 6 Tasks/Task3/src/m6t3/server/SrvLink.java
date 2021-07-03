package m6t3.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class SrvLink {
	final SrvListener server;
	final Socket socket;
	final SrvReceiver reciever;
	final SrvTransmitter transmitter;
	final BlockingQueue<Object> outQueue;
	volatile boolean running = true;
	
//	static final long STOP_TIMEOUT = 1000;

	SrvLink(SrvListener server, Socket socket) throws IOException {
		this.server = server;
		this.socket = socket;
		outQueue = new LinkedBlockingQueue<>();
		reciever = new SrvReceiver(this);
		reciever.start();	
		transmitter = new SrvTransmitter(this);
		transmitter.start();
	}
	
	void close() {
		if (!running) return;
		running = false;
		try {
			if (socket.getInputStream().available() < 1) {
				socket.getInputStream().close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (Thread.currentThread() != reciever) reciever.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		transmitter.interrupt();
		try {
			if (Thread.currentThread() != transmitter) transmitter.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.links.remove(this);
		System.out.println("Link is shut down");
	}	
}

package m6t3.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import m6t3.common.Student;

class SrvLink {
	final ServerThread server;
	final Socket socket;
	final SrvReciever reciever;
	final SrvTransmitter transmitter;
	final BlockingQueue<Student> outQueue;
	
	static final long STOP_TIMEOUT = 1000;

	SrvLink(ServerThread server, Socket socket) throws IOException {
		if (!server.running) {
			throw new RuntimeException("Сервер останавливается");
		}
		this.server = server;
		this.socket = socket;
		outQueue = new LinkedBlockingQueue<>();
		reciever = new SrvReciever(this);
		reciever.start();	
		transmitter = new SrvTransmitter(this);
		transmitter.start();
	}
	
	void close() {
		reciever.interrupt();
		transmitter.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}

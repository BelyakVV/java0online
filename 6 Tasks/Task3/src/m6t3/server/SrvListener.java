package m6t3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.ParserConfigurationException;

import m6t3.common.User;

class SrvListener extends Thread {
	final ServerSocket socket;
	final Queue<SrvLink> links = new LinkedList<>();
	private volatile boolean running = true;
	
	final SrvSyncSender syncSender;
	final SrvData data;
	
	SrvListener(int port, String fileName) throws IOException, ParserConfigurationException {
		data = new SrvData(this, fileName);
		socket = new ServerSocket(port);
		syncSender = new SrvSyncSender(this);
	}

	@Override
	public void run() {
		data.start();
		syncSender.start();
		while (true) {
			try {
				links.add(new SrvLink(this, socket.accept()));
			} catch (IOException e) {
				if (!running) break;
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		syncSender.interrupt();
		while (!links.isEmpty()) {
			links.peek().close();
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.interrupt();
		try {
			data.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void broadcast(Object obj) {
		for (var link: links) {
			link.outQueue.add(obj);
		}		
	}

	/**
	 * 	Disconnect all clients which are logged in as user given.
	 * 
	 * @param user User to be disconnected
	 */
	public void disconnect(User user) {
		for (var link: links) {
			if (link.user.id == user.id) {
				link.close();
			}
		}
	}
	
	public void close() throws InterruptedException {
		running = false;
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.join();
	}
}

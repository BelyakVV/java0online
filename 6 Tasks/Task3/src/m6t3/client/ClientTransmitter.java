package m6t3.client;

import static m6t3.common.Tranceiver.transmitInt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import m6t3.common.Transmittable;

class ClientTransmitter extends Thread {
	static final long WAIT_UNTIL_LINK_IS_READY = 100;
	final ClientMain client;
	final Connection connection;
	private final BlockingQueue<Object> outQueue = new LinkedBlockingQueue<>();
	OutputStream out = null;

	ClientTransmitter(Connection connection) {
		client = connection.client;
		this.connection = connection;
	}

	@Override
	public void run() {
		try {
			while (true) {
				while (null == out) {
					if (!client.isRunning()) return;
					Thread.sleep(WAIT_UNTIL_LINK_IS_READY);
				}
				Object obj = outQueue.take();
				var objClass = obj.getClass();
				try {
					if (obj instanceof Transmittable) {
							((Transmittable) obj).transmit(out);
					} else if (Integer.class == objClass) {
						transmitInt((Integer) obj, out);
					}
				} catch (IOException e) {
					outQueue.add(obj);
					out = null;
					connection.reconnect();
				}
			}
		} catch (InterruptedException e) {
			//Nothing to do here
		}
	}

	public void terminate() {
		if (outQueue.isEmpty()) {
			this.interrupt();
		}
		try {
			this.join();
		} catch (InterruptedException e) {
			//Nothing to do here
		}
	}

	public void send(Object obj) {
		outQueue.add(obj);
	}	
}

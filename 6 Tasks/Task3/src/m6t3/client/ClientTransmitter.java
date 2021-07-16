package m6t3.client;

import static m6t3.common.Tranceiver.transmitInt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import m6t3.common.Transmittable;

/**
 *  Client-side data transmitting thread.
 *
 * @author aabyodj
 */
class ClientTransmitter extends Thread {
	
	static final long WAIT_UNTIL_LINK_IS_READY = 100;
	
	/** Client application main window */
	final ClientMain client;
	
	/** Network connection controller */
	final Connection connection;
	
	/** Queue of objects to be transmitted */
	private final BlockingQueue<Object> outQueue = new LinkedBlockingQueue<>();
	
	OutputStream out = null;

	/**
	 *  Create an instance of client-side data transmitter.
	 *  
	 * @param connection Network connection controller
	 */
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
					outQueue.add(obj); 	//Return unsent object back to queue
					out = null;			//Invalidate output stream
					connection.reconnect();
				}
			}
		} catch (InterruptedException e) {
			//Nothing to do here
		}
	}

	/**
	 * Terminate the transmitting thread when closing the application.
	 */
	void terminate() {
		if (outQueue.isEmpty()) {
			this.interrupt(); //Probably redundant as there is client.isRunning() inside the loop
		}
		try {
			this.join();
		} catch (InterruptedException e) {
			//Nothing to do here
		}
	}

	/**
	 *  Asynchronously send an object to the server.
	 *  
	 * @param obj An object to be sent
	 */
	public void send(Object obj) {
		outQueue.add(obj);
	}	
}

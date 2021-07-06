package m6t3.server;

import static m6t3.common.Tranceiver.transmitInt;
import static m6t3.common.Tranceiver.transmitLong;
import static m6t3.common.Tranceiver.transmitStudent;

import java.io.IOException;
import java.io.OutputStream;

import m6t3.common.Student;
import m6t3.common.User;

class SrvTransmitter extends Thread {
//	final SrvListener server;
	final SrvLink link;
	final OutputStream out;
	
	SrvTransmitter(SrvLink link) throws IOException {
//		server = link.server;
		this.link = link;
		out = link.socket.getOutputStream();
	}

	@Override
	public void run() {
		try {
			while (link.running || !link.outQueue.isEmpty()) {
				Object obj = link.outQueue.take();
				if (obj instanceof Student) {
					transmitStudent((Student) obj, out);
				} else if (obj instanceof Long) {
					transmitLong((Long) obj, out);
				} else if (obj instanceof Integer) {			
					transmitInt((Integer) obj, out);
				} else if (obj instanceof User) {
					((User) obj).transmit(out);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		System.out.println("Transmitter is stopped");
		link.close();
	}
	
}

package m6t3.server;

import static m6t3.common.Tranciever.transmitInt;
import static m6t3.common.Tranciever.transmitStudent;

import java.io.IOException;
import java.io.OutputStream;

import m6t3.common.Student;

class SrvTransmitter extends Thread {
	final SrvLink link;
	final OutputStream out;
	
	SrvTransmitter(SrvLink link) throws IOException {
		this.link = link;
		out = link.socket.getOutputStream();
	}

	@Override
	public void run() {
		try {
			while (true) {
				Object obj = link.outQueue.take();
				if (obj instanceof Student) {
					transmitStudent((Student) obj, out);
				} else if (obj instanceof Integer) {
					transmitInt((Integer) obj, out);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		link.close();
	}
	
}

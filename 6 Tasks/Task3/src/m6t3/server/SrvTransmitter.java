package m6t3.server;
import static m6t3.common.Tranciever.*;

import java.io.IOException;
import java.io.OutputStream;

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
				transmitStudent(link.outQueue.take(), out);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		link.close();
	}
	
}

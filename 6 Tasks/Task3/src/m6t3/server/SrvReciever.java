package m6t3.server;

import static m6t3.common.Tranciever.*;
import static m6t3.common.Tranciever.recieveInt;
import static m6t3.common.Tranciever.recieveStudent;

import java.io.IOException;
import java.io.InputStream;

class SrvReciever extends Thread {
	final SrvLink link;
	final InputStream in;
	
	
	SrvReciever(SrvLink link) throws IOException {
		this.link = link;
		in = link.socket.getInputStream();
	}

	@Override
	public void run() {
		try {
			while (true) {				
				int signature = recieveInt(in);
//				System.out.println(signature);
				if (SEND_STUDENT == signature) {
					System.out.println("Recieved a student");
					link.server.updateStudent(recieveStudent(in));
				} else if (SYNC_REQUEST == signature) {
					System.out.println("Recieved a request of full sync");
					link.outQueue.addAll(link.server.students);
				} else {
					System.err.println("Сервер не опознал передачу");
					break;
//					in.skipNBytes(in.available());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		link.close();
	}

}

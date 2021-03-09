package m6t3.server;

import static m6t3.common.Tranciever.SEND_STUDENT;
import static m6t3.common.Tranciever.SYNC_REQUEST;
import static m6t3.common.Tranciever.recieveInt;
import static m6t3.common.Tranciever.recieveStudent;

import java.io.IOException;
import java.io.InputStream;

class SrvReciever extends Thread {
//	final SrvListener server;
	final SrvLink link;
	final SrvData data;
	final InputStream in;
	
	
	SrvReciever(SrvLink link) throws IOException {
//		server = link.server;
		this.link = link;
		data = link.server.data;
		in = link.socket.getInputStream();
	}

	@Override
	public void run() {
		try {
			while (link.running || in.available() > 0) {				
				int signature = recieveInt(in);
				if (SEND_STUDENT == signature) {
					System.out.println("Recieved a student");
					data.updateStudent(recieveStudent(in));
				} else if (SYNC_REQUEST == signature) {
					System.out.println("Recieved a request of full sync");
					link.outQueue.addAll(data.students);
				} else {
					System.err.println("Сервер не опознал передачу");
					in.skipNBytes(in.available());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		System.out.println("Reciever is stopped");
		link.close();
	}
}

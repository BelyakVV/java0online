package m6t3.server;

import static m6t3.common.Tranciever.SEND_STUDENT;
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
					link.server.updateStudent(recieveStudent(in));
				} else {
					System.err.println("Сервер не опознал передачу");
					link.close();
					in.skipNBytes(in.available());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		link.close();
	}

}

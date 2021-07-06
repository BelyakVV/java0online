package m6t3.server;

import static m6t3.common.Tranceiver.SEND_STUDENT;
import static m6t3.common.Tranceiver.SEND_USER;
import static m6t3.common.Tranceiver.SYNC_STUDENTS_REQUEST;
import static m6t3.common.Tranceiver.SYNC_USERS_REQUEST;
import static m6t3.common.Tranceiver.receiveInt;
import static m6t3.common.Tranceiver.receiveStudent;

import java.io.IOException;
import java.io.InputStream;

import m6t3.common.User;

class SrvReceiver extends Thread {
//	final SrvListener server;
	final SrvLink link;
	final SrvData data;
	final InputStream in;
	
	
	SrvReceiver(SrvLink link) throws IOException {
//		server = link.server;
		this.link = link;
		data = link.server.data;
		in = link.socket.getInputStream();
	}

	@Override
	public void run() {
		try {
			while (link.running || in.available() > 0) {				
				int signature = receiveInt(in);
				if (SEND_STUDENT == signature) {
//					System.out.println("Received a student");
					data.updateStudent(receiveStudent(in));
				} else if (SEND_USER == signature) {
					System.out.println("Received a user");
					data.updateUser(User.receive(in));
				} else if (SYNC_STUDENTS_REQUEST == signature) {
//					System.out.println("Received a students full sync request");
//					data.printStudents();
					link.outQueue.addAll(data.students);
				} else if (SYNC_USERS_REQUEST == signature) {
					System.out.println("Received a users full sync request");
					data.printUsers();
					link.outQueue.addAll(data.users);
				} else {
					System.err.println("Unknown request received");
					in.skipNBytes(in.available());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		System.out.println("Receiver is stopped");
		link.close();
	}
}

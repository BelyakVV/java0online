package m6t3.server;

import static m6t3.common.Tranceiver.CHANGE_PASS_REQUEST;
import static m6t3.common.Tranceiver.SEND_STUDENT;
import static m6t3.common.Tranceiver.SEND_USER;
import static m6t3.common.Tranceiver.SYNC_STUDENTS_REQUEST;
import static m6t3.common.Tranceiver.SYNC_USERS_REQUEST;
import static m6t3.common.Tranceiver.receiveInt;

import java.io.IOException;
import java.io.InputStream;

import m6t3.common.ChangePassRequest;
import m6t3.common.Student;
import m6t3.common.User;

class SrvReceiver extends Thread {
	final SrvLink link;
	final SrvData data;
	final InputStream in;
	
	
	SrvReceiver(SrvLink link) throws IOException {
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
					Student student = Student.receive(in);
					if (link.user.isAdmin()) data.updateStudent(student);
				} else if (SEND_USER == signature) {
					User user = User.receive(in);
					if (link.user.isAdmin()) data.updateUser(user);
				} else if (SYNC_STUDENTS_REQUEST == signature) {
					link.outQueue.addAll(data.students);
				} else if (SYNC_USERS_REQUEST == signature) {
					if (link.user.isAdmin()) link.outQueue.addAll(data.users);
				} else if (CHANGE_PASS_REQUEST == signature) {
					link.user.setPassword(ChangePassRequest.receive(in));
					data.touch();
					link.server.broadcast(link.user);
				} else {
					in.skip(in.available());
				}
			}
		} catch (Exception e) {
			//Nothing to do here
		}
		link.close();
	}
}

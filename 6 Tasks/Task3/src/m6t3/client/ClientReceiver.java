package m6t3.client;

import static m6t3.common.Tranceiver.SEND_STUDENT;
import static m6t3.common.Tranceiver.SEND_USER;
import static m6t3.common.Tranceiver.STUDENTS_CHECKSUM;
import static m6t3.common.Tranceiver.receiveInt;

import java.io.InputStream;

import m6t3.common.Student;
import m6t3.common.User;

/**
 * Receiving facility of the client part of a network connection.
 *
 * @author aabyodj
 */
public class ClientReceiver extends Thread {
	
	/** The main window of the client application */
	final ClientMain client;
	
	/** The controller of the client part of a network connection */
	final Connection connection;
	
	InputStream in = null;

	ClientReceiver(Connection connection) {
		this.connection = connection;
		client = connection.client;
	}
	
	@Override
	public void run() {
		while (true) {				
			try {
				int signature = receiveInt(in);
				if (SEND_STUDENT == signature) {
					client.mergeStudent(Student.receive(in));
				} else if (SEND_USER == signature) {
					client.mergeUser(User.receive(in));
				} else if (STUDENTS_CHECKSUM == signature) {
					int checksum = receiveInt(in);
					connection.synchronizer.setSrvChecksum(checksum);
				} else {
					System.err.println("Пакет данных неизвестного формата");
					in.skip(in.available());
				}
			} catch (Exception e) {
				if (!client.isRunning()) break;
				connection.reconnect();
			}
		}
	}
}

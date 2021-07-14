package m6t3.client;

import static m6t3.common.Tranceiver.SEND_STUDENT;
import static m6t3.common.Tranceiver.SEND_USER;
import static m6t3.common.Tranceiver.STUDENTS_CHECKSUM;
import static m6t3.common.Tranceiver.receiveInt;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.widgets.Display;

import m6t3.common.Student;
import m6t3.common.User;

public class ClientReceiver extends Thread {
	final ClientMain client;
	final Connection connection;
	InputStream in = null;
	final Queue<Student> studentsQueue = new LinkedList<>();
	final Queue<User> usersQueue = new LinkedList<>();
	final Display display;

	ClientReceiver(Connection connection) {
		this.connection = connection;
		client = connection.client;
		display = client.getDisplay();
	}
	
	@Override
	public void run() {
		while (true) {				
			try {
				int signature = receiveInt(in);
				//				System.out.println(signature);
				if (SEND_STUDENT == signature) {
//					System.out.println("Приём студента");
					studentsQueue.add(Student.receive(in));
//					System.out.println("Received a student: \n" + studentsQueue.peek());
					display.wake();
				} else if (SEND_USER == signature) {
//					System.out.println("Приём пользователя");
					User user = User.receive(in);
					if (client.isAdmin()) {
//						System.out.println("Received a user: \n" + user);
						usersQueue.add(user);
						display.wake();
					}
				} else if (STUDENTS_CHECKSUM == signature) {
					int checksum = receiveInt(in);
					connection.synchronizer.setSrvChecksum(checksum);
				} else {
					System.err.println("Invalid transmittion detected");
//					in.skipNBytes(in.available());
					in.skip(in.available());
//					connection.reconnect();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				if (!client.isRunning()) {
//					System.out.println("Client receiver stopped.");
					break;
				}
//				System.err.println("Client receiving loop abandoned or still not started. Reconnecting.");
				connection.reconnect();
			}
		}
	}
}

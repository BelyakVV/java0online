package m6t3.client;

import static m6t3.common.Tranciever.SEND_STUDENT;
import static m6t3.common.Tranciever.recieveInt;
import static m6t3.common.Tranciever.recieveStudent;

import java.io.InputStream;

public class ClntReciever extends Thread {
	final ClientMain client;
	final Connection connection;
	InputStream in;

	ClntReciever(Connection connection) {
		this.connection = connection;
		client = connection.client;
		try {
			in = connection.socket.getInputStream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while (true) {				
			try {
				int signature = recieveInt(in);
				//				System.out.println(signature);
				if (SEND_STUDENT == signature) {
					connection.inStudents.add(recieveStudent(in));
					//					client.shell.getDisplay().asyncExec(() -> client.mergeStudent(student));
				} else {
					System.err.println("Клиент не опознал передачу");
					in.skipNBytes(in.available());
					connection.reconnect();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (!client.running) {
					break;
				}
				connection.reconnect();
			}
		}
	}
}

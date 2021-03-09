package m6t3.client;

import static m6t3.common.Tranciever.CHECKSUM;
import static m6t3.common.Tranciever.SEND_STUDENT;
import static m6t3.common.Tranciever.recieveInt;
import static m6t3.common.Tranciever.recieveStudent;

import java.io.InputStream;

import m6t3.common.Student;

public class ClientReciever extends Thread {
	final ClientMain client;
	final Connection connection;
	InputStream in;

	ClientReciever(Connection connection) {
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
					System.out.println("Приём студента");
//					connection.inQueue.add(recieveStudent(in));
					Student srvStudent = recieveStudent(in);
					client.shell.getDisplay().asyncExec(() -> client.mergeStudent(srvStudent));
				} else if (CHECKSUM == signature) {
					int t = recieveInt(in);
//					System.out.println("Recieved checksum = " + Integer.toHexString(t));
					connection.synchronizer.srvChecksum = t;
				} else {
					System.err.println("Клиент не опознал передачу");
					in.skipNBytes(in.available());
//					connection.reconnect();
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

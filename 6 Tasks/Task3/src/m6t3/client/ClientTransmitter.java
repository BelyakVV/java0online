package m6t3.client;

import static m6t3.common.Tranciever.transmitStudent;

import java.io.IOException;
import java.io.OutputStream;

class ClientTransmitter extends Thread {
	final ClientMain client;
	final Connection connection;
	OutputStream out;

	ClientTransmitter(Connection connection) {
		client = connection.client;
		this.connection = connection;
		try {
			out = connection.socket.getOutputStream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (client.running || !connection.outStudents.isEmpty()) {
			try {
				transmitStudent(connection.outStudents.take(), out);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (!client.running && connection.outStudents.isEmpty()) {
					break;
				}
				connection.reconnect();
			} 
		}
		try {
			connection.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

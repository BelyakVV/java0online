package m6t3.client;

import static m6t3.common.Tranceiver.*;

import java.io.IOException;
import java.io.OutputStream;

import m6t3.common.Student;
import m6t3.common.User;

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
			System.err.println("Unable to acquire an output stream from the socket");
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				while (null == out) {
					Thread.sleep(100);
				}
				Object obj = connection.outQueue.take();
				var objClass = obj.getClass();
				try {
					if (Student.class == objClass) {
							transmitStudent((Student) obj, out);
					} else if (Integer.class == objClass) {
						transmitInt((Integer) obj, out);
//						out.flush();
					} else if (User.class == objClass) {
						((User) obj).transmit(out);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					connection.outQueue.add(obj);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("Client transmitter stopped. Closing the socket.");
		}
		try {
			connection.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}	
}

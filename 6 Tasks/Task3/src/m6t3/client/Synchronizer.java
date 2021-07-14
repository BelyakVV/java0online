package m6t3.client;

import static m6t3.common.Tranceiver.SYNC_INTERVAL;
import static m6t3.common.Tranceiver.SYNC_STUDENTS_REQUEST;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import m6t3.common.Student;

class Synchronizer extends Thread {
	final ClientMain client;
	final Display display;
	private int srvChecksum = 0;
	final Table table;
	private int myChecksum = 0;
	final ClientTransmitter transmitter;
	
	Synchronizer(ClientMain client, Table table, Connection connection) {
		this.client = client;
		display = client.getDisplay();
		transmitter = connection.transmitter;
		this.table = table;
//		System.out.println("Создание синхронизатора");
	}

	@Override
	public void run() {
		transmitter.send(SYNC_STUDENTS_REQUEST);
		try {
			while (client.isRunning()) {
				display.syncExec(() -> calcChecksum());
//				System.out.println("Me: " + myChecksum + ", Server: " + srvChecksum);
				if (myChecksum != srvChecksum) {
//					System.out.println("My: " + Integer.toHexString(myChecksum) + ", server: " + Integer.toHexString(srvChecksum));
					transmitter.send(SYNC_STUDENTS_REQUEST);
				}
				srvChecksum = 0;
				Thread.sleep(SYNC_INTERVAL);
			}
		} catch (InterruptedException e) {
			//Nothing to do here
		}
//		System.out.println("Synchronizer stopped");
	}
	
	void calcChecksum() {
		myChecksum = 0;
		for (int i = 0; i < table.getItemCount(); i++) {
			myChecksum += ((Student) table.getItem(i).getData()).hashCode();
		}		
	}

	public void setSrvChecksum(int checksum) {
		srvChecksum = checksum;
	}
}

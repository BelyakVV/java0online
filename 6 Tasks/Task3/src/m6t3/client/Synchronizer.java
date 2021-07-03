package m6t3.client;

import static m6t3.common.Tranceiver.SYNC_INTERVAL;
import static m6t3.common.Tranceiver.SYNC_STUDENTS_REQUEST;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import m6t3.common.Student;

class Synchronizer extends Thread {
	final ClientMain client;
	final Display display;
	public volatile int srvChecksum = 0;
	Table table;
	private int myChecksum = 0;
	
	Synchronizer(ClientMain client) {
		this.client = client;
		display = client.shell.getDisplay();
		table = client.table;
//		System.out.println("Создание синхронизатора");
	}

	@Override
	public void run() {
		client.connection.outQueue.add(SYNC_STUDENTS_REQUEST);
		try {
			while (true) {
				display.syncExec(() -> calcChecksum());
//				System.out.println("Me: " + myChecksum + ", Server: " + srvChecksum);
				if (myChecksum != srvChecksum) {
					System.out.println("My: " + Integer.toHexString(myChecksum) + ", server: " + Integer.toHexString(srvChecksum));
					client.connection.outQueue.add(SYNC_STUDENTS_REQUEST);
				}
				srvChecksum = 0;
				Thread.sleep(SYNC_INTERVAL);
			}
		} catch (InterruptedException e) {
			System.out.println("Synchronizer stopped");
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	void calcChecksum() {
		myChecksum = 0;
		for (int i = 0; i < table.getItemCount(); i++) {
			myChecksum += ((Student) table.getItem(i).getData()).hashCode();
		}		
	}
}

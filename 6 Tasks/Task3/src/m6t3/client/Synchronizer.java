package m6t3.client;

import static m6t3.common.Tranceiver.SYNC_INTERVAL;
import static m6t3.common.Tranceiver.SYNC_STUDENTS_REQUEST;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import m6t3.common.Student;

/**
 *  The Synchronizer constantly in background checks the main contents of the
 * archive against the server and sends an update request when needed.
 *
 * @author aabyodj
 */
class Synchronizer extends Thread {
	
	/** The main window of the application */
	final ClientMain client;
	
	final Display display;
	
	/** The last checksum received from the server */
	private int srvChecksum = 0;
	
	/** The table showing the archive contents */
	final Table table;
	
	/** The last local checksum */
	private int myChecksum = 0;
	
	/** The transmitting part of the network connection controller */
	final ClientTransmitter transmitter;
	
	/**
	 * Create an instance of data synchronizer.
	 * @param client The main application window
	 * @param table The table showing the archive
	 * @param connection The network connection controller
	 */
	Synchronizer(ClientMain client, Table table, Connection connection) {
		this.client = client;
		display = client.getDisplay();
		transmitter = connection.transmitter;
		this.table = table;
	}

	@Override
	public void run() {
		
		//Initial request of full archive
		transmitter.send(SYNC_STUDENTS_REQUEST);
		
		try {
			while (client.isRunning()) {
				display.syncExec(() -> calcChecksum());
				if (myChecksum != srvChecksum) {
					transmitter.send(SYNC_STUDENTS_REQUEST);
				}
				srvChecksum = 0;
				Thread.sleep(SYNC_INTERVAL);
			}
		} catch (InterruptedException e) {
			//Nothing to do here
		}
	}
	
	/**
	 * Calculate the check sum of local data.
	 */
	private void calcChecksum() {
		myChecksum = 0;
		for (int i = 0; i < table.getItemCount(); i++) {
			Student student = (Student) table.getItem(i).getData();
			myChecksum += student.hashCode();
		}		
	}

	/**
	 * Get into account a new server-side checksum.
	 * @param checksum New checksum from server
	 */
	public void setSrvChecksum(int checksum) {
		srvChecksum = checksum;
	}
}

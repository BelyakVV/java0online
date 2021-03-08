package m6t3.server;

import static m6t3.common.Tranciever.CHECKSUM;
import static m6t3.common.Tranciever.SYNC_INTERVAL;
class SyncSender extends Thread {
	final ServerThread server;
	private volatile int checksum = 0;

	SyncSender(ServerThread server) {
		this.server = server;
	}

	public int getChecksum() {
		return checksum;
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (server.changed) {
					server.changed = false;
					int newChecksum = 0;
					synchronized (server.students) {
						for (var student: server.students) {
							newChecksum += student.getSerial();
						}
					}
					checksum = newChecksum;
				}
				for (var link: server.links) {
					synchronized (link.outQueue) {
						link.outQueue.add(CHECKSUM);
						link.outQueue.add(checksum);
					}
				}
				Thread.sleep(SYNC_INTERVAL >> 1);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}

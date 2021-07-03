package m6t3.server;

import static m6t3.common.Tranceiver.STUDENTS_CHECKSUM;
import static m6t3.common.Tranceiver.SYNC_INTERVAL;
import static m6t3.common.Tranceiver.createMessage;

class SrvSyncSender extends Thread {
	private final SrvListener server;

	SrvSyncSender(SrvListener server) {
		this.server = server;
	}

//	public int getChecksum() {
//		return checksum;
//	}

	@Override
	public void run() {
		try {
			while (true) {
//				System.out.println(Integer.toHexString(server.data.getChecksum()));
				server.broadcast(createMessage(STUDENTS_CHECKSUM, server.data.getChecksum()));
				Thread.sleep(SYNC_INTERVAL >> 1);
			}
		} catch (InterruptedException e) {
			//Ничего не надо делать
		}
	}	

	
}

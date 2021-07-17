package m6t4;

import static m6t4.MainForm.shipQueue;

/**
 *
 * @author aabyodj
 */
public class QueueDispatcher extends Thread {

    @Override
    public void run() {
        try {
            while (true) {
                sleep(1000);
                String name = "Неустрашимый";
                int capacity = 100;
                shipQueue.add(new Ship(name, capacity));
            }
        } catch (InterruptedException ex) {
            //Nothing to do here
        }

    }

}

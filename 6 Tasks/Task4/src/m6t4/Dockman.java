package m6t4;

import static m6t4.MainForm.shipQueue;

/**
 *
 * @author aabyodj
 */
public class Dockman extends Thread {

//    final BlockingQueue<Ship> shipQueue; 
    final PierPanel pier;
    private long stepDuration;
    public final static long DEFAULT_STEP_DURATION = 500;

    public Dockman(PierPanel pier) {
        this.pier = pier;
        stepDuration = DEFAULT_STEP_DURATION;
    }

    @Override
    public void run() {
        try {
            while (true) {
                sleep(stepDuration);           
                synchronized (shipQueue) {
                    if (pier.isFree() && !shipQueue.isEmpty()) {
                        pier.acceptShip(shipQueue.take());
                    }
                }
                pier.proceed();
            }
        } catch (InterruptedException ex) {
            //Nothing to do here
        }
    }    
}

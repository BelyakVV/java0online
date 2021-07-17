package m6t4;

import static m6t4.MainForm.shipQueue;

/**
 * The working thread of a pier.
 * 
 * @author aabyodj
 */
public class Dockman extends Thread {

    /** A pier served by this Dockman */
    final PierPanel pier;
    
    /** Time needed to load or unload one item of cargo */
    private long stepDuration;

    /** Default time needed to load or unload one item of cargo */
    public final static long DEFAULT_STEP_DURATION = 500;

    /**
     * Create a working thread for a pier.
     * 
     * @param pier 
     */
    Dockman(PierPanel pier) {
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
                        continue;
                    }
                }
                pier.proceed();
            }
        } catch (InterruptedException ex) {
            //Nothing to do here
        }
    }    
}

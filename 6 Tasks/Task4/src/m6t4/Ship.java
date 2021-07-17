package m6t4;

import static m6t4.Dockman.DEFAULT_STEP_DURATION;
import static m6t4.MainForm.shipQueue;

/**
 *
 * @author aabyodj
 */
public class Ship {
    
    /** The name of the ship */
    public final String name;
    
    /** Capacity of the ship */
    public final int capacity;
    
    /** Current load of the ship */
    private int load;
    
    /** Maximum duration of sailing */
    static final double MAX_VOYAGE_DURATION = 30000;

    private Ship(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        randomLoad();
    }

    /** Enqueue the ships from SHIP_LIST to the port. */
    public static void startTraffic() {
        for (Ship ship: SHIP_LIST) {
            shipQueue.add(ship);
        }
    }
    
    private static final Ship[] SHIP_LIST = new Ship[]{
        new Ship("Адмирал Крузенштерн", 10),
        new Ship("Беда", 5),
        new Ship("Torrente", 50),
        new Ship("Crete I", 60),
        new Ship("Kobe", 80),
        new Ship("Fos Express", 15),
        new Ship("Leonidio", 70),
        new Ship("Крошечка", 90),
        new Ship("Гордость Сомали", 5),
        new Ship("Ambition", 20),
        new Ship("Ever Given", 100)
    };

    /** Load the ship with random amount of cargo. */
    private void randomLoad() {
        int half = capacity / 2;
        load = half + (int) (Math.random() * half);
    }
    
    /** 
     *  Depart to random voyage and reload with random amount of cargo, then 
     * return to the port. 
    */
    public void depart() {
        Ship me = this;
        new Thread() {
            @Override
            public void run() {
                try {
                    long voyageDuration = (long) (Math.random() * MAX_VOYAGE_DURATION);
                    long unloadingDuration = load * DEFAULT_STEP_DURATION;
                    randomLoad();
                    long loadingDuration = load * DEFAULT_STEP_DURATION;
                    sleep(voyageDuration + unloadingDuration + loadingDuration);
                    shipQueue.add(me);
                } catch (InterruptedException ex) {
                    //Nothing to do here
                }
            }
        }.start();
    }
    
    boolean isEmpty() {
        return 0 == load;
    }

    boolean isFull() {
        return capacity == load;
    }
    
    public int getCurrentLoad() {
        return load;
    }
    
    /**
     * Try to load one item of cargo.
     * @return true on success
     */
    public boolean loadOne() {
        if (capacity == load) return false;
        load++;
        return true;
    }
    
    /**
     * Try to unload one item of cargo.
     * @return true on success
     */
    public boolean unloadOne() {
        if (0 == load) return false;
        load--;
        return true;
    }
}

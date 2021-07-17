package m6t4;

import static m6t4.MainForm.shipQueue;

/**
 *
 * @author aabyodj
 */
public class Ship {
    public final String name;
    public final int capacity;
    private int load;
    static final double MAX_VOYAGE_DURATION = 200000;

    private Ship(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        load = (int) (Math.random() * (capacity + 1));
    }

    public static void startTraffic() {
        for (Ship ship: SHIP_LIST) {
            shipQueue.add(ship);
        }
    }
    
    public void sailAway() {
        Ship me = this;
        new Thread() {
            @Override
            public void run() {
                try {
                    long voyageDuration = (long) (Math.random() * MAX_VOYAGE_DURATION);
                    sleep(voyageDuration);
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
    
    public int getCurrentLoad() {
        return load;
    }
    
    public boolean loadOne() {
        if (capacity == load) return false;
        load++;
        return true;
    }
    
    public boolean unloadOne() {
        if (0 == load) return false;
        load--;
        return true;
    }
    
    private static final Ship[] SHIP_LIST = new Ship[]{
        new Ship("Адмирал Крузенштерн", 10),
        new Ship("Беда", 5),
        new Ship("Ever Given", 100),
        new Ship("Torrente", 27),
        new Ship("Crete I", 32),
        new Ship("Kobe", 58),
        new Ship("Fos Express", 14),
        new Ship("Leonidio", 49)
    };
}

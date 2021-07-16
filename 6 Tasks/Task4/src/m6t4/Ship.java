package m6t4;

/**
 *
 * @author aabyodj
 */
public class Ship {
    public final String name;
    public final int capacity;
    private int load = 0;

    public Ship(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
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
}

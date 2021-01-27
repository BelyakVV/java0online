package dragon;

import static dragon.Treasures.upperFirst;

/**
 * Сокровище. Родительский класс для драгоценных камней и рукотворных предметов.
 * @author aabyodj
 */
public abstract class Treasure implements Comparable {
    /**
     * Получить наименование сокровища
     * @return 
     */
    public abstract String getName();
    
    /**
     * Получить цену сокровища
     * @return 
     */
    public abstract long getPrice();
    
    @Override
    public String toString() {        
        return upperFirst(getName() + ". Цена: " + String.format("%,d", getPrice()));
    }

    @Override
    public int compareTo(Object o) {
        return Long.compare(getPrice(), ((Treasure) o).getPrice());
    }
}


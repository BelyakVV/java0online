package a3;

/**
 * Объект "Населённый пункт"
 * @author aabyodj
 */
class Locality extends Unit {  
    /**
     * Создать населённый пункт с заданными названием и площадью
     * @param name
     * @param area 
     */
    Locality(String name, double area) {
        super(name, area, null);
    }
}

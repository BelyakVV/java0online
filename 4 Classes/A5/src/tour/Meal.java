package tour;

/**
 * Планы питания
 * @author aabyodj
 */
enum Meal {
    RO("Без питания"), 
    BB("Только завтрак"), 
    HB("Завтрак и ужин"), 
    FB("Трёхразовое питание"), 
    AI("Всё включено");
    
    /** Текстовое наименование плана питания */
    public final String value;

    private Meal(String value) {
        this.value = value;
    }
}

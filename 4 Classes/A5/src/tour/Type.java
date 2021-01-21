package tour;

/**
 * Типы путёвок
 * @author aabyodj
 */
public enum Type {
    VACATION("Отдых"), 
    EXCURSION("Экскурсии"), 
    TREATMENT("Лечение"), 
    SHOPPING("Шоппинг"), 
    CRUISE("Круиз");
    
    /** Текстовое наименование типа путёвки */
    public final String value;

    private Type(String value) {
        this.value = value;
    }
}

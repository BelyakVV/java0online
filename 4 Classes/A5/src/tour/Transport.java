package tour;

/**
 * Виды транспорта
 * @author aabyodj
 */
enum Transport {
    NONE("Без транспорта"), 
    MINIBUS("Микроавтобус"), 
    BUS("Автобус"), 
    TRAIN("Поезд"), 
    PLANE("Самолёт"), 
    SHIP("Корабль");
    
    /** Текстовое наименование вида транспорта */
    public final String value;

    private Transport(String value) {
        this.value = value;
    }
}

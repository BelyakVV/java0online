package e4;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author aabyodj
 */
public final class Train {
    /** Список возможных пунктов назначения */
    public enum Station {
        BREST   {public String toString(){return "Брест";}},
        VITEBSK {public String toString(){return "Витебск";}},
        GOMEL   {public String toString(){return "Гомель";}},
        GRODNO  {public String toString(){return "Гродно";}},
        MINSK   {public String toString(){return "Минск";}},
        MOGILEV {public String toString(){return "Могилёв";}}
    }
    
    /** Номер поезда */
    public final int id;

    /** Пункт назначения */
    public final Station destination;
    
    /** 
     *   Время отправления, ofEpochSecond. Количество секунд, прошедшее после
     * 1970-01-01T00:00:00Z
     */
    public final long departure;

    /**
     *
     * @param id Номер поезда
     * @param destination Пункт назначения
     * @param departure Время отправления ofEpochSecond
     */
    public Train(int id, Station destination, long departure) {
        this.id = id;
        this.destination = destination;
        this.departure = departure;
    }

    @Override
    public String toString() {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(departure, 0, 
                OffsetDateTime.now().getOffset());
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT);
        return "№ " + id + ", пункт назначения: " + destination.toString() 
                + ", отправление: " +  dateTime.format(formatter);
    }
}

package e10;

import java.util.Scanner;

/**
 *   Создать класс Airline: пункт назначения, номер рейса, тип самолета, время 
 * вылета, дни недели. Определить конструкторы, set- и get- методы и метод 
 * toString(). 
 * @author aabyodj
 */
public class Airline {
    /** Номер рейса */
    private final int flight;
    /** flight пункта назначения в справочнике */    
    private int destination;
    /** flight типа самолёта в справочнике */
    private int plane;
    /** Время вылета: количество секунд после полуночи */
    private int departure;
    /** Дни вылета: битовое поле, где понедельник = 1, воскресенье = 64 */
    private int daysOfWeek;
    
    /**
     * Создание объекта из строки текста
     * @param line 
     */
    public Airline(String line) {
        Scanner in = new Scanner(line).useDelimiter("\\s*;\\s*");
        flight = in.nextInt(); //Нет номера рейса - нет объекта
        try { //Остальное можно задать потом
            destination = in.nextInt();
            plane = in.nextInt();
            departure = in.nextInt();
            daysOfWeek = in.nextInt();
        } catch (Exception e) {
            //По умолчанию нули
        }
    }

    /**
     * Получить номер рейса
     * @return 
     */
    public int getFlight() {
        return flight;
    }

    /** 
     * Получить id пункта назначения
     * @return 
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Установить id пункта назначения
     * @param destination 
     */
    public void setDestination(int destination) {
        this.destination = destination;
    }

    /**
     * Получить id типа самолёта
     * @return 
     */
    public int getPlane() {
        return plane;
    }

    /**
     * Установить id типа самолёта
     * @param plane 
     */
    public void setPlane(int plane) {
        this.plane = plane;
    }

    /**
     * Получить время вылета в виде количества секунд после полуночи
     * @return 
     */
    public int getDeparture() {
        return departure;
    }

    /**
     * Установить время вылета, заданное в виде количества секунд после полуночи
     * @param departure 
     */
    public void setDeparture(int departure) {
        this.departure = departure;
    }

    /**
     * Получить дни недели, по которым курсирует рейс, в виде битового поля, где
     * понедельник = 1, воскресенье = 64
     * @return 
     */
    public int getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * Установить дни недели, по которым курсирует рейс, в виде битового поля,
     * где понедельник = 1, воскресенье = 64
     * @param daysOfWeek 
     */
    public void setDaysOfWeek(int daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /** 
     * Форматирование для записи в файл
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(flight);
        result.append(';').append(destination);
        result.append(';').append(plane);
        result.append(';').append(departure);
        result.append(';').append(daysOfWeek);
        return result.toString();
    }
}

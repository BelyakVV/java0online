package e10;

import cli.CLI;
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
        flight = in.nextInt();
        try {
            destination = in.nextInt();
            plane = in.nextInt();
            departure = in.nextInt();
            daysOfWeek = in.nextInt();
        } catch (Exception e) {
            //По умолчанию нули
        }
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getPlane() {
        return plane;
    }

    public void setPlane(int plane) {
        this.plane = plane;
    }

    public int getDeparture() {
        return departure;
    }

    public void setDeparture(int departure) {
        this.departure = departure;
    }

    public int getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(int daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /** 
     * Вывод отформатирован в расчёте на дальнейшее форматирование в составе 
     * таблицы с использованием CLI.formatTable()
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(flight);
        result.append(CLI.V_SEPARATOR).append(destination);
        result.append(CLI.V_SEPARATOR).append(departure);
        result.append(CLI.V_SEPARATOR).append(daysOfWeek);
        result.append(CLI.V_SEPARATOR).append(plane);
        return result.toString();
    }
}

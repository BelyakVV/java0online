package oop3;

import java.time.LocalDate;
import oop3.Calendar.Holiday;

/**
 * 5. Basics of OOP. Задача 3.
 * Создать класс Календарь с внутренним классом, с помощью объектов которого 
 * можно хранить информацию о выходных и праздничных днях.
 * @author aabyodj
 */
public class OOP3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Calendar calendar = new Calendar();
        calendar.addAll(new Holiday[]{
            new Holiday(LocalDate.parse("2021-01-01"), "Новый год"),
            new Holiday(LocalDate.parse("2021-10-10"), "День работника культуры"),
            new Holiday(LocalDate.parse("2021-03-08"), "Международный женский день"),
            new Holiday(LocalDate.parse("2021-09-13"), "День программиста")
        });
        System.out.println(calendar);
    }
    
}

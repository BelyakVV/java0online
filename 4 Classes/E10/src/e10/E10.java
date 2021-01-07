package e10;

import cli.CLI;
import java.io.FileNotFoundException;

/**
 *   Простейшие классы и объекты. Задача 10.
 *   Создать класс Airline, спецификация которого приведена ниже. Определить 
 * конструкторы, set- и get- методы и метод toString(). Создать второй класс, 
 * агрегирующий массив типа Airline, с подходящими конструкторами и методами. 
 * Задать критерии выбора данных и вывести эти данные на консоль.
 *   Airline: пункт назначения, номер рейса, тип самолета, время вылета, дни 
 * недели.
 *   Найти и вывести:
 *   a) список рейсов для заданного пункта назначения;
 *   b) список рейсов для заданного дня недели;
 *   c) список рейсов для заданного дня недели, время вылета для которых больше 
 * заданного.
 * @author aabyodj
 */
public class E10 {
    
    /** Экземпляр класса, агрегирующего массив типа Airline */
    static Airlines airlines;
    
    /** Интерфейс командной строки */
    static CLI cli = new CLI(new CLI.Option[]{
        new CLI.Option("Вывести список рейсов для заданного пункта назначения", 
                E10::onlyDestination),
        new CLI.Option("Вывести список рейсов для заданного дня недели", 
                E10::onlyDay),
        new CLI.Option("Вывести список рейсов для заданного дня недели, "
                + "время вылета для которых больше заданного", 
                E10::onlyDayAfterTime),
        new CLI.Option("Выход", () -> System.exit(0))
    });

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        //Прочитать массив рейсов и справочники из файлов
        airlines = new Airlines("airlines.txt", 
                "destinations.txt", "planes.txt");
        //Запустить приложение
        cli.run();
    }
    
    /** Вывести список рейсов для заданного пункта назначения */
    static void onlyDestination() {
        //Справочник пунктов назначения
        var refDest = airlines.destinations;
        //Меню выбора пункта назначения
        var menu = CLI.buildMenu(refDest.toStrings());
        //id пункта назначения
        int destination = refDest.getId(cli.getChoice(menu));
        //Вывод рейсов до заданного пункта назначения
        System.out.println(airlines.onlyDestination(destination));
    }
    
    /** Вывести список рейсов для заданного дня недели */
    static void onlyDay() {
        //Меню выбора дня
        var menu = CLI.buildMenu(Airlines.Day.toStrings());
        //Битовое поле, где понедельник = 1, воскресенье = 64
        int day = 1 << cli.getChoice(menu);
        //Вывод рейсов, вылетающих в заданный день
        System.out.println(airlines.onlyDaysOfWeek(day));
    }
    
    /**
     * Вывести список рейсов для заданного дня недели, время вылета для которых 
     * больше заданного
     */
    static void onlyDayAfterTime() {
        //Меню выбора дня
        var menu = CLI.buildMenu(Airlines.Day.toStrings());
        //Битовое поле, где понедельник = 1, воскресенье = 64
        int day = 1 << cli.getChoice(menu);
        //Ввод времени
        int time = cli.getInt("Введите часы") * 60 * 60;
        time += cli.getInt("Введите минуты") * 60;
        //Вывод рейсов, вылетающих в заданный день после указанного времени
        System.out.println(airlines.onlyDaysOfWeek(day).afterTime(time));
    }
}

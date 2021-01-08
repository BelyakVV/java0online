/**
 *   Создайте класс Train, содержащий поля: название пункта назначения, номер 
 * поезда, время отправления. Создайте данные в массив из пяти элементов типа 
 * Train, добавьте возможность сортировки элементов массива по номерам поездов. 
 * Добавьте возможность вывода информации о поезде, номер которого введен 
 * пользователем. Добавьте возможность сортировки массив по пункту назначения, 
 * причем поезда с одинаковыми пунктами назначения должны быть упорядочены по 
 * времени отправления.
 */
package e4;

import java.time.Instant;
import java.util.Arrays;

/**
 *
 * @author aabyodj
 */
public class E4 {
    /** Массив поездов */
    static final Train[] trains = {
        new Train(10, Train.Station.MINSK, 
                Instant.parse("2020-12-31T11:05:00Z").getEpochSecond()),
        new Train(2, Train.Station.VITEBSK, 
                Instant.parse("2020-12-30T22:49:00Z").getEpochSecond()),
        new Train(9, Train.Station.GRODNO, 
                Instant.parse("2020-12-31T00:34:00Z").getEpochSecond()),
        new Train(5, Train.Station.GRODNO, 
                Instant.parse("2020-12-31T10:52:00Z").getEpochSecond()),
        new Train(1, Train.Station.MINSK, 
                Instant.parse("2020-12-30T19:12:00Z").getEpochSecond())
    };
    
    static final String NOTHING_FOUND = "Ничего не найдено.";
    
    /** Интерфейс командной строки */
    static CLI cli = new CLI();
    /** Главное меню */
    static CLI.Option[] menu = {
        new CLI.Option("Вывод информации о поезде по его номеру", () -> {
            printTrainById(cli.getInt("Введите номер поезда"));
        }),
        new CLI.Option("Отсортировать поезда по номерам", () -> {
            sortTrainsById();
        }),
        new CLI.Option("Отсортировать поезда по пункту назначения", () -> {
            sortTrainsByDestination();
        }),
        new CLI.Option ("Выход", () -> System.exit(0))
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        while (true) {
            cli.getChoice(menu);
            cli.waitForEnter();
            System.out.println();
        }
    }
    
    /**
     * Поиск поезда по номеру и печать информации о нём
     * @param id номер поезда
     */
    private static void printTrainById(int id) {
        for (var train: trains) {
            if (train.id == id) {
                System.out.println(train);
                return;
            }
        }
        System.out.println(NOTHING_FOUND); //Ничего не найдено
    }
    
    /** Вывод массива поездов */
    private static void printTrains() {
        for (var train: trains) {
            System.out.println(train);
        }
    }

    /** Сортировка массива поездов по id и печать отсортированного массива */
    private static void sortTrainsById() {
        Arrays.sort(trains, (Train t1, Train t2) -> t1.id - t2.id);
        printTrains();
    }
    
    /**
     * Сортировка поездов по пункту назначения, при совпадении - по времени
     * отправления. Затем вывод отсортированного массива.
     */
    private static void sortTrainsByDestination() {
        Arrays.sort(trains, (Train t1, Train t2) -> {
            if (t1.destination != t2.destination) {
                return t1.destination.compareTo(t2.destination);
            } else {
                return Long.compare(t1.departure, t2.departure);
            }
        });
        printTrains(); //Вывод массива
    }
    
}

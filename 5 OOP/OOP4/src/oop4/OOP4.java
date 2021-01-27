package oop4;

import cli.CLI;
import cli.CLI.Option;
import dragon.Treasures;
import java.io.FileNotFoundException;

/**
 * Дракон и его сокровища. Создать программу, позволяющую обрабатывать сведения 
 * о 100 сокровищах в пещере дракона. Реализовать возможность просмотра 
 * сокровищ, выбора самого дорогого по стоимости сокровища и выбора сокровищ на 
 * заданную сумму.
 * @author aabyodj
 */
public class OOP4 {
    /** Количество сокровищ */
    final static int TREASURES_COUNT = 100;
    /** Агрегатор массива сокровищ */
    static Treasures treasures;
    
    /** Интерфейс командной строки */
    static CLI cli = new CLI(new Option[]{
        new Option("Показать всё", OOP4::printAll),
        new Option("Показать самый дорогой предмет", OOP4::printMostExpensive),
        new Option("Выбрать на заданную сумму", OOP4::printSpecifiedTotal),
        new Option("Выход", ()-> System.exit(0))
    });

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        //Загрузить параметры генератора из файлов и сгенерировать сокровища
        treasures = new Treasures(TREASURES_COUNT, 
                "gems.txt", "materials.txt", "types.txt");
        //Запустить интерфейс командной строки
        cli.run();
    }
    
    /** Напечатать полный список сокровищ */
    static void printAll() {
        System.out.println(treasures);
    }
    
    /** Вывести самый дорогой предмет */
    static void printMostExpensive() {
        System.out.println(treasures.getMostExpensive());
    }
    
    /** Выбрать сокровища на заданную сумму */
    static void printSpecifiedTotal() {
        long total = cli.getLong("Введите сумму");
        System.out.println(treasures.selectTotal(total));
    }
}

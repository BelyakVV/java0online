package a3;

import cli.CLI;
import cli.CLI.Option;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 *   Создать объект класса Государство, используя классы Область, Район, Город. 
 * Методы: вывести на консоль столицу, количество областей, площадь, областные 
 * центры.
 * @author aabyodj
 */
public class A3 {
    /** Объект класса Государство */
    static State state;
    
    /** Интерфейс командной строки */
    static CLI cli = new CLI(new Option[]{
        new Option("Вывести столицу", A3::printCapital),
        new Option("Вывести количество областей", A3::printRegionsCount),
        new Option("Вывести площадь", A3::printArea),
        new Option("Вывести областные центры", A3::printRegionCapitals),
        //TODO: удалить дубликаты, когда центр района совпадает с центром области
//        new Option("Вывести все населённые пункты", A3::printAllLocalities),
        new Option("Выход", ()-> System.exit(0))
    });
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        state = State.load("belarus.txt");
        
        cli.run();
        // TODO code application logic here
    }
    
    /** Вывести на консоль столицу */
    static void printCapital() {
        System.out.println(state.getCapital().getName());
    }
    
    /** Вывести на консоль количество областей */
    static void printRegionsCount() {
        System.out.println(state.getRegions().length);
    }
    
    /** Вывести на консоль площадь */
    static void printArea() {
        System.out.println(state.getArea() + " кв. км");
    }
    
    /** Вывести на консоль областные центры */
    static void printRegionCapitals() {
        //Массив областей
        State.Region[] regions = state.getRegions();
        if (regions.length < 1) { //Области не найдены
            System.out.println("Ничего не найдено");
            return;
        }
        //Для каждой области вывести её облцентр
        for (var region: regions) {
            System.out.println(region.getCapital().getName());
        }
    }
    
    /** Вывести на консоль все населённые пункты */
    static void printAllLocalities() {
        Locality[] localities = state.getAllLocalities();
        if (localities.length < 1) {
            System.out.println("Ничего не найдено");
            return;
        }
        Arrays.sort(localities, (l1, l2) -> 
            l1.getName().compareToIgnoreCase(l2.getName()));
        for (var locality: localities) {
            System.out.println(locality.getName());
        }
    }
}

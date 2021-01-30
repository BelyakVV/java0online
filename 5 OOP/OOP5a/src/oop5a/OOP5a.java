package oop5a;

import bouquet.Bouquet;
import static bouquet.Bouquet.colorsInStock;
import static bouquet.Bouquet.flowersInStock;
import static bouquet.Bouquet.toStrings;
import static bouquet.Bouquet.wrappersInStock;
import cli.CLI;
import cli.CLI.Option;
import static cli.CLI.buildMenu;

/**
 *   Создать консольное приложение, удовлетворяющее следующим требованиям:
 *   * Корректно спроектируйте и реализуйте предметную область задачи. (WTF?)
 *   * Для создания объектов из иерархии классов продумайте возможность 
 * использования порождающих шаблонов проектирования. (В данной задаче это 
 * совершенно неэффективно, но надо, значит надо).
 *   * Реализуйте проверку данных, вводимых пользователем, но не на стороне 
 * клиента. (Предоставление пользователю для выбора закрытого списка подойдёт?)
 *   * Для проверки корректности переданных данных можно применить регулярные 
 * выражения. (Закрытый список, какие регулярные выражения? Спасибо, что это
 * необязательно).
 *   * Меню выбора действия пользователем можно не реализовывать, используйте 
 * заглушку. (Функционал меню уже давно реализован в процессе решения предыдущих 
 * задач.)
 *   * Особое условие: переопределите, где необходимо, методы toString(), 
 * equals() и hashCode().
 * 
 *   Вариант A. Цветочная композиция. Реализовать приложение, позволяющее 
 * создавать цветочные композиции (объект, представляющий собой цветочную 
 * композицию). Составляющими цветочной композиции являются цветы и упаковка.
 * 
 * @author aabyodj
 */
public class OOP5a {

    /** Цветочная композиция */
    static Bouquet bouquet = new Bouquet();
    
    /** Интерфейс командной строки */
    static CLI cli = new CLI(new Option[]{
        new Option("Добавить цветок", OOP5a::addFlower),
        new Option("Выбрать упаковку", OOP5a::setWrapper),
        new Option("Посмотреть результат", OOP5a::print),
        new Option("Выход", ()-> System.exit(0))
    });
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        cli.run();
    }
    
    /** Добавить цветок в композицию */
    static void addFlower() {
        Class[] flowers = flowersInStock();
        try {
            Option[] menu = buildMenu(toStrings(flowers));
            Class flower = flowers[cli.getChoice(menu)];
            menu = buildMenu(toStrings(colorsInStock(flower)));
            int color = cli.getChoice(menu);
            bouquet.add(flower, color);
        } catch (Exception e) {
            System.out.println("Ашыпка: " + e);
        }
    }
    
    /** Выбрать обёртку */
    static void setWrapper() {
        Class[] wrappers = wrappersInStock();
        try {
            Option[] menu = buildMenu(toStrings(wrappers));
            Class wrapper = wrappers[cli.getChoice(menu)];
            menu = buildMenu(toStrings(colorsInStock(wrapper)));
            int color = cli.getChoice(menu);
            bouquet.setWrapper(wrapper, color);
        } catch (Exception e) {
            System.out.println("Ашыпка: " + e);
        }
    }
    
    /** Показать результат */
    static void print() {
        System.out.println(bouquet);
    }
}

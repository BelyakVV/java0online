package a5;

import cli.CLI;
import cli.CLI.Option;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import tour.Tours;

/**
 *   Туристические путевки. Сформировать набор предложений клиенту по выбору 
 * туристической путевки различного типа (отдых, экскурсии, лечение, шопинг, 
 * круиз и т. д.) для оптимального выбора. Учитывать возможность выбора 
 * транспорта, питания и числа дней. Реализовать выбор и сортировку путевок.
 * @author aabyodj
 */
public class A5 {
    /** Агрегатор массива путёвок, пунктов назначения и фильтра */
    static Tours tours;
 
    /** Интерфейс командной строки */
    static CLI cli = new CLI(new Option[]{
        new Option("Выбрать пункт назначения", A5::selectDestination),
        new Option("Выбрать тип", A5::selectType),
        new Option("Выбрать вид транспорта", A5::selectTransport),
        new Option("Выбрать питание", A5::selectMeal),
        new Option("Выбрать длительность", A5::selectLength),
        new Option("Выбрать цену", A5::selectPrice),
        new Option("Искать по выбранным критериям", A5::printFiltered),
        new Option("Выход", ()-> System.exit(0))
    });
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        //Загрузка из файлов
        tours = new Tours("tours.txt", "countries.txt", "destinations.txt");
        //Запуск интерфейса
        cli.run(); 
    }
    
    /** Выбор пункта назначения */
    static void selectDestination() {
        //Список стран
        LinkedList<String> destinations = tours.filter.getCountries();
        //Размер списка стран
        int countries = destinations.size();
        //Дописать список пунктов назначения в конец списка стран
        destinations.addAll(tours.filter.getDestinations());
        //Пункт для отмены выбора
        destinations.add("Отменить");
        //Создать меню из списка
        var menu = CLI.buildMenu(destinations);
        //Получить выбор пользователя - номер элемента в списке
        int destination = cli.getChoice(menu);
        //Полученный выбор - страна или пункт в стране?
        if (destination >= countries) { //Не страна
            //Установить пункт назначения
            tours.filter.setDestination(destination - countries);
        } else { //Полученный выбор - страна
            //Установить страну
            tours.filter.setCountry(destination);
        }
    }
    
    /** Выбор типа путёвки: отдых, экскурсии, лечение, шопинг... */
    static void selectType() {
        //Список возможных вариантов
        LinkedList<String> types = tours.filter.getTypes();
        //Вариант для отмены
        types.add("Отменить");
        //Создать меню из списка
        var menu = CLI.buildMenu(types);
        //Получить выбор пользователя и передать его в фильтр
        tours.filter.setType(cli.getChoice(menu));
    }
    
    /** Выбор вида транспорта */
    static void selectTransport() {
        //Список возможных вариантов
        LinkedList<String> trTypes = tours.filter.getTrTypes();
        //Пункт для отмены
        trTypes.add("Отменить");
        //Создать меню из списка
        var menu = CLI.buildMenu(trTypes);
        //Получить выбор пользователя и передать его в фильтр
        tours.filter.setTransport(cli.getChoice(menu));
    }
    
    /** Выбор питания */
    static void selectMeal() {
        //Список возможных вариантов
        LinkedList<String> meals = tours.filter.getMeals();
        //Пункт для отмены
        meals.add("Отменить");
        //Создать меню из списка
        var menu = CLI.buildMenu(meals);
        //Получить выбор пользователя и передать его в фильтр
        tours.filter.setMeal(cli.getChoice(menu));
    }
    
    /** Выбор длительности */
    static void selectLength() {
        int minLength = cli.getInt("Минимальная длительность (0 для отмены)");
        int maxLength = cli.getInt("Максимальная длительность (0 для отмены)");
        //Передать выбор пользователя в фильтр
        tours.filter.setLengthBounds(minLength, maxLength);
    }
    
    /** Выбор ценового диапазона */
    static void selectPrice() {
        int minPrice = cli.getInt("Минимальная цена (0 для отмены)");
        int maxPrice = cli.getInt("Максимальная цена (0 для отмены)");
        //Передать выбор пользователя в фильтр
        tours.filter.setPriceBounds(minPrice * 100, maxPrice * 100);
    }
    
    /** Вывести отфильтрованный результат */
    static void printFiltered() {
        System.out.println(tours);
    }
}

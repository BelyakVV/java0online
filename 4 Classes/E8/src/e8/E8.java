package e8;

import java.io.FileNotFoundException;

/**
 *   Создать класс Customer, спецификация которого приведена ниже. Определить 
 * конструкторы, set- и get- методы и метод toString(). Создать второй класс, 
 * агрегирующий массив типа Customer, с подходящими конструкторами и методами. 
 * Задать критерии выбора данных и вывести эти данные на консоль. Класс 
 * Customer: id, фамилия, имя, отчество, адрес, номер кредитной карточки, номер 
 * банковского счета.
 *   Найти и вывести:
 *   a) список покупателей в алфавитном порядке;
 *   b) список покупателей, у которых номер кредитной карточки находится в 
 * заданном интервале
 * @author aabyodj
 */
public class E8 {
    /** Главное меню */
    private static final CLI.Option[] MAIN_MENU = {
        new CLI.Option("Вывести список покупателей в алфавитном порядке", 
                () -> alphabetical()),
        new CLI.Option("Вывести список покупателей, у которых номер кредитной "
                + "карточки находится в заданном интервале", 
                () -> rangeCards()),
        new CLI.Option("Выход", () -> System.exit(0))
    };
    
    /** Интерфейс командной строки */
    private static CLI cli;
    
    /** Требуемый класс */
    private static Customers customers;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Чтение списка покупателей из файла
        customers = Customers.read("src/customers.txt");
        //Создание и запуск интерфейса командной строки
        cli = new CLI(MAIN_MENU);
        cli.run();
    }

    /** Отсортировать покупателей в алфавитном порядке и вывести на экран. */
    private static void alphabetical() {
        System.out.println(customers.alphabetical());
    }

    /** 
     * Вывести список покупателей, у которых номер кредитной карточки находится 
     * заданном интервале.
     */
    private static void rangeCards() {
        long min = cli.getLong("Введите нижнюю границу интервала");
        long max = cli.getLong("Введите верхнюю границу интервала");
        Customers range = customers.cardRange(min, max);
        if (range != null) {
            System.out.println(range.alphabetical());
        } else {
            System.out.println("Ничего не найдено.");
        }
    }    
}

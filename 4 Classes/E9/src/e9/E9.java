package e9;

import cli.CLI;
import cli.CLI.Option;
import java.io.FileNotFoundException;

/**
 *   Создать класс Book, спецификация которого приведена ниже. Определить 
 * конструкторы, set- и get- методы и метод toString(). Создать второй класс, 
 * агрегирующий массив типа Book, с подходящими конструкторами и методами. 
 * Задать критерии выбора данных и вывести эти данные на консоль.
 *   Book: id, название, автор(ы), издательство, год издания, количество 
 * страниц, цена, тип переплета.
 *   Найти и вывести:
 *   a) список книг заданного автора;
 *   b) список книг, выпущенных заданным издательством;
 *   c) список книг, выпущенных после заданного года.
 * @author aabyodj
 */
public class E9 {
    /** Класс, агрегирующий массив типа Book */
    static Books books;
    
    /** Интерфейс командной строки */
    static CLI cli;
    
    /** Главное меню */
    static final Option[] mainMenu = {
        new Option("Вывести список книг заданного автора", 
                E9::onlyAuthor),
        new Option("Вывести список книг, выпущенных заданным издательством", 
                E9::onlyPublisher),
        new Option("Вывести список книг, выпущенных после заданного года", 
                E9::printedAfter),
        new Option("Изменить формат вывода",
                E9::changeFormat),
        new Option("Выход", () -> System.exit(0))
    };

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        books = new Books("src/books.txt", "src/authors.txt", 
                                "src/publishers.txt", "src/covers.txt");
        cli = new CLI(mainMenu);
        cli.run();
    }

    /** Вывести список книг заданного автора */
    private static void onlyAuthor() {
        //Справочник авторов
        var authRef = books.reference.authors;
        //Меню выбора автора
        Option[] authors = CLI.buildMenu(authRef.toStrings());
        //id выбранного автора
        int author = authRef.getId(cli.getChoice(authors));
        //Вывод книг этого автора
        System.out.println(books.onlyAuthor(author));
    }

    /** Вывести список книг, выпущенных заданным издательством */
    private static void onlyPublisher() {
        //Справочник издателей
        var publRef = books.reference.publishers;
        //Меню выбора издателя
        Option[] publishers = CLI.buildMenu(publRef.toStrings());
        //id выбранного издателя
        int publisher = publRef.getId(cli.getChoice(publishers));
        //Вывод книг этого издателя
        System.out.println(books.onlyPublisher(publisher));
    }

    /** Вывести список книг, выпущенных после заданного года */
    private static void printedAfter() {
        System.out.println(books.printedAfter(cli.getInt("Введите год")));
    }
   
    /** Меню выбора формата вывода данных */
    private static final Option[] FORMAT_MENU = {
        new Option("Таблица"),
        new Option("Библиографический список")
    };
    
    /** Изменить формат вывода данных */
    private static void changeFormat() {
        System.out.println("Текущий формат: " 
                + FORMAT_MENU[books.returnBibliographic ? 1 : 0].text);
        //Новый формат вывода
        books.returnBibliographic = cli.getChoice(FORMAT_MENU) == 1;
    }
}

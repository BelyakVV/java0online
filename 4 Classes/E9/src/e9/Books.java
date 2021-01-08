package e9;

import cli.CLI;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *   Класс, агрегирующий массив типа Book.
 *   Найти и вывести:
 *   a) список книг заданного автора;
 *   b) список книг, выпущенных заданным издательством;
 *   c) список книг, выпущенных после заданного года.
 * @author aabyodj
 */
public class Books {
    /** Массив книг */
    private final Book[] books;
    /** Справочник авторов, издательств и типов обложки */
    public final Book.Reference reference;
    /** Вывод в формате таблицы (false) или библиографического списка (true) */
    public boolean returnBibliographic = false;
    /** Максимальная ширина поля id среди книг в books */
    private final int maxIdWidth;
    
    private Books(Book[] books, Book.Reference reference, 
            boolean returnBibliographic, int maxIdWidth) {
        this.books = books;
        this.reference = reference;
        this.returnBibliographic = returnBibliographic;
        this.maxIdWidth = maxIdWidth;
    }
    
    /**
     * Чтение из файлов массива книг и справочников авторов, издательств и видов
     * обложки.
     * @param booksFileName Имя файла книг
     * @param authorsFileName Имя файла авторов
     * @param publishersFileName Имя файла издательств
     * @param coversFileName Имя файла видов обложки
     * @throws FileNotFoundException 
     */
    public Books(String booksFileName, 
                 String authorsFileName,
                 String publishersFileName,
                 String coversFileName) throws FileNotFoundException {
        //Сначала читаем справочники
        this.reference = new Book.Reference(authorsFileName, 
                                            publishersFileName, 
                                            coversFileName);
        //Затем - книги
        File file = new File(booksFileName);
        Scanner in = new Scanner(file);
        var result = new LinkedList<Book>();
        //Максимальная ширина поля id
        int maxIdWidth = 0; //Не используем поле, а вычисляем заново
        while (in.hasNextLine()) {
            //По строке на книгу
            Book book = Book.parse(in.nextLine(), reference);
            if (book != null) {
                result.add(book);
                int idWidth = (int)Math.log10(book.getId());
                if (idWidth > maxIdWidth) maxIdWidth = idWidth;
            }
        }
        this.books = result.toArray(new Book[0]);
        this.maxIdWidth = maxIdWidth;
    }
    
    @Override
    public String toString() {
        if (books.length == 0) return "Ничего не найдено.";
        if (returnBibliographic) {
            return asBibliographic(); //Возврат в виде библиографического списка      
        } else {
            return asTable(); //Возврат в виде таблицы
        }
    }
    
    /**
     * Поиск книг заданного автора и возврат в новом экземпляре агрегатора
     * @param author id автора
     * @return 
     */
    public Books onlyAuthor(int author) {
        var result = new LinkedList<Book>();
        //Максимальная ширина поля id
        int maxIdWidth = 0; //Не используем поле, а вычисляем заново
        booksLoop:
        for (var book: books) {
            for (var bookAuthor: book.getAuthors()) {
                if (bookAuthor == author) {
                    result.add(book);
                    int idWidth = (int)Math.log10(book.getId());
                    if (idWidth > maxIdWidth) maxIdWidth = idWidth;
                    continue booksLoop;
                }
            }
        }
        return new Books(result.toArray(new Book[0]), reference, 
                returnBibliographic, maxIdWidth);
    }
    
    /**
     * Поиск книг заданного издательства и возврат в новом экземпляре агрегатора
     * @param publisher id издательства
     * @return 
     */
    public Books onlyPublisher(int publisher) {
        var result = new LinkedList<Book>();
        //Максимальная ширина поля id
        int maxIdWidth = 0; //Не используем поле, а вычисляем заново
        for (var book: books) {
            if (book.getPublisher() == publisher) {
                result.add(book);
                int idWidth = (int)Math.log10(book.getId());
                if (idWidth > maxIdWidth) maxIdWidth = idWidth;
            }
        }
        return new Books(result.toArray(new Book[0]), reference,
                returnBibliographic, maxIdWidth);
    }
    
    /**
     * Поиск книг, выпущенных после заданного года и возврат в новом экземпляре
     * агрегатора
     * @param year Год
     * @return 
     */
    public Books printedAfter(int year) {
        var result = new LinkedList<Book>();
        //Максимальная ширина поля id
        int maxIdWidth = 0; //Не используем поле, а вычисляем заново
        for (var book: books) {
            if (book.getYear() > year) {
                result.add(book);
                int idWidth = (int)Math.log10(book.getId());
                if (idWidth > maxIdWidth) maxIdWidth = idWidth;
            }
        }
        return new Books(result.toArray(new Book[0]), reference, 
                returnBibliographic, maxIdWidth);
    }
    
    /**
     * Возврат массива книг в виде библиографического списка
     * @return 
     */
    private String asBibliographic() {
        String result = "";
        for (var book: books) {
            result += book + "\n";
        }        
        return result.stripTrailing();
    }

    //<editor-fold defaultstate="collapsed" desc="Константы для таблицы">
    //Заголовки таблицы
    private static final String ID_TXT = "id";
    private static final String TITLE_TXT = "Название";
    private static final String AUTHORS_TXT = "Авторы";
    private static final String PUBLISHER_TXT = "Издательство";
    private static final String YEAR_TXT = "Год";
    private static final String PAGES_TXT = "стр";
    private static final String PRICE_TXT = "Цена";
    private static final String COVER_TXT = "Обложка";    
    //Количество вертикальных разделителей
    private static final int DELIMITERS_COUNT = 7; 
    //Неизменяемые ширины полей
    private static final int YEAR_WIDTH = 4; //Год
    private static final int PAGES_WIDTH = 5; //Количество страниц
    private static final int PRICE_WIDTH = 10; //Цена
    private static final int COVER_WIDTH = COVER_TXT.length(); //Обложка
    //</editor-fold>
    
    /**
     * Возврат массива книг в виде таблицы
     * @return 
     */
    private String asTable() {
        //<editor-fold defaultstate="collapsed" desc="Вычисление ширин полей">
        final int screenWidth = CLI.getScreenWidth();
        final int idWidth = Math.max(maxIdWidth, ID_TXT.length());
        final int constWidth = idWidth + YEAR_WIDTH + PAGES_WIDTH + PRICE_WIDTH
                + COVER_WIDTH + DELIMITERS_COUNT;
        final int varWidth = screenWidth - constWidth;
        final int titleWidth = varWidth * 2 / 5;
        final int authorsWidth = (varWidth - titleWidth) * 3 / 5;
        final int publisherWidth = varWidth - titleWidth - authorsWidth;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Шапка таблицы">
        final String hr1 = CLI.repeatChar(CLI.H_SEPARATOR, screenWidth);
        final String hr2 = CLI.repeatChar(CLI.H_SEPARATOR, idWidth)
                + CLI.CR_SEPARATOR
                + CLI.repeatChar(CLI.H_SEPARATOR, titleWidth)
                + CLI.CR_SEPARATOR
                + CLI.repeatChar(CLI.H_SEPARATOR, authorsWidth)
                + CLI.CR_SEPARATOR
                + CLI.repeatChar(CLI.H_SEPARATOR, publisherWidth)
                + CLI.CR_SEPARATOR
                + CLI.repeatChar(CLI.H_SEPARATOR, YEAR_WIDTH)
                + CLI.CR_SEPARATOR
                + CLI.repeatChar(CLI.H_SEPARATOR, PAGES_WIDTH)
                + CLI.CR_SEPARATOR
                + CLI.repeatChar(CLI.H_SEPARATOR, PRICE_WIDTH)
                + CLI.CR_SEPARATOR
                + CLI.repeatChar(CLI.H_SEPARATOR, COVER_WIDTH);
        String head = hr1 + "\n"
                + CLI.alignCenter(ID_TXT, idWidth) + CLI.V_SEPARATOR
                + CLI.alignCenter(TITLE_TXT, titleWidth) + CLI.V_SEPARATOR
                + CLI.alignCenter(AUTHORS_TXT, authorsWidth) + CLI.V_SEPARATOR
                + CLI.alignCenter(PUBLISHER_TXT, publisherWidth) + CLI.V_SEPARATOR
                + CLI.alignCenter(YEAR_TXT, YEAR_WIDTH) + CLI.V_SEPARATOR
                + CLI.alignCenter(PAGES_TXT, PAGES_WIDTH) + CLI.V_SEPARATOR
                + CLI.alignCenter(PRICE_TXT, PRICE_WIDTH) + CLI.V_SEPARATOR
                + CLI.alignCenter(COVER_TXT, COVER_WIDTH) + "\n" + hr2 + "\n";
        //</editor-fold> 
        String result = head;       
        for (var book: books) {
            //Поле id
            result += CLI.alignRight(Integer.toString(book.getId()), idWidth) 
                    + CLI.V_SEPARATOR;
            //Название
            result += CLI.alignLeft(book.getTitle(), titleWidth) 
                    + CLI.V_SEPARATOR;
            //Авторы
            result += CLI.alignLeft(concatAuthors(book.getAuthors()), authorsWidth) 
                    + CLI.V_SEPARATOR;
            //Издательство
            result += CLI.alignLeft(reference.publishers.idToName(book.getPublisher()), publisherWidth)
                    + CLI.V_SEPARATOR;
            //Год издания
            result += CLI.alignRight(Integer.toString(book.getYear()), YEAR_WIDTH)
                    + CLI.V_SEPARATOR;
            //Количество страниц
            result += CLI.alignRight(Integer.toString(book.getPages()), PAGES_WIDTH)
                    + CLI.V_SEPARATOR;
            //Цена
            result += formatPrice(book.getPrice()) + CLI.V_SEPARATOR;
            //Тип обложки
            result += CLI.alignLeft(reference.covers.idToName(book.getCover()), COVER_WIDTH)
                + "\n";
        }
        return result + hr1;
    }

    /**
     * Соединение нескольких авторов водну строку
     * @param authors Массив id авторов
     * @return 
     */
    private String concatAuthors(int[] authors) {
        String result = "";
        for (int i = 0; i < authors.length - 1; i++) {
            result += reference.authors.idToName(authors[i]) + ", ";
        }
        result += reference.authors.idToName(authors[authors.length - 1]);
        return result;
    }

    /**
     * Преобразование цены в копейках в цену в рублях и копейках. Для нулевой
     * либо отрицательной цены возврат строки "неизвестно".
     * @param price Цена в копейках
     * @return Цена в рублях и копейках либо строка "неизвестно"
     */
    private String formatPrice(int price) {
        if (price < 1) return CLI.alignCenter("неизвестно", PRICE_WIDTH);
        return CLI.alignRight((price / 100) + " р " 
                + String.format("%02d", price % 100) + " к", PRICE_WIDTH);
    }
}

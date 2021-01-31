package task1;

import cli.CLI;
import cli.CLI.Option;
import static cli.CLI.buildMenu;
import homelib.Book;
import homelib.Book.Author;
import homelib.Library;
import java.util.List;

/**
 *
 * @author aabyodj
 */
public class Task1 {
    
    static Library library = new Library("books.txt", "authors.txt");
    
    static CLI cli = new CLI(new Option[]{
        new Option("Показать все книги", Task1::printAll),
        new Option("Изменить информацию о книге", Task1::modifyBook),
        new Option("Добавить книгу", Task1::addBook),
        new Option("Удалить книгу", Task1::removeBook),
        new Option("Показать список авторов", Task1::printAuthors),
        new Option("Изменить имя автора", Task1::editAuthor),
        new Option("Добавить автора", Task1::addAuthor),
        new Option("Выход", Task1::exit)
    });

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        cli.run();
    }
    
    static void printAll() {
        System.out.println(library);        
    }
    
    static void modifyBook() {
        int id = cli.getInt("Введите id книги");
        try {
            Book book = library.getBook(id);
            System.out.println("Название: " + book.getTitle());
            String title = cli.getString("Введите новое название (пустая строка - оставить без изменений)");
            boolean changed = false;
            if (!title.isBlank()) {
                book.setTitle(title);
                changed = true;
            }
            List<String> authors = library.getAuthorsList();
            authors.add("Оставить без изменений");
            System.out.println("Авторы: " + book.authorsToString());
            int authIndex = cli.getChoice(buildMenu(authors));
            if (authIndex != authors.size() - 1) {
                book.setAuthors(library.authorsByIndexes(new int[]{authIndex}));
                changed = true;
            }
            if (changed) {
                System.out.println("Информация о книге успешно изменена.");
            } else {
                System.out.println("Информация осталась без изменений.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
    
    static void addBook() {
        Option[] menu = buildMenu(Book.Type.getFullNames());
        Book.Type type = Book.Type.values()[cli.getChoice(menu)];
        String title = cli.getString("Введите название");
        if (title.isBlank()) {
            System.out.println("Ошибка: пустое название");
            return;
        }
        List<String> authors = library.getAuthorsList();
        authors.add("Добавить автора");
        int authIndex = cli.getChoice(buildMenu(authors));
        try {
            if (authIndex == authors.size() - 1) {
                Author author = addAuthor();
                if (author == null) return;
                library.addBook(type, title, new Author[]{author});
            } else {
                library.addBook(type, title, new int[]{authIndex});
            }
            System.out.println("Книга успешно добавлена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
    
    static void removeBook() {
        int id = cli.getInt("Введите id книги");
        try {
            library.removeBook(id);
            System.out.println("Книга успешно удалена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
    
    static void printAuthors() {
        System.out.println(library.getAuthorsTable());
    }
    
    static void editAuthor() {
        int id = cli.getInt("Введите id автора");
        try {
            Author author = library.getAuthor(id);
            System.out.println(author.getName());
            String name = cli.getString("Введите новое имя (пустая строка - оставить без изменений)");
            if (name.isBlank()) return;
            author.setName(name);
            System.out.println("Имя автора успешно изменено.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
    
    /**
     * Добавить автора в справочник
     * @return id нового автора или Author.INVALID_ID в случае ошибки
     */
    static Author addAuthor() {
        String name = cli.getString("Введите полное имя");
        try {
            Author author =  library.addAuthor(name);
            System.out.println("Автор успешно добавлен.");
            return author;
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            return null;
        }
    }
    
    static void exit() {
        System.exit(0);
    }
}

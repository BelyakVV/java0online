package task1;

import cli.CLI;
import cli.CLI.Option;
import static cli.CLI.buildMenu;
import homelib.Book;
import homelib.Book.Author;
import homelib.Library;
import homelib.User;
import homelib.Users;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aabyodj
 */
public class Task1 {
    
    static Library library = new Library("books.txt", "authors.txt");
    
    static Users users;
    
    static CLI cli = new CLI(new Option[]{
        new Option("Показать все книги", Task1::printAll),
        new Option("Найти книгу", Task1::searchBook),
        
        new Option("Изменить информацию о книге", Task1::modifyBook),
        new Option("Добавить книгу", Task1::addBook),
        new Option("Удалить книгу", Task1::removeBook),
        
        new Option("Показать список авторов", Task1::printAuthors),
        new Option("Изменить имя автора", Task1::editAuthor),
        new Option("Добавить автора", Task1::addAuthor),
        
        new Option("Показать список пользователей", Task1::printUsers),
        new Option("Редактировать учётную запись пользователя", Task1::editUser),
        new Option("Добавить пользователя", Task1::addUser),
        new Option("Удалить пользователя", Task1::removeUser),
        
        new Option("Выход", Task1::exit)
    });

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            users = new Users("users.txt");
        } catch (FileNotFoundException ex) {
            users = new Users();
        }
        if (users.size() < 1) {
            System.out.println("Не удалось загрузить список пользователей. Создаём учётную запись администратора...");
            if (!addUser()) System.exit(1);
            User user = users.get(0);
            user.setAdmin(true);
            users.login(user);
        } else if (!users.login(cli.getString("Введите логин"), cli.getPass("Введите пароль"))) {
            System.out.println("Неверный пароль");
            System.exit(1);
        }
        cli.run();
    }
    
    static void printAll() {
        System.out.println(library);        
    }
    
    static void searchBook() {
        String title = cli.getString("Название содержит (пустая строка - игнорировать название)");
        Library search = library.onlyTitle(title);
        List<String> authors = library.getAuthorsList();
        authors.add("Любой автор");
        int authIndex = cli.getChoice(buildMenu(authors));
        if (authIndex < authors.size() - 1)
            search = search.onlyAuthor(library.authorsByIndexes(new int[]{authIndex})[0]);
        System.out.println(search);
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
    
    static void printUsers() {
        System.out.println(users);
    }
    
    static final Option[] ADMIN_OR_USER = new Option[]{
        new Option("Администратор"),
        new Option("Пользователь")
    };
    
    static boolean addUser() {
        String name = cli.getString("Введите имя нового пользователя").strip();
        if (name.isBlank()) {
            System.out.println("Ошибка: " + User.EMPTY_NAME);
            return false;
        }
        if (!users.nameIsFree(name)) {
            System.out.println("Ошибка: такое имя пользователя уже есть");
            return false;
        }
        String pass = cli.getPass("Введите пароль нового пользователя");
        if (pass.isEmpty()) {
            System.out.println("Ошибка: пароль не может быть пустым");
            return false;
        }
        if (!pass.contentEquals(cli.getPass("Повторите пароль"))) {
            System.out.println("Ошибка: введённые пароли не совпадают");
            return false;
        }
//        boolean admin = cli.getChoice(ADMIN_OR_USER) == 0;
        try {
            boolean result =  users.add(name, pass);
            if (result) System.out.println("Пользователь успешно добавлен.");
            return result;
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            return false;
        }
    }
    
    static void editUser() {
        var usersList = buildMenu(users.getList());
        User user = users.get(cli.getChoice(usersList));
        boolean changed = false;
        String name = cli.getString("Введите новое имя (пустая строка - оставить без изменений)");        
        if (!name.isBlank() && !name.contentEquals(user.getName())) {
            if (users.nameIsFree(name)) {
                user.setName(name);
                changed = true;
            } else {
                System.out.println("Ошибка: такое имя пользователя уже есть");
            }
        }
        String pass = cli.getPass("Введите новый пароль (пустая строка - оставить без изменений)");
        if (!pass.isEmpty()) {
            if (pass.contentEquals(cli.getPass("Повторите пароль"))) {
                try {
                    user.setPassword(pass);
                    changed = true;
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            } else {
                System.out.println("Ошибка: введённые пароли не совпадают");
            }
        }
        if (users.canDelete(user)) {
            boolean admin = cli.getChoice(ADMIN_OR_USER) == 0;
            if (admin != user.isAdmin()) {
                user.setAdmin(admin);
                changed = true;
            }
        }
        if (changed) {
            System.out.println("Учётная запись пользователя " + user.getName() + " успешна изменена.");
        } else {
            System.out.println("Учётная запись пользователя " + user.getName() + " осталась без изменений.");
        }
    }
    
    static void removeUser() {
        var usersList = buildMenu(users.getList());        
        try {
            users.remove(cli.getChoice(usersList));
            System.out.println("Учётная запись пользователя успешно удалена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
    
    static void exit() {
        System.exit(0);
    }
}

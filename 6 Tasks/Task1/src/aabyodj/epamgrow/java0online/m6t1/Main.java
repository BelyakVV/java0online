package aabyodj.epamgrow.java0online.m6t1;

import aabyodj.console.CommandLineInterface;
import aabyodj.console.CommandLineInterface.Option;
import static aabyodj.console.CommandLineInterface.buildMenu;
import static aabyodj.epamgrow.java0online.m6t1.Util.sendMail;
import aabyodj.epamgrow.java0online.m6t1.library.Book;
import aabyodj.epamgrow.java0online.m6t1.library.Book.Author;
import aabyodj.epamgrow.java0online.m6t1.library.Library;
import aabyodj.epamgrow.java0online.m6t1.security.User;
import aabyodj.epamgrow.java0online.m6t1.security.UserManager;
import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.List;
import static aabyodj.console.CommandLineInterface.printErrorMsg;
import static aabyodj.console.Const.BR;
import static aabyodj.datafiles.Const.FILE_SEPARATOR;
import aabyodj.epamgrow.java0online.m6t1.library.Book.Type;

/**
 *
 * @author aabyodj
 */
public class Main {
    static final String PATH = "data" + FILE_SEPARATOR;
    
    static Library books = new Library(PATH + "books.csv", PATH + "authors.csv");
    
    static UserManager users = new UserManager(PATH + "users.csv");
    
    static CommandLineInterface cli = new CommandLineInterface();
    
    static final Option[] ADMIN_MENU = new Option[]{
        new Option("Показать все книги", Main::printAll),
        new Option("Найти книгу", Main::searchBook),        
        new Option("Управление книгами", new Option[]{
            new Option("Изменить информацию о книге", Main::modifyBook),
            new Option("Добавить книгу", Main::addBook),
            new Option("Удалить книгу", Main::removeBook)
        }),
        new Option("Управление списком авторов", new Option[]{
            new Option("Показать список авторов", Main::printAuthors),
            new Option("Изменить имя автора", Main::editAuthor),
            new Option("Добавить автора", Main::addAuthor),
        }),
        new Option("Управление списком пользователей", new Option[]{
            new Option("Показать список пользователей", Main::printUsers),
            new Option("Редактировать учётную запись пользователя", Main::editUser),
            new Option("Добавить пользователя", Main::addUser),
            new Option("Удалить пользователя", Main::removeUser),
        }),        
        new Option("Выход", Main::exit)
    };
    
    static final Option[] USER_MENU = new Option[]{
        new Option("Показать все книги", Main::printAll),
        new Option("Найти книгу", Main::searchBook),
        new Option("Предложить книгу", Main::suggestBook),
        new Option("Выход", Main::exit)
    };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (users.size() < 1) {
            printErrorMsg("Не удалось загрузить список пользователей. Создаём учётную запись администратора...");
            if (!addUser()) {
                System.exit(1);
            }
            User user = users.get(0);
            user.setAdmin(true);
            users.login(user);
        } else if (!users.login(cli.readLine("Введите логин"), 
                                cli.readPassword("Введите пароль"))) {
            printErrorMsg("Неверный логин или пароль");
            System.exit(1);
        }
        if (users.getLogged().isAdmin()) {
            cli.setMenu(ADMIN_MENU);
        } else {
            cli.setMenu(USER_MENU);
        }
        cli.run();
    }
    
    static void printAll() {
        cli.println(books.toString());        
    }
    
    static void searchBook() {
        String title = cli.readLine("Название содержит (пустая строка - игнорировать название)");
        Library result = books.selectTitle(title);
        List<String> authors = books.getAuthorsList();
        authors.add("Любой автор");
        int authIndex = cli.getChoice(buildMenu(authors));
        if (authIndex < authors.size() - 1) {
            Author author = books.getAuthorByIndex(authIndex);
            result = result.selectAuthor(author);
        }
        cli.println(result.toString());
    }
    
    static void suggestBook() {
        Option[] typeMenu = buildMenu(Book.Type.getFullNames());
        Book.Type type = Book.Type.values()[cli.getChoice(typeMenu)];
        String title = cli.readLine("Введите название");
        if (title.isBlank()) {
            printErrorMsg("Пустое название");
            return;
        }
        List<String> authors = books.getAuthorsList();
        authors.add("Добавить автора");
        int authIndex = cli.getChoice(buildMenu(authors));
        String authorName;
        if (authIndex == authors.size() - 1) {
            authorName = cli.readLine("Введите полное имя автора");
            if (authorName.isBlank()) {
                printErrorMsg("Имя автора не может быть пустым");
                return;
            }
        } else {
            authorName = authors.get(authIndex);
        }
        sendSuggest(users.getLogged().getName(), type, authorName, title, 
                    users.getAdminsEmails());
    }
    
    static void modifyBook() {
        int id = cli.getInt("Введите id книги");
        try {
            Book book = books.getBook(id);
            cli.println("Название: " + book.getTitle());
            String title = cli.getString("Введите новое название (пустая строка - оставить без изменений)");
            boolean changed = false;
            if (!title.isBlank()) {
                book.setTitle(title);
                changed = true;
            }
            List<String> authors = books.getAuthorsList();
            authors.add("Оставить без изменений");
            cli.println("Авторы: " + book.authorsToString());
            int authIndex = cli.getChoice(buildMenu(authors));
            if (authIndex != authors.size() - 1) {
                book.setAuthors(books.authorsByIndexes(new int[]{authIndex}));
                changed = true;
            }
            if (changed) {
                cli.println("Информация о книге успешно изменена.");
            } else {
                cli.println("Информация осталась без изменений.");
            }
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
        }
    }
    
    static void addBook() {
        Option[] menu = buildMenu(Book.Type.getFullNames());
        Book.Type type = Book.Type.values()[cli.getChoice(menu)];
        String title = cli.getString("Введите название");
        if (title.isBlank()) {
            cli.println("Ошибка: пустое название");
            return;
        }
        List<String> authors = books.getAuthorsList();
        Author author;
        authors.add("Добавить автора");
        int authIndex = cli.getChoice(buildMenu(authors));
        try {
            if (authIndex == authors.size() - 1) {
                author = addAuthor();
                if (author == null) return;
                books.addBook(type, title, new Author[]{author});
            } else {
                author = books.getAuthorByIndex(authIndex);
                books.addBook(type, title, new Author[]{author});
            }
            cli.println("Книга успешно добавлена.");
            List<Address> emails = users.getAllEmailsExcept(users.getLogged());
            StringBuilder emailText = new StringBuilder("Администратор ");
            emailText.append(users.getLogged().getName());
            emailText.append(" добавил книгу");
            String emailSubject = emailText.toString();
            String br = System.lineSeparator();
            emailText.append('.').append(br);
            emailText.append("Тип: ").append(type.fullName).append(br);            
            emailText.append("Автор: ").append(author.getName()).append(br);            
            emailText.append("Название: ").append(title);
            sendMail(emails, emailSubject, emailText.toString());
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
        }
    }
    
    static void removeBook() {
        int id = cli.getInt("Введите id книги");
        try {
            books.removeBook(id);
            cli.println("Книга успешно удалена.");
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
        }
    }
    
    static void printAuthors() {
        cli.println(books.getAuthorsTable());
    }
    
    static void editAuthor() {
        int id = cli.getInt("Введите id автора");
        try {
            Author author = books.getAuthor(id);
            cli.println(author.getName());
            String name = cli.getString("Введите новое имя (пустая строка - оставить без изменений)");
            if (name.isBlank()) return;
            author.setName(name);
            cli.println("Имя автора успешно изменено.");
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
        }
    }
    
    /**
     * Добавить автора в справочник
     * @return id нового автора или Author.INVALID_ID в случае ошибки
     */
    static Author addAuthor() {
        String name = cli.getString("Введите полное имя");
        try {
            Author author =  books.addAuthor(name);
            cli.println("Автор успешно добавлен.");
            return author;
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
            return null;
        }
    }
    
    static void printUsers() {
        cli.println(users.toString());
    }
    
    static final Option[] ADMIN_OR_USER = new Option[]{
        new Option("Администратор"),
        new Option("Пользователь")
    };
    
    static boolean addUser() {
        String name = cli.getString("Введите имя нового пользователя").strip();
        if (name.isBlank()) {
            cli.println("Ошибка: " + User.EMPTY_NAME);
            return false;
        }
        if (!users.nameIsFree(name)) {
            cli.println("Ошибка: такое имя пользователя уже есть");
            return false;
        }
        char[] pass = cli.getPass("Введите пароль нового пользователя");
        if (pass.length < 1) {
            cli.println("Ошибка: пароль не может быть пустым");
            return false;
        }
        if (!Arrays.equals(pass, cli.getPass("Повторите пароль"))) {
            cli.println("Ошибка: введённые пароли не совпадают");
            return false;
        }
        Address email;
        try {
            email = new InternetAddress(cli.readLine("Введите email"));
        } catch (AddressException ex) {
            printErrorMsg("неверный формат.");
            email = null;
        }
//        boolean admin = cli.getChoice(ADMIN_OR_USER) == 0;
        try {
            boolean result =  users.add(name, pass, email);
            if (result) cli.println("Пользователь успешно добавлен.");
            return result;
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
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
                cli.println("Ошибка: такое имя пользователя уже есть");
            }
        }
        char[] pass = cli.getPass("Введите новый пароль (пустая строка - оставить без изменений)");
        if (pass.length > 0) {
            if (Arrays.equals(pass, cli.getPass("Повторите пароль"))) {
                try {
                    user.setPassword(pass);
                    changed = true;
                } catch (Exception e) {
                    cli.println("Ошибка: " + e.getMessage());
                }
            } else {
                cli.println("Ошибка: введённые пароли не совпадают");
            }
        }
        String email = cli.readLine("Введите новый email (пустая строка - оставить без изменений)");
        if (!email.isBlank()) {
            try {
                user.setEmail(new InternetAddress(email));
                changed = true;
            } catch (AddressException e) {
                printErrorMsg("Неверный формат");
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
            cli.println("Учётная запись пользователя " + user.getName() + " успешна изменена.");
        } else {
            cli.println("Учётная запись пользователя " + user.getName() + " осталась без изменений.");
        }
    }
    
    static void removeUser() {
        var usersList = buildMenu(users.getList());        
        try {
            users.remove(cli.getChoice(usersList));
            cli.println("Учётная запись пользователя успешно удалена.");
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
        }
    }
    
    static void exit() {
        try {
            books.saveAuthors();            
        } catch (Exception e) {
            printErrorMsg(e.getMessage());
        }
        try {
            books.saveBooks();            
        } catch (Exception e) {
            printErrorMsg(e.getMessage());
        }
        try {
            users.save();            
        } catch (Exception e) {
            printErrorMsg(e.getMessage());
        }
        System.exit(0);
    }
    
    static void sendSuggest(String userName, Type type, String authorName, 
                            String title, List<Address> adminsEmails) {
        StringBuilder emailText = new StringBuilder("Пользователь ");
        emailText.append(userName).append(" предлагает добавить книгу");
        String emailSubject = emailText.toString();
        emailText.append('.').append(BR);
        emailText.append("Тип: ").append(type.fullName).append(BR);
        emailText.append("Автор: ").append(authorName).append(BR); 
        emailText.append("Название: ").append(title);
        sendMail(adminsEmails, emailSubject, emailText.toString());
    }
}

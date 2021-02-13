/*
    6. Tasks
    Задание 1: создать консольное приложение “Учет книг в домашней библиотеке”
    Общие требования к заданию:
    * Система учитывает книги как в электронном, так и в бумажном варианте.
    * Существующие роли: пользователь, администратор.
    * Пользователь может просматривать книги в каталоге книг, осуществлять 
поиск книг в каталоге.
    * Администратор может модифицировать каталог.
    * При добавлении описания книги в каталог оповещение о ней рассылается на
e-mail всем пользователям.
    * При просмотре каталога желательно реализовать постраничный просмотр
    * Пользователь может предложить добавить книгу в библиотеку, переслав её
администратору на e-mail.
    * Каталог книг хранится в текстовом файле.
    * Данные аутентификации пользователей хранятся в текстовом файле. Пароль
не хранится в открытом виде
*/

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

/**
 * Запускаемый класс
 * @author aabyodj
 */
public class Main {
//TODO: вынести функционал администратора и пользователя в отдельные классы
    
    /** Путь к файлам данных */
    static final String PATH = "data" + FILE_SEPARATOR;
    
    /** Агрегатор списков книг и авторов */
    static Library books = new Library(PATH + "books.csv", PATH + "authors.csv");
    
    /** Агрегатор списка пользователей и администраторов */
    static UserManager users = new UserManager(PATH + "users.csv");
    
    /** Интерфейс командной строки */
    static CommandLineInterface cli = new CommandLineInterface();
    
    /** Меню администратора */
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
    
    /** Меню пользователя */
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
            User firstAdmin = addUser();
            if (firstAdmin == null) {
                System.exit(1);
            }
            firstAdmin.setAdmin(true);
            users.login(firstAdmin);
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
    
    /**
     * Вывести все книги из каталога
     */
    static void printAll() {
        cli.printByPages(books.toString());        
    }
    
    /**
     * Поиск книг в каталоге
     */
    static void searchBook() {
        String title = cli.readLine("Название содержит (пустая строка - игнорировать название)");
        Library result = books.selectByTitle(title);
        List<String> authors = books.getAuthorsList();
        authors.add("Любой автор");
        int authIndex = cli.getChoice(buildMenu(authors));
        if (authIndex < authors.size() - 1) {
            Author author = books.getAuthorByIndex(authIndex);
            result = result.selectByAuthor(author);
        }
        cli.printByPages(result.toString());
    }
    
    /**
     * Предложить администратору добавить книгу в каталог
     */
    static void suggestBook() {
        
        //Тип книги: бумажная или электронная
        Option[] typeMenu = buildMenu(Book.Type.getFullNames());
        Book.Type type = Book.Type.values()[cli.getChoice(typeMenu)];
        
        //Название книги
        String title = cli.readLine("Введите название");
        if (title.isBlank()) {
            printErrorMsg("Пустое название");
            return;
        }
        
        /*
         * Выбрать автора из существующих или предложить нового
         * TODO: у книги может быть несколько авторов
         */
        List<String> authors = books.getAuthorsList();
        authors.add("Добавить автора");
        int authIndex = cli.getChoice(buildMenu(authors));
        String authorName;
        if (authIndex == authors.size() - 1) {  //Добавить автора
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
    
    /**
     * Отправить адресатам по списку newEmail с предложением добавить книгу
     * @param userName Имя отправителя
     * @param type Тип книги: бумажная или электронная
     * @param authorName Автор книги
     * @param title Название книги
     * @param adminsEmails Список адресатов
     */
    static void sendSuggest(String userName, Book.Type type, String authorName, 
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
    
    /**
     * Изменить сведения о книге
     */
    static void modifyBook() {
        int id = cli.getInt("Введите id книги");
        try {
            Book book = books.getBook(id);
            boolean changed = false;
            
            //Изменить название
            String newTitle = cli.readLine("Название: " + book.getTitle() + BR
                    + "Введите новое название (пустая строка - оставить без изменений)");
            if (!newTitle.isBlank()) {
                book.setTitle(newTitle);
                changed = true;
            }
            
            /*
             * Изменить автора
             * TODO: у книги может быть несколько авторов
             */            
            List<String> authors = books.getAuthorsList();
            authors.add("Оставить без изменений");
            System.out.println("Авторы: " + book.authorsToString());
            int authIndex = cli.getChoice(buildMenu(authors));
            if (authIndex != authors.size() - 1) {
                book.setAuthors(books.authorsByIndexes(new int[]{authIndex}));
                changed = true;
            }
            
            if (changed) {
                System.out.println("Информация о книге успешно изменена.");
            } else {
                System.out.println("Информация осталась без изменений.");
            }
        } catch (Exception e) {
            printErrorMsg(e.getMessage());
        }
    }
    
    /**
     * Добавить книгу в каталог
     */
    static void addBook() {
        
        //Тип книги: бумажная или электронная
        Option[] typeMenu = buildMenu(Book.Type.getFullNames());
        Book.Type type = Book.Type.values()[cli.getChoice(typeMenu)];
        
        //Название
        String title = cli.getString("Введите название");
        if (title.isBlank()) {
            printErrorMsg("Пустое название");
            return;
        }
        
        /*
         * Автор
         * TODO: у книги может быть несколько авторов
         */
        List<String> authors = books.getAuthorsList();
        Author author;
        authors.add("Добавить автора");
        int authIndex = cli.getChoice(buildMenu(authors));
        if (authIndex == authors.size() - 1) {  //Добавить автора
            author = addAuthor();
            if (author == null) return;         //Не удалось добавить
        } else {                                //Автор из списка
            author = books.getAuthorByIndex(authIndex);
        }
        
        if (books.addBook(type, title, new Author[]{author}) != null) {
            System.out.println("Книга успешно добавлена.");
            User me = users.getLogged();
            List<Address> emails = users.getAllEmailsExcept(me);
            sendNotification(me.getName(), type, author.getName(), title, emails);
        } else {
            printErrorMsg("Не удалось добавить книгу.");
        }
    }
    
    /**
     * Отправить адресатам по списку оповещение о новой книге
     * @param adminName Имя отправителя
     * @param type Тип книги: бумажная или электронная
     * @param authorName Автор книги
     * @param title Название книги
     * @param emails Список адресатов
     */
    static void sendNotification(String adminName, Book.Type type, 
            String authorName, String title, List<Address> emails) {
        StringBuilder emailText = new StringBuilder("Администратор ");
        emailText.append(adminName).append(" добавил книгу");
        String emailSubject = emailText.toString();
        emailText.append('.').append(BR);
        emailText.append("Тип: ").append(type.fullName).append(BR);
        emailText.append("Автор: ").append(authorName).append(BR); 
        emailText.append("Название: ").append(title);
        sendMail(emails, emailSubject, emailText.toString());
    }
    
    /**
     * Удалить книгу из каталога
     */
    static void removeBook() {
        int id = cli.getInt("Введите id книги");
        try {
            books.removeBook(id);
            System.out.println("Книга успешно удалена.");
        } catch (Exception e) {
            printErrorMsg(e.getMessage());
        }
    }
    
    /**
     * Вывести всех авторов из каталога
     */
    static void printAuthors() {
        cli.printByPages(books.getAuthorsTable());
    }
    
    /**
     * Изменить сведения об авторе
     */
    static void editAuthor() {
        int id = cli.getInt("Введите id автора");
        try {
            Author author = books.getAuthor(id);
            String newName = cli.readLine(author.getName() + BR
                    + "Введите новое имя (пустая строка - оставить без изменений)");
            if (newName.isBlank()) return;
            author.setName(newName);
            cli.printByPages("Имя автора успешно изменено.");
        } catch (Exception e) {
            printErrorMsg(e.getMessage());
        }
    }
    
    /**
     * Добавить автора в справочник
     * @return новый автор или null в случае ошибки
     */
    static Author addAuthor() {
        String name = cli.readLine("Введите полное имя");
        try {
            Author author =  books.addAuthor(name);
            System.out.println("Автор успешно добавлен.");
            return author;
        } catch (Exception e) {
            printErrorMsg(e.getMessage());
            return null;
        }
    }
    
    /**
     * Вывести пользователей и администраторов
     */
    static void printUsers() {
        cli.printByPages(users.toString());
    }
    
    /** Меню выбора статуса нового пользователя */
    static final Option[] ADMIN_OR_USER = new Option[]{
        new Option("Администратор"),
        new Option("Пользователь")
    };
    
    /**
     * Добавить нового пользователя
     * @return 
     */
    static User addUser() {
        String name = cli.readLine("Введите имя нового пользователя").strip();
        if (name.isBlank()) {
            printErrorMsg(User.EMPTY_NAME);
            return null;
        }
        if (!users.nameIsFree(name)) {
            printErrorMsg("Такое имя пользователя уже есть");
            return null;
        }
        char[] pass = cli.readPassword("Введите пароль нового пользователя");
        if (pass.length < 1) {
            printErrorMsg("Пароль не может быть пустым");
            return null;
        }
        if (!Arrays.equals(pass, cli.readPassword("Повторите пароль"))) {
            printErrorMsg("Введённые пароли не совпадают");
            return null;
        }
        Address email;
        try {
            email = new InternetAddress(cli.readLine("Введите email"));
        } catch (AddressException e) {
            printErrorMsg("неверный формат.");
            email = null;
        }
//        boolean admin = cli.getChoice(ADMIN_OR_USER) == 0;
        try {
            User result =  users.add(name, pass, email);
            if (result != null) {
                System.out.println("Пользователь успешно добавлен.");
            } else {
                printErrorMsg("Не удалось добавить пользователя.");
            }
            return result;
        } catch (Exception e) {
            printErrorMsg(e.getMessage());
            return null;
        }
    }
    
    /**
     * Изменить учётную запись пользователя
     */
    static void editUser() {
        //Выбрать пользователя из списка
        var usersList = buildMenu(users.getList());
        User user = users.getByIndex(cli.getChoice(usersList));
        boolean changed = false;
        
        //Изменить имя
        String newName = cli.readLine("Введите новое имя (пустая строка - оставить без изменений)");        
        if (!newName.isBlank() && !newName.contentEquals(user.getName())) {
            try {
                user.setName(newName);
                changed = true;
            } catch (Exception e) {
                printErrorMsg(e.getMessage());
            }
        }
        
        //Изменить пароль
        char[] newPass = cli.readPassword("Введите новый пароль (пустая строка - оставить без изменений)");
        if (newPass.length > 0) {
            if (Arrays.equals(newPass, cli.readPassword("Повторите пароль"))) {
                try {
                    user.setPassword(newPass);
                    changed = true;
                } catch (Exception e) {
                    printErrorMsg(e.getMessage());
                }
            } else {
                printErrorMsg("Введённые пароли не совпадают");
            }
        }
        
        //Изменить email
        String newEmail = cli.readLine("Введите новый email (пустая строка - оставить без изменений)");
        if (!newEmail.isBlank()) {
            try {
                user.setEmail(new InternetAddress(newEmail));
                changed = true;
            } catch (AddressException e) {
                printErrorMsg("Неверный формат");
            }
        }
        
        //Изменить статус: администратор или пользоатель
        if (users.canDelete(user)) {
            boolean admin = cli.getChoice(ADMIN_OR_USER) == 0;
            if (admin != user.isAdmin()) {
                user.setAdmin(admin);
                changed = true;
            }
        }
                
        if (changed) {
            cli.printByPages("Учётная запись пользователя " + user.getName() + " успешна изменена.");
        } else {
            cli.printByPages("Учётная запись пользователя " + user.getName() + " осталась без изменений.");
        }
    }
    
    /**
     * Удалить пользователя
     */
    static void removeUser() {
        var usersMenu = buildMenu(users.getList());        
        try {
            users.remove(cli.getChoice(usersMenu));
            System.out.println("Учётная запись пользователя успешно удалена.");
        } catch (Exception e) {
            printErrorMsg(e.getMessage());
        }
    }
    
    /**
     * Завершение работы приложения
     */
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
}

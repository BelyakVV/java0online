package task1;

import cli.CLI;
import cli.CLI.Option;
import static cli.CLI.buildMenu;
import homelib.Book;
import homelib.Book.Author;
import homelib.Library;
import homelib.User;
import homelib.Users;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author aabyodj
 */
public class Task1 {
    
    static Library library = new Library("books.txt", "authors.txt");
    
    static Users users = new Users("users.txt");
    
    static CLI cli = new CLI();
    
    static final Option[] ADMIN_MENU = new Option[]{
        new Option("Показать все книги", Task1::printAll),
        new Option("Найти книгу", Task1::searchBook),        
        new Option("Управление книгами", new Option[]{
            new Option("Изменить информацию о книге", Task1::modifyBook),
            new Option("Добавить книгу", Task1::addBook),
            new Option("Удалить книгу", Task1::removeBook)
        }),
        new Option("Управление списком авторов", new Option[]{
            new Option("Показать список авторов", Task1::printAuthors),
            new Option("Изменить имя автора", Task1::editAuthor),
            new Option("Добавить автора", Task1::addAuthor),
        }),
        new Option("Управление списком пользователей", new Option[]{
            new Option("Показать список пользователей", Task1::printUsers),
            new Option("Редактировать учётную запись пользователя", Task1::editUser),
            new Option("Добавить пользователя", Task1::addUser),
            new Option("Удалить пользователя", Task1::removeUser),
        }),        
        new Option("Выход", Task1::exit)
    };
    
    static final Option[] USER_MENU = new Option[]{
        new Option("Показать все книги", Task1::printAll),
        new Option("Найти книгу", Task1::searchBook),
        new Option("Предложить книгу", Task1::suggestBook),
        new Option("Выход", Task1::exit)
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (users.size() < 1) {
            errorMsg("Не удалось загрузить список пользователей. Создаём учётную запись администратора...");
            if (!addUser()) System.exit(1);
            User user = users.get(0);
            user.setAdmin(true);
            users.login(user);
        } else if (!users.login(cli.getString("Введите логин"), cli.getPass("Введите пароль"))) {
            errorMsg("Неверный логин или пароль");
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
        cli.println(library.toString());        
    }
    
    static void searchBook() {
        String title = cli.getString("Название содержит (пустая строка - игнорировать название)");
        Library search = library.onlyTitle(title);
        List<String> authors = library.getAuthorsList();
        authors.add("Любой автор");
        int authIndex = cli.getChoice(buildMenu(authors));
        if (authIndex < authors.size() - 1)
            search = search.onlyAuthor(library.authorsByIndexes(new int[]{authIndex})[0]);
        cli.println(search.toString());
    }
    
    static void suggestBook() {
        Option[] menu = buildMenu(Book.Type.getFullNames());
        Book.Type type = Book.Type.values()[cli.getChoice(menu)];
        String title = cli.getString("Введите название");
        if (title.isBlank()) {
            cli.println("Ошибка: пустое название");
            return;
        }
        List<String> authors = library.getAuthorsList();
        authors.add("Добавить автора");
        int authIndex = cli.getChoice(buildMenu(authors));
        String authorName;
        if (authIndex == authors.size() - 1) {
            authorName = cli.getString("Введите полное имя автора");
            if (authorName.isBlank()) {
                cli.println("Ошибка: имя автора не может быть пустым");
                return;
            }
        } else {
            authorName = authors.get(authIndex);
        }
        List<Address> adminsEmails = users.getAdminsEmails();
        StringBuilder emailText = new StringBuilder("Пользователь ");
        emailText.append(users.getLogged().getName());
        emailText.append(" предлагает добавить книгу");
        String emailSubject = emailText.toString();
        String br = System.lineSeparator();
        emailText.append('.').append(br);
        emailText.append("Тип: ").append(type.fullName).append(br);
        emailText.append("Автор: ").append(authorName).append(br); 
        emailText.append("Название: ").append(title);
        sendMail(adminsEmails, emailSubject, emailText.toString());
    }
    
    static void modifyBook() {
        int id = cli.getInt("Введите id книги");
        try {
            Book book = library.getBook(id);
            cli.println("Название: " + book.getTitle());
            String title = cli.getString("Введите новое название (пустая строка - оставить без изменений)");
            boolean changed = false;
            if (!title.isBlank()) {
                book.setTitle(title);
                changed = true;
            }
            List<String> authors = library.getAuthorsList();
            authors.add("Оставить без изменений");
            cli.println("Авторы: " + book.authorsToString());
            int authIndex = cli.getChoice(buildMenu(authors));
            if (authIndex != authors.size() - 1) {
                book.setAuthors(library.authorsByIndexes(new int[]{authIndex}));
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
        List<String> authors = library.getAuthorsList();
        Author author;
        authors.add("Добавить автора");
        int authIndex = cli.getChoice(buildMenu(authors));
        try {
            if (authIndex == authors.size() - 1) {
                author = addAuthor();
                if (author == null) return;
                library.addBook(type, title, new Author[]{author});
            } else {
                author = library.getAuthorByIndex(authIndex);
                library.addBook(type, title, new Author[]{author});
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
            library.removeBook(id);
            cli.println("Книга успешно удалена.");
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
        }
    }
    
    static void printAuthors() {
        cli.println(library.getAuthorsTable());
    }
    
    static void editAuthor() {
        int id = cli.getInt("Введите id автора");
        try {
            Author author = library.getAuthor(id);
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
            Author author =  library.addAuthor(name);
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
            errorMsg("неверный формат.");
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
                errorMsg("Неверный формат");
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
            library.saveAuthors();            
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
        }
        try {
            library.saveBooks();            
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
        }
        try {
            users.save();            
        } catch (Exception e) {
            cli.println("Ошибка: " + e.getMessage());
        }
        System.exit(0);
    }
    
    //TODO переделать архитектуру приложения в клиент/сервер и вынести отправку email на сервер
    static final String SMTP_HOST = "smtp.yandex.com";
    static final String SMTP_PORT = "465";
    static final String SMTP_LOGIN = "epam-grow@aab.by"; 
    static final String SMTP_PASSWORD = "J99oSLrt)jsf}mw8";
    static final String APP_NAME = "Учёт книг в домашней библиотеке";
        
    static void sendMail(List<Address> recipients, String subject, String text) {
        System.out.print("Отправка email...");
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.socketFactory.port", SMTP_PORT);
        properties.put("mail.smtp.socketFactory.class", 
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");    
        properties.put("mail.smtp.port", SMTP_PORT);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_LOGIN, SMTP_PASSWORD);
            }
        });
        try {    
            MimeMessage message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(SMTP_LOGIN, APP_NAME));
            } catch (UnsupportedEncodingException ex) {
                message.setFrom(new InternetAddress(SMTP_LOGIN));
            }
            for (var recipient: recipients) {
                message.addRecipient(Message.RecipientType.TO, recipient);
            }
            message.setSubject(subject);    
            message.setText(text); 
            Transport.send(message);    
            System.out.println("ok");    
          } catch (MessagingException e) {
              errorMsg(e.getMessage());
          }
    }
    
    static void errorMsg(String msg) {
        cli.println("Ошибка: " + msg);
    }
}

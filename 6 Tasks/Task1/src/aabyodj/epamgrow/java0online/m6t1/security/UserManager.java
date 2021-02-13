package aabyodj.epamgrow.java0online.m6t1.security;

import aabyodj.console.Table;
import static aabyodj.epamgrow.java0online.m6t1.Util.createIfNeeded;
import jakarta.mail.Address;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Агрегатор пользователей. 
 * Администратор является частным случаем пользователя.
 * @author aabyodj
 */
public final class UserManager {
    
    /** Список пользователей */
    List<User> users;
    
    /** Файл пользователей */
    File file;
    
    /** Признак того, что список изменился по сравнению с файлом */
    boolean changed = true;
    
    /** Залогиненный пользователь */
    User loggedUser;
    
    /**
     * Загрузить список пользователей из файла. В случае ошибки - создать пустой
     * список.
     * @param fileName 
     */
    public UserManager(String fileName) {
        file = new File(fileName).getAbsoluteFile();
        try {
            load();
        } catch (FileNotFoundException ex) {
            users = new LinkedList<>();
        }
    }
    
    /**
     * Добавить нового пользователя
     * @param name Имя пользователя
     * @param password Пароль пользователя
     * @param email email пользователя
     * @return Добавленный пользователь
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public User add(String name, char[] password, Address email) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {  
        
        User result = new User(name, password, email, this);        
        if (!users.add(result)) 
            throw new RuntimeException("Не удалось добавить пользователя");
        
        users.sort(User::compareTo);
        changed = true;
        return result;
    }
    
    /**
     * Получить пользователя по его индексу в списке
     * @param i Индекс пользователя
     * @return 
     */
    public User getByIndex(int i) {
        return users.get(i);
    }
    
    /**
     * Получить пользователя по его имени
     * @param name
     * @return 
     */
    public User get(String name) {
        for (var user: users) {
            if (user.name.equalsIgnoreCase(name))
                return user;
        }
        throw new NoSuchElementException(User.NOT_FOUND);
    }
    
    /**
     * Получить список имён пользователей
     * @return 
     */
    public List<String> getList() {
        List<String> result = new LinkedList<>();
        for (var user: users) {
            result.add(user.name);
        }
        return result;
    }
    
    /**
     * Получить список email всех пользователей, кроме заданного
     * @param exclude Исключаемый пользователь
     * @return 
     */
    public List<Address> getAllEmailsExcept(User exclude) {
        List<Address> result = new LinkedList<>();
        for (var user: users) {
            
            //Предполагается, что клиент может создать пользователя ТОЛЬКО посредством данного агрегатора
            if ((user != exclude) && (user.email != null)) 
                result.add(user.email);
        }
        return result;
    }
    
    /**
     * Получить список email администраторов
     * @return 
     */
    public List<Address> getAdminsEmails() {
        List<Address> result = new LinkedList<>();
        for (var user: users) {
            if (user.admin && user.email != null) 
                result.add(user.email);
        }
        return result;
    }
    
    /**
     * Удалить пользователя, заданного своим индексом в списке. Последнего
     * администратора удалить нельзя.
     * @param i Индекс удаляемого пользователя
     * @return 
     */
    public User remove(int i) {
        int j = 0;        
        for (var user: users) {
            if ((i == j++) ^ user.admin) {                
                users.remove(i);
                changed = true;
                return user;
            }
        }
        throw new IllegalStateException("Попытка удалить последнего администратора");
        
    }
    
    /**
     * Залогинить заданного пользователя
     * @param user 
     */
    public void login(User user) {
        loggedUser = user;
    }
    
    /**
     * Залогинить пользователя, используя его имя и пароль
     * @param userName
     * @param password
     * @return true, если успешно
     */
    public boolean login(String userName, char[] password) {
        try {
            User user = get(userName);
            boolean result = user.isValidPassword(password);
            if (result) loggedUser = user;
            return result;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Получить залогиненного пользователя
     * @return 
     */
    public User getLogged() {
        return loggedUser;
    }
    
    /**
     * Получить количество пользователей в списке
     * @return 
     */
    public int size() {
        return users.size();
    }
    
    /** Шапка таблицы пользователей */
    static final String[] HEAD = new String[]{"Логин", "email", "Админ"};
    
    /**
     * Отформатировать список пользователей в виде текстовой таблицы
     * @return 
     */
    @Override
    public String toString() {
        
        //Создать таблицу с заданной шапкой
        Table result = new Table(HEAD);
        
        //Отцентрировать поле "Админ"
        result.getCol(2).setAlign(Table.Align.CENTER);
        
        for (var user: users) {
            result.addRow(new String[]{
                user.name,
                (user.email != null) ? user.email.toString() : "не указан",
                user.admin ? "+" : ""
            });
        }
        return result.toString();
    }
    
    /**
     * Определить, не является ли заданный пользователь последним 
     * администратором (и его можно безопасно удалить).
     * @param victim Заданный пользователь
     * @return true, если заданный пользователь не является последним 
     * администратором
     */
    public boolean canDelete(User victim) {
        if (!victim.admin) return true;
        for (var user: users) {
            if ((user != victim) && user.admin) return true;
        }
        return false;
    }
    
    /**
     * Определить, свободно ли заданное имя пользователя
     * @param name Заданное имя
     * @return 
     */
    public boolean nameIsFree(String name) {
        name = name.strip().toLowerCase();
        for (var user: users) {
            if (user.name.toLowerCase().contentEquals(name)) return false;
        }
        return true;
    }

    /**
     * Загрузить список пользователей из файла
     * @throws FileNotFoundException 
     */
    void load() throws FileNotFoundException {
        Scanner in = new Scanner(file);
        users = new LinkedList<>();
        while (in.hasNextLine()) {
            try {
                users.add(new User(in.nextLine(), this));
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        users.sort(User::compareTo);
        changed = false;
    }
    
    /**
     * Сохранить список пользователей в файл
     * @throws IOException 
     */
    public void save() throws IOException {
        if (!changed) return;
        createIfNeeded(file);
        try (PrintStream out = new PrintStream(file)) {
            for (var user: users) {
                out.println(user);
            }
            changed = false;
        }
    }
}

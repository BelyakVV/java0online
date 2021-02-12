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
 *
 * @author aabyodj
 */
public final class UserManager {
    List<User> users;
    File file;
    boolean changed;
    User loggedUser;
    
//    public UserManager() {
//        users = new LinkedList<>();
//    }
    
    public UserManager(String fileName) {
        file = new File(fileName).getAbsoluteFile();
        try {
            load(file);
        } catch (FileNotFoundException ex) {
            users = new LinkedList<>();
            changed = true;
        }
    }
    
    public boolean add(String name, char[] password, Address email) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {        
        boolean result = users.add(new User(name, password, email, this));
        if (result) {
            users.sort(User::compareTo);
            changed = true;
        }
        return result;
    }
    
    public User get(int i) {
        return users.get(i);
    }
    
    public User get(String name) {
        for (var user: users) {
            if (user.name.equalsIgnoreCase(name))
                return user;
        }
        throw new NoSuchElementException(User.NOT_FOUND);
    }
    
    public List<String> getList() {
        List<String> result = new LinkedList<>();
        for (var user: users) {
            result.add(user.name);
        }
        return result;
    }
    
    public List<Address> getAllEmailsExcept(User exclude) {
        List<Address> result = new LinkedList<>();
        for (var user: users) {
            //Предполагается, что клиент может создать пользователя ТОЛЬКО посредством данного агрегатора
            if (user != exclude && user.email != null) 
                result.add(user.email);
        }
        return result;
    }
    
    public List<Address> getAdminsEmails() {
        List<Address> result = new LinkedList<>();
        for (var user: users) {
            if (user.admin && user.email != null) 
                result.add(user.email);
        }
        return result;
    }
    
    public User remove(int i) {
        int j = 0;        
        for (var user: users) {
            if (i == j++ ^ user.admin) {
                users.remove(i);
                changed = true;
                return user;
            }
        }
        throw new IllegalStateException("Попытка удалить последнего администратора");
        
    }
    
    public void login(User user) {
        loggedUser = user;
    }
    
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
    
    public User getLogged() {
        return loggedUser;
    }
    
    public int size() {
        return users.size();
    }
    
    static final String[] HEAD = new String[]{"Логин", "email", "Админ"};
    
    @Override
    public String toString() {
        Table result = new Table(HEAD);
        result.getCol(2).setAlign(Table.Align.CENTER);
        for (var user: users) {
            result.addRow(new String[]{
                user.name,
                user.email != null ? user.email.toString() : "не указан",
                user.admin ? "+" : ""
            });
        }
        return result.toString();
    }
    
    public boolean canDelete(User victim) {
        if (!victim.admin) return true;
        for (var user: users) {
            if (user != victim && user.admin) return true;
        }
        return false;
    }
    
    public boolean nameIsFree(String name) {
        name = name.strip().toLowerCase();
        for (var user: users) {
            if (user.name.toLowerCase().contentEquals(name)) return false;
        }
        return true;
    }

    void load(File file) throws FileNotFoundException {
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

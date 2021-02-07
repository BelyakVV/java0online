package homelib;

import cli.Table;
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
public final class Users {
    List<User> users;
    String fileName;
    boolean changed;
    User loggedUser;
    
//    public Users() {
//        users = new LinkedList<>();
//    }
    
    public Users(String fileName) {
        try {
            load(fileName);
        } catch (FileNotFoundException ex) {
            users = new LinkedList<>();
            this.fileName = fileName;
            changed = true;
        }
    }
    
    public boolean add(String name, char[] password) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {        
        boolean result = users.add(new User(name, password, this));
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
    
    static final String[] HEAD = new String[]{"Логин", "Админ"};
    
    @Override
    public String toString() {
        Table result = new Table(HEAD);
        result.getCol(1).setAlign(Table.Align.CENTER);
        for (var user: users) {
            result.addRow(new String[]{
                user.name,
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

    public void load(String fileName) throws FileNotFoundException {
        Scanner file = new Scanner(new File(fileName));
        this.fileName = fileName;
        users = new LinkedList<>();
        while (file.hasNextLine()) {
            try {
                users.add(new User(file.nextLine(), this));
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        users.sort(User::compareTo);
        changed = false;
    }
    
    public void save() throws IOException {
        if (!changed) return;
        File file = new File(fileName);
        if (!file.exists()) file.createNewFile();
        try (PrintStream out = new PrintStream(file)) {
            for (var user: users) {
                out.println(user);
            }
            changed = false;
        }
    }
}

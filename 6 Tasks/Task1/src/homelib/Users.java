package homelib;

import cli.Table;
import java.io.File;
import java.io.FileNotFoundException;
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
public class Users {
    List<User> users;
    User loggedUser;
    
    public Users() {
        users = new LinkedList<>();
    }
    
    public Users(String fileName) throws FileNotFoundException {
        Scanner file = new Scanner(new File(fileName));
        users = new LinkedList<>();
        while (file.hasNextLine()) {
            try {
                users.add(new User(file.nextLine()));
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
    }
    
    public boolean add(String name, String password) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return users.add(new User(name, password));
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
                return users.remove(i);
            }
        }
        throw new IllegalStateException("Попытка удалить последнего администратора");
        
    }
    
    public void login(User user) {
        loggedUser = user;
    }
    
    public boolean login(String userName, String password) {
        try {
            User user = get(userName);
            boolean result = user.isValidPassword(password);
            if (result) loggedUser = user;
            return result;
        } catch (Exception e) {
            return false;
        }
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
}

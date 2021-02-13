package aabyodj.epamgrow.java0online.m6t1.security;

import static aabyodj.datafiles.Const.FLD_DLM;
import static aabyodj.datafiles.Const.FLD_DLM_PTTRN;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

/**
 * Пользователь домашней библиотеки. 
 * Администратор является частным случаем пользователя.
 * @author aabyodj
 */
public final class User implements Comparable {
    
    /** Имя пользователя */
    String name;
    
    /** Признак того, что это администратор */
    boolean admin;
    
    static final int SALT_LENGTH = 16;
    static final int ITERATIONS = 10000;
    static final int KEY_LENGTH = 256;
    
    /** Хэш пароля пользователя */
    byte[] hash;
    
    /** Соль */
    byte[] salt = new byte[SALT_LENGTH];
    
    /** email пользователя */
    Address email;    
    
    /** Агрегатор пользователей */
    UserManager users;
    
    public static final String DUPLICATE_NAME = "Пользователь с таким именем уже существует";
    public static final String EMPTY_NAME = "Имя пользователя не может быть пустым";
    public static final String NOT_FOUND = "Нет такого пользователя";
    
    User(String name, char[] password, Address email, UserManager users) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.users = users;
        this.name = "";
        this.email = email;
        setName(name);
        setPassword(password);        
    }
    
    /**
     * Загрузить пользователя из строки текстового файла
     * @param line Исходная строка
     * @param users Агрегатор пользователей
     */
    User(String line, UserManager users) {
        Scanner in = new Scanner(line).useDelimiter(FLD_DLM_PTTRN);
        name = in.next();
        
        //Хэш и соль
        byte[] saltAndHash = Base64.getDecoder().decode(in.next());
        System.arraycopy(saltAndHash, 0, salt, 0, SALT_LENGTH);
        hash = new byte[saltAndHash.length - SALT_LENGTH];
        System.arraycopy(saltAndHash, SALT_LENGTH, hash, 0, hash.length);
        
        admin = in.nextInt() == 1;
        
        if (in.hasNext()) {
            try {
                email = new InternetAddress(in.next());
            } catch (AddressException ex) {
                email = null;
            }
        }
        this.users = users;
    }
    
    /**
     * Получить имя пользователя
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Задать имя пользователя
     * @param newName Новое имя
     */
    public void setName(String newName) {
        if (newName.isBlank())
            throw new IllegalArgumentException(EMPTY_NAME);
        
        newName = newName.strip();
        if (newName.contentEquals(this.name)) return;
        
        if (!users.nameIsFree(newName)) 
            throw new IllegalStateException(DUPLICATE_NAME);
        
        this.name = newName;
        users.users.sort(User::compareTo);
        users.changed = true;
    }
    
    /**
     * Задать новый пароль для пользователя
     * @param newPassword Новый пароль
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public void setPassword(char[] newPassword) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        hash = createHash(newPassword);
        users.changed = true;
    }
    
    /**
     * Проверить, соответствует ли заданный пароль текущему
     * @param password Заданный пароль
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public boolean isValidPassword(char[] password) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Arrays.equals(hash, createHash(password));
    }
    
    /**
     * Создать хэш от пароля, используя текущую соль
     * @param password Заданный пароль
     * @return Хэш от заданного пароля
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    byte[] createHash(char[] password) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        var spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
    }
    
    /**
     * Получить email пользователя
     * @return 
     */
    public Address getEmail() {
        return email;
    }
    
    /**
     * Задать newEmail пользователя
     * @param newEmail Новый newEmail
     */
    public void setEmail(Address newEmail) {
        if (newEmail.equals(this.email)) return;
        this.email = newEmail;
        users.changed = true;
    }
    
    /**
     * Проверить, является ли пользователь администратором
     * @return 
     */
    public boolean isAdmin() {
        return admin;
    }
    
    /**
     * Задать наличие либо отсутствие статуса администратора. Последнего 
     * администратора разжаловать нельзя.
     * @param admin Желаемый статус: true, если администратор
     * @return true, если новый текущий статус соответствует заданному
     */
    public boolean setAdmin(boolean admin) {
        if (this.admin == admin) return true;
        
        //Нельзя разжаловать последнего администратора
        if (!admin && !users.canDelete(this)) return false;
        
        this.admin = admin;
        users.changed = true;
        return true;
    }

    @Override
    public int compareTo(Object o) {
        return name.compareToIgnoreCase(((User) o).name);
    }
    
    /**
     * Отформатировать строковое представление пользователя для записи в файл
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name);
        result.append(FLD_DLM);
        
        //Соль и хэш
        byte[] saltAndHash = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
        System.arraycopy(hash, 0, saltAndHash, salt.length, hash.length);
        result.append(Base64.getEncoder().encodeToString(saltAndHash));
        
        result.append(FLD_DLM).append(admin ? '1' : '0');
        
        if (email != null) 
            result.append(FLD_DLM).append(email);
        
        return result.toString();
    }
}

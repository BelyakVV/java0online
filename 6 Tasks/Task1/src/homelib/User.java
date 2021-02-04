package homelib;

import static homelib.Library.F_DELIMITER;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author aabyodj
 */
public final class User {
    //final int id;
    String name;
    byte[] hash;
    byte[] salt = new byte[SALT_LENGTH];
    String email;
    boolean admin;
    
    static final int SALT_LENGTH = 16;
    static final int ITERATIONS = 10000;
    static final int KEY_LENGTH = 256;
    
    public static final String EMPTY_NAME = "Имя пользователя не может быть пустым";
    public static final String NOT_FOUND = "Нет такого пользователя";
    
    User(String name, String password) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        setName(name);
        setPassword(password);
    }
    
    User(String line) {
        Scanner in = new Scanner(line).useDelimiter(F_DELIMITER);
        name = in.next();
        byte[] saltAndHash = Base64.getDecoder().decode(in.next());
        System.arraycopy(saltAndHash, 0, salt, 0, SALT_LENGTH);
        hash = new byte[saltAndHash.length - SALT_LENGTH];
        System.arraycopy(saltAndHash, SALT_LENGTH, hash, 0, hash.length);
        admin = in.nextInt() == 1;
        email = in.next();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name.isBlank())
            throw new IllegalArgumentException(EMPTY_NAME);
        this.name = name.strip();
    }
    
    public void setPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        hash = createHash(password);
    }
    
    public boolean isValidPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Arrays.equals(hash, createHash(password));
    }
    
    byte[] createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
    }
    
    public boolean isAdmin() {
        return admin;
    }
    
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}

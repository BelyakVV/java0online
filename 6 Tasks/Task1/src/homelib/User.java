package homelib;

import static homelib.Library.FLD_DLM;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import static homelib.Library.FLD_DLM_PTTRN;

/**
 *
 * @author aabyodj
 */
public final class User implements Comparable {
    //final int id;
    String name;
    byte[] hash;
    byte[] salt = new byte[SALT_LENGTH];
    String email;
    boolean admin;
    
    Users users;
    
    static final int SALT_LENGTH = 16;
    static final int ITERATIONS = 10000;
    static final int KEY_LENGTH = 256;
    
    public static final String EMPTY_NAME = "Имя пользователя не может быть пустым";
    public static final String NOT_FOUND = "Нет такого пользователя";
    
    User(String name, String password, Users users) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.users = users;
        this.name = "";
        setName(name);
        setPassword(password);        
    }
    
    User(String line, Users users) {
        Scanner in = new Scanner(line).useDelimiter(FLD_DLM_PTTRN);
        name = in.next();
        byte[] saltAndHash = Base64.getDecoder().decode(in.next());
        System.arraycopy(saltAndHash, 0, salt, 0, SALT_LENGTH);
        hash = new byte[saltAndHash.length - SALT_LENGTH];
        System.arraycopy(saltAndHash, SALT_LENGTH, hash, 0, hash.length);
        admin = in.nextInt() == 1;
        email = in.next();
        this.users = users;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name.isBlank())
            throw new IllegalArgumentException(EMPTY_NAME);
        name = name.strip();
        if (name.contentEquals(this.name)) return;
        if (!users.nameIsFree(name)) 
            throw new IllegalStateException("Пользователь с таким именем уже существует");
        this.name = name;
        users.users.sort(User::compareTo);
        users.changed = true;
    }
    
    public void setPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        hash = createHash(password);
        users.changed = true;
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
        if (this.admin == admin) return;
        this.admin = admin;
        users.changed = true;
    }

    @Override
    public int compareTo(Object o) {
        return name.compareToIgnoreCase(((User) o).name);
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(name).append(FLD_DLM);
        byte[] saltAndHash = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
        System.arraycopy(hash, 0, saltAndHash, salt.length, hash.length);
        result.append(Base64.getEncoder().encodeToString(saltAndHash)).append(FLD_DLM);
        result.append(admin ? '1' : '0').append(FLD_DLM);
        result.append(email);
        return result.toString();
    }
}

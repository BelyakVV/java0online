package m6t3.common;

import static m6t3.common.Const.INVALID_ID;
import static m6t3.common.Const.INVALID_SERIAL;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User {
    
    static final int SALT_LENGTH = 16;
    static final int ITERATIONS = 10000;
    static final int KEY_LENGTH = 256;
    
	public final int id;
	int serial;
	public String login;
	
	byte[] hash;
	byte[] salt;// = new byte[SALT_LENGTH];
	
	public boolean admin;
	
	public User(int id, int serial, String login, byte[] hash, byte[] salt, boolean admin) {
		this.id = id;
		this.serial = serial;
		this.login = login;
		this.hash = hash;
		this.salt = salt;
		this.admin = admin;
	}
	
//	public User(int id, String login, boolean admin) {
//		this(id, 0, login, admin);
//	}
	
	public User() {
		this(INVALID_ID, 0, "", null, null, false);
	}
	
	public User(int id, User origin) {
		this.id = id;
		this.serial = origin.serial;
		this.login = origin.login;
		this.hash = origin.hash;
		this.salt = origin.salt;
		this.admin = origin.admin;
	}

	public int getSerial() {
		return serial;
	}

	public void incSerial() {
		serial++;
	}

	public String getLogin() {
		return login;
	}

	public boolean setLogin(String login) {
		if (login.isBlank()) {
			return false;
		}
		if (login.contentEquals(this.login)) return true;
		this.login = login;
		serial++;
		return true;
	}	
	
	public boolean isValidPassword(char[] password) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Arrays.equals(hash, createHash(password));
    }
	
	public void setPassword(char[] newPassword) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        hash = createHash(newPassword);
        serial++;
    }
	
	byte[] createHash(char[] password) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        var spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
    }
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		if (admin == this.admin) return;
		this.admin = admin;
		serial++;
	}

	public boolean update(User upd) {
		if (upd.serial > serial) {
			serial = upd.serial;
			login = upd.login;
			hash = upd.hash;
			salt = upd.salt;
			admin = upd.admin;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", serial=" + serial + ", login=" + login 
//				+ ", hash=" + Arrays.toString(hash) + ", salt=" + Arrays.toString(salt) 
				+ ", admin=" + admin + "]";
	}

	public Object suicide() {
		serial = INVALID_SERIAL;
		return this;
	}
}

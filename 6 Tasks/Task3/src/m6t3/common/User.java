package m6t3.common;

import static m6t3.common.Const.INVALID_ID;
import static m6t3.common.Const.INVALID_SERIAL;
import static m6t3.common.Tranceiver.SEND_USER;
import static m6t3.common.Tranceiver.getInt;
import static m6t3.common.Tranceiver.receiveBytes;
import static m6t3.common.Tranceiver.receiveInt;
import static m6t3.common.Tranceiver.toBytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class User implements Transmittable, XMLable {
    
	static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    static final int SALT_LENGTH = 16;
    static final int ITERATIONS = 10000;
    static final int KEY_LENGTH = 256;
    
	public final int id;
	private int serial;
	private String login;
	
	private byte[] hash;
	private byte[] salt;// = new byte[SALT_LENGTH];
	
	private boolean admin;
	
	private User(int id, int serial, String login, byte[] hash, byte[] salt, boolean admin) {
		this.id = id;
		this.serial = serial;
		this.login = login;
		this.hash = hash;
		this.salt = salt;
		this.admin = admin;
	}
	
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
	
	public static User createAdmin(String login, char[] pass) 
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		User result = new User();
		result.setLogin(login);
		result.setPassword(pass);
		result.admin = true;
		return result;
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
        return Arrays.equals(hash, createHash(password, salt));
    }
	
	public void setPassword(char[] newPassword) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        hash = createHash(newPassword, salt);
        serial++;
    }
	
	static byte[] createHash(char[] password, byte[] salt) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        var spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        var factory = SecretKeyFactory.getInstance(ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }
	
	public AuthChallenge createChallenge() {
        SecureRandom random = new SecureRandom();
        byte[] challenge = new byte[SALT_LENGTH];
        random.nextBytes(challenge);
        return new AuthChallenge(challenge, salt);
	}
	
	public boolean isValidResponse(AuthChallenge challenge, AuthResponse response) 
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] validResponse = createHash(toCharArray(hash), challenge.challenge);
		return Arrays.mismatch(response.response, validResponse) < 0 ? true : false;
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

	public User suicide() {
		serial = INVALID_SERIAL;
		return this;
	}

	@Override
	public void transmit(OutputStream out) throws IOException {
//		System.out.println("Transmitting user:\n" + this);		
		byte[] signature = toBytes(SEND_USER);
		byte[] login = this.login.getBytes();
		int length = (Integer.BYTES * (3 + 3)) //id, serial, admin + login.length, hash.length, salt.length
				+ login.length + hash.length + salt.length;
		byte[] result = new byte[signature.length + Integer.BYTES + length];
		int pos = 0;
		System.arraycopy(signature, 0, result, pos, signature.length);
		pos += signature.length;
		System.arraycopy(toBytes(length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(toBytes(id), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(toBytes(serial), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(toBytes(login.length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(login, 0, result, pos, login.length);
		pos += login.length;
		System.arraycopy(toBytes(hash.length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(hash, 0, result, pos, hash.length);
		pos += hash.length;
		System.arraycopy(toBytes(salt.length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(salt, 0, result, pos, salt.length);
		pos += salt.length;
		System.arraycopy(toBytes(admin ? 1 : 0), 0, result, pos, Integer.BYTES);
		
		out.write(result);
	}

	public static User receive(InputStream in) throws IOException {
//		System.out.println("Приём пользователя");
		int length = receiveInt(in);
		byte[] buffer = receiveBytes(in, length);
		int pos = 0;
		int id = getInt(buffer, pos);
		pos += Integer.BYTES;
		int serial = getInt(buffer, pos);
		pos += Integer.BYTES;
		int loginLength = getInt(buffer, pos);	
		pos += Integer.BYTES;
		String login = new String(buffer, pos, loginLength);
		pos += loginLength;
		int hashLength = getInt(buffer, pos);
		pos += Integer.BYTES;
		byte[] hash = new byte[hashLength];
		System.arraycopy(buffer, pos, hash, 0, hashLength);
		pos += hashLength;
		int saltLength = getInt(buffer, pos);
		pos += Integer.BYTES;
		byte salt[] = new byte[saltLength];
		System.arraycopy(buffer, pos, salt, 0, saltLength);
		pos += saltLength;
		boolean admin = 0 == getInt(buffer, pos) ? false : true;
				
		return new User(id, serial, login, hash, salt, admin);
	}

	@Override
	public Element toXML(Document xmlDoc) {
		Element result = xmlDoc.createElement("user");
		result.setAttribute("id", Integer.toString(id));
		result.setAttribute("serial", Integer.toString(serial));
		result.setAttribute("login", login);
		result.setAttribute("hash", encodeHex(hash));
		result.setAttribute("salt", encodeHex(salt));
		result.setAttribute("admin", admin ? "true" : "false");
		return result;
	}

	private static String encodeHex(byte[] byteArray) {
		StringBuilder result = new StringBuilder();
		for (byte b: byteArray) {
			result.append(encodeHexDigit(b >> 4));
			result.append(encodeHexDigit(b));
		}
		return result.toString();
	}

	private static char encodeHexDigit(int i) {
		i = i & 0x0f;
		return (i > 9) ? (char) (i + 'a' - 10) : (char) (i + '0');
	}

	private static int decodeHexDigit(char digit) {
		if (digit < '0') return 0;
		if (digit <= '9') return digit - '0';
		digit = Character.toLowerCase(digit);
		if (digit < 'a' || digit > 'f') return 0;
		return digit - 'a' + 10;
	}
	
	public static User fromXML(Element elem) {
		int id = Integer.parseInt(elem.getAttribute("id"));
		int serial = Integer.parseInt(elem.getAttribute("serial"));
		String login = elem.getAttribute("login");
		String hash = elem.getAttribute("hash");
		String salt = elem.getAttribute("salt");
		String admin = elem.getAttribute("admin");
		return new User(id, serial, login, 
				toByteArray(hash), toByteArray(salt), 
				admin.equalsIgnoreCase("true") ? true : false);
	}

	private static byte[] toByteArray(String string) {
		char[] charArray = string.toCharArray();
		byte[] result = new byte[charArray.length >> 1]; 
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) ((decodeHexDigit(charArray[i << 1]) << 4)
					+ decodeHexDigit(charArray[(i << 1) + 1]));
			
		}
		return result;
	}

	static char[] toCharArray(byte[] byteArray) {
		char[] result = new char[byteArray.length >> 1];
		for (int i = 0; i < result.length; i++) {
			int hi = byteArray[i << 1];
			int lo = byteArray[(i << 1) + 1];
			result[i] = (char) ((hi << 8) + lo);
		}
		return result;
	}
}

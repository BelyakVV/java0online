package m6t3.common;

import static m6t3.common.Tranceiver.CHANGE_PASS_REQUEST;
import static m6t3.common.Tranceiver.getInt;
import static m6t3.common.Tranceiver.receiveBytes;
import static m6t3.common.Tranceiver.receiveInt;
import static m6t3.common.Tranceiver.toBytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *	Request from a user to change his password
 *
 * @author aabyodj
 */
public class ChangePassRequest implements Transmittable {
	final byte[] newHash;
	final byte[] newSalt;

	ChangePassRequest(byte[] newHash, byte[] newSalt) {
		this.newHash = newHash;
		this.newSalt = newSalt;
	}

	@Override
	public void transmit(OutputStream out) throws IOException {
		byte[] signature = toBytes(CHANGE_PASS_REQUEST);
		int length = Integer.BYTES * 2 //newHash.length, newSalt.length
				+ newHash.length + newSalt.length;
		byte[] result = new byte[signature.length + Integer.BYTES + length];
		int pos = 0;
		System.arraycopy(signature, 0, result, pos, signature.length);
		pos += signature.length;
		System.arraycopy(toBytes(length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(toBytes(newHash.length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(newHash, 0, result, pos, newHash.length);
		pos += newHash.length;
		System.arraycopy(toBytes(newSalt.length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(newSalt, 0, result, pos, newSalt.length);
		
		out.write(result);
	}

	public static ChangePassRequest receive(InputStream in) throws IOException {
		int length = receiveInt(in);
		byte[] buffer = receiveBytes(in, length);
		int pos = 0;
		int hashLength = getInt(buffer, pos);
		pos += Integer.BYTES;
		byte[] hash = new byte[hashLength];
		System.arraycopy(buffer, pos, hash, 0, hash.length);
		pos += hash.length;
		int saltLength = getInt(buffer, pos);
		pos += Integer.BYTES;
		byte[] salt = new byte[saltLength];
		System.arraycopy(buffer, pos, salt, 0, salt.length);
		
		return new ChangePassRequest(hash, salt);
	}
}

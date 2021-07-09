package m6t3.common;

import static m6t3.common.Tranceiver.AUTH_CHALLENGE;
import static m6t3.common.Tranceiver.getInt;
import static m6t3.common.Tranceiver.receiveBytes;
import static m6t3.common.Tranceiver.receiveInt;
import static m6t3.common.Tranceiver.toBytes;
import static m6t3.common.User.createHash;
import static m6t3.common.User.toCharArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 *	Authentication challenge is sent by a server upon receiving a login request.
 *
 * @author aabyodj
 */
public class AuthChallenge implements Transmittable {
	
	/** A random bytes to be processed into response by a client */
	final byte[] challenge;
	final byte[] salt;
	private char[] hash = null;

	AuthChallenge(byte[] challenge, byte[] salt) {
		this.challenge = challenge;
		this.salt = salt;
	}

	public AuthResponse createResponse(AuthChallenge previous) 
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		if (previous != this) hash = previous.hash;
		byte[] result = createHash(hash, challenge);		
		return new AuthResponse(result);		
	}
	
	public AuthResponse createResponse(char[] password) 
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		hash = toCharArray(createHash(password, salt));
		return createResponse(this);
	}

	@Override
	public void transmit(OutputStream out) throws IOException {	
		byte[] signature = toBytes(AUTH_CHALLENGE);
		int length = (Integer.BYTES * 2) //challenge.length, salt.length
				+ challenge.length + salt.length;
		byte[] result = new byte[signature.length + Integer.BYTES + length];
		int pos = 0;
		System.arraycopy(signature, 0, result, pos, signature.length);
		pos += signature.length;
		System.arraycopy(toBytes(length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(toBytes(challenge.length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(challenge, 0, result, pos, challenge.length);
		pos += challenge.length;
		System.arraycopy(toBytes(salt.length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(salt, 0, result, pos, salt.length);
		
		out.write(result);
	}

	public static AuthChallenge receive(InputStream in) throws IOException {
		int length = receiveInt(in);
		byte[] buffer = receiveBytes(in, length);
		int pos = 0;
		int challengeLength = getInt(buffer, pos);
		pos += Integer.BYTES;
		byte[] challenge = new byte[challengeLength];
		System.arraycopy(buffer, pos, challenge, 0, challengeLength);
		pos += challengeLength;
		int saltLength = getInt(buffer, pos);
		pos += Integer.BYTES;
		byte salt[] = new byte[saltLength];
		System.arraycopy(buffer, pos, salt, 0, saltLength);
				
		return new AuthChallenge(challenge, salt);
	}
}

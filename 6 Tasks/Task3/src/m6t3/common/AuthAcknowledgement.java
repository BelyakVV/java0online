package m6t3.common;

import static m6t3.common.Const.INVALID_ID;
import static m6t3.common.Tranceiver.AUTH_ACKNOWLEDGEMENT;
import static m6t3.common.Tranceiver.getInt;
import static m6t3.common.Tranceiver.receiveBytes;
import static m6t3.common.Tranceiver.receiveInt;
import static m6t3.common.Tranceiver.toBytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *	Acknowledgement must be sent by server to client upon receiving challenge 
 * response from it. 
 *
 * @author aabyodj
 */
public class AuthAcknowledgement implements Transmittable {
	
	public final int userId;
	
	/** Whether just authenticated user has administrative rights */
	public final boolean admin;

	/**
	 * Create an acknowledge.
	 * @param user Associated account or null if login has failed 
	 */
	public AuthAcknowledgement(User user) {
		if (null == user) {
			
			//Access denied
			userId = INVALID_ID;
			admin = false;
		} else {
			
			//Access granted
			userId = user.id;
			admin = user.isAdmin();
		}
	}
	
	private AuthAcknowledgement(int userId, boolean admin) {
		this.userId = userId;
		this.admin = admin; 
	}

	@Override
	public void transmit(OutputStream out) throws IOException {
		byte[] signature = toBytes(AUTH_ACKNOWLEDGEMENT);
		int length = Integer.BYTES * 2; //userId, admin
		byte[] result = new byte[signature.length + Integer.BYTES + length];
		int pos = 0;
		System.arraycopy(signature, 0, result, pos, signature.length);
		pos += signature.length;
		System.arraycopy(toBytes(length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(toBytes(userId), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(toBytes(admin ? 1 : 0), 0, result, pos, Integer.BYTES);
		
		out.write(result);
	}

	public static AuthAcknowledgement receive(InputStream in) throws IOException {
		int length = receiveInt(in);
		byte[] buffer = receiveBytes(in, length);
		int pos = 0;
		int userId = getInt(buffer, pos);
		pos += Integer.BYTES;
		boolean admin = (0 == getInt(buffer, pos)) ? false : true;
				
		return new AuthAcknowledgement(userId, admin);
	}
}

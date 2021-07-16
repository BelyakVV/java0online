package m6t3.common;

import static m6t3.common.Tranceiver.LOGIN_REQUEST;
import static m6t3.common.Tranceiver.getInt;
import static m6t3.common.Tranceiver.receiveBytes;
import static m6t3.common.Tranceiver.receiveInt;
import static m6t3.common.Tranceiver.toBytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *	Login request must be sent by a client to a server immediately upon 
 * connection, otherwise the connection must be withdrawn by the server.
 * 
 * @author aabyodj
 */
public class LoginRequest implements Transmittable {
	public final String login;
	
	public LoginRequest(String login) {
		this.login = login;
	}

	@Override
	public void transmit(OutputStream out) throws IOException {	
		byte[] signature = toBytes(LOGIN_REQUEST);
		byte[] login = this.login.getBytes();
		int length = Integer.BYTES	+ login.length;
		byte[] result = new byte[signature.length + Integer.BYTES + length];
		int pos = 0;
		System.arraycopy(signature, 0, result, pos, signature.length);
		pos += signature.length;
		System.arraycopy(toBytes(length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(toBytes(login.length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(login, 0, result, pos, login.length);		
		out.write(result);
	}

	public static LoginRequest receive(InputStream in) throws IOException {
		int length = receiveInt(in);
		byte[] buffer = receiveBytes(in, length);
		int pos = 0;
		int loginLength = getInt(buffer, pos);	
		pos += Integer.BYTES;
		String login = new String(buffer, pos, loginLength);				
		return new LoginRequest(login);
	}
}

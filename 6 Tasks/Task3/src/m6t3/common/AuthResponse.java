package m6t3.common;

import static m6t3.common.Tranceiver.AUTH_RESPONSE;
import static m6t3.common.Tranceiver.getInt;
import static m6t3.common.Tranceiver.receiveBytes;
import static m6t3.common.Tranceiver.receiveInt;
import static m6t3.common.Tranceiver.toBytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *	Client response to a server authentication challenge.
 *
 * @author aabyodj
 */
public class AuthResponse implements Transmittable {
	
	final byte[] response;

	AuthResponse(byte[] response) {
		this.response = response;
	}

	@Override
	public void transmit(OutputStream out) throws IOException {
		byte[] signature = toBytes(AUTH_RESPONSE);
		int length = Integer.BYTES + response.length;
		byte[] result = new byte[signature.length + Integer.BYTES + length];
		int pos = 0;
		System.arraycopy(signature, 0, result, pos, signature.length);
		pos += signature.length;
		System.arraycopy(toBytes(length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(toBytes(response.length), 0, result, pos, Integer.BYTES);
		pos += Integer.BYTES;
		System.arraycopy(response, 0, result, pos, response.length);
		
		out.write(result);
	}

	public static AuthResponse receive(InputStream in) throws IOException {
		int length = receiveInt(in);
		byte[] buffer = receiveBytes(in, length);
		int pos = 0;
		int responseLength = getInt(buffer, pos);
		pos += Integer.BYTES;
		byte[] response = new byte[responseLength];
		System.arraycopy(buffer, pos, response, 0, responseLength);
				
		return new AuthResponse(response);
	}
}

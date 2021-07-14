package m6t3.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Tranceiver {
	
	public static final int LOGIN_REQUEST = signatureToInt("LOGN");
	public static final int AUTH_CHALLENGE = signatureToInt("CHLG");
	public static final int AUTH_RESPONSE = signatureToInt("RSPS");
	public static final int AUTH_ACKNOWLEDGEMENT = signatureToInt("ACKN");
	
	public static final int STUDENTS_CHECKSUM = signatureToInt("CHCK");
	public static final int SEND_ALL_STUDENTS = signatureToInt("STDS");
	public static final int SEND_STUDENT = signatureToInt("STUD");
	public static final int STOP = signatureToInt("STOP");
	public static final int SYNC_STUDENTS_REQUEST = signatureToInt("SNCS");
	
	public static final int NEW_USER = signatureToInt("NUSR");
	public static final int SEND_ALL_USERS = signatureToInt("USRS");
	public static final int SEND_USER = signatureToInt("USER");
	public static final int SYNC_USERS_REQUEST = signatureToInt("SNCU");	

	public static final int CHANGE_PASS_REQUEST = signatureToInt("CHPS");
	
	public static final long SYNC_INTERVAL = 3000;
	
	public static int signatureToInt(String str) {
		return ByteBuffer.wrap(str.getBytes()).getInt();
	}
	
	public static Long createMessage(int signature, int data) {
		return signature | (((long) data) << Integer.SIZE);
	}
	
	public static byte[] toBytes(int n) {
		byte[] result = new byte[Integer.BYTES];
		for (int i = 0; i < Integer.BYTES; i++) {
			result[i] = (byte) n;
			n = n >> 8;
		}
		return result;
	}
	
	public static byte[] toBytes(long n) {
		byte[] result = new byte[Long.BYTES];
		for (int i = 0; i < Long.BYTES; i++) {
			result[i] = (byte) n;
			n = n >> 8;
		}
		return result;
	}
	
	public static int getInt(byte[] bytes, int start) {
		int result = 0;
		int end = start + Integer.BYTES - 1;
		for (int i = end; i >= start; i--) {
			result = (result << 8) | Byte.toUnsignedInt(bytes[i]);
		}
		return result;
	}
	
	public static int getInt(byte[] bytes) {
		return getInt(bytes, 0);
	}
	
	public static long getLong(byte[] bytes, int start) {
		long result = 0;
		int end = start + Long.BYTES - 1;
		for (int i = end; i >= start; i--) {
			result = (result << 8) | Byte.toUnsignedInt(bytes[i]);
		}
		return result;
	}
	
	public static long getLong(byte[] bytes) {
		return getLong(bytes, 0);
	}
	
	public static byte[] receiveBytes(InputStream in, int len) throws IOException {
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			int x = in.read();
			if (x < 0) {
				throw new IOException("End of stream reached");
			} else {
				result[i] = (byte) x;
			}
		}
		return result;
	}
	
	public static int receiveInt(InputStream in) throws IOException {
		byte[] bytes = receiveBytes(in, Integer.BYTES);
		return getInt(bytes);
	}
	
	public static long receiveLong(InputStream in) throws IOException {
		byte[] bytes = receiveBytes(in, Long.BYTES);
		return getLong(bytes);
	}
	
	public static void transmitInt(int n, OutputStream out) throws IOException {
		out.write(toBytes(n));
	}	
	
	public static void transmitLong(long n, OutputStream out) throws IOException {
		out.write(toBytes(n));
	}
}

package m6t3.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class Tranciever {
	
	public static final int CHECKSUM = signatureToInt("CHCK");
	public static final int SEND_ALL = signatureToInt("BULK");
	public static final int SEND_STUDENT = signatureToInt("STUD");
	public static final int STOP = signatureToInt("STOP");
	public static final int SYNC_REQUEST = signatureToInt("SYNC");
	public static final long SYNC_INTERVAL = 3000;
	
	public static int signatureToInt(String str) {
		return ByteBuffer.wrap(str.getBytes()).getInt();
	}
	
	public static int recieveInt(InputStream in) throws IOException {
		byte[] bytes = new byte[4];
		if (in.read(bytes) < 4) {
			in.close();
			throw new IOException("End of stream reached");
		}
		int result = 0;
		for (int i = 3; i >= 0; i--) {
			result = (result << 8) + bytes[i];
		}
		return result;
	}
	
	public static void transmitInt(int n, OutputStream out) throws IOException {
//		System.out.print("transmitInt: " + n + " ");
		byte[] result = new byte[4];
		for (int i = 0; i < 4; i++) {
			result[i] = (byte) n;
			n = n >> 8;
		}
		out.write(result);
	}
	
	public static Student recieveStudent(InputStream in) throws IOException {
		int id = recieveInt(in);
		int serial = recieveInt(in);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String number = br.readLine();
		String surname = br.readLine();
		String name = br.readLine();
		String patronymic = br.readLine();
		return new Student(id, serial, number, surname, name, patronymic);
	}
	
	public static void transmitStudent(Student student, OutputStream out) throws IOException {
		System.out.println("Отправка студента");
		transmitInt(SEND_STUDENT, out);
		transmitInt(student.id, out);
		transmitInt(student.getSerial(), out);
		PrintStream ps = new PrintStream(out);
		ps.println(student.getNumber());
		ps.println(student.getSurname());
		ps.println(student.getName());
		ps.println(student.getPatronymic());
		ps.flush();
		out.flush();
	}
	
	public static LinkedList<Student> recieveStudentsList(InputStream in) throws IOException {
		LinkedList<Student> result = new LinkedList<>();
		int signature = recieveInt(in);
		while (signature == SEND_STUDENT) {
			result.add(recieveStudent(in));
			signature = recieveInt(in);
		}
		if (signature != STOP) {
			in.close();
		}
		return result;
	}
}

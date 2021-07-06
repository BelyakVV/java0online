package m6t3.common;

import static m6t3.common.Const.*;
import static m6t3.common.Tranceiver.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Student implements Transmittable, XMLable {
	
	public final int id;
	private int serial;
	private String number;
	private String surname;
	private String name;
	private String patronymic;
	
	/**
	 * @param number
	 * @param surname
	 * @param name
	 * @param patronymic
	 */
	public Student(int id, int serial, String number, String surname, String name, String patronymic) {
		this.id = id;
		this.serial = serial;
		this.number = number;
		this.surname = surname;
		this.name = name;
		this.patronymic = patronymic;
	}	
	
	public Student(int id, Student student) {
		this.id = id;
		serial = 0;
		number = student.number;
		surname = student.surname;
		name = student.name;
		patronymic = student.patronymic;
	}
	
	public Student() {
		id = INVALID_ID;	
		serial = 1;
		number = "";
		surname = "";
		name = "";
		patronymic = "";
	}

	public int getSerial() {
		return serial;
	}
	
	public void incSerial() {
		serial++;
	}
	
	public String getNumber() {
		return number;
	}
	
	public boolean setNumber(String number) {
		number = number.strip();
		if (number.isEmpty()) return false;
		if (number.contentEquals(this.number)) return true;
		this.number = number;
		serial++;
		return true;
	}

	public String getSurname() {
		return surname;
	}

	public boolean setSurname(String surname) {
		surname = surname.strip();
		if (surname.isEmpty()) return false;
		if (surname.contentEquals(this.surname)) return true;
		this.surname = surname;
		serial++;
		return true;
	}

	public String getName() {
		return name;
	}

	public boolean setName(String name) {
		name = name.strip();
		if (name.isEmpty()) return false;
		if (name.contentEquals(this.name)) return true;
		this.name = name;
		serial++;
		return true;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public boolean setPatronymic(String patronymic) {
		patronymic = patronymic.strip();
		if (patronymic.isEmpty()) return false;
		if (patronymic.contentEquals(this.patronymic)) return true;
		this.patronymic = patronymic;
		serial++;
		return true;
	}

	public String getFullName() {
		return surname + ' ' + name + ' ' + patronymic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((patronymic == null) ? 0 : patronymic.hashCode());
		result = prime * result + serial;
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
//		StringBuilder sb = new StringBuilder(" id=");
//		sb.append(id).append(", serial=");
//		sb.append(serial).append(", hash=");
//		sb.append(Integer.toHexString(result));
//		System.out.println(sb.toString());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (patronymic == null) {
			if (other.patronymic != null)
				return false;
		} else if (!patronymic.equals(other.patronymic))
			return false;
		if (serial != other.serial)
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", serial=" + serial + ", number=" + number + ", surname=" + surname + ", name="
				+ name + ", patronymic=" + patronymic + "]";
	}

	public boolean update(Student upd) {
		if (upd.serial > serial) {
			serial = upd.serial;
			number = upd.number;
			surname = upd.surname;
			name = upd.name;
			patronymic = upd.patronymic;
			return true;
		}
		return false;
	}

	public Student suicide() {
		serial = INVALID_SERIAL;
		return this;
	}
	
	public static int calcChecksum(Iterable<Student> students) {
		int result = 0;
		for (var student: students) {
			result += student.serial;
		}
		return result;
	}

	@Override
	public void transmit(OutputStream out) throws IOException {
//		System.out.println("Отправка студента");
		byte[] signature = toBytes(SEND_STUDENT);
		byte[] number = this.number.getBytes();
		byte[] surname = this.surname.getBytes();
		byte[] name = this.name.getBytes();
		byte[] patronymic = this.patronymic.getBytes();	
		//length of data packet
		int len = (Integer.BYTES * (2 + 4)) //id, serial + number.length, surname.length, name.length, patronymic.length
				+ number.length + surname.length + name.length + patronymic.length;
		byte[] result = new byte[signature.length + Integer.BYTES + len];
		int i = 0; //position in result array
		System.arraycopy(signature, 0, result, i, signature.length);
		i += signature.length;
		System.arraycopy(toBytes(len), 0, result, i, Integer.BYTES);
		i += Integer.BYTES;
		System.arraycopy(toBytes(id), 0, result, i, Integer.BYTES);
		i += Integer.BYTES;
		System.arraycopy(toBytes(serial), 0, result, i, Integer.BYTES);
		i += Integer.BYTES;
		System.arraycopy(toBytes(number.length), 0, result, i, Integer.BYTES);
		i += Integer.BYTES;
		System.arraycopy(number, 0, result, i, number.length);
		i += number.length;
		System.arraycopy(toBytes(surname.length), 0, result, i, Integer.BYTES);
		i += Integer.BYTES;
		System.arraycopy(surname, 0, result, i, surname.length);
		i += surname.length;
		System.arraycopy(toBytes(name.length), 0, result, i, Integer.BYTES);
		i += Integer.BYTES;
		System.arraycopy(name, 0, result, i, name.length);
		i += name.length;
		System.arraycopy(toBytes(patronymic.length), 0, result, i, Integer.BYTES);
		i += Integer.BYTES;
		System.arraycopy(patronymic, 0, result, i, patronymic.length);
		out.write(result);
	}
	
	public static Student receive(InputStream in) throws IOException {
//		System.out.println("Приём студента");
		int len = receiveInt(in);
		byte[] buffer = receiveBytes(in, len);
		int pos = 0;
		int id = getInt(buffer, pos);
		pos += Integer.BYTES;
		int serial = getInt(buffer, pos);
		pos += Integer.BYTES;	
		int numLen = getInt(buffer, pos);
		pos += Integer.BYTES;	
		String number = new String(buffer, pos, numLen);
		pos += numLen; 
		int surLen = getInt(buffer, pos);
		pos += Integer.BYTES;	
		String surname = new String(buffer, pos, surLen);
		pos += surLen; 
		int nameLen = getInt(buffer, pos);
		pos += Integer.BYTES;	
		String name = new String(buffer, pos, nameLen);
		pos += nameLen; 
		int patLen = getInt(buffer, pos);
		pos += Integer.BYTES;	
		String patronymic = new String(buffer, pos, patLen);
		return new Student(id, serial, number, surname, name, patronymic);
	}

	@Override
	public Element toXML(Document xmlDoc) {
		Element result = xmlDoc.createElement("student");
		result.setAttribute("id", Integer.toString(id));
		result.setAttribute("serial", Integer.toString(serial));
		result.setAttribute("number", number);
		result.setAttribute("surname", surname);
		result.setAttribute("name", name);
		result.setAttribute("patronymic", patronymic);
		return result;
	}
	
	public static Student fromXML(Element elem) {
		int id = Integer.parseInt(elem.getAttribute("id"));
		int serial = Integer.parseInt(elem.getAttribute("serial"));
		String number = elem.getAttribute("number");
		String surname = elem.getAttribute("surname");
		String name = elem.getAttribute("name");
		String patronymic = elem.getAttribute("patronymic");
		return new Student(id, serial, number, surname, name, patronymic);
	}
}

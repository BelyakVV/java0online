package m6t3.common;

import static m6t3.common.Const.INVALID_SERIAL;

public class Student {
	
	public final int id;
	int serial;
	String number;
	String surname;
	String name;
	String patronymic;
	
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
		id = -1;	
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
	
	public void setNumber(String number) {
		this.number = number.strip();
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname.strip();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.strip();
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic.strip();
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
}

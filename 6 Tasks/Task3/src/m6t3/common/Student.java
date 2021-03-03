package m6t3.common;

public class Student {
	public static final int INVALID_ID = -1;
	public static final int INVALID_SERIAL = -1;
	
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
}

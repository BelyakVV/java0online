package m6t3.server;

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
	
	public int getSerial() {
		return serial;
	}
	
	public void incSerial() {
		serial++;
	}

	public String getFullName() {
		return surname + ' ' + name + ' ' + patronymic;
	}
}

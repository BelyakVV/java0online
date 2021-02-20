package m6t3.server;

public class Student {
//	final int id;
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
	public Student(String number, String surname, String name, String patronymic) {
		this.number = number;
		this.surname = surname;
		this.name = name;
		this.patronymic = patronymic;
	}	
}

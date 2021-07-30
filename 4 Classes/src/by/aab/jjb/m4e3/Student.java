package by.aab.jjb.m4e3;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Создайте класс с именем Student, содержащий поля: фамилия и инициалы, номер
 * группы, успеваемость (массив из пяти элементов). Создайте массив из десяти
 * элементов такого типа. Добавьте возможность вывода фамилий и номеров групп
 * студентов, имеющих оценки, равные только 9 или 10.
 * 
 * @author aabyodj
 */
public class Student {
	public static final int MARKS_ARRAY_SIZE = 5;

	static final Student[] STUDENTS = new Student[] { 
			new Student("Иванов И.И.", 1, new int[] { 1, 2, 3, 4, 5 }),
			new Student("Петров П.П.", 1, new int[] { 6, 7, 8, 9, 10 }),
			new Student("Сидоров С.С.", 1, new int[] { 9, 10, 9, 10, 9 }),
			new Student("Борисов Б.Б.", 2, new int[] { 10, 9, 10, 9, 10 }),
			new Student("Фёдоров Ф.Ф.", 2, new int[] { 6, 7, 8, 9, 10 }),
			new Student("Александров А.А.", 2, new int[] { 9, 9, 9, 9, 9 }),
			new Student("Денисов Д.Д.", 3, new int[] { 6, 7, 8, 9, 10 }),
			new Student("Юрьев Ю.Ю.", 3, new int[] { 6, 7, 8, 9, 10 }),
			new Student("Захаров З.З.", 3, new int[] { 6, 7, 8, 9, 10 }),
			new Student("Яковлев Я.Я.", 3, new int[] { 10, 10, 10, 10, 10 }) };

	private String name;
	private int group;
	private final int[] marks = new int[MARKS_ARRAY_SIZE];

	public Student(String name, int group, int[] marks) {
		this.name = name;
		this.group = group;
		System.arraycopy(marks, 0, this.marks, 0, MARKS_ARRAY_SIZE);
	}

	public boolean isExcellent() {
		for (int mark : marks) {
			if (mark < 9)
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("ФИО: ").append(name).append(", группа: ").append(group);
		return result.toString();
	}

	public static void main(String[] args) {
		System.out.println("Список отличников:");
		printCollection(selectExcellents(STUDENTS));
	}

	public static Collection<Student> selectExcellents(Student[] students) {
		Collection<Student> result = new LinkedList<>();
		for (var student : students) {
			if (student.isExcellent())
				result.add(student);
		}
		return result;
	}

	public static void printCollection(Collection collection) {
		if (collection.isEmpty()) {
			System.out.println("пусто");
		}
		for (var elem : collection) {
			System.out.println(elem);
		}
	}
}

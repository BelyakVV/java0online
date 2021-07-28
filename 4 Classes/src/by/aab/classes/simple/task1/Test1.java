package by.aab.classes.simple.task1;

/**
 * Создайте класс Test1 двумя переменными. Добавьте метод вывода на экран и
 * методы изменения этих переменных. Добавьте метод, который находит сумму
 * значений этих переменных, и метод, который находит наибольшее значение из
 * этих двух переменных.
 * 
 * @author aabyodj
 */
public class Test1 {
	
	private int a;
	private int b;
	
	public void printAll() {
		System.out.print("a = " + a + ", b = " + b);
	}
	
	public void printA() {
		System.out.print(a);
	}
	
	public void setA(int value) {
		a = value;
	}
	
	public void printB() {
		System.out.print(b);
	}
	
	public void setB(int value) {
		b = value;
	}

	public int sum() {
		return a + b;
	}
	
	public int max() {
		return a > b ? a : b;
	}
}

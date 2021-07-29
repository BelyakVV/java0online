package by.aab.jjb.m4e5;

public class Main {

	public static void main(String[] args) {
		System.out.println("Значение по умолчанию:");
		DecimalCounter counter = new DecimalCounter();
		System.out.println(counter);
		System.out.println("Значение в виде int: " + counter.toInt());
		System.out.println();
		
		System.out.print("Увеличить на единицу: ");
		counter.inc();
		System.out.println(counter.toInt());
		
		System.out.print("Уменьшить на единицу: ");
		counter.dec();
		System.out.println(counter.toInt() + System.lineSeparator());

		System.out.println("Инициализация произвольным значением:");
		counter = new DecimalCounter(-5, -15, 42);
		System.out.println(counter);
		System.out.println("Значение в виде int: " + counter.toInt());
		System.out.println();
		
		System.out.print("Увеличить на единицу: ");
		counter.inc();
		System.out.println(counter.toInt());
		
		System.out.print("Уменьшить на единицу: ");
		counter.dec();
		System.out.println(counter.toInt() + System.lineSeparator());
		
		System.out.println("Попытка выйти за нижнюю границу:");
		counter.setValue(counter.getFloor().toInt());
		System.out.println("Новое значение: " + counter.toInt());
		System.out.print("Уменьшить на единицу: ");
		counter.dec();
		System.out.println(counter.toInt() + System.lineSeparator());
		
		System.out.println("Попытка выйти за верхнюю границу:");
		counter.setValue(counter.getCeiling().toInt());
		System.out.println("Новое значение: " + counter.toInt());
		System.out.print("Увеличить на единицу: ");
		counter.inc();
		System.out.println(counter.toInt());
	}
}

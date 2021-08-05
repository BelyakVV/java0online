package by.aab.jjb.m4e8;

import static by.aab.console.ConIO.readLong;

public class Main {
	private static int c = 0;
	static final Customer[] CUSTOMERS_ARRAY = new Customer[] {
			new Customer(c++, "Котов", "Владимир", "Михайлович", "ул.Котовского, 1",
					1000_0000_0000_0000L, "BY34 MMBN 3012 0000 0000 0933 0000"),
			new Customer(c++, "Иванов", "Иван", "Иванович", "ул.Ивановская, 7",
					1000_0000_0000_0001L, "BY34 MMBN 3012 0000 0001 0933 0000"),
			new Customer(c++, "Петров", "Пётр", "Петрович", "ул.Петровская, 9",
					0000_0000_0000_0002L, "BY34 MMBN 3012 0000 0002 0933 0000"),
			new Customer(c++, "Юрьев", "Юрий", "Юрьевич", "ул.Юрьевская, 2",
					1000_0000_0000_0003L, "BY34 MMBN 3012 0000 0003 0933 0000"),
			new Customer(c++, "Иванов", "Георгий", "Никитич", "ул.Никитская, 4",
					0000_0000_0000_0004L, "BY34 MMBN 3012 0000 0004 0933 0000"),
			new Customer(c++, "Иванов", "Георгий", "Георгиевич", "ул.Георгиевская, 3",
					1000_0000_0000_0005L, "BY34 MMBN 3012 0000 0005 0933 0000")
	};
	
	public static void main(String[] args) {
		Aggregator customers = new Aggregator(CUSTOMERS_ARRAY);
		
		System.out.println("Список покупателей в алфавитном порядке:");
		System.out.println(customers.sort(Main::compareAlphabetically));
		System.out.println();
		
		System.out.println("Список покупателей, у которых номер кредитной карточки находится в заданном интервале:");
		long floor = readLong("Введите нижнюю границу: ");
		long ceiling = readLong("Введите верхнюю границу: ");
		System.out.println(customers.selectByCardRange(floor, ceiling));
	}

	private static int compareAlphabetically(Customer c1, Customer c2) {
		int result = c1.getSurname().compareToIgnoreCase(c2.getSurname());
		if (result != 0) return result;
		result = c1.getName().compareToIgnoreCase(c2.getName());
		if (result != 0) return result;
		result = c1.getPatronymic().compareToIgnoreCase(c2.getPatronymic());
		return result;
	}
}
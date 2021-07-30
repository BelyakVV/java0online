package by.aab.jjb.m4e4;

import static by.aab.console.ConIO.readInt;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Создайте класс Train, содержащий поля: название пункта назначения, номер
 * поезда, время отправления. Создайте данные в массив из пяти элементов типа
 * Train, добавьте возможность сортировки элементов массива по номерам поездов.
 * Добавьте возможность вывода информации о поезде, номер которого введен
 * пользователем. Добавьте возможность сортировки массив по пункту назначения,
 * причем поезда с одинаковыми пунктами назначения должны быть упорядочены по
 * времени отправления.
 * 
 * @author aabyodj
 */
public class Train {
	static final Train[] TRAINS = new Train[]{
			new Train("Брест", 1, "09:00"),
			new Train("Витебск", 3, "16:20"),
			new Train("Гомель", 2, "12:15"),
			new Train("Гродно", 4, "14:15"),
			new Train("Витебск", 5, "10:10")
	};
	
	private String destination;
	private int number;
	private LocalTime departure;
	
	public Train(String destination, int number, LocalTime departure) {
		this.destination = destination;
		this.number = number;
		this.departure = departure;
	}

	public Train(String destination, int number, String departure) {
		this.destination = destination;
		this.number = number;
		this.departure = LocalTime.parse(departure);
	}	
	
	@Override
	public String toString() {
		StringBuilder result =  new StringBuilder("№ ").append(number)
				.append(", пункт назначения: ").append(destination)
				.append(", отправление: ").append(departure);
		return result.toString();
	}

	public static void main(String[] args) {
		System.out.println("Сортировка по номерам:");
		printCollection(sort(selectAll(), Train::compareByNumber));
		System.out.println();
		
		int myNumber = readInt("Введите номер поезда: ");
		printCollection(selectByNumber(myNumber));
		System.out.println();

		System.out.println("Сортировка по пункту назначения и времени отправления:");
		printCollection(sort(selectAll(), Train::compareByDestThenByDeparture));		
	}

	public static Collection<Train> selectAll() {
		return Arrays.asList(TRAINS);
	}
	
	public static Collection<Train> selectByNumber(int number) {
		Collection<Train> result = new LinkedList<>();
		for (Train train: TRAINS) {
			if (train.number == number) result.add(train);
		}
		return result;
	}
	
	public static Collection<Train> sort(Collection<Train> trains, Comparator<Train> comparator) {
		if (trains instanceof List) {
			((List<Train>) trains).sort(comparator);
			return trains;
		} else {
			Collection<Train> result = new TreeSet<>(comparator);
			result.addAll(trains);
			return result;
		}
	}
	
	private static int compareByNumber(Train tr1, Train tr2) {
		return Integer.compare(tr1.number, tr2.number);
	}
	
	private static int compareByDestThenByDeparture(Train tr1, Train tr2) {
		int result = tr1.destination.compareToIgnoreCase(tr2.destination);
		if (0 == result) result = tr1.departure.compareTo(tr2.departure);
		return result;
	}
	
	public static Train[] sortByDestThenByDeparture(Train[] trains) {
		Arrays.sort(trains, (t1, t2) -> {
			int result = t1.destination.compareToIgnoreCase(t2.destination);
			if (0 == result) result = t1.departure.compareTo(t2.departure);
			return result;
		});
		return trains;
	}
	
	public static void printCollection(Collection collection) {
		if (collection.isEmpty()) System.out.println("пусто");
		for (var elem: collection) {
			System.out.println(elem);
		}
	}
}

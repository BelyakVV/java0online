package by.aab.classes.simple.task4;

import static by.aab.console.ConIO.readInt;

import java.time.LocalTime;
import java.util.Arrays;

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
	
	public static Train[] sortByNumber(Train[] trains) {
		Arrays.sort(trains, (t1, t2) -> Integer.compare(t1.number, t2.number));
		return trains;
	}
	
	public static Train[] sortByDestThenByDeparture(Train[] trains) {
		Arrays.sort(trains, (t1, t2) -> {
			int result = t1.destination.compareToIgnoreCase(t2.destination);
			if (0 == result) result = t1.departure.compareTo(t2.departure);
			return result;
		});
		return trains;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("№ ").append(number)
				.append(", пункт назначения: ").append(destination)
				.append(", отправление: ").append(departure).toString();
	}

	public static void main(String[] args) {
		System.out.println("Сортировка по номерам:");
		printTrains(sortByNumber(TRAINS));
		System.out.println();
		
		int myNumber = readInt("Введите номер поезда: ");
		System.out.println(getTrainByNumber(TRAINS, myNumber));
		System.out.println();

		System.out.println("Сортировка по пункту назначения и времени отправления:");
		printTrains(sortByDestThenByDeparture(TRAINS));		
	}

	public static void printTrains(Train[] trains) {
		for (Train train: trains) {
			System.out.println(train);
		}
	}

	public static Train getTrainByNumber(Train[] trains, int number) {
		for (Train train: trains) {
			if (train.number == number) return train;
		}
		return null;
	}
}

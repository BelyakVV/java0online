package by.aab.jjb.m4e10;

import static by.aab.console.ConIO.getChoice;
import static by.aab.console.ConIO.readLine;
import static by.aab.jjb.m4e10.Airline.DayOfWeek.FRI;
import static by.aab.jjb.m4e10.Airline.DayOfWeek.MON;
import static by.aab.jjb.m4e10.Airline.DayOfWeek.SAT;
import static by.aab.jjb.m4e10.Airline.DayOfWeek.SUN;
import static by.aab.jjb.m4e10.Airline.DayOfWeek.THU;
import static by.aab.jjb.m4e10.Airline.DayOfWeek.TUE;
import static by.aab.jjb.m4e10.Airline.DayOfWeek.WED;
import static by.aab.jjb.m4e10.Airline.Plane.BOEING;
import static by.aab.jjb.m4e10.Airline.Plane.TU;

import java.time.LocalTime;

import by.aab.jjb.m4e10.Aggregator.Filter;
import by.aab.jjb.m4e10.Airline.DayOfWeek;

public class Main {
	private static int c = 1;
	static final Airline[] AIRLINES_ARRAY = new Airline[] {
			new Airline("Стамбул", c++, BOEING, "09:10", new DayOfWeek[] { 
					MON, WED, FRI}),
			new Airline("Тбилиси", c++, BOEING, "11:00", new DayOfWeek[] { 
					TUE, THU, SAT}),
			new Airline("Москва", c++, TU, "18:30", new DayOfWeek[] { 
					TUE, THU, SUN}),
			new Airline("Каир", c++, BOEING, "12:55", new DayOfWeek[] { 
					MON, THU, SAT}),
			new Airline("Сочи", c++, TU, "15:15", new DayOfWeek[] { 
					TUE, FRI})
	};
	
	public static void main(String[] args) {
		Aggregator flights = new Aggregator(AIRLINES_ARRAY);
		
		System.out.println("Список рейсов для заданного пункта назначения");
		String destination = readLine("Введите пункт назначения> ");
		System.out.println(flights.select(new Filter(Main::checkDestination, destination)));

		System.out.println("Список рейсов для заданного дня недели");
		DayOfWeek[] days = DayOfWeek.values();
		DayOfWeek day = days[getChoice(days)];
		System.out.println(flights.select(new Filter(Main::checkDay, day)));

		System.out.println("Список рейсов для заданного дня недели, время вылета для которых больше заданного");
		day = days[getChoice(days)];
		LocalTime time = LocalTime.parse(readLine("Введите время (hh:mm)> "));
		System.out.println(flights.selectAnd(new Filter[] {
				new Filter(Main::checkDay, day),
				new Filter(Main::departsAfter, time)
		})); 
	}
	
	static boolean checkDestination(Airline flight, Object destination) {
		return flight.getDestination().equalsIgnoreCase((String) destination);
	}
	
	static boolean checkDay(Airline flight, Object day) {
		return flight.getDays().contains(day);
	}
	
	static boolean departsAfter(Airline flight, Object time) {
		return flight.getDeparture().isAfter((LocalTime) time);
	}
}

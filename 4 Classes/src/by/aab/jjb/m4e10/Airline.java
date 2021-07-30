package by.aab.jjb.m4e10;

import java.time.LocalTime;
import java.util.EnumSet;

/**
 * Создать класс Airline: пункт назначения, номер рейса, тип самолета, время
 * вылета, дни недели.
 * 
 * Определить конструкторы, set- и get- методы и метод toString().
 * 
 * @author aabyodj
 */
public class Airline {

	private String destination;
	private int flightNumber;
	private Plane plane;
	private LocalTime departure;
	private EnumSet<DayOfWeek> days;	
	
	public Airline() {
		destination = "";
		plane = Plane.UNKNOWN;
		departure = LocalTime.MIDNIGHT;
		days = EnumSet.noneOf(DayOfWeek.class);
	}
	
	public Airline(String destination, int flightNumber, Plane plane, LocalTime departure, EnumSet<DayOfWeek> days) {
		this.destination = destination;
		this.flightNumber = flightNumber;
		this.plane = plane;
		this.departure = departure;
		this.days = days;
	}	
	
	public Airline(String destination, int flightNumber, Plane plane, String departure, DayOfWeek[] days) {
		this.destination = destination;
		this.flightNumber = flightNumber;
		this.plane = plane;
		setDeparture(departure);
		this.days = EnumSet.noneOf(DayOfWeek.class);
		setDays(days);
	}	

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(int flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	public LocalTime getDeparture() {
		return departure;
	}

	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}

	public void setDeparture(String departure) {
		this.departure = LocalTime.parse(departure);
	}
	
	public EnumSet<DayOfWeek> getDays() {
		return days;
	}

	public void setDays(EnumSet<DayOfWeek> days) {
		this.days = days;
	}
	
	public void setDays(DayOfWeek[] days) {
		for (var day: days) {
			this.days.add(day);
		}
	}	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder()
			.append("№ ").append(flightNumber)
			.append("; назначение: ").append(destination)
			.append("; вылет: ").append(departure)
			.append("; дни: ").append(formatDays(days))
			.append("; самолёт: ").append(plane);
		return builder.toString();
	}

	private static StringBuilder formatDays(EnumSet<DayOfWeek> days) {
		if (days.isEmpty()) {
			return new StringBuilder("нет");
		}
		StringBuilder result = new StringBuilder();
		for (var day: days) {
			result.append(day.shortName).append(' ');
		}
		result.setLength(result.length() - 1);
		return result;
	}

	public enum Plane {
		UNKNOWN("неизвестно"), BOEING("Boeing"), TU("Ту-134");
		
		public final String name;
		
		private Plane(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum DayOfWeek {
		
		MON("понедельник", 	"пн"), 
		TUE("вторник", 		"вт"), 
		WED("среда", 		"ср"), 
		THU("четверг", 		"чт"), 
		FRI("пятница", 		"пт"), 
		SAT("суббота", 		"сб"), 
		SUN("воскресенье",  "вс");
		
		public final String fullName;
		public final String shortName;
		
		private DayOfWeek(String fullName, String shortName) {
			this.fullName = fullName;
			this.shortName = shortName;
		}
		
		@Override
		public String toString() {
			return fullName;
		}
	}
}

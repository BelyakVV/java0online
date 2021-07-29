package by.aab.jjb.m4e6;

/**
 * Составьте описание класса для представления времени. Предусмотрте возможности
 * установки времени и изменения его отдельных полей (час, минута, секунда) с
 * проверкой допустимости вводимых значений. В случае недопустимых значений
 * полей поле устанавливается в значение 0. Создать методы изменения времени на
 * заданное количество часов, минут и секунд.
 * 
 * @author aabyodj
 *
 */
public class Time {
	static final int SECONDS_PER_DAY = 60 * 60 * 24;
	static final int SECONDS_PER_HOUR = 60 * 60;
	
	private int hour;
	private int minute;
	private int second;
	
	public Time() {		
	}
	
	public Time (int hour, int minute, int second) {
		setAll(hour, minute, second);
	}
	
	public Time(int secondOfDay) {
		setAll(secondOfDay);
	}
	
	public int getHour() {
		return hour;
	}
	
	public boolean setHour(int hour) {
		if (hour >= 0 && hour < 24) {
			this.hour = hour;
			return true;
		} else {
			this.hour = 0;
			return false;
		}		
	}
	
	public int getMinute() {
		return minute;
	}
	
	public boolean setMinute(int minute) {
		if (minute >= 0 && minute < 60) {
			this.minute = minute;
			return true;
		} else {
			this.minute = 0;
			return false;
		}
	}
	
	public int getSecond() {
		return second;
	}
	
	public boolean setSecond(int second) {
		if (second >= 0 && second < 60) {
			this.second = second;
			return true;
		} else {
			this.second = 0;
			return false;
		}
	}	
	
	public boolean setAll(int hour, int minute, int second) {
		boolean result = setHour(hour);
		result &= setMinute(minute);
		result &= setSecond(second);
		return result;
	}
	
	public boolean setAll(int secondOfDay) {
		if (secondOfDay < 0 || secondOfDay >= SECONDS_PER_DAY) return totalFail();
		second = secondOfDay % 60;
		secondOfDay = secondOfDay / 60;
		minute = secondOfDay % 60;
		hour = secondOfDay / 60;
		return true;
	}
	
	public boolean plusHours(int hours) {
		if (hours > 23) return totalFail();
		return setAll(hours * SECONDS_PER_HOUR + toSecondOfDay());
	}
	
	public boolean minusHours(int hours) {
		return plusHours(-hours);
	}
	
	public boolean plusMinutes(int minutes) {
		return setAll(minutes * 60 + toSecondOfDay());
	}
	
	public boolean minusMinutes(int minutes) {
		return plusMinutes(-minutes);
	}
	
	public boolean plusSeconds(int seconds) {
		return setAll(seconds + toSecondOfDay());
	}
	
	public boolean minusSeconds(int seconds) {
		return plusSeconds(-seconds);
	}

	public int toSecondOfDay() {
		return SECONDS_PER_HOUR * hour + 60 * minute + second;
	}
	
	private boolean totalFail() {
		hour = 0;
		minute = 0;
		second = 0;
		return false;
	}
}

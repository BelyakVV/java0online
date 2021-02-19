package aabyodj.epamgrow.java0online.m6t2.util;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Date {
	
	static final Pattern DATE_PTRN = Pattern.compile("([+-]?\\d+)-(\\d{1,2})-(\\d{1,2})");
	
	public static String encode(long epochDay) {
		LocalDate result = LocalDate.ofEpochDay(epochDay);		
		return String.format("%d-%02d-%02d", result.getYear(), result.getMonthValue(), result.getDayOfMonth());			
	}
	
	public static long decode(String str) {
		Matcher matcher = DATE_PTRN.matcher(str);
		if (!matcher.find()) {
			throw new IllegalArgumentException("Неверный формат даты");
		}
		int year = Integer.parseInt(matcher.group(1));
		int month = Integer.parseInt(matcher.group(2));
		int dayOfMonth = Integer.parseInt(matcher.group(3));
		LocalDate date = LocalDate.of(year, month, dayOfMonth);
		return date.toEpochDay();
	}
}

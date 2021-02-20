package aabyodj.epamgrow.java0online.m6t2.util;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Преобразование даты между разными форматами
 * @author aabyodj
 */
public class Date {
	
	/** Паттерн для разбора даты в формате ГГГГ-ММ-ДД */
	static final Pattern DATE_PTRN = Pattern.compile("([+-]?\\d+)-(\\d{1,2})-(\\d{1,2})");
	
	/**
	 * Отформатировать дату в формате ММММ-ГГ-ДД
	 * @param epochDay число дней после 1 января 1970
	 * @return
	 */
	public static String encode(long epochDay) {
		LocalDate result = LocalDate.ofEpochDay(epochDay);		
		return String.format("%d-%02d-%02d", result.getYear(), result.getMonthValue(), result.getDayOfMonth());			
	}
	
	/**
	 * Преобразовать дату из формата ГГГГ-ММ-ДД в число дней после 1 января 1970
	 * @param str Дата в формате ГГГГ-ММ-ДД
	 * @return Число дней после 1 января 1970
	 */
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

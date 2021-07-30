package by.aab.console;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Console input/output
 * 
 * @author aabyodj
 */
public class ConIO {

	public static final Console CONSOLE = System.console();
	
	public static final BufferedReader IN = CONSOLE == null ? 
			new BufferedReader(new InputStreamReader(System.in)) : null; 
	
	private static final String MSG_SELECT = "Выберите:" 
			+ System.lineSeparator() + "---------";
	public static final String MSG_SELECTED = "Выбрано: ";
	public static final String MSG_BAD_CHOICE = "Нет такого варианта.";
	public static final int BAD_CHOICE = -1;
	
	public static int readInt(String hint) {
		return Integer.parseInt(readLine(hint));
	}
	
	public static long readLong(String hint) {
		return Long.parseLong(readLine(hint));
	}
	
	public static String readLine() {
		if (CONSOLE != null) return CONSOLE.readLine("");
		try {
			return IN.readLine();
		} catch (IOException e) {
			throw new IOError(e);
		}
	}
	
	public static String readLine(String hint) {
		if (CONSOLE != null) return CONSOLE.readLine(hint);
		if (!hint.isEmpty()) System.out.print(hint);
		try {
			return IN.readLine();
		} catch (IOException e) {
			throw new IOError(e);
		}
	}
	
	public static int getChoice(Object[] menu, boolean validRequired) {
		if (menu.length < 1) {
			if (validRequired) {
				throw new RuntimeException("Impossible to get valid index for empty menu");
			} else {
				return BAD_CHOICE;
			}
		}
		int result;
		do {
			System.out.println(MSG_SELECT);
			for (int i = 1; i <= menu.length; i++) {
				System.out.println(i + " - " + menu[i - 1]);
			}
			try {
				result = readInt("> ") - 1;
			} catch (Exception e) {
				result = BAD_CHOICE;
			}
			if (result >= 0 && result < menu.length) {
				System.out.println(MSG_SELECTED + menu[result]);
				return result;
			}
			System.out.println(MSG_BAD_CHOICE);
		} while (validRequired);
		return BAD_CHOICE;
	}
	
	public static int getChoice(Object[] menu) {
		return getChoice(menu, true);
	}
}

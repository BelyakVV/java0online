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
	private static final Console CONSOLE = System.console();
	private static final BufferedReader IN = CONSOLE == null ? 
			new BufferedReader(new InputStreamReader(System.in)) : null; 
	
	public static int readInt(String hint) {
		return Integer.parseInt(readLine(hint));
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
}

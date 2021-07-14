package m6t3.server;

import static m6t3.common.Const.INVALID_ID;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import m6t3.common.User;

public class ServerMain {

	public static final int DEFAULT_IP_PORT = 10000;
	public static final String DEFAULT_FILE_NAME = "data/archive.xml";
	
	static int port = DEFAULT_IP_PORT;
	static String fileName = DEFAULT_FILE_NAME;
	
	static SrvListener server;
	
	public static void main(String[] args) 
			throws IOException, ParserConfigurationException, InterruptedException {
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				//Оставить номер порта по умолчанию
			}
		}
		server = new SrvListener(port, fileName);
		server.start();
		if (server.data.noOtherAdmins(INVALID_ID)) {
			System.out.println("Ошибка: в базе нет ни одного администратора.");
			if (!createAdmin()) {
				System.out.println("Не удалось создать администратора. Сервер завершает работу.");
				server.close();
				return;
			}
		}
		System.out.println("Сервер успешно запущен на порту " + port);
		System.out.print("Нажмите ВВОД для завершения...");
		readLine();
		server.close();
		System.out.println("Сервер успешно остановлен");
	}

	private static boolean createAdmin() {
		System.out.println("Создание администратора");
		String login = readLine("Введите логин");
		if (login.isBlank()) {
			System.out.println("Ошибка: пустой логин не допускается.");
			return false;
		}
		char[] pass = readPassword("Введите пароль");
		if (pass.length < 1) {
			System.out.println("Ошибка: пустой пароль не допускается.");
			return false;
		}
		char[] passAgain = readPassword("Повторите ввод пароля");
		if (!Arrays.equals(pass, passAgain)) {
			System.out.println("Ошибка: пароли не совпадают.");
			return false;
		}
		try {
			return server.data.updateUser(User.createAdmin(login, pass));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private static String readLine(String hint) {
		Console console = ConsoleHelper.getConsoleInstance();
		if (console != null) {
			return console.readLine(hint + ": ");
		} else {
			return myReadLine(hint);
		}
	}

	private static String readLine() {
		return readLine("");		
	}

	private static char[] readPassword(String hint) {
		Console console = ConsoleHelper.getConsoleInstance();
		if (console != null) {
			return console.readPassword(hint + ": ");
		} else {
			return myReadLine(hint).toCharArray();
		}
	}

	private static String myReadLine(String hint) {
		if (!hint.isBlank()) {
			System.out.print(hint + ": ");
		}
		BufferedReader in = ConsoleHelper.getInputStreamReaderInstance();
		try {
			return in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private static class ConsoleHelper {
		private static final Console CONSOLE = System.console();
		private static final BufferedReader IN_READER = (null == CONSOLE) ?
				new BufferedReader(new InputStreamReader(System.in)) :
					null;
		
		private ConsoleHelper() {			
		}
		
		public static Console getConsoleInstance() {
			return CONSOLE;
		}
		
		public static BufferedReader getInputStreamReaderInstance() {
			return IN_READER;
		}
	}
}

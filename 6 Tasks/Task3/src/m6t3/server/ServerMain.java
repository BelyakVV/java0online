package m6t3.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

public class ServerMain {

	public static final int DEFAULT_IP_PORT = 10000;
	public static final String DEFAULT_FILE_NAME = "data/archive.xml";
	
	static int port = DEFAULT_IP_PORT;
	static String fileName = DEFAULT_FILE_NAME;
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, InterruptedException {
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				//Оставить номер порта по умолчанию
			}
		}
		SrvListener server = new SrvListener(port, fileName);
		server.start();
		System.out.println("Сервер успешно запущен на порту " + port);
		System.out.print("Нажмите ВВОД для завершения...");
		new BufferedReader(new InputStreamReader(System.in)).readLine();
		server.close();
		System.out.println("Сервер успешно остановлен");
	}

}

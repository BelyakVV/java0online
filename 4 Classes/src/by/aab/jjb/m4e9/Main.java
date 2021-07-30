package by.aab.jjb.m4e9;

import static by.aab.console.ConIO.readInt;
import static by.aab.console.ConIO.readLine;
import static by.aab.jjb.m4e9.Book.Binding.HARD;
import static by.aab.jjb.m4e9.Book.Binding.SOFT;

import by.aab.jjb.m4e9.Aggregator.Filter;

public class Main {
	static int c;
	static final Book[] BOOKS_ARRAY = new Book[] {
			
			new Book(c++, "Казкі", new String[] {"Вітка В."}, 
					"Мн: Мастацкая літаратура", 1976, 127, 101, HARD),
			
			new Book(c++, "Устройство и монтаж электрооборудования", 
					new String[] {"Гусев Н.", "Мельцер Н."}, 
					"Мн: Вышэйшая школа", 1979, 320, 65, HARD),
			
			new Book(c++, "Le avventure di Cipollino", 
					new String[] {"Rodari G."}, 
					"СПб.: КАРО", 2013, 336, 3500, SOFT)
	};

	public static void main(String[] args) {
		Aggregator books = new Aggregator(BOOKS_ARRAY);
		
		System.out.println("Список книг заданного автора");
		String author = readLine("Введите автора: ");
		System.out.println(books.select(new Filter(Main::checkAuthor, author)));		

		System.out.println("Список книг, выпущенных заданным издательством");
		String publisher = readLine("Введите название издательства: ");
		System.out.println(books.select(new Filter(Main::checkPublisher, publisher)));

		System.out.println("Список книг, выпущенных после заданного года");
		int year = readInt("Введите год: ");
		System.out.println(books.select(new Filter(Main::isNewer, year)));		
	}
	
	static boolean checkAuthor(Book book, Object author) {
		for (var bookAuthor: book.getAuthors()) {
			if (bookAuthor.equalsIgnoreCase((String) author)) return true;
		}
		return false;
	}
	
	static boolean checkPublisher(Book book, Object publisher) {
		return book.getPublisher().equalsIgnoreCase((String) publisher);
	}
	
	static boolean isNewer(Book book, Object year) {
		return book.getYear() > (Integer) year;
	}
}

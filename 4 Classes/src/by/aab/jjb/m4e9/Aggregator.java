package by.aab.jjb.m4e9;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.BiFunction;

/**
 * Класс, агрегирующий массив типа Book
 * 
 * @author aabyodj
 */
public class Aggregator {

	private static final Book[] ZERO_ARRAY = new Book[0];
	private static final String EMPTY = "пусто" + System.lineSeparator();
	
	private Book[] books;

	public Aggregator() {
		books = ZERO_ARRAY;
	}

	public Aggregator(Book[] books) {
		this.books = books;
	}
	
	public Aggregator(Collection<Book> books) {
		this(books.toArray(ZERO_ARRAY));
	}
	
	public Aggregator select(Filter filter) {
		Collection<Book> result = new LinkedList<>();
		for (var book: books) {
			if (filter.check.apply(book, filter.criterion))
				result.add(book);
		}
		return new Aggregator(result);
	}
	
	@Override
	public String toString() {
		if (books.length < 1) return EMPTY;
		StringBuilder result = new StringBuilder();
		for (var book: books) {
			result.append(book).append(System.lineSeparator());
		}
		return result.toString();
	}
	
	public static class Filter {
		public final BiFunction<Book, Object, Boolean> check;
		public final Object criterion;
		
		public Filter(BiFunction<Book, Object, Boolean> check, Object criterion) {
			this.check = check;
			this.criterion = criterion;
		}		
	}
}

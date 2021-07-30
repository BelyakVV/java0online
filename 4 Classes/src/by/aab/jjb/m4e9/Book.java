package by.aab.jjb.m4e9;

/**
 * Класс Book: id, название, автор(ы), издательство, год издания, количество
 * страниц, цена, тип переплета.
 * 
 * Определить конструкторы, set- и get- методы и метод toString().
 * 
 * @author aabyodj
 */
public class Book {

	private int id;
	private String name;
	private String[] authors;
	private String publisher;
	private int year;
	private int pagesCount;
	private int price;
	private Binding binding;
	
	public Book() {
		name = "";
		authors = new String[0];
		publisher = "";
		binding = Binding.values()[0];
	}	
	
	public Book(int id, String name, String[] authors, String publisher, int year, int pagesCount, int price, Binding binding) {
		this.id = id;
		this.name = name;
		this.authors = authors;
		this.publisher = publisher;
		this.year = year;
		this.pagesCount = pagesCount;
		this.price = price;
		this.binding = binding;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getAuthors() {
		return authors;
	}

	public void setAuthors(String[] authors) {
		this.authors = authors;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}

	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public Binding getBinding() {
		return binding;
	}

	public void setBinding(Binding binding) {
		this.binding = binding;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder()
				.append("id: ").append(id)
				.append("; Название: ").append(name)
				.append("; авторы: ").append(authorsToSB())
				.append("; издатель: ").append(publisher)
				.append("; год: ").append(year)
				.append("; страницы: ").append(pagesCount)
				.append("; цена: ").append(price / 100).append(" р. ").append(price % 100)
				.append(" к.; переплёт: ").append(binding);
		return builder.toString();
	}

	private StringBuilder authorsToSB() {
		if (authors.length < 1) return new StringBuilder("не указано");
		StringBuilder result = new StringBuilder();
		for (var author: authors) {
			result.append(author).append(", ");
		}
		result.setLength(result.length() - 2);
		return result;
	}

	public enum Binding {
		UNKNOWN("не указано"), HARD("твёрдый"), SOFT("мягкий");
		
		private final String name;
		
		private Binding(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
}

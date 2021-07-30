package by.aab.jjb.m4a1;

import java.util.LinkedList;
import java.util.List;

/**
 * Создать объект класса Текст, используя классы Предложение, Слово.
 * 
 * Методы: дополнить текст, вывести на консоль текст, заголовок текста.
 * 
 * @author aabyodj
 */
public class Text {
	private Sentence title;
	private List<Sentence> body;
	
	public Text(Sentence title, List<Sentence> body) {
		this.title = title;
		this.body = body;
	}
	
	public Text() {
		this(new Sentence(), new LinkedList<>());
	}
	
	public void append(Sentence sentence) {
		body.add(sentence);
	}
	
	public void append(Word word) {
		if (body.isEmpty()) body.add(new Sentence());
		body.get(body.size() - 1).append(word);
	}
	
	public void printAll() {
		String result = toString();
		if (result.isBlank()) return;
		System.out.println(result);
	}
	
	public void printTitle() {
		StringBuilder result = titleToSB();
		if (result.length() == 0) return;
		System.out.println(result.toString());
	}

	private StringBuilder titleToSB() {
		StringBuilder result = title.toStringBuilder();

		// There is no period at the end of title
		if (result.length() > 0)
			result.setLength(result.length() - 1);

		return result;
	}
	
	public void printBody() {
		if (body.isEmpty()) return;
		System.out.println(bodyToSB().toString());
	}

	private StringBuilder bodyToSB() {
		StringBuilder result = new StringBuilder();
		for (var sentence: body) {
			result.append(sentence.toStringBuilder()).append(' ');
		}
		if (result.length() > 0) result.setLength(result.length() - 1);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = titleToSB();
		if (!body.isEmpty()) {
			if (result.length() > 0) {
				result.append(System.lineSeparator()).append(System.lineSeparator());
			}
			result.append(bodyToSB());
		}
		return result.toString();
	}
	
	public Sentence getTitle() {
		return title;
	}

	public void setTitle(Sentence title) {
		this.title = title;
	}

	public List<Sentence> getBody() {
		return body;
	}

	public void setBody(List<Sentence> body) {
		this.body = body;
	}	
}

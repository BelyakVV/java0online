package by.aab.jjb.m4a1;

import java.util.LinkedList;
import java.util.List;

public class Sentence {
	private List<Word> words;

	public Sentence(List<Word> words) {
		this.words = words;
	}
	
	public Sentence() {
		this(new LinkedList<>());
	}
	
	public void append(Word word) {
		words.add(word);
	}
	
	public StringBuilder toStringBuilder() {
		StringBuilder result = new StringBuilder();
		for (var word: words) {
			result.append(word).append(' ');
		}
		if (result.length() > 0) {
			result.setCharAt(result.length() - 1, '.');
		}
		return result;
	}
	
	@Override
	public String toString() {
		return toStringBuilder().toString();
	}
}

/**
 * 
 */
package aabyodj.epamgrow.java0online.m6t2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.mail.internet.AddressException;

/**
 * @author aabyodj
 *
 */
class Notepad {
	List<Note> notes;	//TODO: сделать деревом
	File file;
	boolean changed;	
	
	Notepad(List<Note> notes, File file, boolean changed) {
		this.notes = notes;
		this.file = file;
		this.changed = changed;
	}

	public Note add() {
		Note result = new Note();
		if (add(result)) return result;
		throw new RuntimeException(Const.COULD_NOT_ADD);
	}
	
	public boolean add(Note note) {
		boolean result = notes.add(note);
		if (result) {
			note.notepad = this;
		}
		return result;
	}
	
	public Note add(String subject, String email, String body) throws AddressException {
		Note result = new Note(subject, email, body);
		if (add(result)) {
			return result;
		}
		throw new RuntimeException(Const.COULD_NOT_ADD);
	}
	
	public boolean remove(Note note) {
		boolean result = notes.remove(note);
		if (result) {
			note.notepad = null;
		}
		return result;
	}
	
//	public Note remove(int i) {
//		Note result = notes.remove(i);
//		result.notepad = null;
//		return result;
//	}
	
	public Notepad selectBySubject(String regex) {
		if (regex.isBlank()) return this;
		Pattern pattern = Pattern.compile(regex);
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (pattern.matcher(note.subject).find()) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed);
	}
	
	public Notepad selectAfter(long date) {
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (Long.compare(note.created, date) > 0 ) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed);
	}
	
	public Notepad selectBefore(long date) {
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (Long.compare(note.created, date) < 0 ) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed);
	}
	
	public Notepad selectByEmail(String regex) {
		if (regex.isBlank()) return this;
		Pattern pattern = Pattern.compile(regex);
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (pattern.matcher(note.email.toString()).find()) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed);
	}
	
	public Notepad selectByBody(String regex) {
		if (regex.isBlank()) return this;
		Pattern pattern = Pattern.compile(regex);
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (pattern.matcher(note.body).find()) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed);
	}	
	
	void load() throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			notes = new LinkedList<>();
			String line;
			while ((line = in.readLine()) != null) {
				Matcher matcher = Const.NOTE_PTRN.matcher(line);
				if (matcher.find()) {
					try {
						notes.add(new Note(matcher, this));
					} catch (Exception e) {
						//Заметки с ошибками игнорируются
					}					
				}
			}
		}		
		changed = false;
	}
	
	void save() throws IOException {
		if (!changed) return;
		createIfNeeded(file);
		try (PrintStream out = new PrintStream(file)) {
			for (var note: notes) {
				out.println(note.encode());
			}
			changed = false;
		}		
	}

    /**
     * Создать файл в случае отсутствия
     * @param file Требуемый файл
     * @throws IOException 
     */
    public static void createIfNeeded(File file) throws IOException {
        if (file.exists()) {
            return;
        }
        File parent = file.getParentFile();
        if (parent != null) {
            if (!parent.exists()) {
                parent.mkdirs();
            } else if (!parent.isDirectory()) {
                throw new RuntimeException('\"' + parent.getCanonicalPath() + '\"'
                                           + " не является каталогом");
            }
        }
        file.createNewFile();
    }

    public static String maskMeta(String str) {
    	for (String meta: Const.REGEX_META) {
    		str = str.replaceAll(meta, meta);
    	}
    	return str;
    }
    
    static class Const {
    	static final Pattern NOTE_PTRN = Pattern.compile("(.*?);\\s*(\\d*)\\s*;(.*?);(.*)");
    	
    	static final String[] REGEX_META = new String[]{
    			"\\\\", "\\^", "\\.", "\\[", "\\]", "\\$", "\\(", "\\)", "\\*", "\\{", "\\}", "\\?", "\\+", "\\|"  
    	};
    	
    	static final String COULD_NOT_ADD = "Не удалось добавить заметку";
    }
}

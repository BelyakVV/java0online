package aabyodj.epamgrow.java0online.m6t2.notepad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aabyodj.console.Table;
import aabyodj.epamgrow.java0online.m6t2.util.Date;
import aabyodj.epamgrow.java0online.m6t2.util.Email.AddressException;


/**
 * @author aabyodj
 *
 */
public class Notepad {
	List<Note> notes;	//TODO: сделать деревом
	File file;
	boolean changed;
	int nextNoteId;
	
	public Notepad(String fileName) {
		file = new File(fileName).getAbsoluteFile();
		try {
			load();
		} catch (IOException e) {
			notes = new LinkedList<>();
			changed = true;
		}
	}
	
	Notepad(List<Note> notes, File file, boolean changed, int nextNoteId) {
		this.notes = notes;
		this.file = file;
		this.changed = changed;
		this.nextNoteId = nextNoteId;
	}

	public Note getNote(int id) {
		for (var note: notes) {
			if (note.id == id) {
				return note;
			}
		}
		throw new RuntimeException(Const.NOT_FOUND);
	}
	
	public Note add() {
		Note result = new Note(nextNoteId);
		if (add(result)) {
			return result;
		}
		throw new RuntimeException(Const.COULD_NOT_ADD);
	}
	
	public boolean add(Note note) {
		boolean result = notes.add(note);
		if (result) {
			note.notepad = this;
			if (note.id >= nextNoteId) {
				nextNoteId = note.id + 1;
			}
			notes.sort(null);
			changed = true;
		}
		return result;
	}
	
	public Note add(String subject, String email, String body) throws AddressException {
		Note result = new Note(nextNoteId, subject, email, body);
		if (add(result)) {
			return result;
		}
		throw new RuntimeException(Const.COULD_NOT_ADD);
	}
	
	public boolean remove(Note note) {
		boolean result = notes.remove(note);
		if (result) {
			note.notepad = null;
			changed = true;
		}
		return result;
	}
	
	public Note remove(int id) {
		Note result = getNote(id);
		if (remove(result)) return result;
		throw new RuntimeException("Не удалось удалить заметку");
	}
	
	public Notepad selectBySubject(String regex) {
		if (regex.isBlank()) return this;
		Pattern pattern = Pattern.compile(regex);
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (pattern.matcher(note.subject).find()) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed, nextNoteId);
	}
	
	public Notepad selectAfter(long date) {
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (Long.compare(note.created, date) > 0 ) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed, nextNoteId);
	}
	
	public Notepad selectFromDate(String date) {
		return selectAfter(Date.decode(date) - 1);
	}
	
	public Notepad selectBefore(long date) {
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (Long.compare(note.created, date) < 0 ) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed, nextNoteId);
	}
	
	public Notepad selectUntilDate(String date) {
		return this.selectBefore(Date.decode(date) + 1);
	}
	
	public Notepad selectByEmail(String regex) {
		if (regex.isBlank()) return this;
		Pattern pattern = Pattern.compile(regex);
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (note.email == null) continue;
			if (pattern.matcher(note.email.toString()).find()) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed, nextNoteId);
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
		return new Notepad(result, file, changed, nextNoteId);
	}	
	
	static final String[] HEADER = new String[] {
			"id", "Тема", "Создано", "email", "Текст"
	};
	
	@Override
	public String toString() {
		Table result = new Table(HEADER);
		result.getCol(0).setAlign(Table.Align.RIGHT);
		for (var note: notes) {
			result.addRow(new String[]{
					Integer.toString(note.id),
					note.subject,
					Date.encode(note.created),
					(note.email != null) ? note.email : "не указан",
					note.body
			});
		}
		return result.toString();
	}

	void load() throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			notes = new LinkedList<>();
			nextNoteId = 0;
			String line;
			while ((line = in.readLine()) != null) {
				Matcher matcher = Const.NOTE_PTRN.matcher(line);
				if (matcher.find()) {
					try {
						Note note = new Note(matcher, this);
						if (notes.add(note)) {
							if (note.id >= nextNoteId) {
								nextNoteId = note.id + 1;
							}
						}
					} catch (Exception e) {
						//Заметки с ошибками игнорируются
					}					
				}
			}
		}		
		notes.sort(null);
		changed = false;
	}
	
	public void save() throws IOException {
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
    	static final Pattern NOTE_PTRN = Pattern.compile("\\s*(\\d*)\\s*;(.*?);\\s*(\\d*)\\s*;(.*?);(.*)");
    	
    	static final String[] REGEX_META = new String[]{
    			"\\\\", "\\^", "\\.", "\\[", "\\]", "\\$", "\\(", "\\)", "\\*", "\\{", "\\}", "\\?", "\\+", "\\|"  
    	};
    	
    	static final String COULD_NOT_ADD = "Не удалось добавить заметку";
    	static final String NOT_FOUND = "Нет такой заметки";
    }
}

package aabyodj.epamgrow.java0online.m6t2.notepad;

import static aabyodj.console.Const.BR;

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
 * Агрегатор заметок
 * @author aabyodj
 */
public class Notepad {
	
	/** Список заметок */
	List<Note> notes;	//TODO: сделать деревом
	
	/** Файл заметок */
	File file;
	
	/** Признак того, что список изменился по сравнению с файлом */
	boolean changed;
	
	/** Идентификатор для следующей заметки */
	int nextNoteId;
	
	/**
	 * Загрузить заметки из файла. В случае неудачи - создать пустой агрегатор.
	 * @param fileName Имя файла заметок
	 */
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

	/**
	 * Получить заметку по её id
	 * @param id
	 * @return
	 */
	public Note getNote(int id) {
		for (var note: notes) {
			if (note.id == id) {
				return note;
			}
		}
		throw new RuntimeException(Const.NOT_FOUND);
	}
	
//	/**
//	 * Добавить пустую заметку
//	 * @return Новая заметка
//	 */
//	public Note add() {
//		Note result = new Note(nextNoteId);
//		if (add(result)) {
//			return result;
//		}
//		throw new RuntimeException(Const.COULD_NOT_ADD);
//	}
	
	/**
	 * Добавить в список заметку и отсортировать список в случае удачи
	 * @param note Заметка для добавления
	 * @return true, если успешно
	 */
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
	
	/**
	 * Создать новую заметку с указанными полями в список, после чего список отсортировать
	 * @param subject Тема новой заметки
	 * @param email email для новой заметки
	 * @param body текст новой заметки
	 * @return Добавленная заметка
	 * @throws AddressException ошибка формата email адреса
	 */
	public Note add(String subject, String email, String body) throws AddressException {
		Note result = new Note(nextNoteId, subject, email, body);
		if (add(result)) {
			return result;
		}
		throw new RuntimeException(Const.COULD_NOT_ADD);
	}
	
	/**
	 * Удалить заметку из списка
	 * @param note Заметка для удаления
	 * @return true, если успешно
	 */
	public boolean remove(Note note) {
		boolean result = notes.remove(note);
		if (result) {
			note.notepad = null;
			changed = true;
		}
		return result;
	}
	
	/**
	 * Удалить заметку по её id
	 * @param id Идентификатор заметки для удаления
	 * @return Удалённая заметка
	 */
	public Note remove(int id) {
		Note result = getNote(id);
		if (remove(result)) return result;
		throw new RuntimeException(Const.COULD_NOT_REMOVE);
	}
	
	/**
	 * Найти заметки с заданной темой и вернуть их в новом экземпляре агрегатора
	 * @param regex Регулярное выражение для поиска в теме
	 * @return Новый экземпляр агрегатора с результатами поиска
	 */
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
	
	/**
	 * Найти заметки, созданные после указанной даты и вернуть их в новом экземпляре агрегатора
	 * @param date Дата: число дней после 1 января 1970
	 * @return новый экземпляр агрегатора с результатами поиска
	 */
	public Notepad selectAfter(long date) {
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (Long.compare(note.created, date) > 0 ) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed, nextNoteId);
	}
	
	/**
	 * Найти заметки, созданные начиная с указанной даты, и вернуть их в новом экземпляре агрегатора
	 * @param date Дата в формате ГГГГ-ММ-ДД
	 * @return
	 */
	public Notepad selectFromDate(String date) {
		return selectAfter(Date.decode(date) - 1);
	}
	
	/**
	 * Найти заметки, созданные ранее указанной даты, и вернуть их в новом экземпляре агрегатора
	 * @param date Дата: число дней после 1 января 1970
	 * @return новый экземпляр агрегатора с результатами поиска
	 */
	public Notepad selectBefore(long date) {
		List<Note> result = new LinkedList<>();
		for (var note: notes) {
			if (Long.compare(note.created, date) < 0 ) {
				result.add(note);
			}
		}
		return new Notepad(result, file, changed, nextNoteId);
	}
	
	/**
	 * Найти заметки, созданные до указанной даты включительно, и вернуть их в новом экземпляре агрегатора
	 * @param date Дата в формате ГГГГ-ММ-ДД
	 * @return новый экземпляр агрегатора с результатами поиска
	 */
	public Notepad selectUntilDate(String date) {
		return this.selectBefore(Date.decode(date) + 1);
	}
	
	/**
	 * Найти заметки по адресу email и вернуть их в новом экземпляре агрегатора
	 * @param regex Регулярное выражение для поиска
	 * @return новый экземпляр агрегатора с результатами поиска
	 */
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
	
	/**
	 * Найти заметки по содержимому основного текста и вернуть их в новом экземпляре агрегатора
	 * @param regex Регулярное выражение для поиска
	 * @return новый экземпляр агрегатора с результатами поиска
	 */
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
	
	/**
	 * Отформатировать список заметок в виде текстовой таблицы
	 */
	@Override
	public String toString() {
		Table result = new Table(Const.TABLE_HEADER);
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

	/**
	 * Загрузить заметки из файла
	 * @throws IOException
	 */
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
	
	/**
	 * Сохранить заметки в файл
	 * @throws IOException
	 */
	public void save() throws IOException {
		if (!changed) return;
		createIfNeeded(file);
		try (PrintStream out = new PrintStream(file)) {
			out.println("# Это автоматически сгенерированный файл. " + BR
					+ "# Все изменения и комментарии при следующем сохранении будут перезаписаны." + BR + BR
					+ "# id; тема; дата создания; email; сообщение" + BR);
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

    /**
     * Экранировать в строке метасимволы, используемые в регулярных выражениях
     * @param str Исходная строка
     * @return
     */
    public static String maskMeta(String str) {
    	for (String meta: Const.REGEX_META) {
    		str = str.replaceAll(meta, meta);
    	}
    	return str;
    }
    
    /** Набор констант */
    static class Const {
    	
    	/** Паттерн для выделения полей заметки в строке файла */
    	static final Pattern NOTE_PTRN = Pattern.compile("\\s*(\\d*)\\s*;(.*?);\\s*(\\d*)\\s*;(.*?);(.*)");
    	
    	/** Массив экранированных метасимволов, используемых в регулярных выражениях */
    	static final String[] REGEX_META = new String[]{
    			"\\\\", "\\^", "\\.", "\\[", "\\]", "\\$", "\\(", "\\)", "\\*", "\\{", "\\}", "\\?", "\\+", "\\|"  
    	};
    	
    	/** Заголовок таблицы заметок */
    	static final String[] TABLE_HEADER = new String[] {
    			"id", "Тема", "Создано", "email", "Текст"
    	};
    	    	
    	static final String COULD_NOT_ADD = "Не удалось добавить заметку";
    	static final String COULD_NOT_REMOVE = "Не удалось удалить заметку";
    	static final String NOT_FOUND = "Нет такой заметки";
    }
}

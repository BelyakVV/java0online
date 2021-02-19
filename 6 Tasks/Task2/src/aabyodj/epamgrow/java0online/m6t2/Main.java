package aabyodj.epamgrow.java0online.m6t2;

import static aabyodj.console.CommandLineInterface.printErrorMsg;

import java.io.IOException;

import aabyodj.console.CommandLineInterface;
import aabyodj.console.CommandLineInterface.Option;
import aabyodj.epamgrow.java0online.m6t2.notepad.Note;
import aabyodj.epamgrow.java0online.m6t2.notepad.Notepad;
import aabyodj.epamgrow.java0online.m6t2.util.Email;
import aabyodj.epamgrow.java0online.m6t2.util.Email.AddressException;

/**
 * Задание 2. Блокнот. 
 * Разработать консольное приложение, работающее с Заметками в Блокноте. 
 * Каждая Заметка это: Заметка (тема, дата создания, e-mail, сообщение).
 *
 * Общие пояснения к практическому заданию.
 * • В начале работы приложения данные должны считываться из файла, в конце
 * работы – сохраняться в файл.
 * • У пользователя должна быть возможность найти запись по любому параметру
 * или по группе параметров (группу параметров можно определить
 * самостоятельно), получить требуемые записи в отсортированном виде, найти
 * записи, текстовое поле которой содержит определенное слово, а также
 * добавить новую запись.
 * • Особое условие: поиск, сравнение и валидацию вводимой информации
 * осуществлять с использованием регулярных выражений.
 * • Особое условие: проверку введенной информации на валидность должен
 * осуществлять код, непосредственно добавляющий информацию.
 * 
 * @author aabyodj
 */
public class Main {
	
	/** Агрегатор заметок */
	static Notepad notepad = new Notepad("data/notes.csv");
	
	/** Интерфейс командной строки */
	static CommandLineInterface cli = new CommandLineInterface(new Option[]{
			new Option("Найти заметку", Main::searchNote),
			new Option("Добавить заметку", Main::addNote),
			new Option("Редактировать заметку", Main::editNote),
			new Option("Удалить заметку", Main::removeNote),
			new Option("Выход", Main::exit)
	});

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		cli.run();
	}

	/** Найти заметку */
	static void searchNote() {
		String regex = cli.readLine("Введите регулярное выражене для поиска по теме и тексту");
		Notepad result = notepad.selectBySubject(regex).selectByBody(regex);
		
		String begin = cli.readLine("Искать заметки, созданные в указанном интервале. Начало");
		try {
			result = result.selectFromDate(begin);
		} catch (Exception e) {
			printErrorMsg(e.getMessage() + ". Игнорируем...");
		}
		
		String end = cli.readLine("Конец интервала");
		try {
			result = result.selectUntilDate(end);
		} catch (Exception e) {
			printErrorMsg(e.getMessage() + ". Игнорируем...");
		}
		
		String email = cli.readLine("Введите регулярное выражение для поиска по email");
		result = result.selectByEmail(email);
		
		cli.printByPages(result.toString());
	}
	
	/** Добавить заметку */
	static void addNote() {
		String subj = cli.readLine("Введите тему").strip();
		if (subj.isBlank()) {
			printErrorMsg("Тема не может быть пустой");
			return;
		}
		
		String email = cli.readLine("Введите email");
		if (!Email.isValidMailbox(email)) {
			printErrorMsg("Неверный формат email адреса. Заметка будет сохранена без email.");
			email = null;
		}
		
		String body = cli.readLine("Введите текст");
		try {
			if (notepad.add(subj, email, body)!= null) {
				System.out.println("Заметка успешно добавлена");
				return;
			}
		} catch (AddressException e) {
			//Ошибок не будет. Наверное.
		}
		printErrorMsg("Не удалось добавить заметку");
	}
	
	/** Редактировать заметку */
	static void editNote() {
		int id = cli.getInt("Введите id");
		Note note;
		try {
			note = notepad.getNote(id);
		} catch (Exception e) {
			printErrorMsg(e.getMessage());
			return;
		}		
		boolean changed = false;
		System.out.println(note);
		
		String newSubj = cli.readLine("Введите новую тему (пустая строка - оставить без изменения)");
		if (!newSubj.isBlank()) {
			note.setSubject(newSubj);
			changed = true;
		}
		
		String newDate = cli.readLine("Изменить дату создания (пустая строка - оставить без изменения)");
		if (!newDate.isBlank()) {
			try {
				note.setDate(newDate);
				changed = true;
			} catch (Exception e) {
				printErrorMsg(e.getMessage());
			}
		}
		
		String newEmail = cli.readLine("Введите новый email (пустая строка - оставить без изменения)");
		if (!newEmail.isBlank()) {
			try {
				note.setEmail(newEmail);
				changed = true;
			} catch (Exception e) {
				printErrorMsg(e.getMessage());
			}
		}
		
		String newBody = cli.readLine("Введите новый текст (пустая строка - оставить без изменения)");
		if (!newBody.isBlank()) {
			note.setBody(newBody);
			changed = true;
		}
		
		if (changed) {
			System.out.println("Заметка успешно изменена");
		} else {
			System.out.println("Заметка осталась без изменений");
		}
	}
	
	/** Удалить заметку */
	static void removeNote() {
		int id = cli.getInt("Введите id");
		try {
			notepad.remove(id);
			System.out.println("Заметка успешно удалена");
		} catch (Exception e) {
			printErrorMsg(e.getMessage());
		}		
	}
	
	/** Выход из программы */
	static void exit() {
		try {
			notepad.save();
		} catch (IOException e) {
			printErrorMsg("Не удалось сохранить файл заметок. " + e.getMessage());
		}
		System.exit(0);
	}
}

package aabyodj.epamgrow.java0online.m6t2;

import aabyodj.console.CommandLineInterface;
import aabyodj.epamgrow.java0online.m6t2.Note.Email.AddressException;

import static aabyodj.console.CommandLineInterface.Option;
import static aabyodj.console.CommandLineInterface.printErrorMsg;

import java.io.IOException;

/**
 * @author aabyodj
 *
 */
public class Main {
	
	static Notepad notepad = new Notepad("data/notes.csv");
	
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
	
	static boolean addNote() {
		String subj = cli.readLine("Введите тему").strip();
		if (subj.isBlank()) {
			printErrorMsg("Тема не может быть пустой");
			return false;
		}
		String email = cli.readLine("Введите email");
		if (!Note.Email.isValidMailbox(email)) {
			printErrorMsg("Неверный формат email адреса. Заметка будет сохранена без email.");
			email = null;
		}
		String body = cli.readLine("Введите текст");
		try {
			if (notepad.add(subj, email, body)!= null) {
				System.out.println("Заметка успешно добавлена");
				return true;
			};
		} catch (AddressException e) {
			//Ошибок не будет. Наверное.
		}
		printErrorMsg("Не удалось добавить заметку");
		return false;
	}
	
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
	
	static void removeNote() {
		int id = cli.getInt("Введите id");
		try {
			notepad.remove(id);
			System.out.println("Заметка успешно удалена");
		} catch (Exception e) {
			printErrorMsg(e.getMessage());
		}		
	}
	
	static void exit() {
		try {
			notepad.save();
		} catch (IOException e) {
			printErrorMsg("Не удалось сохранить файл заметок. " + e.getMessage());
		}
		System.exit(0);
	}
}

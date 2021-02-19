package aabyodj.epamgrow.java0online.m6t2.notepad;

import static aabyodj.console.Const.BR;

import java.time.LocalDate;
import java.util.regex.Matcher;

import aabyodj.epamgrow.java0online.m6t2.util.Date;
import aabyodj.epamgrow.java0online.m6t2.util.Email;
import aabyodj.epamgrow.java0online.m6t2.util.Email.AddressException;

/**
 * Заметка: тема, дата создания, e-mail, сообщение
 * @author aabyodj
 */
public class Note implements Comparable<Note> {
	
	/** Идентификатор заметки */
	final int id;
	
	/** Тема заметки */
	String subject;
	
	/** Дата создания: число дней после 1 января 1970 */
	long created;
	
	/** Адрес email */
	String email;
	
	/** Основной текст заметки */
	String body;
	
	/** Блокнот, которому принадлежит эта заметка */
	Notepad notepad;
	
	Note(int id) {
		this.id = id;
		touch();			//Установить дату создания заметки сегодняшним днём
		notepad = null;
	}
	
	/**
	 * Создать заметку с указанными полями
	 * @param id Идентификатор заметки
	 * @param subject Тема заметки
	 * @param email Адрес email
	 * @param body Основной текст заметки
	 * @throws Email.AddressException Ошибка формата адреса email
	 */
	Note(int id, String subject, String email, String body) throws Email.AddressException {
		this(id);
		setSubject(subject);
		setEmail(email);
		setBody(body);
	}
		
	/**
	 * Создать заметку из строки файла
	 * @param matcher Исходная строка
	 * @param notepad Блокнот, в который следует поместить прочитанную заметку
	 */
	Note(Matcher matcher, Notepad notepad) {
		id = Integer.parseInt(matcher.group(1));
		subject = decode(matcher.group(2).strip());
		created = Long.parseLong(matcher.group(3).strip());
		body = decode(matcher.group(5));
		this.notepad = notepad;
		try {
			setEmail(decode(matcher.group(4).strip()));
		} catch (Exception e) {
			email = null;
		}
	}

	/**
	 * Получит тему заметки
	 * @return
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * Задать тему заметки
	 * @param newSubject Новая тема
	 */
	public void setSubject(String newSubject) {
		newSubject = newSubject.strip();
		if (newSubject.equals(subject)) return;
		subject = newSubject;
		if (notepad != null) {
			notepad.changed = true;
		}
	}
	
	/**
	 * Получить дату создания заметки в формате ГГГГ-ММ-ДД
	 * @return
	 */
	public String getDate() {
		return Date.encode(created);
	}
	
	/**
	 * Установить дату создания заметки
	 * @param str Новая дата в формате ГГГГ-ММ-ДД
	 */
	public void setDate(String str) {
		setDate(Date.decode(str));
	}
	
	/**
	 * Установить дату создания заметки
	 * @param epochDay Новая дата: число дней после 1 января 1970 
	 */
	public void setDate(long epochDay) {
		if (epochDay == created) return;
		created = epochDay;
		if (notepad != null) {
			notepad.notes.sort(null);
			notepad.changed = true;
		}		
	}
	
	/** Установить дату создания заметки сегодняшним днём */
	public void touch() {
		setDate(LocalDate.now().toEpochDay());
	}
	
	/**
	 * Получить адрес email
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Задать адрес email
	 * @param newEmail Новый адрес
	 * @throws AddressException Ошибка в формате адреса
	 */
	public void setEmail(String newEmail) throws AddressException {
		if (newEmail == null) {
			if (email != null) {
				email = null;
				if (notepad != null) {
					notepad.changed = true;
				}
			}
			return;
		}
		newEmail = newEmail.strip();
		if (newEmail.equals(email)) return;
		if (!Email.isValidMailbox(newEmail)) {
			throw new AddressException("Неверный формат email адреса");
		}
		email = newEmail;
		if (notepad != null) {
			notepad.changed = true;
		}
	}
	
	/**
	 * Получить основной текст заметки
	 * @return
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * Задать основной текст заметки
	 * @param newBody
	 */
	public void setBody(String newBody) {
		if (newBody.equals(body)) return;
		body = newBody;
		if (notepad != null) {
			notepad.changed = true;
		}
	}

	/** Компаратор по умолчанию: сортировка по дате */
	@Override
	public int compareTo(Note o) {
		return Long.compare(created, o.created);
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Тема: ");
		result.append(subject).append(BR);
		result.append("Дата создания: ").append(LocalDate.ofEpochDay(created)).append(BR);
		if (email != null) result.append(email.toString()).append(BR);
		result.append(body);
		return result.toString();
	}
	
	/**
	 * Отформатировать в виде строки для записи в текстовый файл
	 * @return
	 */
	String encode() {
		StringBuilder result = new StringBuilder();
		result.append(Integer.toString(id)).append(';');
		result.append(encode(subject)).append(';');
		result.append(created).append(';');
		if (email != null) result.append(email.toString());
		result.append(';').append(encode(body));
		return result.toString();
	}
	
	/**
	 * Восстановить строковую переменную, в которой ранее были экранированы
	 * разрывы строк и разделители полей
	 * @param in Исходная строка
	 * @return Восстановленная строка
	 */
	static String decode(String in) {
		return in.replaceAll("\\\\u003b", ";").replaceAll("\\\\u000a", BR);
	}
	
	/**
	 * Подготовить строковую переменную для записи в одну строку файла:
	 * экранировать разрывы строк и разделители полей
	 * @param in Исходная строка
	 * @return
	 */
	static String encode(String in) {
		return in.replaceAll(";", "\\\\u003b").replaceAll("\\R", "\\\\000a");
	}
}

package aabyodj.epamgrow.java0online.m6t2.notepad;

import static aabyodj.console.Const.BR;

import java.time.LocalDate;
import java.util.regex.Matcher;

import aabyodj.epamgrow.java0online.m6t2.util.Date;
import aabyodj.epamgrow.java0online.m6t2.util.Email;
import aabyodj.epamgrow.java0online.m6t2.util.Email.AddressException;

/**
 * @author aabyodj
 *
 */
public class Note implements Comparable<Note> {
	final int id;
	String subject;
	long created;
	String email;
	String body;
	Notepad notepad;
	
	Note(int id) {
		this.id = id;
		touch();
		notepad = null;
	}
	
	Note(int id, String subject, String email, String body) throws Email.AddressException {
		this(id);
		setSubject(subject);
		setEmail(email);
		setBody(body);
	}
		
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

	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String newSubject) {
		newSubject = newSubject.strip();
		if (newSubject.equals(subject)) return;
		subject = newSubject;
		if (notepad != null) {
			notepad.changed = true;
		}
	}
	
	public String getDate() {
		return Date.encode(created);
	}
	
	public void setDate(String str) {
		setDate(Date.decode(str));
	}
	
	void setDate(long epochDay) {
		if (epochDay == created) return;
		created = epochDay;
		if (notepad != null) {
			notepad.notes.sort(null);
			notepad.changed = true;
		}		
	}
	
	public void touch() {
		setDate(LocalDate.now().toEpochDay());
	}
	
	public String getEmail() {
		return email;
	}
	
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
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String newBody) {
		if (newBody.equals(body)) return;
		body = newBody;
		if (notepad != null) {
			notepad.changed = true;
		}
	}

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
	
	String encode() {
		StringBuilder result = new StringBuilder();
		result.append(Integer.toString(id)).append(';');
		result.append(encode(subject)).append(';');
		result.append(created).append(';');
		if (email != null) result.append(email.toString());
		result.append(';').append(encode(body));
		return result.toString();
	}
	
	static String decode(String in) {
		return in.replaceAll("\\\\u003b", ";").replaceAll("\\\\u000a", BR);
	}
	
	static String encode(String in) {
		return in.replaceAll(";", "\\\\u003b").replaceAll("\\R", "\\\\000a");
	}
}

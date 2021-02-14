/**
 * 
 */
package aabyodj.epamgrow.java0online.m6t2;

import static aabyodj.console.Const.BR;

import java.time.LocalDate;
import java.util.regex.Matcher;
import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

/**
 * @author master
 *
 */
class Note {
	String subject;
	long created;
	Address email;
	String body;
	Notepad notepad;
	
//	public Note() {
//		touch();
//	}
	
//	Note(String subject, long created, String email, String body) throws AddressException {
//		this.subject = subject;
//		this.created = created;
//		setEmail(email);
//		this.body = body;
//	}
		
	Note(Matcher matcher, Notepad notepad) {
		subject = decode(matcher.group(1).strip());
		created = Long.parseLong(matcher.group(2).strip());
		body = decode(matcher.group(4));
		this.notepad = notepad;
		try {
			setEmail(decode(matcher.group(3).strip()));
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
		notepad.changed = true;
	}
	
	public long getDate() {
		return created;
	}
	
	public void touch() {
		long newDate = LocalDate.now().toEpochDay();
		if (newDate == created) return;
		created = newDate;
		notepad.changed = true;
	}
	
	public String getEmail() {
		return (email != null) ? email.toString() : null;
	}
	
	public void setEmail(String newEmail) throws AddressException {
		if (newEmail == null) {
			if (email != null) {
				email = null;
				notepad.changed = true;
			}
			return;
		}
		Address ne = new InternetAddress(newEmail);		
		if (ne.equals(email)) return;
		email = ne;
		notepad.changed = true;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String newBody) {
		if (newBody.equals(body)) return;
		body = newBody;
		notepad.changed = true;
	}
	
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
		return in.replaceAll(";", "\\u003b").replaceAll("\\R", "\\000a");
	}
}

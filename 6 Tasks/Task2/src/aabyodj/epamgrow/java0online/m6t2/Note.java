/**
 * 
 */
package aabyodj.epamgrow.java0online.m6t2;

import static aabyodj.console.Const.BR;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

/**
 * @author aabyodj
 *
 */
public class Note {
	String subject;
	long created;
	Address email;
	String body;
	Notepad notepad;
	
	public Note() {
		touch();
		notepad = null;
	}
	
	Note(String subject, String email, String body) throws AddressException {
		this();
		setSubject(subject);
		setEmail(email);
		setBody(body);
	}
		
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
			notepad.changed = true;
		}		
	}
	
	public void touch() {
		setDate(LocalDate.now().toEpochDay());
	}
	
	public String getEmail() {
		return (email != null) ? email.toString() : null;
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
		Address ne = new InternetAddress(newEmail);		
		if (ne.equals(email)) return;
		email = ne;
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
	
	public static class Date {
		
		static final Pattern DATE_PTRN = Pattern.compile("([+-]?\\d+)-(\\d{1,2})-(\\d{1,2})");
		
		public static String encode(long epochDay) {
			LocalDate result = LocalDate.ofEpochDay(epochDay);		
			return String.format("%d-%02d-%02d", result.getYear(), result.getMonthValue(), result.getDayOfMonth());			
		}
		
		public static long decode(String str) {
			Matcher matcher = DATE_PTRN.matcher(str);
			matcher.find();
			int year = Integer.parseInt(matcher.group(1));
			int month = Integer.parseInt(matcher.group(2));
			int dayOfMonth = Integer.parseInt(matcher.group(3));
			LocalDate date = LocalDate.of(year, month, dayOfMonth);
			return date.toEpochDay();
		}
	}
	
	/**
	 * Функционал для проверки адреса email и его составных частей на соответствие RFC 2822
	 * @author aabyodj
	 */
	public static class Email {
				
		static final String CRLF = "\\r\\n";		
		static final String FWS = "(\\s*" + CRLF + ")?\\s+";
		
		static final String NO_WS_CTL = "[\\x01-\\x1f\\x7f&&[^\\x09\\x0a\\x0d]]";		
		static final String TEXT = "[\\x01-\\x7f&&[^\\n\\r]]";
		//static final String OBS_QP = "\\\\[\\x00-\\x7f]";		
		static final String QUOTED_PAIR = "\\\\" + TEXT;// + '|' + OBS_QP;
		
		static final String CTEXT = NO_WS_CTL + "|[\\x21-\\x27\\x2a-\\x5b\\x5d-\\x7e]";
		static final String CCONTENT = CTEXT + "|(" + QUOTED_PAIR + ')';
		static final String COMMENT = "\\(((" + FWS + ")?(" + CCONTENT + "))*(" + FWS + ")?\\)";
//		static final String COMMENT = "\\(.*\\)";
		static final String CFWS = "((" + FWS + ")?" + COMMENT + ")*((" + FWS + ")?" + COMMENT + '|' + FWS + ')';
		
		static final String ATEXT = "[\\p{Alnum}\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]";
		static final String ATOM = '(' + CFWS + ")?" + ATEXT + "+(" + CFWS + ")?";
		static final String DOT_ATOM_TEXT = ATEXT + "+(\\." + ATEXT + "+)*";
		static final String DOT_ATOM = '(' + CFWS + ")?" + DOT_ATOM_TEXT + '(' + CFWS + ")?";
		
		static final String QTEXT = "[\\x21-\\x7e&&[^\\x22\\x5c]]";
		static final String QCONTENT = QTEXT + '|' + QUOTED_PAIR; 
		static final String QUOTED_STRING = '(' + CFWS + ")?\"((" + FWS + ")?(" + QCONTENT + "))*(" + FWS + ")?\"(" + CFWS + ")?";
		
		static final String WORD = ATOM + '|' + QUOTED_STRING;
		static final String OBS_PHRASE = WORD + '(' + WORD + "|\\.|" + CFWS + ")+";
		static final String PHRASE = '(' + WORD + ")+|" + OBS_PHRASE;
		
		static final String DTEXT = '[' + NO_WS_CTL + "\\x21-\\x7e&&[^\\x5b-\\x5d]]";
		static final String DCONTENT = DTEXT + '|' + QUOTED_PAIR;
		static final String DOMAIN_LITERAL = '(' + CFWS + ")?\\[((" + FWS + ")?(" + DCONTENT + "))*(" + FWS + ")?\\](" + CFWS + ")?";
		
		static final String OBS_LOCAL_PART = '(' + WORD + ")(\\.(" + WORD + "))*";
		static final String OBS_DOMAIN = ATOM + "(\\." + ATOM + ")*";
		
		static final String LOCAL_PART = DOT_ATOM + '|' + QUOTED_STRING + '|' + OBS_LOCAL_PART;
		static final String DOMAIN = DOT_ATOM + '|' + DOMAIN_LITERAL + '|' + OBS_DOMAIN;	
		static final String ADDR_SPEC = '(' + LOCAL_PART + ")@(" + DOMAIN + ')';
		
		static final String DISPLAY_NAME = PHRASE;
		
		static final String OBS_DOMAIN_LIST = "@(" + DOMAIN + ")((" + CFWS + "|,)*(" + CFWS + ")?@(" + DOMAIN + "))*";
		static final String OBS_ROUTE = '(' + CFWS + ")?" + OBS_DOMAIN_LIST + ":(" + CFWS + ")?";  
		static final String OBS_ANGLE_ADDR = '(' + CFWS + ")?<(" + OBS_ROUTE + ")?" + ADDR_SPEC + ">(" + CFWS + ")?";
		
		static final String ANGLE_ADDR = '(' + CFWS + ")?<" + ADDR_SPEC + ">(" + CFWS + ")?|" + OBS_ANGLE_ADDR;
		static final String NAME_ADDR = '(' + DISPLAY_NAME + ")?(" + ANGLE_ADDR + ')';
		static final String MAILBOX = NAME_ADDR + '|' + ADDR_SPEC;
		
		public static final Pattern MAILBOX_PTRN = Pattern.compile(MAILBOX);
		
		public static boolean isValidMailbox(String str) {
			return MAILBOX_PTRN.matcher(str).matches();
		}
	}
}

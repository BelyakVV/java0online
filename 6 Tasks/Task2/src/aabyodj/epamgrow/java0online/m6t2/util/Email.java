package aabyodj.epamgrow.java0online.m6t2.util;

import java.util.regex.Pattern;

/**
 * Функционал для проверки адреса email и его составных частей на соответствие RFC 2822
 * @author aabyodj
 */
public class Email {

	static final String CRLF = "\\r\\n";		
	static final String FWS = "(\\s*" + CRLF + ")?\\s+";

	static final String NO_WS_CTL = "[\\x01-\\x1f\\x7f&&[^\\x09\\x0a\\x0d]]";		
	static final String TEXT = "[\\x01-\\x7f&&[^\\n\\r]]";
	//static final String OBS_QP = "\\\\[\\x00-\\x7f]";		
	static final String QUOTED_PAIR = "\\\\" + TEXT;// + '|' + OBS_QP;

	static final String CTEXT = NO_WS_CTL + "|[\\x21-\\x27\\x2a-\\x5b\\x5d-\\x7e]";
	static final String CCONTENT = CTEXT + "|(" + QUOTED_PAIR + ')';
	static final String COMMENT = "\\(((" + FWS + ")?(" + CCONTENT + "))*(" + FWS + ")?\\)";
	//	static final String COMMENT = "\\(.*\\)";
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

	/** Паттерн для проверки адреса email на соответствие RFC 2822 */
	public static final Pattern MAILBOX_PTRN = Pattern.compile(MAILBOX);

	/**
	 * Проверить адрес email на соответствие RFC 2822
	 * @param str
	 * @return
	 */
	public static boolean isValidMailbox(String str) {
		return MAILBOX_PTRN.matcher(str).matches();
	}

	/**
	 * Ошибка формата email адреса
	 * @author aabyodj
	 */
	public static class AddressException extends Exception {

		public AddressException(String msg) {
			super(msg);
		}
		
		private static final long serialVersionUID = -7860464595616956496L;
	}
}
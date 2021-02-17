/**
 * 
 */
package aabyodj.epamgrow.java0online.m6t2.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import aabyodj.epamgrow.java0online.m6t2.Note.Email;

/**
 * @author aabyodj
 *
 */
class EmailTest extends Email {

	static final String[] GOOD_MAILBOX = new String[] {
			"user@example.com", "user@domain", "user.group@example.com", "user.group@domain",
			"!#$%&'*+-/=?^_`{|}~@domain", "(!#$%&'*+-/=?^_`{|}~)user@domain", "(\\@)user@domain",
			" user@domain", "user @domain", "user@ domain", "user@domain ",
			"\r\nuser@domain", "user\r\n@domain", "user@\r\ndomain", "user@domain\r\n",
			"(comment)user@domain", "user(comment)@domain", "user@(comment)domain", "user@domain(comment)",
			"\"user\"@domain", " \"user\"@domain", "\"user\" @domain", "\"user\"@ domain", "\"user\"@domain ",
			"\"@\"@domain", "\"\"@domain", "\"..\"@domain",
			"user.\"group\"@domain", "user .\"group\"@domain", "user. \"group\"@domain",
			"user@example .com", "user@example. com",
			"user@[]", "user@[!#$%&'*+-/=?^_`{|}~]", "user@[\\@]",
			"User <user@domain>", "<user@domain>"
	};
	static final String[] BAD_MAILBOX = new String[] {
			"", "@", "@domain", "user@", ".@domain", "user@.",
			".user@domain", "user.@domain", "user@.domain", "user@domain.",
			"\n@domain", "\r@domain", "user@\n", "user@\r", "user@group@domain",
			"user@[\\]"
	};
	
	/**
	 * Test method for {@link aabyodj.epamgrow.java0online.m6t2.Note.Email#isValidMailbox(java.lang.String)}.
	 */
	@Test
	void testIsValidMailbox() {
		for (var mailbox: GOOD_MAILBOX) {
			assertTrue(isValidMailbox(mailbox), mailbox);
		}
		for (var mailbox: BAD_MAILBOX) {
			assertFalse(isValidMailbox(mailbox), mailbox);
		}
	}
}

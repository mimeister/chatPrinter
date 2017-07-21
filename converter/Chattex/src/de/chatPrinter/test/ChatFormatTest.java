package de.chatPrinter.test;

import static org.junit.Assert.*;
import org.junit.Test;

import de.chatPrinter.enums.*;

public class ChatFormatTest {

	@Test
	public void testSkypeParseDate() {
		String dateStr = " Sonntag, 22. Dezember 2013";
		String dateStr2 = " Sonntag, 2. Dezember 2013";
		String parsed = ChatFormat.SKYPE.parseDate(dateStr);
		String parsed2 = ChatFormat.SKYPE.parseDate(dateStr2);
		assertTrue(parsed.equals("22.12.2013"));
		assertTrue(parsed2.equals("02.12.2013"));
	}

}

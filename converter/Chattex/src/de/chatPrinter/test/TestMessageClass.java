package de.chatPrinter.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.chatPrinter.data.Message;
import de.chatPrinter.enums.MessageType;

import java.time.format.DateTimeFormatter;

public class TestMessageClass {
	
	Message msg;
	
	@Before
	public void setUp(){
		msg = new Message("Hans", "11.02.15, 22:18", "dd.MM.yy, HH:mm", "Lorem ipsum ...", MessageType.LEFT);
	}

	@Test
	public void testGetTime() {
		assertTrue("Times not equal", msg.getTime().equals("22:18"));
	}
	
	@Test
	public void testGetDate() {
		assertTrue("Dates not equal", msg.getDate("dd.MM.yyyy").equals("11.02.2015"));
	}
	
	@Test
	public void testGetDate2() {
		assertTrue("Dates not equal", msg.getDate(DateTimeFormatter.ISO_LOCAL_DATE).equals("2015-02-11"));
	}
	
	@Test
	public void testToLatex() {
		System.out.println(msg.toLatex());
	}

}

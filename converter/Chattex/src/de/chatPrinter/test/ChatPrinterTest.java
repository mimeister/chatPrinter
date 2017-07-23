package de.chatPrinter.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.chatPrinter.data.Message;
import de.chatPrinter.enums.MessageType;
import de.chatPrinter.tools.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ChatPrinterTest {
	private final String AUTHOR = "author";
	private final String PATTERN = "dd.MM.yyyy, HH:mm";
	private final String MSG = "Messenger %d, message %d";
	private List<List<Message>> messageLists;
	private int count = 0;

	@Before
	public void setUp() throws Exception {
		String timestamp = "15.12.2013, 22:";
		messageLists = new ArrayList<>();
		List<Message> msgL1 = new ArrayList<>();
		List<Message> msgL2 = new ArrayList<>();
		List<Message> msgL3 = new ArrayList<>();
		messageLists.add(msgL1);
		messageLists.add(msgL2);
		messageLists.add(msgL3);
		for (int i = 0; i < 10; i++) {
			msgL1.add(new Message(AUTHOR, timestamp+(i+10), PATTERN, String.format(MSG, 1, i), MessageType.LEFT));
			msgL2.add(new Message(AUTHOR, timestamp+(i+15), PATTERN, String.format(MSG, 2, i), MessageType.RIGHT));
			count = (i+1)<<1;
		}
		msgL3.add(new Message(AUTHOR, timestamp+"25:22", PATTERN+":ss", String.format(MSG, 3, 0), MessageType.LEFT));	
		count++;
	}

	@Test
	public void testJoinMessages() {
		List<Message> joined = ChatPrinter.joinMessages(messageLists);
		joined.forEach(msg -> System.out.println(msg.toString()));
		assertEquals("Message count incorrect", count, joined.size());
	}
	
	@Test
	public void testPrintLatex() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("\nCurrent relative path is: " + s);
		s = s.replaceAll("converter/Chattex$", "");
		ChatPrinter printer = new ChatPrinter(0, s + "example_data/wa_chat", s + "example_data/skype_chat");
		System.out.println(printer.printLatex());
	}

}

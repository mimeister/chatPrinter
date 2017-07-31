package de.chatPrinter.test;

//import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import de.chatPrinter.data.Message;
import de.chatPrinter.loader.*;

public class ChatLoaderTest {


	@Test
	public void testRead() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);
		s = s.replaceAll("converter/Chattex$", "");
		ChatLoader loader = new WhatsappLoader(s + "example_data/wa_chat");
		List<Message> wa = loader.read();
		loader = new SkypeLoader(s + "example_data/skype_chat");
		List<Message> sk = loader.read();
		System.out.println("\nWhatsApp:");
		for (Message msg : wa) {
			System.out.println(msg.toString());
		}
		System.out.println("\nSkype:");
		for (Message msg : sk)
			System.out.println(msg.toString());
	}
	

//	@Test
//	public void testReadEmojis() {
//		System.out.println("\n\nEmojis Test");
//		Path currentRelativePath = Paths.get("");
//		String s = currentRelativePath.toAbsolutePath().toString();
//		System.out.println("Current relative path is: " + s);
//		s = s.replaceAll("converter/Chattex$", "");
//		ChatLoader loader = new WhatsappLoader(s + "example_data/wa_emoji");
//		loader.debug = true;
//		List<Message> wa = loader.read();
//		for (Message msg : wa) {
//			System.out.println(msg.toString());
//		}
//	}

}

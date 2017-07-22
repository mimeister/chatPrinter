package de.chatPrinter.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import de.chatPrinter.data.Message;
import de.chatPrinter.enums.ChatFormat;

public class ChatLoader {
	
	private File file;
	private ChatFormat format;
	
	public ChatLoader(String file, ChatFormat format){
		this.file = new File(file);
		this.format = format;
	}
	
	/**
	 * Reads the given file and parses the contained messages into a list of messages
	 * @return a list of all contained messages
	 */
	public List<Message> read(){
		List<Message> chat = new ArrayList<>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String dateStr = "", timestamp;
			Matcher lineMatcher;
			Message msg = null;
			while ((line = br.readLine()) != null) {
				if (format.DATE_REGEX != null) {
					lineMatcher = format.DATE_REGEX.matcher(line);
					if (lineMatcher.matches()) {
						dateStr = lineMatcher.group();
						if (format.DATE_PARSING_REQUIRED)
							dateStr = format.parseDate(dateStr);
						continue;
					}
				}
				lineMatcher = format.REGEX.matcher(line);
				if (lineMatcher.matches()) {
					if (msg != null)		//old message is over, add it to the list
						chat.add(msg);
					if (format.DATE_REGEX == null) {
						timestamp = lineMatcher.group("date");
						if (format.DATE_PARSING_REQUIRED)
							timestamp = format.parseDate(timestamp);
					}
					else {
						timestamp = dateStr + " " + lineMatcher.group("date");
					}
					msg = new Message(lineMatcher.group("author"), timestamp, format.DATE_FORMAT, lineMatcher.group("message"));
				}
				else {
					msg.append("\n" + line);
				}
			}
			if (msg != null)		//add last message to the list
				chat.add(msg);
			fr.close();			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return chat;
	}

}

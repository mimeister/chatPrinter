package de.chatPrinter.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

import de.chatPrinter.data.Message;
import de.chatPrinter.enums.ChatFormat;
import de.chatPrinter.enums.MessageType;
import de.chatPrinter.exception.ChatFileFormatException;

public class ChatLoader {
	
	private File file;
	private ChatFormat format;
	private boolean authorSide = false; //false = left, true = right;
	private String dateStr = null, line;
	private Map<String, Boolean> authors;
	private long lineNumber = 0;
	
	private static final String LEFT_START_TAG = "left:";
	private static final String RIGHT_START_TAG = "right:";
	private static final Pattern AUTHOR_PATTERN = Pattern.compile("\\t(?<author>[^\t]+)$");
	
	public ChatLoader(String file){
		this.file = new File(file);
	}
	
	/**
	 * Reads the given file and parses the contained messages into a list of messages
	 * @return a list of all contained messages
	 */
	public List<Message> read(){
		List<Message> chat = new ArrayList<>();
		authors = new HashMap<>();
		boolean metaInfoInitialized = false; 
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String timestamp;
			Matcher lineMatcher;
			Message msg = null;
			lineNumber = 0;
			while ((line = br.readLine()) != null) {
				//printBytes(line.getBytes());
				lineNumber++;
				if (!metaInfoInitialized) { //initialize the authors
					metaInfoInitialized = initializeMetaInfo();
					continue;
				}
				if (format.DATE_REGEX != null && findDate()) //try to find a date, if they're separated from the messages
						continue;
				lineMatcher = format.REGEX.matcher(line);
				if (lineMatcher.matches()) {
					if (msg != null)		//preceding message is complete, add it to the list
						chat.add(msg);
					if (format.DATE_REGEX == null) {
						timestamp = lineMatcher.group("date");
						if (format.DATE_PARSING_REQUIRED)
							timestamp = format.parseDate(timestamp);
					}
					else {
						timestamp = dateStr + " " + lineMatcher.group("date");
					}
					if (!authors.containsKey(lineMatcher.group("author")))
							throw new ChatFileFormatException("Unknown author '" + lineMatcher.group("author"), file, line, lineNumber);							
					boolean messageSide = authors.get(lineMatcher.group("author"));
					msg = new Message(lineMatcher.group("author"),
							timestamp,
							format.DATE_FORMAT,
							lineMatcher.group("message"),
							messageSide ? MessageType.RIGHT : MessageType.LEFT);
				}
				else if (format.otherSpecialLine(line)) {
					if (msg != null)		//preceding message is complete, add it to the list
						chat.add(msg);
					msg = format.processSpecialLine(line, authors, dateStr);
				}
				else {
					if (msg == null)
						throw new ChatFileFormatException("Bad file format.", file, line, lineNumber);
					msg.append("\n" + line);
				}
			}
			if (msg != null)		//add last message to the list
				chat.add(msg);
			br.close();
			fr.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}		
		return chat;
	}
	
	private boolean initializeMetaInfo() {
		if (lineNumber == 1) {
			try {
				format = ChatFormat.valueOf(line);
			}
			catch (IllegalArgumentException ex) {
				throw new ChatFileFormatException("Unknown chat file format", file, line, 1);
			}
			return false;
		}
		Matcher lineMatcher = AUTHOR_PATTERN.matcher(line);		
		if (line.equals(LEFT_START_TAG))
			authorSide = false;
		else if (line.equals(RIGHT_START_TAG))
			authorSide = true;
		else if (lineMatcher.matches())
			authors.put(lineMatcher.group("author"), authorSide);
		else if (line.equals(""))
			return true;
		else
			throw new ChatFileFormatException("Chat file header isn't formatted correctly.", file, line, lineNumber);
		return false;
	}
	
	private boolean findDate() {
		Matcher lineMatcher = format.DATE_REGEX.matcher(line);
		if (lineMatcher.matches()) {
			dateStr = lineMatcher.group("date");
			if (format.DATE_PARSING_REQUIRED)
				dateStr = format.parseDate(dateStr);
			return true;
		}
		else if(dateStr == null){
			throw new ChatFileFormatException("Chat files with separated dates must start with a date.", file, line, lineNumber);
		}
		return false;
	}

	private static void printBytes(byte[] bs) {
		StringBuilder sb = new StringBuilder();
	    for (byte b : bs) {
	        sb.append(String.format("%02X ", b));
	    }
	    System.out.println(sb.toString());
	}
}

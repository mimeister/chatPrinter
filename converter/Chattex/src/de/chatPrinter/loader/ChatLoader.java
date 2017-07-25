package de.chatPrinter.loader;

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

public abstract class ChatLoader {
	
	File file;
	boolean authorSide = false; //false = left, true = right;
	String dateStr = null, line;
	Map<String, Boolean> authors;
	long lineNumber = 0;
	public boolean debug = false;
	
	static final String LEFT_START_TAG = "left:";
	static final String RIGHT_START_TAG = "right:";
	static final Pattern AUTHOR_PATTERN = Pattern.compile("\\t(?<author>[^\t]+)$");
	static final int MAX_LINES_PER_MESSAGE = 10;
	static final int MAX_MESSAGE_LENGTH = 500;
	
	public ChatLoader(String file){
		this.file = new File(file);
	}
	
	/**
	 * Reads the given file and parses the contained messages into a list of messages
	 * @return a list of all contained messages
	 */
	public abstract List<Message> read(); 
	
	boolean initializeMetaInfo() {
		if (lineNumber == 1) {
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

	static void printBytes(byte[] bs) {
		StringBuilder sb = new StringBuilder();
	    for (byte b : bs) {
	        sb.append(String.format("%02X ", b));
	    }
	    System.out.println(sb.toString());
	}
}

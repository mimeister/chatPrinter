package de.chatPrinter.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.chatPrinter.data.Message;
import de.chatPrinter.enums.MessageType;
import de.chatPrinter.exception.ChatFileFormatException;

public class WhatsappLoader extends ChatLoader {
	
	private static final Pattern MESSAGE_REGEX = Pattern.compile("^(?<date>(?:\\d\\d\\.){2}\\d\\d, \\d\\d:\\d\\d) - (?<author>\\S[^:]+): (?<message>.*)");
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm").withLocale(Locale.GERMAN);

	public WhatsappLoader(String file) {
		super(file);
	}
	
	@Override
	public List<Message> read(){
		List<Message> chat = new ArrayList<>();
		authors = new HashMap<>();
		boolean metaInfoInitialized = false; 
		try {
			System.out.print("\nLoading '" + file + "'");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String timestamp;
			Matcher lineMatcher;
			Message msg = null;
			lineNumber = 0;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				//printBytes(line.getBytes());
				//System.out.println("Processing line " + lineNumber + ": " + line);
				if (!metaInfoInitialized) { //initialize the authors
					metaInfoInitialized = initializeMetaInfo();
					continue;
				}
				lineMatcher = MESSAGE_REGEX.matcher(line);
				if (lineMatcher.matches()) {
					if (msg != null)		//preceding message is complete, add it to the list
						chat.add(msg);
					timestamp = lineMatcher.group("date");
					if (!authors.containsKey(lineMatcher.group("author")))
							throw new ChatFileFormatException("Unknown author '" + lineMatcher.group("author"), file, line, lineNumber);							
					boolean messageSide = authors.get(lineMatcher.group("author"));
					msg = new Message(lineMatcher.group("author"),
							timestamp,
							DATE_FORMAT,
							lineMatcher.group("message"),
							messageSide ? MessageType.RIGHT : MessageType.LEFT);
				}
				else if (line.trim().equals("")) { //skip empty lines; they cause latex errors
					continue;
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
}

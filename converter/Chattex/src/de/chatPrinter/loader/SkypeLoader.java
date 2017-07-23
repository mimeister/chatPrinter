package de.chatPrinter.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.chatPrinter.data.Message;
import de.chatPrinter.enums.MessageType;
import de.chatPrinter.exception.ChatFileFormatException;

public class SkypeLoader extends ChatLoader {
	
	private static final Pattern MESSAGE_REGEX = Pattern.compile("^\\[(?<date>(?:\\d\\d:){2}\\d\\d)\\] (?<author>\\S[^:]+): (?<message>.*)");
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d. MMMM yyyy HH:mm:ss").withLocale(Locale.GERMAN);
	private static final Pattern DATE_REGEX = Pattern.compile("^ [A-Z]\\w+, (?<date>(?<day>\\d{1,2})\\. (?<month>[A-Z]\\w+) (?<year>\\d{4}))$");
	private static final Pattern OTHER_REGEX = Pattern.compile("^\\[(?<date>(?:\\d\\d:){2}\\d\\d)\\]  (?<message>\\S.*)");

	
	public SkypeLoader(String file) {
		super(file);
	}
	
	public List<Message> read(){
		List<Message> chat = new ArrayList<>();
		authors = new HashMap<>();
		boolean metaInfoInitialized = false; 
		try {
			System.out.print("\nLoading '" + file + "'");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String timestamp;
			Matcher lineMatcher, dateMatcher;
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
				dateMatcher = DATE_REGEX.matcher(line);
				if (dateMatcher.matches()) { //try to find a date
					dateStr = dateMatcher.group("date");
					continue;
				}
				if(dateStr == null){
					throw new ChatFileFormatException("Chat files with separated dates must start with a date.", file, line, lineNumber);
				}
				lineMatcher = MESSAGE_REGEX.matcher(line);
				if (lineMatcher.matches()) { //process normal message
					if (msg != null)		//preceding message is complete, add it to the list
						chat.add(msg);
					timestamp = dateStr + " " + lineMatcher.group("date");
					if (!authors.containsKey(lineMatcher.group("author")))
							throw new ChatFileFormatException("Unknown author '" + lineMatcher.group("author"), file, line, lineNumber);							
					boolean messageSide = authors.get(lineMatcher.group("author"));
					msg = new Message(lineMatcher.group("author"),
							timestamp,
							DATE_FORMAT,
							lineMatcher.group("message"),
							messageSide ? MessageType.RIGHT : MessageType.LEFT);
					continue;
				}
				lineMatcher = OTHER_REGEX.matcher(line);
				if (lineMatcher.matches()) { //process special message (file/call)
					if (msg != null)		//preceding message is complete, add it to the list
						chat.add(msg);
					timestamp = dateStr + " " + lineMatcher.group("date");
					msg = new Message("",
							timestamp,
							DATE_FORMAT,
							"<" + lineMatcher.group("message") + ">",
							MessageType.CENTER);
					continue;
				}
				if (line.trim().equals("")) { //skip empty lines; they cause latex errors
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

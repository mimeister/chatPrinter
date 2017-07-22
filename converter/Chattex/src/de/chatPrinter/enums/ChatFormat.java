package de.chatPrinter.enums;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.chatPrinter.data.Message;
import de.chatPrinter.exception.*;

public enum ChatFormat {
	WHATSAPP("^(?<date>(?:\\d\\d\\.){2}\\d\\d, \\d\\d:\\d\\d) - (?<author>[^:]+): (?<message>.*)",
			DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm"),
			null,
			false),
	SKYPE("^\\[(?<date>(?:\\d\\d:){2}\\d\\d)\\] (?<author>[^:]+): (?<message>.*)",
			DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"),
			"^ [A-Z]\\w+, (?<day>\\d{1,2})\\. (?<month>[A-Z]\\w+) (?<year>\\d{4})$",
			true){
		private final Pattern fileSendingNote = Pattern.compile("^\\[(?<date>(?:\\d\\d:){2}\\d\\d)\\]  (?<message>\\S.*)");
		
		@Override
		public String parseDate(String rawDate) {
			Matcher match = DATE_REGEX.matcher(rawDate);
			StringBuffer parsedDate = new StringBuffer("");
			if (match.matches()) {
				String day = match.group("day");
				String month = Month.monthOfYearFromName(match.group("month"));
				String year = match.group("year");
				if (day.length() == 1)
					parsedDate.append("0");
				parsedDate.append(day);
				parsedDate.append(".");
				parsedDate.append(month);
				parsedDate.append(".");
				parsedDate.append(year);
			}
			return parsedDate.toString();
		}
		
		@Override
		public boolean otherSpecialLine(String line) {
			Matcher lineMatcher = fileSendingNote.matcher(line);
			return lineMatcher.matches();
		}
		
		@Override
		public Message processSpecialLine(String line, Map<String, Boolean> authors, String dateStr) {
			Matcher lineMatcher = fileSendingNote.matcher(line);
			if (lineMatcher.matches()) {
				String timestamp = dateStr + " " + lineMatcher.group("date");
				String rawMsg = lineMatcher.group("message");
				String author = findAuthor(rawMsg, authors.keySet());
				return new Message(author, timestamp, DATE_FORMAT, "<" + rawMsg + ">", authors.get(author) ? MessageType.RIGHT : MessageType.LEFT);
			}
			else
				throw new ChatFileFormatException("Given line is not a file-sending note");
		}
		
		private String findAuthor(String rawMsg, Set<String> authors) {
			String author = null;
			for (String athr : authors) {
				if (rawMsg.indexOf(athr) == 0)
					author = athr;
			}
			if (author == null)
				throw new ChatFileFormatException("No author found for file-sending note");
			return author;
		}
	};
	
	/**
	 * The regex pattern to recognize and split messages
	 */
	public final Pattern REGEX;
	/**
	 * The DateTimeFormatter used to parse the date+time
	 */
	public final DateTimeFormatter DATE_FORMAT;
	/**
	 * The regex pattern used to parse the date. Only set, if the dates are given on seperate lines for every new day.
	 */
	public final Pattern DATE_REGEX;
	/**
	 * Does the date need extra processing? If yes -> override parseDate()
	 */
	public final Boolean DATE_PARSING_REQUIRED;
	
	private ChatFormat(String regex, DateTimeFormatter dateFormat, String dateRegex, boolean dateParsingRqrd){
		REGEX = Pattern.compile(regex);
		DATE_FORMAT = dateFormat;
		if (dateRegex == null )
			DATE_REGEX = null;
		else
			DATE_REGEX = Pattern.compile(dateRegex);
		DATE_PARSING_REQUIRED = dateParsingRqrd;
	}
	
	public String parseDate(String rawDate) {		
		return rawDate;
	}
	
	/**
	 * Checks if the given line matches a rule for a special line of the format. 
	 * Must be overridden for the enum element.
	 * @param line to check
	 * @return matching result
	 */
	public boolean otherSpecialLine(String line) {
		return false;
	}
	
	/**
	 * Processes a special line defined by the format.
	 * Can only process single-lined special structures (e.g. skype file-sending notes)
	 * Must be overridden for the enum element.
	 * @param line to process
	 * @param authors in file
	 * @param dateStr the current date as string, if needed
	 * @return a message object built using the line, null if no special structures defined
	 */
	public Message processSpecialLine(String line, Map<String, Boolean> authors, String dateStr) {
		return null;
	}

}

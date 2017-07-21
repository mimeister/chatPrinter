package de.chatPrinter.enums;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ChatFormat {
	WHATSAPP("(?<date>(?:\\d\\d\\.){2}\\d\\d, \\d\\d:\\d\\d) - (?<author>[^:]+): (?<message>.*)",
			DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm"),
			null,
			false),
	SKYPE("\\[(?<date>(?:\\d\\d:){2}\\d\\d)\\] (?<author>[^:]+): (?<message>.*)",
			DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"),
			" [A-Z]\\w+, (?<day>\\d{1,2})\\. (?<month>[A-Z]\\w+) (?<year>\\d{4})$",
			true){
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

}

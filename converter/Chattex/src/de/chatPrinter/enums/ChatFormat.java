package de.chatPrinter.enums;

import java.time.format.DateTimeFormatter;


public enum ChatFormat {
	WHATSAPP("(?<date>(?:\\d\\d\\.){2}\\d\\d, \\d\\d:\\d\\d) - (?<author>[^:]+): (?<message>.*)",
			DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm"), ""
			),
	SKYPE("\\[(?<date>(?:\\d\\d:){2}\\d\\d)\\] (?<author>[^:]+): (?<message>.*)",
			DateTimeFormatter.ofPattern("HH:mm:ss"), //TODO pattern anpassen, damit es ausgeschriebenen monat frisst
			" [A-Z]\\w+, (\\d{1,2}\\. [A-Z]\\w+ \\d{4})$"
			);
	
	public final String REGEX;
	public final DateTimeFormatter DATE_FORMAT;
	public final String DATE_REGEX;
	
	private ChatFormat(String regex, DateTimeFormatter dateFormat, String dateRegex){
		REGEX = regex;
		DATE_FORMAT = dateFormat;
		DATE_REGEX = dateRegex;
	}

}

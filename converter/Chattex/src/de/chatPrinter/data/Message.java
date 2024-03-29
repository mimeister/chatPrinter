package de.chatPrinter.data;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import de.chatPrinter.enums.*;

public class Message {
	private LocalDateTime timestamp;
	private String author, message;
	private MessageType type;
	
	private Message(String author, LocalDateTime timestamp, MessageType type) {
		this.timestamp = timestamp;
		this.author = author;
		this.type = type;
		message = "";
	}
	
	public Message(String author, String dateTime, String pattern, String message, MessageType type){
		this.author = author;
		this.message = message;
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		this.timestamp = LocalDateTime.parse(dateTime, format);
		this.type = type;
	}
	
	public Message(String author, String dateTime, DateTimeFormatter format, String message, MessageType type){
		this.author = author;
		this.message = message;
		this.timestamp = LocalDateTime.parse(dateTime, format);
		this.type = type;
	}
	
	public Message createEmptyClone() {
		return new Message(author, timestamp, type);
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public String getTime(){
		return timestamp.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
	}
	
	public String getDate(DateTimeFormatter format){
		return timestamp.toLocalDate().format(format.withLocale(Locale.GERMAN));
	}
	
	public String getDate(String pattern){
		return timestamp.toLocalDate().format(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.GERMAN));
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getMessage() {
		return message;
	}
	
	public MessageType getType() {
		return type;
	}
	
	public void append(String msg) {
		message += msg;
	}
	
	public String toLatex() {
		if (type == MessageType.CENTER) {
			return String.format(type.latexCommand, timestamp.format(DateTimeFormatter.ofPattern("HH:mm")), escapeToLatex(message));
		}
		else
			return String.format(type.latexCommand, escapeToLatex(author), timestamp.format(DateTimeFormatter.ofPattern("HH:mm")), escapeToLatex(message));
	}
	
	private static String escapeToLatex(String str) {
		str = str.replaceAll("\\S{40}", "$0 "); //break overlong words
		str = str.replace("\\", "\\textbackslash ");
		str = str.replace("%", "\\%");
		str = str.replace("$", "\\$");//"
		str = str.replace("~", "\\textasciitilde ");
		str = str.replace("{", "\\{");
		str = str.replace("}", "\\}");
		str = str.replace("&", "\\&");
		str = str.replace("#", "\\#");
		str = str.replace("_", "\\_");
		str = str.replace("^", "\\textasciicircum ");
		str = str.replace("<", "\\textless ");
		str = str.replace(">", "\\textgreater ");
		if (str.startsWith("\n")) str = str.substring(1);
		str = str.replace("\n", "\\\\\n");
		str = str.replaceAll("(?<=\\s|^)\\\"(?=\\S)", "”");
		str = str.replace("\"", "“");
		str = str.replaceAll("(?<=\\s|^)'(?=\\S)", "’");
		str = str.replace("'", "‘");
//		StringBuffer buf = new StringBuffer(str);
//		buf.
		//TODO implement more escaping
		return str;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		buf.append("| ");
		buf.append(type.name());
		buf.append("| ");
		buf.append(author);
		buf.append(": ");
		buf.append(message);
		return buf.toString();
	}
}
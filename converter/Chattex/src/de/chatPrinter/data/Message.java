package de.chatPrinter.data;

import java.time.*;
import java.time.format.DateTimeFormatter;
import de.chatPrinter.enums.*;

public class Message {
	private LocalDateTime timestamp;
	private String author, message;
	private MessageType type;
	
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
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public String getTime(){
		return timestamp.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
	}
	
	public String getDate(DateTimeFormatter format){
		return timestamp.toLocalDate().format(format);
	}
	
	public String getDate(String pattern){
		return timestamp.toLocalDate().format(DateTimeFormatter.ofPattern(pattern));
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
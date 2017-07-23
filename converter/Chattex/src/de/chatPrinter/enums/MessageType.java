package de.chatPrinter.enums;

public enum MessageType {
	LEFT("\\leftMsg{%s}{%s}{%s}"),
	RIGHT("\\rightMsg{%s}{%s}{%s}"),
	CENTER("\\centerMsg{%s}{%s}");
	
	public final String latexCommand;
	
	MessageType(String latexCommand) {
		this.latexCommand = latexCommand;
	}
}

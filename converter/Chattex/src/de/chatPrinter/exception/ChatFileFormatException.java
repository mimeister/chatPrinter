package de.chatPrinter.exception;

import java.io.File;

/**
 * Indicates a problem with the format of a chat file.
 * @author Michael Meister
 */
public class ChatFileFormatException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7492683499257914090L;
	
	public ChatFileFormatException() {
		super();
	}
	
	public ChatFileFormatException(String message) {
		super(message);
	}
	
	public ChatFileFormatException(String message, File file, String lineContent, long lineNumber) {
		super (message + "'.\n Error while reading '" + file.getName() + "', line " + lineNumber + ": '" + lineContent + "'");
	}
	
}

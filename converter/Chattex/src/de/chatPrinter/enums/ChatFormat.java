package de.chatPrinter.enums;

import java.text.DateFormat;

public enum ChatFormat {
	WHATSAPP(){
		@Override
		void setConstants(){
			regex = "";
			dateFormat = "";
		}
	},
	SKYPE(){
		@Override
		void setConstants(){
			regex = "";
			dateFormat = "";
		}
	};
	
	public final String REGEX;
	public final String DATE_FORMAT;
	String regex, dateFormat;
	
	ChatFormat() {
		setConstants();
		REGEX = regex;
		DATE_FORMAT = dateFormat;
	}
	
	void setConstants(){
		regex = "";
		dateFormat = "";
	}

}

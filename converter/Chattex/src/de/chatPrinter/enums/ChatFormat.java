package de.chatPrinter.enums;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import de.chatPrinter.loader.*;

public enum ChatFormat {
	WHATSAPP{
		@Override
		public ChatLoader getLoader(String file) {
			return new WhatsappLoader(file);
		}
	},
	SKYPE{
		@Override
		public ChatLoader getLoader(String file) {
			return new SkypeLoader(file);
		}
	};
	
	public ChatLoader getLoader(String file) {return null;}
	
	public static ChatFormat findFormat(String file) throws IOException, FileNotFoundException {
		File f = new File(file);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		br.close();
		fr.close();
		return valueOf(line);
	}
	
	public static ChatLoader findLoader(String file) throws IOException, FileNotFoundException {
		return findFormat(file).getLoader(file);
	}

}

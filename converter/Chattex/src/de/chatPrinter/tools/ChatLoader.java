package de.chatPrinter.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import de.chatPrinter.data.Message;

public class ChatLoader {
	
	private File file;
	
	public ChatLoader(String file){
		this.file = new File(file);
	}
	
	public List<Message> read(){
		List<Message> chat = new ArrayList<>();
		Pattern pat = Pattern.compile("");
		Matcher match;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				
			}
			fr.close();			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return chat;
	}

}

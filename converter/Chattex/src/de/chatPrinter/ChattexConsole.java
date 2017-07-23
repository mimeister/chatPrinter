package de.chatPrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.chatPrinter.exception.ChatFileFormatException;
import de.chatPrinter.tools.ChatPrinter;

public class ChattexConsole {
	private static Scanner userInput = new Scanner(System.in);
	
	public static void main(String[] args) {
		ChatPrinter printer;
		if (args.length == 0) {
			List<String> files = new ArrayList<>();
			System.out.print("Enter the path to a chat file: ");
			files.add(userInput.next());
			while (askUserForMore()) {
				System.out.print("\nEnter path to chat file: ");
				files.add(userInput.next());
			}
			printer = new ChatPrinter(files.toArray(new String[0]));
		}
		else {
			printer = new ChatPrinter(args);
		}
		System.out.println("\nEnter a target file name: ");
		String target = userInput.next();
		userInput.close();
		try {
			printer.exportLatex(target, ChattexConsole::askUserFileExists);
		}
		catch (ChatFileFormatException e) {
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace(System.err);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	private static boolean askUserForMore() {
		System.out.print("Do you want to add more files? (y|n): ");
		String ui = userInput.next();
		while (!(ui.toLowerCase().trim().equals("y") || ui.toLowerCase().trim().equals("n"))) {
			System.out.println("\nBad input. Answer with y or n: ");
			ui = userInput.next();
		}
		return ui.toLowerCase().trim().equals("y");
	}
	
	private static boolean askUserFileExists() {
		System.out.print("\nThe file already exists. Do you want to overwrite it? (y|n): ");
		String ui = userInput.next();
		while (!(ui.toLowerCase().trim().equals("y") || ui.toLowerCase().trim().equals("n"))) {
			System.out.println("\nBad input. Answer with y or n: ");
			ui = userInput.next();
		}
		return ui.toLowerCase().trim().equals("y");
	}

}

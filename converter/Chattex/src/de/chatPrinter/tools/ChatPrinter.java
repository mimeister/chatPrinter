package de.chatPrinter.tools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.chatPrinter.data.Message;
import de.chatPrinter.data.Pair;



public class ChatPrinter {
	
	private ChatLoader[] chatLoaders = null;
	
	public ChatPrinter(String... files) {
		this.chatLoaders = new ChatLoader[files.length];
		for (int i = 0; i < files.length; i++)
			chatLoaders[i] = new ChatLoader(files[i]);
	}
	
	public String printLatex() {
		List<List<Message>> chats = new ArrayList<>();
		for (ChatLoader loader : chatLoaders)
			chats.add(loader.read());
		List<Message> joined = joinMessages(chats);
		StringBuffer buf = new StringBuffer();
		LocalDate lastDate = null, currentDate = null;
		for (Message msg : joined) {
			if (lastDate == null)
				lastDate = msg.getTimestamp().toLocalDate();
			else
				lastDate = currentDate;
			currentDate = msg.getTimestamp().toLocalDate();
			if (buf.length() == 0 || currentDate.isAfter(lastDate)) {
				if (buf.length() == 0 || currentDate.getYear() > lastDate.getYear())
					buf.append("\\newyear{" + currentDate.getYear() + "}\n");
				buf.append("\\newday{" + msg.getDate("eeee, d. MMMM") + "}\n");
			}
			buf.append(msg.toLatex() + "\n");			
		}
		return buf.toString();
	}
	
	public static List<Message> joinMessages(List<List<Message>> messageLists){
		List<Message> joined = new ArrayList<>();
		List<Pair<Iterator<Message>, Message>> iterators = new ArrayList<>(); //key = iterator, value = current element
		for (int i = 0; i < messageLists.size(); i++) {
			iterators.add(new Pair<>(messageLists.get(i).iterator()));
			iterators.get(i).setValue(iterators.get(i).key().next());
		}
		while (elementsLeft(iterators)) {
			Pair<Iterator<Message>, Message> next = findOldestMsg(iterators);
			joined.add(next.value());
			if (next.key().hasNext())
				next.setValue(next.key().next());
			else
				next.setValue(null);
		}
		return joined;
	}
	
	private static boolean elementsLeft(List<Pair<Iterator<Message>, Message>> iterators) {
		boolean elementsLeft = false;
		for (Pair<Iterator<Message>, Message> i : iterators)
			if (i.key().hasNext() || i.value() != null) elementsLeft = true;
		return elementsLeft;
	}
	
	private static Pair<Iterator<Message>, Message> findOldestMsg(List<Pair<Iterator<Message>, Message>> iterators) {
		Pair<Iterator<Message>, Message> oldest = null;
		for (Pair<Iterator<Message>, Message> i : iterators) {
			if (i.value() != null && (oldest == null || oldest.value().getTimestamp().isAfter(i.value().getTimestamp())))
				oldest = i;
		}
		return oldest;
	}

}

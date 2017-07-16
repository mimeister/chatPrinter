package de.chatPrinter.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.chatPrinter.data.Message;
import de.chatPrinter.data.Pair;



public class Toolbox {
	
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

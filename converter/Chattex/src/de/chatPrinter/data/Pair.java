package de.chatPrinter.data;

public class Pair<K, V> {
	
	private K key;
	private V val;
	
	public Pair(K key, V value){
		this.key = key;
		this.val = value;
	}	
	
	public Pair(K key){
		this.key = key;
		this.val = null;
	}

	public K key(){
		return key;
	}
	
	public V value(){
		return val; 
	}
	
	public void setValue(V val) {
		this.val = val;
	}
}

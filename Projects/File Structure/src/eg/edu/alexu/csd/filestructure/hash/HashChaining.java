package eg.edu.alexu.csd.filestructure.hash;

import java.util.ArrayList;

public class HashChaining<K, V> implements IHash<K, V>, IHashChaining {
	private ArrayList<ArrayList <Element<K, V>>> hashtable;
	private ArrayList<K> keys;
	private int size;
	private int capacity;
	private int collisions;
	public HashChaining(){
		capacity = 1200;
		size = 0;
		hashtable = new ArrayList<ArrayList <Element<K, V>>>();
		for(int i = 0; i < 1200;i++) {
			hashtable.add(0, new ArrayList<Element<K, V>>());
		}
		keys = new ArrayList<K>();
	}
	
	@Override
	public void put(K key, V value) {
		Element<K, V> temp = new Element<K, V>(key, value);
		int hash = (key.hashCode() % capacity + capacity) % capacity;
		collisions += hashtable.get(hash).size();
		hashtable.get(hash).add(0, temp);
		size++;
		keys.add(key);
	}

	@Override
	public String get(K key) {
		int hash = (key.hashCode() % capacity + capacity) % capacity;
		for(Element<K, V> temp : hashtable.get(hash)) {
			if(temp.getKey().equals(key)) {
				return temp.getValue().toString();
			}
		}
		return null;
	}

	@Override
	public void delete(K key) {
		int hash = (key.hashCode() % capacity + capacity) % capacity;
		for(int i = 0; i< hashtable.get(hash).size(); i++) {
			if(hashtable.get(hash).get(i).getKey().equals(key)) {
				hashtable.get(hash).remove(i);
				size--;
				keys.remove(key);
				return;
			}
		}
	}

	@Override
	public boolean contains(K key) {
		int hash = (key.hashCode() % capacity + capacity) % capacity;
		for(int i = 0; i< hashtable.get(hash).size(); i++) {
			if(hashtable.get(hash).get(i).getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int capacity() {
		return capacity;
	}

	@Override
	public int collisions() {
		return collisions;
	}

	@Override
	public Iterable<K> keys() {
		return keys;
	}
}

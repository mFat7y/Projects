package eg.edu.alexu.csd.filestructure.hash;

import java.util.ArrayList;

public class HashDouble<K, V> implements IHash<K, V>, IHashDouble {
	private ArrayList<Element<K, V>> hashtable;
	private int size;
	private int capacity;
	private int mod;
	private int collisions;
	public HashDouble(){
		capacity = 1200;
		collisions = 0;
		mod = 1193;
		size = 0;
		hashtable = new ArrayList<Element<K, V>>();
		for(int i = 0; i < capacity; i++) {
			hashtable.add(null);
		}
	}
	
	@Override
	public void put(K key, V value) {
		Element<K, V> temp = new Element<K, V>(key, value);
		int hash = (key.hashCode() % capacity + capacity) % capacity;
		int hash2 = mod - (((Integer)key % mod) + mod) % mod;
		int j = hash;
		int i = 0;
		while (hashtable.get(j) != null && hashtable.get(j).getKey() != null && i < capacity) {
			collisions++;
			i++;
			j = (hash + i * hash2) % capacity;
		}
		if(i < capacity) {
			hashtable.set(j, temp);
			size++;
		}
		else {
			collisions++;
			rehash();
			put(key, value);
		}
	}
	private void rehash() {
		capacity *= 2;
		size = 0;
		ArrayList<Element<K, V>> temp = hashtable;
		//mod = findPrime(capacity);
		hashtable = new ArrayList<Element<K, V>> ();
		for(int i = 0; i < capacity; i++) {
			hashtable.add(null);
		}
		for (Element<K, V> x : temp){
			if (x != null) {
				put(x.getKey(), x.getValue());
			}
		}
	}
	/*private int findPrime(int n){
		for (int i = n; i > 0; i++) {
			if (prime(n)) {
				return n;
			}
		}
		return n;
	}
	private boolean prime(int n) {
		for (int i = 2; i * i < n; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}*/
	@Override
	public String get(K key) {
		int hash = (key.hashCode() % capacity + capacity) % capacity;
		int hash2 = mod - (((Integer)key % mod) + mod) % mod;
		int j = hash;
		int i = 0;
		while (hashtable.get(j) != null && i < capacity) {
			if (hashtable.get(j).getKey() == null || !hashtable.get(j).getKey().equals(key)) {
				i++;
				j = (hash + i * hash2) % capacity;
				continue;
			}
			return hashtable.get(j).getValue().toString();
		}
		return null;
	}

	@Override
	public void delete(K key) {
 		int hash = (key.hashCode() % capacity + capacity) % capacity;
 		int i = 0;
 		int j = hash;
 		int hash2 = mod - (((Integer)key % mod) + mod) % mod;
 		while (hashtable.get(j) != null) {
 			if (hashtable.get(j).getKey() == null || !hashtable.get(j).getKey().equals(key)) {
 				i++;
 				j = (hash + i * hash2) % capacity;
 				continue;
 			}
 			hashtable.get(j).setKey(null);
 			size--;
 			return;
 		}
 	}

	@Override
	public boolean contains(K key) {
		return get(key) != null;
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
		ArrayList<K> arr = new ArrayList<K>();
		for(Element<K, V> x: hashtable) {
			if(x != null && x.getKey() != null) {
				arr.add(x.getKey());
			}
		}
		return arr;
	}
}

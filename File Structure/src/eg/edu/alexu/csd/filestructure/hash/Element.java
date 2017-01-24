package eg.edu.alexu.csd.filestructure.hash;

public class Element<K, V> {
	private K key;
	private V value;
	public Element(K key, V value){
		this.key = key;
		this.value = value;
	}
	public K getKey(){
		return key;
	}
	public V getValue(){
		return value;
	}
	public void setKey(K key){
		this.key = key;
	}
}

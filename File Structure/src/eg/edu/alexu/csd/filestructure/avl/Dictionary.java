package eg.edu.alexu.csd.filestructure.avl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Dictionary implements IDictionary {
	private Avl<String> data;
	private int size;
	public Dictionary() {
		data = new Avl<String>();
		size = 0;
	}
	@Override
	public void load(File file) {
		try {
			BufferedReader read = 
				new BufferedReader(new FileReader(file));
			String s = read.readLine();
			while (s != null) {
				insert(s);
				s = read.readLine();
			}
			read.close();
		} catch (Exception e) {
			return;
		}
	}
	@Override
	public boolean insert(String word) {
		if (data.search(word)) {
			return false;
		}
		size++;
		data.insert(word);
		return false;
	}

	@Override
	public boolean exists(String word) {
		return data.search(word);
	}
	@Override
	public boolean delete(String word) {
		if (data.delete(word)) {
			size--;
			return true;
		}
		return false;
	}
	@Override
	public int size() {
		return size;
	}
	@Override
	public int height() {
		return data.height();
	}
}

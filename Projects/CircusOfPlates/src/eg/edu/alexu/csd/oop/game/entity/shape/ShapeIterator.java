package eg.edu.alexu.csd.oop.game.entity.shape;

import java.util.Iterator;
import java.util.List;

public class ShapeIterator implements Iterator<Shape>{
	private List<Shape>list;
	private int index;
	public ShapeIterator(List<Shape>list) {
		this.list = list;
		index = 0;
	}
	@Override
	public boolean hasNext() {
		return index < list.size();
	}

	@Override
	public Shape next() {
		return list.get(index++);
	}

	@Override
	public void remove() {
		list.remove(index);
	}
	
}

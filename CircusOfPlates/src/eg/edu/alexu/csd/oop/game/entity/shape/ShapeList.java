package eg.edu.alexu.csd.oop.game.entity.shape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapeList implements Iterable<Shape>,Cloneable{
	private ArrayList<Shape> data;
	
	public ShapeList(List<Shape>list) {
		data = new ArrayList<Shape>(list);
	}
	public ShapeList() {
		data = new ArrayList<Shape>();
	}
	public void add(Shape shape){
		data.add(shape);
	}
	public Object clone(){
		Object list =  data.clone();
		if(list instanceof List){
			return new ShapeList((List<Shape>)list);
		}
		return new ShapeList();
	}
	public void remove(int index){
		if( index < 0 || index >= data.size() )
			return;
		data.remove(index);
	}
	public Shape get(int index){
		if( index < 0 || index >= data.size() )
			return null;
		return data.get(index);
	}
	public Shape getFirst(){
		return data.get(0);
	}
	public Shape getLast(){
		return data.get(data.size()-1);
	}
	public int size(){
		return data.size();
	}
	@Override
	public Iterator<Shape> iterator() {
		return new ShapeIterator(data);
	}
	public void removeLast() {
		remove(data.size()-1);
	}
	

}

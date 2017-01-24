package eg.edu.alexu.csd.filestructure.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Heap<T extends Comparable<T>> implements IHeap<T> {
    private ArrayList<Node<T>> list;
	public Heap() {
		list = new ArrayList<Node<T>>();
	}
	
	public void setList(ArrayList<T> list) {
		ArrayList<Node<T>> temp = new ArrayList<Node<T>>();
		for (int i = 0; i < list.size(); i++) {
			Node<T> node = new Node<T>(this);
			node.setIndex(i);
			node.setValue(list.get(i));
			temp.add(node);
		}
		this.list = temp;
	}
	
	@Override
	public INode<T> getRoot() {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public void heapify(INode<T> node) {
		if (node == null) {
			return;
		}
		INode<T> mx = node;
		if (node.getLeftChild() != null) {
			if (node.getLeftChild().getValue() != null) {
				if (node.getValue().compareTo(node.
					getLeftChild().getValue()) < 0) {
					mx = node.getLeftChild();
				}
			}
		}
		if (node.getRightChild() != null) {
			if (node.getRightChild()
					.getValue() != null) {
				if (mx.getValue().
				compareTo(node.getRightChild()
				.getValue()) < 0) {
					mx = node.getRightChild();
				}
			}
		}
		if (mx != node) {
			T temp = node.getValue();
			node.setValue(mx.getValue());
			mx.setValue(temp);
			heapify(mx);
		}
	}

	@Override
	public T extract() {
		if (list.size() == 0) {
			return null;
		}
		T ret = list.get(0).getValue();
		list.get(0).setValue(list.
				get(list.size() - 1).getValue());
		list.remove(list.size() - 1);
		if (list.size() > 0) {
			heapify(list.get(0));
		}
		return ret;
	}

	@Override
	public void insert(T element) {
		Node<T> node = new Node<T>(this);
		node.setIndex(list.size());
		node.setValue(element);
		list.add(node);
		Node<T> nd = node;
		while (nd.getParent() != null) {
			if (nd.getValue().compareTo(nd.getParent()
					.getValue()) > 0) {
				T temp = nd.getValue();
				nd.setValue(nd.getParent().getValue());
				nd.getParent().setValue(temp);
				nd = nd.getP();
			}
			else {
				break;
			}
		}
	}

	@Override
	public void build(Collection<T> unordered) {
		list.clear();
		if (unordered == null) {
			return;
		}
		if (unordered.isEmpty()) {
			return;
		}
		Iterator<T> it = unordered.iterator();
		while (it.hasNext()) {
			T comp = it.next();
			Node<T> node = new Node<T>(this);
			node.setValue(comp);
		    node.setIndex(list.size());
			list.add(node);
		}
		if (list.size() > 1) {
			for (int i = list.size() / 2 - 1; i >= 0; i--) {
				heapify(list.get(i));
			}
		}
	}
	
	/*public IHeap<T> clone(){
		Heap<T> temp = new Heap<T>();
		temp.setList((ArrayList<Node<T>>)list.clone());
		return temp;
	}*/
	
	public ArrayList<Node<T>> getList() {
		return list;
	}
}
	/*int current=0, l=1;
	while(l<list.size()){
		int mx = l;
		if(l+1<list.size())
			if(list.get(l+1).getValue().
			compareTo(list.get(l).getValue())>0)mx=l+1;
		if(list.get(mx).getValue().
		compareTo(list.get(current).getValue())>0){
			T temp = list.get(current).getValue();
			list.get(current).setValue(list.get(mx).getValue());
			list.get(mx).setValue(temp);
			current = mx;
			l = 2*current+1;
		}
		else break;
	}*/

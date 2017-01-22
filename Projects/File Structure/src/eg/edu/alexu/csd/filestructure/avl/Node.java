package eg.edu.alexu.csd.filestructure.avl;

public class Node<T extends Comparable<T>> implements INode<T> {
	private Node<T> left;
	private Node<T> right;
	private T value;
	private int height;
	
	public Node() {
		left = null;
		right = null;
		value = null;
		height = 1;
	}
	@Override
	public INode<T> getLeftChild() {
		return left;
	}
	
	@Override
	public INode<T> getRightChild() {
		return right;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}
	
	public Node<T> getLeft() {
		return left;
	}
	
	public Node<T> getRight() {
		return right;
	}
	
	public void setLeft(Node<T> node) {
		left = node;
	}
	
	public void setRight(Node<T> node) {
		right = node;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}

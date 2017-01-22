package eg.edu.alexu.csd.filestructure.sort;


public class Node<T extends Comparable<T>> implements INode<T> {
    private int index = 0;
	private T value;
	private Heap<T> heap;
	
	public Node(Heap<T> heap) {
		this.heap = heap;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	public int getIndex() {
		return index;
	}
	
	@Override
	public INode<T> getLeftChild() {
		if (heap.getList() == null) {
			return null;
		}
		if (heap.getList().size() > index * 2 + 1) {
			return heap.getList().get(index * 2 + 1);
		}
		return null;
	}

	@Override
	public INode<T> getRightChild() {
		if (heap.getList() == null
				|| index * 2 + 2 >= heap.getList().size()) {
			return null;
		}
		return heap.getList().get(index * 2 + 2);
	}

	@Override
	public INode<T> getParent() {
		if (heap.getList() == null || index == 0) {
			return null;
		}
		return heap.getList().get((index - 1) / 2);
	}
	
	public Node<T> getP() {
		if (heap.getList() == null || index == 0) {
			return null;
		}
		return heap.getList().get((index - 1) / 2);
	}
	
	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}
}

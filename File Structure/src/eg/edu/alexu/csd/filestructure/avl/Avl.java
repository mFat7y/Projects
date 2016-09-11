package eg.edu.alexu.csd.filestructure.avl;

public class Avl<T extends Comparable<T>> implements IAVLTree<T> {
	private Node<T> root;
	public Avl() {
		root = null;
	}
	public INode<T> getTree() {
		return root;
	}
	private Node<T> rightRotate(Node<T> root) {
		Node<T> temp = root.getLeft();
		root.setLeft(temp.getRight());
		temp.setRight(root);
		root.setHeight(Math.max(height(root.getLeft())
				, height(root.getRight())) + 1);
		temp.setHeight(Math.max(height(temp.getLeft())
				, height(temp.getRight())) + 1);
		return temp;
	}
	private Node<T> leftRotate(Node<T> root) {
		Node<T> temp = root.getRight();
		root.setRight(temp.getLeft());
		temp.setLeft(root);
		root.setHeight(Math.max(height(root.getLeft())
				, height(root.getRight())) + 1);
		temp.setHeight(Math.max(height(temp.getLeft())
				, height(temp.getRight())) + 1);
		return temp;
	}
	private int getBalance(Node<T> node) {
		if (node == null) {
			return 0;
		}
		return height(node.getLeft()) - height(node.getRight());
	}
	private Node<T> balanceIt(Node<T> root) {
		int balance = getBalance(root);
		
		if (balance > 1 &&  getBalance(root.getLeft()) >= 0) {
			return rightRotate(root);
		}
		
		if (balance > 1) {
			root.setLeft(leftRotate(root.getLeft()));
			return rightRotate(root);
		}
		
		if (balance < -1 && getBalance(root.getRight()) <= 0) {
			return leftRotate(root);
		}
		
		if (balance < -1) {
			root.setRight(rightRotate(root.getRight()));
			return leftRotate(root);
		}
		return root;
	}
	@Override
	public void insert(T key) {
		root = insert(root, key);
	}
	private Node<T> insert(Node<T> root, T key) {
		if (root == null) {
			root = new Node<T>();
			root.setValue(key);
		}
		else {
			if (root.getValue().compareTo(key) > 0) {
				root.setLeft(insert(root.getLeft(), key));
			}
			else {
				root.setRight(insert(root.getRight(), key));
			}
		}
		root.setHeight(Math.max(height(root.getLeft()),
				height(root.getRight())) + 1);
		return balanceIt(root);
	}
	@Override
	public boolean delete(T key) {
		if (search(root, key)) {
			root = delete(root, key);
			return true;
		}
		return false;
	}
	private Node<T> delete(Node<T> node, T key) {
		if (node == null) {
			return null;
		}
		if (node.getValue().compareTo(key) > 0) {
			node.setLeft(delete(node.getLeft(), key));
		}
		else if (node.getValue().compareTo(key) < 0) {
			node.setRight(delete(node.getRight(), key));
		}
		else {
			if (node.getLeft() == null && node.getRight() == null) {
				return null;
			}
			if (node.getLeft() == null && node.getRight() != null) {
				return node.getRight();
			}
			if (node.getRight() == null && node.getLeft() != null) {
				return node.getLeft();
			}
			Node<T> successor = successor(node.getRight());
			node.setValue(successor.getValue());
			node.setRight(delete(node.getRight(),
					successor.getValue()));
		}
		node.setHeight(Math.max(height(node.getLeft())
				, height(node.getRight())) + 1);
		return balanceIt(node);
	}
	private Node<T> successor(Node<T> node) {
		if (node.getLeft() == null) {
			return node;
		}
		return successor(node.getLeft());
	}
	@Override
	public boolean search(T key) {
		return search(root, key);
	}
	private boolean search(Node<T> node, T key) {
		if (node == null) {
			return false;
		}
		if (node.getValue().compareTo(key) > 0) {
			return search(node.getLeft(), key);
		}
		if (node.getValue().compareTo(key) < 0) {
			return search(node.getRight(), key);
		}
		return true;
	}
	@Override
	public int height() {
		return height(root);
	}
	private int height(Node<T> node) {
		if (node == null) {
			return 0;
		}
		return node.getHeight();
	}
}

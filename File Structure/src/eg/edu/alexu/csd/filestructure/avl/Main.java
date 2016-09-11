package eg.edu.alexu.csd.filestructure.avl;

public class Main {
	public static void main(String[] args) {
		Avl<String> t  = new Avl<String>();
		//t.insert("m");
		System.out.println(t.height());
		t.insert("1");
		t.insert("2");
		t.insert("3");
		t.insert("4");
		t.delete("4");
		System.out.println(t.height());
	}
	static void inOrder(Node<Integer> node) {
        if (node != null) {
        	System.out.print(node.getValue() + " ");
        	inOrder(node.getLeft());
            inOrder(node.getRight());
        }
    }
}

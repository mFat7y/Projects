package signalFlow;

import java.util.ArrayList;
import java.util.Iterator;

public class Main {
	public static void main(String [] argus) {
		Node a = new Node(0);
		Node b = new Node(1);
		Node c = new Node(2);
		Node d = new Node(3);
		Node e = new Node(4);
		Node f = new Node(5);
		Node g = new Node(6);
		a.addNeighbour(c, 2, "a02_0");
		a.addNeighbour(c, 1.23, "a02_1");
		a.addNeighbour(b, 1, "a01");
		b.addNeighbour(c, 2, "a12");
		b.addNeighbour(c, 3.2, "a12_1");
		c.addNeighbour(d, 3.5, "a23");
		//d.addNeighbour(e, 2, "a34");
		d.addNeighbour(b, 2.5, "a31");
//		d.addNeighbour(c, -1, "a32");
//		e.addNeighbour(d, -2, "a43");
//		e.addNeighbour(b, -1, "a41");
//		b.addNeighbour(g, 10, "a16");
//		g.addNeighbour(g, -1, "a66");
//		g.addNeighbour(e, 2, "a64");
		Solve s = new Solve();
		s.findForward(a, d);
		System.out.print("Forward paths:");
		for(ArrayList<Node> temp:s.getNodes()){
			System.out.println("");
			for(Node x: temp)
				System.out.print(x.getValue() + " ");
		}
		System.out.println("");
		System.out.println("forward paths values:");
		for(ArrayList<Edge> temp:s.getEdges()){
			System.out.println("");
			for(Edge x: temp)
				System.out.print(x.getLabel() + " ");
		}
		System.out.println();
		System.out.print("Loops:");
		for(ArrayList<Node> temp:s.getLoop()){
			System.out.println("");
			for(Node x: temp)
				System.out.print(x.getValue() + " ");
			System.out.print("          Degree:" + s.getLoopDegree().get(temp));
		}
		System.out.println("\nMain delta:");
		System.out.println(s.deltaS);
		System.out.println("delta Value: " + s.delta);
		System.out.println("small Deltas:");
		for(ArrayList<Node> temp:s.getNodes()){
			System.out.println(s.getNodesDelta().get(temp));
		}
		System.out.println("Loops Gains:");
		for(ArrayList<Node> temp:s.getLoop()){
			System.out.println(s.getLoopsGain().get(temp));
		}
		System.out.println("gain values of forward paths:");
		for(ArrayList<Node> temp:s.getNodes()){
			System.out.println(s.getForwardGain().get(temp));
		}
		System.out.println("overAll Gain = " + s.calculateOverAllGain());
		//for(Edge x: s.getEdges().get(0))System.out.print(x.getDest().getValue() + " ");
	}
}

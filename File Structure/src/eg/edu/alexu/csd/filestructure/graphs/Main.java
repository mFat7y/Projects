package eg.edu.alexu.csd.filestructure.graphs;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		Graph g = new Graph();
		File f  = new File("C:/Users/user/git/master/Heaps/src/eg/edu/alexu/csd/filestructure/graphs/in.txt");
		g.readGraph(f);
		int [] d = new int [9];
		int [] s = d.clone();
		g.runBellmanFord(0, d);
		for (int temp : d) {
			System.out.println(temp);
		}
		g.runDijkstra(0, s);
		for (int temp : s) {
			System.out.println(temp);
		}
		//System.out.println(g.getDijkstraProcessedOrder().size());
		for(int i: g.getDijkstraProcessedOrder())
			System.out.print(i);
	}
}

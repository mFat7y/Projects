package eg.edu.alexu.csd.filestructure.graphs;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Graph implements IGraph {
	private int edges= 0;
	private int vertices = 0;
	private ArrayList<Edge> edgeList;
	private ArrayList<ArrayList<Point>> adj;
	private ArrayList<Integer> verts;
	private ArrayList<ArrayList<Integer>> neighbours;
	private ArrayList<Integer> procOrder;
	public Graph() {
		edgeList = new ArrayList<Edge>();
		adj = new ArrayList<ArrayList<Point>>();
		verts = new ArrayList<Integer>();
		neighbours = new ArrayList<ArrayList<Integer>>();
		procOrder = new ArrayList<Integer>();
	}
	private class Edge {
		private int source;
		private int destination;
		private int weight;
		public Edge(Integer s, Integer d, Integer w) {
			source = s;
			destination = d;
			weight = w;
		}
	}
	@Override
	public void readGraph(File file) {
		try {
			BufferedReader read = 
				new BufferedReader(new FileReader(file));
			String s = read.readLine();
			String [] strs = s.split("\\s+");
			neighbours.clear();
			edgeList.clear();
			verts.clear();
			adj.clear();
			vertices = Integer.parseInt(strs[0]);
			edges = Integer.parseInt(strs[1]);
			for (int i = 0; i < vertices; i++) {
				neighbours.add(new ArrayList<Integer>());
				adj.add(new ArrayList<Point>());
			}
			for (int i = 0; i < edges; i++) {
				s = read.readLine();
				String [] str = s.split("\\s+");
				Integer source = Integer.parseInt(str[0]);
				Integer dest = Integer.parseInt(str[1]);
				Integer weight = Integer.parseInt(str[2]);
				neighbours.get(source).add(dest);
				edgeList.add(new Edge(source, dest, weight));
				adj.get(source).add(new Point(weight, dest));
			}
			read.close();
			for (int i = 0; i < vertices; i++) {
				verts.add(i);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}

	}
	@Override
	public int size() {
		return edges;
	}
	@Override
	public ArrayList<Integer> getVertices() {
		return verts;
	}
	@Override
	public ArrayList<Integer> getNeighbors(int v) {
		return new ArrayList<Integer>(neighbours.get(v));
	}
	@Override
	public void runDijkstra(int src, int[] distances) {//INFFFFFFFFFFFFFFFFFFFF
		procOrder.clear();
		boolean [] visited = new boolean [distances.length];
		for (int i = 0; i < vertices; i++) {
			distances[i] = Integer.MAX_VALUE / 2;
			visited[i] = false;
		}
		distances[src] = 0;
		PriorityQueue<Point> queue = new PriorityQueue<Point>(vertices, new Comparator<Point>(){
            public int compare(Point p, Point q){
            	return p.x - q.x;
            }
        } );
		queue.add(new Point(0, src));
		while (!queue.isEmpty()) {
			Point pt = queue.poll();
			int next = pt.y;
			if (!visited[next]) {
				procOrder.add(next);
				visited[next] = true;
				for (int i = 0; i < adj.get(next).size(); i++) {
					Point target = adj.get(next).get(i);
					if (distances[next] != Integer.MAX_VALUE / 2 &&
						distances[target.y] > distances[next] +
						target.x && !visited[target.y]) {
							distances[target.y] = distances[next] +
							target.x;
							queue.add(new Point(distances[target.y], target.y));
					}
				}
			}	
		}
	}
	@Override
	public ArrayList<Integer> getDijkstraProcessedOrder() {
		return procOrder;
	}
	@Override
	public boolean runBellmanFord(int src, int[] distances) {
		for (int i = 0; i < vertices; i++) {
			distances[i] = Integer.MAX_VALUE / 2;
		}
		distances[src] = 0;
		for (int i = 0; i < vertices; i++) { 
			for (int j = 0; j < edges; j++) {
				Edge temp = edgeList.get(j);
				if (distances[temp.source] != Integer.MAX_VALUE / 2
				&& distances[temp.source] + temp.weight
				< distances[temp.destination]) {
					distances[temp.destination] =
					distances[temp.source] + temp.weight;
				}
			}
		}
		for (int j = 0; j < edges; j++) {
			Edge temp = edgeList.get(j);
			if (distances[temp.source] != Integer.MAX_VALUE / 2
			&& distances[temp.source] + temp.weight
			< distances[temp.destination]) {
				return false;
			}
		}
		return true;
	}
}
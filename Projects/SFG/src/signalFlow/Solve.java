package signalFlow;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class Solve{
    private ArrayList<ArrayList<Edge>> edges;
    private ArrayList<ArrayList<Node>> nodes;
    private ArrayList<ArrayList<Node>> loops;
    private Map<Node, Boolean> vis;
    private Map<ArrayList<Node>, Double> forwardGains;
    private Map<ArrayList<Node>, Double> nodesDelta;
    private Map<ArrayList<Node>, String> nodesDeltaS;
    private Map<ArrayList<Node>, Double> loopGain;
    private Map<ArrayList<Node>, String> loopGainS;
	private Map<ArrayList<Node>, Integer> loopDegree;
    private Map<Integer,Boolean>exist;
    String deltaS = "";
    double delta = 0;
    public Solve(){
        edges = new ArrayList<ArrayList<Edge>>();
        nodes = new ArrayList<ArrayList<Node>>();
        loops = new ArrayList<ArrayList<Node>>();
    	forwardGains = new Hashtable<ArrayList<Node>, Double>();
        vis =    new Hashtable<Node, Boolean>(); 
        loopGain = new Hashtable<ArrayList<Node>, Double>();
        loopGainS = new Hashtable<ArrayList<Node>, String>();
        loopDegree = new Hashtable<ArrayList<Node>, Integer>();
        nodesDelta = new Hashtable<ArrayList<Node>, Double>();
        nodesDeltaS = new Hashtable<ArrayList<Node>, String>();
        exist = new Hashtable<Integer,Boolean>();
    }  
    public ArrayList<ArrayList<Node>> getNodes() {
    	return nodes;
    }
    public ArrayList<ArrayList<Edge>> getEdges() {
    	return edges;
    }
    public ArrayList<ArrayList<Node>> getLoop() {
    	return loops;
    }
    public void findForward(Node start, Node end){
        dfs(new ArrayList<Node>(), new ArrayList<Edge>(), start, end);
        delta = delta();
        getDeltaN();
        calcGainM();
    }
    private boolean intersects(ArrayList<Node> targetA,  ArrayList<Node> targetB) {
    	for(Node x : targetA) {
    		for(Node y : targetB) {
    			if(x.getValue() == y.getValue())return true;
    		}
    	}
    	return false;
    }
    private void dfs(ArrayList<Node> n, ArrayList<Edge> e, Node node, Node end){
        n.add(node);
        vis.put(node, true);
        if(node.equals(end)){
            nodes.add((ArrayList<Node>) n.clone());
            edges.add((ArrayList<Edge>) e.clone());
            for(Edge x: node.getEdges()) {
            	if(vis.get(x.getDest())!=null && vis.get(x.getDest())) {
            		ArrayList<Node> p = new ArrayList<Node>(n.subList(n.indexOf(x.getDest()), n.indexOf(node) + 1));
                    p.add(x.getDest());
                    int hash = 19;
    				for(Node z : p){
    					hash +=  (z.getValue()*z.getValue() * 17);
    				}
    				if(!exist.containsKey(hash)) {
	                    calculateGain(p);
	                    loopDegree.put(p, 1);
	                    loops.add(p);
	                    exist.put(hash, true);
    				}
            	}
            }
            n.remove(n.size()-1);
            vis.put(node, false);
            return;
        }
        for(Edge x: node.getEdges()){
            if(vis.get(x.getDest())==null || !vis.get(x.getDest())){
                e.add(x);
            	dfs(n, e, x.getDest(), end);
                e.remove(e.size()-1);
            }
            else{
                ArrayList<Node> p = new ArrayList<Node>(n.subList(n.indexOf(x.getDest()), n.indexOf(node) + 1));
                int hash = 19;
				for(Node z : p){
					hash +=  (z.getValue()*z.getValue() * 17);
				}
				p.add(x.getDest());
				if(!exist.containsKey(hash)) {
                    calculateGain(p);
                    loopDegree.put(p, 1);
                    loops.add(p);
                    exist.put(hash, true);
				}
            }
        }
        n.remove(n.size() - 1);
        vis.put(node, false);
    }
    void calculateGain(ArrayList<Node> arr) {
    	String sgain = "";
    	double gain = 1;
        for (int k = 1;k < arr.size(); k++) {
        	Edge target = arr.get(k - 1).getEdge(arr.get(k));
        	gain *= target.getWeight();
        	sgain += target.getLabel();
        }
      //  Edge target = arr.get(arr.size()-1).getEdge(arr.get(0));
      //  gain *= target.getWeight();
       // sgain += target.getLabel();
        loopGain.put(arr, gain);
        loopGainS.put(arr, sgain);
    }
    void calcGainM() {
    	for(ArrayList<Node> x: nodes) {
    		Double gain = 1.0;
    		for(int i=0; i < x.size()-1; i++) {
    			gain *= x.get(i).getEdge(x.get(i + 1)).getWeight();
    		}
    		forwardGains.put(x, gain);
    	}
    }
    
    public double delta() {
    	double delta = 1;
    	String deltaS = "1 ";
    	for(int i = 0; i < loops.size() && loopDegree.get(loops.get(i)) == 1; i++) {
    		for(int j = i + 1; j < loops.size(); j++) {
    			boolean flag = true;
    			for(Node x:loops.get(i)) {
    				if(loops.get(j).indexOf(x) != -1){
    					flag = false;
    					break;
    				}
    			}
    			if(flag){
    				//two non touching loops found
    				
    	    		ArrayList<Node> temp = new ArrayList<Node>();
    				int x = 0, y = 0;
    				while(x < loops.get(i).size() && y < loops.get(j).size()) {
    					if(loops.get(i).get(x).getValue() < loops.get(j).get(y).getValue()
    					&& temp.indexOf(loops.get(i).get(x))==-1) {
    						temp.add(loops.get(i).get(x));
    						x++;
    					}
    					else if(temp.indexOf(loops.get(i).get(x))!=-1)x++;
    					else if(loops.get(i).get(x).getValue() >= loops.get(j).get(y).getValue()
    	    					&& temp.indexOf(loops.get(j).get(y))==-1){
    						temp.add(loops.get(j).get(y));
    						y++;
    					}
    					else y++;
    				}
    				while(x < loops.get(i).size()) {
    					if(temp.indexOf(loops.get(i).get(x)) ==-1) {
    						temp.add(loops.get(i).get(x));
    					}
    					x++;
    				}
    				while(y < loops.get(j).size()) {
    					if(temp.indexOf(loops.get(j).get(y)) ==-1) {
    						temp.add(loops.get(j).get(y));
    					}
    					y++;
    				}//howa ana kont 3amelha leh O.o
    				/*int hash = 19;
    				for(Node node : temp){
    					hash = hash ^ (node.getValue() * 997);
    				}
    				if(true||!temp.isEmpty() && !exist.containsKey(hash)) {*/
					loopDegree.put(temp, loopDegree.get(loops.get(i)) + loopDegree.get(loops.get(j)));
					loopGain.put(temp, loopGain.get(loops.get(i)) * loopGain.get(loops.get(j)));
					loopGainS.put(temp, loopGainS.get(loops.get(i)) + loopGainS.get(loops.get(j)));
					loops.add(temp);
					//exist.put(hash, true);
				
    			}
    		}
    	}
    	for (ArrayList<Node> temp : loops) {
    		if(loopDegree.get(temp)%2 == 1){
    			delta -= loopGain.get(temp);
    			deltaS += " - " + loopGainS.get(temp);
    		}
    		else {
    			delta += loopGain.get(temp) * (loopDegree.get(temp)%2==1?-1:1);
    			deltaS += " + " + loopGainS.get(temp);
    		}
    	}
    	this.deltaS = deltaS;
    	return delta;
    }
    private void getDeltaN() {
    	for (ArrayList<Node> temp : nodes) {
    		double negative = 1.0;
    		String negativeS = "1 ";
    		for(ArrayList<Node> loop : loops) {
    			if(!intersects(temp, loop)){
    				if(loopDegree.get(loop)%2 == 1) {
    					negative -= loopGain.get(loop);
        				negativeS += " - " + loopGainS.get(loop);
    				}
    				else {
        				negative += loopGain.get(loop);
        				negativeS += " + " + loopGainS.get(loop);
    				}
    			}
    		}
    		nodesDelta.put(temp, negative);
    		nodesDeltaS.put(temp, negativeS);
    	}
    }
    
    public Double calculateOverAllGain(){
    	Double overAll = 0.0;
    	for(ArrayList<Node> x: nodes) {
    		overAll += forwardGains.get(x) * nodesDelta.get(x);
    	}
    	return overAll/delta;
    }
    
    public Map<ArrayList<Node>, String> getNodesDeltaS(){
    	return nodesDeltaS;
    }
    public Map<ArrayList<Node>, Double> getNodesDelta(){
    	return nodesDelta;
    }
    public Map<ArrayList<Node>, Integer> getLoopDegree() {
    	return loopDegree;
    }
    public Map<ArrayList<Node>, Double> getForwardGain() {
    	return forwardGains;
    }
    public Map<ArrayList<Node>, Double> getLoopsGain() {
    	return loopGain;
    }
}
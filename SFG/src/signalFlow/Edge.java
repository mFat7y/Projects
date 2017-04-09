package signalFlow;

public class Edge{
    private double weight;
    private Node dest;
    private String label;
    public Edge(double weight, Node dest, String label){
        this.weight = weight;
        this.dest = dest;
        this.label = label;
    }
    
    public double getWeight(){
        return weight;
    }
    
    public String getLabel(){
    	return label;
    }
    
    public Node getDest(){
        return dest;
    }
    public void updateWeight(double weight) {
    	this.weight += weight;
    }
}
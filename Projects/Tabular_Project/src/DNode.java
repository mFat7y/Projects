

public class DNode {
	private Object value;
	private DNode next;
	private DNode previous;
	
	public DNode(Object element, DNode x ,DNode y){
		this.value = element;
		this.previous = x;
		this.next = y;
	}
	
	public Object getValue(){return value;}
	
	public DNode getNext(){return next;}
	
	public DNode getPrevious(){return previous;}
	
	public void setValue(Object newValue){ value =  newValue;}
	
	public void setPrevious(DNode newPrevious) { previous = newPrevious;}
	
	public void setNext(DNode newNext) { next = newNext;}

}
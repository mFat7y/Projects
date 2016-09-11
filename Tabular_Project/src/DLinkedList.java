

public class DLinkedList implements MyLinkedList{
	private DNode header, trailer;
	private int size = 0;
	public DLinkedList(){
		size = 0;
		header = new DNode(null, null, null);
		trailer = new DNode(null, header, null);
		header.setNext(trailer);
	}

	
	@Override
	public void add(int index, Object element) {
		if (index>size || index<0)
			throw new RuntimeException();
		else{
			if (index==0){
				DNode x = new DNode(element, header, header.getNext());
				header.getNext().setPrevious(x);
				header.setNext(x);
			}
			else{
				DNode x = header.getNext(); 
				for (int i=0 ; i<index-1 ; i++)
					x = x.getNext();
				DNode newNode = new DNode(element, x, x.getNext());
				x.setNext(newNode);
			}
			size++;
		}
	}
	public void sortIt(){
		DNode curr = trailer.getPrevious();
		for(int i = size()-1;i>1;i--){
			if((int)curr.getValue() < (int)curr.getPrevious().getValue()){
				Object temp = curr.getValue();
				curr.setValue(curr.getPrevious().getValue());
				curr.getPrevious().setValue(temp);
			}
			curr = curr.getPrevious();
		}
	}
	@Override
	public void add(Object element) {
		DNode newNode = new DNode(element, header, trailer);
		if (size == 0){
			header.setNext(newNode);
			trailer.setPrevious(newNode);
		}
		else{
			newNode.setPrevious(trailer.getPrevious());
			trailer.getPrevious().setNext(newNode);
			trailer.setPrevious(newNode);
		}
		size++;
		try{
		sortIt();
		}catch(Exception ex){
			
		}
	}
	
	@Override
	public Object get(int index) {
		if (index>size-1 || index<0)
			throw new RuntimeException();
		else{
			DNode x = header.getNext();
			for (int i=0 ; i<index ; i++)
				x=x.getNext();
			return x.getValue();
		}
	}
	@Override
	public void set(int index, Object element){
		if (index>size-1 || index<0){
			throw new RuntimeException();
		}
		else{
			DNode x = header.getNext();
			for (int i=0 ; i<index ; i++)
				x = x.getNext();
			x.setValue(element);
			}
		}

	@Override
	public void clear() {
		header.setNext(trailer);
		trailer.setPrevious(header);
		size = 0;
		
	}
	@Override
	public boolean isEmpty() {
		if (header.getNext()==trailer)
			return true;
		return false;
	}
	@Override
	public void remove(int index) {
		if (header.getNext()==trailer || index>size-1 || index<0){
			throw new RuntimeException();
		}
		else{
			DNode x = header.getNext();
			for (int i=0 ; i<index ; i++)
				x = x.getNext();
			x.getNext().setPrevious(x.getPrevious());
			x.getPrevious().setNext(x.getNext());
			}
		size--;
		}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public DLinkedList sublist(int fromIndex, int toIndex) {
		if (fromIndex>toIndex || fromIndex<0 || toIndex<0 || toIndex>size-1)
			throw new RuntimeException();
		DNode v = this.header.getNext();
		DLinkedList list = new DLinkedList();
		for (int i=0 ; i<fromIndex ; i++)
			v=v.getNext();
		for (int j=0 ; j<=toIndex-fromIndex ; j++){
			list.add(j, v.getValue());
			v=v.getNext();
		}
		list.size = toIndex - fromIndex +1;
		return list;
	}
	public long hashIt(){
		long hash = 0;
		for(int i=0;i<size();i++){
			hash = (13131*hash)^(21*((int)get(i)+5));
		}
		return hash;
	}
		
	@Override
	public boolean contains(Object o) {
		DNode x = header.getNext();
		while(x.getNext() != null){
			if (x.getValue().equals(o))
				return true;
			x=x.getNext();
		}
		return false;
	}
}

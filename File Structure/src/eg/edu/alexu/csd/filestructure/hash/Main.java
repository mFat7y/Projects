package eg.edu.alexu.csd.filestructure.hash;

public class Main {

	public static void main(String[] args) {
		IHash<Integer, String> h2 = new HashQuadratic<Integer, String>();
		for(int i=0;i<3600;i++){
			h2.put((int)(Math.random() * 10000), String.valueOf(i));
		}
		/*for(int i=0;i<40000;i+=2){
			h2.delete(i);
		}
		int counter = 0;
		for(int i=0;i<40000;i++){
			if(h2.get(i)!=null)counter++;
		}*/
		System.out.println(h2.size());
		System.out.println(h2.capacity());
		System.out.println(h2.collisions());
		System.out.println(((float)h2.collisions()/10000));
		
	}
}

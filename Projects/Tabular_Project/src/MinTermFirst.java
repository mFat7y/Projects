import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinTermFirst {
	static JButton submit = new JButton("Minimize");
	static JTextField inDont= new JTextField();
	static JTextField inField = new JTextField();
	static JTextField inVars = new JTextField();
	static int vars;
	static print show;
	public MinTermFirst(int vars){
		this.vars = vars;
	}
	public static int countONes(int x){
		int count =0;
		while(x>0){
			if(x % 2 > 0)count++;
			x/=2;
		}
		return count;
	}
	public static MyLinkedList FirstStep(MyLinkedList temp, MyLinkedList Dont){
		MyLinkedList tempOrg = new DLinkedList();
		for(int i=0;i<temp.size();i++){
			tempOrg.add( new DLinkedList() );
			((MyLinkedList)tempOrg.get(i)).add(((MyLinkedList)temp.get(i)).get(0));
		}
		boolean done[] = new boolean[10000];
		boolean flag = true;
		Map<Long,Boolean> stored = new HashMap<Long,Boolean>();
		show = new print();
		while(flag){
			show.printFirst(tempOrg);
			flag = false;
			int tempSize = temp.size();
			for(int i=0;i<tempSize;i++){
				for(int j=0;j<tempSize;j++){
					
					if(i == j)continue;
					MyLinkedList curr = (MyLinkedList)temp.get(i);
					MyLinkedList later = (MyLinkedList)temp.get(j);
					int rmask = (int)curr.get(0);
					int bmask = (int)later.get(0);
					if(rmask >= bmask)continue;
					boolean skip = false;
					if(curr.size() != later.size())continue;
					for(int k=1;k<later.size();k++){
						if(curr.get(k) != later.get(k)){
							skip = true;
							break;
						}
					}
					if(skip)continue;
					if( countONes(bmask) - countONes(rmask) != 1 )continue;
					if( Math.abs(countONes(Math.abs(rmask-bmask))) != 1)continue;
					
					flag = true;
					MyLinkedList tList = new DLinkedList();
					MyLinkedList oList = new DLinkedList();
					for(int k=0;k<curr.size();k++){
						tList.add(curr.get(k));
					}
					for(int k=0;k<((MyLinkedList)tempOrg.get(i)).size();k++){
						oList.add(((MyLinkedList)tempOrg.get(i)).get(k));
					}
					for(int k=0;k<((MyLinkedList)tempOrg.get(j)).size();k++){
						oList.add(((MyLinkedList)tempOrg.get(j)).get(k));
					}
					tList.add(Math.abs(bmask-rmask));
					long tempHash = tList.hashIt();
					if(!stored.containsKey(tempHash)){
						temp.add(tList);
						tempOrg.add(oList);
						stored.put(tempHash, true);
					}
					done[j] = true;
					done[i]  = true;
					
				}
			}
			for(int i=0;i<tempSize;i++){
				if(!done[i]){
					temp.add(temp.get(i));
					tempOrg.add(tempOrg.get(i));
				}
			}
			for(int i=0;i<tempSize;i++){
				temp.remove(0);
				tempOrg.remove(0);
			}
			done = new boolean[1000];
			
		}
		return SecondStep(tempOrg,temp,Dont);
	}	
	public static MyLinkedList SecondStep(MyLinkedList temp,MyLinkedList org,MyLinkedList Dont){
		int n =0 ;
		int[] countIt = new int[10000];
		boolean[] skip = new boolean[10000];
		int[] implicants = new int[10000];
		MyLinkedList result = new DLinkedList();
		for(int i=0;i<temp.size();i++){
			for(int j=0;j<((MyLinkedList)temp.get(i)).size();j++){
				if(countIt[(int)((MyLinkedList)temp.get(i)).get(j)] == 0)
					implicants[n++] = (int)((MyLinkedList)temp.get(i)).get(j);
				countIt[(int)((MyLinkedList)temp.get(i)).get(j)]++;
			}
		}
		SecondStepPrint printIt = new SecondStepPrint(n,implicants,temp);
		printIt.print(skip.clone());
		try{
			for(int i=0;i<Dont.size();i++){
				for(int j=0;j<((MyLinkedList)Dont.get(i)).size();j++){
					countIt[(int)((MyLinkedList)Dont.get(i)).get(j)] = 0;
				}
			}
		}catch(Exception ex){}
		
		for(int i=0;i<temp.size();i++){
			for(int j=0;j<((MyLinkedList)temp.get(i)).size();j++){
				if(countIt[((int)((MyLinkedList)temp.get(i)).get(j))] == 1){
					result.add((MyLinkedList)org.get(i));
					for(int k=0;k<((MyLinkedList)temp.get(i)).size();k++){
						countIt[((int)((MyLinkedList)temp.get(i)).get(k))] = 0;
					}
					skip[i] = true;
					printIt.print(skip.clone());
					break;
				}
					
			}
		}
		boolean operating = true;
		while(operating){
			operating = false;
			int maxScore = 0;
			int chosen = -1;
			for(int i=0;i<temp.size();i++){
				if(skip[i])continue;
				boolean flag = false;
				int score = 0;
				for(int j=0;j<((MyLinkedList)temp.get(i)).size();j++){
					if(countIt[((int)((MyLinkedList)temp.get(i)).get(j))] > 0){
						score ++;
						flag = true;
						operating = true;
					}
				}
				
				if(!flag){
					skip[i] = true;
				}
				if(score > 0 && score > maxScore){
					maxScore = score ;
					chosen = i;
				}
				else if(score == maxScore && chosen != -1 && score!=0){
					if(((MyLinkedList)org.get(i)).size() > ((MyLinkedList)org.get(chosen)).size() ){
						maxScore = score ;
						chosen = i;
					}
				}

			}
			if(chosen == -1)break;
			result.add((MyLinkedList)org.get(chosen));
			for(int i=0;i<((MyLinkedList)temp.get(chosen)).size();i++){
				countIt[((int)((MyLinkedList)temp.get(chosen)).get(i))] = 0;
			}
			skip[chosen] = true;
			printIt.print(skip.clone());
		}
		show.show(printIt,result,vars);
//		printIt.show(result,vars);
		return result;
	}
	
	static public String expression(MyLinkedList exp, int bits){
		boolean[] skip = new boolean[100];
		int number = (int)exp.get(0);
		String s = "";
		for(int i=0;i<100;i++)
			skip[i]=false;
		
		for(int i=1;i<exp.size();i++)
			skip[(int)exp.get(i)] = true;
		
		
		for(int i=0;i<bits ; i++){
			if(skip[1<<(bits-i-1)])continue;
			s+=(char)('A'+i);
			if((number&(1<<bits-i-1))==(1<<bits-i-1) )s+="";
			else s+="'";
		}
		if(s == "")s="1";
		return s;
	}

}
/*
 * Enter the number of variables : 5
Enter the minterms : 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63

 0,1,2,5,6,7,8,9,10,14 => f= b'c' + cd' + a'bd
 0,3,5,6,9,10,11,14,15 => y = (x̄3x̄2x̄1x̄0) ∨ (x̄2x1x0) ∨ (x̄3x2x̄1x0) ∨ (x2x1x̄0) ∨ (x3x̄2x0) ∨ (x3x1)
 1,2,3,4,5,7,9,10,11,13,15 => y = (x0), (x̄2x1), (x̄3x2x̄1)
 2,3,7,9,11,13,14 => y = (x̄3x̄2x1) ∨ (x̄3x1x0) ∨ (x3x̄1x0) ∨ (x3x2x1x̄0) ∨ (x̄2x1x0)
 
 1,3,4,5,6 => y = (x̄2x0) ∨ (x2x̄0) ∨ (x̄1x0) => (x̄2x0), (x2x̄0)
 0,1,2,4,5,7,8,9,10,13,14,15,16,18,19,22,24,25,26,28,29,30,31 => y = (x̄2x̄0) ∨ (x̄4x̄3x̄1) ∨ (x̄4x2x0) ∨ (x4x̄3x̄2x1) ∨ (x4x1x̄0) ∨ (x3x̄1x0) ∨ (x3x2x1) ∨ (x4x3x̄1)
 8,9,10,12,16,17,20,21,22,23,26,28,32,33,34,36,37,39,40,41,42,44,47,49,50,52,54,57,58,62,63

1,2,5,9,11,12,14,15,16,17,25_____0,3,4,6,10,13,19,20,21,22,23,24,27, 
0,1,2,3,5,8,10,11,13,15

___9,12,14,18,21,23,29,30____0,1,2,5,6,7,8,10,11,13,20,22,26,28 __ 
 */

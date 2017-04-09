import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;


public class print implements ActionListener {
  int counter=0;
	private JPanel contentPane;
	private JButton next;
	private int vars;
	private SecondStepPrint printIt;
	private MyLinkedList result;
private JFrame mine;
	
	boolean flag = true;
	 int length = 0;
	JLabel[][] grid;
	private JTextField txtMintermMinimization;
	public print( ) {
		mine= new JFrame();
		mine.getContentPane().setBackground(new Color(0, 191, 255));
		grid=new JLabel[100][100];
		for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				grid[i][j] = new JLabel();
//				grid[i][j].setSize(200, 200);
				grid[i][j].setFont(new Font("Snap ITC", Font.PLAIN, 20));
			}
		}
		next = new JButton("Next");
		next.setFont(new Font("Sitka Banner", Font.BOLD | Font.ITALIC, 20));
	   next.addActionListener(this);
		   mine.add(next);
		mine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		mine.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		mine.getContentPane().setLayout(new BorderLayout());
		
		txtMintermMinimization = new JTextField();
		txtMintermMinimization.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		txtMintermMinimization.setText("Minterm minimization");
		mine.getContentPane().add(txtMintermMinimization, BorderLayout.NORTH);
		txtMintermMinimization.setColumns(10);
		mine.setSize(900,600);

	
	}
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
  public void printFirst(MyLinkedList temp){
	  if (flag){
		  length=temp.size();
		  flag=false;
	  }
	  for(int i=0;i<temp.size();i++){
			MyLinkedList curr = (MyLinkedList)temp.get(i);
			String term="";
			for(int j=0;j<curr.size();j++){
				term+= "("+Integer.toString((int)curr.get(j))+")";
			}
			grid[counter][i] = new JLabel(term);
			grid[counter][i].setFont(new Font("Snap ITC", Font.PLAIN, 20));
		}
	  for(int i=temp.size();i<100;i++){
			
			grid[counter][i].setText("");	
			grid[counter][i].setFont(new Font("Snap ITC", Font.PLAIN, 20));
		}
	  counter++;
  }
  
   public void show(SecondStepPrint printI,MyLinkedList res,int var){
	   for(int i=0;i<counter;i++){
		   for(int j=0;j<100;j++){
//			   mine.getContentPane().add(grid[i][j]);
			   mine.getContentPane().add(grid[i][j]);
			   grid[i][j].setBounds(i*250,j*20+30,250,20);
		   }
	   }
	   next.setBounds(mine.getWidth()-120, mine.getHeight()-60, 100, 31);
	   
	   result = res;
	   vars = var;
	   printIt = printI;
	   mine.setVisible(true);
   }
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == next){
			mine.dispose();
			printIt.show(result,vars);
		}
		
	}
	

}

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;


public class End extends JPanel implements ActionListener{
   int vars;
   String term;
   JFrame gui = new JFrame();
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	MyLinkedList list ;
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
	/**
	 * Create the frame.
	 */
	public void print(MyLinkedList list){
		 term="";
		 int x=list.size();
		  for(int i=0;i<x;i++){
				MyLinkedList curr = (MyLinkedList)list.get(i);
					term+=expression(curr,vars);
					if(i!=list.size()-1)
					term+="+";
					System.out.println(term);
			}
	}
	public End(MyLinkedList exp,int vars) {
		this.vars=vars;
		System.out.println(exp.size());
		list = new DLinkedList();
		list=exp;
		print(exp);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setBounds(100, 100, 1260, 284);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 191, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		gui.setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel lblNewLabel = new JLabel(term);
		lblNewLabel.setFont(new Font("Snap ITC", Font.PLAIN, 16));
		lblNewLabel.setBounds(35, 55, 1035, 94);
		contentPane.add(lblNewLabel);
		JLabel lblNewLabel_1 = new JLabel("The final minization expression :");
		lblNewLabel_1.setFont(new Font("Traditional Arabic", Font.BOLD | Font.ITALIC, 24));
		lblNewLabel_1.setBounds(494, 13, 369, 45);
		contentPane.add(lblNewLabel_1);
		JButton generate = new JButton("Generate Output File");
		generate.setBounds(150, 180, 150, 30);
		generate.addActionListener(this);
		contentPane.add(generate);
		gui.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			PrintWriter writer = new PrintWriter("output.txt");
			writer.println(term);
			writer.close();
			JOptionPane.showMessageDialog(gui, "output.txt has been generated !");
		}catch(Exception ex){
			JOptionPane.showMessageDialog(gui, "Error Occured");
		}
		
	}
}


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
 
public class SecondStepPrint extends JPanel implements ActionListener{
    JFrame frame = new JFrame("");
    JButton next;
    Object[][] data;
    Object[] columnNames;
    int counter = 1;
    MyLinkedList result;
    int vars ;
    JScrollPane scroll;
    JPanel gui;
    public SecondStepPrint(int n,int[] implicants,MyLinkedList temp) {
	    data = new Object[temp.size()][n+1];
	    columnNames = new Object[n+1];
	    columnNames[0] = "          ";
	    for(int i=0;i<n;i++){
	    	columnNames[i+1] = implicants[i];
	    }
	    for(int i=0;i<temp.size();i++){
	    	MyLinkedList curr = (MyLinkedList)temp.get(i);
	    	String tempStr = "";
	    	for(int j=0;j<curr.size();j++){
	    		int t = (int)curr.get(j);
	    		if(j > 0)
	    			tempStr += ", "+Integer.toString(t);
	    		else
	    			tempStr += Integer.toString(t);
	    		for(int k=0;k<n;k++){
	    			if(implicants[k] == t){
	    				data[i][k+1] = "X";
	    				break;
	    			}
	    		}
	    	}
	    	data[i][0] = tempStr;
	    }

		next = new JButton("Next");
		next.setFont(new Font("Sitka Banner", Font.BOLD | Font.ITALIC, 20));
	    next.addActionListener(this);
//	    next.setBounds(500,10,100,50);
	    gui = new JPanel();
//	    frame.getContentPane().setLayout(new GridBagLayout());
	    gui.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridx  =0;
	    c.gridy = 0;
//	    frame.getContentPane().add(next,c);
	    gui.add(next, c);
	    scroll = new JScrollPane(gui);
	    scroll.setPreferredSize(new Dimension(900,800));
    	frame.add(scroll);
    }
    public void print(final boolean[] skip){
    	JTable table = new JTable(data, columnNames){
	        public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
	            Component returnComp = super.prepareRenderer(renderer, row, column);
	            if (!returnComp.getBackground().equals(getSelectionBackground())){
	            	if(skip[row])
	            		returnComp .setBackground(Color.red);
	            	else
	            		returnComp .setBackground(Color.WHITE);
            		
	            }
	            return returnComp;
	        }
	    };
	 
	        //Create the scroll pane and add the table to it.
	    JScrollPane scrollPane = JTable.createScrollPaneForTable(table);
	    scrollPane.setPreferredSize(new Dimension(900, 150));
	    GridBagConstraints c = new GridBagConstraints();
	    c.gridx = 0;
	    c.gridy = counter++;
        gui.add(scrollPane,c);
    }
    public void show(MyLinkedList res,int var){
//    	scroll.setSize(1000, 900);
//    	frame.setSize(1000, 900);
    	scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	scroll.setVisible(false);
    	scroll.setVisible(true);
    	frame.setTitle("Second Step");
//        frame.getContentPane().setSize(400, 1100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.repaint();
        frame.revalidate();
        vars = var;
        result = res;
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == next){
			frame.dispose();
			End print = new End(result,vars);
		}
		
	}
}
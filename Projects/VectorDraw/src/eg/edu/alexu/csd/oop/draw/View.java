package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class View implements Observer{
	private JComponent myCanvas;
	private ActionListener btnActionListener;
	
	private JFrame frame;
	private JPanel panel;
	private JLabel myPos;
	private JButton btnRedo;
	private JButton btnUndo;
	private JButton btnSave;
	private JButton btnLoad;
	private JButton btnLoadPlugin;
	private JColorChooser jc;
	private JColorChooser jfc;
	private Color myClr ;
	private Color myFillClr;
	private LinkedList<JButton> myBtns;
	@Override
	public void update(Observable arg0, Object arg1) {
		myCanvas.repaint();
		
	}
	public void initialize(JComponent sentCanv, ActionListener buttonAL) {
		myBtns = new LinkedList<JButton>();
		myCanvas =  sentCanv;
		btnActionListener = buttonAL;
		myCanvas.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				myPos.setText("                                      X :  " +String.valueOf(e.getX()) + " , Y : "+String.valueOf(e.getY()));
			}
		});
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1280, 720);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.black);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(50,50,50));
		panel.setBounds(0,15,110,720);
		frame.getContentPane().add(panel);

		myPos = new JLabel("                                      X :  , Y : ");
		myPos.setForeground(Color.green);
		myPos.setBounds(0,0,1280,15);
		frame.getContentPane().add(myPos);

		btnRedo = new JButton(new ImageIcon("icons/undo_green.png"));
		btnRedo.setBackground(new Color(50,50,50));
		btnRedo.setBounds(60,720-180,48,48);
		btnRedo.setFocusable(false);
		btnRedo.setBorder(null);
		btnRedo.addActionListener(btnActionListener);
		btnRedo.setName("undo");
		panel.add(btnRedo);
		
		btnUndo = new JButton(new ImageIcon("icons/redo_green.png"));
		btnUndo.setBackground(new Color(50,50,50));
		btnUndo.setBounds(10,720-180,48,48);
		btnUndo.setFocusable(false);
		btnUndo.setBorder(null);
		btnUndo.addActionListener(btnActionListener);
		btnUndo.setName("redo");
		panel.add(btnUndo);
		
		btnSave = new JButton(new ImageIcon("icons/save_green.png"));
		btnSave.setBackground(new Color(50,50,50));
		btnSave.setBounds(60,720-120,48,48);
		btnSave.setFocusable(false);
		btnSave.setBorder(null);
		btnSave.addActionListener(btnActionListener);
		btnSave.setName("save");
		panel.add(btnSave);

		btnLoad = new JButton(new ImageIcon("icons/open_green.png"));
		btnLoad.setBackground(new Color(50,50,50));
		btnLoad.setBounds(10,720-120,48,48);
		btnLoad.setFocusable(false);
		btnLoad.setBorder(null);
		btnLoad.addActionListener(btnActionListener);
		btnLoad.setName("load");
		panel.add(btnLoad);
		
		btnLoadPlugin = new JButton("Plugin");
		btnLoadPlugin.setBackground(Color.black);
		btnLoadPlugin.setForeground(Color.GREEN);
		btnLoadPlugin.setBounds(10,720-240,90,30);
		btnLoadPlugin.setFocusable(false);
		btnLoadPlugin.setBorder(null);
		btnLoadPlugin.addActionListener(btnActionListener);
		btnLoadPlugin.setName("plugin");
		panel.add(btnLoadPlugin);

		myCanvas.setBounds(111,15,1280-111,720-15);
		frame.getContentPane().add(myCanvas);
		frame.setVisible(true);
		
	}

	private void generatorBtns(String name,int index){
		JButton newBtn = new JButton(name);
		newBtn.setForeground(Color.GREEN);
		newBtn.setFocusable(false);
		newBtn.setFocusTraversalKeysEnabled(false);
		newBtn.setBackground(new Color(35,35,35));
		newBtn.setMargin(new Insets(10, 14, 10, 14));
		newBtn.setBounds(5,5*(index+1) + index*30 , 100,30);
		newBtn.addActionListener(btnActionListener);
		newBtn.setName(name);
		myBtns.add(newBtn);
		panel.add(newBtn);
	}
	public void addShapes(LinkedList<String>myList){
		Iterator<String> it = myList.iterator();
		int index = myBtns.size()-1;
		while(it.hasNext()){
			generatorBtns(it.next(),index++);
		}
		panel.repaint();
	}

	public String saveFile(){
		JFileChooser jf = new JFileChooser();
		if(jf.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
			return jf.getSelectedFile().getPath();
		else 
			return null;
	}
	public String loadFile(){
		JFileChooser jf = new JFileChooser();
		if(jf.showOpenDialog(frame) == jf.APPROVE_OPTION)
			return jf.getSelectedFile().getPath();
		else 
			return null;
	}
	public Map<String,Double> getProp(Map<String,Double>myMp,int option ){
		jc = new JColorChooser();
		jfc = new JColorChooser();
		if(myMp == null){
			myMp = new TreeMap<String,Double>();
		}
		JButton btnClr = new JButton("Stroke Color");
		JButton btnFillClr = new JButton("Fill Color");
		if(myMp.containsKey("#CR")){
			Color myClr = new Color(myMp.get("#CR").intValue(),myMp.get("#CG").intValue(),myMp.get("#CB").intValue(),myMp.get("#CA").intValue());
			Color myFillClr = new Color(myMp.get("#CFR").intValue(),myMp.get("#CFG").intValue(),myMp.get("#CFB").intValue(),myMp.get("#CFA").intValue());
			jc.setColor(myClr);
			jfc.setColor(myFillClr);
			myMp.remove("#CR");
			myMp.remove("#CG");
			myMp.remove("#CB");
			myMp.remove("#CA");
			myMp.remove("#CFR");
			myMp.remove("#CFG");
			myMp.remove("#CFB");
			myMp.remove("#CFA");
		}else{
			myClr = new Color(255,255,255,255);
			myFillClr =  new Color(255,180,100,255);
		}
		
		btnClr.setBackground(new Color(myClr.getRed() , myClr.getGreen(), myClr.getBlue()));
		btnClr.setForeground(new Color(255-myClr.getRed() , 255-myClr.getGreen(),255- myClr.getBlue()));
		btnFillClr.setBackground(new Color(myFillClr.getRed() , myFillClr.getGreen(), myFillClr.getBlue()));
		btnFillClr.setForeground(new Color(255-myFillClr.getRed() ,255- myFillClr.getGreen(), 255-myFillClr.getBlue()));
		
		Object[]msg = new Object[myMp.size()*2+2];
		JTextField[] in = new JTextField[myMp.size()];
		int i = 0;
		for( Entry<String,Double>tS : myMp.entrySet() ){
			in[i] = new JTextField(tS.getValue().toString());
			msg[i*2] = tS.getKey();
			msg[i*2 + 1] = in[i];
			i++;
		}
		class clrAction implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				String x = ((JButton)e.getSource()).getText();
				if(x == "Stroke Color"){
					Color tmpClr =jc.showDialog(null, "Stroke Color", new Color(0,0,0,255));
					if( tmpClr != null ){
						myClr = tmpClr;
						((JButton)e.getSource()).setBackground(new Color(myClr.getRed() , myClr.getGreen(), myClr.getBlue()));
						((JButton)e.getSource()).setForeground(new Color(255-myClr.getRed() , 255-myClr.getGreen(),255- myClr.getBlue()));
					}
				}else{ 
					Color tmpClr =  jfc.showDialog(null, "Fill Color", new Color(0,0,0,0));
					if( tmpClr != null ){
						myFillClr = tmpClr;
						((JButton)e.getSource()).setBackground(new Color(myFillClr.getRed() , myFillClr.getGreen(), myFillClr.getBlue() ));
						((JButton)e.getSource()).setForeground(new Color(255-myFillClr.getRed() ,255- myFillClr.getGreen(), 255-myFillClr.getBlue()));
					}
				}
			}	
		}
		btnClr.addActionListener(new clrAction());
		btnFillClr.addActionListener(new clrAction());
		msg[msg.length-2] = btnClr;
		msg[msg.length-1] = btnFillClr;
		String btns[] = new String[( option == 1?2:3 )];
		if(option == 1)
			btns[0] = "Add";
		else 
			btns[0] = "Edit";
		if( option == 1 ){
			btns[1] = "Cancel";
		}else{
			btns[1] = "Remove";
			btns[2] = "Cancel";
		}
		int choice = JOptionPane.showOptionDialog(frame, msg, "Enter the Properties", JOptionPane.WARNING_MESSAGE,0,new ImageIcon("icons/paint.png"),btns,btns[0]);
		if(choice == 0){
			for(i = 0 ; i < myMp.size() ; i++){
				if(in[i].getText().length() == 0){
					showError("Invalid Input");
					return null;
				}
				try{
					myMp.put((String) msg[i*2],Double.valueOf(in[i].getText())) ;
				}catch(Exception ex){
					showError("Invalid Input");
					return null;
				}
			}
			if(myClr == null)
				myClr = new Color(0,0,0,255);
			if(myFillClr == null)
				myFillClr = new Color(0,0,0,0);
			myMp.put("#CR",1.0*myClr.getRed());
			myMp.put("#CG",1.0*myClr.getGreen());
			myMp.put("#CB",1.0*myClr.getBlue());
			myMp.put("#CA",1.0*myClr.getAlpha());
			myMp.put("#CFR",1.0*myFillClr.getRed());
			myMp.put("#CFG",1.0*myFillClr.getGreen());
			myMp.put("#CFB",1.0*myFillClr.getBlue());
			myMp.put("#CFA",1.0*myFillClr.getAlpha());
			return myMp;
		}else if( option == 2 && choice == 1 ){
			Map<String,Double> tmpMp = new HashMap<String,Double>();
			tmpMp.put("#REMOVE#", 0.0);
			return tmpMp;
		}
		return null;
	}
	public void showError(String x){
		JOptionPane.showMessageDialog(frame, x);
	}
	
	
}

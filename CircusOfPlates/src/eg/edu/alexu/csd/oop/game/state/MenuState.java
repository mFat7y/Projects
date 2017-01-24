
package eg.edu.alexu.csd.oop.game.state;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import eg.edu.alexu.csd.oop.game.configurations.GameConfigurations;
import eg.edu.alexu.csd.oop.game.configurations.WorldConfigurations;
import eg.edu.alexu.csd.oop.game.gfx.ImageLoader;

public class MenuState implements State{
	private static MenuState inst = null;
	private StateManager manager;
	private String firstWorldPath,firstWorldName,firstWorldIcon;
	private String secondWorldPath,secondWorldName,secondWorldIcon;
	private String thirdWorldPath,thirdWorldName,thirdWorldIcon;
	private int thirdWorldDif,secondWorldDif,firstWorldDif;
	private int height;
	private int width;
	
	public static State getInst(){
		if( inst == null ){
			inst = new MenuState();}
		return inst;
	}
	private MenuState() {
		GameConfigurations config = GameConfigurations.getInst();
		height = config.getGameHeight();
		width = config.getGameWidth();
		firstWorldPath = config.getFirstWorld();
		secondWorldPath = config.getSecondWorld();
		thirdWorldPath = config.getThirdWorld();
		
		firstWorldIcon = WorldConfigurations.getInst().getIcon(firstWorldPath);
		secondWorldIcon = WorldConfigurations.getInst().getIcon(secondWorldPath);
		thirdWorldIcon = WorldConfigurations.getInst().getIcon(thirdWorldPath);
		
		firstWorldName = WorldConfigurations.getInst().getName(firstWorldPath);
		secondWorldName = WorldConfigurations.getInst().getName(secondWorldPath);
		thirdWorldName = WorldConfigurations.getInst().getName(thirdWorldPath);
		
		firstWorldDif = WorldConfigurations.getInst().getDifficulity(firstWorldPath);
		secondWorldDif = WorldConfigurations.getInst().getDifficulity(secondWorldPath);
		thirdWorldDif = WorldConfigurations.getInst().getDifficulity(thirdWorldPath);		
		
		firstWorldDif = checkDif(firstWorldDif)-1;
		secondWorldDif = checkDif(secondWorldDif)-1;
		thirdWorldDif = checkDif(thirdWorldDif)-1;
		
	}
	private int checkDif(int x){
		if(x < 1)return 1;
		if(x > 3)return 3;
		return x;
	}
	private class myActionListener implements ActionListener {
		 public void actionPerformed(ActionEvent e) {	
			 if( !(e.getSource() instanceof JButton ))
				 return ;
			 level=((JButton)e.getSource()).getName();
			 manager.setCurrState(manager.getGameState());
			 manager.getCurrState().setManager(manager);
			 manager.getCurrState().start(level);
			 frame.dispose();
		}
	 }
	private JFrame frame;
	private JPanel panel;
	private JButton firstWorldBtn,secondWorldBtn,thirdWorldBtn;
	private String level;
	private Integer bestScore1=0,lastScore1=0;
	public void start(String menu){
		frame=new JFrame();
		panel= new JPanel();
	    panel.setPreferredSize(new Dimension(width, height));
		panel.setBackground(new Color(70, 130, 180));
		panel.setLayout(null);
		
		//btns
	    firstWorldBtn=new JButton();
	    secondWorldBtn=new JButton();
	    thirdWorldBtn=new JButton();
	    firstWorldBtn.setName(firstWorldPath);
	    secondWorldBtn.setName(secondWorldPath);
	    thirdWorldBtn.setName(thirdWorldPath);
	    firstWorldBtn.setBounds(200,400, 200, 200);
	    secondWorldBtn.setBounds(550,400, 200, 200);
	    thirdWorldBtn.setBounds(900,400, 200, 200);
	    
	    //labels
	    String dif[] = {"Easy","Medium","Hard"};
	    JLabel beachLabel = new JLabel(dif[firstWorldDif]);
	    JLabel jungleLabel = new JLabel(dif[secondWorldDif]);
	    JLabel circusLabel = new JLabel(dif[thirdWorldDif]);
	    JLabel jungleLabel2 = new JLabel(firstWorldName);
	    JLabel beachLabel2 = new JLabel(secondWorldName);
	    JLabel circusLabel2 = new JLabel(thirdWorldName);
	    JLabel lastScore = new JLabel("");
	    JLabel bestScore = new JLabel("");
	    JLabel menuLabel = new JLabel("Collecting Plates");
	    jungleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
	    menuLabel.setFont(new Font("Times New Roman", Font.BOLD, 100));
	    circusLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
	    beachLabel2.setFont(new Font("Times New Roman", Font.PLAIN, 35));
	    jungleLabel2.setFont(new Font("Times New Roman", Font.PLAIN, 35));
	    circusLabel2.setFont(new Font("Times New Roman", Font.PLAIN, 35));
	    beachLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
	    lastScore.setFont(new Font("Times New Roman", Font.PLAIN, 25));
	    bestScore.setFont(new Font("Times New Roman", Font.PLAIN, 25));
	    jungleLabel.setBounds(610, 580, 150,100);
	    circusLabel.setBounds(970, 580, 150,100);
	    beachLabel.setBounds(270, 580, 150,100);
	    menuLabel.setBounds(310, 100, 1000,200);
	    jungleLabel2.setBounds(190, 300, 250,100);
	    circusLabel2.setBounds(890, 300, 250,100);
	    beachLabel2.setBounds(540, 300, 250,100);
	    lastScore.setBounds(140, 700, 400,100);
	    bestScore.setBounds(140, 750, 400,100);
	    lastScore.setText("Your Score : "+ lastScore1+" Points");
	    bestScore.setText("Your Best   : "+ bestScore1+" Points");
	    
	    firstWorldBtn.addActionListener(new myActionListener());
	    secondWorldBtn.addActionListener(new myActionListener());
	    thirdWorldBtn.addActionListener(new myActionListener());
	    
	    panel.add(circusLabel);
	    panel.add(jungleLabel);
	    panel.add(beachLabel);
	    panel.add(menuLabel);
	    panel.add(jungleLabel2);
	    panel.add(circusLabel2);
	    panel.add(beachLabel2);
	    panel.add(bestScore);
	    panel.add(lastScore);
	    
	    
	    firstWorldBtn.setIcon(new ImageIcon(ImageLoader.getInst().loadImage(firstWorldIcon)));
	    secondWorldBtn.setIcon(new ImageIcon(ImageLoader.getInst().loadImage(secondWorldIcon)));
	    thirdWorldBtn.setIcon(new ImageIcon(ImageLoader.getInst().loadImage(thirdWorldIcon))); 
	    
	    panel.add(firstWorldBtn);
	    panel.add(secondWorldBtn);
	    panel.add(thirdWorldBtn);
	    frame.setContentPane(panel);
	    
	    
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(null);
	    frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setTitle("Collecting Plates");
		frame.setVisible(true);
	    
	    
	}
	public void setManager(StateManager manager){
		this.manager=manager;
	}
	public void setScore(int x){
		lastScore1=x;
		if(lastScore1>bestScore1)bestScore1=lastScore1;
	}
	
	
	


}

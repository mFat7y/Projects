import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI {
	private final int HEIGHT = 600;
	private final int WIDTH = 800;
	private JFrame frame;
	private JPanel panel;
	private MyCanvas canvas;
	private JButton addBtn;
	private MouseLs mls;
	private ArrayList<NodeGUI> nodes;
	private ArrayList<EdgeGUI> edges;
	private NodeGUI selectedNode = null;
	public GUI() {
		frame = new JFrame("SFG");
		panel = new JPanel();
		frame.setContentPane(panel);
		frame.setMinimumSize(new Dimension(WIDTH+20,HEIGHT+100));
		panel.setMinimumSize(new Dimension(WIDTH+20,HEIGHT+100));

		nodes = new ArrayList<NodeGUI>();
		edges = new ArrayList<EdgeGUI>();
		mls = new MouseLs();
		
		canvas = new MyCanvas();
		canvas.addMouseListener(mls);
		canvas.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		
		panel.add(canvas);
		
		addBtn = new JButton("Add Node");
		addBtn.addMouseListener(mls);
		panel.add(addBtn);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	private class MyCanvas extends JPanel{
		@Override
		public void paint(Graphics g2){
			Graphics2D g = (Graphics2D) g2;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setFont(new Font("default",Font.BOLD,14));
			for(EdgeGUI edge : edges){
				edge.draw(g);
			}
			
			for(NodeGUI node : nodes){
				node.draw(g);
			}
		}
	}
	private class MovableBlock extends JComponent{
		private Point last = new Point();
		public MovableBlock() {
				addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				public void mousePressed(MouseEvent e) {
					last = new Point(getX()-e.getXOnScreen(),getY()-e.getYOnScreen());
				}
				
				public void mouseExited(MouseEvent e) {
					setSize(getWidth()-5, getHeight()-5);
					canvas.repaint();
				}
				
				public void mouseEntered(MouseEvent e) {
					setSize(getWidth()+5, getHeight()+5);
					canvas.repaint();
				}
				
				public void mouseClicked(MouseEvent e) {
					mouseClick(e);
				}
			});
			addMouseMotionListener(new MouseMotionListener() {
				
				public void mouseMoved(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				public void mouseDragged(MouseEvent e) {
					int newX = e.getXOnScreen()+last.x;
					int newY = e.getYOnScreen()+last.y;
					if(newX < 0)newX = 0;
					if(newY < 0)newY = 0;
					if(newX+getWidth() >= canvas.getWidth())newX = canvas.getWidth()-getWidth();
					if(newY+getHeight() >= canvas.getHeight())newY = canvas.getHeight()-getHeight();
					setLocation(newX,newY);
					canvas.repaint();
				}
			});
		}
		protected void mouseClick(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}
	private class EdgeWeight extends MovableBlock{
		private String label;
		public EdgeWeight(String label,int x,int y) {
			this.label = label;
			setLocation(x,y);
			setSize(label.length()*10, 20);
			canvas.add(this);
		}
		public void updatePos(int x,int y){
			setLocation(x,y);
		}
		public void draw(Graphics g){
			g.setColor(Color.RED);
			g.fillRect(getX()-2, getY()-getHeight()/2-2, getWidth()+4, getHeight()+4);
			g.setColor(Color.GREEN);
			g.fillRect(getX(), getY()-getHeight()/2, getWidth(), getHeight());
			g.setColor(Color.RED);
			g.drawString(label, getX(), getY()+4);
		}
	}
	private class EdgeGUI{
		private NodeGUI x,dist;
		private double val;
		private EdgeWeight weight;
		private int x1,x2,y1,y2;
		public EdgeGUI(NodeGUI x,NodeGUI dist,double val) {
			this.x = x;
			this.dist = dist;
			this.val = val;
			this.weight = new EdgeWeight(Double.toString(val),(x1+x2)/2,(y1+y2)/2);
			updatePos(true);
		}
		public NodeGUI getX(){
			return x;
		}
		public NodeGUI getDist(){
			return dist;
		}
		public double getVal(){
			return val;
		}
		public void updatePos(boolean updWeight){
			x1 = getX().getX()+getX().getWidth()/2;
			y1 = getX().getY()+getX().getHeight()/2+5;
			x2 = getDist().getX()+getDist().getWidth()/2;
			y2 = getDist().getY()+getDist().getHeight()/2+5;
			if(updWeight)
				weight.updatePos((x1+x2)/2,(y1+y2)/2);
		}
		public void draw(Graphics g){
			updatePos(false);
			g.setColor(Color.GREEN);
			drawArrow(g,x1,y1,weight.getX(),weight.getY());
			drawArrow(g,weight.getX()+weight.getWidth(),weight.getY(),x2,y2);
			weight.draw(g);
		}
	}
	private class NodeGUI extends MovableBlock{
		private String label;
		private Color color;
		private Point last;
		public NodeGUI(String label, int x, int y,int w,int h) {
			this.label = label;
			last = new Point();
			canvas.add(this);
			setLocation(x,y);
			setSize(w*2,h*2+5);
			this.color = new Color((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random()));
		}
		public String getLabel(){
			return label;
		}
		public Color getColor(){
			return color;
		}
		@Override
		public void mouseClick(MouseEvent e){
			if( selectedNode == null )
				selectedNode = (NodeGUI) e.getSource();
			else{
				String temp = JOptionPane.showInputDialog("Enter Value");
				try{
					if(temp != null)
						edges.add(new EdgeGUI(selectedNode,(NodeGUI) e.getSource(),Double.parseDouble(temp)));
					canvas.repaint();
				}finally{
					selectedNode = null;
				}
			}
		}
		public void draw(Graphics g){
			g.setColor(Color.GREEN);
			g.drawString(getLabel(), getX(), getY());
			g.fillOval(getX()-3, getY()+2, getWidth()+6, getHeight()+1);
			g.setColor(getColor());
			g.fillOval(getX(), getY()+5, getWidth(), getHeight()-5);
		}
		
	}
	private class MouseLs extends MouseAdapter{
		boolean flag = false;
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == addBtn){
				flag = true;
			}
			if(e.getSource() == canvas){
				if(!flag)
					return;
				flag = false;
				String label = JOptionPane.showInputDialog("Write the node Label");
				if( label == null )
					return;
				nodes.add(new NodeGUI(label,e.getX(),e.getY(),16,16));
				canvas.repaint();
			}
			
		}
		
	}
	void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		final int ARR_SIZE = 10;
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
	public static void main(String[] atgs){
		new GUI();
	}
}

package signalFlow;
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
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GUI {
	private final int HEIGHT = 600;
	private final int WIDTH = 800;
	private JFrame frame;
	private JPanel panel;
	private MyCanvas canvas;
	private JButton addBtn;
	private JButton solveBtn;
	private JLabel label;
	private JScrollPane scroller;
	private MouseLs mls;
	private ArrayList<NodeGUI> nodes;
	private ArrayList<EdgeGUI> edges;
	private NodeGUI selectedNode = null;
	private Map<Integer,NodeGUI>names;
	public GUI() {
		names = new HashMap<Integer,NodeGUI>();
		frame = new JFrame("SFG");
		panel = new JPanel();
		frame.setContentPane(panel);
		frame.setMinimumSize(new Dimension(450+WIDTH+20,HEIGHT+80));
		panel.setMinimumSize(new Dimension(450+WIDTH+20,HEIGHT+80));
		panel.setBackground(Color.BLACK);

		nodes = new ArrayList<NodeGUI>();
		edges = new ArrayList<EdgeGUI>();
		mls = new MouseLs();
		
		canvas = new MyCanvas();
		canvas.addMouseListener(mls);
		canvas.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		panel.add(canvas);

		label = new JLabel("<html>Add Edges by clicking on the source node then dist node .</html>");
		scroller = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setBounds(canvas.getX()+canvas.getWidth()+5, 10, WIDTH, canvas.getHeight());
		scroller.setMinimumSize(new Dimension(450,HEIGHT));
		scroller.setPreferredSize(new Dimension(450,HEIGHT));
		scroller.getViewport().setBackground(Color.black);
		scroller.setBorder(null);
		label.setForeground(Color.green);
		panel.add(scroller);
		//System.out.println(scroller.getLocation());
		
		addBtn = new JButton("Add Node");
		addBtn.addMouseListener(mls);
		panel.add(addBtn);
		solveBtn = new JButton("Solve Gain");
		solveBtn.addMouseListener(mls);
		panel.add(solveBtn);
		
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
			g.setColor(Color.GREEN);
			g.fillRect(getWidth()-5, 0, getWidth(), getHeight());
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
					if(mls.isSolving())
						return;
					mouseClick(e);
				}
			});
			addMouseMotionListener(new MouseMotionListener() {
				
				public void mouseMoved(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				public void mouseDragged(MouseEvent e) {
					if(mls.isSolving())
						return;
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
		private Edge edge;
		private NodeGUI x,dist;
		private double val;
		private EdgeWeight weight;
		private int x1,x2,y1,y2;
		public EdgeGUI(NodeGUI x,NodeGUI dist,double val) {
			//this.edge = new Edge(val,dist.getNode(),"E("+x.getLabel()+"."+dist.getLabel()+")");
			x.getNode().addNeighbour(dist.getNode(), val, "E("+x.getLabel()+"."+dist.getLabel()+")");
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
			x1 = getX().getX()+getX().getWidth()/2+3;
			y1 = getX().getY()+getX().getHeight()/2+3+5;
			x2 = getDist().getX()+getDist().getWidth()/2+3;
			y2 = getDist().getY()+getDist().getHeight()/2+3+5;
			if( x == dist ){
				x2 = x1+50;
				y2 = y1+50;	
				if(updWeight)
					weight.updatePos(x2,y2);
				else{
					x2 = weight.getX();
					y2 = weight.getY();
				}
			}
			else if(updWeight)
				weight.updatePos((x1+x2)/2,(y1+y2)/2);
		}
		public void draw(Graphics g){
			updatePos(false);
			g.setColor(Color.GREEN);
			if( x == dist ){
				int tx = x1;
				int ty = y1;
				final Ellipse2D.Double el = new Ellipse2D.Double(tx > x2 ? x2 : tx, 
                        ty > y2 ? y2 : ty,
                        Math.abs(tx - x2),
                        Math.abs(ty - y2));
				((Graphics2D) g).draw(el);
				drawArrow(g, (int)el.getCenterX(), (int)el.getMaxY(), (int)el.getCenterX(), (int)el.getMaxY());
			}else{
				if(Math.sqrt((x1-weight.getX())*(x1-weight.getX()) + (y1-weight.getY())*(y1-weight.getY())) 
					<
				Math.sqrt((x1-weight.getX()-weight.getWidth())*(x1-weight.getX()-weight.getWidth()) + (y1-weight.getY()-weight.getHeight())*(y1-weight.getY()-weight.getHeight())) 
						){
					drawArrow(g,x1,y1,weight.getX(),weight.getY());
					drawArrow(g,weight.getX()+weight.getWidth(),weight.getY(),x2,y2);
				}else{
					drawArrow(g,weight.getX(),weight.getY(),x2,y2);
					drawArrow(g,x1,y1,weight.getX()+weight.getWidth(),weight.getY());
				}
			}
			weight.draw(g);
		}
	}
	private class NodeGUI extends MovableBlock{
		private Node node;
		private String label;
		private Color color;
		public NodeGUI(String label, int x, int y,int w,int h) {
			this.node = new Node(names.size()+1);
			names.put(names.size()+1,this);
			this.label = label;
			canvas.add(this);
			setLocation(x,y);
			setSize(w*2,h*2+5);
			this.color = new Color((int)(255*Math.random()),0,(int)(255*Math.random()));
			addMouseListener(mls);
		}
		public Node getNode(){
			return node;
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
					if(temp != null){
						double val = Double.parseDouble(temp);
						edges.add(new EdgeGUI(selectedNode,(NodeGUI) e.getSource(),val));
					}
					canvas.repaint();
				}catch(Exception ex){
				}
				selectedNode = null;
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
		private boolean flag = false;
		private int solving = 0;
		private Node s,t;
		public boolean isSolving(){
			return solving > 0;
		}
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == addBtn){
				flag = true;
			}
			if(e.getSource() == canvas){
				if(solving > 0)
					return;
				if(!flag)
					return;
				flag = false;
				String label = JOptionPane.showInputDialog("Write the node Label");
				if( label == null )
					return;
				nodes.add(new NodeGUI(label,e.getX(),e.getY(),16,16));
				canvas.repaint();
			}
			if(e.getSource() == solveBtn){
				flag = false;
				solving = 1;
				JOptionPane.showMessageDialog(panel, "Choose the Input and Output nodes from the Graph.");
			}
			if(e.getSource() instanceof NodeGUI){
				if(solving == 0)
					return;
				if(solving == 1){
					solving ++;
					s = ((NodeGUI) e.getSource()).getNode();
				}else{
					solving = 0;
					t = ((NodeGUI) e.getSource()).getNode();
					String msg = "";
					Solve slv = new Solve();
					slv.findForward(s,t);
					msg += ("<h3>Forward paths:</h3><ul>");
					for(ArrayList<Node> temp:slv.getNodes()){
						msg += "<li>"+("");
						for(Node x: temp)
							msg += (names.get(x.getValue()).getLabel() + " ");
						msg += "</li>";
					}
					msg += "</ul>"+("");
					msg += "</br>"+("<h3>Forward paths values:</h3><ul>");
					for(ArrayList<Edge> temp:slv.getEdges()){
						msg += "<li>"+("");
						for(Edge x: temp)
							msg += (x.getLabel() + " ");
						msg += "</li>";
					}
					msg += "</ul>";
					msg += ("<h3>Loops:</h3><ul>");
					for(ArrayList<Node> temp:slv.getLoop()){
						msg += "<li>"+("");
						for(Node x: temp)
							msg += (names.get(x.getValue()).getLabel() + " ");
						msg += (" , Degree:" + slv.getLoopDegree().get(temp));
						msg += "</li>";
					}
					msg += "</ul>"+("<h3>Main delta:");
					msg += (slv.deltaS);
					msg += "</h3></br>"+("<h3>Small Deltas:</h3><ul>");
					for(ArrayList<Node> temp:slv.getNodes()){
						msg += "<li>"+(slv.getNodesDeltaS().get(temp)) + " = " + slv.getNodesDelta().get(temp).toString();
						msg += "</li>";
					}

					msg += ("</ul><h3>Loops Gains:</h3><ul>");
					for(ArrayList<Node> temp:slv.getLoop()){
						msg += "<li>"+(slv.getLoopsGain().get(temp))+"</li>";
					}
					msg += ("</ul><br><h3>Gain values of forward paths:</h3><ul>");
					for(ArrayList<Node> temp:slv.getNodes()){
						msg += "<li>"+(slv.getForwardGain().get(temp))+"</li>";
					}
					msg += ("</ul></br><h3>OverAll Gain = " + slv.calculateOverAllGain()+"</h3>");
//					JOptionPane.showMessageDialog(canvas, msg);
					label.setText("<html>"+msg+"</html>");
				}
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
        g.drawLine(0, 0, len, 0);
        len /= 2;
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
	public static void main(String[] atgs){
		new GUI();
	}
}

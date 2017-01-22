package eg.edu.alexu.csd.oop.game.worlds;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import eg.edu.alexu.csd.oop.game.GameLogger;
import eg.edu.alexu.csd.oop.game.GameObject;
import eg.edu.alexu.csd.oop.game.configurations.GameConfigurations;
import eg.edu.alexu.csd.oop.game.entity.Entity;
import eg.edu.alexu.csd.oop.game.entity.Player;
import eg.edu.alexu.csd.oop.game.entity.PlayerMaker;
import eg.edu.alexu.csd.oop.game.entity.shape.Shape;
import eg.edu.alexu.csd.oop.game.entity.shape.ShapeFlyWeightFactory;
import eg.edu.alexu.csd.oop.game.entity.shape.ShapeInfo;
import eg.edu.alexu.csd.oop.game.gfx.BackGround;
import eg.edu.alexu.csd.oop.game.gfx.ImageLoader;
import eg.edu.alexu.csd.oop.game.state.MenuState;
import eg.edu.alexu.csd.oop.game.state.StateManager;

public class MyWorld extends Observable implements eg.edu.alexu.csd.oop.game.World{
	private Integer score,gameSpeed,controlSpeed;
	private int width;
	private int height;
	private int positions[];
	private double delayFactor;
	private boolean isDead;
	private PlayerMaker maker;
	private Player player1, player2;
	private SnapShot snapShot;
	private long lastUpdated = 0,timeOut=100000,startTime;
	private final int DELAY = 1000;
	private final int REWIND_TIME = 10000;
	private long lastRewindUpdate;
	private  List<GameObject> constants= new LinkedList<GameObject>();
	private  List<GameObject> movables = new LinkedList<GameObject>();
	private  List<GameObject> control  = new LinkedList<GameObject>();
	private  List<GameObject> 	  dead   = new LinkedList<GameObject>();
	private List<ShapeInfo> supportedShapes;
	private float shapeSpeed;
	private ShapeInfo sourceShape;
	private BufferedImage background,player;
	private int difficulty;
	private StateManager manager;
	public MyWorld(int difficulty,int gameSpeed,int controlSpeed,float shapeSpeed,double delayFactor,List<ShapeInfo>supportedShapes,BufferedImage player,BufferedImage background,ShapeInfo sourceShape,StateManager manager) {
		this.width = GameConfigurations.getInst().getGameWidth();
		this.height = GameConfigurations.getInst().getGameHeight();
		this.gameSpeed=gameSpeed;
		this.controlSpeed=controlSpeed;
		this.delayFactor=delayFactor;
		this.manager=manager;
		this.score=0;
		this.shapeSpeed = shapeSpeed;
		this.supportedShapes = supportedShapes;
		this.difficulty = difficulty;
		this.sourceShape = sourceShape;
		this.player = player;
		this.background = background;
		this.snapShot = new SnapShot();
		positions = new int[] {2*width/10, 3*width/10, 6*width/10, 7*width/10}; 
		
		init();
		}
	

	private void init() {
	    Entity backGround=new BackGround(0, 0,getWidth(),getHeight(),background);	    
	    Entity source1 = new BackGround(positions[0]-15,0,
	    		sourceShape.getWidth(),
	    		sourceShape.getHeight(),
	    		ImageLoader.getInst().loadImage(sourceShape.getImgPath()));
	    Entity source2 = new BackGround(positions[1]-15,0,sourceShape.getWidth(),sourceShape.getHeight(),ImageLoader.getInst().loadImage(sourceShape.getImgPath()));
	    Entity source3 = new BackGround(positions[2]-15,0,sourceShape.getWidth(),sourceShape.getHeight(),ImageLoader.getInst().loadImage(sourceShape.getImgPath()));
	    Entity source4 = new BackGround(positions[3]-15,0,sourceShape.getWidth(),sourceShape.getHeight(),ImageLoader.getInst().loadImage(sourceShape.getImgPath()));
	    backGround.setVisible(true);
	    source1.setVisible(true);source2.setVisible(true);source3.setVisible(true);source4.setVisible(true);
		constants.add(backGround);
	    constants.add(source1);constants.add(source2);constants.add(source3);constants.add(source4); 
	    Point[] player1Points = {new Point(300 + 56, getHeight() - 280 ), new Point(300 + 145, getHeight() - 280 ),
	    						new Point(300 + 233, getHeight() - 280 )};
	    Point[] player2Points = {new Point(800 + 56, getHeight() - 280 ), new Point(800 + 145, getHeight() - 280 ),
	    						new Point(800 + 233, getHeight() - 280 )};
		maker = new PlayerMaker(this);
		
		maker.setStrategy(difficulty);
		
		player1 = maker.getPlayer1(player1Points, player);
		player2 = maker.getPlayer2(player2Points, player);
		
		player1.setControl(true);
		player1.setVisible(true);
		control.add(player1);
		player2.setControl(true);
		player2.setVisible(true);
		control.add(player2);			
		startTime=System.currentTimeMillis();
		
		updateSnapShot();
	}

	private void updateSnapShot() {
		snapShot.setSnapShot(player1,player2);
	}
	public void rewind(){
		snapShot.getSnapShotPlayer1(player1);
		snapShot.getSnapShotPlayer2(player2);
		timeOut+=REWIND_TIME/3;
		GameLogger.getInst().info("The world has rewinded") ;
	}
	public void addTime(long added){
		timeOut += added;
	}
	public void removeTime(long added){
		timeOut -= added;
		timeOut = Math.max(2000, timeOut);
	}


	@Override
	public boolean refresh() {
		if( System.currentTimeMillis() - lastRewindUpdate > REWIND_TIME ){
			updateSnapShot();
			lastRewindUpdate = System.currentTimeMillis();
		}
		
		shapeSpeed += 0.0001;
		setChanged();
		notifyObservers();
		player1.checkShape();
		player2.checkShape();
		
		
		if( System.currentTimeMillis() - lastUpdated < (int)(delayFactor*DELAY) )
			return true;
		Shape shape = ShapeFlyWeightFactory.getInst().generateRandomShape(this);
		movables.add(shape);
		shape.setVisible(true);
		addObserver(shape);
		lastUpdated = System.currentTimeMillis();

		for(GameObject shape1: movables){
			if(((Entity) shape1).isDead()){
				dead.add(shape1);
			}
		}
		for(GameObject shape1: dead){
			movables.remove(shape1);
		}
		dead.clear();
		for(GameObject shape1: control){
			if(((Entity) shape1).isDead()){
				dead.add(shape1);
			}
		}
		for(GameObject shape1: dead){
			movables.remove(shape1);
			//control.remove(shape1);
		}
		dead.clear();
		if(System.currentTimeMillis()-startTime>=timeOut)return false;
		return true;
		
		
	}
	@Override
	public String getStatus() {
		Long x=(timeOut-(System.currentTimeMillis()-startTime))/1000;
		if(x<-2&&!isDead){
			GameLogger.getInst().info("Game Over") ;
			manager.setCurrState(MenuState.getInst());
			manager.getCurrState().setManager(manager);
			manager.getCurrState().setScore(score);
			manager.getCurrState().start("Collecting Plates");
			isDead=true;
		}
		if(x<=0)return "Game Over";
		
		return "Score: "+score.toString()+" Remaing time : "+ x.toString();
		
	}
	public boolean confirm(Player target, int destination) {
		int temp = Math.abs(destination + target.getX() - player1.getX() - player2.getX());
		return temp < 515 && temp > 485 && (destination) > 30 && (destination + 300) < width;
	}
	
	//getters
	public float getShapeSpeed(){
		return shapeSpeed;
	}
	public List<GameObject> getConstantObjects() {
		return constants;
	}

	@Override
	public List<GameObject> getMovableObjects() {
		return movables;
	}

	@Override
	public List<GameObject> getControlableObjects() {
		return control;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
    

	@Override
	public int getSpeed() {	
		return gameSpeed;
	}
    
	@Override
	public int getControlSpeed() {
		return controlSpeed;
	}
	
	public int getPosition(int i){
		return positions[i];
	}
	
	public boolean isDead(){
		return isDead;
	}


	public List<ShapeInfo> getSupportedShapes() {
		return supportedShapes;
	}

	public void addScore() {
		GameLogger.getInst().info("Score has increased") ;
		score++;	
	}
    public  List<GameObject> getDead(){
    	return dead;
    }
}

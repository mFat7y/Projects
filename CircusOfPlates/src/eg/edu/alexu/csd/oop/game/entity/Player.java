

package eg.edu.alexu.csd.oop.game.entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import eg.edu.alexu.csd.oop.game.GameLogger;
import eg.edu.alexu.csd.oop.game.GameObject;
import eg.edu.alexu.csd.oop.game.entity.shape.Shape;
import eg.edu.alexu.csd.oop.game.worlds.MyWorld;

public class Player extends Entity implements Observer{
	private List<Arm>arms;
	private MyWorld world;
    private boolean moved;
	public Player(MyWorld world, int x, int y,BufferedImage img) {
		super(x, y,300,300,new BufferedImage[]{img});
		GameLogger.getInst().info("Player has been created at x and y") ;
		this.world = world;
		this.arms = new ArrayList<Arm>();
	}
	public void addArm(Arm arm){
		arms.add(arm);
	}
	public List<Arm>getArms(){
		return new ArrayList<Arm>(arms);
	}
	public void checkShape(){
		for(GameObject shape : world.getMovableObjects()){
			for(Arm a : arms){
				a.checkCollision((Shape)shape);
			}
		}
	}
	public boolean isMoved(){
		return moved;
	}
	
	public MyWorld getWorld(){
		return world;
	}
	
	@Override
	public void setX(int x) {
		if(world.confirm(this, x)){
			moved = true;
			for(Arm a:arms){
				a.increasePoint(x - this.x);
			}
			this.x = x;
		}
		else moved  = false;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		world.addScore();
		
	}
}

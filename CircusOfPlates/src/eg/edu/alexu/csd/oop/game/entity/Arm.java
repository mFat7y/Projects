package eg.edu.alexu.csd.oop.game.entity;

import java.awt.Point;
import java.util.Observable;

import eg.edu.alexu.csd.oop.game.GameLogger;
import eg.edu.alexu.csd.oop.game.entity.shape.Shape;
import eg.edu.alexu.csd.oop.game.entity.shape.ShapeFactory;
import eg.edu.alexu.csd.oop.game.entity.shape.ShapeList;

public class Arm extends Observable implements Cloneable{
	private ShapeList plates;
	private Player player;
    private boolean full;
	public Arm(Player player, Point collisionPt){
		plates = new ShapeList();
		this.player = player;
		Shape dummy = ShapeFactory.getInst().generateDummyShape(
				player.getWorld(),
				collisionPt.x-30, collisionPt.y,
				60, 20);
		dummy.setVisible(false);
		dummy.setControl(true);
		dummy.setAttached(true, this.player);
		player.getWorld().getMovableObjects().add(dummy);
		plates.add(dummy);
		addObserver(player);
	}
	
	public void increasePoint(int x){
		plates.getFirst().move(x, 0);
	}
	
	public void checkCollision(Shape shape){
		if(plates.getLast().getY() < 100)
			full = true;
		else full = false;
		if(plates.size() > 0 && !shape.isAttached()&&!full){
			Shape s = plates.getLast();
			if(s.intersects(shape)){
				shape.setAttached(true, player);
				shape.setX(plates.getLast().getX());
				shape.setY(plates.getLast().getY() - shape.getHeight()+10);
				if( shape.shouldBeAdded() ){
					GameLogger.getInst().info("Shape added to the arm") ;
					plates.add(shape);
					shape.setControl(true);
					player.getWorld().getControlableObjects().add(shape);
					player.getWorld().getDead().add(shape);
				}
				if(check()){
					for(int i=0;i<3;i++){
						plates.getLast().setVisible(false);
					    plates.getLast().kill();
						plates.removeLast();		
					}
				}
			}
		}
	}
	public boolean check(){
		if(plates.size()<4)return false;
		Shape first = plates.getLast();
		Shape second = plates.get(plates.size()-2);
		Shape third = plates.get(plates.size()-3);
		if((first.hasColor(second)&&third.hasColor(second))||
			(third.hasColor(first)&&second.hasColor(first))||
			(first.hasColor(third)&&second.hasColor(third))){
			setChanged();
		  notifyObservers();
		  return true;
		}
		return false;
	}
	public void setPlates(ShapeList plates){
		for(Shape s:this.plates){
			s.setVisible(false);
		}
		this.plates = (ShapeList)plates.clone();
		for(Shape p : plates){
			this.addObserver(p);
		}
		for(Shape s:plates){
			s.setVisible(true);
		}
		plates.get(0).setVisible(false);

	}
	public ShapeList getPlates() {
		return (ShapeList) plates.clone();
		
	}
}
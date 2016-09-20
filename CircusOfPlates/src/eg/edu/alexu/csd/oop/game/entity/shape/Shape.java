package eg.edu.alexu.csd.oop.game.entity.shape;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Observer;

import eg.edu.alexu.csd.oop.game.entity.Entity;
import eg.edu.alexu.csd.oop.game.entity.Player;
import eg.edu.alexu.csd.oop.game.worlds.MyWorld;

public abstract class Shape extends Entity implements Observer,ShapeStrategy{
	protected boolean isAttached;
	protected BufferedImage img;
	protected ShapeInfo shapeInfo;
	protected Player player;
	protected int shapeColor;
	protected MyWorld world;
	protected float speed;
	protected String nonColoredImage;
	public Shape(MyWorld myWorld,int x, int y, int width,int height,BufferedImage img,String nonColoredImage,int shapeColor,float speed) {
		super(x, y,width,height, new BufferedImage[]{img});
		this.world = myWorld;
		this.shapeColor = shapeColor;
		this.speed = speed;
		this.nonColoredImage = nonColoredImage;
	}
	public int getColor(){
		return shapeColor;
	}
	public boolean isAttached(){
		return isAttached;
	}
	public void setAttached(boolean attached, Player player){
		isAttached = attached;
		this.player = player;
	}
	public boolean intersects(Shape shape){
		Rectangle bounds = new Rectangle(x,y,width,height);
		Point temp = new Point(shape.getX()+shape.getWidth()/2 , shape.getY()+shape.getHeight()-10);
		return bounds.contains(temp);
	}
	
	public void move(int moveX, int moveY){
		if(!isAttached)y += moveY;
		x += moveX;	
		if(this.y>world.getHeight())
			kill();
	}
	@Override
	public void setY(int y) {
		if(!isControl)
		this.y=y;
		if(this.y>world.getHeight())
			kill();
	}
	@Override
	public void setX(int x) {
		if(isAttached && !player.isMoved())return;
		this.x = x;
	}


}
package eg.edu.alexu.csd.oop.game.entity;

import java.awt.image.BufferedImage;

import eg.edu.alexu.csd.oop.game.GameObject;

public class Entity implements GameObject{
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected boolean visible;
	protected boolean isControl=false,dead= false;
	protected BufferedImage[] images;
		public Entity(int x,int y,int width ,int height,BufferedImage img[]) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.images = img;
	}	
		
	@Override
	public int getX() {
		return x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y;

	}

	@Override
	public void setY(int y) {
		if(isControl)return;
		this.y = y;
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
	public boolean isVisible() {
		return visible;
	}
	@Override
	public BufferedImage[] getSpriteImages() {
		return  images;
	}
	public void setImage(BufferedImage image){
		images[0]=image;	
	}
	public void setVisible(boolean visible){
		this.visible=visible;
	}
	public void setControl(boolean control){
		isControl=control;
	}

	public void kill(){
		dead=true;
	}
	public boolean isDead(){
		return dead;
	}
}	
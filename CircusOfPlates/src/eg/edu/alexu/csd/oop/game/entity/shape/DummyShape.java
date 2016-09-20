package eg.edu.alexu.csd.oop.game.entity.shape;

import java.awt.image.BufferedImage;
import java.util.Observable;

import eg.edu.alexu.csd.oop.game.worlds.MyWorld;

public class DummyShape extends Shape {

	public DummyShape(MyWorld world,int x, int y, int width, int height) {
		super(world,x, y, width, height, new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB),"/lol", 0,0);
	}
	@Override
	public boolean hasColor(Shape shape){
		return false;
	}

	@Override
	public void update(Observable o, Object arg) {
	}
	@Override
	public boolean shouldBeAdded() {
		return false;
	}
}

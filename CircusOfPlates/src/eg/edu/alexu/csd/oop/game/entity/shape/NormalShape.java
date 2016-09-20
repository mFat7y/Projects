package eg.edu.alexu.csd.oop.game.entity.shape;

import java.awt.image.BufferedImage;
import java.util.Observable;

import eg.edu.alexu.csd.oop.game.worlds.MyWorld;

public class NormalShape extends Shape{
	

	public NormalShape(MyWorld world, int x, int y, int width, int height, BufferedImage img, String nonColoredImage,
			int shapeColor, float speed) {
		super(world, x, y, width, height, img, nonColoredImage, shapeColor, speed);
	}

	public boolean hasColor(Shape shape){
		return shapeColor == shape.getColor();
	}

	@Override
	public void update(Observable o, Object arg) {
		move(0,(int)speed);
	}

	@Override
	public boolean shouldBeAdded() {
		return true;
	}
}

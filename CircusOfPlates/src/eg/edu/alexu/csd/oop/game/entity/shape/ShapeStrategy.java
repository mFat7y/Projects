package eg.edu.alexu.csd.oop.game.entity.shape;

import java.util.Observable;

public interface ShapeStrategy {
	public boolean hasColor(Shape shape);

	public void update(Observable o, Object arg);
	public boolean shouldBeAdded();
}

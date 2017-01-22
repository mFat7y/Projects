package eg.edu.alexu.csd.oop.game.entity;

import java.awt.Point;
import java.awt.image.BufferedImage;

public interface PlayerStrategy {
	public void setStrategy(int strategy);
	public Player getPlayer1(Point[] points, BufferedImage image);
	public Player getPlayer2(Point[] points, BufferedImage image);
}

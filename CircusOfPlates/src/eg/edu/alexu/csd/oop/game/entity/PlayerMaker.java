package eg.edu.alexu.csd.oop.game.entity;

import java.awt.Point;
import java.awt.image.BufferedImage;

import eg.edu.alexu.csd.oop.game.worlds.MyWorld;

public class PlayerMaker implements PlayerStrategy{
	private final int EASY = 1, MEDIUM = 2;
	private int strategy;
	private MyWorld world;
	
	public PlayerMaker(MyWorld world){
		this.world = world;
	}
	
	@Override
	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}

	@Override
	public Player getPlayer1(Point[] points, BufferedImage image) {
		if(strategy == EASY)
			return PlayerBuilder.getInst().makeEasyPlayer(world, points[0], 300, world.getHeight()-280, image);
		else if(strategy == MEDIUM)
			return PlayerBuilder.getInst().makeMediumPlayer(world, points, 300, world.getHeight()-280, image);
		else 
			return PlayerBuilder.getInst().makeHardPlayer(world, points, 300, world.getHeight()-280, image);
	}

	@Override
	public Player getPlayer2(Point[] points, BufferedImage image) {
		if(strategy == EASY)
			return PlayerBuilder.getInst().makeEasyPlayer(world, points[0], 800, world.getHeight()-280, image);
		else if(strategy == MEDIUM)
			return PlayerBuilder.getInst().makeMediumPlayer(world, points, 800, world.getHeight()-280, image);
		else 
			return PlayerBuilder.getInst().makeHardPlayer(world, points, 800, world.getHeight()-280, image);
	}

}
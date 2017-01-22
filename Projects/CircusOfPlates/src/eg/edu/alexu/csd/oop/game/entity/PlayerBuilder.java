package eg.edu.alexu.csd.oop.game.entity;

import java.awt.Point;
import java.awt.image.BufferedImage;

import eg.edu.alexu.csd.oop.game.worlds.MyWorld;

public class PlayerBuilder {
	private PlayerBuilder(){}
	
	private static PlayerBuilder inst;
	public static PlayerBuilder getInst(){
		if(inst == null)inst = new PlayerBuilder();
		return inst;
	}
	
	
	public Player makeEasyPlayer(MyWorld world, Point point, int x, int y,BufferedImage img){
		Player player = new Player(world, x, y,img);
		player.addArm(new Arm(player, new Point(point.x + 10, point.y + 25)));
		return player;	
	}
	
	public Player makeMediumPlayer(MyWorld world, Point[] point, int x, int y,BufferedImage img){
		Player player = new Player(world, x, y,img);
		player.addArm(new Arm(player, new Point(point[0].x, point[0].y + 10)));
		player.addArm(new Arm(player, new Point(point[2].x + 25, point[2].y + 10)));
		return player;	
	}
	
	public Player makeHardPlayer(MyWorld world, Point[] point, int x, int y,BufferedImage img){
		Player player = new Player(world, x, y,img);
		player.addArm(new Arm(player, new Point(point[0].x - 10, point[0].y + 30)));
		player.addArm(new Arm(player, new Point(point[1].x, point[1].y + 15)));
		player.addArm(new Arm(player, new Point(point[2].x - 10, point[2].y + 20)));
		return player;
	}
}

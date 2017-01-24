package eg.edu.alexu.csd.oop.game.gfx;

import java.awt.image.BufferedImage;

import eg.edu.alexu.csd.oop.game.entity.Entity;

public class BackGround extends Entity{
	public BackGround(int x, int y,int width,int height,BufferedImage img) {
		super(x, y,width,height,new BufferedImage[]{img});
	}
	

}

package eg.edu.alexu.csd.oop.game.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	private BufferedImage img ;
	private int cellWidth;
	private int cellHeight;
	public SpriteSheet(String path,int cellWidth,int cellHeight) {
		img = ImageLoader.getInst().loadImage(path);
		this.cellHeight = cellHeight;
		this.cellWidth = cellWidth;
	}
	public BufferedImage getImageAt(int x,int y){
		return img.getSubimage(x*cellWidth, y*cellHeight, cellWidth, cellHeight);
	}
}

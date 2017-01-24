package eg.edu.alexu.csd.oop.game.gfx;

import java.awt.image.BufferedImage;

public class Assets {
	
	private static Assets inst;
	private ImageLoader loader;
	private BufferedImage tempBackGround, tempPlayer[];
	
	public static Assets getInstance(){
		if(inst == null)return new Assets();
		return inst;
	}
	
	private Assets() {
		loader = ImageLoader.getInst();
		try{
			tempBackGround = loader.loadImage("/textures/circus2.jpg");
			tempPlayer = new BufferedImage[]{loader.loadImage("/textures/clown .gif")};
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public BufferedImage getBackGroundImage(String name){
		try{
			return loader.loadImage("/textures/" + name + ".png");
		}catch(Exception e){
			return tempBackGround;
		}
	}
	public BufferedImage[] getPlayerImage(String name){
		try{
			return new BufferedImage[]{loader.loadImage("/textures/" + name + ".png")};
		}catch(Exception e){
			return tempPlayer;
		}
	}
	
}

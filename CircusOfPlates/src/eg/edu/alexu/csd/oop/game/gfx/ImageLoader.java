package eg.edu.alexu.csd.oop.game.gfx;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import eg.edu.alexu.csd.oop.game.GameLogger;
import eg.edu.alexu.csd.oop.game.configurations.GameConfigurations;

public class ImageLoader {
	private static ImageLoader inst = null;
	public static ImageLoader getInst(){
		if(inst == null)
			inst = new ImageLoader();
		return inst;
	}
	private int colors[];
	private ImageLoader() {
		colors = GameConfigurations.getInst().getColors();
	}
	public BufferedImage loadImageResource(String path){
		try{
			BufferedImage img = ImageIO.read(this.getClass().getResource(path));
			GameLogger.getInst().info("Succeeded in loading "+path+" from resources.");
			return img;
		}catch(IOException e){
			e.printStackTrace();
			GameLogger.getInst().fatal("Failed to load "+path);
			System.exit(1);
		}
		return null;
	}
	public BufferedImage loadImage(String path){
		try{
			BufferedImage ret =  ImageIO.read(new File(path));
			GameLogger.getInst().info("Succeeded in loading "+path+" .");
			return ret;
		}catch(IOException e){	
			return loadImageResource(path);
		}
	}

	public int getRandomColor(){
		return colors[(int)(Math.random()*(colors.length-1))+1] ;
	}
	public BufferedImage colorImage(BufferedImage img, int color){
		if(img == null)
			return new BufferedImage(50,50,BufferedImage.TYPE_INT_ARGB);
		int pixels[] = img.getRGB(0,0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		BufferedImage newImg= new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
		int newPixels[] = ((DataBufferInt)newImg.getRaster().getDataBuffer()).getData();
		for(int i=0;i<pixels.length;i++){
			if(pixels[i] == Color.black.getRGB() || pixels[i] == Color.BLACK.getRGB())
				newPixels[i] = color;
			else if(pixels[i] == Color.white.getRGB() || pixels[i] == Color.WHITE.getRGB())
				newPixels[i] = new Color(0,0,0,0).getRGB();			
			else
				newPixels[i] = pixels[i];
				
		}
		return newImg;
	}
}

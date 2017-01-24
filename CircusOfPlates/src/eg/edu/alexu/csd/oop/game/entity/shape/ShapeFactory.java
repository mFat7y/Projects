package eg.edu.alexu.csd.oop.game.entity.shape;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.game.GameLogger;
import eg.edu.alexu.csd.oop.game.gfx.ImageLoader;
import eg.edu.alexu.csd.oop.game.worlds.MyWorld;

public class ShapeFactory {
	
	private static ShapeFactory inst = null;
	private Map<String, BufferedImage>preparedImages;
	private Map<String , Map<Integer,BufferedImage> > preparedColoredImages;
	public static ShapeFactory getInst(){
		if( inst == null )
			inst = new ShapeFactory();
		return inst;
	}
	private ShapeFactory() {
		preparedImages = new HashMap<String,BufferedImage>();
		preparedColoredImages = new HashMap<String , Map<Integer,BufferedImage>> ();
	}
	public BufferedImage getImage(String path,int color){
		if( !preparedImages.containsKey(path) )
			preparedImages.put(path, ImageLoader.getInst().loadImage(path));
		if( !preparedColoredImages.containsKey(path) )
			preparedColoredImages.put(path,new HashMap<Integer,BufferedImage>());
		if( color == 0 )
			return preparedImages.get(path);
		Map<Integer,BufferedImage>map = preparedColoredImages.get(path);
		if( !map.containsKey(color) ){
			BufferedImage img = preparedImages.get(path);
			img = ImageLoader.getInst().colorImage(img, color);
			map.put(color, img);
		}
		return map.get(color);
	}
	public Shape generateDummyShape(MyWorld world,int x,int y,int width,int height){
		return new DummyShape(world,x, y, width, height);
	}
	public Shape generateNormalShape(MyWorld world, int x, int y, int width, int height, String img, String nonColored, int color, float speed){
		return new NormalShape(world, x, y, width, height, getImage(img,color), nonColored, color, speed);
	}
	public Shape generateWildShape(Class<? extends Shape>shapeClass,MyWorld world, int x, int y, int width, int height, String img, String nonColored, int color, float speed){
		try{
			Constructor<? extends Shape> c = shapeClass.getConstructor(MyWorld.class, int.class, int.class, int.class, int.class, BufferedImage.class, String.class, int.class, float.class);
			return c.newInstance(world,x,y,width,height,getImage(img,color),nonColored,color,world.getShapeSpeed());
		}catch(Exception ex){
			GameLogger.getInst().fatal(shapeClass.getName()+" Class couldn't be loaded.");
			System.exit(1);
		}
		return null;
	}
	
}

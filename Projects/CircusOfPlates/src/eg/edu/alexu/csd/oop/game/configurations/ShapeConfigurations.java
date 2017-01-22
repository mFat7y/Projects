package eg.edu.alexu.csd.oop.game.configurations;

import java.util.Properties;

import eg.edu.alexu.csd.oop.game.GameLogger;
import eg.edu.alexu.csd.oop.game.entity.shape.ShapeInfo;

public class ShapeConfigurations {
	private static ShapeConfigurations inst = null;
	public static ShapeConfigurations getInst(){
		if(inst == null)
			inst = new ShapeConfigurations();
		return inst;
	}
	private final String DEFAULT_WORLD_PATH = "/default.shape";
	private Properties defShapeProp; 
	private ShapeConfigurations() {

		try{
			defShapeProp = new Properties();
			defShapeProp.load(GameConfigurations.class.getResourceAsStream(DEFAULT_WORLD_PATH));
			GameLogger.getInst().info("Loaded default world") ;
		}catch(Exception ex){
			ex.printStackTrace();
			GameLogger.getInst().fatal("Failed to load default shape file.");
			System.exit(1);
		}
		
		
	}
	public ShapeInfo loadShape(String shapePath){
		int width = getWidth(shapePath);
		int height = getHeight(shapePath);
		String imagePath = getImage(shapePath);
		return new ShapeInfo(width,height,imagePath);
	}
	public int getHeight(String index){
		return PropertiesHandler.getInst().getInt("height",index,defShapeProp);
	}
	public int getWidth(String index){
		return PropertiesHandler.getInst().getInt("width",index,defShapeProp);
	}
	public String getImage(String index){
		return PropertiesHandler.getInst().getString("image",index,defShapeProp);
	}

	
}

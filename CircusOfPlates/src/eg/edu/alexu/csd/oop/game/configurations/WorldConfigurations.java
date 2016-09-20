package eg.edu.alexu.csd.oop.game.configurations;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import eg.edu.alexu.csd.oop.game.GameLogger;
import eg.edu.alexu.csd.oop.game.entity.shape.ShapeInfo;
import eg.edu.alexu.csd.oop.game.gfx.ImageLoader;
import eg.edu.alexu.csd.oop.game.state.StateManager;
import eg.edu.alexu.csd.oop.game.worlds.MyWorld;

public class WorldConfigurations {
	private static WorldConfigurations inst = null;
	public static WorldConfigurations getInst(){
		if(inst == null)
			inst = new WorldConfigurations();
		return inst;
	}
	private final String DEFAULT_WORLD_PATH = "/default.world";
	private Properties defWorldProp; 
	private WorldConfigurations() {
		try{
			defWorldProp = new Properties();
			defWorldProp.load(GameConfigurations.class.getResourceAsStream(DEFAULT_WORLD_PATH));
			GameLogger.getInst().info("default world loaded") ;
		}catch(Exception ex){
			ex.printStackTrace();
			GameLogger.getInst().fatal("Failed to load default world file.");
			System.exit(1);
		}
		
		
	}
	public MyWorld loadWorld(String worldPath,StateManager manager){
		int difficulty = getDifficulity(worldPath);
		int gameSpeed = GameConfigurations.getInst().getGameSpeed();
		int controlSpeed = GameConfigurations.getInst().getControlSpeed();
		float shapeSpeed = getShapeSpeed(worldPath);
		float delayFactor = getDelayFactor(worldPath);
		List<ShapeInfo>SupportedShapes = getWorldShapes(worldPath);
		BufferedImage player = ImageLoader.getInst().loadImage(getPlayer(worldPath));
		BufferedImage background = ImageLoader.getInst().loadImage(getBackground(worldPath));
		ShapeInfo sourceShape = ShapeConfigurations.getInst().loadShape(getSourceShape(worldPath));
		return new MyWorld(difficulty, gameSpeed, controlSpeed, shapeSpeed, delayFactor,SupportedShapes,player,background,sourceShape, manager);
	}
	public int getShapesCount(String index){
		return PropertiesHandler.getInst().getInt("shapes_count",index,defWorldProp);
	}
	public int getDifficulity(String index){
		return PropertiesHandler.getInst().getInt("difficulty",index,defWorldProp);
	}
	public float getDelayFactor(String index){
		return PropertiesHandler.getInst().getFloat("delay_factor",index,defWorldProp);
	}
	public float getShapeSpeed(String index){
		return PropertiesHandler.getInst().getFloat("shape_speed",index,defWorldProp);
	}
	public String getBackground(String index){
		return PropertiesHandler.getInst().getString("background",index,defWorldProp);
	}
	public String getPlayer(String index){
		return PropertiesHandler.getInst().getString("player",index,defWorldProp);
	}
	public String getIcon(String index){
		return PropertiesHandler.getInst().getString("icon",index,defWorldProp);
	}
	public String getName(String index){
		return PropertiesHandler.getInst().getString("name",index,defWorldProp);
	}
	public String getSourceShape(String index){
		return PropertiesHandler.getInst().getString("source_shape",index,defWorldProp);
	}
	
	public List<ShapeInfo> getWorldShapes(String index){
		List<ShapeInfo>list = new ArrayList<ShapeInfo>();
		ShapeConfigurations config = ShapeConfigurations.getInst();
		int s = getShapesCount(index);
		for(int i=1;i<=s;i++){
			String shapePath = PropertiesHandler.getInst().getString("shape_"+i, index,defWorldProp);
			list.add(config.loadShape(shapePath));
		}
		if(list.isEmpty())
			list.add(config.loadShape(GameConfigurations.getInst().getDefaultShape()));
		return list;
	}
}

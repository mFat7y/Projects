package eg.edu.alexu.csd.oop.game.configurations;

import java.awt.Color;
import java.io.IOException;
import java.util.Properties;

import eg.edu.alexu.csd.oop.game.GameLogger;

public class GameConfigurations {
	private static GameConfigurations inst = null;
	public static GameConfigurations getInst(){
		if(inst == null)
			inst = new GameConfigurations();
		return inst;
	}
	private final String GAME_PROP_PATH = "config/game.prop";
	private final String GAME_DEFAULT_PATH = "/default.prop";
	private Properties defGameProp; 

	private GameConfigurations() {
		defGameProp = new Properties();
		try {
			defGameProp.load(GameConfigurations.class.getResourceAsStream(GAME_DEFAULT_PATH));
		} catch (IOException e) {
			e.printStackTrace();
			GameLogger.getInst().fatal("Default Game Properties File isn't found");
			System.exit(1);
		}
		PropertiesHandler.getInst().getProp(GAME_PROP_PATH, defGameProp);
	}
	public int getColorCount(){
		return PropertiesHandler.getInst().getInt("colors_count",GAME_PROP_PATH,defGameProp);
	}
	public int[] getColors(){
		int s = getColorCount();
		int []colors = new int[s+1];
		colors[0] = Color.black.getRGB();
		for(int i=1;i<=s;i++)
			colors[i] = PropertiesHandler.getInst().getInt("color_"+i,GAME_PROP_PATH,defGameProp);
		return colors;
	}
	public int getDevMode() {
		return PropertiesHandler.getInst().getInt("dev_mode",GAME_PROP_PATH,defGameProp);
	}public int getWildShapeFreq() {
		return PropertiesHandler.getInst().getInt("wildshape_freq",GAME_PROP_PATH,defGameProp);
	}
	public float getGameAcceleration(){
		return PropertiesHandler.getInst().getFloat("game_acceleration",GAME_PROP_PATH,defGameProp);
	}
	public int getGameHeight(){
		return PropertiesHandler.getInst().getInt("height",GAME_PROP_PATH,defGameProp);
	}
	public int getGameWidth(){
		return PropertiesHandler.getInst().getInt("width",GAME_PROP_PATH,defGameProp);
	}
	public int getGameSpeed(){
		return PropertiesHandler.getInst().getInt("game_speed",GAME_PROP_PATH,defGameProp);
	}
	public int getControlSpeed(){
		return PropertiesHandler.getInst().getInt("control_speed",GAME_PROP_PATH,defGameProp);
	}
	public String getDefaultShape(){
		return PropertiesHandler.getInst().getString("default_shape",GAME_PROP_PATH,defGameProp);
	}
	public String getDefaultWorld(){
		return PropertiesHandler.getInst().getString("default_world",GAME_PROP_PATH,defGameProp);
	}
	public String getFirstWorld(){
		return PropertiesHandler.getInst().getString("first_world",GAME_PROP_PATH,defGameProp);
	}
	public String getSecondWorld(){
		return PropertiesHandler.getInst().getString("second_world",GAME_PROP_PATH,defGameProp);
	}
	public String getThirdWorld(){
		return PropertiesHandler.getInst().getString("third_world",GAME_PROP_PATH,defGameProp);
	}
	
}

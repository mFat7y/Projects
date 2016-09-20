package eg.edu.alexu.csd.oop.game.configurations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import eg.edu.alexu.csd.oop.game.GameLogger;

public class PropertiesHandler {
	private static PropertiesHandler inst;
	public static PropertiesHandler getInst(){
		if(inst==null)inst = new PropertiesHandler();
		return inst;
	}
	private Map<String,Properties> storedProp;
	private PropertiesHandler() {
		storedProp = new HashMap<String,Properties>();
	}
	private Properties getPropFromResource(String path,Properties defstoredProp){
		try{
			Properties temp = new Properties();
			temp.load(GameConfigurations.class.getResourceAsStream(path));
			storedProp.put(path, temp);
		}catch(Exception ex){
			GameLogger.getInst().warn(path+" Couldn't be loaded from resources, default loaded");
			storedProp.put(path, (Properties)defstoredProp.clone());
			try{
				File file = new File(path);
				try{
					file.getParentFile().mkdirs();
				}catch(Exception e){
					
				}
				defstoredProp.store(new FileOutputStream(path), "Defaults loaded");
			}catch(Exception e){
				GameLogger.getInst().warn("Couldn't create a file with defaults");
			}
		}
		return storedProp.get(path);
	}
	public void addProp(String path,Properties prop){
		storedProp.put(path, prop);
	}
	public Properties getProp(String path,Properties defstoredProp){
		if( !storedProp.containsKey(path) ){
			try{
				Properties temp = new Properties();
				temp.load(new FileInputStream(path));
				storedProp.put(path, temp);
			}catch(Exception ex){
				return getPropFromResource(path,defstoredProp);
			}
		}
		return storedProp.get(path);
	}
	public int getInt(String key,String index,Properties defstoredProp){
		int val = 0;
		Properties prop = getProp(index,defstoredProp);
		try{
			val = Integer.valueOf(prop.getProperty(key).trim());
			return val;
		}catch(Exception ex){
			GameLogger.getInst().error(key+" couldn't be loaded from file , loading defaults.");
		}
		try{
			val = Integer.valueOf(defstoredProp.getProperty(key).trim());
			return val;
		}catch(Exception ex){
			GameLogger.getInst().fatal(key+" couldn't be loaded from defaults.");
			System.exit(1);
		}
		return val;
	}

	public float getFloat(String key,String index,Properties defstoredProp){
		float val = 0;
		Properties prop = getProp(index,defstoredProp);
		try{
			val = Float.valueOf(prop.getProperty(key).trim());
			return val;
		}catch(Exception ex){
			GameLogger.getInst().error(key+" couldn't be loaded from file , loading defaults.");
		}
		try{
			val = Float.valueOf(defstoredProp.getProperty(key).trim());
			return val;
		}catch(Exception ex){
			GameLogger.getInst().fatal(key+" couldn't be loaded from defaults.");
			System.exit(1);
		}
		return val;
	}
	public String getString(String key,String index,Properties defstoredProp){
		String val = "";
		Properties prop = getProp(index,defstoredProp);
		try{
			val = prop.getProperty(key).trim();
			return val;
		}catch(Exception ex){
			GameLogger.getInst().error(key+" couldn't be loaded from file , loading defaults.");
		}
		try{
			val = defstoredProp.getProperty(key).trim();
			return val;
		}catch(Exception ex){
			GameLogger.getInst().fatal(key+" couldn't be loaded from defaults.");
			System.exit(1);
		}
		return val;
	}
	
}

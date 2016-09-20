package eg.edu.alexu.csd.oop.game;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import eg.edu.alexu.csd.oop.game.configurations.GameConfigurations;

public class GameLogger {
	private static Logger inst = null ;
	public static Logger getInst(){
		if(inst == null){
			inst = Logger.getLogger(Launcher.class) ;
			init();

			return inst ;
		}
		return inst ;
	}
	private static void init() {
		PropertyConfigurator.configure(GameLogger.class.getResource("/Log4j.properties"));
		int level = GameConfigurations.getInst().getDevMode();
		level = Math.max(0, level);
		inst.setLevel(level != 0?Level.ALL:Level.OFF);
	}
	private GameLogger() {}
	
}

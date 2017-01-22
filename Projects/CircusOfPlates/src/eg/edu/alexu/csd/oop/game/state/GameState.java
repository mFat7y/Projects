package eg.edu.alexu.csd.oop.game.state;

import eg.edu.alexu.csd.oop.game.GameEngine;
import eg.edu.alexu.csd.oop.game.configurations.WorldConfigurations;
import eg.edu.alexu.csd.oop.game.worlds.MyWorld;
public class GameState implements State{
	private static GameState inst = null;
	private StateManager manager;
	public static GameState getInst(){
		if( inst == null )
			inst = new GameState();
		return inst;
	}
	private GameState() {}
	public void start(String level){
		MyWorld world = WorldConfigurations.getInst().loadWorld(level, manager);
	    	 GameEngine.start(level, world);
		
	}
	@Override
	public void setManager(StateManager manager) {
		this.manager=manager;
		
	}
	@Override
	public void setScore(int x) {
	}
		
}

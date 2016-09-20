package eg.edu.alexu.csd.oop.game.state;


public class StateManager {
	private State currState;
	private State gameState;
	private State menuState;
	public StateManager() {
		gameState = GameState.getInst();
		menuState = MenuState.getInst();
		currState = menuState;
	}
	public State getCurrState(){
		return currState;
	}
	public void setCurrState(State m){
		currState=m;
	}
	public State getGameState() {
		return gameState;
	}
	public State getMenuState() {
		return menuState;
	}
	
	
}

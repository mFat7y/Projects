package eg.edu.alexu.csd.oop.game;



import eg.edu.alexu.csd.oop.game.state.StateManager;


public class Launcher {
	public static void main(String arg[]) {
		StateManager manager=new StateManager();
		manager.setCurrState(manager.getMenuState());
		manager.getCurrState().setManager(manager);
		manager.getCurrState().start("Collecting Plates");
	}
}

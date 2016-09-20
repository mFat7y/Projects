package eg.edu.alexu.csd.oop.game.state;


public interface  State {
   public void setManager(StateManager manager);

	public void start(String level);
	public void setScore(int x);
}

package eg.edu.alexu.csd.oop.game.worlds;

import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.game.entity.Arm;
import eg.edu.alexu.csd.oop.game.entity.Player;
import eg.edu.alexu.csd.oop.game.entity.shape.ShapeList;

public class SnapShot {
	private Map<Arm,ShapeList> player1SnapShot;
	private Map<Arm,ShapeList> player2SnapShot;
	public SnapShot() {
		player1SnapShot = new HashMap<Arm,ShapeList>();
		player2SnapShot = new HashMap<Arm,ShapeList>();
	}
	public void getSnapShotPlayer1(Player player1){
		for(Arm a : player1.getArms()){
			a.setPlates((ShapeList)(player1SnapShot.get(a)).clone());
		}
	}
	public void getSnapShotPlayer2(Player player2){
		for(Arm a : player2.getArms()){
			a.setPlates((ShapeList)(player2SnapShot.get(a)).clone());
		}
	}
	public void setPlayer1(Player player1){
		for(Arm a : player1.getArms()){
			player1SnapShot.put(a,a.getPlates());
		}
	}
	public void setPlayer2(Player player2){
		for(Arm a : player2.getArms()){
			player2SnapShot.put(a,a.getPlates());
		}
	}
	
	public void setSnapShot(Player player1,Player player2){
		setPlayer1(player1);
		setPlayer2(player2);
	}
}

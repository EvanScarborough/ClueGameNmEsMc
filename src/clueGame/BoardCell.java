package clueGame;

public class BoardCell {
	public int x;
	public int y;
	
	public String toString(){
		return Integer.toString(x) + " " + Integer.toString(y);
	}
	
	public boolean isDoorway(){
		return false;
	}
	
	public DoorDirection getDoorDirection(){
		return null;
	}
	
	public char getInitial(){
		return ' ';
	}
}

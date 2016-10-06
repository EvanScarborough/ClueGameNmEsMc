package clueGame;

public class BoardCell {
	public int x;
	public int y;
	public char initial;
	public DoorDirection doorDir;
	
	public String toString(){
		return Integer.toString(x) + " " + Integer.toString(y);
	}
	
	public boolean isDoorway(){
		if (doorDir != DoorDirection.NONE) return true;
		
		return false;
	}
	
	public DoorDirection getDoorDirection(){
		return doorDir;
	}
	
	public char getInitial(){
		return initial;
	}
}

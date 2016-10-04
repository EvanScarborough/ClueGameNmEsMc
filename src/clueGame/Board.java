package clueGame;

import java.util.Map;

public class Board {
	
	public static Board theInstance = new Board();
	
	private Board(){}
	
	public static Board getInstance(){return theInstance;}
	
	public static void setConfigFiles(String layout, String legend){
		
	}
	
	public static void initialize(){
		
	}
	
	public static Map<Character, String> getLegend(){
		return null;
	}
	
	public static int getNumRows(){
		return 0;
	}
	public static int getNumColumns(){
		return 0;
	}
	
	public static BoardCell getCellAt(int x, int y){
		return null;
	}
}

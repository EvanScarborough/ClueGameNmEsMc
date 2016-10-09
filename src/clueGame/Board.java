package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	String boardFile;
	String legendFile;
	
	int numRows = 0;
	int numColumns = 0;
	
	static BoardCell[][] board;
	
	public static Board theInstance = new Board();
	Map<Character, String> myMap;
	
	private Board(){}
	
	public static Board getInstance(){return theInstance;}
	
	public void setConfigFiles(String layout, String legend){
		boardFile = layout;
		legendFile = legend;
	}
	
	public void initialize(){ //throws BadConfigFormatException{
		try {
			loadRoomConfig();
			loadBoardConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public Map<Character, String> getLegend(){
		return myMap;
	}
	
	public int getNumRows(){
		return numRows;
	}
	public int getNumColumns(){
		return numColumns;
	}
	
	public BoardCell getCellAt(int x, int y){
		return board[x][y];
	}

	public void loadRoomConfig() throws BadConfigFormatException{
		FileReader reader = null;
		Scanner in = null;
		
		try {
			reader = new FileReader(legendFile);
			in = new Scanner(reader);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return;
		}
		myMap = new HashMap<Character, String>();
		
		while(in.hasNext()){
			String s = in.next();
			char c = s.charAt(0);
			s = in.next();
			while(s.charAt(s.length()-1) != ','){
				s += " " + in.next();
			}
			myMap.put(c,s.substring(0, s.length() - 1));
			s = in.next();
			if(!s.equals("Card") && !s.equals("Other")){
				throw new BadConfigFormatException("Bad Legend Format: Bad Card Type");
			}
		}
		
		/*Set<Character> myKeys = new HashSet<Character>();
		while (in.hasNext()){
			String s = in.next();
			char c = s.charAt(0);
			myKeys.add(c);
			s = in.next();
			while(s.charAt(s.length()-1) != ','){
				s += " " + in.next();
			}
			if (myMap.containsKey(c)){
				myMap.put(c,s.substring(0, s.length() - 1));
			}
			else{
				//System.out.println(c);
				throw new BadConfigFormatException("Found a legend entry not on the board!");
			}
			s = in.next();
		}
		
		for(char c: myMap.keySet()){
			if(myMap.get(c).equals("")) throw new BadConfigFormatException("Found a char that isn't in the legend!");
		}
		*/
		
	}

	public void loadBoardConfig() throws BadConfigFormatException{
		FileReader reader = null;
		Scanner in = null;
		
		try {
			reader = new FileReader(boardFile);
			in = new Scanner(reader);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		ArrayList<String> boardInit = new ArrayList<String>();
		
		while(in.hasNext()){
			try{
				boardInit.add(in.nextLine());
			} catch(Exception e){
				//throw new BadConfigFormatException();
			}
		}
		
		numRows = boardInit.size();
		
		String input = boardInit.get(0);
		for (int i = 0; i < input.length(); i++){
			if (input.charAt(i) == ',') numColumns++;
		}
		numColumns++;
		
		for(int i = 0; i < numRows; i++){
			input = boardInit.get(i);
			int cols = 0;
			for (int j = 0; j < input.length(); j++){
				if (input.charAt(j) == ',') cols++;
			}
			if(cols != numColumns - 1) throw new BadConfigFormatException("Cols don't match");
		}
		
		board = new BoardCell[numRows][numColumns];
		
		for(int i = 0; i < numRows; i++){
			//System.out.println("kajsdlkjafls");
			input = boardInit.get(i);
			Scanner s = new Scanner(input).useDelimiter("\\s*,\\s*");
			for(int j = 0; j < numColumns; j++){
				String whatever = s.next();
				
				if(!myMap.containsKey(whatever.charAt(0))) throw new BadConfigFormatException("Isn't in the legend");
				board[i][j] = new BoardCell();
				
				board[i][j].initial = whatever.charAt(0);
				board[i][j].x = j;
				board[i][j].y = i;
				
				board[i][j].doorDir = DoorDirection.NONE;
				if (whatever.length() > 1){
					char c = whatever.charAt(1);
					switch(c){
					case 'U':
						board[i][j].doorDir = DoorDirection.UP;
						break;
					case 'D':
						board[i][j].doorDir = DoorDirection.DOWN;
						break;
					case 'L':
						board[i][j].doorDir = DoorDirection.LEFT;
						break;
					case 'R':
						board[i][j].doorDir = DoorDirection.RIGHT;
						break;
					}
					
				}
			}
		}
		//System.out.println("cats");
		/*for(char c: myMap.keySet()){
			System.out.println(c);
		}*/
		
	}
	
	public Set<BoardCell> getAdjList(int x, int y){
		return null;
	}
	
	public void calcTargets(int x, int y, int distance){
		
	}
	
	public Set<BoardCell> getTargets(){
		return null;
	}
	
}

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
	
	private Map<BoardCell, Set<BoardCell>> adjMtx;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	
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
			e.printStackTrace();
		}
		
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		adjMtx = new HashMap<BoardCell, Set<BoardCell>>();
		
		calcAdjacencies();
	}
	
	private void calcAdjacencies(){
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numColumns; j++){
				adjMtx.put(getCellAt(i,j), new HashSet<BoardCell>());
				
				if(getCellAt(i,j).doorDir == DoorDirection.DOWN){
					adjMtx.get(getCellAt(i,j)).add(getCellAt(i+1,j));
					continue;
				}
				if(getCellAt(i,j).doorDir == DoorDirection.UP){
					adjMtx.get(getCellAt(i,j)).add(getCellAt(i-1,j));
					continue;
				}
				if(getCellAt(i,j).doorDir == DoorDirection.LEFT){
					adjMtx.get(getCellAt(i,j)).add(getCellAt(i,j-1));
					continue;
				}
				if(getCellAt(i,j).doorDir == DoorDirection.RIGHT){
					adjMtx.get(getCellAt(i,j)).add(getCellAt(i,j+1));
					continue;
				}
				if(getCellAt(i,j).initial != 'W'){
					continue;
				}
				if(isValid(i-1,j)){ //Cell Above
					if((getCellAt(i-1,j).initial == 'W' || getCellAt(i-1,j).doorDir == DoorDirection.DOWN)){
						adjMtx.get(getCellAt(i,j)).add(getCellAt(i-1,j));
					}
				}
				if(isValid(i+1,j)){ //Cell Below
					if((getCellAt(i+1,j).initial == 'W' || getCellAt(i+1,j).doorDir == DoorDirection.UP)){
						adjMtx.get(getCellAt(i,j)).add(getCellAt(i+1,j));
					}
				}
				if(isValid(i,j-1)){ //To the Left
					if((getCellAt(i,j-1).initial == 'W' || getCellAt(i,j-1).doorDir == DoorDirection.RIGHT)){
						adjMtx.get(getCellAt(i,j)).add(getCellAt(i,j-1));
					}
				}
				if(isValid(i,j+1)){ //To the Right
					if((getCellAt(i,j+1).initial == 'W' || getCellAt(i,j+1).doorDir == DoorDirection.LEFT)){
						adjMtx.get(getCellAt(i,j)).add(getCellAt(i,j+1));
					}
				}
				
			}
		}
	}
	
	public void printBoard(){
		for(int y = 0; y < numRows; y++){
			for(int x = 0; x < numColumns; x++){
				System.out.print(getCellAt(y,x).initial);
			}
			System.out.println();
		}
	}
	
	private boolean isValid(int y, int x){
		return x >= 0 && y >= 0 && y < numRows && x < numColumns;
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
	
	public BoardCell getCellAt(int y, int x){
		return board[y][x];
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
	
	public Set<BoardCell> getAdjList(int y, int x){
		return adjMtx.get(getCellAt(y,x));
	}
	
	public void calcTargets(int y, int x, int distance){
		targets.clear();
		visited.clear();
		if(y == 25 && x == 20 && distance == 6) System.out.println("!!!!!!!!!!!!!");
		targetCalc(y,x,distance);
	}
	private void targetCalc(int y, int x, int distance){
		//System.out.println("targetCalc:" + y + " " + x);
		visited.add(getCellAt(y,x));
		for (BoardCell bork: getAdjList(y,x)){
			if(visited.contains(bork)) continue;
			if(bork.isDoorway()){
				targets.add(bork);
				continue;
			}
			
			visited.add(bork);
			
			if(distance == 1) targets.add(bork);
			else targetCalc(bork.y, bork.x, distance - 1);
			
			visited.remove(bork);
		}
	}
	
	public Set<BoardCell> getTargets(){
		System.out.println();
		for(BoardCell b:targets) System.out.println(b.y + " " + b.x);
		return targets;
	}
	
}

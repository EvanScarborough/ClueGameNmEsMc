package clueGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IntBoard {
	public static int boardWidth = 4;
	public static int boardHeight = 4;
	
	private BoardCell[][] board;
	private Map<BoardCell, Set<BoardCell>> adjMtx;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	
	public IntBoard(){
		board = new BoardCell[boardWidth][boardHeight];
		for(int i = 0; i < boardWidth; i++){
			for(int j = 0; j < boardWidth; j++){
				board[i][j] = new BoardCell();
				board[i][j].x = i;
				board[i][j].y = j;
			}
		}
		adjMtx = new HashMap<BoardCell, Set<BoardCell>>();
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		targets.clear();
		visited.clear();
		calcAdjacencies();
	}
	
	public void calcTargets(BoardCell start, int pathLength){
		visited.add(start);
		for (BoardCell bork: getAdjList(start)){
			if(visited.contains(bork)) continue;
			
			visited.add(bork);
			
			if(pathLength == 1) targets.add(bork);
			else calcTargets(bork, pathLength - 1);
			
			visited.remove(bork);
		}
	}
	
	private void calcAdjacencies(){
		for(int i = 0; i < boardWidth; i++){
			for(int j = 0; j < boardHeight; j++){
				adjMtx.put(getCell(i,j), new HashSet<BoardCell>());
				if(isValid(i-1,j)) adjMtx.get(getCell(i,j)).add(getCell(i-1,j));
				if(isValid(i+1,j)) adjMtx.get(getCell(i,j)).add(getCell(i+1,j));
				if(isValid(i,j-1)) adjMtx.get(getCell(i,j)).add(getCell(i,j-1));
				if(isValid(i,j+1)) adjMtx.get(getCell(i,j)).add(getCell(i,j+1));
			}
		}
	}
	
	private boolean isValid(int x, int y){
		return x >= 0 && y >= 0 && x < boardWidth && y < boardHeight;
	}
	
	public BoardCell getCell(int x, int y){
		return board[x][y];
	}
	
	public Set<BoardCell> getAdjList(BoardCell cell){
		return adjMtx.get(cell);
	}
	
	public Set<BoardCell> getTargets(){
		return targets;
	}
}

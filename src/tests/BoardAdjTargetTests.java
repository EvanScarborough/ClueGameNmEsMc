package tests;

/*
 * This program tests that adjacencies and targets are calculated correctly.
 */

import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance and initialize it		
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "Legend.txt");		
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesInsideRooms(){
		// Test a corner
		Set<BoardCell> testList = board.getAdjList(0,24);
		assertEquals(0, testList.size());
		// Test one that has walkway underneath
		testList = board.getAdjList(5, 7);
		assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(18, 23);
		assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(22, 17);
		assertEquals(0, testList.size());
		// Test one beside a door
		testList = board.getAdjList(4, 21);
		assertEquals(0, testList.size());
		// Test one in a corner of room
		testList = board.getAdjList(0, 7);
		assertEquals(0, testList.size());
	}

	// Ensure that the adjacency list from a doorway is only the
	// walkway. NOTE: This test could be merged with door 
	// direction test. 
	// These tests are PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT 
		Set<BoardCell> testList = board.getAdjList(7,5);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(7,6)));
		// TEST DOORWAY LEFT 
		testList = board.getAdjList(11,21);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(11,20)));
		//TEST DOORWAY DOWN
		testList = board.getAdjList(5,9);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(6,9)));
		//TEST DOORWAY UP
		testList = board.getAdjList(18,16);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(17,16)));
		
	}
	
	// Test adjacency at entrance to rooms
	// These tests are GREEN in planning spreadsheet
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT
		Set<BoardCell> testList = board.getAdjList(21,12);
		assertTrue(testList.contains(board.getCellAt(21,11)));
		assertTrue(testList.contains(board.getCellAt(21,13)));
		assertTrue(testList.contains(board.getCellAt(20,12)));
		assertTrue(testList.contains(board.getCellAt(22,12)));
		assertEquals(4, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(6, 16);
		assertTrue(testList.contains(board.getCellAt(5, 16)));
		assertTrue(testList.contains(board.getCellAt(7, 16)));
		assertTrue(testList.contains(board.getCellAt(6, 15)));
		assertTrue(testList.contains(board.getCellAt(6, 17)));
		assertEquals(4, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(21,21);
		assertTrue(testList.contains(board.getCellAt(21,22)));
		assertTrue(testList.contains(board.getCellAt(20,21)));
		assertTrue(testList.contains(board.getCellAt(22,21)));
		assertEquals(3, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(17,2);
		assertTrue(testList.contains(board.getCellAt(16,2)));
		assertTrue(testList.contains(board.getCellAt(18,2)));
		assertTrue(testList.contains(board.getCellAt(17,3)));
		assertTrue(testList.contains(board.getCellAt(17,1)));
		assertEquals(4, testList.size());
	}

	// Test a variety of walkway scenarios
	// These tests are LIGHT PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(0,11);
		assertTrue(testList.contains(board.getCellAt(0,12)));
		assertEquals(1, testList.size());
		
		// Test on left edge of board, three walkway pieces
		testList = board.getAdjList(14, 0);
		assertTrue(testList.contains(board.getCellAt(15, 0)));
		assertTrue(testList.contains(board.getCellAt(14, 1)));
		assertTrue(testList.contains(board.getCellAt(13, 0)));
		assertEquals(3, testList.size());

		// Test between two rooms, walkways up and down
		testList = board.getAdjList(3,6);
		assertTrue(testList.contains(board.getCellAt(2,6)));
		assertTrue(testList.contains(board.getCellAt(4,6)));
		assertEquals(2, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(11,19);
		assertTrue(testList.contains(board.getCellAt(11,18)));
		assertTrue(testList.contains(board.getCellAt(11,20)));
		assertTrue(testList.contains(board.getCellAt(10,19)));
		assertTrue(testList.contains(board.getCellAt(12,19)));
		assertEquals(4, testList.size());
		
		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(25,21);
		assertTrue(testList.contains(board.getCellAt(24,21)));
		assertTrue(testList.contains(board.getCellAt(25,20)));
		assertEquals(2, testList.size());
		
		// Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(7,24);
		assertTrue(testList.contains(board.getCellAt(8,24)));
		assertTrue(testList.contains(board.getCellAt(7,23)));
		assertEquals(2, testList.size());

		// Test on walkway next to  door that is not in the needed
		// direction to enter
		testList = board.getAdjList(18,20);
		assertTrue(testList.contains(board.getCellAt(17,20)));
		assertTrue(testList.contains(board.getCellAt(19,20)));
		assertTrue(testList.contains(board.getCellAt(18,21)));
		assertEquals(3, testList.size());
	}
	
	
	// Tests of just walkways, 1 step, includes on edge of board
	// and beside room
	// Have already tested adjacency lists on all four edges, will
	// only test two edges here
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsOneStep() {
		board.calcTargets(21, 7, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(20, 7)));
		assertTrue(targets.contains(board.getCellAt(21, 6)));	
		assertTrue(targets.contains(board.getCellAt(22, 7)));
		
		board.calcTargets(0, 0, 1);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(0, 1)));	
		assertTrue(targets.contains(board.getCellAt(1, 0)));			
	}
	
	// Tests of just walkways, 2 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsTwoSteps() {
		board.calcTargets(25, 13, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(24, 12)));
		assertTrue(targets.contains(board.getCellAt(23, 13)));
		
		board.calcTargets(18, 24, 2);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 24)));
		assertTrue(targets.contains(board.getCellAt(17, 23)));				
	}
	
	// Tests of just walkways, 4 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsFourSteps() {
		board.calcTargets(25, 13, 4);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(23, 13)));
		assertTrue(targets.contains(board.getCellAt(22, 12)));
		assertTrue(targets.contains(board.getCellAt(21, 13)));
		assertTrue(targets.contains(board.getCellAt(24, 12)));
		
		// Includes a path that doesn't have enough length 
		board.calcTargets(25, 20, 4);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(23, 20)));
		assertTrue(targets.contains(board.getCellAt(24, 21)));	
		assertTrue(targets.contains(board.getCellAt(22, 21)));		
	}	
	
	// Tests of just walkways plus one door, 6 steps
	// These are LIGHT BLUE on the planning spreadsheet

	@Test
	public void testTargetsSixSteps() {
		board.calcTargets(25, 20, 6);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(22, 21)));
		assertTrue(targets.contains(board.getCellAt(21, 22)));
		assertTrue(targets.contains(board.getCellAt(20, 21)));
	}	
	
	// Test getting into a room
	// These are LIGHT BLUE on the planning spreadsheet

	@Test 
	public void testTargetsIntoRoom()
	{
		// One room is exactly 2 away
		board.calcTargets(16, 16, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(7, targets.size());
		// directly left
		assertTrue(targets.contains(board.getCellAt(16,14)));
		// directly right
		assertTrue(targets.contains(board.getCellAt(16,18)));
		// diagonals
		assertTrue(targets.contains(board.getCellAt(15, 15)));
		assertTrue(targets.contains(board.getCellAt(17, 17)));
		assertTrue(targets.contains(board.getCellAt(17, 15)));
		assertTrue(targets.contains(board.getCellAt(15,17)));
		// down into door
		assertTrue(targets.contains(board.getCellAt(18,16)));
	}
	
	// Test getting into room, doesn't require all steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(6, 10, 3);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCellAt(7, 10)));
		// directly left and right
		assertTrue(targets.contains(board.getCellAt(6, 7)));
		assertTrue(targets.contains(board.getCellAt(6, 13)));
		//down then left and right
		assertTrue(targets.contains(board.getCellAt(7,12)));
		assertTrue(targets.contains(board.getCellAt(7,8)));
		//right then up
		assertTrue(targets.contains(board.getCellAt(5, 12)));
		//left then into door
		assertTrue(targets.contains(board.getCellAt(5, 9)));
		//down then up left and right
		assertTrue(targets.contains(board.getCellAt(6,9)));
		assertTrue(targets.contains(board.getCellAt(6,11)));
	}

	// Test getting out of a room
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.calcTargets(3,21, 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(3,20)));
		// Take two steps
		board.calcTargets(3,21, 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(3, 19)));
		assertTrue(targets.contains(board.getCellAt(2,20)));
		assertTrue(targets.contains(board.getCellAt(4,20)));
	}

}
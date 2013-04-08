package clueGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	
	private ArrayList<BoardCell> cells;
	private Map<Character, String> rooms;
	private LinkedList<LinkedList<Integer>> adjacencies;
	private Set<BoardCell> targets;
	private boolean[] visited;
	private int numRows, numColumns;
	private String boardLayoutLocation = "boardLayout.csv", boardLegendLocation = "roomKey.txt"; //default values

	public Board() {
		cells = new ArrayList<BoardCell>();
		rooms = new HashMap<Character, String>();
		adjacencies = new LinkedList<LinkedList<Integer>>();
		targets = new HashSet<BoardCell>();
		
		loadConfigFiles();
		calcAdjacencies();        //Calculate adjacencices when the board is initialized
	}
	
	public Board(String boardLayoutPath, String boardLegendPath) {
		cells = new ArrayList<BoardCell>();
		rooms = new HashMap<Character, String>();
		adjacencies = new LinkedList<LinkedList<Integer>>();
		targets = new HashSet<BoardCell>();
		boardLayoutLocation = boardLayoutPath;
		boardLegendLocation = boardLegendPath;
		
		loadConfigFiles();
		calcAdjacencies();
	}
	
	public void loadConfigFiles() {
		try {
			loadBoardLegend();
			loadBoardLayout();		
		} 
		catch (BadConfigFormatException ex) {
			System.err.println(ex.toString());
		}	
	}
	
	public void loadBoardLayout() throws BadConfigFormatException {
		Scanner in = null;
		try {
	    	FileReader reader = new FileReader(boardLayoutLocation);
	    	in = new Scanner(reader);
		  
	    	String line;
	    	int cRow = 0;
	        while (in.hasNext()) {
	        	int cCol = 0;
	        	line = in.nextLine();
		        String[] strCells = line.split(",");
		        for (String strCell : strCells) {	        	
		        	if (strCell.length() > 1) {
		        		//cell is a door      		
		        		char roomChar = strCell.charAt(0);
		        		
		        		//make sure room key is valid (defined in legend)
		        		if (!rooms.containsKey(roomChar))
		        			throw new BadConfigFormatException(boardLayoutLocation);
		        		
		        		//get door direction
		        		switch (strCell.charAt(1)) {
		        			case 'L': 
		        				//add door cell to board
		        				cells.add(new RoomCell(cRow, cCol, roomChar, RoomCell.DoorDirection.LEFT));
		        				break;
		        			case 'U': 
		        				//add door cell to board
		        				cells.add(new RoomCell(cRow, cCol, roomChar, RoomCell.DoorDirection.UP));
		        				break;
		        			case 'R':
		        				//add door cell to board
		        				cells.add(new RoomCell(cRow, cCol, roomChar, RoomCell.DoorDirection.RIGHT));
		        				break;
		        			case 'D': 
		        				//add door cell to board
		        				cells.add(new RoomCell(cRow, cCol, roomChar, RoomCell.DoorDirection.DOWN));
		        				break;
		        			case 'T':
		        				//this cell indicates a cell where the label text for the room should be shown
		        				cells.add(new RoomCell(cRow, cCol, roomChar, rooms.get(roomChar)));
		        				break;
		        			default: 
		        				//invalid door direction, lets make cell a regular room cell instead
		        				cells.add(new RoomCell(cRow, cCol, roomChar));
		        				//throw new BadConfigFormatException(boardLayoutLocation);
		        				break;
		        		}
		        	}
		        	else if (strCell.equalsIgnoreCase("w")) {
		        		//cell is a walkway
		        		
		        		//add walkway cell to board
		        		cells.add(new WalkwayCell(cRow, cCol));
		        	}
		        	else {
		        		//cell is a room
		        		char roomChar = strCell.charAt(0);
		        		
		        		//make sure room key is valid (defined in legend)
		        		if (!rooms.containsKey(roomChar))
		        			throw new BadConfigFormatException(boardLayoutLocation);
		        		
		        		//add room cell to board
		        		cells.add(new RoomCell(cRow, cCol, roomChar));
		        	}
		        	//increment column counter
		        	cCol++;
		        }
		        //increment row counter
		        cRow++;
		        
		        if (numColumns <= 0) {
		        	//set number of columns
		        	numColumns = cCol;
		        }
		        else {
		        	//number of columns does not match
		        	if (cCol != numColumns)
		        		throw new BadConfigFormatException(boardLayoutLocation);
		        }
	        }
	        //set number of rows
	        numRows = cRow;      
	    } 
		catch (FileNotFoundException fnf_ex) {
			System.err.println("Can't open file: " + boardLayoutLocation);
	    }	
		finally {
			in.close();	
		}
	}
	
	public void loadBoardLegend() throws BadConfigFormatException {
		Scanner in = null;
		try {
	    	FileReader reader = new FileReader(boardLegendLocation);
	    	in = new Scanner(reader);
		  
	    	String line;
	        while (in.hasNext()) {
	        	line = in.nextLine();
	        	String[] strs = line.split(",");
	        	if (strs.length != 2) {
	        		//bad line
	        		throw new BadConfigFormatException(boardLegendLocation);
	        	}
	        	if (strs[0].length() > 1) {
	        		//first part is not a single character
	        		throw new BadConfigFormatException(boardLegendLocation);
	        	}
	        	//add new room to legend
	        	rooms.put(strs[0].charAt(0), strs[1].trim());
	        }
        }
    	catch (FileNotFoundException fnf_ex) {
			System.err.println("Can't open file: " + boardLegendLocation);
	    }	
		finally {
			in.close();	
		}		
	}
	
	public int calcIndex(int row, int col) {
		return row*numColumns + col;
	}
	
	//Row, col version of getRoomCellAt
	public RoomCell getRoomCellAt(int row, int col) {
		return getRoomCellAt(calcIndex(row, col));
	}
	
	//Cell version
	public RoomCell getRoomCellAt(int cell) {
		BoardCell cellAt = cells.get(cell);
		if (cellAt instanceof RoomCell)
			return (RoomCell) cellAt;
		
		return null;
	}
	
	public BoardCell getCellAt(int cell) {
		return cells.get(cell);
	}
	
	public Map<Character, String> getRooms() {
		return rooms;
	}

	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	//creates list of cells adjacent to each cell
	public void calcAdjacencies() {
		adjacencies.clear();
		//Step through each cell in the table
		for(int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				LinkedList<Integer> adj = new LinkedList<Integer>();
				adjacencies.add(adj);
				int curCell = calcIndex(i,j);
				boolean isDoorway = cells.get(curCell).isDoorway();
				boolean isRoom = cells.get(curCell).isRoom();
				//Room cells can't have adjacencies
				if (isRoom && !isDoorway) {
					continue;
				}
				if (isDoorway) { //if doorway, can only leave in same direction
					switch(getRoomCellAt(curCell).getDoorDirection()) {
					case LEFT:
						if (j >= 1) adj.add(calcIndex(i, j - 1));
						continue;
					case RIGHT:
						if (j < numColumns - 1) adj.add(calcIndex(i, j + 1));
						continue;
					case UP:
						if (i >= 1) adj.add(calcIndex(i - 1, j));
						continue;
					case DOWN:
						if (i < numRows - 1) adj.add(calcIndex(i + 1, j));
						continue;
					default:
						continue;
					}
				}
				
				//Test cells surrounding current cell for validity.  Add to adjacency list if
				//they are valid
				if(j >= 1) { //left
					int leftCell = calcIndex(i,j-1);
					BoardCell left = cells.get(leftCell);
					if (left.isWalkway()) {    //Add all walkways to list
						adj.add(leftCell);
					}				
					else if ((left.isDoorway())) {   //Make sure door is facing the right direction
						if (getRoomCellAt(leftCell).getDoorDirection() == RoomCell.DoorDirection.RIGHT) {
							adj.add(leftCell);
						}
					}
				}
				if(j < numColumns - 1) { //right
					int rightCell = calcIndex(i,j+1);
					BoardCell right = cells.get(rightCell);
					if (right.isWalkway()) {
						adj.add(rightCell);
					}				
					else if ((right.isDoorway())) {
						if (getRoomCellAt(rightCell).getDoorDirection() == RoomCell.DoorDirection.LEFT) {
							adj.add(rightCell);
						}
					}
				}
				if(i >= 1) { //up
					int upCell = calcIndex(i-1,j);
					BoardCell up = cells.get(upCell);
					if (up.isWalkway()) {
						adj.add(upCell);
					}				
					else if ((up.isDoorway())) {
						if (getRoomCellAt(upCell).getDoorDirection() == RoomCell.DoorDirection.DOWN) {
							adj.add(upCell);
						}
					}
				}
				if(i < numRows - 1) { //down
					int downCell = calcIndex(i+1,j);
					BoardCell down = cells.get(downCell);
					if (down.isWalkway()) {
						adj.add(downCell);
					}				
					else if ((down.isDoorway())) {
						if (getRoomCellAt(downCell).getDoorDirection() == RoomCell.DoorDirection.UP) {
							adj.add(downCell);
						}
					}
				}				
			}
		}	
	}
	
	//creates target list for cell at ([row], [col]) with [numSteps] steps
	public void startTargets(int row, int col, int numSteps) {
		startTargets(calcIndex(row, col), numSteps);
	}
	
	//creates target list for [cell] with [numSteps] steps
	public void startTargets(int cell, int numSteps) {
		//reset visited array
		visited = new boolean[numColumns*numRows];
		targets.clear(); //clear targets
		visited[cell] = true; //set starting cell as visited
		calcTargets(cell, numSteps); //create target list
	}
	
	//used to recursively create target list
	private void calcTargets(int cell, int numSteps) {
		LinkedList<Integer> adjacentCells = getAdjList(cell); //get all adjacent cells
		
		for (int adjCell : adjacentCells) {
			//make sure adjCell has not been visited
			if (visited[adjCell] == false) {
				visited[adjCell] = true; //set adjCell as visited
				if (numSteps == 1 || cells.get(adjCell).isDoorway()) {      //make sure doors get added to targets
					targets.add(cells.get(adjCell)); //add cell at index adjCell to target list
				} else {
					calcTargets(adjCell, (numSteps - 1)); //recursive call
				}
				visited[adjCell] = false; //set adjCell as not visited
			}
		}
	}
	
	//return target list
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	//returns list of cells adjacent to [cell]
	public LinkedList<Integer> getAdjList(int cell) {
		return adjacencies.get(cell);
	}
	
	//returns a location for drawing based on the row and col parameters
	public Point getDrawingPoint(int row, int col) {
		Dimension size = new Dimension(clueGUI.BoardPanel.BOARD_CELL_SIZE, clueGUI.BoardPanel.BOARD_CELL_SIZE);
		Point loc = new Point(0, 0);
		loc.x = col * size.width;
		loc.y = row * size.height;
		return loc;
	}
	
	//returns a location for drawing based on the index parameter
		public Point getDrawingPoint(int index) {
			int row = index / numColumns;
			int col = index - (row*numColumns);
			return getDrawingPoint(row, col);
		}

	//paints all of the board cells using the graphics parameter
	public void drawBoard(Graphics g) {
		//paint the board cells
		Dimension size = new Dimension(clueGUI.BoardPanel.BOARD_CELL_SIZE, clueGUI.BoardPanel.BOARD_CELL_SIZE);
		Point loc = new Point(0, 0);
		int row = 0;
		int col = 0;
		
		//for (int i = 0; i < cells.size(); i++) {
		for (int i = cells.size() - 1; i >= 0; i--) {
			BoardCell cell = cells.get(i);
			loc = getDrawingPoint(cell.getRow(), cell.getCol());
			if (targets.contains(cell)) {
				cell.drawTarget(g, size, loc);
			} else {
				cell.draw(g, size, loc);
			}
			
			//increment column
			col++;
			if (col >= numColumns) {
				//column exceeds max, go to next row
				col = 0;
				row++;
			}
		}
	}
}

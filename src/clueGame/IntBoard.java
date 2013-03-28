package clueGame;

import java.util.*;

public class IntBoard {
	
	private static final int NUM_COLS = 4;
	private static final int NUM_ROWS = 4;
	private LinkedList<LinkedList<Integer>> adjacencies;
	private Set<Integer> targets;
	private boolean[] visited;

	public IntBoard() {
		super();
		
		adjacencies = new LinkedList<LinkedList<Integer>>();
		targets = new HashSet<Integer>();
		visited = new boolean[NUM_COLS*NUM_ROWS];	
	}
	
	//creates list of cells adjacent to each cell
	public void calcAdjacencies() {
		adjacencies.clear();
		//Step through each cell in the table
		for(int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				LinkedList<Integer> adj = new LinkedList<Integer>();
				//Test cells surrounding current cell for validity.  Add to adjacency list if
				//they are valid
				if(j - 1 >= 0) {
					adj.add(calcIndex(i,j-1));
				}
				if(j + 1 < NUM_COLS) {
					adj.add(calcIndex(i,j+1));
				}
				if(i-1 >= 0) {
					adj.add(calcIndex(i-1,j));
				}
				if(i+1 < NUM_ROWS) {
					adj.add(calcIndex(i+1,j));
				}
				adjacencies.add(adj);				
			}
		}	
	}
	
	//creates target list for cell at ([row], [col]) with [numSteps] steps
	public void startTargets(int row, int col, int numSteps) {
		startTargets(calcIndex(row, col), numSteps);
	}
	
	//creates target list for [cell] with [numSteps] steps
	public void startTargets(int cell, int numSteps) {
		//set all visited to false
		for (int i = 0; i < 16; i++) {
			visited[i] = false;
		}
		calcAdjacencies(); //create adjacency list
		targets.clear(); //clear targets
		visited[cell] = true; //set starting cell as visited
		calcTargets(cell, numSteps); //create target list
		
		//System.out.println("TARGETS FOR CELL " + cell + " WITH " + numSteps + " STEPS.");
		//printTargets();
	}
	
	//used to recursively create target list
	private void calcTargets(int cell, int numSteps) {
		LinkedList<Integer> adjacentCells = getAdjList(cell); //get all adjacent cells
		
		for (int adjCell : adjacentCells) {
			//make sure adjCell has not been visited
			if (visited[adjCell] == false) {
				visited[adjCell] = true; //set adjCell as visited
				if (numSteps == 1) {
					targets.add(adjCell); //add adjCell to target list
				}
				else {
					calcTargets(adjCell, (numSteps - 1)); //recursive call
				}
				visited[adjCell] = false; //set adjCell as not visited
			}
		}
	}
	
	//prints all values in target list
	private void printTargets() {
		for (int num : targets) {
			System.out.print("" + num + ", ");
		}
		System.out.println("");
	}
	
	//return target list
	public Set<Integer> getTargets() {
		return targets;
	}
	
	//returns list of cells adjacent to [cell]
	public LinkedList<Integer> getAdjList(int cell) {
		return adjacencies.get(cell);
	}
	
	//returns cell at ([row], [col])
	public int calcIndex(int row, int col) {
		return row*NUM_COLS + col;
	}
}

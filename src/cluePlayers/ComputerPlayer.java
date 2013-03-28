package cluePlayers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.RoomCell;

public class ComputerPlayer extends Player {
	private LinkedList<Card> seen;
	private Random rand = new Random();

	public ComputerPlayer(String name, LinkedList<Card> cards, String color, int index) {
		super(name, cards, color, index);
		seen = new LinkedList<Card>();
		for (Card c : cards) seen.add(c);
	}

	public ComputerPlayer(String name, String color, int index) {
		super(name, color, index);
		seen = new LinkedList<Card>();
	}

	public int pickLocation(int moves, Board board) { //need to account for not reentering same room
		board.startTargets(getIndex(), moves); //looks for targets from current location
		Set<BoardCell> targets = board.getTargets();
		RoomCell cell = board.getRoomCellAt(getIndex()); //gets current cell to check room
		Set<RoomCell> rooms = new HashSet<RoomCell>();
		for (BoardCell c : targets)	{
			if (c.isRoom()) {
				int row = c.getRow();
				int col = c.getCol();
				rooms.add(board.getRoomCellAt(board.calcIndex(row, col))); //adds all RoomCells to different set
			}
		}
		if (cell != null) { //checks if current cell is a room
			if (!rooms.isEmpty()) { //checks if there are doors to enter
				for (RoomCell c : rooms) {
					if (c.getInitial() == cell.getInitial()) {
						rooms.remove(c); //removes from possible rooms
						int row = c.getRow();
						int col = c.getCol();
						targets.remove(board.getCellAt(board.calcIndex(row, col))); //remove from possible targets
					}
				}
			}
		}
		BoardCell target = null;
		//scan for room, then pick at random
		if (!rooms.isEmpty()) target = randomSelectRoom(rooms); //pick from available rooms first
		else target = randomSelect(targets); //otherwise choose randomly
		if (target == null) return -1; //error state- should always be able to choose a target
		else return target.getRow()*board.getNumColumns() + target.getCol();
	}

	public BoardCell randomSelect(Set<BoardCell> targets) {
		int counter = 0;
		int stop = rand.nextInt(targets.size());
		for (BoardCell b : targets) {
			if (counter == stop) return b;
			counter++;
		}
		return null; //should never trigger, but necessary since choice is in if loop
	}

	public RoomCell randomSelectRoom(Set<RoomCell> targets) { //same as above, but takes a RoomCell set instead
		int counter = 0;
		int stop = rand.nextInt(targets.size());
		for (RoomCell b : targets) {
			if (counter == stop) return b;
			counter++;
		}
		return null; //should never trigger, but necessary since choice is in if loop
	}

	public LinkedList<Card> getSeen() {
		return seen;
	}

	public void setSeen(LinkedList<Card> seen) {
		this.seen = seen;
	}
}

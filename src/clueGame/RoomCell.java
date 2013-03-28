package clueGame;

public class RoomCell extends BoardCell {

	public enum DoorDirection {UP, DOWN, LEFT, RIGHT, NONE};
	private DoorDirection doorDirection;
	private char roomInitial;
	
	//creates a room cell
	public RoomCell(int row, int col, char roomInitial) {
		super(row, col);
		this.doorDirection = DoorDirection.NONE;
		this.roomInitial = roomInitial;
	}
	
	//creates a doorway cell
	public RoomCell(int row, int col, char roomInitial, DoorDirection doorDirection) {
		super(row, col);
		this.doorDirection = doorDirection;
		this.roomInitial = roomInitial;
	}

	@Override
	public boolean isRoom() {
		return true;
	}
	
	@Override
	public boolean isDoorway() {
		return (doorDirection != DoorDirection.NONE);
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public char getInitial() {
		return roomInitial;
	}
	
	/*
	 * Space to implement draw function
	 */
	
}

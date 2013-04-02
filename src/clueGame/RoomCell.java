package clueGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class RoomCell extends BoardCell {

	public enum DoorDirection {UP, DOWN, LEFT, RIGHT, NONE};
	private DoorDirection doorDirection;
	private char roomInitial;
	private boolean isLabelCell;
	
	//creates a room cell
	public RoomCell(int row, int col, char roomInitial) {
		super(row, col);
		this.doorDirection = DoorDirection.NONE;
		this.roomInitial = roomInitial;
		this.isLabelCell = false;
	}
	
	//creates a doorway cell
	public RoomCell(int row, int col, char roomInitial, DoorDirection doorDirection) {
		super(row, col);
		this.doorDirection = doorDirection;
		this.roomInitial = roomInitial;
		this.isLabelCell = false;
	}
	
	//creates a doorway cell
		public RoomCell(int row, int col, char roomInitial, boolean isLabelCell) {
			super(row, col);
			this.doorDirection = DoorDirection.NONE;
			this.roomInitial = roomInitial;
			this.isLabelCell = isLabelCell;
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

	@Override
	public void draw(Graphics g, Dimension size, Point loc) {
		//draw room cell
		g.setColor(Color.GRAY);
		g.fillRect(loc.x, loc.y, size.width, size.height);
		
		//draw a doorway if applicable
		if (isDoorway()) {
			g.setColor(Color.BLUE);
			switch (doorDirection) {
				case UP: 
					g.fillRect(loc.x, loc.y, size.width, clueGUI.BoardPanel.DOOR_SIZE);
					break;
				case DOWN: 
					g.fillRect(loc.x, loc.y + size.height - clueGUI.BoardPanel.DOOR_SIZE, size.width, clueGUI.BoardPanel.DOOR_SIZE);
					break;
				case LEFT: 
					g.fillRect(loc.x, loc.y, clueGUI.BoardPanel.DOOR_SIZE, size.height);
					break;
				case RIGHT: 
					g.fillRect(loc.x + size.width - clueGUI.BoardPanel.DOOR_SIZE, loc.y, clueGUI.BoardPanel.DOOR_SIZE, size.height);
					break;
				default:
					break;
			}
		}
		
		//show label if applicable
		if (isLabelCell) {
			g.setColor(Color.BLACK);
			int margin = clueGUI.BoardPanel.BOARD_CELL_SIZE / 2;
			g.drawString("Test", loc.x, loc.y + margin);
		}
	}
	
}

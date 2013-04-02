package clueGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

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

	@Override
	public void draw(Graphics g, Dimension size, Point loc) {
		//draw room cell
		g.setColor(Color.GRAY);
		g.fillRect(loc.x, loc.y, size.width, size.height);
		
		//draw a doorway if applicable
		if (isDoorway()) {
			g.setColor(Color.BLUE);
			int doorPixels = 5; //width or height of doorway
			switch (doorDirection) {
				case UP: 
					g.fillRect(loc.x, loc.y, size.width, doorPixels);
					break;
				case DOWN: 
					g.fillRect(loc.x, loc.y + size.height - doorPixels, size.width, doorPixels);
					break;
				case LEFT: 
					g.fillRect(loc.x, loc.y, doorPixels, size.height);
					break;
				case RIGHT: 
					g.fillRect(loc.x + size.width - doorPixels, loc.y, doorPixels, size.height);
					break;
				default:
					break;
			}
		}
	}
	
}

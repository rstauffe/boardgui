package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public abstract class BoardCell {

	private int row;
	private int col;
	
	public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public boolean isWalkway() {
		return false;
	}
	
	public boolean isRoom() {
		return false;
	}
	
	public boolean isDoorway() {
		return false;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	 @Override
	public int hashCode() { //set comparison code
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) { //.equals logic
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardCell other = (BoardCell) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	public abstract void draw(Graphics g, Dimension size, Point loc);

	public void drawTarget(Graphics g, Dimension size, Point loc) {
		if (isRoom()) {
			g.setColor(Color.BLUE);
			g.fillRect(loc.x, loc.y, size.width, size.height);
			g.setColor(Color.BLACK);
			g.drawRect(loc.x, loc.y, size.width, size.height);
		} else {
			g.setColor(Color.CYAN);
			g.fillRect(loc.x, loc.y, size.width, size.height);
			g.setColor(Color.BLACK);
			g.drawRect(loc.x, loc.y, size.width, size.height);
		}
	}
}

package clueGame;

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

	 public abstract void draw(Graphics g, Dimension size, Point loc);

}

package clueGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class WalkwayCell extends BoardCell {

	public WalkwayCell(int row, int col) {
		super(row, col);
	}

	@Override
	public boolean isWalkway() {
		return true;
	}

	@Override
	public void draw(Graphics g, Dimension size, Point loc) {
		//draw walkway
		g.setColor(Color.YELLOW);
		g.fillRect(loc.x, loc.y, size.width, size.height);
		//draw walkway border
		g.setColor(Color.BLACK);
		g.drawRect(loc.x, loc.y, size.width, size.height);
	}
	
}

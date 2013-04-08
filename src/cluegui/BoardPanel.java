package clueGUI;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import cluePlayers.ClueGame;

public class BoardPanel extends JPanel {
	
	public static final int BOARD_CELL_SIZE = 30;
	public static final int DOOR_SIZE = 5;

	private ClueGame game;
	private Point clickPt;
	private int x, y;
	
	public BoardPanel(ClueGame game) {
		super();
		this.game = game;
		clickPt = null;
		
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				clickPt = me.getPoint();
				x = (int) (clickPt.getX() / BOARD_CELL_SIZE);
				y = (int) (clickPt.getY() / BOARD_CELL_SIZE);
				System.out.println(x + ", " + y);
			}
		});
		
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.getBoard().drawBoard(g);
		game.drawPlayers(g);
	}

	public Point getClickPt() {
		return clickPt;
	}

	public void setClickPt(Point clickPt) {
		this.clickPt = clickPt;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

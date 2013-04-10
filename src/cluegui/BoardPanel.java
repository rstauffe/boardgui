package clueGUI;

import java.awt.Color;
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
	
	public BoardPanel(ClueGame game) {
		super();
		this.game = game;
		
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.getBoard().drawBoard(g);
		game.drawPlayers(g);
		
		if (!game.isGameOver()) {
			//originally had board drawing functions here instead of above
		}
		else {
			//paint on board that game is over
			int x = 235;
			int y = 255;
		
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x - 85, y - 30, 240, 80);
			g.setColor(Color.BLACK);
			g.drawRect(x - 85, y - 30, 240, 80);
			g.setColor(Color.BLACK);
			g.drawString("GAME OVER", x, y);
			if (game.getTurn() == 0) {
				x = x - 60;
				y = y + 25;
				g.drawString("Congratulations, you won the game!", x, y);
			}
			else {
				x = x - 30;
				y = y + 25;
				g.drawString("Player " + game.getTurn() + " won the game.", x, y);
			}
		}
	}
}

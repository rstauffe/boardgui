package clueGUI;

import java.awt.Graphics;
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
	}
}

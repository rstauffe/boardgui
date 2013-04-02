package clueGUI;

import java.awt.Graphics;
import javax.swing.JPanel;
import clueGame.Board;

public class BoardPanel extends JPanel {

	private Board board;
	
	public BoardPanel(Board board) {
		super();
		this.board = board;
		
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		board.drawBoard(g);
	}
}

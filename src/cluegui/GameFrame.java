package clueGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cluePlayers.ClueGame;

public class GameFrame extends JFrame {
	
	private ClueGame game;

	public GameFrame() {
		super();
		game = new ClueGame();
		BoardPanel board = new BoardPanel(game.getBoard());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");
		setSize(new Dimension(750, 600));
		
		//Main panel, contains all components
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2, 1));
		JPanel sPanel = new JPanel();
		JPanel nPanel = new JPanel();
		//nPanel.setLayout(new GridLayout(1, 3));
		
		//Whose Turn? section
		JPanel whoseTurnPanel = new JPanel();
		whoseTurnPanel.setLayout(new GridLayout(2, 0));
		JLabel whoseTurnLabel = new JLabel("Whose Turn?");
		whoseTurnPanel.add(whoseTurnLabel);
		JTextField whoseTurn = new JTextField(15);
		whoseTurnPanel.add(whoseTurn);
		nPanel.add(whoseTurnPanel);
		
		//Next Player and Make Accusation buttons
		JButton nextPlayer = new JButton("Next Player");
		JButton makeAccusation = new JButton("Make Accusation");
		nPanel.add(nextPlayer);
		nPanel.add(makeAccusation);
		
		//Die roll
		JPanel dieRollPanel = new JPanel();
		JTextField dieRoll = new JTextField(2);
		dieRoll.setEditable(false);  //Can't edit this field
		JLabel dieRollLabel = new JLabel("Roll:");
		dieRollPanel.add(dieRollLabel);
		dieRollPanel.add(dieRoll);
		dieRollPanel.setBorder(new TitledBorder(new EtchedBorder(), "Die Roll"));
		sPanel.add(dieRollPanel);
		
		//Guess panel
		JPanel guessPanel = new JPanel();
		JTextField guessText = new JTextField(20);
		guessText.setEditable(false);  //Can't edit this field
		JLabel guess = new JLabel("Guess:");
		guessPanel.add(guess);
		guessPanel.add(guessText);
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		sPanel.add(guessPanel);
		
		//Guess Result panel
		JPanel guessResultPanel = new JPanel();
		JTextField guessResultText = new JTextField(20);
		guessResultText.setEditable(false);  //Can't edit this field
		JLabel guessResult = new JLabel("Guess Result:");
		guessResultPanel.add(guessResult);
		guessResultPanel.add(guessResultText);
		guessResultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		sPanel.add(guessResultPanel);
		
		mainPanel.add(nPanel);
		mainPanel.add(sPanel);
		
		//Card display panel
		JPanel cardDisplayPanel = new JPanel();
		//cardDisplayPanel.setLayout(new GridLayout(4, 1));
		JLabel cardTitle = new JLabel("My Cards");
		cardDisplayPanel.add(cardTitle);
		cardDisplayPanel.setPreferredSize(new Dimension(175, 580));
		
		this.add(board, BorderLayout.CENTER);
		this.add(mainPanel, BorderLayout.SOUTH);
		this.add(cardDisplayPanel, BorderLayout.EAST);
		this.validate();
		
		board.setPreferredSize(new Dimension(-1, 580));
		this.pack();
		this.repaint();
	}

	public static void main(String[] args) {
		GameFrame gui = new GameFrame();
		gui.setVisible(true);
	}

}

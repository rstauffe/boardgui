package clueGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cluePlayers.Card;
import cluePlayers.ClueGame;

public class GameFrame extends JFrame {
	
	private static ClueGame game;
	private DetectiveFrame notes;

	public GameFrame() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");
		setSize(new Dimension(750, 600));
		setLocation(20, 20);
		//Create game and use game board to create board panel
		game = new ClueGame();
		notes = new DetectiveFrame(game); //enables notes to see same cards as game
		BoardPanel boardPanel = new BoardPanel(game);
		boardPanel.setPreferredSize(new Dimension(-1, 580));	
		
		this.add(boardPanel, BorderLayout.CENTER);
		this.add(createBottomPanel(), BorderLayout.SOUTH);
		this.add(createCardDisplayPanel(), BorderLayout.EAST);
		this.setJMenuBar(createFileMenuBar());
		this.validate();
		this.pack();
		this.repaint();
	}
	
	private JPanel createBottomPanel() {
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
		whoseTurn.setEditable(false); //shouldn't be changed by user
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
		
		return mainPanel;
	}
	
	private JPanel createCardDisplayPanel() {
		//Card display panel
		JPanel cardDisplayPanel = new JPanel();
		//cardDisplayPanel.setLayout(new GridLayout(4, 1));
		cardDisplayPanel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		cardDisplayPanel.setPreferredSize(new Dimension(175, 580));
		cardDisplayPanel.setLayout(new GridLayout(0, 1));
		for (Card c : game.getPlayer().getCards()) { //gets the player's cards
			cardDisplayPanel.add(new JLabel(c.getName() + " - " + c.getType()));
		}
		return cardDisplayPanel;
	}
	
	private JMenuBar createFileMenuBar()
	{
		//create File menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File"); 
		menu.add(createShowNotesItem());
		menu.add(createFileExitItem());
		menuBar.add(menu);
		return menuBar;
	}

	private JMenuItem createFileExitItem()
	{
		//create Exit option that will be placed in the File menu
		JMenuItem item = new JMenuItem("Exit");
		//Exit the application when the button is pressed
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	private JMenuItem createShowNotesItem() {
		JMenuItem item = new JMenuItem("Show Notes");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e)
			{
				notes.setVisible(true);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}


	//MAIN
	public static void main(String[] args) {
		GameFrame gui = new GameFrame();
		gui.setVisible(true);
	}
}

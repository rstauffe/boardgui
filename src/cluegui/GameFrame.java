package clueGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import clueGame.BoardCell;
import clueGame.RoomCell;
import cluePlayers.Card;
import cluePlayers.ClueGame;
import cluePlayers.ComputerPlayer;
import cluePlayers.Player;

public class GameFrame extends JFrame {

	private ClueGame game;
	private DetectiveFrame notes;

	//scope these components higher so that they can easily have their properties changes during gameplay
	private JTextField whoseTurn, dieRoll, guessText, guessResultText;
	private JPanel cardDisplayPanel;
	private int x, y;
	private BoardPanel boardPanel;

	public GameFrame() {
		super();
		//set frame defaults
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");
		setSize(new Dimension(750, 600));
		setLocation(20, 20);

		//Create game and use game board to create board panel
		game = new ClueGame();
		notes = new DetectiveFrame(game); //enables notes to see same cards as game
		boardPanel = new BoardPanel(game);
		boardPanel.setPreferredSize(new Dimension(-1, 580));	
		boardPanel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				Point clickPt = me.getPoint();
				x = (int) (clickPt.getY() / BoardPanel.BOARD_CELL_SIZE); //gets row
				y = (int) (clickPt.getX() / BoardPanel.BOARD_CELL_SIZE); //gets column
				if (game.getTurn() == 0 && game.isPlayerMoved() == false) {
					Set<BoardCell> targets = game.getBoard().getTargets();
					int index = game.getBoard().calcIndex(x, y);
					if (targets.contains(game.getBoard().getCellAt(index))) {
						game.getPlayer().setIndex(index);
						game.getBoard().getTargets().clear();
						game.setPlayerMoved(true);
						if(game.getBoard().getCellAt(index).isRoom()) { //make a suggestion if in a room
							RoomCell room = game.getBoard().getRoomCellAt(index);
							String roomName = game.getBoard().getRooms().get(room.getInitial());
							Card roomCard = null;
							for (Card c : game.getCards()) {
								if (c.getName().equals(roomName)) {
									roomCard = c;
									break;
								}
							}
							Card personCard = null; //placeholders for dialog selections
							Card weaponCard = null;
							Card shown = game.makeSuggestion(roomCard, personCard, weaponCard, 0);
							setSuggestion(roomCard, personCard, weaponCard, shown, game.getPlayer().getIndex());
							//need logic to find accused player and move
						}
					} else {
						JOptionPane.showMessageDialog(null, "That is not a valid move location!");
					}
					boardPanel.repaint();
				} else {
					if (game.getTurn() != 0) {
						JOptionPane.showMessageDialog(null, "It's not your turn!");
					} else {
						JOptionPane.showMessageDialog(null, "You can't move right now!");
					}
				}
			}
		});
		//create and add components to frame
		this.add(boardPanel, BorderLayout.CENTER);
		this.add(createBottomPanel(), BorderLayout.SOUTH);
		this.add(createCardDisplayPanel(), BorderLayout.EAST);
		this.setJMenuBar(createFileMenuBar());

		//refresh frame
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
		whoseTurn = new JTextField(15);
		whoseTurn.setEditable(false); //shouldn't be changed by user
		whoseTurnPanel.add(whoseTurn);
		nPanel.add(whoseTurnPanel);

		//Next Player and Make Accusation buttons
		JButton nextPlayer = new JButton("Next Player");

		class TurnListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (game.isPlayerMoved() == false) {
					JOptionPane.showMessageDialog(null, "You must make a move before you complete your turn!");
				} else {
					nextPlayer();
				}
			}
		}

		nextPlayer.addActionListener(new TurnListener());
		JButton makeAccusation = new JButton("Make Accusation");

		class AccuseListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				//need to write accusation logic
			}
		}

		makeAccusation.addActionListener(new AccuseListener());
		nPanel.add(nextPlayer);
		nPanel.add(makeAccusation);

		//Die roll
		JPanel dieRollPanel = new JPanel();
		dieRoll = new JTextField(2);
		dieRoll.setEditable(false);  //Can't edit this field
		JLabel dieRollLabel = new JLabel("Roll:");
		dieRollPanel.add(dieRollLabel);
		dieRollPanel.add(dieRoll);
		dieRollPanel.setBorder(new TitledBorder(new EtchedBorder(), "Die Roll"));
		sPanel.add(dieRollPanel);

		//Guess panel
		JPanel guessPanel = new JPanel();
		guessText = new JTextField(20);
		guessText.setEditable(false);  //Can't edit this field
		JLabel guess = new JLabel("Guess:");
		guessPanel.add(guess);
		guessPanel.add(guessText);
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		sPanel.add(guessPanel);

		//Guess Result panel
		JPanel guessResultPanel = new JPanel();
		guessResultText = new JTextField(20);
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
		cardDisplayPanel = new JPanel();
		//cardDisplayPanel.setLayout(new GridLayout(4, 1));
		cardDisplayPanel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		cardDisplayPanel.setPreferredSize(new Dimension(175, 580));
		cardDisplayPanel.setLayout(new GridLayout(0, 1));
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

	/* GAMEPLAY FUNCTIONS */

	public void startGame() {
		//show player cards in panel
		for (Card c : game.getPlayer().getCards()) { //gets the player's cards
			cardDisplayPanel.add(new JLabel(c.getName() + " - " + c.getType()));
		}

		//show player which character they will be
		cluePlayers.Player hPlayer = game.getPlayer();
		JOptionPane.showMessageDialog(this, "You are " + hPlayer.getName() + " (" + hPlayer.getColor() + ")" + ". " +
				"Press Next Player to begin play.", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
	}

	private void setWhoseTurn() {
		//set the text in the Whose Turn? box to the current player
		whoseTurn.setText(game.getCurrentPlayer().getName()); //grabs player name
	}

	private void setRoll() {
		//set the text in the Roll box to the last roll
		int roll = game.getRoll();
		dieRoll.setText("" + roll);
	}

	private void nextPlayer() {
		game.nextTurn();
		setWhoseTurn();
		setRoll();
		this.repaint();
		
		//logic for AI to make a suggestion
		if (game.getTurn() > 0 && game.getBoard().getCellAt(game.getCurrentPlayer().getIndex()).isRoom()) {
			Card shown = game.makeSuggestion(game.getTurn()); //AI makes suggestion
			setSuggestion(game.getRoomCard(), game.getPersonCard(), game.getWeaponCard(), shown, game.getCurrentPlayer().getIndex());
			if (shown == null) {
				//set to make accusation next turn
			}
		}
	}

	private void setSuggestion(Card roomCard, Card personCard, Card weaponCard, Card shown, int index) {
		//takes the 3 guess cards, the disproving card, and the index of the accuser
		//logic to move accused to accuser's location
		if (personCard.getName().equals(game.getPlayer().getName())) { //find if player needs to be moved
			game.getPlayer().setIndex(index);
		} else { //else check which computer was accused
			for (ComputerPlayer ai : game.getComps()) {
				if (personCard.getName().equals(ai.getName())) {
					ai.setIndex(index);
					break;
				}
			}
		}
		
		guessText.setText(roomCard.getName() + ", " + personCard.getName() + ", " +
				weaponCard.getName()); //set guess into window
		if (shown == null) {
			guessResultText.setText("(No matches)");
		} else {
			guessResultText.setText(shown.getName()); //set result into result box
		}
		boardPanel.repaint(); //repaint to set accused's new location
	}

	//MAIN
	public static void main(String[] args) {
		GameFrame gameGUI = new GameFrame();
		gameGUI.setVisible(true);
		gameGUI.startGame();
	}
}

package clueGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	JButton nextPlayer, makeAccusation;
	private JPanel cardDisplayPanel;
	private SuggestionFrame guessFrame;
	private int x, y;
	private BoardPanel boardPanel;
	private AccuseFrame accusePanel;

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
		guessFrame = new SuggestionFrame(game, null);
		accusePanel = new AccuseFrame(game);
		boardPanel = new BoardPanel(game);
		boardPanel.setPreferredSize(new Dimension(-1, 580));	
		boardPanel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				Point clickPt = me.getPoint();
				x = (int) (clickPt.getY() / BoardPanel.BOARD_CELL_SIZE); //gets row
				y = (int) (clickPt.getX() / BoardPanel.BOARD_CELL_SIZE); //gets column
				if (game.isGameOver()) {
					return;
				}
				if (game.getTurn() == 0 && game.isPlayerMoved() == false) {
					Set<BoardCell> targets = game.getBoard().getTargets();
					int index = game.getBoard().calcIndex(x, y);
					if (targets.contains(game.getBoard().getCellAt(index))) {
						//check if target is a room
						if (game.getBoard().getCellAt(index).isRoom()) { 
							//player is entering a room, show suggestion form
							RoomCell room = game.getBoard().getRoomCellAt(index);
							String roomName = game.getBoard().getRooms().get(room.getInitial());
							Card roomCard = null;
							for (Card c : game.getCards()) {
								if (c.getName().equals(roomName)) {
									roomCard = c;
									break;
								}
							}
							//show suggestion form and wait for user input
							guessFrame = new SuggestionFrame(game, roomCard);
							guessFrame.setVisible(true);
							//only move player if they have submitted the suggestion
							if (guessFrame.isSubmitted()) {
								//submit suggestion and move player
								game.getPlayer().setIndex(index);
								game.getBoard().getTargets().clear();
								game.setPlayerMoved(true);
								humanSuggest(roomCard);
							}
						}
						else {
							//not moving to a room, simply move player
							game.getPlayer().setIndex(index);
							game.getBoard().getTargets().clear();
							game.setPlayerMoved(true);
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
		nextPlayer = new JButton("Next Player");

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
		makeAccusation = new JButton("Make Accusation");

		class AccuseListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (game.getTurn() > 0) {
					JOptionPane.showMessageDialog(null, "You can only make an accusation on your turn!");
				} else if (game.isPlayerMoved()) {
					JOptionPane.showMessageDialog(null, "You can only make an accusation at the beginning of your turn!");
				} else {
					accusePanel.setVisible(true);
					if (accusePanel.isSubmitted()) {
						HashSet<Card> accusation = game.getPlayer().makeAccusation(accusePanel.getRoomCard(), 
								accusePanel.getPersonCard(), accusePanel.getWeaponCard());
						checkAccusation(accusation);
					}
				}
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
		HashSet<Card> accusing = game.nextTurn();
		setWhoseTurn();
		setRoll();
		this.repaint();
		
		if (accusing != null) {
			checkAccusationAI(accusing,(ComputerPlayer) game.getCurrentPlayer());
		}
		//logic for AI to make a suggestion
		if (game.getTurn() > 0 && game.getBoard().getCellAt(game.getCurrentPlayer().getIndex()).isRoom()) {
			ComputerPlayer ai = (ComputerPlayer) game.getCurrentPlayer(); //gets AI
			Card shown = game.makeSuggestion(game.getTurn()); //AI makes suggestion
			setSuggestion(game.getRoomCard(), game.getPersonCard(), game.getWeaponCard(), shown, ai.getIndex());
			if (shown == null) {
				ai.setAccuse(true); //set flags to make an accusation next turn
				ai.setRoomCard(game.getRoomCard());
				ai.setPersonCard(game.getPersonCard());
				ai.setWeaponCard(game.getWeaponCard());
			}
		}
	}
	
	private void humanSuggest(Card roomCard) { 
		Card personCard = guessFrame.getPersonCard();
		Card weaponCard = guessFrame.getWeaponCard();
		Card shown = game.makeSuggestion(roomCard, personCard, weaponCard, 0);
		setSuggestion(roomCard, personCard, weaponCard, shown, game.getPlayer().getIndex());
		guessFrame.setVisible(false);
	}

	private void setSuggestion(Card roomCard, Card personCard, Card weaponCard, Card shown, int index) {
		//takes the 3 guess cards, the disproving card, and the index of the accuser
		//logic to move accused to accuser's location
		if (personCard.getName().equals(game.getPlayer().getName())) { //find if player needs to be moved
			game.getPlayer().setIndex(index);
		} else { //else check which computer was accused
			for (Player ai : game.getComps()) {
				if (personCard.getName().equals(ai.getName())) {
					ai.setIndex(index);
					break;
				}
			}
		}
		
		guessText.setText(personCard.getName() + ", " + roomCard.getName() + ", " +
				weaponCard.getName()); //set guess into window
		if (shown == null) {
			guessResultText.setText("No new clue");
		} else {
			for (ComputerPlayer ai : game.getComps()) {
				ai.getSeen().add(shown); //add new card to stack of seen cards
			}
			guessResultText.setText(shown.getName()); //set result into result box
		}
		boardPanel.repaint(); //repaint to set accused's new location
	}
	
	public void checkAccusation(HashSet<Card> accusation) {
		//get cards
		Card roomCard = null;
		Card personCard = null;
		Card weaponCard = null;
		for (Card c: accusation) {
			switch (c.getType()) {
				case PERSON:
					personCard = c;
					break;
				case ROOM:
					roomCard = c;
					break;
				case WEAPON:
					weaponCard = c;
					break;
				default:
					break;
			}
		}
		
		//check solution
		if (game.isSolutionCorect(accusation)) {
			JOptionPane.showMessageDialog(null, 
					"Room: " + roomCard.getName() +
					"\nPerson: " + personCard.getName() +
					"\nWeapon: " + weaponCard.getName() +
					"\nAccusation correct! " + game.getCurrentPlayer().getName() + " wins!");
			//end game
			nextPlayer.setEnabled(false);
			makeAccusation.setEnabled(false);
			boardPanel.repaint();
		} else {
			JOptionPane.showMessageDialog(null, 
					"Room: " + roomCard.getName() +
					"\nPerson: " + personCard.getName() +
					"\nWeapon: " + weaponCard.getName() +
					"\nSorry " + game.getCurrentPlayer().getName() + ", that's incorrect.");
			game.setPlayerMoved(true); //ends the player's turn (since usually player leaves, rules are unclear)
			game.getBoard().getTargets().clear();
			boardPanel.repaint(); //clears movement squares
		}
	}

	public void checkAccusationAI(HashSet<Card> accusation, ComputerPlayer ai) {
		if (game.isSolutionCorect(accusation)) {
			JOptionPane.showMessageDialog(null, 
					"Room: " + ai.getRoomCard().getName() +
					"\nPerson: " + ai.getPersonCard().getName() +
					"\nWeapon: " + ai.getWeaponCard().getName() +
					"\nAccusation correct! " + game.getCurrentPlayer().getName() + " wins!");
			
			//end game
			nextPlayer.setEnabled(false);
			makeAccusation.setEnabled(false);
			boardPanel.repaint();
		} else {
			JOptionPane.showMessageDialog(null, 
					"Room: " + ai.getRoomCard().getName() +
					"\nPerson: " + ai.getPersonCard().getName() +
					"\nWeapon: " + ai.getWeaponCard().getName() +
					"\nSorry " + game.getCurrentPlayer().getName() + ", that's incorrect.");
		}
	}
	
	//MAIN
	public static void main(String[] args) {
		GameFrame gameGUI = new GameFrame();
		gameGUI.setVisible(true);
		gameGUI.startGame();
	}
}

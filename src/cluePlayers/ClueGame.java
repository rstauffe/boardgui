package cluePlayers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import clueGame.Board;
import clueGame.RoomCell;

public class ClueGame {
	private LinkedList<Card> cards;
	private LinkedList<Card> toDeal;
	private LinkedList<Card> answer;
	private LinkedList<Card> people, rooms, weapons;
	private LinkedList<ComputerPlayer> comps;
	private HumanPlayer player;
	private int turn;
	private Board board;
	private Random rand;
	private int numActive; //number of currently active players
	private int roll;
	private boolean playerMoved;
	Card weaponCard, personCard, roomCard;
	private boolean gameOver;
	private int winningPlayer;

	@SuppressWarnings("unchecked")
	public ClueGame() {
		board = new Board();
		cards = new LinkedList<Card>();
		answer = new LinkedList<Card>();
		people = new LinkedList<Card>();
		rooms = new LinkedList<Card>();
		weapons = new LinkedList<Card>();
		comps = new LinkedList<ComputerPlayer>();
		rand = new Random();
		numActive = 0;
		turn = -1;
		playerMoved = true;
		roll = rand.nextInt(6) + 1;
		gameOver = false;
		winningPlayer = -1;
		try {
			loadCards("Cards.txt"); //loads cards from file
			loadPlayers("Players.txt"); //loads players from file
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("A loading error occurred. Please check the config files.");
		}
		sortList(people, weapons, rooms, cards);
		toDeal = (LinkedList<Card>) cards.clone(); //error okay, known to be same type
		dealCards();
	}

	public void loadCards(String filename) throws Exception {
		File f = new File(filename);
		Scanner in = new Scanner(f);
		Scanner s = null;
		while (in.hasNextLine()) {
			s = new Scanner(in.nextLine());
			s.useDelimiter(",");
			String name = s.next().trim();
			String type = s.next().trim(); //cuts off spaces (safety feature)
			if (s.hasNext()) {
				s.close();
				in.close();
				throw new Exception("Too many lines");
			}
			Card card = new Card(name, type);
			cards.add(card);
		}
		in.close();
	}

	public void loadPlayers(String filename) throws Exception {
		File f = new File(filename);
		Scanner in = new Scanner(f);
		loadPlayer(in, 0);
		numActive++;
		while (in.hasNextLine()) {
			loadPlayer(in, 1);
			numActive++;
		}
		in.close();
	}

	public void loadPlayer(Scanner in, int type) throws Exception {
		Scanner s = new Scanner(in.nextLine());
		s.useDelimiter(",");
		while (s.hasNext()) {
			String name = s.next().trim(); //player name
			String color = s.next().trim(); //player color
			String row = s.next().trim(); //starting location row
			String col = s.next().trim(); //starting location column
			int rowNum = Integer.parseInt(row);
			int colNum = Integer.parseInt(col);
			if (s.hasNext()) {
				s.close();
				throw new Exception("Too many arguments");
			}
			//toggle - 0 for human, 1 for computer
			if (type == 0) player = new HumanPlayer(name, color, board.calcIndex(rowNum, colNum));
			else comps.add(new ComputerPlayer(name, color, board.calcIndex(rowNum, colNum)));
		}
		s.close();
	}

	public void dealCards() {
		LinkedList<Card> people = new LinkedList<Card>();
		LinkedList<Card> weapons = new LinkedList<Card>();
		LinkedList<Card> rooms = new LinkedList<Card>();
		sortList(people, weapons, rooms, toDeal);
		answer.add(people.get(rand.nextInt(people.size())));
		answer.add(weapons.get(rand.nextInt(weapons.size())));
		answer.add(rooms.get(rand.nextInt(rooms.size())));
		for (Card c : answer) toDeal.remove(c);
		int curPlayer = 0; //start dealing to human
		while (!toDeal.isEmpty()) {
			Player player;
			if (curPlayer == 0) player = this.player; //pick player to deal to
			else player = comps.get(curPlayer - 1);
			LinkedList<Card> hand = player.getCards();
			Card card = toDeal.get(rand.nextInt(toDeal.size()));
			hand.add(card); //picks a random card to give
			player.setCards(hand);
			toDeal.remove(card); //removes the card from the cards left to deal
			if (curPlayer == numActive - 1) curPlayer = 0; //move to next player
			else curPlayer++;
		}
	}

	public Card makeSuggestion(Card a, Card b, Card c, int playerIndex) {
		int currentPlayer = -1; //placeholder number to ensure a different player is picked
		while ((currentPlayer == -1) || (currentPlayer == playerIndex)) currentPlayer = rand.nextInt(numActive);
		Card card = null;
		for (int numCompared = 0; numCompared < numActive; numCompared++) {
			if (currentPlayer == playerIndex) {
				currentPlayer++;
				if (currentPlayer == numActive) currentPlayer = 0;
				continue;
			}
			Player curPlayer;
			if (currentPlayer == 0) curPlayer = player;
			else curPlayer = comps.get(currentPlayer - 1);
			card = curPlayer.disproveSuggestion(a, b, c);
			if (card != null) return card;
			++currentPlayer; //check next player
			if (currentPlayer == numActive) currentPlayer = 0; //ensures wrap-around of list
		}
		return card; 
		//should return null if no disproving card is shown
	}

	//precondition- only called by computer players (index 1 to 5)- randomizer
	public Card makeSuggestion(int playerIndex) {
		LinkedList<Card> seen = comps.get(playerIndex - 1).getSeen();
		LinkedList<Card> possibilities = new LinkedList<Card>();
		for (Card c : cards) possibilities.add(c);
		for (Card d : seen) possibilities.remove(d);
		LinkedList<Card> people = new LinkedList<Card>();
		LinkedList<Card> weapons = new LinkedList<Card>();
		LinkedList<Card> rooms = new LinkedList<Card>();
		sortList(people, weapons, rooms, possibilities);
		personCard = people.get(rand.nextInt(people.size()));
		weaponCard = weapons.get(rand.nextInt(weapons.size()));
		//Card c = rooms.get(rand.nextInt(rooms.size())); //obsolete code to select room
		RoomCell room = board.getRoomCellAt(comps.get(playerIndex - 1).getIndex());
		String roomName = board.getRooms().get(room.getInitial());
		roomCard = null;
		for (Card c : cards) {
			if (c.getName().equals(roomName)) {
				roomCard = c;
				break;
			}
		}
		return makeSuggestion(personCard, weaponCard, roomCard, playerIndex);
	}

	public void sortList(LinkedList<Card> people, LinkedList<Card> weapons, 
			LinkedList<Card> rooms, LinkedList<Card> toSort) { //method that sorts a list into the different types of cards
		for (Card d : toSort) {
			switch (d.getType()) {
			case PERSON:
				people.add(d);
				continue;
			case WEAPON:
				weapons.add(d);
				continue;
			case ROOM:
				rooms.add(d);
				continue; 
			default:
				continue;
			}
		}
	}
	
	public HashSet<Card> nextTurn() {
		turn++;
		if (turn > numActive - 1) { //max turn is players - 1
			turn = 0;
		}
		if (turn == 0) {
			setPlayerMoved(false); //senses if player has moved, reset on player turn
		}
		Player cPlayer = getCurrentPlayer();
		
		roll = rand.nextInt(6) + 1;
		if (turn > 0) { //computer's move
			ComputerPlayer ai = comps.get(turn - 1);
			if (ai.isAccuse()) {
				ai.setAccuse(false); //not to keep making the same accusation
				return ai.makeAccusation(ai.getRoomCard(), ai.getPersonCard(), ai.getWeaponCard());
			} else {
				ai.setIndex(ai.pickLocation(roll, board));
				board.getTargets().clear(); //dumps target list after move to ensure no draw of possibles
			}
		} else {
			board.startTargets(cPlayer.getIndex(), roll); //generates target list for use in GameFrame
		}
		
		return null;
	}
	
	public boolean isSolutionCorect(HashSet<Card> accusation) {
		boolean result = accusation.containsAll(answer);
		if (result) {
			//a player has won the game
			gameOver = true;
			winningPlayer = turn;
		}
		return result;
	}
	
	public int getWinningPlayer() {
		return winningPlayer;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}

	public LinkedList<Card> getCards() {
		return cards;
	}

	public void setCards(LinkedList<Card> cards) {
		this.cards = cards;
	}

	public LinkedList<ComputerPlayer> getComps() {
		return comps;
	}

	public void setComps(LinkedList<ComputerPlayer> comps) {
		this.comps = comps;
	}

	public HumanPlayer getPlayer() {
		return player;
	}

	public void setPlayer(HumanPlayer player) {
		this.player = player;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public Player getCurrentPlayer() {
		return getPlayerAt(turn);
	}
	
	public Player getPlayerAt(int index) {
		if (index == 0)
			return player;
		else
			return comps.get(index - 1);
	}
	
	public int getRoll() {
		return roll;
	}

	public LinkedList<Card> getToDeal() {
		return toDeal;
	}

	public void setToDeal(LinkedList<Card> toDeal) {
		this.toDeal = toDeal;
	}

	public LinkedList<Card> getAnswer() {
		return answer;
	}

	public void setAnswer(LinkedList<Card> answer) {
		this.answer = answer;
	}

	public Board getBoard() {
		return board;
	}
	
	public LinkedList<Card> getPeople() {
		return people;
	}

	public LinkedList<Card> getRooms() {
		return rooms;
	}

	public LinkedList<Card> getWeapons() {
		return weapons;
	}

	public boolean isPlayerMoved() {
		return playerMoved;
	}

	public void setPlayerMoved(boolean playerMoved) {
		this.playerMoved = playerMoved;
	}

	public Card getWeaponCard() {
		return weaponCard;
	}

	public void setWeaponCard(Card weaponCard) {
		this.weaponCard = weaponCard;
	}

	public Card getPersonCard() {
		return personCard;
	}

	public void setPersonCard(Card personCard) {
		this.personCard = personCard;
	}

	public Card getRoomCard() {
		return roomCard;
	}

	public void setRoomCard(Card roomCard) {
		this.roomCard = roomCard;
	}

	public void drawPlayers(Graphics g) {
		Dimension size = new Dimension(clueGUI.BoardPanel.BOARD_CELL_SIZE, clueGUI.BoardPanel.BOARD_CELL_SIZE);
		player.draw(g, size, board.getDrawingPoint(player.getIndex()));
		for (Player p: comps) {
			p.draw(g, size, board.getDrawingPoint(p.getIndex()));
		}
	}
}

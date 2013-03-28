package test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import cluePlayers.Card;
import cluePlayers.ClueGame;
import cluePlayers.ComputerPlayer;
import cluePlayers.HumanPlayer;
import cluePlayers.Player;

public class GameActionTests {
	private static ClueGame game;
	private static Board board;

	@BeforeClass
	public static void setup() {
		game = new ClueGame();
		board = game.getBoard();
	}

	@Test
	public void testAccusationCorrect() { //tests if an accusation is correct
		HashSet<Card> answer = new HashSet<Card>();
		answer.add(new Card("Mrs White", "Person"));
		answer.add(new Card("Wrench", "Weapon"));
		answer.add(new Card("Pool", "Room"));
		Player player = new HumanPlayer("Miss Scarlett", "Red", 0);
		HashSet<Card> accuse = player.makeAccusation(new Card("Mrs White", "Person"), 
				new Card("Wrench", "Weapon"), new Card("Pool", "Room"));
		HashSet<Card> difference = (HashSet<Card>) accuse.clone();
		difference.removeAll(answer);
		assertEquals(answer.size(), accuse.size());
		assertTrue(difference.isEmpty());
	}

	@Test
	public void testAccusationPerson() { //tests if the accusation has the wrong person
		HashSet<Card> answer = new HashSet<Card>();
		answer.add(new Card("Mrs White", "Person"));
		answer.add(new Card("Wrench", "Weapon"));
		answer.add(new Card("Pool", "Room"));
		LinkedList<Card> cards = new LinkedList<Card>();
		Player player = new HumanPlayer("Miss Scarlett", cards, "Red", 0);
		HashSet<Card> accuse = player.makeAccusation(new Card("Colonel Mustard", "Person"), 
				new Card("Wrench", "Weapon"), new Card("Pool", "Room"));
		HashSet<Card> difference = (HashSet<Card>) accuse.clone();
		difference.removeAll(answer);
		assertEquals(answer.size(), accuse.size());
		assertEquals(difference.size(), 1);
		assertTrue(difference.contains(new Card("Colonel Mustard", "Person")));
	}

	@Test
	public void testAccusationWeapon() { //tests if accusation has wrong weapon
		HashSet<Card> answer = new HashSet<Card>();
		answer.add(new Card("Mrs White", "Person"));
		answer.add(new Card("Wrench", "Weapon"));
		answer.add(new Card("Pool", "Room"));
		LinkedList<Card> cards = new LinkedList<Card>();
		Player player = new HumanPlayer("Miss Scarlett", cards, "Red", 0);
		HashSet<Card> accuse = player.makeAccusation(new Card("Mrs White", "Person"), 
				new Card("Candlestick", "Weapon"), new Card("Pool", "Room"));
		HashSet<Card> difference = (HashSet<Card>) accuse.clone();
		difference.removeAll(answer);
		assertEquals(answer.size(), accuse.size());
		assertEquals(difference.size(), 1);
		assertTrue(difference.contains(new Card("Candlestick", "Weapon")));
	}

	@Test
	public void testAccusationRoom() { //tests if accusation has wrong room
		HashSet<Card> answer = new HashSet<Card>();
		answer.add(new Card("Mrs White", "Person"));
		answer.add(new Card("Wrench", "Weapon"));
		answer.add(new Card("Pool", "Room"));
		LinkedList<Card> cards = new LinkedList<Card>();
		Player player = new HumanPlayer("Miss Scarlett", cards, "Red", 0);
		HashSet<Card> accuse = player.makeAccusation(new Card("Mrs White", "Person"), 
				new Card("Wrench", "Weapon"), new Card("Bowling Alley", "Room"));
		HashSet<Card> difference = (HashSet<Card>) accuse.clone();
		difference.removeAll(answer);
		assertEquals(answer.size(), accuse.size());
		assertEquals(difference.size(), 1);
		assertTrue(difference.contains(new Card("Bowling Alley", "Room")));
	}

	@Test
	public void testTargetSelect() { //various target tests, see individual comments for different instances
		LinkedList<Card> cards = new LinkedList<Card>();
		ComputerPlayer player = new ComputerPlayer("Miss Scarlett", cards, "Red", 233);
		int location = player.pickLocation(5, board);
		assertEquals(location, 267); //should only pick door
		player = new ComputerPlayer("Miss Scarlett", cards, "Red", 8);
		location = player.pickLocation(2, board);
		assertEquals(location, 44); //checks only one location possible
		//test multiple possible locations at index (10,10)
		int loc192 = 0;
		int loc209 = 0;
		int loc226 = 0; //counter variables
		player = new ComputerPlayer("Miss Scarlett", cards, "Red", 190);
		for (int i = 0; i < 100; i++) {
			location = player.pickLocation(2, board);
			switch (location) {
			case 192:
				loc192++;
				continue;
			case 209:
				loc209++;
				continue;
			case 226:
				loc226++;
				continue;
			default:
				System.out.println("You done goofed");
				continue;
			}
		}
		assertEquals(loc192 + loc209 + loc226, 100);
		assertTrue(loc192 > 0);
		assertTrue(loc209 > 0);
		assertTrue(loc226 > 0);
		//test to not reenter room
		player = new ComputerPlayer("Miss Scarlett", cards, "Red", 297);
		for (int i = 0; i < 100; i++) {
			location = player.pickLocation(3, board);
			assertFalse(location == 296);
		}
	}

	@Test
	public void testDisproveOneOne() { //disprove suggestion- one player, one card possible
		//sorry in advance for the wall of text- couldn't think of a better way to change hands
		LinkedList<Card> cardsHuman = new LinkedList<Card>(); //create hands for each player
		LinkedList<Card> cards1 = new LinkedList<Card>();
		LinkedList<Card> cards2 = new LinkedList<Card>();
		LinkedList<Card> cards3 = new LinkedList<Card>();
		LinkedList<Card> cards4 = new LinkedList<Card>();
		LinkedList<Card> cards5 = new LinkedList<Card>();
		//put cards into each player's hand (changes with every test)
		cardsHuman.add(new Card("Colonel Mustard", "Person"));
		cardsHuman.add(new Card("Reverend Green", "Person"));
		cardsHuman.add(new Card("Bowling Alley", "Room"));
		cards1.add(new Card("Miss Scarlett", "Person"));
		cards1.add(new Card("Study", "Room"));
		cards1.add(new Card("Rope", "Weapon"));
		cards2.add(new Card("Mrs White", "Person")); //this is what will be returned
		cards2.add(new Card("Revolver", "Weapon"));
		cards2.add(new Card("Foyer", "Room"));
		cards3.add(new Card("Mrs Peacock", "Person"));
		cards3.add(new Card("Candlestick", "Weapon"));
		cards3.add(new Card("Knife", "Weapon"));
		cards4.add(new Card("Lounge", "Room"));
		cards4.add(new Card("Great Hall", "Room"));
		cards4.add(new Card("Lead Pipe", "Weapon"));
		cards5.add(new Card("Bedroom", "Room"));
		cards5.add(new Card("Library", "Room"));
		cards5.add(new Card("Kitchen", "Room"));
		//creates the players with these hands
		HumanPlayer human = new HumanPlayer("Professor Plum", cardsHuman, "Purple", 0);
		ComputerPlayer player1 = new ComputerPlayer("Miss Scarlett", cards1, "Red", 297);
		ComputerPlayer player2 = new ComputerPlayer("Mrs White", cards2, "White", 297);
		ComputerPlayer player3 = new ComputerPlayer("Colonel Mustard", cards3, "Yellow", 297);
		ComputerPlayer player4 = new ComputerPlayer("Mrs Peacock", cards4, "Blue", 297);
		ComputerPlayer player5 = new ComputerPlayer("Reverend Green", cards5, "Green", 297);
		LinkedList<ComputerPlayer> players = new LinkedList<ComputerPlayer>();
		//adds the players to a LinkedList so game can iterate
		game.setPlayer(human);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game.setComps(players);
		Card shown = game.makeSuggestion(new Card("Mrs White", "Person"), 
				new Card("Wrench", "Weapon"), new Card("Pool", "Room"), 0);
		//System.out.println("One-one, card = " + shown);
		assertEquals(shown, new Card("Mrs White", "Person"));
	}

	@Test
	public void testDisproveOneTwo() { //disprove for one player, two cards
		LinkedList<Card> cardsHuman = new LinkedList<Card>();
		LinkedList<Card> cards1 = new LinkedList<Card>();
		LinkedList<Card> cards2 = new LinkedList<Card>();
		LinkedList<Card> cards3 = new LinkedList<Card>();
		LinkedList<Card> cards4 = new LinkedList<Card>();
		LinkedList<Card> cards5 = new LinkedList<Card>();
		cardsHuman.add(new Card("Colonel Mustard", "Person"));
		cardsHuman.add(new Card("Reverend Green", "Person"));
		cardsHuman.add(new Card("Bowling Alley", "Room"));
		cards1.add(new Card("Miss Scarlett", "Person"));
		cards1.add(new Card("Study", "Room"));
		cards1.add(new Card("Rope", "Weapon"));
		cards2.add(new Card("Mrs White", "Person")); //this is what will be returned
		cards2.add(new Card("Wrench", "Weapon")); //second possibility
		cards2.add(new Card("Foyer", "Room"));
		cards3.add(new Card("Mrs Peacock", "Person"));
		cards3.add(new Card("Candlestick", "Weapon"));
		cards3.add(new Card("Knife", "Weapon"));
		cards4.add(new Card("Lounge", "Room"));
		cards4.add(new Card("Great Hall", "Room"));
		cards4.add(new Card("Lead Pipe", "Weapon"));
		cards5.add(new Card("Bedroom", "Room"));
		cards5.add(new Card("Library", "Room"));
		cards5.add(new Card("Kitchen", "Room"));
		//creates the players with these hands
		HumanPlayer human = new HumanPlayer("Professor Plum", cardsHuman, "Purple", 0);
		ComputerPlayer player1 = new ComputerPlayer("Miss Scarlett", cards1, "Red", 297);
		ComputerPlayer player2 = new ComputerPlayer("Mrs White", cards2, "White", 297);
		ComputerPlayer player3 = new ComputerPlayer("Colonel Mustard", cards3, "Yellow", 297);
		ComputerPlayer player4 = new ComputerPlayer("Mrs Peacock", cards4, "Blue", 297);
		ComputerPlayer player5 = new ComputerPlayer("Reverend Green", cards5, "Green", 297);
		LinkedList<ComputerPlayer> players = new LinkedList<ComputerPlayer>();
		//adds the players to a LinkedList so game can iterate
		game.setPlayer(human);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game.setComps(players);
		int white = 0;
		int wrench = 0;
		//System.out.println("One-two");
		for (int i = 0; i < 100; i++) {
			Card shown = game.makeSuggestion(new Card("Mrs White", "Person"), 
					new Card("Wrench", "Weapon"), new Card("Pool", "Room"), 0);
			//System.out.println("Shown = " + shown);
			Card w = new Card("Wrench", "Weapon");
			//System.out.println(w);
			if (new Card("Mrs White", "Person").equals(shown)) white++;
			if (new Card("Wrench", "Weapon").equals(shown)) wrench++;
		}
		assertEquals(white + wrench, 100);
		assertTrue(white > 0);
		assertTrue(wrench > 0);
	}

	@Test
	public void testDisproveTwoOne() { //disprove for two players, one card
		LinkedList<Card> cardsHuman = new LinkedList<Card>();
		LinkedList<Card> cards1 = new LinkedList<Card>();
		LinkedList<Card> cards2 = new LinkedList<Card>();
		LinkedList<Card> cards3 = new LinkedList<Card>();
		LinkedList<Card> cards4 = new LinkedList<Card>();
		LinkedList<Card> cards5 = new LinkedList<Card>();
		cardsHuman.add(new Card("Colonel Mustard", "Person"));
		cardsHuman.add(new Card("Reverend Green", "Person"));
		cardsHuman.add(new Card("Bowling Alley", "Room"));
		cards1.add(new Card("Miss Scarlett", "Person"));
		cards1.add(new Card("Study", "Room"));
		cards1.add(new Card("Rope", "Weapon"));
		cards2.add(new Card("Mrs White", "Person")); //this is what will be returned
		cards2.add(new Card("Revolver", "Weapon")); 
		cards2.add(new Card("Foyer", "Room"));
		cards3.add(new Card("Mrs Peacock", "Person"));
		cards3.add(new Card("Candlestick", "Weapon"));
		cards3.add(new Card("Knife", "Weapon"));
		cards4.add(new Card("Lounge", "Room"));
		cards4.add(new Card("Pool", "Room")); //second player possibility
		cards4.add(new Card("Lead Pipe", "Weapon"));
		cards5.add(new Card("Bedroom", "Room"));
		cards5.add(new Card("Library", "Room"));
		cards5.add(new Card("Kitchen", "Room"));
		//creates the players with these hands
		HumanPlayer human = new HumanPlayer("Professor Plum", cardsHuman, "Purple", 0);
		ComputerPlayer player1 = new ComputerPlayer("Miss Scarlett", cards1, "Red", 297);
		ComputerPlayer player2 = new ComputerPlayer("Mrs White", cards2, "White", 297);
		ComputerPlayer player3 = new ComputerPlayer("Colonel Mustard", cards3, "Yellow", 297);
		ComputerPlayer player4 = new ComputerPlayer("Mrs Peacock", cards4, "Blue", 297);
		ComputerPlayer player5 = new ComputerPlayer("Reverend Green", cards5, "Green", 297);
		LinkedList<ComputerPlayer> players = new LinkedList<ComputerPlayer>();
		//adds the players to a LinkedList so game can iterate
		game.setPlayer(human);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game.setComps(players);
		int white = 0;
		int pool = 0;
		for (int i = 0; i < 100; i++) {
			Card shown = game.makeSuggestion(new Card("Mrs White", "Person"), 
					new Card("Wrench", "Weapon"), new Card("Pool", "Room"), 0);
			if (new Card("Mrs White", "Person").equals(shown)) white++;
			if (new Card("Pool", "Room").equals(shown)) pool++;
		}
		assertEquals(white + pool, 100);
		assertTrue(white > 0);
		assertTrue(pool > 0);
	}

	@Test 
	public void testDisproveHuman() { //disprove with human player's card only
		LinkedList<Card> cardsHuman = new LinkedList<Card>();
		LinkedList<Card> cards1 = new LinkedList<Card>();
		LinkedList<Card> cards2 = new LinkedList<Card>();
		LinkedList<Card> cards3 = new LinkedList<Card>();
		LinkedList<Card> cards4 = new LinkedList<Card>();
		LinkedList<Card> cards5 = new LinkedList<Card>();
		cardsHuman.add(new Card("Colonel Mustard", "Person"));
		cardsHuman.add(new Card("Mrs White", "Person")); //player's card, should be returned
		cardsHuman.add(new Card("Bowling Alley", "Room"));
		cards1.add(new Card("Miss Scarlett", "Person"));
		cards1.add(new Card("Study", "Room"));
		cards1.add(new Card("Rope", "Weapon"));
		cards2.add(new Card("Reverend Green", "Person")); 
		cards2.add(new Card("Revolver", "Weapon")); 
		cards2.add(new Card("Foyer", "Room"));
		cards3.add(new Card("Mrs Peacock", "Person"));
		cards3.add(new Card("Candlestick", "Weapon"));
		cards3.add(new Card("Knife", "Weapon"));
		cards4.add(new Card("Lounge", "Room"));
		cards4.add(new Card("Great Hall", "Room")); 
		cards4.add(new Card("Lead Pipe", "Weapon"));
		cards5.add(new Card("Bedroom", "Room"));
		cards5.add(new Card("Library", "Room"));
		cards5.add(new Card("Kitchen", "Room"));
		//creates the players with these hands
		HumanPlayer human = new HumanPlayer("Professor Plum", cardsHuman, "Purple", 0);
		ComputerPlayer player1 = new ComputerPlayer("Miss Scarlett", cards1, "Red", 297);
		ComputerPlayer player2 = new ComputerPlayer("Mrs White", cards2, "White", 297);
		ComputerPlayer player3 = new ComputerPlayer("Colonel Mustard", cards3, "Yellow", 297);
		ComputerPlayer player4 = new ComputerPlayer("Mrs Peacock", cards4, "Blue", 297);
		ComputerPlayer player5 = new ComputerPlayer("Reverend Green", cards5, "Green", 297);
		LinkedList<ComputerPlayer> players = new LinkedList<ComputerPlayer>();
		//adds the players to a LinkedList so game can iterate
		game.setPlayer(human);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game.setComps(players);
		Card shown = game.makeSuggestion(new Card("Mrs White", "Person"), 
				new Card("Wrench", "Weapon"), new Card("Pool", "Room"), 2);
		assertEquals(shown, new Card("Mrs White", "Person"));
	}

	@Test
	public void testDisproveSelf() { //disprove when holding suggested card (should return null)
		LinkedList<Card> cardsHuman = new LinkedList<Card>();
		LinkedList<Card> cards1 = new LinkedList<Card>();
		LinkedList<Card> cards2 = new LinkedList<Card>();
		LinkedList<Card> cards3 = new LinkedList<Card>();
		LinkedList<Card> cards4 = new LinkedList<Card>();
		LinkedList<Card> cards5 = new LinkedList<Card>();
		cardsHuman.add(new Card("Colonel Mustard", "Person"));
		cardsHuman.add(new Card("Reverend Green", "Person")); 
		cardsHuman.add(new Card("Bowling Alley", "Room"));
		cards1.add(new Card("Miss Scarlett", "Person"));
		cards1.add(new Card("Study", "Room"));
		cards1.add(new Card("Rope", "Weapon"));
		cards2.add(new Card("Mrs White", "Person")); //player 2 is suggesting their own card
		cards2.add(new Card("Revolver", "Weapon")); 
		cards2.add(new Card("Foyer", "Room"));
		cards3.add(new Card("Mrs Peacock", "Person"));
		cards3.add(new Card("Candlestick", "Weapon"));
		cards3.add(new Card("Knife", "Weapon"));
		cards4.add(new Card("Lounge", "Room"));
		cards4.add(new Card("Great Hall", "Room")); 
		cards4.add(new Card("Lead Pipe", "Weapon"));
		cards5.add(new Card("Bedroom", "Room"));
		cards5.add(new Card("Library", "Room"));
		cards5.add(new Card("Kitchen", "Room"));
		//creates the players with these hands
		HumanPlayer human = new HumanPlayer("Professor Plum", cardsHuman, "Purple", 0);
		ComputerPlayer player1 = new ComputerPlayer("Miss Scarlett", cards1, "Red", 297);
		ComputerPlayer player2 = new ComputerPlayer("Mrs White", cards2, "White", 297);
		ComputerPlayer player3 = new ComputerPlayer("Colonel Mustard", cards3, "Yellow", 297);
		ComputerPlayer player4 = new ComputerPlayer("Mrs Peacock", cards4, "Blue", 297);
		ComputerPlayer player5 = new ComputerPlayer("Reverend Green", cards5, "Green", 297);
		LinkedList<ComputerPlayer> players = new LinkedList<ComputerPlayer>();
		//adds the players to a LinkedList so game can iterate
		game.setPlayer(human);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game.setComps(players);
		Card shown = game.makeSuggestion(new Card("Mrs White", "Person"), 
				new Card("Wrench", "Weapon"), new Card("Pool", "Room"), 2);
		assertEquals(shown, null); //should find no matches
	}

	@Test
	public void testSuggestionCorrect() { //tests making a suggestion when all cards are known
		LinkedList<Card> cardsHuman = new LinkedList<Card>();
		LinkedList<Card> cards1 = new LinkedList<Card>();
		LinkedList<Card> cards2 = new LinkedList<Card>();
		LinkedList<Card> cards3 = new LinkedList<Card>();
		LinkedList<Card> cards4 = new LinkedList<Card>();
		LinkedList<Card> cards5 = new LinkedList<Card>();
		cardsHuman.add(new Card("Colonel Mustard", "Person"));
		cardsHuman.add(new Card("Professor Plum", "Person"));
		cardsHuman.add(new Card("Bowling Alley", "Room"));
		cards1.add(new Card("Miss Scarlett", "Person"));
		cards1.add(new Card("Study", "Room"));
		cards1.add(new Card("Rope", "Weapon"));
		cards2.add(new Card("Reverend Green", "Person")); 
		cards2.add(new Card("Revolver", "Weapon")); 
		cards2.add(new Card("Foyer", "Room"));
		cards3.add(new Card("Mrs Peacock", "Person"));
		cards3.add(new Card("Candlestick", "Weapon"));
		cards3.add(new Card("Knife", "Weapon"));
		cards4.add(new Card("Lounge", "Room"));
		cards4.add(new Card("Great Hall", "Room")); 
		cards4.add(new Card("Lead Pipe", "Weapon"));
		cards5.add(new Card("Bedroom", "Room"));
		cards5.add(new Card("Library", "Room"));
		cards5.add(new Card("Kitchen", "Room"));
		//creates the players with these hands
		HumanPlayer human = new HumanPlayer("Professor Plum", cardsHuman, "Purple", 0);
		ComputerPlayer player1 = new ComputerPlayer("Miss Scarlett", cards1, "Red", 297);
		ComputerPlayer player2 = new ComputerPlayer("Mrs White", cards2, "White", 297);
		ComputerPlayer player3 = new ComputerPlayer("Colonel Mustard", cards3, "Yellow", 297);
		ComputerPlayer player4 = new ComputerPlayer("Mrs Peacock", cards4, "Blue", 297);
		ComputerPlayer player5 = new ComputerPlayer("Reverend Green", cards5, "Green", 297);
		LinkedList<ComputerPlayer> players = new LinkedList<ComputerPlayer>();
		//adds the players to a LinkedList so game can iterate
		game.setPlayer(human);
		LinkedList<Card> cardsSeen = (LinkedList<Card>) game.getCards().clone();
		cardsSeen.remove(new Card("Mrs White", "Person"));
		cardsSeen.remove(new Card("Wrench", "Weapon"));
		cardsSeen.remove(new Card("Pool", "Room"));
		player2.setSeen(cardsSeen);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game.setComps(players);
		Card shown = game.makeSuggestion(2);
		assertEquals(shown, null);
	}

	@Test
	public void testSuggestionRandom() { //tests when AI hasn't seen all other cards
		LinkedList<Card> cardsHuman = new LinkedList<Card>();
		LinkedList<Card> cards1 = new LinkedList<Card>();
		LinkedList<Card> cards2 = new LinkedList<Card>();
		LinkedList<Card> cards3 = new LinkedList<Card>();
		LinkedList<Card> cards4 = new LinkedList<Card>();
		LinkedList<Card> cards5 = new LinkedList<Card>();
		cardsHuman.add(new Card("Colonel Mustard", "Person"));
		cardsHuman.add(new Card("Professor Plum", "Person"));
		cardsHuman.add(new Card("Bowling Alley", "Room"));
		cards1.add(new Card("Miss Scarlett", "Person"));
		cards1.add(new Card("Study", "Room"));
		cards1.add(new Card("Rope", "Weapon"));
		cards2.add(new Card("Reverend Green", "Person")); 
		cards2.add(new Card("Revolver", "Weapon")); 
		cards2.add(new Card("Foyer", "Room"));
		cards3.add(new Card("Mrs Peacock", "Person"));
		cards3.add(new Card("Candlestick", "Weapon"));
		cards3.add(new Card("Knife", "Weapon"));
		cards4.add(new Card("Lounge", "Room"));
		cards4.add(new Card("Great Hall", "Room")); 
		cards4.add(new Card("Lead Pipe", "Weapon"));
		cards5.add(new Card("Bedroom", "Room"));
		cards5.add(new Card("Library", "Room"));
		cards5.add(new Card("Kitchen", "Room"));
		//creates the players with these hands
		HumanPlayer human = new HumanPlayer("Professor Plum", cardsHuman, "Purple", 0);
		ComputerPlayer player1 = new ComputerPlayer("Miss Scarlett", cards1, "Red", 297);
		ComputerPlayer player2 = new ComputerPlayer("Mrs White", cards2, "White", 297);
		ComputerPlayer player3 = new ComputerPlayer("Colonel Mustard", cards3, "Yellow", 297);
		ComputerPlayer player4 = new ComputerPlayer("Mrs Peacock", cards4, "Blue", 297);
		ComputerPlayer player5 = new ComputerPlayer("Reverend Green", cards5, "Green", 297);
		LinkedList<ComputerPlayer> players = new LinkedList<ComputerPlayer>();
		//adds the players to a LinkedList so game can iterate
		game.setPlayer(human);
		//sets the list of cards seen by player 2
		LinkedList<Card> cardsSeen = (LinkedList<Card>) game.getCards().clone();
		cardsSeen.remove(new Card("Mrs White", "Person"));
		cardsSeen.remove(new Card("Wrench", "Weapon"));
		cardsSeen.remove(new Card("Pool", "Room"));
		cardsSeen.remove(new Card("Mrs Peacock", "Person"));
		player2.setSeen(cardsSeen);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game.setComps(players);
		int white = 0;
		int peacock = 0;
		for (int i = 0; i < 100; i++) {
			Card shown = game.makeSuggestion(2); //generic computer suggestion, with random choice
			if (new Card("Mrs Peacock", "Person").equals(shown)) {
				peacock++; //sees someone else has Peacock
			} if (shown == null) {
				white++; //since White is in closet, won't return anything
			}
		}
		assertEquals(white + peacock, 100);
		assertTrue(white > 0);
		assertTrue(peacock > 0);
	}
}

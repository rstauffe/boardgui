package test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

import cluePlayers.Card;
import cluePlayers.ClueGame;
import cluePlayers.ComputerPlayer;
import cluePlayers.Player;

public class LoadingTests {
	private static ClueGame game;
	public static final int NUM_COLUMNS = 18;
	
	@BeforeClass
	public static void setup() {
		game = new ClueGame();
	}
	
	@Test
	public void testHuman() { //tests loading human player
		Player human = game.getPlayer();
		assertEquals(human.getName(), "Professor Plum");
		assertEquals(human.getColor(), "Magenta"); //had to change color for contrast
		assertEquals(human.getIndex(), 0*NUM_COLUMNS + 4);
	}

	@Test
	public void testComputer1() { //tests loading first AI
		Player ai1 = game.getComps().get(0);
		assertEquals(ai1.getName(), "Reverend Green");
		assertEquals(ai1.getColor(), "Green");
		assertEquals(ai1.getIndex(), 0*NUM_COLUMNS + 13);
	}
	
	@Test
	public void testComputer5() { //tests loading last AI
		Player ai5 = game.getComps().get(4);
		assertEquals(ai5.getName(), "Miss Scarlett");
		assertEquals(ai5.getColor(), "Red");
		assertEquals(ai5.getIndex(), 13*NUM_COLUMNS + 17);
	}
	
	@Test
	public void testCards() { //test loading cards
		LinkedList<Card> cards = game.getCards();
		assertEquals(21, cards.size());
		int rooms = 0;
		int weapons = 0;
		int people = 0;
		for (Card c : cards) {
			switch (c.getType()) {
			case ROOM:
				rooms++;
				continue;
			case WEAPON:
				weapons++;
				continue;
			case PERSON:
				people++;
				continue;
			}
		}
		//test correct number of each type appears in deck
		assertEquals(people, 6);
		assertEquals(weapons, 6);
		assertEquals(rooms, 9);
		//test specific cards
		Card card = new Card("Professor Plum", "Person");
		assertTrue(cards.contains(card));
		card = new Card("Miss Scarlett", "Person");
		assertTrue(cards.contains(card));
		card = new Card("Great Hall", "Room");
		assertTrue(cards.contains(card));
		card = new Card("Knife", "Weapon");
		assertTrue(cards.contains(card));
	}
	
	@Test
	public void testDeal() { //tests dealing cards
		LinkedList<Card> toDeal = game.getToDeal();
		assertTrue(toDeal.isEmpty()); //shows all cards dealt
		Player human = game.getPlayer();
		int numCards = human.getCards().size();
		LinkedList<ComputerPlayer> ais = game.getComps();
		for (ComputerPlayer c : ais) {
			assertFalse((Math.abs(c.getCards().size()-numCards) > 1)); //test if hands are similar size
		}
		HashSet<Card> cardsDealt = new HashSet<Card>();
		int numDealt = 0;
		for (Card c : human.getCards()) {
			cardsDealt.add(c);
			numDealt++;
		}
		for (ComputerPlayer ai : ais) {
			for (Card c : ai.getCards()) {
				cardsDealt.add(c);
				numDealt++;
			}
		}
		for (Card c : game.getAnswer()) {
			cardsDealt.add(c);
			numDealt++;
		}
		assertEquals(numDealt, 21); //checks all cards dealt
		assertEquals(21, cardsDealt.size()); //check that all cards only dealt once
	}
}

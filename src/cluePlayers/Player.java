package cluePlayers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Player {
	private String name;
	private LinkedList<Card> cards;
	private String color;
	private int index; 
	private Random rand = new Random();

	public Player(String name, LinkedList<Card> cards, String color, int index) {
		this.name = name;
		this.cards = cards;
		this.color = color;
		this.index = index;
	}

	public Player(String name, String color, int index) {
		this.name = name;
		this.color = color;
		this.index = index;
		cards = new LinkedList<Card>();

	}

	public Card disproveSuggestion(Card a, Card b, Card c) {
		@SuppressWarnings("unchecked") //ignores recast warning
		LinkedList<Card> hand = (LinkedList<Card>) cards.clone();
		LinkedList<Card> matches = new LinkedList<Card>();
		for (Card d : hand) {
			if ((d.equals(c)) || (d.equals(b)) || (d.equals(a))) matches.add(d);
		}
		if (!matches.isEmpty()) {
			return matches.get(rand.nextInt(matches.size())); //returns a random card from hand if matching
		}	
		return null; 
	}

	public HashSet<Card> makeAccusation(Card a, Card b, Card c) {
		HashSet<Card> accuse = new HashSet<Card>();
		accuse.add(a);
		accuse.add(b);
		accuse.add(c);
		return accuse;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<Card> getCards() {
		return cards;
	}

	public void setCards(LinkedList<Card> cards) {
		this.cards = cards;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

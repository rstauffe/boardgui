package cluePlayers;

import java.util.LinkedList;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name, LinkedList<Card> cards, String color, int index) {
		super(name, cards, color, index);
	}
	
	public HumanPlayer(String name, String color, int index) {
		super(name, color, index);
	}
}

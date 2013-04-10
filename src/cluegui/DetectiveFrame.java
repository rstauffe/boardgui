package clueGUI;

import java.awt.GridLayout;

import javax.swing.JFrame;

import cluePlayers.ClueGame;

public class DetectiveFrame extends JFrame {
	
	public DetectiveFrame(ClueGame game) {
		setTitle("Notes");
		setSize(600, 600);
		setLocation(770, 20);
		NotePanel pPanel = new NotePanel("People", game);
		GuessPanel pGuess = new GuessPanel("People", game);
		NotePanel rPanel = new NotePanel("Rooms", game);
		GuessPanel rGuess = new GuessPanel("Rooms", game);
		NotePanel wPanel = new NotePanel("Weapons", game);
		GuessPanel wGuess = new GuessPanel("Weapons", game);
		setLayout(new GridLayout(3, 2));
		add(pPanel);
		add(pGuess);
		add(rPanel);
		add(rGuess);
		add(wPanel);
		add(wGuess);
	}
}

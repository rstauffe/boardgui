package clueGUI;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class DetectiveFrame extends JFrame {
	
	public DetectiveFrame() {
		setTitle("Notes");
		setSize(600, 600);
		setLocation(770, 20);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		NotePanel pPanel = new NotePanel("People");
		GuessPanel pGuess = new GuessPanel("People");
		NotePanel rPanel = new NotePanel("Rooms");
		GuessPanel rGuess = new GuessPanel("Rooms");
		NotePanel wPanel = new NotePanel("Weapons");
		GuessPanel wGuess = new GuessPanel("Weapons");
		setLayout(new GridLayout(3, 2));
		add(pPanel);
		add(pGuess);
		add(rPanel);
		add(rGuess);
		add(wPanel);
		add(wGuess);
	}
}

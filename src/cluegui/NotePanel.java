package clueGUI;

import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cluePlayers.Card;
import cluePlayers.ClueGame;

public class NotePanel extends JPanel {
	private static ClueGame game = new ClueGame();
	
	public NotePanel(String type) {
		setLayout(new GridLayout(0, 2));
		
		switch(type) {
		case "People":
			for (Card c : game.getPeople()) {
				JCheckBox person = new JCheckBox(c.getName());
				add(person);
			}
			break;
		case "Rooms":
			for (Card c : game.getRooms()) {
				JCheckBox room = new JCheckBox(c.getName());
				add(room);
			}
			break;
		case "Weapons":
			for (Card c : game.getWeapons()) {
				JCheckBox weapon = new JCheckBox(c.getName());
				add(weapon);
			}
			break;
		}
		setBorder(new TitledBorder(new EtchedBorder(), type));
	}
}

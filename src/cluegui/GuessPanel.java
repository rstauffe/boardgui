package clueGUI;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cluePlayers.Card;
import cluePlayers.ClueGame;

public class GuessPanel extends JPanel {
	
	public GuessPanel(String type, ClueGame game) {
		JComboBox<String> list = new JComboBox<String>();
		
		switch(type) {
		case "People":
			for (Card c : game.getPeople()) {
				list.addItem(c.getName());
			}
			break;
		case "Rooms":
			for (Card c : game.getRooms()) {
				list.addItem(c.getName());
			}
			break;
		case "Weapons":
			for (Card c : game.getWeapons()) {
				list.addItem(c.getName());
			}
			break;
		}
		setBorder(new TitledBorder(new EtchedBorder(), type + " Guess"));
		add(list);
	}
}

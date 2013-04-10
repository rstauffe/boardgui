package clueGUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cluePlayers.Card;
import cluePlayers.ClueGame;

public class SuggestionPanel extends JPanel {
	private Card personCard, weaponCard;
	private JComboBox<String> people, weapons;
	private LinkedList<Card> peopleList, weaponList;

	public SuggestionPanel(ClueGame game, Card roomCard) {
		setLayout(new GridLayout(0, 2));
		peopleList = game.getPeople();
		weaponList = game.getWeapons();
		JTextField room = new JTextField(12);
		JLabel roomLabel = new JLabel("Current room: ");
		room.setEditable(false);
		if (roomCard != null)
			room.setText(roomCard.getName());
		people = new JComboBox<String>();
		weapons = new JComboBox<String>();
		JLabel peopleLabel = new JLabel("Person:");
		JLabel weaponLabel = new JLabel("Weapon:");

		for (Card c : game.getPeople()) {
			people.addItem(c.getName());
			if (personCard == null)
				personCard = c; //set defaults
		}
		for (Card c : game.getWeapons()) {
			weapons.addItem(c.getName());
			if (weaponCard == null)
				weaponCard = c; //set defaults
		}
		add(roomLabel);
		add(room);

		class ComboListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == people) {
					for (Card c : peopleList) {
						if (people.getSelectedItem().equals(c.getName())) {
							personCard = c;
							break;
						}
					}
				} else {
					for (Card c : weaponList) {
						if (weapons.getSelectedItem().equals(c.getName())) {
							weaponCard = c;
							break;
						}
					}
				}
			}
		}
		ComboListener listener = new ComboListener();
		people.addActionListener(listener);
		weapons.addActionListener(listener);
		add(peopleLabel);
		add(people);
		add(weaponLabel);
		add(weapons);
		
	}

	public Card getPersonCard() {
		return personCard;
	}

	public Card getWeaponCard() {
		return weaponCard;
	}
}

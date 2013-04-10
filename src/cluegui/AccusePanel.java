package clueGUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cluePlayers.Card;
import cluePlayers.ClueGame;

public class AccusePanel extends JPanel {
	private Card roomCard, personCard, weaponCard;
	private JComboBox<String> rooms, people, weapons;
	private LinkedList<Card> roomList, peopleList, weaponList;
	
	public AccusePanel(ClueGame game) {
		setLayout(new GridLayout(0, 2));
		roomList = game.getRooms();
		peopleList = game.getPeople();
		weaponList = game.getWeapons();
		rooms = new JComboBox<String>();
		people = new JComboBox<String>();
		weapons = new JComboBox<String>();
		JLabel roomLabel = new JLabel("Room: ");
		JLabel peopleLabel = new JLabel("Person:");
		JLabel weaponLabel = new JLabel("Weapon:");

		for (Card c : roomList) {
			rooms.addItem(c.getName());
			if (roomCard == null)
				roomCard = c;
		}
		for (Card c : peopleList) {
			people.addItem(c.getName());
			if (personCard == null)
				personCard = c; //set defaults
		}
		for (Card c : weaponList) {
			weapons.addItem(c.getName());
			if (weaponCard == null)
				weaponCard = c; //set defaults
		}

		class ComboListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == people) {
					for (Card c : peopleList) {
						if (people.getSelectedItem().equals(c.getName())) {
							personCard = c;
							break;
						}
					}
				} else if (e.getSource() == weapons) {
					for (Card c : weaponList) {
						if (weapons.getSelectedItem().equals(c.getName())) {
							weaponCard = c;
							break;
						}
					}
				} else {
					for (Card c : roomList) {
						if (rooms.getSelectedItem().equals(c.getName())) {
							roomCard = c;
							break;
						}
					}
				}
			}
		}
		ComboListener listener = new ComboListener();
		rooms.addActionListener(listener);
		people.addActionListener(listener);
		weapons.addActionListener(listener);
		add(roomLabel);
		add(rooms);
		add(peopleLabel);
		add(people);
		add(weaponLabel);
		add(weapons);
	}

	public Card getRoomCard() {
		return roomCard;
	}

	public Card getPersonCard() {
		return personCard;
	}

	public Card getWeaponCard() {
		return weaponCard;
	}
}

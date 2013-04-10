package clueGUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import cluePlayers.Card;
import cluePlayers.ClueGame;

public class AccuseFrame extends JDialog {
	private Card roomCard, personCard, weaponCard;
	private AccusePanel display;
	private JButton submit, cancel;
	private boolean submitted;

	public AccuseFrame(ClueGame game) {
		setModal(true);
		submitted = false;
		setTitle("Make an Accusation");
		setSize(300, 200);
		display = new AccusePanel(game);
		submit = new JButton("Submit");
		cancel = new JButton("Cancel");

		class ButtonListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == submit) {
					roomCard = display.getRoomCard();
					personCard = display.getPersonCard();
					weaponCard = display.getWeaponCard();
					submitted = true;
					setVisible(false);
				} else {
					submitted = false;
					setVisible(false);
				}
			}
		}

		submit.addActionListener(new ButtonListener());
		cancel.addActionListener(new ButtonListener());
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 2));
		buttons.add(submit);
		buttons.add(cancel);
		add(display, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
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

	public boolean isSubmitted() {
		return submitted;
	}
}

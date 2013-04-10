package clueGUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cluePlayers.Card;
import cluePlayers.ClueGame;

public class SuggestionFrame extends JDialog {
	private Card personCard, weaponCard;
	private SuggestionPanel display;
	
	public SuggestionFrame(ClueGame game, Card roomCard) {
		setModal(true);
		setTitle("Make a Suggestion");
		setSize(300, 200);
		display = new SuggestionPanel(game, roomCard);
		JButton submit = new JButton("Submit");
		
		class ButtonListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				personCard = display.getPersonCard();
				weaponCard = display.getWeaponCard();
				setVisible(false);
			}
		}
		
		submit.addActionListener(new ButtonListener());
		add(display, BorderLayout.CENTER);
		add(submit, BorderLayout.SOUTH);
	}

	public Card getPersonCard() {
		return personCard;
	}

	public Card getWeaponCard() {
		return weaponCard;
	}
}

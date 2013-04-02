package clueGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameFrame extends JFrame {
	
	private JTextField whoseTurn;

	public GameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Game Control");
		setSize(new Dimension(700, 200));
		
		//Main panel, contains all components
		JPanel mainPanel = new JPanel();
		
		//Whose Turn? section
		JPanel whoseTurnPanel = new JPanel();
		whoseTurnPanel.setLayout(new GridLayout(2, 0));
		JLabel whoseTurnLabel = new JLabel("Whose Turn?");
		whoseTurnPanel.add(whoseTurnLabel);
		whoseTurn = new JTextField(15);
		whoseTurnPanel.add(whoseTurn);
		mainPanel.add(whoseTurnPanel);
		
		//Next Player and Make Accusation buttons
		JButton nextPlayer = new JButton("Next Player");
		JButton makeAccusation = new JButton("Make Accusation");
		mainPanel.add(nextPlayer);
		mainPanel.add(makeAccusation);
		
		//Die roll
		JPanel dieRollPanel = new JPanel();
		JTextField dieRoll = new JTextField(2);
		dieRoll.setEditable(false);  //Can't edit this field
		JLabel dieRollLabel = new JLabel("Roll:");
		dieRollPanel.add(dieRollLabel);
		dieRollPanel.add(dieRoll);
		dieRollPanel.setBorder(new TitledBorder(new EtchedBorder(), "Die Roll"));
		mainPanel.add(dieRollPanel);
		
		//Guess panel
		JPanel guessPanel = new JPanel();
		JTextField guessText = new JTextField(20);
		guessText.setEditable(false);  //Can't edit this field
		JLabel guess = new JLabel("Guess:");
		guessPanel.add(guess);
		guessPanel.add(guessText);
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		mainPanel.add(guessPanel);
		
		//Guess Result panel
		JPanel guessResultPanel = new JPanel();
		JTextField guessResultText = new JTextField(20);
		guessResultText.setEditable(false);  //Can't edit this field
		JLabel guessResult = new JLabel("Guess Result:");
		guessResultPanel.add(guessResult);
		guessResultPanel.add(guessResultText);
		guessResultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		mainPanel.add(guessResultPanel);
		
		add(mainPanel, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		GameFrame gui = new GameFrame();
		gui.setVisible(true);
	}

}

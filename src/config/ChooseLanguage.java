package config;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import text.Text;
import text.TextEnglish;
import text.TextFrancais;

public class ChooseLanguage extends JDialog {
	private Text language = null;
	private JRadioButton french;
	private JRadioButton english;

	public ChooseLanguage(JFrame parent, boolean modal, Text text) {
		super(parent, text.windowTitle() + " - " + text.chooseLanguageTitle(), modal);
		setSize(350, 140);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// Instruction for the user
		JLabel languageLabel = new JLabel(text.chooseLanguageInstruction());		
		JPanel north = new JPanel();
		north.add(languageLabel);
		container.add(north, BorderLayout.NORTH);

		// Available language to choose
		english = new JRadioButton(text.englishLanguageName());		french = new JRadioButton(text.frenchLanguageName());		

		ButtonGroup language = new ButtonGroup();
		if (text.isFrenchLanguage()) {
			french.setSelected(true);
		} else if (text.isEnglishLanguage()) {
			english.setSelected(true);
		}
		language.add(english);
		language.add(french);
		JPanel languagePan = new JPanel();
		languagePan.setLayout(new GridLayout(2, 1, 5, 5));
		languagePan.add(english);
		languagePan.add(french);
		container.add(languagePan, BorderLayout.CENTER);

		// Button to valide its choice
		JButton languageButton = new JButton("Valider");
		languageButton.addActionListener(new LanguageButtonListener());
		JPanel south = new JPanel();
		south.add(languageButton);
		container.add(south, BorderLayout.SOUTH);

		setContentPane(container);
	}

	public Text getText() {
		// User can now interact with the dialog box
		setVisible(true);
		// When user stops use it (when isVisible == false), we return his language
		return language;
	}

	class LanguageButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (french.isSelected()) {
				language = new TextFrancais();
			} else {
				language = new TextEnglish();
			}
			// End of the dialogue
			setVisible(false);
		}
	}
}

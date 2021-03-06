package base;

import javax.swing.JOptionPane;

import config.ChooseLanguage;
import config.ConfigStorage;
import config.WorkingSession;
import text.Text;
import text.TextEnglish;
import text.TextFrancais;

/**
 * Main of the program
 * 
 * @author Mouchi
 *
 */
public class Main {
	public static void main(String[] args) {
		Window window = null;
		try {
			// Language define by the script
			Text defaultText;
			if (args.length > 0 && args[0].equals("-fr")) {
				defaultText = new TextFrancais();
			} else {
				defaultText = new TextEnglish();
			}

			// Gather the configuration file
			final String configurationFile = "ressources/config.xml";
			ConfigStorage configuration = new ConfigStorage(configurationFile);
			Text text = configuration.getText();

			if (text == null) {
				// The configuration is invalid (or don't exist)
				// Language define by the user
				ChooseLanguage languageChoiceDialog = new ChooseLanguage(null, true,
						defaultText);
				text = languageChoiceDialog.getText();
				if (text == null) {
					// The user cancels
					languageChoiceDialog.dispose();
					throw new IllegalArgumentException(defaultText.missingLanguage());
				}

				// Create the configuration file
				configuration = new ConfigStorage(text, configurationFile);
				configuration.saveConfigFile();

				// Create an empty window
				window = new Window(text, configuration);
			} else {
				if (configuration.hasWorkingSession()) {
					// Try to create a window with the first working session
					WorkingSession ws;
					ws = configuration.getFirst();
					window = new Window(ws, text, configuration);
				} else {
					// Create an empty window
					window = new Window(text, configuration);
				}
			}
		} catch (Exception e) {
			try {
				if (!e.getMessage().equals("")) {
					JOptionPane.showMessageDialog(window, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(window, "An error has occured", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				// Close application
				if (window != null) {
					window.dispose();
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(window, "An error has occured", "ERROR", JOptionPane.ERROR_MESSAGE);
				// Close application
				if (window != null) {
					window.dispose();
				}
			}
		}
	}
}

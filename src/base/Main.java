package base;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import config.ChooseLanguage;
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
		// Language define by the script
		Text defaultText;
		if (args.length > 0 && args[0].equals("-en")) {
			defaultText = new TextEnglish();
		} else {
			defaultText = new TextFrancais();
		}

		// TODO read config file, choose language if it doesn't exist
		// Language define by the user
		ChooseLanguage languageChoiceDialog = new ChooseLanguage(null, true,
				defaultText);
		Text text = languageChoiceDialog.getText();
		if (text == null) {
			languageChoiceDialog.dispose();
			throw new IllegalArgumentException(defaultText.missingLanguage());
		}
		
		// TODO read config file, load the last working session
		Window window = new Window(text);
	}
}

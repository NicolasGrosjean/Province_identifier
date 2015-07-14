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
		
		// Read files
		final String nomFichierLecture = "definition.csv";
		final String nomFichierProvince = "provinces.bmp";
		FileInputStream fichierLecture = null;
		
		/**
		 * Province data base
		 */
		ProvinceStorage provinces = new ProvinceStorage();
		
		try {
			// Open file
			fichierLecture = new FileInputStream(nomFichierLecture);

			// Read by separating with ";"
			Scanner scanner = new Scanner(fichierLecture, "ISO-8859-1"); 
			// ISO-8859-1 for special char
			scanner.useDelimiter(Pattern.compile("[;\n]"));
			
			// Searching integer
			while (!scanner.hasNextInt()) {
				@SuppressWarnings("unused")
				String useless = scanner.next();
			}

			while (scanner.hasNextInt()) {
				// Reading a province			
				int id = scanner.nextInt();
				if (!scanner.hasNextInt())
					break; // End of reading
				int r = scanner.nextInt();
				if (!scanner.hasNextInt())
					break; // End of reading
				int g = scanner.nextInt();	
				if (!scanner.hasNextInt())
					break; // End of reading
				int b = scanner.nextInt();
				String nom = scanner.next();
				while (!scanner.hasNextInt() && scanner.hasNext()) {
					@SuppressWarnings("unused")
					String useless = scanner.next();
				}
				
				// Store the province
				provinces.addProvince(id, r, g, b, nom);
			}
			scanner.close();
			
			// Create the map to display
			Panel pan = new Panel(nomFichierProvince, text);
			
			// Create the window
			@SuppressWarnings("unused")
			Window window = new Window(provinces, pan, text, new MiniMap(nomFichierProvince, text, pan));			
		} catch (FileNotFoundException e) {
			System.out.println(text.fileNotFound(nomFichierLecture));
		} catch (IOException e) {
			System.out.println(text.fileNotFound(nomFichierLecture));
		} finally {
			try {
				if (fichierLecture != null)
					fichierLecture.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

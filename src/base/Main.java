package base;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import text.Text;
import text.TextEnglish;
import text.TextFrancais;
import version2.FenetreV2;


public class Main {
	public static void main(String[] args) {
		boolean v2 = true; // version du logiciel
		//boolean v2 = false;
		
		// Langue
		Text text;
		if (args.length > 0 && args[0].equals("-en")) {
			text = new TextEnglish();
		} else {
			text = new TextFrancais();
		}
		
		// Fichiers lus
		final String nomFichierLecture = "definition.csv";
		final String nomFichierProvince = "provinces.bmp";
		FileInputStream fichierLecture = null;
		
		/**
		 * Base de données des provinces
		 */
		StockageProvince provinces = new StockageProvince();
		
		try {
			// Ouverture du fichier
			fichierLecture = new FileInputStream(nomFichierLecture);

			// Lecture en délimitant par des ;
			Scanner scanner = new Scanner(fichierLecture, "ISO-8859-1"); 
			// ISO-8859-1 pour caractères spéciaux
			scanner.useDelimiter(Pattern.compile("[;\n]"));
			
			// On cherche des entiers
			while (!scanner.hasNextInt()) {
				@SuppressWarnings("unused")
				String useless = scanner.next();
			}

			while (scanner.hasNextInt()) {
				// Lecture d'une province			
				int id = scanner.nextInt();
				if (!scanner.hasNextInt())
					break; // Fin de la lecture
				int r = scanner.nextInt();
				if (!scanner.hasNextInt())
					break; // Fin de la lecture
				int g = scanner.nextInt();	
				if (!scanner.hasNextInt())
					break; // Fin de la lecture
				int b = scanner.nextInt();
				String nom = scanner.next();
				while (!scanner.hasNextInt() && scanner.hasNext()) {
					@SuppressWarnings("unused")
					String useless = scanner.next();
				}
				
				// Stockage de la province
				provinces.addProvince(id, r, g, b, nom);
			}
			scanner.close();
			
			// Création de l'image à afficher
			Panneau pan = new Panneau(nomFichierProvince, text);
			
			// Création de la fenêtre qui fait tout le reste du boulot
			// (on boucle indéfiniment chez elle)
			if (v2) {
				@SuppressWarnings("unused")
				FenetreV2 windowV2 = new FenetreV2(provinces, pan, text, new MiniMap(nomFichierProvince, text, pan));
			} else {
				@SuppressWarnings("unused")
				Fenetre window = new Fenetre(provinces, pan);
			}			
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

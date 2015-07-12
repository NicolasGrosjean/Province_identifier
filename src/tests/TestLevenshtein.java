package tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import base.Province;
import base.ProvinceStorage;

public class TestLevenshtein extends TestCase {
	private final String nomFichierLecture = "definition.csv";
	private final String nomFichierProvince = "provinces.bmp";
	private FileInputStream fichierLecture = null;
	private ProvinceStorage provinces = new ProvinceStorage();

	private void SetUp() {
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

		} catch (FileNotFoundException e) {
			System.out.println("Fichier " + nomFichierLecture + " non trouvé !");
		} catch (IOException e) {
			System.out.println("Fichier " + nomFichierProvince + " non trouvé !");
		} finally {
			try {
				if (fichierLecture != null)
					fichierLecture.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void testNearestProvinces(){
		// Initialisation
		SetUp();
		
		LinkedList<Province> nearestProvinces = provinces.nearestProvinces("Troie", 3);
		for (Province p : nearestProvinces) {
			System.out.println(p);
		}
	}
}
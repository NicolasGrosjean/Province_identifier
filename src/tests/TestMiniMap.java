package tests;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import text.Text;
import text.TextFrancais;
import version2.FenetreV2;
import base.MiniMap;
import base.Panneau;
import base.StockageProvince;
import junit.framework.TestCase;

public class TestMiniMap extends TestCase {
	private final String nomFichierLecture = "definition.csv";
	private final String nomFichierProvince = "provinces.bmp";
	private final Text text = new TextFrancais();
	private FileInputStream fichierLecture = null;
	private StockageProvince provinces = new StockageProvince();
	private Panneau pan;
	
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
			
			// Création de l'image à afficher
			pan = new Panneau(nomFichierProvince, text);

			FenetreV2 windowV2 = new FenetreV2(provinces, pan, text, new MiniMap(nomFichierProvince, text, pan));		
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
	
	public void testMouse() throws IOException {
		// Initialisation
		SetUp();
		MiniMap miniMap = new MiniMap(nomFichierProvince, text, pan);
		FenetreV2 windowV2 = new FenetreV2(provinces, pan, text, miniMap);	
		
		// Clic de souris
		MouseEvent evt = new MouseEvent(miniMap, MouseEvent.MOUSE_CLICKED, 1, 0, 0, 0, 1, false);
		
		// Lecture du clic de souris
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Numero de largeur invalide", pan.getNumLargeur(), 0);
		assertEquals("Numero de hauteur invalide", pan.getNumHauteur(), 0);
		
		// Nouveau clic ...
		evt.translatePoint((int)((float)pan.getLargeurAfficheImageReelle() / pan.getLargeurReelle()
				 * miniMap.getLargeurImage()), 0);	
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Numero de largeur invalide", pan.getNumLargeur(), 1);
		assertEquals("Numero de hauteur invalide", pan.getNumHauteur(), 0);
		
		evt.translatePoint(0, (int)((float)pan.getHauteurAfficheImageReelle() / pan.getHauteurReelle()
				 * miniMap.getHauteurImage()));	
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Numero de largeur invalide", pan.getNumLargeur(), 1);
		assertEquals("Numero de hauteur invalide", pan.getNumHauteur(), 1);
		
		// Coin en haut à gauche
		evt = new MouseEvent(miniMap, MouseEvent.MOUSE_CLICKED, 1, 0, 0, 0, 1, false);
		evt.translatePoint(miniMap.getLargeurImage() - 1, 0);	
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Numero de largeur invalide", pan.getNumLargeur(),
				pan.getLargeurReelle() / (pan.getLargeurAfficheImageReelle()/2) - 2);
		assertEquals("Numero de hauteur invalide", pan.getNumHauteur(), 0);
		
		// Coin en bas à droite
		evt.translatePoint(0, miniMap.getHauteurImage() - 1);	
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Numero de largeur invalide", pan.getNumLargeur(),
				pan.getLargeurReelle() / (pan.getLargeurAfficheImageReelle()/2) - 2);
		assertEquals("Numero de hauteur invalide", pan.getNumHauteur(), 
				pan.getHauteurReelle() /(pan.getHauteurAfficheImageReelle()/2) - 2);
	}
}

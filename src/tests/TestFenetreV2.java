package tests;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

/**
 * Test unitaire de la classe Fenetre (test d'intégration du point de vue du projet)
 * @author Mouchi
 *
 */
public class TestFenetreV2 extends TestCase {
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

	public void testConstructeur() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		new FenetreV2(new StockageProvince(), pan , text, new MiniMap(nomFichierProvince, text, pan));
	}

	/****************************************************
	 *  WARNING : test dépendant de la map (SWMH 2.854) 
	 * @throws IOException *
	 ****************************************************/
	public void testMouse() throws IOException {
		// Initialisation
		SetUp();
		FenetreV2 window = new FenetreV2(provinces, pan, text, new MiniMap(nomFichierProvince, text, pan));

		// Clic de souris
		MouseEvent evt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, 25, 214, 1, false);

		// Lecture du clic de souris
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de province inattendu", window.getRes(), ""); // (25, 214)

		// Nouveau clic, ...
		evt.translatePoint(1, 0);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de province inattendu", window.getRes(), ""); // (26, 214)
		evt.translatePoint(-1, 1);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de province inattendu", window.getRes(), ""); // (25, 215)
		evt.translatePoint(1, 0);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de Tyrifjorden raté", window.getRes(), "1691 - Tyrifjorden"); // (26,215)

		// Clic de souris en haut à droite
		evt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, 1031, 78, 1, false);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de province inattendu", window.getRes(), ""); // (1031, 78)
		evt.translatePoint(1, 0);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de province inattendu", window.getRes(), ""); // (1032, 78)
		evt.translatePoint(0, 1);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de province inattendu", window.getRes(), ""); // (1032, 79)
		evt.translatePoint(-1, 0);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de North Sea raté", window.getRes(), "1696 - North Sea"); // (1031, 79)

		// Clic de souris en bas à droite
		evt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, 1032, 568, 1, false);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de province inattendu", window.getRes(), ""); // (1032, 568)
		evt.translatePoint(0, 1);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de province inattendu", window.getRes(), ""); // (1032, 569)
		evt.translatePoint(-1, 0);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de province inattendu", window.getRes(), ""); // (1031, 569)
		evt.translatePoint(0, -1);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		assertEquals("Identification de Gulf of Bothnia raté", window.getRes(), "1717 - Gulf of Bothnia"); // (1031, 568)
	}

	private void assertMiniMap(MiniMap miniMap, int numLargeur, int numHauteur) {
		assertEquals("MiniMap incorrecte", miniMap.getRectX() * (miniMap.getLargeurReelle()/miniMap.getLargeurImage()),
				numLargeur * pan.getLargeurAfficheImageReelle() / 2);
		assertEquals("MiniMap incorrecte", miniMap.getRectY() * (miniMap.getHauteurReelle()/miniMap.getHauteurImage()),
				numHauteur * pan.getHauteurAfficheImageReelle() / 2);
		assertEquals("MiniMap incorrecte", miniMap.getRectWidth() * (miniMap.getLargeurReelle()/miniMap.getLargeurImage()),
				pan.getLargeurAfficheImageReelle());
		assertEquals("MiniMap incorrecte", miniMap.getRectHeight() * (miniMap.getHauteurReelle()/miniMap.getHauteurImage()),
				pan.getHauteurAfficheImageReelle());
	}
	
	public void testIntegration() throws IOException {
		// Initialisation
		SetUp();
		MiniMap miniMap = new MiniMap(nomFichierProvince, text, pan);
		FenetreV2 window = new FenetreV2(provinces, pan, text, miniMap);

		// Déplacement tout à droite
		int i = 1;
		if (pan.getLargeurImage() < pan.getLargeurReelle()) {
			KeyEvent evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 39, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), i);
			assertMiniMap(miniMap, i, 0);
			i++;
		}
		while (pan.getNumLargeur() * pan.getLargeurAfficheImageReelle() / 2  + pan.getLargeurAfficheImageReelle()
				< pan.getLargeurReelle()) {
			KeyEvent evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 39, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), i);
			assertMiniMap(miniMap, i, 0);
			i++;
		}
		KeyEvent evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 39, KeyEvent.CHAR_UNDEFINED);
		for(KeyListener kl: window.getKeyListeners()){
			kl.keyPressed(evt);
		}
		assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), --i);
		assertMiniMap(miniMap, i, 0);

		// Trois zoom plus
		for (int k = 0; k < 3; k++) {
			int largeurImageReellePrecedente = pan.getLargeurAfficheImageReelle();
			int hauteurImageReellePrecedente = pan.getHauteurAfficheImageReelle();
			int numLargeurPrecedent = pan.getNumLargeur();
			int numHauteurPrecedent = pan.getNumHauteur();
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 107, '+');
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			assertEquals("Largeur incorrecte", pan.getLargeurAfficheImageReelle(), largeurImageReellePrecedente / 2);
			assertEquals("Hauteur incorrecte", pan.getHauteurAfficheImageReelle(), hauteurImageReellePrecedente / 2);
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), numLargeurPrecedent * 2 + 1);
			assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), numHauteurPrecedent * 2 + 1);
			assertMiniMap(miniMap, pan.getNumLargeur(), pan.getNumHauteur());
		}

		// On clique en (0, HauteurImage)
		MouseEvent mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, 0, pan.getHauteurImage(), 1, false);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		assertEquals("Identification de North Atlantic raté", window.getRes(), "1694 - North Atlantic");

		// On dézoom trois fois
		for (int k = 0; k < 3; k++) {
			int largeurImageReellePrecedente = pan.getLargeurAfficheImageReelle();
			int hauteurImageReellePrecedente = pan.getHauteurAfficheImageReelle();
			int numLargeurPrecedent = pan.getNumLargeur();
			int numHauteurPrecedent = pan.getNumHauteur();
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 109, '-');
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			assertEquals("Largeur incorrecte", pan.getLargeurAfficheImageReelle(), largeurImageReellePrecedente * 2);
			assertEquals("Hauteur incorrecte", pan.getHauteurAfficheImageReelle(), hauteurImageReellePrecedente * 2);
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), (numLargeurPrecedent - 1) / 2);
			assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), (numHauteurPrecedent - 1) / 2);
			assertMiniMap(miniMap, pan.getNumLargeur(), pan.getNumHauteur());
		}

		// Déplacement tout à gauche
		while (pan.getNumLargeur() > 0) {
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 37, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			i--;
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), i);
			assertMiniMap(miniMap, i, 0);
		}
		evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 37, KeyEvent.CHAR_UNDEFINED);
		for(KeyListener kl: window.getKeyListeners()){
			kl.keyPressed(evt);
		}
		assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), i);
		assertMiniMap(miniMap, i, 0);

		// On clique en (LargeurMax, HauteurMax)
		mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, window.premierPixelX() + pan.getLargeurImage() - 1,
				window.premierPixelY() + pan.getHauteurImage() - 1, 1, false);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		assertEquals("Identification de Gulf of Bothnia raté", window.getRes(), "1717 - Gulf of Bothnia");

		// Déplacement tout en bas
		if (pan.getHauteurImage() < pan.getHauteurReelle()) {
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 40, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			i++;
			assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), i);
			assertMiniMap(miniMap, 0, i);
		}
		while (pan.getNumHauteur() * pan.getHauteurAfficheImageReelle() / 2  + pan.getHauteurAfficheImageReelle()
				< pan.getHauteurReelle()) {
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 40, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			i++;
			assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), i);
			assertMiniMap(miniMap, 0, i);
		}
		evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 40, KeyEvent.CHAR_UNDEFINED);
		for(KeyListener kl: window.getKeyListeners()){
			kl.keyPressed(evt);
		}
		assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), i);
		assertMiniMap(miniMap, 0, i);
		
		// On clique en (LargeurMax, HauteurMax)
		mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, window.premierPixelX() + pan.getLargeurImage() - 1,
				window.premierPixelY() + pan.getHauteurImage() - 1, 1, false);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		assertEquals("Identification de Gulf of Bothnia raté", window.getRes(), "1487 - Central Africa");
		
		// Déplacement tout en haut
		while (pan.getNumHauteur() > 0) {
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 38, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			i--;
			assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), i);
			assertMiniMap(miniMap, 0, i);
		}
		evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 38, KeyEvent.CHAR_UNDEFINED);
		for(KeyListener kl: window.getKeyListeners()){
			kl.keyPressed(evt);
		}
		assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), i);
		assertMiniMap(miniMap, 0, i);

		// On clique en (LargeurMax, HauteurMax)
		mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, window.premierPixelX() + pan.getLargeurImage() - 1,
				window.premierPixelY() + pan.getHauteurImage() - 1, 1, false);
		for(MouseListener ml: window.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		assertEquals("Identification de Gulf of Bothnia raté", window.getRes(), "1717 - Gulf of Bothnia");
	}
}

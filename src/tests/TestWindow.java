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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import config.ConfigStorage;
import text.Text;
import text.TextFrancais;
import base.Window;
import base.MiniMap;
import base.Panel;
import base.ProvinceStorage;

/**
 * Unit tests for Window (test d'intégration du point de vue du projet)
 * @author Mouchi
 *
 */
public class TestWindow {
	private static final String nomFichierLecture = "definition.csv";
	private static final String nomFichierProvince = "provinces.bmp";
	private static final Text text = new TextFrancais();
	private static FileInputStream fichierLecture = null;
	private static ProvinceStorage provinces = new ProvinceStorage();
	private static Panel pan;

	@BeforeClass
	public static void SetUp() {
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
			pan = new Panel(nomFichierProvince, text);
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

	@Test
	public void testConstructeur() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		new Window(new ProvinceStorage(), pan , text,
				new MiniMap(nomFichierProvince, text, pan),
				new ConfigStorage("config_test.xml")).dispose();
	}

	/****************************************************
	 *  WARNING : test dépendant de la map (SWMH 2.854) 
	 * @throws IOException *
	 ****************************************************/
	@Test
	public void testMouse() throws IOException {
		// Initialisation
		Window window = new Window(provinces, pan, text,
				new MiniMap(nomFichierProvince, text, pan), new ConfigStorage("config.xml"));

		// Clic de souris (position par rapport au Panel)
		MouseEvent evt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, 17, 157, 1, false);

		// Lecture du clic de souris
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (17, 157)

		// Nouveau clic, ...
		evt.translatePoint(1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (18, 157)
		evt.translatePoint(-1, 1);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (17, 158)
		evt.translatePoint(1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de Tyrifjorden raté", window.getRes(), "1691 - Tyrifjorden"); // (18,158)

		// Clic de souris en haut à droite
		evt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, 1023, 21, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1023, 21)
		evt.translatePoint(1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1024, 21)
		evt.translatePoint(0, 1);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1024, 22)
		evt.translatePoint(-1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de North Sea raté", window.getRes(), "1696 - North Sea"); // (1023, 22)

		// Clic de souris en bas à droite
		evt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, 1024, 511, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1024, 511)
		evt.translatePoint(0, 1);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1024, 512)
		evt.translatePoint(-1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1023, 512)
		evt.translatePoint(0, -1);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de Gulf of Bothnia raté", window.getRes(), "1717 - Gulf of Bothnia"); // (1023, 511)

		// Close the window
		window.dispose();
	}

	private void assertMiniMap(MiniMap miniMap, int numLargeur, int numHauteur) {
		Assert.assertEquals("MiniMap incorrecte", miniMap.getRectX() * (miniMap.getRealWidth()/miniMap.getImageWidth()),
				numLargeur * pan.getDisplayingRealImageWidth() / 2);
		Assert.assertEquals("MiniMap incorrecte", miniMap.getRectY() * (miniMap.getRealHeight()/miniMap.getImageHeight()),
				numHauteur * pan.getDisplayingRealImageHeight() / 2);
		Assert.assertEquals("MiniMap incorrecte", miniMap.getRectWidth() * (miniMap.getRealWidth()/miniMap.getImageWidth()),
				pan.getDisplayingRealImageWidth());
		Assert.assertEquals("MiniMap incorrecte", miniMap.getRectHeight() * (miniMap.getRealHeight()/miniMap.getImageHeight()),
				pan.getDisplayingRealImageHeight());
	}
	
	@Test
	public void testIntegration() throws IOException {
		// Initialisation
		MiniMap miniMap = new MiniMap(nomFichierProvince, text, pan);
		Window window = new Window(provinces, pan, text, miniMap, new ConfigStorage("config.xml"));

		// Déplacement tout à droite
		int i = 1;
		if (pan.getImageWidth() < pan.getRealWidth()) {
			KeyEvent evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 39, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
			assertMiniMap(miniMap, i, 0);
			i++;
		}
		while (pan.getWidthNumber() * pan.getDisplayingRealImageWidth() / 2  + pan.getDisplayingRealImageWidth()
				< pan.getRealWidth()) {
			KeyEvent evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 39, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
			assertMiniMap(miniMap, i, 0);
			i++;
		}
		KeyEvent evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 39, KeyEvent.CHAR_UNDEFINED);
		for(KeyListener kl: window.getKeyListeners()){
			kl.keyPressed(evt);
		}
		Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), --i);
		assertMiniMap(miniMap, i, 0);

		// Trois zoom plus
		for (int k = 0; k < 3; k++) {
			int largeurImageReellePrecedente = pan.getDisplayingRealImageWidth();
			int hauteurImageReellePrecedente = pan.getDisplayingRealImageHeight();
			int numLargeurPrecedent = pan.getWidthNumber();
			int numHauteurPrecedent = pan.getHeightNumber();
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 107, '+');
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			Assert.assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), largeurImageReellePrecedente / 2);
			Assert.assertEquals("Hauteur incorrecte", pan.getDisplayingRealImageHeight(), hauteurImageReellePrecedente / 2);
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), numLargeurPrecedent * 2 + 1);
			Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), numHauteurPrecedent * 2 + 1);
			assertMiniMap(miniMap, pan.getWidthNumber(), pan.getHeightNumber());
		}

		// On clique en (0, HauteurImage)
		MouseEvent mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, 0-8, pan.getImageHeight()-57, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		Assert.assertEquals("Identification de North Atlantic raté", window.getRes(), "1694 - North Atlantic");

		// On dézoom trois fois
		for (int k = 0; k < 3; k++) {
			int largeurImageReellePrecedente = pan.getDisplayingRealImageWidth();
			int hauteurImageReellePrecedente = pan.getDisplayingRealImageHeight();
			int numLargeurPrecedent = pan.getWidthNumber();
			int numHauteurPrecedent = pan.getHeightNumber();
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 109, '-');
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			Assert.assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), largeurImageReellePrecedente * 2);
			Assert.assertEquals("Hauteur incorrecte", pan.getDisplayingRealImageHeight(), hauteurImageReellePrecedente * 2);
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), (numLargeurPrecedent - 1) / 2);
			Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), (numHauteurPrecedent - 1) / 2);
			assertMiniMap(miniMap, pan.getWidthNumber(), pan.getHeightNumber());
		}

		// Déplacement tout à gauche
		while (pan.getWidthNumber() > 0) {
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 37, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			i--;
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
			assertMiniMap(miniMap, i, 0);
		}
		evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 37, KeyEvent.CHAR_UNDEFINED);
		for(KeyListener kl: window.getKeyListeners()){
			kl.keyPressed(evt);
		}
		Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
		assertMiniMap(miniMap, i, 0);

		// On clique en (LargeurMax, HauteurMax)
		mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, pan.getImageWidth() - 1,
				pan.getImageHeight() - 1, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		Assert.assertEquals("Identification de Gulf of Bothnia raté", window.getRes(), "1717 - Gulf of Bothnia");

		// Déplacement tout en bas
		if (pan.getImageHeight() < pan.getRealHeight()) {
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 40, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			i++;
			Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), i);
			assertMiniMap(miniMap, 0, i);
		}
		while (pan.getHeightNumber() * pan.getDisplayingRealImageHeight() / 2  + pan.getDisplayingRealImageHeight()
				< pan.getRealHeight()) {
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 40, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			i++;
			Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), i);
			assertMiniMap(miniMap, 0, i);
		}
		evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 40, KeyEvent.CHAR_UNDEFINED);
		for(KeyListener kl: window.getKeyListeners()){
			kl.keyPressed(evt);
		}
		Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), i);
		assertMiniMap(miniMap, 0, i);
		
		// On clique en (LargeurMax, HauteurMax)
		mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, pan.getImageWidth() - 1,
				pan.getImageHeight() - 1, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		Assert.assertEquals("Identification de Gulf of Bothnia raté", window.getRes(), "1487 - Central Africa");
		
		// Déplacement tout en haut
		while (pan.getHeightNumber() > 0) {
			evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 38, KeyEvent.CHAR_UNDEFINED);
			for(KeyListener kl: window.getKeyListeners()){
				kl.keyPressed(evt);
			}
			i--;
			Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), i);
			assertMiniMap(miniMap, 0, i);
		}
		evt = new KeyEvent(window, KeyEvent.KEY_PRESSED, 1, 0, 38, KeyEvent.CHAR_UNDEFINED);
		for(KeyListener kl: window.getKeyListeners()){
			kl.keyPressed(evt);
		}
		Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), i);
		assertMiniMap(miniMap, 0, i);

		// On clique en (LargeurMax, HauteurMax)
		mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, pan.getImageWidth() - 1,
				pan.getImageHeight() - 1, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		Assert.assertEquals("Identification de Gulf of Bothnia raté", window.getRes(), "1717 - Gulf of Bothnia");

		// Close the window
		window.dispose();
	}
}

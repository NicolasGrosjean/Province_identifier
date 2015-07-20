package tests;

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

import text.Text;
import text.TextFrancais;
import base.Window;
import base.MiniMap;
import base.Panel;
import base.ProvinceStorage;

public class TestMiniMap {
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

	@Test
	public void testMouse() throws IOException {
		MiniMap miniMap = new MiniMap(nomFichierProvince, text, pan);
		Window windowV2 = new Window(provinces, pan, text, miniMap, null);	
		
		// Clic de souris
		MouseEvent evt = new MouseEvent(miniMap, MouseEvent.MOUSE_CLICKED, 1, 0, 0, 0, 1, false);
		
		// Lecture du clic de souris
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Numero de largeur invalide", pan.getWidthNumber(), 0);
		Assert.assertEquals("Numero de hauteur invalide", pan.getHeightNumber(), 0);
		
		// Nouveau clic ...
		evt.translatePoint((int)((float)pan.getDisplayingRealImageWidth() / pan.getRealWidth()
				 * miniMap.getImageWidth()), 0);	
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Numero de largeur invalide", pan.getWidthNumber(), 1);
		Assert.assertEquals("Numero de hauteur invalide", pan.getHeightNumber(), 0);
		
		evt.translatePoint(0, (int)((float)pan.getDisplayingRealImageHeight() / pan.getRealHeight()
				 * miniMap.getImageHeight()));	
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Numero de largeur invalide", pan.getWidthNumber(), 1);
		Assert.assertEquals("Numero de hauteur invalide", pan.getHeightNumber(), 1);
		
		// Coin en haut à gauche
		evt = new MouseEvent(miniMap, MouseEvent.MOUSE_CLICKED, 1, 0, 0, 0, 1, false);
		evt.translatePoint(miniMap.getImageWidth() - 1, 0);	
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Numero de largeur invalide", pan.getWidthNumber(),
				pan.getRealWidth() / (pan.getDisplayingRealImageWidth()/2) - 2);
		Assert.assertEquals("Numero de hauteur invalide", pan.getHeightNumber(), 0);
		
		// Coin en bas à droite
		evt.translatePoint(0, miniMap.getImageHeight() - 1);	
		for(MouseListener ml: miniMap.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Numero de largeur invalide", pan.getWidthNumber(),
				pan.getRealWidth() / (pan.getDisplayingRealImageWidth()/2) - 2);
		Assert.assertEquals("Numero de hauteur invalide", pan.getHeightNumber(), 
				pan.getRealHeight() /(pan.getDisplayingRealImageHeight()/2) - 2);

		// Close the window
		windowV2.dispose();
		windowV2 = null;
	}
}

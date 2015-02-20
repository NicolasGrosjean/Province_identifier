package tests;

import java.io.IOException;

import text.Text;
import text.TextFrancais;
import base.Panel;
import junit.framework.TestCase;

/**
 * Test unitaire de classe Panneau
 * @author Mouchi
 *
 */
public class TestPanneau extends TestCase {
	private final String nomFichierProvince = "provinces.bmp";
	private final Text text = new TextFrancais();
	
	public void testConstructeur() {		
		final String nomFichierInexistant = "fichier inexistant";
		boolean ouvertureFichierOK = true;
		
		// Tente de créer le panneau sur un test inexistant
		try {
			new Panel(nomFichierInexistant, text);
			ouvertureFichierOK = false;
		} catch (IOException e) {
			System.out.println("OK : Fichier " + nomFichierInexistant + " non trouvé !");
		}
		
		// Tente de créer le panneau sur le bon fichier
		try {
			new Panel(nomFichierProvince, text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("L'ouverture d'un fichier inexistant n'a pas fait planter",
				ouvertureFichierOK, true);
	}
	
	public void testGetLargeurImage() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		assertEquals("Largeur incorrecte", pan.getImageWidth(), Panel.IMAGE_WIDTH);
	}
	
	public void testGetHauteurImage() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		assertEquals("Heuteur incorrecte", pan.getImageHeight(), Panel.IMAGE_HEIGHT);
	}
	
	public void testGetLargeurReduite() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), Panel.IMAGE_WIDTH);
	}
	
	public void testGetHauteurReduite() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		assertEquals("Heuteur incorrecte", pan.getDisplayingRealImageHeight(), Panel.IMAGE_HEIGHT);
	}	
	
	public void testGetNumLargeur() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), 0);
	}
	
	public void testGetNumHauteur() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), 0);
	}
	
	public void testNumLargeurPlus() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		int i = 1;
		if (pan.getImageWidth() < pan.getRealWidth()) {
			pan.widthNumberMore();
			assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
			i++;
		}
		while (pan.getWidthNumber() * pan.getDisplayingRealImageWidth() / 2  + pan.getDisplayingRealImageWidth()
				< pan.getRealWidth()) {
			pan.widthNumberMore();
			assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
			i++;
		}
		try {
			pan.widthNumberMore();
			assertEquals("Débordement en largeur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}
	
	public void testNumHauteurPlus() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		int i = 1;
		if (pan.getImageHeight() < pan.getRealHeight()) {
			pan.heightNumberMore();
			assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), i);
			i++;
		}
		while (pan.getHeightNumber() * pan.getDisplayingRealImageHeight() / 2  + pan.getDisplayingRealImageHeight()
				< pan.getRealHeight()) {
			pan.heightNumberMore();
			assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), i);
			i++;
		}
		try {
			pan.heightNumberMore();
			assertEquals("Débordement en hauteur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}
	
	public void testNumLargeurMoins() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		int i = 0;
		while (pan.getWidthNumber() * pan.getDisplayingRealImageWidth() / 2  + pan.getDisplayingRealImageWidth()
				< pan.getRealWidth()) {
			pan.widthNumberMore();
			i++;
		}	
		while (pan.getWidthNumber() > 0) {
			pan.widthNumberLeast();
			i--;
			assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
		}
		try {
			pan.widthNumberLeast();
			assertEquals("Débordement en largeur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}
	
	public void testNumHauteurMoins() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		int i = 0;
		while (pan.getHeightNumber() * pan.getDisplayingRealImageHeight() / 2  + pan.getDisplayingRealImageHeight()
				< pan.getRealHeight()) {
			pan.heightNumberMore();
			i++;
		}	
		while (pan.getHeightNumber() > 0) {
			pan.heightNumberLeast();
			i--;
			assertEquals("Numéro de Hauteur incorrect", pan.getHeightNumber(), i);
		}
		try {
			pan.heightNumberLeast();
			assertEquals("Débordement en hauteur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}
	
	public void testZoomPlus() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		for (int i = 0; i < 3; i++) {
			int largeurImageReellePrecedente = pan.getDisplayingRealImageWidth();
			int hauteurImageReellePrecedente = pan.getDisplayingRealImageHeight();
			int numLargeurPrecedent = pan.getWidthNumber();
			int numHauteurPrecedent = pan.getHeightNumber();
			pan.zoomMore();
			assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), largeurImageReellePrecedente / 2);
			assertEquals("Hauteur incorrecte", pan.getDisplayingRealImageHeight(), hauteurImageReellePrecedente / 2);
			assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), numLargeurPrecedent * 2 + 1);
			assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), numHauteurPrecedent * 2 + 1);
		}
	}
	
	public void testZoomMoins() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text);
		// On zoome pour être sûr de pouvoir dézoomer après
		for (int i = 0; i < 3; i++) {
			pan.zoomMore();
		}
		while (pan.getDisplayingRealImageWidth() < pan.getRealWidth()
				&& pan.getDisplayingRealImageHeight() < pan.getRealHeight()) {
			int largeurImageReellePrecedente = pan.getDisplayingRealImageWidth();
			int hauteurImageReellePrecedente = pan.getDisplayingRealImageHeight();
			int numLargeurPrecedent = pan.getWidthNumber();
			int numHauteurPrecedent = pan.getHeightNumber();
			pan.zoomLeast();
			assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), largeurImageReellePrecedente * 2);
			assertEquals("Hauteur incorrecte", pan.getDisplayingRealImageHeight(), hauteurImageReellePrecedente * 2);
			assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), (numLargeurPrecedent - 1) / 2);
			assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), (numHauteurPrecedent - 1) / 2);
		}
	}
}

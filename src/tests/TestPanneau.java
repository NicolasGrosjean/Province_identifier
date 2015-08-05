package tests;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import text.Text;
import text.TextFrancais;
import base.Panel;

/**
 * Test unitaire de classe Panneau
 * @author Mouchi
 *
 */
public class TestPanneau {
	private final String nomFichierProvince = "provinces.bmp";
	private final Text text = new TextFrancais();

	@Test
	public void testConstructeur() {		
		final String nomFichierInexistant = "fichier inexistant";
		boolean ouvertureFichierOK = true;
		
		// Tente de créer le panneau sur un test inexistant
		try {
			new Panel(nomFichierInexistant, text, false);
			ouvertureFichierOK = false;
		} catch (IOException e) {
			System.out.println("OK : Fichier " + nomFichierInexistant + " non trouvé !");
		}
		
		// Tente de créer le panneau sur le bon fichier
		try {
			new Panel(nomFichierProvince, text, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertEquals("L'ouverture d'un fichier inexistant n'a pas fait planter",
				ouvertureFichierOK, true);
	}

	@Test
	public void testGetLargeurImage() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		Assert.assertEquals("Largeur incorrecte", pan.getImageWidth(), Panel.IMAGE_WIDTH);
	}

	@Test
	public void testGetHauteurImage() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		Assert.assertEquals("Heuteur incorrecte", pan.getImageHeight(), Panel.IMAGE_HEIGHT);
	}

	@Test
	public void testGetLargeurReduite() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		Assert.assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), Panel.IMAGE_WIDTH);
	}

	@Test
	public void testGetHauteurReduite() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		Assert.assertEquals("Heuteur incorrecte", pan.getDisplayingRealImageHeight(), Panel.IMAGE_HEIGHT);
	}	

	@Test
	public void testGetNumLargeur() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), 0);
	}

	@Test
	public void testGetNumHauteur() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), 0);
	}

	@Test
	public void testNumLargeurPlus() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		int i = 1;
		if (pan.getImageWidth() < pan.getRealWidth()) {
			pan.widthNumberMore();
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
			i++;
		}
		while (pan.getWidthNumber() * pan.getDisplayingRealImageWidth() / 2  + pan.getDisplayingRealImageWidth()
				< pan.getRealWidth()) {
			pan.widthNumberMore();
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
			i++;
		}
		try {
			pan.widthNumberMore();
			Assert.assertEquals("Débordement en largeur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}

	@Test
	public void testNumHauteurPlus() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		int i = 1;
		if (pan.getImageHeight() < pan.getRealHeight()) {
			pan.heightNumberMore();
			Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), i);
			i++;
		}
		while (pan.getHeightNumber() * pan.getDisplayingRealImageHeight() / 2  + pan.getDisplayingRealImageHeight()
				< pan.getRealHeight()) {
			pan.heightNumberMore();
			Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), i);
			i++;
		}
		try {
			pan.heightNumberMore();
			Assert.assertEquals("Débordement en hauteur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}

	@Test
	public void testNumLargeurMoins() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		int i = 0;
		while (pan.getWidthNumber() * pan.getDisplayingRealImageWidth() / 2  + pan.getDisplayingRealImageWidth()
				< pan.getRealWidth()) {
			pan.widthNumberMore();
			i++;
		}	
		while (pan.getWidthNumber() > 0) {
			pan.widthNumberLeast();
			i--;
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), i);
		}
		try {
			pan.widthNumberLeast();
			Assert.assertEquals("Débordement en largeur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}

	@Test
	public void testNumHauteurMoins() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		int i = 0;
		while (pan.getHeightNumber() * pan.getDisplayingRealImageHeight() / 2  + pan.getDisplayingRealImageHeight()
				< pan.getRealHeight()) {
			pan.heightNumberMore();
			i++;
		}	
		while (pan.getHeightNumber() > 0) {
			pan.heightNumberLeast();
			i--;
			Assert.assertEquals("Numéro de Hauteur incorrect", pan.getHeightNumber(), i);
		}
		try {
			pan.heightNumberLeast();
			Assert.assertEquals("Débordement en hauteur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}

	@Test
	public void testZoomPlus() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
		for (int i = 0; i < 3; i++) {
			int largeurImageReellePrecedente = pan.getDisplayingRealImageWidth();
			int hauteurImageReellePrecedente = pan.getDisplayingRealImageHeight();
			int numLargeurPrecedent = pan.getWidthNumber();
			int numHauteurPrecedent = pan.getHeightNumber();
			pan.zoomMore();
			Assert.assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), largeurImageReellePrecedente / 2);
			Assert.assertEquals("Hauteur incorrecte", pan.getDisplayingRealImageHeight(), hauteurImageReellePrecedente / 2);
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), numLargeurPrecedent * 2 + 1);
			Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), numHauteurPrecedent * 2 + 1);
		}
	}

	@Test
	public void testZoomMoins() throws IOException {
		Panel pan = new Panel(nomFichierProvince, text, false);
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
			Assert.assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), largeurImageReellePrecedente * 2);
			Assert.assertEquals("Hauteur incorrecte", pan.getDisplayingRealImageHeight(), hauteurImageReellePrecedente * 2);
			Assert.assertEquals("Numéro de largeur incorrect", pan.getWidthNumber(), (numLargeurPrecedent - 1) / 2);
			Assert.assertEquals("Numéro de hauteur incorrect", pan.getHeightNumber(), (numHauteurPrecedent - 1) / 2);
		}
	}
}

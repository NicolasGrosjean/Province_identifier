package tests;

import java.io.IOException;

import text.Text;
import text.TextFrancais;
import base.Panneau;
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
			new Panneau(nomFichierInexistant, text);
			ouvertureFichierOK = false;
		} catch (IOException e) {
			System.out.println("OK : Fichier " + nomFichierInexistant + " non trouvé !");
		}
		
		// Tente de créer le panneau sur le bon fichier
		try {
			new Panneau(nomFichierProvince, text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("L'ouverture d'un fichier inexistant n'a pas fait planter",
				ouvertureFichierOK, true);
	}
	
	public void testGetLargeurImage() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		assertEquals("Largeur incorrecte", pan.getLargeurImage(), Panneau.LARGEUR_IMAGE);
	}
	
	public void testGetHauteurImage() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		assertEquals("Heuteur incorrecte", pan.getHauteurImage(), Panneau.HAUTEUR_IMAGE);
	}
	
	public void testGetLargeurReduite() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		assertEquals("Largeur incorrecte", pan.getLargeurAfficheImageReelle(), Panneau.LARGEUR_IMAGE);
	}
	
	public void testGetHauteurReduite() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		assertEquals("Heuteur incorrecte", pan.getHauteurAfficheImageReelle(), Panneau.HAUTEUR_IMAGE);
	}	
	
	public void testGetNumLargeur() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), 0);
	}
	
	public void testGetNumHauteur() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), 0);
	}
	
	public void testNumLargeurPlus() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		int i = 1;
		if (pan.getLargeurImage() < pan.getLargeurReelle()) {
			pan.numLargeurPlus();
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), i);
			i++;
		}
		while (pan.getNumLargeur() * pan.getLargeurAfficheImageReelle() / 2  + pan.getLargeurAfficheImageReelle()
				< pan.getLargeurReelle()) {
			pan.numLargeurPlus();
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), i);
			i++;
		}
		try {
			pan.numLargeurPlus();
			assertEquals("Débordement en largeur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}
	
	public void testNumHauteurPlus() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		int i = 1;
		if (pan.getHauteurImage() < pan.getHauteurReelle()) {
			pan.numHauteurPlus();
			assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), i);
			i++;
		}
		while (pan.getNumHauteur() * pan.getHauteurAfficheImageReelle() / 2  + pan.getHauteurAfficheImageReelle()
				< pan.getHauteurReelle()) {
			pan.numHauteurPlus();
			assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), i);
			i++;
		}
		try {
			pan.numHauteurPlus();
			assertEquals("Débordement en hauteur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}
	
	public void testNumLargeurMoins() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		int i = 0;
		while (pan.getNumLargeur() * pan.getLargeurAfficheImageReelle() / 2  + pan.getLargeurAfficheImageReelle()
				< pan.getLargeurReelle()) {
			pan.numLargeurPlus();
			i++;
		}	
		while (pan.getNumLargeur() > 0) {
			pan.numLargeurMoins();
			i--;
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), i);
		}
		try {
			pan.numLargeurMoins();
			assertEquals("Débordement en largeur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}
	
	public void testNumHauteurMoins() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		int i = 0;
		while (pan.getNumHauteur() * pan.getHauteurAfficheImageReelle() / 2  + pan.getHauteurAfficheImageReelle()
				< pan.getHauteurReelle()) {
			pan.numHauteurPlus();
			i++;
		}	
		while (pan.getNumHauteur() > 0) {
			pan.numHauteurMoins();
			i--;
			assertEquals("Numéro de Hauteur incorrect", pan.getNumHauteur(), i);
		}
		try {
			pan.numHauteurMoins();
			assertEquals("Débordement en hauteur espéré", false, true);
		} catch (IllegalArgumentException e) {			
		}
	}
	
	public void testZoomPlus() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		for (int i = 0; i < 3; i++) {
			int largeurImageReellePrecedente = pan.getLargeurAfficheImageReelle();
			int hauteurImageReellePrecedente = pan.getHauteurAfficheImageReelle();
			int numLargeurPrecedent = pan.getNumLargeur();
			int numHauteurPrecedent = pan.getNumHauteur();
			pan.zoomPlus();
			assertEquals("Largeur incorrecte", pan.getLargeurAfficheImageReelle(), largeurImageReellePrecedente / 2);
			assertEquals("Hauteur incorrecte", pan.getHauteurAfficheImageReelle(), hauteurImageReellePrecedente / 2);
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), numLargeurPrecedent * 2 + 1);
			assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), numHauteurPrecedent * 2 + 1);
		}
	}
	
	public void testZoomMoins() throws IOException {
		Panneau pan = new Panneau(nomFichierProvince, text);
		// On zoome pour être sûr de pouvoir dézoomer après
		for (int i = 0; i < 3; i++) {
			pan.zoomPlus();
		}
		while (pan.getLargeurAfficheImageReelle() < pan.getLargeurReelle()
				&& pan.getHauteurAfficheImageReelle() < pan.getHauteurReelle()) {
			int largeurImageReellePrecedente = pan.getLargeurAfficheImageReelle();
			int hauteurImageReellePrecedente = pan.getHauteurAfficheImageReelle();
			int numLargeurPrecedent = pan.getNumLargeur();
			int numHauteurPrecedent = pan.getNumHauteur();
			pan.zoomMoins();
			assertEquals("Largeur incorrecte", pan.getLargeurAfficheImageReelle(), largeurImageReellePrecedente * 2);
			assertEquals("Hauteur incorrecte", pan.getHauteurAfficheImageReelle(), hauteurImageReellePrecedente * 2);
			assertEquals("Numéro de largeur incorrect", pan.getNumLargeur(), (numLargeurPrecedent - 1) / 2);
			assertEquals("Numéro de hauteur incorrect", pan.getNumHauteur(), (numHauteurPrecedent - 1) / 2);
		}
	}
}

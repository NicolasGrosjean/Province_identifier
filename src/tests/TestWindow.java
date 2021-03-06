package tests;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import base.MiniMap;
import base.Panel;
import base.Window;
import config.ConfigStorage;
import text.Text;
import text.TextFrancais;

/**
 * Unit tests for Window (test d'integration du point de vue du projet)
 * @author Mouchi
 *
 */
public class TestWindow {
	private static final Text text = new TextFrancais();
	private static final String configFile = "config_test.xml";
	private static ConfigStorage configuration;
	private static Panel pan;

	@BeforeClass
	public static void SetUp() throws IOException {
		configuration = new ConfigStorage(configFile);
		configuration.getFirst().initialize();
		pan = configuration.getFirst().getPanel();
	}

	@Test
	public void testConstructeur() throws IOException {
		new Window(configuration.getFirst(), text, configuration);
	}

	/****************************************************
	 *  WARNING : test dependant de la map (SWMH 2.854) 
	 * @throws IOException *
	 ****************************************************/
	@Test
	public void testMouse() throws IOException {
		// Initialisation
		Window window = new Window(configuration.getFirst(), text, configuration);

		// Clic de souris (position par rapport au Panel)
		MouseEvent evt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 1, 0, 18, 140, 1, false);

		// Lecture du clic de souris
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (18, 140)

		// Nouveau clic, ...
		evt.translatePoint(1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (19, 140)
		evt.translatePoint(-1, 1);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (18, 141)
		evt.translatePoint(1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de Tyrifjorden rate", window.getRes(), "1755 - Tyrifjorden"); // (19, 141)

		// Test the right limit which is 1280 = 1024 + 256
		// Check the width of the panel
		Assert.assertEquals("Bad width", pan.getImageWidth(), 1280);
		evt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 2, 0, 1279, 108, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de North Atlantic rate", window.getRes(), "1758 - North Atlantic"); // (1279, 108)
		evt.translatePoint(1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		// It is not displayed by the software, so we found no province
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1280, 108)
		evt.translatePoint(0, 1);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1280, 109)
		evt.translatePoint(-1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de North Atlantic rate", window.getRes(), "1758 - North Atlantic"); // (1279, 109)

		// Test the bottom limit which is 512
		// Check the width of the panel
		Assert.assertEquals("Bad width", pan.getImageHeight(), 512);
		evt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 3, 0, 1280, 511, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		// No province because right limit is attained
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1280, 511)
		evt.translatePoint(0, 1);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		// No province because right (and bottom) limit is attained
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1280, 512)
		evt.translatePoint(-1, 0);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		// No province because bottom limit is attained
		Assert.assertEquals("Identification de province inattendu", window.getRes(), ""); // (1279, 512)
		evt.translatePoint(0, -1);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(evt);
		}
		Assert.assertEquals("Identification de Kola rate", window.getRes(), "387 - Kola"); // (1279, 511)

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
		Window window = new Window(configuration.getFirst(), text, configuration);
		MiniMap miniMap = configuration.getFirst().getMiniMap();

		// Deplacement tout à droite
		int i = 1;
		if (pan.getImageWidth() < pan.getRealWidth()) {
			ActionEvent actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
			for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(3).getActionListeners()) {
				a.actionPerformed(actionEvt);
			}
			Assert.assertEquals("Numero de largeur incorrect", pan.getWidthNumber(), i);
			assertMiniMap(miniMap, i, 0);
			i++;
		}
		while (pan.getWidthNumber() * pan.getDisplayingRealImageWidth() / 2  + pan.getDisplayingRealImageWidth()
				< pan.getRealWidth()) {
			ActionEvent actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
			for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(3).getActionListeners()) {
				a.actionPerformed(actionEvt);
			}
			Assert.assertEquals("Numero de largeur incorrect", pan.getWidthNumber(), i);
			assertMiniMap(miniMap, i, 0);
			i++;
		}
		ActionEvent actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
		for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(3).getActionListeners()) {
			a.actionPerformed(actionEvt);
		}
		Assert.assertEquals("Numero de largeur incorrect", pan.getWidthNumber(), --i);
		assertMiniMap(miniMap, i, 0);

		// Trois zoom plus
		for (int k = 0; k < 3; k++) {
			int largeurImageReellePrecedente = pan.getDisplayingRealImageWidth();
			int hauteurImageReellePrecedente = pan.getDisplayingRealImageHeight();
			int numLargeurPrecedent = pan.getWidthNumber();
			int numHauteurPrecedent = pan.getHeightNumber();
			actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
			for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(5).getActionListeners()) {
				a.actionPerformed(actionEvt);
			}
			Assert.assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), largeurImageReellePrecedente / 2);
			Assert.assertEquals("Hauteur incorrecte", pan.getDisplayingRealImageHeight(), hauteurImageReellePrecedente / 2);
			Assert.assertEquals("Numero de largeur incorrect", pan.getWidthNumber(), numLargeurPrecedent * 2 + 1);
			Assert.assertEquals("Numero de hauteur incorrect", pan.getHeightNumber(), numHauteurPrecedent * 2 + 1);
			assertMiniMap(miniMap, pan.getWidthNumber(), pan.getHeightNumber());
		}

		// On clique en (0, HauteurImage)
		MouseEvent mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 8, 0, 0-8, pan.getImageHeight()-57, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		Assert.assertEquals("Identification de Pechora rate", window.getRes(), "1832 - Pechora");

		// On dezoom trois fois
		for (int k = 0; k < 3; k++) {
			int largeurImageReellePrecedente = pan.getDisplayingRealImageWidth();
			int hauteurImageReellePrecedente = pan.getDisplayingRealImageHeight();
			int numLargeurPrecedent = pan.getWidthNumber();
			int numHauteurPrecedent = pan.getHeightNumber();
			actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
			for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(6).getActionListeners()) {
				a.actionPerformed(actionEvt);
			}
			Assert.assertEquals("Largeur incorrecte", pan.getDisplayingRealImageWidth(), largeurImageReellePrecedente * 2);
			Assert.assertEquals("Hauteur incorrecte", pan.getDisplayingRealImageHeight(), hauteurImageReellePrecedente * 2);
			Assert.assertEquals("Numero de largeur incorrect", pan.getWidthNumber(), (numLargeurPrecedent - 1) / 2);
			Assert.assertEquals("Numero de hauteur incorrect", pan.getHeightNumber(), (numHauteurPrecedent - 1) / 2);
			assertMiniMap(miniMap, pan.getWidthNumber(), pan.getHeightNumber());
		}

		// Deplacement tout à gauche
		while (pan.getWidthNumber() > 0) {
			actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
			for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(1).getActionListeners()) {
				a.actionPerformed(actionEvt);
			}
			i--;
			Assert.assertEquals("Numero de largeur incorrect", pan.getWidthNumber(), i);
			assertMiniMap(miniMap, i, 0);
		}
		actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
		for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(1).getActionListeners()) {
			a.actionPerformed(actionEvt);
		}
		Assert.assertEquals("Numero de largeur incorrect", pan.getWidthNumber(), i);
		assertMiniMap(miniMap, i, 0);

		// On clique en (LargeurMax, HauteurMax)
		mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 11, 0, pan.getImageWidth() - 1,
				pan.getImageHeight() - 1, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		Assert.assertEquals("Identification de Kola rate", window.getRes(), "387 - Kola");

		// Deplacement tout en bas
		if (pan.getImageHeight() < pan.getRealHeight()) {
			actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
			for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(2).getActionListeners()) {
				a.actionPerformed(actionEvt);
			}
			i++;
			Assert.assertEquals("Numero de hauteur incorrect", pan.getHeightNumber(), i);
			assertMiniMap(miniMap, 0, i);
		}
		while (pan.getHeightNumber() * pan.getDisplayingRealImageHeight() / 2  + pan.getDisplayingRealImageHeight()
				< pan.getRealHeight()) {
			actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
			for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(2).getActionListeners()) {
				a.actionPerformed(actionEvt);
			}
			i++;
			Assert.assertEquals("Numero de hauteur incorrect", pan.getHeightNumber(), i);
			assertMiniMap(miniMap, 0, i);
		}
		actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
		for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(2).getActionListeners()) {
			a.actionPerformed(actionEvt);
		}
		Assert.assertEquals("Numero de hauteur incorrect", pan.getHeightNumber(), i);
		assertMiniMap(miniMap, 0, i);
		
		// On clique en (LargeurMax, HauteurMax)
		mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 15, 0, pan.getImageWidth() - 1,
				pan.getImageHeight() - 1, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		Assert.assertEquals("Identification de Central Africa rate", window.getRes(), "1487 - Central Africa");
		
		// Deplacement tout en haut
		while (pan.getHeightNumber() > 0) {
			actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
			for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(0).getActionListeners()) {
				a.actionPerformed(actionEvt);
			}
			i--;
			Assert.assertEquals("Numero de hauteur incorrect", pan.getHeightNumber(), i);
			assertMiniMap(miniMap, 0, i);
		}
		actionEvt = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "", 1, 0);
		for (ActionListener a: window.getJMenuBar().getMenu(2).getItem(0).getActionListeners()) {
			a.actionPerformed(actionEvt);
		}
		Assert.assertEquals("Numero de hauteur incorrect", pan.getHeightNumber(), i);
		assertMiniMap(miniMap, 0, i);

		// On clique en (LargeurMax, HauteurMax)
		mevt = new MouseEvent(window, MouseEvent.MOUSE_CLICKED, 18, 0, pan.getImageWidth() - 1,
				pan.getImageHeight() - 1, 1, false);
		for(MouseListener ml: pan.getMouseListeners()){
			ml.mouseClicked(mevt);
		}
		Assert.assertEquals("Identification de Kola rate", window.getRes(), "387 - Kola");

		// Close the window
		window.dispose();
	}
}

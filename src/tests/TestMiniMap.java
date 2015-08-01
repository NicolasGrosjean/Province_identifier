package tests;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import config.ConfigStorage;
import text.Text;
import text.TextFrancais;
import base.Window;
import base.MiniMap;
import base.Panel;

public class TestMiniMap {
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
	public void testMouse() throws IOException {
		Window window = new Window(configuration.getFirst(), text, configuration);
		MiniMap miniMap = configuration.getFirst().getMiniMap();
		
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
		window.dispose();
		window = null;
	}
}

package base;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import text.Text;

/**
 * Classe qui affiche la fenêtre graphique et fait principalement le boulot
 * 
 * @author Mouchi
 *
 */
public class Window extends JFrame implements MouseListener, KeyListener {

	// Constantes de la position du premier pixel de l'image par rapport à la fenêtre
	static private int PREMIER_PIXEL_X = 8;
	static private int PREMIER_PIXEL_Y = 31;

	// Taille de la fenêtre par défaut
	static private int LARGEUR_FENETRE = 1024 +  2 * PREMIER_PIXEL_X + 256 + 5; 
	// Largeur : marge (barre bleue) à gauche et à droite
	static private int HAUTEUR_FENETRE = 512 + PREMIER_PIXEL_Y + 26 + 8;
	// Hauteur : PREMIER_PIXEL_Y pour le haut, 26 pour le texte, 8 pour la marge en bas

	// Image de la map
	private Panel pan;

	// Mini image de la map
	private MiniMap miniMap;

	// Conteneur des objets
	private JPanel container = new JPanel();

	// Affichage textuel
	private JLabel labelText = new JLabel();
	private JLabel labelRes = new JLabel("");

	// Bouton pour copier le résultat
	private JButton copyButton = new JButton();

	// Demande d'accès à une province particulière
	private JButton searchButton = new JButton();

	// Base de données des provinces
	private ProvinceStorage provinces;

	// Redondance de l'état activé pour le clavier
	private boolean enabledHaut = false; // car on commence en haut
	private boolean enabledBas = true;
	private boolean enabledGauche = false; // car on commence à gauche
	private boolean enabledDroit = true;
	private boolean enabledPlus = true;
	private boolean enabledMoins = true;

	// Text
	private Text text;

	// On récupère seulement les pointeurs (i.e pas de copie)
	public Window(ProvinceStorage provinces, Panel panel, Text text, MiniMap miniMap) {
		this(text.windowTitle(), LARGEUR_FENETRE, HAUTEUR_FENETRE, provinces,
				panel, text, miniMap);
	}

	// On récupère seulement les pointeurs (i.e pas de copie)
	public Window(String title, int width, int height,
			ProvinceStorage provinces, Panel panel, Text text, MiniMap miniMap) {
		// Accès aux provinces
		this.provinces = provinces;

		// Accès à l'affichage de la map
		this.pan = panel;

		// Accès à la fenêtre à partir de la mini map
		this.miniMap = miniMap;
		miniMap.setWindow(this);

		// Text
		this.text = text;

		// Fenêtre
		this.setTitle(title);		
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null); // place au centre		

		// Conteneur pour nos objets
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());

		// Ajout du panneau et de la mini-map
		container.add(pan, BorderLayout.CENTER);
		JPanel east = new JPanel();
		east.setLayout(new GridLayout(2, 1, 5, 5));
		east.add(new JPanel());
		east.add(miniMap);
		container.add(east, BorderLayout.EAST);

		// Ajout des textes et boutons
		Font police = new Font("Tahoma", Font.BOLD, 14);
		labelText.setText(text.clickedProvince());
		labelText.setFont(police);
		labelText.setForeground(Color.blue);
		labelRes.setFont(police);
		labelRes.setForeground(Color.blue);
		copyButton.setText(text.copyClipboard());
		searchButton.setText(text.provinceSearch());
		JPanel north = new JPanel();
		north.setLayout(new GridLayout(1, 4, 5, 5));
		north.add(labelText);
		north.add(labelRes);
		north.add(copyButton);
		north.add(searchButton);
		container.add(north, BorderLayout.NORTH);

		// Actions des boutons
		copyButton.addActionListener(new BoutonCopierListener());
		searchButton.addActionListener(new SearchButtonListener());

		// On rend la fenêtre focusable pour lire le clavier, mais pas le bouton de résultat
		setFocusable(true);
		copyButton.setFocusable(false);

		// Ajout du conteneur
		this.setContentPane(container);

		// Fenêtre visible
		this.setVisible(true);

		// Ajout de la lecture de la souris
		this.addMouseListener(this);
		this.addKeyListener(this);
	}

	/**
	 * Utile pour tester que le programme fonctionne
	 * @return
	 */
	public String getRes() {
		return labelRes.getText();
	}

	/**
	 * Verrouille les boutons selon l'image
	 * et met à jour les booléens pour "verrouiller" les touches clavier
	 */
	public void verouillageDeverouillageBoutonsDirections() {
		if (pan.getHeightNumber() == 0) {
			enabledHaut = false;		
		} else {
			enabledHaut = true;		
		}
		if (pan.getWidthNumber() == 0) {
			enabledGauche = false;		
		} else {
			enabledGauche = true;		
		}
		if (pan.getHeightNumber() == pan.getRealHeight()
				/ (pan.getDisplayingRealImageHeight() / 2) - 2) {
			enabledBas = false;		
		} else {
			enabledBas = true;		
		}
		if (pan.getWidthNumber() == pan.getRealWidth()
				/ (pan.getDisplayingRealImageWidth() / 2) - 2) {
			enabledDroit = false;		
		} else {
			enabledDroit = true;		
		}
	}

	// ---------------- Actions souris ---------------------------
	@Override
	public void mouseClicked(MouseEvent arg0) {	
		int premier_pixel_y = PREMIER_PIXEL_Y + labelText.getHeight();
		if (arg0.getX() < pan.getImageWidth() + PREMIER_PIXEL_X
				&& arg0.getY() < pan.getImageHeight() + premier_pixel_y
				&& arg0.getY() > premier_pixel_y) {
			/*Algo : on calcule la position par rapport au pixel en haut à gauche
			 * de l'image affichée. (On retranche la position du premier pixel
			 * et on divise par le coefficient de zoom)
			 * Enfin on ajoute la position du pixel en haut à gauche
			 */
			int rgb = pan.getRGB(
					(int)((arg0.getX() - PREMIER_PIXEL_X ) /
							((float)pan.getImageWidth() / (float)pan.getDisplayingRealImageWidth()) + 
							(pan.getWidthNumber() * pan.getDisplayingRealImageWidth() / 2))
							, (int)((arg0.getY() - premier_pixel_y) /
									((float)pan.getImageHeight() / (float)pan.getDisplayingRealImageHeight()) +
									(pan.getHeightNumber() * pan.getDisplayingRealImageHeight() / 2)));
			/*
			 * On a fait appel à des cast car 1/2 = 0 alors que
			 * (float)1/(float)2 = 0.5 On recast en int car on veut un int
			 */
			int r = rgb >> 16 & 0xff;
			int g = rgb >> 8 & 0xff;
			int b = rgb & 0xff;
			Province province = provinces.getProvince(r, g, b);
			if (province != null) {
				labelRes.setText(province.toString());
			} else {
				labelRes.setText("");
			}
		} else {
			labelRes.setText("");
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	// ---------------- Actions clavier ---------------------------
	@Override
	public void keyPressed(KeyEvent event) {
		/* Codes :
		 * 	37 : <-
		 * 	38 : flèche du haut
		 * 	39 : ->
		 * 	40 : flèche du bas
		 */
		if (event.getKeyCode() == 37) {
			if (enabledGauche)
				actionGauche();
		} else if (event.getKeyCode() == 38) {
			if (enabledHaut)
				actionHaut();
		} else if (event.getKeyCode() == 39) {
			if (enabledDroit)
				actionDroit();
		} else if (event.getKeyCode() == 40) {
			if (enabledBas)
				actionBas();
		} else if (event.getKeyChar() == '+') {
			if (enabledPlus)
				actionPlus();
		} else if (event.getKeyChar() == '-') {
			if (enabledMoins)
				actionMoins();
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {	 
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

	// ---------------- Déplacement de l'image ---------------------------
	/**
	 * Déplacement de l'image vers le haut
	 */
	private void actionHaut() {
		pan.heightNumberLeast();
		verouillageDeverouillageBoutonsDirections();
		this.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Déplacement de l'image vers le bas
	 */
	private void actionBas() {
		pan.heightNumberMore();
		verouillageDeverouillageBoutonsDirections();
		this.repaint();	
		miniMap.setRectangle();
	}

	/**
	 * Déplacement de l'image vers la gauche
	 */
	private void actionGauche() {
		pan.widthNumberLeast();
		verouillageDeverouillageBoutonsDirections();
		this.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Déplacement de l'image vers la droite
	 */
	private void actionDroit() {
		pan.widthNumberMore();
		verouillageDeverouillageBoutonsDirections();
		this.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Zoom sur l'image
	 */
	private void actionPlus() {
		pan.zoomMore();
		enabledMoins = true;
		// On bloque le zoom à X256
		if (pan.getImageWidth() / pan.getDisplayingRealImageWidth() == 256) {
			enabledPlus = false;
		}			
		verouillageDeverouillageBoutonsDirections();
		this.repaint();
		miniMap.setRectangle();
	}

	/** 
	 * Dézoom de l'image
	 */
	private void actionMoins() {
		if (2 * pan.getDisplayingRealImageWidth() > pan.getRealWidth()
				|| 2 * pan.getDisplayingRealImageHeight() > pan.getRealHeight()) {
			enabledMoins = false;
		} else {
			pan.zoomLeast();
			enabledPlus = true;			
			verouillageDeverouillageBoutonsDirections();
			this.repaint();
			miniMap.setRectangle();
		}
	}

	// ---------------- Actions des boutons ---------------------------
	class BoutonCopierListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {			
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(labelRes.getText()), null);
		}
	}

	class SearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {			
			SearchDialog searchDialog = new SearchDialog(null, text.provinceSearch(), true, text, provinces, false, null, null);
			Province searchProvince = searchDialog.getSearchResult();
			if (searchProvince != null) {
				try {
					// Calcul du barycentre de la province cherchée		
					Point barycentre = pan.getPosition(searchProvince.getIdentifiantRGB());
					// On multiple par 4 / pan.getLargeurAfficheImageReelle() pour avoir le double du numéro
					int numLargeur = (int)barycentre.getX() * 4 / pan.getDisplayingRealImageWidth();
					// On enlève 1 pour centrer
					numLargeur--;
					// On divise par 2 pour avoir un nombre correct
					numLargeur /= 2;
					// Si on dépasse on prend le max
					if (numLargeur >= pan.getRealWidth()/(pan.getDisplayingRealImageWidth()/2) - 2) {
						numLargeur = pan.getRealWidth()/(pan.getDisplayingRealImageWidth()/2) - 2;
					}
					// On met à jour l'image
					pan.setWidthNumber(numLargeur);
					// On fait de même pour la hauteur
					int numHauteur = (int)barycentre.getY() * 4 / pan.getDisplayingRealImageHeight();
					numHauteur--;
					numHauteur /= 2;
					if (numHauteur >= pan.getRealHeight()/(pan.getDisplayingRealImageHeight()/2) - 2) {
						numHauteur = pan.getRealHeight()/(pan.getDisplayingRealImageHeight()/2) - 2;			
					}
					pan.setHeightNumber(numHauteur);
					// On met à jour la fenetre (gestions des touches + affichage)
					verouillageDeverouillageBoutonsDirections();
					repaint();
					miniMap.setRectangle();

				} catch (IllegalArgumentException e) {
					// Province not found
					JOptionPane.showMessageDialog(null, text.provinceNotFound(), text.warningMessage(), JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}


	// ---------------- Testing ---------------------------
	public int premierPixelX() {
		return PREMIER_PIXEL_X;
	}

	public int premierPixelY() {
		return PREMIER_PIXEL_Y + labelText.getHeight();
	}
}

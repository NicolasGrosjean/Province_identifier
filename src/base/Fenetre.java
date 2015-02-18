package base;
import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe qui affiche la fenêtre graphique et fait principalement le boulot
 * 
 * @author Mouchi
 *
 */
public class Fenetre extends JFrame implements MouseListener, KeyListener {

	// Taille de la fenêtre par défaut
	static private int LARGEUR_FENETRE = 1300;
	static private int HAUTEUR_FENETRE = 512 + 40; // 40 de marge pour le top

	// Constantes de la position du premier pixel de l'image par rapport à la fenêtre
	static private int PREMIER_PIXEL_X = 8;
	static private int PREMIER_PIXEL_Y = 30;

	// Image de la map
	private Panneau pan;

	// Conteneur des objets
	private JPanel container = new JPanel();

	// Affichage textuel
	private JLabel labelText = new JLabel("Province cliquée :");
	private JLabel labelRes = new JLabel("");

	// Boutons
	private JButton boutonHaut = new JButton("Haut");
	private JButton boutonBas = new JButton("Bas");
	private JButton boutonGauche = new JButton("Gauche");
	private JButton boutonDroit = new JButton("Droite");
	private JButton boutonPlus = new JButton("Plus");
	private JButton boutonMoins = new JButton("Moins");

	// Base de données des provinces
	private StockageProvince provinces;

	// Redondance de l'état activé pour le clavier
	private boolean enabledHaut = false; // car on commence en haut
	private boolean enabledBas = true;
	private boolean enabledGauche = false; // car on commence à gauche
	private boolean enabledDroit = true;
	private boolean enabledPlus = true;
	private boolean enabledMoins = true;

	// On récupère seulement les pointeurs (i.e pas de copie)
	public Fenetre(StockageProvince provinces, Panneau panneau) {
		this("Identificateur de provinces", LARGEUR_FENETRE, HAUTEUR_FENETRE, provinces,
				panneau);
	}

	// On récupère seulement les pointeurs (i.e pas de copie)
	public Fenetre(String title, int largeur, int hauteur,
			StockageProvince provinces, Panneau panneau) {
		// Accès aux provinces
		this.provinces = provinces;

		// Accès à l'affichage de la map
		this.pan = panneau;

		// Fenêtre
		this.setTitle(title);
		this.setSize(largeur, hauteur);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null); // place au centre

		// Conteneur pour nos objets
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());

		// Ajout du panneau
		container.add(pan, BorderLayout.CENTER);

		// Ajout des textes et boutons
		Font police = new Font("Tahoma", Font.BOLD, 14);
		labelText.setFont(police);
		labelText.setForeground(Color.blue);
		labelRes.setFont(police);
		labelRes.setForeground(Color.blue);
		JPanel east = new JPanel();
		east.setLayout(new GridLayout(5, 1, 5, 5));
		east.add(labelText);
		east.add(labelRes);
		JPanel boutonsZoom = new JPanel();
		boutonsZoom.add(boutonPlus);
		boutonsZoom.add(boutonMoins);
		east.add(boutonsZoom);
		east.add(boutonHaut);
		JPanel boutonsRegroupe = new JPanel();
		boutonsRegroupe.add(boutonGauche);
		boutonsRegroupe.add(boutonBas);
		boutonsRegroupe.add(boutonDroit);
		east.add(boutonsRegroupe);
		container.add(east, BorderLayout.EAST);

		// Actions des boutons
		boutonHaut.addActionListener(new BoutonHautListener());
		boutonBas.addActionListener(new BoutonBasListener());
		boutonGauche.addActionListener(new BoutonGaucheListener());
		boutonDroit.addActionListener(new BoutonDroitListener());
		boutonPlus.addActionListener(new BoutonPlusListener());
		boutonMoins.addActionListener(new BoutonMoinsListener());
		// Comme on commence en haut à gauche, on désactive ces 2 boutons
		boutonHaut.setEnabled(false);
		boutonGauche.setEnabled(false);

		// Pour permettre la lecture du clavier
		boutonBas.setFocusable(false);
		boutonHaut.setFocusable(false);
		boutonGauche.setFocusable(false);
		boutonDroit.setFocusable(false);
		boutonPlus.setFocusable(false);
		boutonMoins.setFocusable(false);

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
	private void verouillageDeverouillageBoutonsDirections() {
		if (pan.getNumHauteur() == 0) {
			enabledHaut = false;		
		} else {
			enabledHaut = true;		
		}
		boutonHaut.setEnabled(enabledHaut);
		if (pan.getNumLargeur() == 0) {
			enabledGauche = false;		
		} else {
			enabledGauche = true;		
		}
		boutonGauche.setEnabled(enabledGauche);
		if (pan.getNumHauteur() == pan.getHauteurReelle()
				/ (pan.getHauteurAfficheImageReelle() / 2) - 2) {
			enabledBas = false;		
		} else {
			enabledBas = true;		
		}
		boutonBas.setEnabled(enabledBas);
		if (pan.getNumLargeur() == pan.getLargeurReelle()
				/ (pan.getLargeurAfficheImageReelle() / 2) - 2) {
			enabledDroit = false;		
		} else {
			enabledDroit = true;		
		}
		boutonDroit.setEnabled(enabledDroit);
	}

	// ---------------- Actions souris ---------------------------
	@Override
	public void mouseClicked(MouseEvent arg0) {	

		if (arg0.getX() < pan.getLargeurImage() + PREMIER_PIXEL_X
				&& arg0.getY() < pan.getHauteurImage() + PREMIER_PIXEL_Y) {
			/*Algo : on calcule la position par rapport au pixel en haut à gauche
			 * de l'image affichée. (On retranche la position du premier pixel
			 * et on divise par le coefficient de zoom)
			 * Enfin on ajoute la position du pixel en haut à gauche
			 */
			int rgb = pan.getRGB(
					(int)((arg0.getX() - PREMIER_PIXEL_X ) /
							((float)pan.getLargeurImage() / (float)pan.getLargeurAfficheImageReelle()) + 
							(pan.getNumLargeur() * pan.getLargeurAfficheImageReelle() / 2))
							, (int)((arg0.getY() - PREMIER_PIXEL_Y) /
									((float)pan.getHauteurImage() / (float)pan.getHauteurAfficheImageReelle()) +
									(pan.getNumHauteur() * pan.getHauteurAfficheImageReelle() / 2)));
			/*
			 * On a fait appel à des cast car 1/2 = 0 alors que (float)1/(float)2 = 0.5
			 * On recast en int car on veut un int
			 */
			int r = rgb >> 16 & 0xff;
		int g = rgb >> 8 & 0xff;
		int b = rgb & 0xff;
		Province province = provinces.getProvince(r, g, b);
		if (province != null)
			labelRes.setText(province.toString());
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
		pan.numHauteurMoins();
		verouillageDeverouillageBoutonsDirections();
		this.repaint();
	}

	/**
	 * Déplacement de l'image vers le bas
	 */
	private void actionBas() {
		pan.numHauteurPlus();
		verouillageDeverouillageBoutonsDirections();
		this.repaint();
	}

	/**
	 * Déplacement de l'image vers la gauche
	 */
	private void actionGauche() {
		pan.numLargeurMoins();
		verouillageDeverouillageBoutonsDirections();
		this.repaint();
	}

	/**
	 * Déplacement de l'image vers la droite
	 */
	private void actionDroit() {
		pan.numLargeurPlus();
		verouillageDeverouillageBoutonsDirections();
		this.repaint();
	}

	/**
	 * Zoom sur l'image
	 */
	private void actionPlus() {
		pan.zoomPlus();
		boutonMoins.setEnabled(true);
		enabledMoins = true;
		// On bloque le zoom à X256
		if (pan.getLargeurImage() / pan.getLargeurAfficheImageReelle() == 256) {
			boutonPlus.setEnabled(false);
			enabledPlus = false;
		}			
		verouillageDeverouillageBoutonsDirections();
		this.repaint();
	}

	/** 
	 * Dézoom de l'image
	 */
	private void actionMoins() {
		if (2 * pan.getLargeurAfficheImageReelle() > pan.getLargeurReelle()
				|| 2 * pan.getHauteurAfficheImageReelle() > pan.getHauteurReelle()) {
			boutonMoins.setEnabled(false);
			enabledMoins = false;
		} else {
			pan.zoomMoins();
			boutonPlus.setEnabled(true);
			enabledPlus = true;			
			verouillageDeverouillageBoutonsDirections();
			this.repaint();
		}
	}

	// ---------------- Actions des boutons ---------------------------
	class BoutonHautListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {			
			actionHaut();
		}
	}

	class BoutonBasListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			actionBas();
		}
	}

	class BoutonGaucheListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			actionGauche();
		}
	}

	class BoutonDroitListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			actionDroit();
		}
	}

	class BoutonPlusListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			actionPlus();
		}
	}

	class BoutonMoinsListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			actionMoins();
		}
	}
}

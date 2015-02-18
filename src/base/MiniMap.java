package base;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import text.Text;
import version2.FenetreV2;

public class MiniMap extends JPanel implements MouseListener {
	static public int LARGEUR_IMAGE = 256;
	static public int HAUTEUR_IMAGE = 256;

	private BufferedImage image;
	private Panneau pan;
	private FenetreV2 window;
	
	/** 
	 * Paramètres du rectangle de la mini map
	 */
	private int rectX;
	private int rectY;
	private int rectWidth;
	private int rectHeight;
	
	/**
	 * Largeur réelle de l'image
	 */
	private int largeurReelle;

	/**
	 * Hauteur réelle de l'image
	 */
	private int hauteurReelle;

	/**
	 * Largeur de l'image affichée
	 */
	private int largeurImage;

	/**
	 * Hauteur de l'image affichée
	 */
	private int hauteurImage;

	/**
	 * Text pour les exceptions
	 */
	private Text text;
	
	public MiniMap(String image, int largeurImage, int hauteurImage, Text text, Panneau pan) throws IOException {
		this.setLargeurImage(largeurImage);
		this.setHauteurImage(hauteurImage);
		setPreferredSize(new Dimension(largeurImage, hauteurImage));
		this.text = text;
		this.pan = pan;	
		File file = new File(image);
		this.image = ImageIO.read(file);
		largeurReelle = this.image.getWidth();
		hauteurReelle = this.image.getHeight();
		this.setRectangle(); // Doit être ici par utilise largeurReelle et hauteurReelle
		this.addMouseListener(this);
	}
	
	public MiniMap(String image, Text text, Panneau pan) throws IOException {
		this(image, LARGEUR_IMAGE, HAUTEUR_IMAGE, text, pan);
	}
	
	public int getLargeurImage() {
		return largeurImage;
	}

	public int getHauteurImage() {
		return hauteurImage;
	}

	private void setLargeurImage(int largeurImage) {
		if (largeurImage > 0)
			this.largeurImage = largeurImage;
		else
			throw new IllegalArgumentException(
					text.invalidWidth());
	}

	private void setHauteurImage(int hauteurImage) {
		if (hauteurImage > 0)
			this.hauteurImage = hauteurImage;
		else
			throw new IllegalArgumentException(
					text.invalidHeight());
	}
		
	/**
	 * Référence sur la fenêtre pour la mettre à jour
	 * @param window
	 */
	public void setWindow(FenetreV2 window) {
		this.window = window;
	}

	/**
	 * Met à jour le rectangle de la mini map
	 */
	public void setRectangle() {
		// Valeurs sur le panneau
		rectX = pan.getNumLargeur() * pan.getLargeurAfficheImageReelle() / 2;
		rectY = pan.getNumHauteur() * pan.getHauteurAfficheImageReelle() / 2;
		rectWidth = pan.getLargeurAfficheImageReelle();
		rectHeight = pan.getHauteurAfficheImageReelle();
		
		// Réduction par rapport à la mini map
		// Comme largeurImage/largeurReelle peut être < 1, on passe en float et on repasse en int
		rectX *= (int)(float)largeurImage/(float)largeurReelle;
		rectY *= (int)(float)hauteurImage/(float)hauteurReelle;
		rectWidth *= (int)(float)largeurImage/(float)largeurReelle;
		rectHeight *= (int)(float)hauteurImage/(float)hauteurReelle;
		
		// On repaint
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, largeurImage, hauteurImage, this);
		// On passe par Graphics2D pour pouvoir choisir l'épaisseur du trait
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(3));
		g2d.drawRect(rectX, rectY, rectWidth, rectHeight);
	}
	
	// --------------------- Actions souris ---------------------------
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Position en x sur l'image taille réelle : arg0.getX() * (largeurReelle/largeurImage)
		// On multiple par 4 / pan.getLargeurAfficheImageReelle() pour avoir le double du numéro
		int numLargeur = arg0.getX() * (largeurReelle/largeurImage) * 4 / pan.getLargeurAfficheImageReelle();
		// On enlève 1 pour centrer
		numLargeur--;
		// On divise par 2 pour avoir un nombre correct
		numLargeur /= 2;
		// Si on dépasse on prend le max
		if (numLargeur >= pan.getLargeurReelle()/(pan.getLargeurAfficheImageReelle()/2) - 2) {
			numLargeur = pan.getLargeurReelle()/(pan.getLargeurAfficheImageReelle()/2) - 2;
		}
		// On met à jour l'image
		pan.setNumLargeur(numLargeur);
		// On fait de même pour la hauteur
		int numHauteur = arg0.getY() * (hauteurReelle/hauteurImage) * 4 / pan.getHauteurAfficheImageReelle();
		numHauteur--;
		numHauteur /= 2;
		if (numHauteur >= pan.getHauteurReelle()/(pan.getHauteurAfficheImageReelle()/2) - 2) {
			numHauteur = pan.getHauteurReelle()/(pan.getHauteurAfficheImageReelle()/2) - 2;			
		}
		pan.setNumHauteur(numHauteur);
		// On met à jour la fenetre (gestions des touches + affichage)
		window.verouillageDeverouillageBoutonsDirections();
		window.repaint();
		// On met à jour le rectangle de la mini map
		this.setRectangle();
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

	// --------------------- Testing ---------------------------
	public int getRectX() {
		return rectX;
	}

	public int getRectY() {
		return rectY;
	}

	public int getRectWidth() {
		return rectWidth;
	}

	public int getRectHeight() {
		return rectHeight;
	}

	public int getLargeurReelle() {
		return largeurReelle;
	}

	public int getHauteurReelle() {
		return hauteurReelle;
	}	
}

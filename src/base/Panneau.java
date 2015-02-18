package base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import text.Text;

/**
 * Panneau (Panel) qui affiche et gère l'image 
 * 
 * @author Mouchi
 *
 */
public class Panneau extends JPanel {

	static public int LARGEUR_IMAGE = 1024;
	static public int HAUTEUR_IMAGE = 512;

	private BufferedImage image;
	
	// Pour les flashs
	private LinkedList<Point> provincePoints;
	private int provinceRGB;

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
	 * Largeur affichée de l'image réelle
	 */
	private int largeurAfficheImageReelle;

	/**
	 * Hauteur affichée de l'image réelle
	 */
	private int hauteurAfficheImageReelle;

	/**
	 * Numéro du bloc image en largeur
	 */
	private int numLargeur = 0;

	/**
	 * Numéro du bloc image en hauteur
	 */
	private int numHauteur = 0;

	private Text text;
	
	public Panneau(String image, int largeurImage, int hauteurImage, Text text) throws IOException {
		this.setLargeurImage(largeurImage);
		this.setHauteurImage(hauteurImage);
		this.setLargeurAfficheImageReelle(largeurImage);
		this.setHauteurAfficheImageReelle(hauteurImage);
		File file = new File(image);
		this.image = ImageIO.read(file);
		largeurReelle = this.image.getWidth();
		hauteurReelle = this.image.getHeight();
		this.text = text;
	}

	public Panneau(String nomFichierProvince, Text text) throws IOException {
		this(nomFichierProvince, LARGEUR_IMAGE, HAUTEUR_IMAGE, text);
	}

	public int getLargeurReelle() {
		return largeurReelle;
	}

	public int getHauteurReelle() {
		return hauteurReelle;
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

	private void setLargeurAfficheImageReelle(int largeurAfficheImageReelle) {
		if (largeurAfficheImageReelle > 0)
			this.largeurAfficheImageReelle = largeurAfficheImageReelle;
		else
			throw new IllegalArgumentException(
					text.invalidWidth());
	}

	private void setHauteurAfficheImageReelle(int hauteurAfficheImageReelle) {
		if (hauteurAfficheImageReelle > 0)
			this.hauteurAfficheImageReelle = hauteurAfficheImageReelle;
		else
			throw new IllegalArgumentException(
					text.invalidHeight());
	}	

	public int getLargeurAfficheImageReelle() {
		return largeurAfficheImageReelle;
	}

	public int getHauteurAfficheImageReelle() {
		return hauteurAfficheImageReelle;
	}

	public void zoomPlus() {
		// On zoom
		largeurAfficheImageReelle /= 2;
		hauteurAfficheImageReelle /= 2;
		numLargeur *= 2;
		numHauteur *= 2;
		// On centre le zoom
		numLargeur++;
		numHauteur++;
	}

	public void zoomMoins() {
		// On décentre
		numLargeur--;
		numHauteur--;
		// On dézoom
		largeurAfficheImageReelle *= 2;
		hauteurAfficheImageReelle *= 2;
		numLargeur /= 2;
		numHauteur /= 2;
	}

	public int getNumLargeur() {
		return numLargeur;
	}

	public int getNumHauteur() {
		return numHauteur;
	}

	public void numLargeurMoins() {
		if (numLargeur > 0)
			numLargeur--;
		else
			throw new IllegalArgumentException(text.invalidWidthNumber());
	}

	public void numLargeurPlus() {
		if (numLargeur < largeurReelle/(largeurAfficheImageReelle/2) - 2)
			numLargeur++;
		else
			throw new IllegalArgumentException(text.invalidWidthNumber());
	}

	public void numHauteurMoins() {
		if (numHauteur > 0)
			numHauteur--;
		else
			throw new IllegalArgumentException(text.invalidHeightNumber());
	}

	public void numHauteurPlus() {
		if (numHauteur < hauteurReelle/(hauteurAfficheImageReelle/2) - 2) {
			numHauteur++;
		}else
			throw new IllegalArgumentException(text.invalidHeightNumber());
	}
	
	public void setNumLargeur(int numLargeur){
		if (numLargeur >= 0 && numLargeur < largeurReelle/(largeurAfficheImageReelle/2) - 1) {
			this.numLargeur = numLargeur;
		} else {
			throw new IllegalArgumentException(text.invalidWidthNumber());
		}
	}
	
	public void setNumHauteur(int numHauteur){
		if (numHauteur >= 0 && numHauteur < hauteurReelle/(hauteurAfficheImageReelle/2) - 1) {
			this.numHauteur = numHauteur;			
		} else {
			throw new IllegalArgumentException(text.invalidHeightNumber());
		}
	}

	/**
	 * Dessine l'image comme souhaitée
	 * i.e on calcule ce que l'on affiche
	 * et on l'affiche dans le rectangle largeurImage*hauteurImage
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(
				image.getSubimage(numLargeur * largeurAfficheImageReelle / 2, 
						numHauteur * hauteurAfficheImageReelle / 2, largeurAfficheImageReelle, hauteurAfficheImageReelle),
						0, 0, largeurImage, hauteurImage, this);
	}

	/**
	 * Donne le code RGB d'un pixel de l'image
	 * @param x abscisse du pixel
	 * @param y ordonnée du pixel
	 * @return code RGB du pixel
	 */
	public int getRGB(int x, int y) {
		if (0 <= x && x <= largeurReelle && 0 <= y && y <= hauteurReelle)
			return image.getRGB(x, y);
		else
			throw new IllegalArgumentException(text.invalidRGB());
	}
	
	/**
	 * Renvoie le barycentre de la province ayant ce code rgb
	 * @param rgb
	 * @return
	 */
	public Point getPosition(int rgb) {
		int sumX = 0;
		int sumY = 0;
		int nbPoints = 0;
		provincePoints = new LinkedList<Point>();
		for (int x = 0; x < largeurReelle; x++) {
			for (int y = 0; y < hauteurReelle; y++) {
				// On chercher une province ayant le même R && G && B
				if ((image.getRGB(x, y) >> 16 & 0xff) == (rgb >> 16 & 0xff)
						&& (image.getRGB(x, y) >> 8 & 0xff) == (rgb >> 8 & 0xff)
						&& (image.getRGB(x, y) & 0xff) == (rgb & 0xff)) {
					sumX += x;
					sumY += y;
					nbPoints++;
					provincePoints.add(new Point(x, y));
					provinceRGB = rgb;
				}
			}
		}
		if (nbPoints > 0) {
			flashProvince(3);
			return new Point(sumX / nbPoints, sumY / nbPoints);
		} else {
			throw new IllegalArgumentException();
		}	
	}
	
	/**
	 * Flash une province
	 * ADAPTED FROM THE SCENARIO EDITOR
	 * @param numFlashes
	 */
	public void flashProvince(int numFlashes) {
		ActionListener listener = new ActionListener() {
			private boolean color = true;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (color) {
					for (Point p : provincePoints) {
						image.setRGB((int)p.getX(), (int)p.getY(), Color.WHITE.getRGB());
					}
				} else {
					for (Point p : provincePoints) {
						image.setRGB((int)p.getX(), (int)p.getY(), provinceRGB);
					}
				}
				color = !color;
				repaint();
			}
		};

		for (int i = 1; i <= numFlashes * 2; i++) {
			Timer t = new Timer(333 * i, listener);
			t.setRepeats(false);
			t.start();
		}
	}
}

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

/**
 * Mini-map to navigate on the map
 * 
 * @author Mouchi
 *
 */
public class MiniMap extends JPanel implements MouseListener {
	static public int IMAGE_WIDTH = 256;
	static public int IMAGE_HEIGHT = 256;

	private BufferedImage image;
	private Panel pan;
	private Window window;
	
	/** 
	 * Mini-map rectangle parameters
	 */
	private int rectX;
	private int rectY;
	private int rectWidth;
	private int rectHeight;
	
	/**
	 * Real image width 
	 */
	private int realWidth;

	/**
	 * Real image height
	 */
	private int realHeight;

	/**
	 * Displaying image width
	 */
	private int imageWidth;

	/**
	 * Displaying image height
	 */
	private int imageHeight;

	/**
	 * Language for the text
	 */
	private Text text;
	
	public MiniMap(String image, int imageWidth, int imageHeight, Text text, Panel pan) throws IOException {
		this.setImageWidth(imageWidth);
		this.setImageHeight(imageHeight);
		setPreferredSize(new Dimension(imageWidth, imageHeight));
		this.text = text;
		this.pan = pan;	
		File file = new File(image);
		this.image = ImageIO.read(file);
		realWidth = this.image.getWidth();
		realHeight = this.image.getHeight();
		this.setRectangle(); // Must be here because use pan
		this.addMouseListener(this);
	}
	
	public MiniMap(String image, Text text, Panel pan) throws IOException {
		this(image, IMAGE_WIDTH, IMAGE_HEIGHT, text, pan);
	}
	
	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	private void setImageWidth(int imageWidth) {
		if (imageWidth > 0)
			this.imageWidth = imageWidth;
		else
			throw new IllegalArgumentException(
					text.invalidWidth());
	}

	private void setImageHeight(int imageHeight) {
		if (imageHeight > 0)
			this.imageHeight = imageHeight;
		else
			throw new IllegalArgumentException(
					text.invalidHeight());
	}
		
	/**
	 * Access to he window in order to actualize it
	 * @param window
	 */
	public void setWindow(Window window) {
		this.window = window;
	}

	/**
	 * Actualize mini-map rectangle
	 */
	public void setRectangle() {
		// Pan values
		rectX = pan.getWidthNumber() * pan.getDisplayingRealImageWidth() / 2;
		rectY = pan.getHeightNumber() * pan.getDisplayingRealImageHeight() / 2;
		rectWidth = pan.getDisplayingRealImageWidth();
		rectHeight = pan.getDisplayingRealImageHeight();
		
		// Reduction to the mini-map size
		// Like imageWidth/realWidth may be < 1, we calculate in float then return integer result
		rectX *= (int)(float)imageWidth/(float)realWidth;
		rectY *= (int)(float)imageHeight/(float)realHeight;
		rectWidth *= (int)(float)imageWidth/(float)realWidth;
		rectHeight *= (int)(float)imageHeight/(float)realHeight;
		
		// Actualize displaying
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, imageWidth, imageHeight, this);
		// Using Graphics2D to choose line thickness
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(3));
		g2d.drawRect(rectX, rectY, rectWidth, rectHeight);
	}
	
	// --------------------- Mouse actions ---------------------------
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// X position on real image : arg0.getX() * (realWidth/imageWidth)
		// Multiplying by 4 / pan.getDisplayingRealImageWidth() to have twice width number
		int numLargeur = arg0.getX() * (realWidth/imageWidth) * 4 / pan.getDisplayingRealImageWidth();
		// Subtract 1 to center
		numLargeur--;
		// Dividing by 2 to have the correct number
		numLargeur /= 2;
		// If width number is too big, it become the max
		if (numLargeur >= pan.getRealWidth()/(pan.getDisplayingRealImageWidth()/2) - 2) {
			numLargeur = pan.getRealWidth()/(pan.getDisplayingRealImageWidth()/2) - 2;
		}
		// Actualize panel
		pan.setWidthNumber(numLargeur);
		// Same thing with height
		int numHauteur = arg0.getY() * (realHeight/imageHeight) * 4 / pan.getDisplayingRealImageHeight();
		numHauteur--;
		numHauteur /= 2;
		if (numHauteur >= pan.getRealHeight()/(pan.getDisplayingRealImageHeight()/2) - 2) {
			numHauteur = pan.getRealHeight()/(pan.getDisplayingRealImageHeight()/2) - 2;			
		}
		pan.setHeightNumber(numHauteur);
		// Actualize window (key management + displaying)
		window.movingActionLockingUnlocking();
		window.repaint();
		// Actualize mini-map rectangle
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

	public int getRealWidth() {
		return realWidth;
	}

	public int getRealHeight() {
		return realHeight;
	}	
}

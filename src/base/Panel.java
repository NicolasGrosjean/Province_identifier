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
 * Panel displaying the map 
 * 
 * @author Mouchi
 *
 */
public class Panel extends JPanel {

	static public int IMAGE_WIDTH = 1024;
	static public int IMAGE_HEIGHT = 512;

	private BufferedImage image;

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
	 * Width for the real image which is displayed
	 */
	private int displayinRealImageWidth;

	/**
	 * Height for the real image which is displayed
	 */
	private int displayingRealImageHeight;

	/**
	 * Width bloc number
	 */
	private int widthNumber = 0;

	/**
	 * Height bloc number
	 */
	private int heightNumber = 0;

	/**
	 * Language for the text
	 */
	private Text text;
	
	public Panel(String image, int imageWidth, int imageHeight, Text text) throws IOException {
		this.setImageWidth(imageWidth);
		this.setImageHeight(imageHeight);
		this.setDisplayingRealImageWidth(imageWidth);
		this.setDisplayingRealImageHeight(imageHeight);
		File file = new File(image);
		this.image = ImageIO.read(file);
		realWidth = this.image.getWidth();
		realHeight = this.image.getHeight();
		this.text = text;
	}

	public Panel(String nomFichierProvince, Text text) throws IOException {
		this(nomFichierProvince, IMAGE_WIDTH, IMAGE_HEIGHT, text);
	}

	public int getRealWidth() {
		return realWidth;
	}

	public int getRealHeight() {
		return realHeight;
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

	private void setDisplayingRealImageWidth(int displayinRealImageWidth) {
		if (displayinRealImageWidth > 0)
			this.displayinRealImageWidth = displayinRealImageWidth;
		else
			throw new IllegalArgumentException(
					text.invalidWidth());
	}

	private void setDisplayingRealImageHeight(int displayingRealImageHeight) {
		if (displayingRealImageHeight > 0)
			this.displayingRealImageHeight = displayingRealImageHeight;
		else
			throw new IllegalArgumentException(
					text.invalidHeight());
	}	

	public int getDisplayingRealImageWidth() {
		return displayinRealImageWidth;
	}

	public int getDisplayingRealImageHeight() {
		return displayingRealImageHeight;
	}

	public void zoomMore() {
		// Zooming
		displayinRealImageWidth /= 2;
		displayingRealImageHeight /= 2;
		widthNumber *= 2;
		heightNumber *= 2;
		// Centering
		widthNumber++;
		heightNumber++;
	}

	public void zoomLeast() {
		// Uncentering
		widthNumber--;
		heightNumber--;
		// Unzooming
		displayinRealImageWidth *= 2;
		displayingRealImageHeight *= 2;
		widthNumber /= 2;
		heightNumber /= 2;
	}

	public int getWidthNumber() {
		return widthNumber;
	}

	public int getHeightNumber() {
		return heightNumber;
	}

	public void widthNumberLeast() {
		if (widthNumber > 0)
			widthNumber--;
		else
			throw new IllegalArgumentException(text.invalidWidthNumber());
	}

	public void widthNumberMore() {
		if (widthNumber < realWidth/(displayinRealImageWidth/2) - 2)
			widthNumber++;
		else
			throw new IllegalArgumentException(text.invalidWidthNumber());
	}

	public void heightNumberLeast() {
		if (heightNumber > 0)
			heightNumber--;
		else
			throw new IllegalArgumentException(text.invalidHeightNumber());
	}

	public void heightNumberMore() {
		if (heightNumber < realHeight/(displayingRealImageHeight/2) - 2) {
			heightNumber++;
		}else
			throw new IllegalArgumentException(text.invalidHeightNumber());
	}
	
	public void setWidthNumber(int widthNumber){
		if (widthNumber >= 0 && widthNumber < realWidth/(displayinRealImageWidth/2) - 1) {
			this.widthNumber = widthNumber;
		} else {
			throw new IllegalArgumentException(text.invalidWidthNumber());
		}
	}
	
	public void setHeightNumber(int heightNumber){
		if (heightNumber >= 0 && heightNumber < realHeight/(displayingRealImageHeight/2) - 1) {
			this.heightNumber = heightNumber;			
		} else {
			throw new IllegalArgumentException(text.invalidHeightNumber());
		}
	}

	/**
	 * Display wanted image
	 * i.e calculate the subimage to display
	 * and display it in imageWidth*imageHeight
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(
				image.getSubimage(widthNumber * displayinRealImageWidth / 2, 
						heightNumber * displayingRealImageHeight / 2, displayinRealImageWidth, displayingRealImageHeight),
						0, 0, imageWidth, imageHeight, this);
	}

	/**
	 * Give RGB of a pixel
	 * @param x abscissa of pixel
	 * @param y ordered of pixel
	 * @return RGB of the pixel
	 */
	public int getRGB(int x, int y) {
		if (0 <= x && x <= realWidth && 0 <= y && y <= realHeight)
			return image.getRGB(x, y);
		else
			throw new IllegalArgumentException(text.invalidRGB());
	}
	
	/**
	 * Give middle of the province with this rgb and flash the province
	 * @param rgb
	 * @param nmbFlashs Number of flash for the province found
	 * @param window Search the province in the displayingWindow or in all the panel
	 * @return
	 */
	public Point getPosition(int rgb, int nmbFlashs, boolean window) {
		int sumX = 0;
		int sumY = 0;
		int nbPoints = 0;
		LinkedList<Point> provincePoints = new LinkedList<Point>();
		int minX, maxX, minY, maxY;
		if (window) {
			minX = widthNumber * displayinRealImageWidth / 2;
			maxX = minX + displayinRealImageWidth;
			minY = heightNumber * displayingRealImageHeight / 2;
			maxY = minY + displayingRealImageHeight;
		} else {
			minX = 0;
			maxX = realWidth;
			minY = 0;
			maxY = realHeight;
		}
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				// On chercher une province ayant le même R && G && B
				if ((image.getRGB(x, y) >> 16 & 0xff) == (rgb >> 16 & 0xff)
						&& (image.getRGB(x, y) >> 8 & 0xff) == (rgb >> 8 & 0xff)
						&& (image.getRGB(x, y) & 0xff) == (rgb & 0xff)) {
					sumX += x;
					sumY += y;
					nbPoints++;
					provincePoints.add(new Point(x, y));
				}
			}
		}
		if (nbPoints > 0) {
			flashProvince(nmbFlashs, provincePoints, rgb);
			return new Point(sumX / nbPoints, sumY / nbPoints);
		} else {
			throw new IllegalArgumentException();
		}	
	}
	
	/**
	 * Flash a province
	 * ADAPTED from CK2 Scenario Editor 
	 * (http://forum.paradoxplaza.com/forum/showthread.php?686024-TOOL-CK2-Scenario-Editor)
	 * @param numFlashes
	 */
	public void flashProvince(int numFlashes, final LinkedList<Point> provincePoints,
			final int provinceRGB) {
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
				if (e.getActionCommand() == "last") {
					// The last flash is finish
					provincePoints.clear();
				}
			}
		};

		Timer t = null;
		int last = numFlashes * 2;
		for (int i = 1; i <= last; i++) {
			t = new Timer(333 * i, listener);
			t.setRepeats(false);
			if (i == last) {
				t.setActionCommand("last");
			}
			t.start();
		}
		while (!t.isRunning()) {
			System.out.println(t.getDelay());
		}
	}
}

package base;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import config.ConfigStorage;
import config.WorkingSession;
import config.WorkingSessionNewDialog;
import text.Text;

/**
 * Graphic window, the body of the software
 * 
 * @author Mouchi
 *
 */
public class Window extends JFrame implements MouseListener, KeyListener {

	// First pixel position
	static private int FIRST_PIXEL_X = 8;
	static private int FIRST_PIXEL_Y = 31;

	// Default window size
	static private int WINDOW_WIDTH = 1024 +  2 * FIRST_PIXEL_X + 256 + 5; 
	// Width margin : 2 FIRST_PIXEL_X for the  2 border + 256 for the mini-map + 5 for a little extra margin
	static private int WINDOW_HEIGHT = 512 + FIRST_PIXEL_Y + 26 + 22 + 8;
	// Height margin : FIRST_PIXEL_Y for the top border + 26 for text and buttons + 8 for the bottom border
	// 													+ 22 for menus 

	// Map image
	private Panel pan;

	// Mini-map image
	private MiniMap miniMap;

	// Object container
	private JPanel container = new JPanel();

	// Text displaying
	private JLabel textLabel = new JLabel();
	private JLabel resLabel = new JLabel("");

	// Copy button
	private JButton copyButton = new JButton();

	// Search button
	private JButton searchButton = new JButton();

	// Province database
	private ProvinceStorage provinces;

	// Action status for navigation (4 moving + 2 zooming actions)
	private boolean enabledTop = false; // because we start in top
	private boolean enabledBottom = true;
	private boolean enabledLeft = false; // because we start in left
	private boolean enabledRight = true;
	private boolean enabledMore = true;
	private boolean enabledLeast = true;

	// Text
	private Text text;

	// Menus
	private JMenu wsMenu;
	private JMenuItem wsNew;
	private JMenuItem wsOpenRecently;

	// Configuration of the software
	private final ConfigStorage configuration;

	// List of listened component to stop listening them
	private LinkedList<AbstractButton> listenedButton = new LinkedList<>();
	private LinkedList<Component> listenedComponent = new LinkedList<>();

	public Window (Text text, ConfigStorage configuration) {
		this(WINDOW_WIDTH, WINDOW_HEIGHT, text, configuration);
	}

	public Window(int width, int height, Text text, ConfigStorage configuration) {
		// Configuration file
		this.configuration = configuration;

		// Language
		this.text = text;

		// Window
		this.setTitle(text.windowTitle());
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null); // center placement

		// Object container
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());

		// Container adding
		this.setContentPane(container);

		// New icon image
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));

		// Menus
		JMenuBar windowMenuBar = new JMenuBar();
		// New working session
		wsMenu = new JMenu(text.workingSessionMenu());
		wsNew = new JMenuItem(text.newWorkingSessionMenuItem());
		wsNew.addActionListener(new NewWorkingSession());
		wsMenu.add(wsNew);
		windowMenuBar.add(wsMenu);
		// Open recent working session
		wsOpenRecently = new JMenu(text.workingSessionOpenRecently());
		if (!configuration.hasWorkingSession()) {
			// No working session so the menu is not accessible
			wsOpenRecently.setEnabled(false);
		} else {
			updateOpenRecentlyMenu();
		}
		wsMenu.add(wsOpenRecently);
	    setJMenuBar(windowMenuBar);

		// Window displaying
		this.setVisible(true);
	}

	public Window(ProvinceStorage provinces, Panel panel, Text text,
			MiniMap miniMap, ConfigStorage configuration) {
		this(WINDOW_WIDTH, WINDOW_HEIGHT, provinces, panel, text, miniMap, configuration);
	}

	public Window(int width, int height, ProvinceStorage provinces, Panel panel,
			Text text, MiniMap miniMap, ConfigStorage configuration) {
		this(width, height, text, configuration);
		loadWorkingSession(provinces, panel, miniMap);
	}

	private void loadWorkingSession(WorkingSession ws) {
		loadWorkingSession(ws.getProvinces(), ws.getPanel(), ws.getMiniMap());
	}

	private void loadWorkingSession(ProvinceStorage provinces, Panel panel, MiniMap miniMap) {
		// Cleaning : removing listener and component which don't need
		for (AbstractButton absButton : listenedButton) {
			for(ActionListener al : absButton.getActionListeners()) {
				absButton.removeActionListener( al );
		    }
		}
		for (Component component : listenedComponent) {
			for (KeyListener kl : component.getKeyListeners()) {
				component.removeKeyListener(kl);
			}
			for (MouseListener ml : component.getMouseListeners()) {
				component.removeMouseListener(ml);
			}
		}
		container.removeAll();

		// Parameter initialization
		this.provinces = provinces;
		this.pan = panel;
		this.miniMap = miniMap;
		miniMap.setWindow(this);

		// Map and mini-map adding
		container.add(pan, BorderLayout.CENTER);
		JPanel east = new JPanel();
		east.setLayout(new GridLayout(2, 1, 5, 5));
		east.add(new JPanel());
		east.add(miniMap);
		container.add(east, BorderLayout.EAST);

		// Text and button adding
		Font police = new Font("Tahoma", Font.BOLD, 14);
		textLabel.setText(text.clickedProvince());
		textLabel.setFont(police);
		textLabel.setForeground(Color.blue);
		resLabel.setFont(police);
		resLabel.setForeground(Color.blue);
		copyButton.setText(text.copyClipboard());
		searchButton.setText(text.provinceSearch());
		JPanel north = new JPanel();
		north.setLayout(new GridLayout(1, 4, 5, 5));
		north.add(textLabel);
		north.add(resLabel);
		north.add(copyButton);
		north.add(searchButton);
		container.add(north, BorderLayout.NORTH);

		// Button action
		copyButton.addActionListener(new BoutonCopierListener());
		listenedButton.add(copyButton);
		searchButton.addActionListener(new SearchButtonListener());
		listenedButton.add(searchButton);

		// Focus on window for the actions, and reset for the button in order to window keep it
		setFocusable(true);
		copyButton.setFocusable(false);
		searchButton.setFocusable(false);

		// Mouse and key listening for actions
		pan.addMouseListener(this);
		listenedComponent.add(pan);
		this.addKeyListener(this);
		listenedComponent.add(this);

		// Window displaying new components
		repaint();
		this.setVisible(true);
	}

	private void updateOpenRecentlyMenu() {
		// Cleaning
		wsOpenRecently.removeAll();
		// All working session except the first are added
		boolean first = true;
		Iterator<WorkingSession> it = configuration.iterator();
		while (it.hasNext()) {
			WorkingSession ws = it.next();
			if (first) {
				first = false;
			} else {
				JMenuItem wsMenuItem = new JMenuItem(ws.getName());
				wsMenuItem.addActionListener(new OpenRecentWorkingSession(ws));
				wsOpenRecently.add(wsMenuItem);
			}
		}
	}

	/**
	 * Locking/unlocking moving actions
	 */
	public void movingActionLockingUnlocking() {
		if (pan.getHeightNumber() == 0) {
			enabledTop = false;
		} else {
			enabledTop = true;
		}
		if (pan.getWidthNumber() == 0) {
			enabledLeft = false;
		} else {
			enabledLeft = true;
		}
		if (pan.getHeightNumber() == pan.getRealHeight()
				/ (pan.getDisplayingRealImageHeight() / 2) - 2) {
			enabledBottom = false;
		} else {
			enabledBottom = true;
		}
		if (pan.getWidthNumber() == pan.getRealWidth()
				/ (pan.getDisplayingRealImageWidth() / 2) - 2) {
			enabledRight = false;
		} else {
			enabledRight = true;
		}
	}

	// ---------------- Mouse actions ---------------------------
	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getX() < pan.getImageWidth()
				&& event.getY() < pan.getImageHeight()
				&& event.getY() > 0) {
			/* Algorithm : calculation of the position relative to the first
			 * displayed pixel. (Subtraction of the first displaying pixel
			 * and division by zoom coefficient).
			 * Finally adding the absolute position of the first displayed pixel.
			 */
			int rgb = pan.getRGB(
					(int)(event.getX() /
							((float)pan.getImageWidth() / (float)pan.getDisplayingRealImageWidth()) + 
							(pan.getWidthNumber() * pan.getDisplayingRealImageWidth() / 2))
							, (int)((event.getY()) /
									((float)pan.getImageHeight() / (float)pan.getDisplayingRealImageHeight()) +
									(pan.getHeightNumber() * pan.getDisplayingRealImageHeight() / 2)));
			/*
			 * Cast because 1/2 = 0 whereas (float)1/(float)2 = 0.5.
			 * Cast in integer after because need integer
			 */
			int r = rgb >> 16 & 0xff;
			int g = rgb >> 8 & 0xff;
			int b = rgb & 0xff;
			Province province = provinces.getProvince(r, g, b);
			if (province != null) {
				// Display province name
				resLabel.setText(province.toString());
				// Flash the province
				pan.getPosition(rgb, 1, true);
			} else {
				resLabel.setText("");
			}
		} else {
			resLabel.setText("");
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

	// ---------------- Key actions ---------------------------
	@Override
	public void keyPressed(KeyEvent event) {
		/* Codes :
		 * 	37 : <-
		 * 	38 : top arrow
		 * 	39 : ->
		 * 	40 : bottom arrow
		 */
		if (event.getKeyCode() == 37) {
			if (enabledLeft)
				actionGauche();
		} else if (event.getKeyCode() == 38) {
			if (enabledTop)
				actionHaut();
		} else if (event.getKeyCode() == 39) {
			if (enabledRight)
				actionDroit();
		} else if (event.getKeyCode() == 40) {
			if (enabledBottom)
				actionBas();
		} else if (event.getKeyChar() == '+') {
			if (enabledMore)
				actionPlus();
		} else if (event.getKeyChar() == '-') {
			if (enabledLeast)
				actionMoins();
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {	 
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

	// ---------------- Moving actions ---------------------------
	/**
	 * Top moving
	 */
	private void actionHaut() {
		pan.heightNumberLeast();
		movingActionLockingUnlocking();
		this.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Bottom moving
	 */
	private void actionBas() {
		pan.heightNumberMore();
		movingActionLockingUnlocking();
		this.repaint();	
		miniMap.setRectangle();
	}

	/**
	 * Left moving
	 */
	private void actionGauche() {
		pan.widthNumberLeast();
		movingActionLockingUnlocking();
		this.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Right moving
	 */
	private void actionDroit() {
		pan.widthNumberMore();
		movingActionLockingUnlocking();
		this.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Zoom more
	 */
	private void actionPlus() {
		pan.zoomMore();
		enabledLeast = true;
		// Block to X256 zoom
		if (pan.getImageWidth() / pan.getDisplayingRealImageWidth() == 256) {
			enabledMore = false;
		}			
		movingActionLockingUnlocking();
		this.repaint();
		miniMap.setRectangle();
	}

	/** 
	 * Zoom least
	 */
	private void actionMoins() {
		if (2 * pan.getDisplayingRealImageWidth() > pan.getRealWidth()
				|| 2 * pan.getDisplayingRealImageHeight() > pan.getRealHeight()) {
			enabledLeast = false;
		} else {
			pan.zoomLeast();
			enabledMore = true;
			movingActionLockingUnlocking();
			this.repaint();
			miniMap.setRectangle();
		}
	}

	// ---------------- Button actions ---------------------------
	class BoutonCopierListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {			
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(resLabel.getText()), null);
		}
	}

	class SearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {			
			SearchDialog searchDialog = new SearchDialog(null, text.provinceSearch(), true, text, provinces, null);
			Province searchProvince = searchDialog.getSearchResult();
			if (searchProvince != null) {
				try {
					// Calculation of province middle
					Point middle = pan.getPosition(searchProvince.getIdentifiantRGB(), 3, false);
					// Multiplying by 4 / pan.getDisplayingRealImageWidth() to have twice width number
					int numLargeur = (int)middle.getX() * 4 / pan.getDisplayingRealImageWidth();
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
					int numHauteur = (int)middle.getY() * 4 / pan.getDisplayingRealImageHeight();
					numHauteur--;
					numHauteur /= 2;
					if (numHauteur >= pan.getRealHeight()/(pan.getDisplayingRealImageHeight()/2) - 2) {
						numHauteur = pan.getRealHeight()/(pan.getDisplayingRealImageHeight()/2) - 2;			
					}
					pan.setHeightNumber(numHauteur);
					// Actualize window (key management + displaying)
					movingActionLockingUnlocking();
					repaint();
					// Actualize mini-map rectangle
					miniMap.setRectangle();
					// Actualize selected province
					resLabel.setText(searchProvince.toString());
				} catch (IllegalArgumentException e) {
					// Province not found
					JOptionPane.showMessageDialog(null, text.provinceNotFound(), text.warningMessage(), JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	// ---------------- Menu actions ----------------------
	class NewWorkingSession implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			WorkingSessionNewDialog newWSDialog = new WorkingSessionNewDialog(null, true, text);
			try {
				WorkingSession newWS = newWSDialog.getWorkingSession();
				if (newWS !=null) {
					// The user defined a working session
					loadWorkingSession(newWS);

					// There is one or more working session
					wsOpenRecently.setEnabled(true);

					// Update the configuration
					configuration.addFirstWorkingSession(newWS);
					configuration.saveConfigFile();
					updateOpenRecentlyMenu();

					// Close the dialog
					newWSDialog.dispose();
				}
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), text.error(), JOptionPane.ERROR_MESSAGE);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, text.fileNotFound("definition.csv"), text.error(), JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, text.fileNotFound("provinces.bmp.csv"), text.error(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class OpenRecentWorkingSession implements ActionListener {
		private WorkingSession ws;

		OpenRecentWorkingSession(WorkingSession ws) {
			this.ws = ws;
		}

		public void actionPerformed(ActionEvent arg0) {
			loadWorkingSession(ws);

			// Update the configuration and the menu
			configuration.becomeFirst(ws);
			configuration.saveConfigFile();
			updateOpenRecentlyMenu();
		}
	}

	// ---------------- Testing ---------------------------
	public int premierPixelX() {
		return FIRST_PIXEL_X;
	}

	public int premierPixelY() {
		return FIRST_PIXEL_Y + textLabel.getHeight();
	}

	public String getRes() {
		return resLabel.getText();
	}
}

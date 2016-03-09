package base;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import config.ConfigStorage;
import config.PreferenceDialog;
import config.WorkingSession;
import config.WorkingSessionNewDialog;
import config.WorkingSessionNewDialogCK;
import crusaderKings2.BaroniesStorage;
import crusaderKings2.Barony;
import text.Text;

/**
 * Graphic window, the body of the software
 * 
 * @author Mouchi
 *
 */
public class Window extends JFrame implements MouseListener {
	// First pixel position
	static private int FIRST_PIXEL_X = 8;
	static private int FIRST_PIXEL_Y = 31;

	// Default window size
	static private int WINDOW_WIDTH = 1024 +  2 * FIRST_PIXEL_X + 256 + 5; 
	// Width margin : 2 FIRST_PIXEL_X for the  2 border + 256 for the mini-map + 5 for a little extra margin
	static private int WINDOW_HEIGHT = 512 + FIRST_PIXEL_Y + 22 + 8;
	// Height margin : FIRST_PIXEL_Y for the top border + 22 for menus  + 8 for the bottom border

	// Map image
	private Panel pan;

	// Mini-map image
	private MiniMap miniMap;

	// Object container
	private JPanel container = new JPanel();

	// Text displaying
	private JLabel resLabel = new JLabel("");
	private JLabel resRLabel = new JLabel("");
	private JLabel resGLabel = new JLabel("");
	private JLabel resBLabel = new JLabel("");
	private JLabel terrainLabel = new JLabel("");
	private JLabel barony1 = new JLabel("");
	private JLabel barony2 = new JLabel("");
	private JLabel barony3 = new JLabel("");
	private JLabel barony4 = new JLabel("");
	private JLabel barony5 = new JLabel("");
	private JLabel barony6 = new JLabel("");
	private JLabel barony7 = new JLabel("");

	// Copy button
	private JButton copyButton = new JButton();
	private JButton copyRGBButton = new JButton();

	// Province database
	private ProvinceStorage provinces;
	private BaroniesStorage baronnies;

	// Action status for navigation (4 moving + 2 zooming actions)
	private boolean enabledTop = false; // because we start in top
	private boolean enabledBottom = true;
	private boolean enabledLeft = false; // because we start in left
	private boolean enabledRight = true;
	private boolean enabledZoomIn = true;
	private boolean enabledZoomOut = true;

	// Text
	private Text text;

	// Menus
	private JMenuItem wsReload;
	private JMenuItem wsInfo;
	private JMenuItem wsOpenRecently;
	private JMenu searchMenu;
	private JMenuItem searchBarony;
	private JMenu navigation;
	private JMenuItem top;
	private JMenuItem left;
	private JMenuItem bottom;
	private JMenuItem right;
	private JMenuItem zoomIn;
	private JMenuItem zoomOut;
	private JMenu options;
	private JMenuItem color;
	private JMenuItem other;
	private boolean init;

	// Configuration of the software
	private final ConfigStorage configuration;

	// Working session configuration
	private boolean CkGame;

	// List of listened component to stop listening them
	private LinkedList<AbstractButton> listenedButton = new LinkedList<>();
	private LinkedList<Component> listenedComponent = new LinkedList<>();

	public Window (Text text, ConfigStorage configuration) {
		this(WINDOW_WIDTH, WINDOW_HEIGHT, text, configuration, true);
	}

	public Window(int width, int height, Text text,
			ConfigStorage configuration, boolean changeLook) {
		// Configuration file
		this.configuration = configuration;

		// Language
		this.text = text;

		// Window
		this.setTitle(text.windowTitle());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		// Object container
		container.setPreferredSize(new Dimension(width, height));
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());

		// Container adding
		this.setContentPane(container);

		// New icon image
		setIconImage(Toolkit.getDefaultToolkit().getImage("ressources/icon.png"));

		// Menus
		JMenuBar windowMenuBar = new JMenuBar();
		// New working session
		JMenu wsMenu = new JMenu(text.workingSessionMenu());
		JMenu wsNew = new JMenu(text.newWorkingSessionMenuItem());
		JMenuItem wsNewCK = new JMenuItem(text.newWSCKMenuItem());
		JMenuItem wsNewOthers = new JMenuItem(text.newWSBasicMenuItem());
		wsNewCK.addActionListener(new NewWorkingSession(true));
		wsNewOthers.addActionListener(new NewWorkingSession(false));
		wsNew.add(wsNewCK);
		wsNew.add(wsNewOthers);
		wsMenu.add(wsNew);
		// Open recent working session
		wsOpenRecently = new JMenu(text.workingSessionOpenRecently());
		if (!configuration.hasWorkingSession()) {
			// No working session so the menu is not accessible
			wsOpenRecently.setEnabled(false);
		} else {
			updateOpenRecentlyMenu();
		}
		wsMenu.add(wsOpenRecently);
		wsReload = new JMenuItem(text.reloadWSMenu());
		wsReload.setVisible(false);
		wsMenu.addSeparator();
		wsMenu.add(wsReload);
		wsInfo = new JMenuItem(text.WSInformation());
		wsInfo.setVisible(false);
		wsMenu.add(wsInfo);
		windowMenuBar.add(wsMenu);
		searchMenu = new JMenu(text.provinceSearch());
		JMenuItem searchID = new JMenuItem(text.searchID());
		JMenuItem searchName = new JMenuItem(text.searchName());
		searchMenu.add(searchID);
		searchMenu.add(searchName);
		searchID.addActionListener(new SearchListener(0));
		searchName.addActionListener(new SearchListener(1));
		searchName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
		searchMenu.setVisible(false);
		windowMenuBar.add(searchMenu);
		navigation = new JMenu(text.navigation());
		top = new JMenuItem(text.moveTop());
		top.setAccelerator(KeyStroke.getKeyStroke(38, 0));
		left = new JMenuItem(text.moveLeft());
		left.setAccelerator(KeyStroke.getKeyStroke(37, 0));
		bottom = new JMenuItem(text.moveBottom());
		bottom.setAccelerator(KeyStroke.getKeyStroke(40, 0));
		right = new JMenuItem(text.moveRight());
		right.setAccelerator(KeyStroke.getKeyStroke(39, 0));
		zoomIn = new JMenuItem(text.zoomIn());
		zoomIn.setAccelerator(KeyStroke.getKeyStroke('+'));
		zoomOut = new JMenuItem(text.zoomOut());
		zoomOut.setAccelerator(KeyStroke.getKeyStroke('-'));
		navigation.add(top);
		navigation.add(left);
		navigation.add(bottom);
		navigation.add(right);
		navigation.addSeparator();
		navigation.add(zoomIn);
		navigation.add(zoomOut);
		navigation.setVisible(false);
		windowMenuBar.add(navigation);
		options = new JMenu(text.preferencesTitle());
		color = new JMenuItem(text.color());
		other = new JMenuItem(text.other());
		options.add(color);
		options.add(other);
		options.setVisible(false);
		windowMenuBar.add(options);
	    setJMenuBar(windowMenuBar);
	    init = false;

		// Use the look and feel of the system for fileChooser
		// Check a boolean in order to have coherence but not no-beautiful waiting bar
	    if (changeLook) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (InstantiationException e) {}
			catch (ClassNotFoundException e) {}
			catch (UnsupportedLookAndFeelException e) {}
			catch (IllegalAccessException e) {}
	    }

		// Window displaying
	    pack();
	    setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public Window(WorkingSession ws, Text text, ConfigStorage configuration) {
		this(WINDOW_WIDTH, WINDOW_HEIGHT, ws, text, configuration);
	}

	public Window(int width, int height, WorkingSession ws,
			Text text, ConfigStorage configuration) {
		this(width, height, text, configuration, false);
		enableMenus(false);
		WaitingProgressBar WaitingBar = new WaitingProgressBar();
		new Thread(WaitingBar).run();
		try {
			ws.initialize();
			loadWorkingSession(ws);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), text.error(), JOptionPane.ERROR_MESSAGE);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, text.fileNotFound(e.getMessage()), text.error(), JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, text.fileNotFound("provinces.bmp"), text.error(), JOptionPane.ERROR_MESSAGE);
		}
		WaitingBar.stop();
		// Use the look and feel of the system for fileChooser
		// Put here in order to have coherence but not no-beautiful waiting bar
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (InstantiationException e) {}
		catch (ClassNotFoundException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		catch (IllegalAccessException e) {}
		enableMenus(true);
	}

	private void loadWorkingSession(WorkingSession ws) {
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
		for(ActionListener al : wsReload.getActionListeners()) {
			wsReload.removeActionListener(al);
	    }
		for(ActionListener al : wsInfo.getActionListeners()) {
			wsInfo.removeActionListener(al);
	    }
		if (searchBarony != null) {
			searchMenu.remove(searchBarony);
			searchBarony = null;
		}
		container.removeAll();

		// Parameter initialization
		this.CkGame = ws.isCKGame();
		this.provinces = ws.getProvinces();
		this.baronnies = ws.getStoredBaronies();
		this.pan = ws.getPanel();
		this.miniMap = ws.getMiniMap();
		miniMap.setWindow(this);
		container.setPreferredSize(new Dimension(pan.getImageWidth() +  2 * FIRST_PIXEL_X + 256 + 5,
				pan.getImageHeight() + FIRST_PIXEL_Y + 22 + 8));

		// Map adding
		container.add(pan, BorderLayout.CENTER);

		// Text of clicked province
		JPanel east = new JPanel(new GridLayout(2, 1, 0, 5));
		// Province name
		Font provinceFont = new Font("Tahoma", Font.BOLD, 14);
		resLabel.setFont(provinceFont);
		resLabel.setForeground(new Color(configuration.preferences.getProvinceR(),
				configuration.preferences.getProvinceG(),
				configuration.preferences.getProvinceB()));
		JPanel resPanel = new JPanel();
		resLabel.setText("");
		resPanel.add(resLabel);
		ImageIcon copyIcon = new ImageIcon("ressources/copy_icon.png");
		copyButton.setIcon(copyIcon);
		copyButton.setPreferredSize(new Dimension(copyIcon.getIconWidth(),
				copyIcon.getIconHeight()));
		copyButton.setToolTipText(text.copyClipboard());
		copyButton.setEnabled(false);
		resPanel.add(copyButton);
		// Province RGB
		resRLabel.setFont(provinceFont);
		resRLabel.setForeground(Color.RED);
		resGLabel.setFont(provinceFont);
		resGLabel.setForeground(Color.GREEN);
		resBLabel.setFont(provinceFont);
		resBLabel.setForeground(Color.BLUE);
		JPanel resRGBPanel = new JPanel();
		resRLabel.setText("");
		resGLabel.setText("");
		resBLabel.setText("");
		resRGBPanel.add(resRLabel);
		resRGBPanel.add(resGLabel);
		resRGBPanel.add(resBLabel);
		ImageIcon copyRGBIcon = new ImageIcon("ressources/copy_icon.png");
		copyRGBButton.setIcon(copyRGBIcon);
		copyRGBButton.setPreferredSize(new Dimension(copyRGBIcon.getIconWidth(),
				copyRGBIcon.getIconHeight()));
		copyRGBButton.setToolTipText(text.copyClipboard());
		copyRGBButton.setEnabled(false);
		JPanel resRGBButtonPanel = new JPanel();
		resRGBButtonPanel.add(resRGBPanel);
		resRGBButtonPanel.add(copyRGBButton);
		JPanel bothResPanel = new JPanel(new GridLayout(2, 1, 0, 5));
		bothResPanel.add(resPanel);
		bothResPanel.add(resRGBButtonPanel);
		bothResPanel.setBorder(BorderFactory.createTitledBorder(text.selectedProvince()));
		// Province baronies or an image
		if (CkGame) {
			eraseBaronyNames();
			JPanel terrainPanel = new JPanel();
			terrainPanel.setBorder(BorderFactory.createTitledBorder("Terrain :"));
			Font terrainFont = new Font("Tahoma", Font.BOLD, 12);
			terrainLabel.setText("");
			terrainLabel.setFont(terrainFont);
			terrainLabel.setForeground(new Color(configuration.preferences.getProvinceR(),
					configuration.preferences.getProvinceG(),
					configuration.preferences.getProvinceB()));
			terrainPanel.add(terrainLabel);
			terrainPanel.setPreferredSize(new Dimension(100, 60));
			JPanel baroniesPanel = new JPanel(new GridLayout(7, 1));
			baroniesPanel.setBorder(BorderFactory.createTitledBorder(text.provinceBaronies()));
			Font baronyFont = new Font("Tahoma", Font.BOLD, 12);
			barony1.setFont(baronyFont);
			barony2.setFont(baronyFont);
			barony3.setFont(baronyFont);
			barony4.setFont(baronyFont);
			barony5.setFont(baronyFont);
			barony6.setFont(baronyFont);
			barony7.setFont(baronyFont);
			baroniesPanel.add(barony1);
			baroniesPanel.add(barony2);
			baroniesPanel.add(barony3);
			baroniesPanel.add(barony4);
			baroniesPanel.add(barony5);
			baroniesPanel.add(barony6);
			baroniesPanel.add(barony7);
			JPanel ckPanel = new JPanel(new BorderLayout());
			ckPanel.add(terrainPanel, BorderLayout.NORTH);
			ckPanel.add(baroniesPanel);
			JPanel northEast = new JPanel(new BorderLayout());
			northEast.add(bothResPanel, BorderLayout.NORTH);
			northEast.add(ckPanel);
			east.add(northEast);
		} else {
			JPanel illustrationPanel = new JPanel();
			JLabel illustrationLabel = new JLabel(new ImageIcon("ressources/Compas_Illustrations.png"));
			illustrationLabel.setPreferredSize(new Dimension(256, 256));
			illustrationPanel.add(illustrationLabel);
			illustrationPanel.setPreferredSize(new Dimension(256, 256));
			JPanel northEast = new JPanel(new BorderLayout());
			northEast.add(bothResPanel, BorderLayout.NORTH);
			northEast.add(illustrationPanel);
			east.add(northEast);
		}

		// Mini-map adding
		east.add(miniMap);
		container.add(east, BorderLayout.EAST);

		// Update search barony menu item
		if (CkGame) {
			searchBarony = new JMenuItem(text.searchBarony());
			searchMenu.add(searchBarony);
			searchBarony.addActionListener(new SearchListener(2));
		}

		// Update reload working session and info menus
		wsReload.addActionListener(new ReloadWorkingSession(ws));
		wsReload.setVisible(true);
		wsInfo.addActionListener(new WSInfoListener(ws));
		wsInfo.setVisible(true);

		// Button action
		copyButton.addActionListener(new BoutonCopierListener());
		listenedButton.add(copyButton);
		copyRGBButton.addActionListener(new BoutonRGBCopierListener());
		listenedButton.add(copyRGBButton);

		// Focus on window for the actions, and reset for the button in order to window keep it
		setFocusable(true);
		copyButton.setFocusable(false);
		copyRGBButton.setFocusable(false);

		// Mouse listening for actions
		pan.addMouseListener(this);
		listenedComponent.add(pan);
		listenedComponent.add(this);

		// Initialize some menus if necessary
		if (!init)
			initializeWSNeededMenu(ws);

		// Window displaying new components
		setTitle(text.windowTitle() + " - " + ws.getName());
		pack();
		setLocationRelativeTo(null);
		repaint();
		this.setVisible(true);
	}

	/**
	 * Update the menu open recently according the configurations
	 */
	private void updateOpenRecentlyMenu() {
		// Cleaning
		wsOpenRecently.removeAll();
		// All working session except the first are added
		wsOpenRecently.setEnabled(configuration.getSize() > 1);
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
	 * Erase the barony names in the window displaying
	 */
	private void eraseBaronyNames() {
		barony1.setText("");
		barony2.setText("");
		barony3.setText("");
		barony4.setText("");
		barony5.setText("");
		barony6.setText("");
		barony7.setText("");
	}

	/**
	 * Get the i-th barony label
	 * @param i
	 * @return
	 */
	private JLabel getBaronyLabel(int i) {
		switch (i) {
		case 1 :
			return barony1;
		case 2 :
			return barony2;
		case 3 :
			return barony3;
		case 4 :
			return barony4;
		case 5 :
			return barony5;
		case 6 :
			return barony6;
		case 7 :
			return barony7;
		default :
			throw new IllegalArgumentException("The " + i + "-th barony doesn't exist");
		}
	}

	/**
	 * Set to i-th barony label the text of the barony
	 * @param i
	 * @param barony
	 */
	private void setBaroniesText(int i, Barony barony) {
		JLabel baronyLabel = getBaronyLabel(i);
		// Remove "b_" and replace the new first letter by its upper case (a->A)
		baronyLabel.setText(barony.toString());
		if (barony.isCastle()) {
			baronyLabel.setText(baronyLabel.getText() + " (" + text.castle() + ")");
			baronyLabel.setForeground(new Color(configuration.preferences.getCastleR(),
					configuration.preferences.getCastleG(), configuration.preferences.getCastleB()));
		} else if (barony.isCity()) {
			baronyLabel.setText(baronyLabel.getText() + " (" + text.city() + ")");
			baronyLabel.setForeground(new Color(configuration.preferences.getCityR(),
					configuration.preferences.getCityG(), configuration.preferences.getCityB()));
		} else if (barony.isTemple()) {
			baronyLabel.setText(baronyLabel.getText() + " (" + text.temple() + ")");
			baronyLabel.setForeground(new Color(configuration.preferences.getTempleR(),
					configuration.preferences.getTempleG(), configuration.preferences.getTempleB()));
		} else if (barony.isTribal()) {
			baronyLabel.setText(baronyLabel.getText() + " (" + text.tribal() + ")");
			baronyLabel.setForeground(new Color(configuration.preferences.getTribalR(),
					configuration.preferences.getTribalG(), configuration.preferences.getTribalB()));
		} else {
			throw new IllegalArgumentException(barony.getBaronyName() + 
					" is not a castle, a city or a temple.");
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

	/**
	 * SetEnabled(enabled) at all the Menus
	 * @param enabled
	 */
	private void enableMenus(boolean enabled) {
		for (int i = 0; i < getJMenuBar().getMenuCount(); i++) {
			getJMenuBar().getMenu(i).setEnabled(enabled);
		}
	}

	class WaitingProgressBar implements Runnable {
		private JDialog wait;

		@Override
		public void run() {
			wait = new JDialog(Window.this, false);
			wait.setUndecorated(true);
			JProgressBar bar = new JProgressBar();
			bar.setPreferredSize(new Dimension(500, 50));
			bar.setIndeterminate(true);
			wait.add(bar);
			wait.pack();
			wait.setLocationRelativeTo(null);
			wait.setVisible(true);
		}

		public void stop() {
			wait.setVisible(false);
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
				resRLabel.setText(r + "");
				resGLabel.setText(g + "");
				resBLabel.setText(b + "");
				terrainLabel.setText(text.terrainType(province.getTerrain()));
				copyButton.setEnabled(true);
				copyRGBButton.setEnabled(true);
				if (CkGame) {
					// Display barony names
					eraseBaronyNames();
					LinkedList<Barony> provinceBaronnies = baronnies.getBaronies(province.getId());
					if (provinceBaronnies != null) {
						int i = 0;
						for (Barony barony : provinceBaronnies) {
							i++;
							setBaroniesText(i, barony);
						}
					}
				}
				// Flash the province
				pan.getPosition(rgb, 1, true);
			} else {
				resLabel.setText("");
				resRLabel.setText("");
				resGLabel.setText("");
				resBLabel.setText("");
				terrainLabel.setText("");
				copyButton.setEnabled(false);
				copyRGBButton.setEnabled(false);
				if (CkGame) {
					eraseBaronyNames();
				}
			}
		} else {
			resLabel.setText("");
			resRLabel.setText("");
			resGLabel.setText("");
			resBLabel.setText("");
			terrainLabel.setText("");
			copyButton.setEnabled(false);
			copyRGBButton.setEnabled(false);
			if (CkGame) {
				eraseBaronyNames();
			}
		}
		container.repaint();
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

	// ---------------- Moving actions ---------------------------
	/**
	 * Top moving
	 */
	private void actionHaut() {
		pan.heightNumberLeast();
		movingActionLockingUnlocking();
		container.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Bottom moving
	 */
	private void actionBas() {
		pan.heightNumberMore();
		movingActionLockingUnlocking();
		container.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Left moving
	 */
	private void actionGauche() {
		pan.widthNumberLeast();
		movingActionLockingUnlocking();
		container.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Right moving
	 */
	private void actionDroit() {
		pan.widthNumberMore();
		movingActionLockingUnlocking();
		container.repaint();
		miniMap.setRectangle();
	}

	/**
	 * Zoom IN
	 */
	private void actionPlus() {
		boolean possibleZoomInNext = pan.zoomIn();
		enabledZoomOut = true;
		// Block to X256 zoom
		if (!possibleZoomInNext || pan.getImageWidth() / pan.getDisplayingRealImageWidth() == 256) {
			enabledZoomIn = false;
		}
		movingActionLockingUnlocking();
		container.repaint();
		miniMap.setRectangle();
	}

	/** 
	 * Zoom OUT
	 */
	private void actionMoins() {
		if (2 * pan.getDisplayingRealImageWidth() > pan.getRealWidth()
				|| 2 * pan.getDisplayingRealImageHeight() > pan.getRealHeight()) {
			enabledZoomOut = false;
		} else {
			pan.zoomOut();
			enabledZoomIn = true;
			movingActionLockingUnlocking();
			container.repaint();
			miniMap.setRectangle();
		}
	}

	/**
	 * Reload the working session
	 * @param ws
	 * @param changePan Change the panel of the working session
	 */
	private void reloadWS(WorkingSession ws, boolean changePan) {
		try {
			if (!ws.isInit()) {
				ws.initialize();
			} else if (changePan) {
				ws.updatePan(configuration.preferences.hasBlackBorder,
						configuration.preferences.removeSeaRiver);
			}
			loadWorkingSession(ws);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), text.error(), JOptionPane.ERROR_MESSAGE);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, text.fileNotFound(e.getMessage()), text.error(), JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, text.fileNotFound("provinces.bmp"), text.error(), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Initializes the menus which need to have a working session
	 */
	private void initializeWSNeededMenu(WorkingSession ws) {
		// Update reload working session menu
		wsReload.addActionListener(new ReloadWorkingSession(ws));
		wsReload.setVisible(true);
		// We can now search in a working session
		searchMenu.setVisible(true);
		// We can now use navigation
		navigation.setVisible(true);
		top.addActionListener(new TopListener());
		left.addActionListener(new LeftListener());
		bottom.addActionListener(new BottomListener());
		right.addActionListener(new RightListener());
		zoomIn.addActionListener(new ZoomInListener());
		zoomOut.addActionListener(new ZoomOutListener());
		// We can now modify preferences
		options.setVisible(true);
		color.addActionListener(new PreferencesListener(0, ws));
		other.addActionListener(new PreferencesListener(1, ws));
		// Now the menus are initialized
		init = true;
	}

	// ---------------- Button actions ---------------------------
	class BoutonCopierListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {			
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(resLabel.getText()), null);
		}
	}
	
	class BoutonRGBCopierListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {			
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
					new StringSelection(resRLabel.getText() + ";" +
							resGLabel.getText() + ";" + resBLabel.getText()), null);
		}
	}

	class SearchListener implements ActionListener {
		private int selectedIndex;

		SearchListener(int selectedIndex) {
			this.selectedIndex = selectedIndex;
		}

		public void actionPerformed(ActionEvent arg0) {			
			SearchDialog searchDialog = new SearchDialog(null, text.provinceSearch(),
					true, text, provinces, selectedIndex, CkGame, baronnies);
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
					container.repaint();
					// Actualize mini-map rectangle
					miniMap.setRectangle();
					// Actualize selected province
					resLabel.setText(searchProvince.toString());
					resRLabel.setText(searchProvince.getR() + "");
					resGLabel.setText(searchProvince.getG() + "");
					resBLabel.setText(searchProvince.getB() + "");
					terrainLabel.setText(text.terrainType(searchProvince.getTerrain()));
					copyButton.setEnabled(true);
					copyRGBButton.setEnabled(true);
					if (CkGame) {
						// Display barony names
						eraseBaronyNames();
						LinkedList<Barony> provinceBaronnies = baronnies.getBaronies(searchProvince.getId());
						if (provinceBaronnies != null) {
							int i = 0;
							for (Barony barony : provinceBaronnies) {
								i++;
								setBaroniesText(i, barony);
							}
						}
					}
				} catch (IllegalArgumentException e) {
					// Province not found
					JOptionPane.showMessageDialog(null, text.provinceNotFound(), text.warningMessage(), JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	// ---------------- Menu actions ----------------------
	class NewWorkingSession implements ActionListener {
		private boolean ckGame;

		public NewWorkingSession(boolean ckGame) {
			this.ckGame = ckGame;
		}

		public void actionPerformed(ActionEvent arg0) {
			WorkingSessionNewDialog newWSDialog;
			if (ckGame) {
				newWSDialog = new WorkingSessionNewDialogCK(null, true, text,
						configuration.preferences.hasBlackBorder,
						configuration.preferences.removeSeaRiver);
			} else {
				newWSDialog = new WorkingSessionNewDialog(null, true, text,
						configuration.preferences.hasBlackBorder);
			}
			try {
				WorkingSession newWS = newWSDialog.getWorkingSession();
				if (newWS !=null) {
					// The user defined a working session
					loadWorkingSession(newWS);

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
				JOptionPane.showMessageDialog(null, text.fileNotFound(e.getMessage()), text.error(), JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, text.fileNotFound("provinces.bmp"), text.error(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class OpenRecentWorkingSession implements ActionListener {
		private WorkingSession ws;

		OpenRecentWorkingSession(WorkingSession ws) {
			this.ws = ws;
		}

		public void actionPerformed(ActionEvent arg0) {
			try {
				if (!ws.isInit()) {
					ws.initialize();
				}
				loadWorkingSession(ws);

				// Update the configuration and the menu
				configuration.becomeFirst(ws);
				configuration.saveConfigFile();
				updateOpenRecentlyMenu();
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), text.error(), JOptionPane.ERROR_MESSAGE);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, text.fileNotFound(e.getMessage()), text.error(), JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, text.fileNotFound("provinces.bmp"), text.error(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class ReloadWorkingSession implements ActionListener {
		private WorkingSession ws;

		ReloadWorkingSession(WorkingSession ws) {
			this.ws = ws;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			reloadWS(ws, false);
		}
	}

	class PreferencesListener implements ActionListener {
		private int selectedIndex;
		private WorkingSession ws;

		PreferencesListener(int selectedIndex, WorkingSession ws) {
			this.selectedIndex = selectedIndex;
			this.ws = ws;
		}

		public void actionPerformed(ActionEvent arg0) {
			PreferenceDialog dialog = new PreferenceDialog(Window.this, text.preferencesTitle(),
					true, text, selectedIndex, configuration.preferences);
			if (dialog.setUserPreferences()) {
				reloadWS(ws, true);
			} else {
				// We reload the color of the result label
				resLabel.setForeground(new Color(configuration.preferences.getProvinceR(),
						configuration.preferences.getProvinceG(),
						configuration.preferences.getProvinceB()));
				terrainLabel.setForeground(new Color(configuration.preferences.getProvinceR(),
						configuration.preferences.getProvinceG(),
						configuration.preferences.getProvinceB()));
				// Reset the selection informations
				resLabel.setText("");
				resRLabel.setText("");
				resGLabel.setText("");
				resBLabel.setText("");
				terrainLabel.setText("");
				copyButton.setEnabled(false);
				copyRGBButton.setEnabled(false);
				if (CkGame) {
					eraseBaronyNames();
				}
			}
			// Save the preferences
			configuration.saveConfigFile();
		}
	}

	class ZoomInListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabledZoomIn)
				actionPlus();
		}
	}

	class ZoomOutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabledZoomOut)
				actionMoins();
		}
	}

	class TopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabledTop)
				actionHaut();
		}
	}

	class LeftListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabledLeft)
				actionGauche();
		}
	}

	class BottomListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabledBottom)
				actionBas();
		}
	}

	class RightListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabledRight)
				actionDroit();
		}
	}

	class WSInfoListener implements ActionListener {
		private WorkingSession ws;

		public WSInfoListener(WorkingSession ws) {
			this.ws = ws;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = text.workingSessionName() + " : " + ws.getName() + "\n"
					+ text.gameDirectory() + " : " + ws.getGameDirectory() + "\n";
			if (ws.getMapModDirectory() != null)
					message += text.mapModDirectory() + " : " + ws.getMapModDirectory() + "\n";
			if (!ws.getModDirectories().isEmpty())
				for (String modDir : ws.getModDirectories()) {
					message += text.provincesModDirectory() + " : " + modDir; 
				}					
			JOptionPane.showMessageDialog(null, message, text.WSInformation(), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// ---------------- Testing ---------------------------
	public int premierPixelX() {
		return FIRST_PIXEL_X;
	}

	public int premierPixelY() {
		return FIRST_PIXEL_Y;
	}

	public String getRes() {
		return resLabel.getText();
	}
}

package config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import text.Text;

public class PreferenceDialog extends JDialog {
	private Text text;
	private Preferences preferences;
	private boolean reloadWSNeeded;

	private JFormattedTextField provinceRColorTF;
	private JFormattedTextField provinceGColorTF;
	private JFormattedTextField provinceBColorTF;
	private int provinceRColor;
	private int provinceGColor;
	private int provinceBColor;
	private JLabel provinceColorResult;

	private JFormattedTextField castleRColorTF;
	private JFormattedTextField castleGColorTF;
	private JFormattedTextField castleBColorTF;
	private int castleRColor;
	private int castleGColor;
	private int castleBColor;
	private JLabel castleColorResult;

	private JFormattedTextField cityRColorTF;
	private JFormattedTextField cityGColorTF;
	private JFormattedTextField cityBColorTF;
	private int cityRColor;
	private int cityGColor;
	private int cityBColor;
	private JLabel cityColorResult;

	private JFormattedTextField templeRColorTF;
	private JFormattedTextField templeGColorTF;
	private JFormattedTextField templeBColorTF;
	private int templeRColor;
	private int templeGColor;
	private int templeBColor;
	private JLabel templeColorResult;

	private JFormattedTextField tribalRColorTF;
	private JFormattedTextField tribalGColorTF;
	private JFormattedTextField tribalBColorTF;
	private int tribalRColor;
	private int tribalGColor;
	private int tribalBColor;
	private JLabel tribalColorResult;
	
	private JRadioButton french;
	private JCheckBox blackBorder;

	public PreferenceDialog(JFrame parent, String title, boolean modal,
			Text text, int selectedIndex, Preferences preferences) {
		super(parent, title, modal);
		this.text = text;
		this.preferences = preferences;
		reloadWSNeeded = false;
		setSize(400, 450);
		setLocationRelativeTo(null);
		setResizable(false);
		initComponent(selectedIndex);
	}

	/**
	 * Set the preferences chosen by the user
	 * @return if we must reload the working session
	 */
	public boolean setUserPreferences() {
		// User can now interact with the dialog box
		setVisible(true);
		// We reload if the black border change
		reloadWSNeeded = (preferences.hasBlackBorder && !blackBorder.isSelected())
				|| (!preferences.hasBlackBorder && blackBorder.isSelected());
		// Set the user preferences
		preferences.setProvinceRGB(provinceRColor, provinceGColor, provinceBColor);
		preferences.setCastleRGB(castleRColor, castleGColor, castleBColor);
		preferences.setCityRGB(cityRColor, cityGColor, cityBColor);
		preferences.setTempleRGB(templeRColor, templeGColor, templeBColor);
		preferences.setTribalRGB(tribalRColor, tribalGColor, tribalBColor);
		preferences.isFrench = french.isSelected();
		preferences.hasBlackBorder = blackBorder.isSelected();
		preferences.isFrench = french.isSelected();
		return reloadWSNeeded;
	}

	private void initComponent(int selectedIndex) {
		JPanel container = new JPanel(new BorderLayout());

		// Color preferences
		JPanel colorPan = new JPanel(new GridLayout(6, 1, 5, 5));

		// Province color preferences
		JPanel provinceColor = new JPanel(new GridLayout(1, 4, 5, 5));
		JPanel provinceRColorPan = new JPanel();
		JLabel provinceRColorLab = new JLabel("R:");
		provinceRColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		provinceRColor = preferences.getProvinceR();
		provinceRColorTF.setValue(Long.valueOf(provinceRColor));
		provinceRColorTF.setPreferredSize(new Dimension(30, 30));
		provinceRColorTF.addKeyListener(new ColorKeyListener());
		provinceRColorPan.add(provinceRColorLab);
		provinceRColorPan.add(provinceRColorTF);
		JPanel provinceGColorPan = new JPanel();
		JLabel provinceGColorLab = new JLabel("G:");
		provinceGColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		provinceGColor = preferences.getProvinceG();
		provinceGColorTF.setValue(Long.valueOf(provinceGColor));
		provinceGColorTF.setPreferredSize(new Dimension(30, 30));
		provinceGColorTF.addKeyListener(new ColorKeyListener());
		provinceGColorPan.add(provinceGColorLab);
		provinceGColorPan.add(provinceGColorTF);
		JPanel provinceBColorPan = new JPanel();
		JLabel provinceBColorLab = new JLabel("B:");
		provinceBColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		provinceBColor = preferences.getProvinceB();
		provinceBColorTF.setValue(Long.valueOf(provinceBColor));
		provinceBColorTF.setPreferredSize(new Dimension(30, 30));
		provinceBColorTF.addKeyListener(new ColorKeyListener());
		provinceBColorPan.add(provinceBColorLab);
		provinceBColorPan.add(provinceBColorTF);
		provinceColorResult = new JLabel();
		provinceColorResult.setBackground(new Color(preferences.getProvinceRGB()));
		provinceColorResult.setOpaque(true);
		provinceColorResult.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		provinceColor.add(provinceRColorPan);
		provinceColor.add(provinceGColorPan);
		provinceColor.add(provinceBColorPan);
		provinceColor.add(provinceColorResult);
		provinceColor.setBorder(BorderFactory.createTitledBorder(text.provinceColor()));
		colorPan.add(provinceColor);

		// Castle color preferences
		JPanel castleColor = new JPanel(new GridLayout(1, 4, 5, 5));
		JPanel castleRColorPan = new JPanel();
		JLabel castleRColorLab = new JLabel("R:");
		castleRColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		castleRColor = preferences.getCastleR();
		castleRColorTF.setValue(Long.valueOf(castleRColor));
		castleRColorTF.setPreferredSize(new Dimension(30, 30));
		castleRColorTF.addKeyListener(new ColorKeyListener());
		castleRColorPan.add(castleRColorLab);
		castleRColorPan.add(castleRColorTF);
		JPanel castleGColorPan = new JPanel();
		JLabel castleGColorLab = new JLabel("G:");
		castleGColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		castleGColor = preferences.getCastleG();
		castleGColorTF.setValue(Long.valueOf(castleGColor));
		castleGColorTF.setPreferredSize(new Dimension(30, 30));
		castleGColorTF.addKeyListener(new ColorKeyListener());
		castleGColorPan.add(castleGColorLab);
		castleGColorPan.add(castleGColorTF);
		JPanel castleBColorPan = new JPanel();
		JLabel castleBColorLab = new JLabel("B:");
		castleBColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		castleBColor = preferences.getCastleB();
		castleBColorTF.setValue(Long.valueOf(castleBColor));
		castleBColorTF.setPreferredSize(new Dimension(30, 30));
		castleBColorTF.addKeyListener(new ColorKeyListener());
		castleBColorPan.add(castleBColorLab);
		castleBColorPan.add(castleBColorTF);
		castleColorResult = new JLabel();
		castleColorResult.setBackground(new Color(preferences.getCastleRGB()));
		castleColorResult.setOpaque(true);
		castleColorResult.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		castleColor.add(castleRColorPan);
		castleColor.add(castleGColorPan);
		castleColor.add(castleBColorPan);
		castleColor.add(castleColorResult);
		castleColor.setBorder(BorderFactory.createTitledBorder(text.castleColor()));
		colorPan.add(castleColor);

		// City color preferences
		JPanel cityColor = new JPanel(new GridLayout(1, 4, 5, 5));
		JPanel cityRColorPan = new JPanel();
		JLabel cityRColorLab = new JLabel("R:");
		cityRColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		cityRColor = preferences.getCityR();
		cityRColorTF.setValue(Long.valueOf(cityRColor));
		cityRColorTF.setPreferredSize(new Dimension(30, 30));
		cityRColorTF.addKeyListener(new ColorKeyListener());
		cityRColorPan.add(cityRColorLab);
		cityRColorPan.add(cityRColorTF);
		JPanel cityGColorPan = new JPanel();
		JLabel cityGColorLab = new JLabel("G:");
		cityGColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		cityGColor = preferences.getCityG();
		cityGColorTF.setValue(Long.valueOf(cityGColor));
		cityGColorTF.setPreferredSize(new Dimension(30, 30));
		cityGColorTF.addKeyListener(new ColorKeyListener());
		cityGColorPan.add(cityGColorLab);
		cityGColorPan.add(cityGColorTF);
		JPanel cityBColorPan = new JPanel();
		JLabel cityBColorLab = new JLabel("B:");
		cityBColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		cityBColor = preferences.getCityB();
		cityBColorTF.setValue(Long.valueOf(cityBColor));
		cityBColorTF.setPreferredSize(new Dimension(30, 30));
		cityBColorTF.addKeyListener(new ColorKeyListener());
		cityBColorPan.add(cityBColorLab);
		cityBColorPan.add(cityBColorTF);
		cityColorResult = new JLabel();
		cityColorResult.setBackground(new Color(preferences.getCityRGB()));
		cityColorResult.setOpaque(true);
		cityColorResult.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		cityColor.add(cityRColorPan);
		cityColor.add(cityGColorPan);
		cityColor.add(cityBColorPan);
		cityColor.add(cityColorResult);
		cityColor.setBorder(BorderFactory.createTitledBorder(text.cityColor()));
		colorPan.add(cityColor);

		// Temple color preferences
		JPanel templeColor = new JPanel(new GridLayout(1, 4, 5, 5));
		JPanel templeRColorPan = new JPanel();
		JLabel templeRColorLab = new JLabel("R:");
		templeRColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		templeRColor = preferences.getTempleR();
		templeRColorTF.setValue(Long.valueOf(templeRColor));
		templeRColorTF.setPreferredSize(new Dimension(30, 30));
		templeRColorTF.addKeyListener(new ColorKeyListener());
		templeRColorPan.add(templeRColorLab);
		templeRColorPan.add(templeRColorTF);
		JPanel templeGColorPan = new JPanel();
		JLabel templeGColorLab = new JLabel("G:");
		templeGColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		templeGColor = preferences.getTempleG();
		templeGColorTF.setValue(Long.valueOf(templeGColor));
		templeGColorTF.setPreferredSize(new Dimension(30, 30));
		templeGColorTF.addKeyListener(new ColorKeyListener());
		templeGColorPan.add(templeGColorLab);
		templeGColorPan.add(templeGColorTF);
		JPanel templeBColorPan = new JPanel();
		JLabel templeBColorLab = new JLabel("B:");
		templeBColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		templeBColor = preferences.getTempleB();
		templeBColorTF.setValue(Long.valueOf(templeBColor));
		templeBColorTF.setPreferredSize(new Dimension(30, 30));
		templeBColorTF.addKeyListener(new ColorKeyListener());
		templeBColorPan.add(templeBColorLab);
		templeBColorPan.add(templeBColorTF);
		templeColorResult = new JLabel();
		templeColorResult.setBackground(new Color(preferences.getTempleRGB()));
		templeColorResult.setOpaque(true);
		templeColorResult.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		templeColor.add(templeRColorPan);
		templeColor.add(templeGColorPan);
		templeColor.add(templeBColorPan);
		templeColor.add(templeColorResult);
		templeColor.setBorder(BorderFactory.createTitledBorder(text.templeColor()));
		colorPan.add(templeColor);

		// Temple color preferences
		JPanel tribalColor = new JPanel(new GridLayout(1, 4, 5, 5));
		JPanel tribalRColorPan = new JPanel();
		JLabel tribalRColorLab = new JLabel("R:");
		tribalRColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		tribalRColor = preferences.getTribalR();
		tribalRColorTF.setValue(Long.valueOf(tribalRColor));
		tribalRColorTF.setPreferredSize(new Dimension(30, 30));
		tribalRColorTF.addKeyListener(new ColorKeyListener());
		tribalRColorPan.add(tribalRColorLab);
		tribalRColorPan.add(tribalRColorTF);
		JPanel tribalGColorPan = new JPanel();
		JLabel tribalGColorLab = new JLabel("G:");
		tribalGColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		tribalGColor = preferences.getTribalG();
		tribalGColorTF.setValue(Long.valueOf(tribalGColor));
		tribalGColorTF.setPreferredSize(new Dimension(30, 30));
		tribalGColorTF.addKeyListener(new ColorKeyListener());
		tribalGColorPan.add(tribalGColorLab);
		tribalGColorPan.add(tribalGColorTF);
		JPanel tribalBColorPan = new JPanel();
		JLabel tribalBColorLab = new JLabel("B:");
		tribalBColorTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
		tribalBColor = preferences.getTribalB();
		tribalBColorTF.setValue(Long.valueOf(tribalBColor));
		tribalBColorTF.setPreferredSize(new Dimension(30, 30));
		tribalBColorTF.addKeyListener(new ColorKeyListener());
		tribalBColorPan.add(tribalBColorLab);
		tribalBColorPan.add(tribalBColorTF);
		tribalColorResult = new JLabel();
		tribalColorResult.setBackground(new Color(preferences.getTribalRGB()));
		tribalColorResult.setOpaque(true);
		tribalColorResult.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		tribalColor.add(tribalRColorPan);
		tribalColor.add(tribalGColorPan);
		tribalColor.add(tribalBColorPan);
		tribalColor.add(tribalColorResult);
		tribalColor.setBorder(BorderFactory.createTitledBorder(text.tribalColor()));
		colorPan.add(tribalColor);

		// Close button
		JButton closeProvinceButton = new JButton(text.close());
		closeProvinceButton.addActionListener(new CloseButtonListener());
		JPanel closeProvincePanel = new JPanel();
		closeProvincePanel.add(closeProvinceButton);
		colorPan.add(closeProvincePanel);

		// Other pane
		JPanel otherPan = new JPanel(new GridLayout(3, 1, 5, 5));
		// Language
		JLabel languageLabel1 = new JLabel(text.languageChange().getFirst());
		JLabel languageLabel2 = new JLabel(text.languageChange().getLast());
		JRadioButton english = new JRadioButton(text.englishLanguageName());
		french = new JRadioButton(text.frenchLanguageName());
		ButtonGroup language = new ButtonGroup();
		if (text.isFrenchLanguage()) {
			french.setSelected(true);
		} else if (text.isEnglishLanguage()) {
			english.setSelected(true);
		}
		language.add(english);
		language.add(french);
		JPanel languagePan = new JPanel();
		languagePan.setLayout(new GridLayout(4, 1, 5, 5));
		languagePan.add(languageLabel1);
		languagePan.add(languageLabel2);
		languagePan.add(english);
		languagePan.add(french);
		languagePan.setBorder(BorderFactory.createTitledBorder(text.language()));
		otherPan.add(languagePan);
		// Black border
		JPanel blackBorderPanel = new JPanel();
		blackBorder = new JCheckBox(text.blackBorder());
		blackBorder.setSelected(preferences.hasBlackBorder);
		blackBorderPanel.add(blackBorder);
		blackBorderPanel.setBorder(BorderFactory.createTitledBorder(""));
		otherPan.add(blackBorderPanel);

		// Close button
		JButton closeOtherButton = new JButton(text.close());
		closeOtherButton.addActionListener(new CloseButtonListener());
		JPanel closeOtherPanel = new JPanel();
		closeOtherPanel.add(closeOtherButton);
		otherPan.add(closeOtherPanel);

		// Present the different preferences in tabbed pane
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add(text.color(), colorPan);
		tabPane.add(text.other(), otherPan);
		tabPane.setSelectedIndex(selectedIndex);
		container.add(tabPane);

		setContentPane(container);
	}

	private int checkColor(JFormattedTextField tf, int previousVal) {
		int col = ((Long)tf.getValue()).intValue();
		if (col >= 0 && col < 256) {
			return col;
		} else {
			tf.setValue(Long.valueOf(previousVal));
			return previousVal;
		}
	}

	class ColorKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			provinceRColor = checkColor(provinceRColorTF, provinceRColor);
			provinceGColor = checkColor(provinceGColorTF, provinceGColor);
			provinceBColor = checkColor(provinceBColorTF, provinceBColor);
			provinceColorResult.setBackground(new Color(provinceRColor, provinceGColor, provinceBColor));
			castleRColor = checkColor(castleRColorTF, castleRColor);
			castleGColor = checkColor(castleGColorTF, castleGColor);
			castleBColor = checkColor(castleBColorTF, castleBColor);
			castleColorResult.setBackground(new Color(castleRColor, castleGColor, castleBColor));
			cityRColor = checkColor(cityRColorTF, cityRColor);
			cityGColor = checkColor(cityGColorTF, cityGColor);
			cityBColor = checkColor(cityBColorTF, cityBColor);
			cityColorResult.setBackground(new Color(cityRColor, cityGColor, cityBColor));
			templeRColor = checkColor(templeRColorTF, templeRColor);
			templeGColor = checkColor(templeGColorTF, templeGColor);
			templeBColor = checkColor(templeBColorTF, templeBColor);
			templeColorResult.setBackground(new Color(templeRColor, templeGColor, templeBColor));
			tribalRColor = checkColor(tribalRColorTF, tribalRColor);
			tribalGColor = checkColor(tribalGColorTF, tribalGColor);
			tribalBColor = checkColor(tribalBColorTF, tribalBColor);
			tribalColorResult.setBackground(new Color(tribalRColor, tribalGColor, tribalBColor));
		}

		@Override
		public void keyTyped(KeyEvent e) {}
	}

	class CloseButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			setVisible(false);
		}
	}
}

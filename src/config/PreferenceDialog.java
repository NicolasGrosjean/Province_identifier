package config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

	public PreferenceDialog(JFrame parent, String title, boolean modal,
			Text text, int selectedIndex, Preferences preferences) {
		super(parent, title, modal);
		this.text = text;
		this.preferences = preferences;
		reloadWSNeeded = false;
		setSize(400, 370);
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
		return reloadWSNeeded;
	}

	private void initComponent(int selectedIndex) {
		JPanel container = new JPanel(new BorderLayout());

		// Color preferences
		JPanel colorPan = new JPanel(new GridLayout(4, 1, 5, 5));
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
		provinceBColor = preferences.getProvinceG();
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
		// City color preferences
		// Temple color preferences

		// Present the different search means in tabbed pane
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add(text.color(), colorPan);
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
			// TODO add background for others
		}

		@Override
		public void keyTyped(KeyEvent e) {}
	}
}

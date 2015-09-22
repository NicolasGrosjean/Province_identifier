package base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import others.StringComboBoxModel;
import crusaderKings2.BaroniesStorage;
import crusaderKings2.Barony;
import text.Text;

/**
 * Dialog box/window to search a province by its id or its name
 * 
 * @author Mouchi
 *
 */
public class SearchDialog extends JDialog {
	private Text text;
	private ProvinceStorage provinces;
	private Province searchedProvince;
	private JFormattedTextField idReader;
	private JTextField nameReader;
	private LinkedList<Province> nearestProvinces;
	private JButton nameSelectorButton;
	private JComboBox<String> provinceNameList;
	private BaroniesStorage baronnies;
	private JTextField baronyReader;
	private LinkedList<Barony> nearestBaronies;
	private JButton baronySelectorButton;
	private JComboBox<String> baronyNameList;

	public SearchDialog(JFrame parent, String title, boolean modal, Text text,
			ProvinceStorage provinces, int selectedIndex, boolean ckGame,
			BaroniesStorage baronnies) {
		super(parent, title, modal);
		this.text = text;
		this.provinces = provinces;
		this.baronnies = baronnies;
		setSize(400, 250);
		setLocationRelativeTo(null);
		setResizable(false);
		initComponent(selectedIndex, ckGame);
	}

	public Province getSearchResult() {
		// User can now interact with the dialog box
		setVisible(true);
		// When user stops use it (when isVisible == false), we return his Province choice
		return searchedProvince;
	}
	private void initComponent(int selectedIndex, boolean ckGame){
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// Bloc for ID province search
		JPanel idPaneSearch = new JPanel();
		JLabel idProvinceSearchInfo = new JLabel(text.idProvinceSearchLabel());
		idReader = new JFormattedTextField(NumberFormat.getIntegerInstance());
		idReader.addActionListener(new IdSearchButtonListener());
		JButton idSearchButton = new JButton(text.idProvinceSearchButton());
		idSearchButton.addActionListener(new IdSearchButtonListener());
		idPaneSearch.setLayout(new GridLayout(3, 1, 5, 5));
		idPaneSearch.add(idProvinceSearchInfo);
		idPaneSearch.add(idReader);
		idPaneSearch.add(idSearchButton);
		// The bloc occupy only a little part of the space
		JPanel newIdPaneSearch = new JPanel();
		newIdPaneSearch.setLayout(new GridLayout(2, 1, 5, 5));
		newIdPaneSearch.add(idPaneSearch);

		// Bloc for name province search
		JPanel namePaneSearch = new JPanel();
		JLabel nameProvinceSearchInfo = new JLabel(text.nameProvinceSearchLabel());
		nameReader = new JTextField();
		nameReader.addKeyListener(new NameTextFieldListener()); // To have only letters
		nameReader.addActionListener(new NameSearchButtonListener()); // To have same effect of the button with "Enter"
		JButton nameSearchButton = new JButton(text.nameProvinceSearchButton());
		nameSearchButton.addActionListener(new NameSearchButtonListener());
		namePaneSearch.setLayout(new GridLayout(5, 1, 5, 5));
		namePaneSearch.add(nameProvinceSearchInfo);
		namePaneSearch.add(nameReader);
		namePaneSearch.add(nameSearchButton);

		// Bloc for province name search result
		provinceNameList = new JComboBox<String>();
		namePaneSearch.add(provinceNameList);
		provinceNameList.setVisible(false);

		nameSelectorButton = new JButton(text.nameProvinceSelectionButton());
		nameSelectorButton.addActionListener(new NameSelectorButtonListener());
		namePaneSearch.add(nameSelectorButton);
		// No visible because we don't know the search province name
		nameSelectorButton.setVisible(false);

		// Bloc for province search by baronies
		JPanel baronyPaneSearch = null;
		if (ckGame) {
			baronyPaneSearch = new JPanel();
			JLabel baronyProvinceSearchInfo = new JLabel(text.baronyProvinceSearchLabel());
			baronyReader = new JTextField();
			baronyReader.addKeyListener(new NameTextFieldListener()); // To have only letters
			baronyReader.addActionListener(new BaronySearchButtonListener()); // To have same effect of the button with "Enter"
			JButton baronySearchButton = new JButton(text.baronyProvinceSearchButton());
			baronySearchButton.addActionListener(new BaronySearchButtonListener());
			baronyPaneSearch.setLayout(new GridLayout(5, 1, 5, 5));
			baronyPaneSearch.add(baronyProvinceSearchInfo);
			baronyPaneSearch.add(baronyReader);
			baronyPaneSearch.add(baronySearchButton);

			// Bloc for province name search result
			baronyNameList = new JComboBox<String>();
			baronyPaneSearch.add(baronyNameList);
			baronySelectorButton = new JButton(text.baronyProvinceSelectionButton());
			baronySelectorButton.addActionListener(new BaronySelectorButtonListener());
			baronyPaneSearch.add(baronySelectorButton);
			// No visible because we don't know the search province name
			baronyNameList.setVisible(false);
			baronySelectorButton.setVisible(false);
		}

		// Present the different search means in tabbed pane
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add(text.searchID(), newIdPaneSearch);
		tabPane.add(text.searchName(), namePaneSearch);
		if (ckGame) {
			tabPane.add(text.searchBarony(), baronyPaneSearch);
		}
		tabPane.setSelectedIndex(selectedIndex);
		container.add(tabPane);

		// Bloc for cancel
		JButton cancelButton = new JButton(text.cancelButton());
		cancelButton.addActionListener(new CancelButtonListener());
		container.add(cancelButton, BorderLayout.SOUTH);

		setContentPane(container);
	}

	/**
	 * Remove no letter character from searchName
	 */
	private void checkNameReader() {
		int i = 0;
		while (i < nameReader.getText().length()) {
			if (nameReader.getText().codePointAt(i) < 65 ||
					(nameReader.getText().codePointAt(i) > 90 &&
							nameReader.getText().codePointAt(i) < 97) ||
					(nameReader.getText().codePointAt(i) > 122 &&
							nameReader.getText().codePointAt(i) < 192) ||
					(nameReader.getText().codePointAt(i) > 696)) {
				// In valid char, it will be delete
				nameReader.setText(nameReader.getText().replace(String.valueOf(nameReader.getText().charAt(i)), ""));
			} else {
				// Next char to check
				i++;
			}
		}
	}

	// ---------------- Button Actions ---------------------------
	class IdSearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// The searched province is the province with the id typed by the user
			try {
				searchedProvince = provinces.getProvince(((Long)idReader.getValue()).intValue());
				// End of the dialogue
				setVisible(false);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, text.enterIdPlease(), text.warningMessage(), JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	class NameSearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			String searchName = nameReader.getText();
			if (searchName.equals("")) {
				JOptionPane.showMessageDialog(null, text.enterProvinceNamePlease(), text.warningMessage(), JOptionPane.WARNING_MESSAGE);
			} else {
				// Put the province names ordered by nearest to the name typed by the user
				nearestProvinces = provinces.nearestProvinces(searchName, -1);
				LinkedList<String> provinceNames = new LinkedList<String>();
				for (Province p : nearestProvinces) {
					if (!p.getName().equals("")) {
						provinceNames.addLast(p.toString());
					}
				}
				provinceNameList.setModel(new StringComboBoxModel(provinceNames));
				provinceNameList.setSelectedIndex(0);
				provinceNameList.setVisible(true);
				nameSelectorButton.setVisible(true);
			}
		}
	}

	class NameSelectorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// The searched province is the province selected by the user
			searchedProvince = nearestProvinces.get(provinceNameList.getSelectedIndex());

			// End of the dialogue
			setVisible(false);
		}
	}

	class BaronySearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			String searchName = baronyReader.getText();
			if (searchName.equals("")) {
				JOptionPane.showMessageDialog(null, text.enterBaronyNamePlease(), text.warningMessage(), JOptionPane.WARNING_MESSAGE);
			} else {
				// Put the barony names ordered by nearest to the name typed by the user
				nearestBaronies = baronnies.nearestBaronies(searchName, -1);
				LinkedList<String> baronyNames = new LinkedList<String>();
				for (Barony b : nearestBaronies) {
						baronyNames.addLast(b.toString());
				}
				baronyNameList.setModel(new StringComboBoxModel(baronyNames));
				baronyNameList.setSelectedIndex(0);
				baronyNameList.setVisible(true);
				baronySelectorButton.setVisible(true);
			}
		}
	}

	class BaronySelectorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// The searched province is the province selected by the user
			searchedProvince = provinces.getProvince(nearestBaronies.get(baronyNameList.getSelectedIndex()).getProvinceID());

			// End of the dialogue
			setVisible(false);
		}
	}

	class CancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// End of the dialogue
			setVisible(false);
		}
	}

	// ---------------- Keyboard Actions ---------------------------
	class NameTextFieldListener implements KeyListener{
		@Override
		public void keyPressed(KeyEvent event) {
			// nothing
		}
		@Override
		public void keyReleased(KeyEvent event) {
			checkNameReader();
		}

		@Override
		public void keyTyped(KeyEvent event) {
			// nothing
		}
	}
}

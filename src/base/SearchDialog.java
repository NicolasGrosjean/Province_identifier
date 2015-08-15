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
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

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
	private JPanel bottomNameProvinceSearch;
	private JRadioButton firstProvince;
	private JRadioButton secondProvince;
	private JRadioButton thirdProvince;
	private JRadioButton fourthProvince;
	private JRadioButton fifthProvince;
	private BaroniesStorage baronnies;
	private JTextField baronyReader;
	private LinkedList<Barony> nearestBaronies;
	private JPanel bottomNameBaronySearch;
	private JRadioButton firstBarony;
	private JRadioButton secondBarony;
	private JRadioButton thirdBarony;	
	private JRadioButton fourthBarony;
	private JRadioButton fifthBarony;

	public SearchDialog(JFrame parent, String title, boolean modal, Text text,
			ProvinceStorage provinces, int selectedIndex, boolean ckGame,
			BaroniesStorage baronnies) {
		super(parent, title, modal);
		this.text = text;
		this.provinces = provinces;
		this.baronnies = baronnies;
		setSize(400, 370);
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
		JButton nameSearchButton = new JButton(text.nameProvinceSearchButton());
		nameSearchButton.addActionListener(new NameSearchButtonListener());
		namePaneSearch.setLayout(new GridLayout(4, 1, 5, 5));
		namePaneSearch.add(nameProvinceSearchInfo);
		namePaneSearch.add(nameReader);
		namePaneSearch.add(nameSearchButton);
		JPanel newNamePaneSearch = new JPanel();
		bottomNameProvinceSearch = new JPanel();

		// Bloc for province name search result
		firstProvince = new JRadioButton();
		secondProvince = new JRadioButton();
		thirdProvince = new JRadioButton();
		fourthProvince = new JRadioButton();
		fifthProvince = new JRadioButton();
		firstProvince.setSelected(true);
		ButtonGroup bg = new ButtonGroup();
		bg.add(firstProvince);
		bg.add(secondProvince);
		bg.add(thirdProvince);
		bg.add(fourthProvince);
		bg.add(fifthProvince);
		bottomNameProvinceSearch.setLayout(new GridLayout(6, 1, 5, 5));
		bottomNameProvinceSearch.add(firstProvince);
		bottomNameProvinceSearch.add(secondProvince);
		bottomNameProvinceSearch.add(thirdProvince);
		bottomNameProvinceSearch.add(fourthProvince);
		bottomNameProvinceSearch.add(fifthProvince);
		JButton nameSelectorButton = new JButton(text.nameProvinceSelectionButton());
		nameSelectorButton.addActionListener(new NameSelectorButtonListener());
		bottomNameProvinceSearch.add(nameSelectorButton);
		// No visible because we don't know the search province name
		bottomNameProvinceSearch.setVisible(false);

		newNamePaneSearch.setLayout(new GridLayout(2, 1, 5, 5));
		newNamePaneSearch.add(namePaneSearch);
		newNamePaneSearch.add(bottomNameProvinceSearch);

		// Bloc for province search by baronies
		JPanel newBaronyPaneSearch = null;
		if (ckGame) {
			JPanel baronyPaneSearch = new JPanel();
			JLabel baronyProvinceSearchInfo = new JLabel(text.baronyProvinceSearchLabel());
			baronyReader = new JTextField();
			baronyReader.addKeyListener(new NameTextFieldListener()); // To have only letters
			JButton baronySearchButton = new JButton(text.baronyProvinceSelectionButton());
			baronySearchButton.addActionListener(new BaronySearchButtonListener());
			baronyPaneSearch.setLayout(new GridLayout(4, 1, 5, 5));
			baronyPaneSearch.add(baronyProvinceSearchInfo);
			baronyPaneSearch.add(baronyReader);
			baronyPaneSearch.add(baronySearchButton);
			newBaronyPaneSearch = new JPanel();
			bottomNameBaronySearch = new JPanel();

			// Bloc for province name search result
			firstBarony = new JRadioButton();
			secondBarony = new JRadioButton();
			thirdBarony = new JRadioButton();
			fourthBarony = new JRadioButton();
			fifthBarony = new JRadioButton();
			firstBarony.setSelected(true);
			ButtonGroup bgBarony = new ButtonGroup();
			bgBarony.add(firstBarony);
			bgBarony.add(secondBarony);
			bgBarony.add(thirdBarony);
			bgBarony.add(fourthBarony);
			bgBarony.add(fifthBarony);
			bottomNameBaronySearch.setLayout(new GridLayout(6, 1, 5, 5));
			bottomNameBaronySearch.add(firstBarony);
			bottomNameBaronySearch.add(secondBarony);
			bottomNameBaronySearch.add(thirdBarony);
			bottomNameBaronySearch.add(fourthBarony);
			bottomNameBaronySearch.add(fifthBarony);
			JButton baronySelectorButton = new JButton(text.baronyProvinceSelectionButton());
			baronySelectorButton.addActionListener(new BaronySelectorButtonListener());
			bottomNameBaronySearch.add(baronySelectorButton);
			// No visible because we don't know the search province name
			bottomNameBaronySearch.setVisible(false);

			newBaronyPaneSearch.setLayout(new GridLayout(2, 1, 5, 5));
			newBaronyPaneSearch.add(baronyPaneSearch);
			newBaronyPaneSearch.add(bottomNameBaronySearch);
		}

		// Present the different search means in tabbed pane
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add(text.searchID(), newIdPaneSearch);
		tabPane.add(text.searchName(), newNamePaneSearch);
		if (ckGame) {
			tabPane.add(text.searchBarony(), newBaronyPaneSearch);
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
				// Put the 5 nearest province name to the name typed by the user
				nearestProvinces = provinces.nearestProvinces(searchName, 5);
				firstProvince.setText(nearestProvinces.get(0).toString());
				secondProvince.setText(nearestProvinces.get(1).toString());
				thirdProvince.setText(nearestProvinces.get(2).toString());
				fourthProvince.setText(nearestProvinces.get(3).toString());
				fifthProvince.setText(nearestProvinces.get(4).toString());
				bottomNameProvinceSearch.setVisible(true);
			}
		}
	}

	class NameSelectorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// The searched province is the province selected by the user
			if (firstProvince.isSelected()) {
				searchedProvince = nearestProvinces.get(0);
			} else if (secondProvince.isSelected()) {
				searchedProvince = nearestProvinces.get(1);
			} else if (thirdProvince.isSelected()) {
				searchedProvince = nearestProvinces.get(2);
			} else if (fourthProvince.isSelected()) {
				searchedProvince = nearestProvinces.get(3);
			} else if (fifthProvince.isSelected()) {
				searchedProvince = nearestProvinces.get(4);
			}

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
				// Put the 5 nearest province name to the name typed by the user
				nearestBaronies = baronnies.nearestBaronies(searchName, 5);
				firstBarony.setText(nearestBaronies.get(0).toString());
				secondBarony.setText(nearestBaronies.get(1).toString());
				thirdBarony.setText(nearestBaronies.get(2).toString());
				fourthBarony.setText(nearestBaronies.get(3).toString());
				fifthBarony.setText(nearestBaronies.get(4).toString());
				bottomNameBaronySearch.setVisible(true);
			}
		}
	}

	class BaronySelectorButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// The searched province is the province selected by the user
			if (firstBarony.isSelected()) {
				searchedProvince = provinces.getProvince(nearestBaronies.get(0).getProvinceID());
			} else if (secondBarony.isSelected()) {
				searchedProvince = provinces.getProvince(nearestBaronies.get(1).getProvinceID());
			} else if (thirdBarony.isSelected()) {
				searchedProvince = provinces.getProvince(nearestBaronies.get(2).getProvinceID());
			} else if (fourthBarony.isSelected()) {
				searchedProvince = provinces.getProvince(nearestBaronies.get(3).getProvinceID());
			} else if (fifthBarony.isSelected()) {
				searchedProvince = provinces.getProvince(nearestBaronies.get(4).getProvinceID());
			}

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

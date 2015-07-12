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
import javax.swing.JTextField;

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

	public SearchDialog(JFrame parent, String title, boolean modal, Text text,
			ProvinceStorage provinces, LinkedList<Province> nearestProvinces) {
		super(parent, title, modal);
		this.text = text;
		this.provinces = provinces;
		this.nearestProvinces = nearestProvinces;
		setSize(750, 370);
		setLocationRelativeTo(null);
		setResizable(false);
		initComponent();
	}

	public Province getSearchResult() {
		// User can now interact with the dialog box
		setVisible(true);
		// When user stops use it (when isVisible == false), we return his Province choice
		return searchedProvince;
	}
	private void initComponent(){
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// Bloc for ID province search
		JPanel west = new JPanel();
		JLabel idProvinceSearchInfo = new JLabel(text.idProvinceSearchLabel());
		idReader = new JFormattedTextField(NumberFormat.getIntegerInstance());
		JButton idSearchButton = new JButton(text.idProvinceSearchButton());
		idSearchButton.addActionListener(new IdSearchButtonListener());
		west.setLayout(new GridLayout(3, 1, 5, 5));
		west.add(idProvinceSearchInfo);
		west.add(idReader);
		west.add(idSearchButton);
		west.setBorder(BorderFactory.createTitledBorder(text.idProvinceSearchBloc()));
		west.setPreferredSize(new Dimension(getWidth() / 2 - 3, 60));
		// The bloc occupy only a little part of the space
		JPanel newWest = new JPanel();
		newWest.setLayout(new GridLayout(2, 1, 5, 5));
		newWest.add(west);
		container.add(newWest, BorderLayout.WEST);

		// Bloc for ID province search
		JPanel east = new JPanel();
		JLabel nameProvinceSearchInfo = new JLabel(text.nameProvinceSearchLabel());
		nameReader = new JTextField();
		nameReader.addKeyListener(new NameTextFieldListener()); // To have only letters
		JButton nameSearchButton = new JButton(text.nameProvinceSearchButton());
		nameSearchButton.addActionListener(new NameSearchButtonListener());
		east.setLayout(new GridLayout(4, 1, 5, 5));
		east.add(nameProvinceSearchInfo);
		east.add(nameReader);
		east.add(nameSearchButton);
		JPanel newEast = new JPanel();
		bottomNameProvinceSearch = new JPanel();

		// Bloc for province name search
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
		// No visible because we don't now the search province name
		bottomNameProvinceSearch.setVisible(false);

		newEast.setLayout(new GridLayout(2, 1, 5, 5));
		newEast.add(east);
		newEast.add(bottomNameProvinceSearch);	
		newEast.setBorder(BorderFactory.createTitledBorder(text.nameProvinceSearchBloc()));
		newEast.setPreferredSize(new Dimension(getWidth() / 2 - 3, 60));
		container.add(newEast, BorderLayout.EAST);

		// Bloc for cancel
		JButton cancelButton = new JButton(text.cancelButton());
		cancelButton.addActionListener(new CancelButtonListener());
		container.add(cancelButton, BorderLayout.SOUTH);

		setContentPane(container);
	}

	// ---------------- Button Actions ---------------------------
	class IdSearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// The searched province is the province with the id typed by the user
			try {
				searchedProvince = provinces.getProvince(((Long)idReader.getValue()).intValue());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, text.enterIdPlease(), text.warningMessage(), JOptionPane.WARNING_MESSAGE);
			}
			// End of the dialogue
			setVisible(false);
		}
	}

	class NameSearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			String searchName = nameReader.getText();
			if (searchName.equals("")) {
				JOptionPane.showMessageDialog(null, text.enterNamePlease(), text.warningMessage(), JOptionPane.WARNING_MESSAGE);
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
			// We want only letters
			if(event.getKeyCode() < 65 || event.getKeyCode() > 90) {
				nameReader.setText(nameReader.getText().replace(String.valueOf(event.getKeyChar()), "")); 
			}
		}

		@Override
		public void keyTyped(KeyEvent event) {
			// nothing
		}
	}
}

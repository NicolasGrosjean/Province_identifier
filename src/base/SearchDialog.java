package base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class SearchDialog extends JDialog {
	private Text text;
	private StockageProvince provinces;
	private Province searchedProvince;
	private JFormattedTextField idReader;
	private JTextField nameReader;
	private boolean nameSearch;
	private LinkedList<Province> nearestProvinces;
	private String searchName;
	private JRadioButton firstProvince;
	private JRadioButton secondProvince;
	private JRadioButton thirdProvince;	
	private JRadioButton fourthProvince;
	private JRadioButton fifthProvince;

	public SearchDialog(JFrame parent, String title, boolean modal, Text text,
			StockageProvince provinces, boolean nameSearch,
			LinkedList<Province> nearestProvinces, String searchName) {
		super(parent, title, modal);
		this.text = text;
		this.provinces = provinces;
		this.nameSearch = nameSearch;
		this.nearestProvinces = nearestProvinces;
		this.searchName = searchName;
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
		JLabel idProvinceSearchInfo = new JLabel(text.provinceSearch());
		idReader = new JFormattedTextField(NumberFormat.getIntegerInstance());
		JButton idSearchButton = new JButton("Chercher la province correspondant à cette ID");
		idSearchButton.addActionListener(new IdSearchButtonListener());
		west.setLayout(new GridLayout(3, 1, 5, 5));
		west.add(idProvinceSearchInfo);
		west.add(idReader);
		west.add(idSearchButton);
		west.setBorder(BorderFactory.createTitledBorder("Recherche de province par son ID"));
		west.setPreferredSize(new Dimension(getWidth() / 2 - 3, 60));
		// The bloc occupy only a little part of the space
		JPanel newWest = new JPanel();
		newWest.setLayout(new GridLayout(2, 1, 5, 5));
		newWest.add(west);
		container.add(newWest, BorderLayout.WEST);

		// Bloc for ID province search
		JPanel east = new JPanel();
		JLabel nameProvinceSearchInfo = new JLabel("Entrer le nom de la province");
		nameReader = new JTextField();
		if (searchName != null) {
			nameReader.setText(searchName);
		}
		JButton nameSearchButton = new JButton("Chercher les provinces au nom proche de celui-ci");
		nameSearchButton.addActionListener(new NameSearchButtonListener());
		if (nameSearch) {
			east.setLayout(new GridLayout(4, 1, 5, 5));
		} else {
			east.setLayout(new GridLayout(3, 1, 5, 5));
		}		
		east.add(nameProvinceSearchInfo);
		east.add(nameReader);
		east.add(nameSearchButton);
		JPanel newEast = new JPanel();
		JPanel bottomNameProvinceSearch = new JPanel();
		if (nameSearch) {		
			firstProvince = new JRadioButton(nearestProvinces.get(0).toString());
			secondProvince = new JRadioButton(nearestProvinces.get(1).toString());
			thirdProvince = new JRadioButton(nearestProvinces.get(2).toString());
			fourthProvince = new JRadioButton(nearestProvinces.get(3).toString());
			fifthProvince = new JRadioButton(nearestProvinces.get(4).toString());
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
			JButton nameSelectorButton = new JButton("Chercher la province sélectionnée");
			nameSelectorButton.addActionListener(new NameSelectorButtonListener());
			bottomNameProvinceSearch.add(nameSelectorButton);							
		}
		newEast.setLayout(new GridLayout(2, 1, 5, 5));
		newEast.add(east);
		newEast.add(bottomNameProvinceSearch);	
		newEast.setBorder(BorderFactory.createTitledBorder("Recherche de province par son nom"));
		newEast.setPreferredSize(new Dimension(getWidth() / 2 - 3, 60));
		container.add(newEast, BorderLayout.EAST);

		// Bloc for cancel
		JButton cancelButton = new JButton("Annuler");
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
				JOptionPane.showMessageDialog(null, "Province non trouvée", "Attention", JOptionPane.WARNING_MESSAGE);
			}
			// End of the dialogue
			setVisible(false);
		}
	}

	class NameSearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {			
			// Refresh the SearchDialog but with the 3 nearest province name to the name typed by the user
			nameSearch = true;
			searchName = nameReader.getText();
			nearestProvinces = provinces.nearestProvinces(searchName, 5);
			initComponent();
			setVisible(true);
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
}

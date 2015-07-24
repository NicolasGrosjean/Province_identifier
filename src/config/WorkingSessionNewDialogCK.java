package config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import text.Text;

public class WorkingSessionNewDialogCK extends WorkingSessionNewDialog {
	private JTextField mapModDirectory = new JTextField();
	private JTextField modDirectory2TF = new JTextField();
	private JTextField modDirectory3TF = new JTextField();

	public WorkingSessionNewDialogCK(JFrame parent, boolean modal, Text text) {
		// Create the JDialog
		super(parent, modal, text);
		setSize(550, 400);

		// Create all necessary components
		JButton modDirectory1FE = new JButton(fileExplorerText);
		JButton modDirectory2FE = new JButton(fileExplorerText);
		JButton modDirectory3FE = new JButton(fileExplorerText);

		// Group them by JPanel
		JPanel pan2= new JPanel(new BorderLayout());
		pan2.add(mapModDirectory, BorderLayout.CENTER);
		pan2.add(modDirectory1FE, BorderLayout.EAST);
		pan2.setBorder(BorderFactory.createTitledBorder(null, text.mapModDirectory(),
				0, 0, null, Color.RED));
		JPanel pan3 = new JPanel(new BorderLayout());
		pan3.add(modDirectory2TF, BorderLayout.CENTER);
		pan3.add(modDirectory2FE, BorderLayout.EAST);
		pan3.setBorder(BorderFactory.createTitledBorder(null, text.provincesModDirectory(),
				0, 0, null, Color.BLUE));
		JPanel pan4 = new JPanel(new BorderLayout());
		pan4.add(modDirectory3TF, BorderLayout.CENTER);
		pan4.add(modDirectory3FE, BorderLayout.EAST);
		pan4.setBorder(BorderFactory.createTitledBorder(null, text.provincesModDirectory(),
				0, 0, null, Color.BLUE));


		// Add to the container
		container = new JPanel();
		container.setLayout(new GridLayout(6, 1, 5, 5));
		container.add(wsNamePanel);
		container.add(gameDirPanel);
		container.add(pan2);
		container.add(pan3);
		container.add(pan4);
		container.add(validCancelPanel);
		setContentPane(container);

		// Add FileExplorer actions
		modDirectory1FE.addActionListener(new FileExplorer(2));
		modDirectory2FE.addActionListener(new FileExplorer(3));
		modDirectory3FE.addActionListener(new FileExplorer(4));
	}

	@Override
	public WorkingSession getWorkingSession() throws IOException {
		// User can now interact with the dialog box
		setVisible(true);
		// When user stops use it (when isVisible == false), we return game and mod directories
		if (!validated) {
			// The user cancels the creation of a working session
			return null;
		}
		LinkedList<String> modDirectories = new LinkedList<String>();
		String mapDirectory;
		if (!mapModDirectory.getText().equals("")) {
			modDirectories.add(gameDirectoryTF.getText());
			mapDirectory = mapModDirectory.getText();
		} else {
			mapDirectory = gameDirectoryTF.getText();
		}
		if (!modDirectory2TF.getText().equals("")) {
			modDirectories.add(modDirectory2TF.getText());
		}
		if (!modDirectory3TF.getText().equals("")) {
			modDirectories.add(modDirectory3TF.getText());
		}
		return new WorkingSession(wsName.getText(), mapDirectory,
				modDirectories, text, true);
	}

	class FileExplorer implements ActionListener {
		/**
		 * Index of the button which use at file explorer
		 */
		private int fileExplorerIndex;

		public FileExplorer(int fileExplorerIndex) {
			this.fileExplorerIndex = fileExplorerIndex;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (fileChooser.showOpenDialog(WorkingSessionNewDialogCK.this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				// We open fileChooser in the last selected directory
				fileChooser.setCurrentDirectory(fileChooser.getSelectedFile());

				// Fill the corresponding JTextField
				switch (fileExplorerIndex) {
				case 1:
					gameDirectoryTF.setText(file.toString());
					break;
				case 2:
					mapModDirectory.setText(file.toString());
					break;
				case 3:
					modDirectory2TF.setText(file.toString());
					break;
				case 4:
					modDirectory3TF.setText(file.toString());
					break;
				default:
					throw new IllegalArgumentException("FileExplorer number " + fileExplorerIndex + " does not exists");
				}
			}
		}
	}
}

package config;

import java.awt.BorderLayout;
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

public class WorkingSessionNewDialog extends JDialog {
	private static final String fileExplorerText = "...";
	private Text text;
	private JFileChooser fileChooser;
	private JTextField wsName = new JTextField();
	private JTextField gameDirectoryTF = new JTextField();
	private JTextField modDirectory1TF = new JTextField();
	private JTextField modDirectory2TF = new JTextField();
	private JTextField modDirectory3TF = new JTextField();

	/**
	 * The user click on the validate button
	 */
	private boolean validated = false;

	public WorkingSessionNewDialog(JFrame parent, boolean modal, Text text) {
		// Create the JDialog
		super(parent, text.windowTitle() + " - " + text.newWSTitle(), modal);
		setSize(600, 450);
		setLocationRelativeTo(null);
		setResizable(false);

		this.text = text;

		// Create all necessary components
		JButton gameDirectoryFE = new JButton(fileExplorerText);
		JButton modDirectory1FE = new JButton(fileExplorerText);
		JButton modDirectory2FE = new JButton(fileExplorerText);
		JButton modDirectory3FE = new JButton(fileExplorerText);
		JButton validate = new JButton(text.validateButton());
		JButton cancel = new JButton(text.cancelButton());

		// Use the look and feel of the system for fileChooser
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (InstantiationException e) {}
		catch (ClassNotFoundException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		catch (IllegalAccessException e) {}
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Group them by JPanel
		JPanel pan0 = new JPanel(new BorderLayout());
		pan0.add(wsName, BorderLayout.CENTER);
		pan0.setBorder(BorderFactory.createTitledBorder(text.workingSessionName()));
		JPanel pan1 = new JPanel(new BorderLayout());
		pan1.add(gameDirectoryTF, BorderLayout.CENTER);
		pan1.add(gameDirectoryFE, BorderLayout.EAST);
		pan1.setBorder(BorderFactory.createTitledBorder(text.gameDirectory()));
		JPanel pan2= new JPanel(new BorderLayout());
		pan2.add(modDirectory1TF, BorderLayout.CENTER);
		pan2.add(modDirectory1FE, BorderLayout.EAST);
		pan2.setBorder(BorderFactory.createTitledBorder(text.modDirectory()));
		JPanel pan3 = new JPanel(new BorderLayout());
		pan3.add(modDirectory2TF, BorderLayout.CENTER);
		pan3.add(modDirectory2FE, BorderLayout.EAST);
		pan3.setBorder(BorderFactory.createTitledBorder(text.modDirectory()));
		JPanel pan4 = new JPanel(new BorderLayout());
		pan4.add(modDirectory3TF, BorderLayout.CENTER);
		pan4.add(modDirectory3FE, BorderLayout.EAST);
		pan4.setBorder(BorderFactory.createTitledBorder(text.modDirectory()));
		JPanel pan5 = new JPanel();
		pan5.add(validate);
		pan5.add(cancel);

		// Add to the container
		JPanel container = new JPanel();
		container.setLayout(new GridLayout(6, 1, 5, 5));
		container.add(pan0);
		container.add(pan1);
		container.add(pan2);
		container.add(pan3);
		container.add(pan4);
		container.add(pan5);
		setContentPane(container);

		// Add FileExplorer actions
		gameDirectoryFE.addActionListener(new FileExplorer(1));
		modDirectory1FE.addActionListener(new FileExplorer(2));
		modDirectory2FE.addActionListener(new FileExplorer(3));
		modDirectory3FE.addActionListener(new FileExplorer(4));

		// Add validate/cancel listener
		validate.addActionListener(new ValidateAction());
		cancel.addActionListener(new CancelAction());
	}

	public WorkingSession getWorkingSession() throws IOException {
		// User can now interact with the dialog box
		setVisible(true);
		// When user stops use it (when isVisible == false), we return game and mod directories
		if (!validated) {
			// The user cancels the creation of a working session
			return null;
		}
		LinkedList<String> modDirectories = new LinkedList<String>();
		if (!modDirectory1TF.getText().equals("")) {
			modDirectories.add(modDirectory1TF.getText());
		}
		if (!modDirectory2TF.getText().equals("")) {
			modDirectories.add(modDirectory1TF.getText());
		}
		if (!modDirectory3TF.getText().equals("")) {
			modDirectories.add(modDirectory1TF.getText());
		}
		return new WorkingSession(wsName.getText(), gameDirectoryTF.getText(), modDirectories, text);
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
			if (fileChooser.showOpenDialog(WorkingSessionNewDialog.this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				// We open fileChooser in the last selected directory
				fileChooser.setCurrentDirectory(fileChooser.getSelectedFile());

				// Fill the corresponding JTextField
				switch (fileExplorerIndex) {
				case 1:
					gameDirectoryTF.setText(file.toString());
					break;
				case 2:
					modDirectory1TF.setText(file.toString());
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

	class ValidateAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (wsName.getText().equals("")) {
				JOptionPane.showMessageDialog(null, text.missingWorkingSessionName(), text.error(), JOptionPane.ERROR_MESSAGE);
			} else if (gameDirectoryTF.getText().equals("")) {
				JOptionPane.showMessageDialog(null, text.missingGameDirectory(), text.error(), JOptionPane.ERROR_MESSAGE);
			} else {
				validated = true;
				setVisible(false);
			}
		}
	}

	class CancelAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			setVisible(false);
		}
	}
}

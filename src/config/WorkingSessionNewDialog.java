package config;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	protected static final String fileExplorerText = "...";
	protected Text text;
	protected JFileChooser fileChooser;
	protected JTextField wsName = new JTextField();
	protected JTextField gameDirectoryTF = new JTextField();
	protected JPanel container;
	protected JPanel wsNamePanel;
	protected JPanel gameDirPanel;
	protected JPanel validCancelPanel;
	protected boolean blackBorder;
	
	private JCheckBox xSymmetry;

	/**
	 * The user click on the validate button
	 */
	protected boolean validated = false;

	public WorkingSessionNewDialog(JFrame parent, boolean modal, Text text, boolean blackBorder) {
		// Create the JDialog
		super(parent, text.windowTitle() + " - " + text.newWSTitle(), modal);
		setSize(500, 220);
		setLocationRelativeTo(null);
		setResizable(false);

		this.text = text;
		this.blackBorder = blackBorder;

		// Create all necessary components
		JButton gameDirectoryFE = new JButton(fileExplorerText);
		JButton validate = new JButton(text.validateButton());
		JButton cancel = new JButton(text.cancelButton());

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Group them by JPanel
		wsNamePanel = new JPanel(new BorderLayout());
		wsNamePanel.add(wsName, BorderLayout.CENTER);
		wsNamePanel.setBorder(BorderFactory.createTitledBorder(text.workingSessionName()));
		gameDirPanel = new JPanel(new BorderLayout());
		gameDirPanel.add(gameDirectoryTF, BorderLayout.CENTER);
		gameDirPanel.add(gameDirectoryFE, BorderLayout.EAST);
		gameDirPanel.setBorder(BorderFactory.createTitledBorder(text.gameDirectory()));
		JPanel xSymetryPanel = new JPanel();
		xSymmetry = new JCheckBox(text.xSymmetry());
		xSymetryPanel.add(xSymmetry);
		validCancelPanel = new JPanel();
		validCancelPanel.add(validate);
		validCancelPanel.add(cancel);			

		// Add to the container
		container = new JPanel();
		container.setLayout(new GridLayout(4, 1, 5, 5));
		container.add(wsNamePanel);
		container.add(gameDirPanel);
		container.add(xSymetryPanel);
		container.add(validCancelPanel);		
		setContentPane(container);

		// Add FileExplorer actions
		gameDirectoryFE.addActionListener(new FileExplorer(1));

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
		return new WorkingSession(wsName.getText(), gameDirectoryTF.getText(),
				null, null, text, false, true, xSymmetry.isSelected(), blackBorder);
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

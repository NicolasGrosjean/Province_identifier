package config;

import java.io.FileOutputStream;
import java.util.LinkedList;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import text.Text;

public class ConfigStorage {
	/**
	 * Text language of the software
	 */
	private Text text;

	/**
	 * List of working sessions
	 */
	private LinkedList<WorkingSession> workingSessions = new LinkedList<WorkingSession>();

	public ConfigStorage(Text text) {
		this.text = text;
	}

	public ConfigStorage(String configFile) {
		// TODO
	}

	/**
	 * Transform a working session to the corresponding Element
	 * @param ws
	 */
	private void workingSessionToElement(WorkingSession ws) {
		// Create a working session XML element
		Element workingSessionElem = new Element("workingSession");
		workingSessionElem.setAttribute(new Attribute("name", ws.getName()));
		workingSessionElem.setAttribute(new Attribute("gameDirectory", ws.getGameDirectory()));
		for (String modDirectory : ws.getModDirectories()) {
			Element modDirectoryElem = new Element("modDirectory");
			modDirectoryElem.setText(modDirectory);
			workingSessionElem.addContent(modDirectoryElem);
		}
	}

	public void addFirstWorkingSession(WorkingSession ws) {
		workingSessions.addFirst(ws);
	}

	public WorkingSession removeFirst(WorkingSession ws) {
		return workingSessions.removeFirst();
	}

	public boolean removeWorkingSession(WorkingSession ws) {
		return workingSessions.remove(ws);
	}

	/**
	 * The working session becomes the first of working session list
	 * @param ws
	 */
	public void becomeFirst(WorkingSession ws) {
		if (workingSessions.remove(ws)) {
			addFirstWorkingSession(ws);
		} else {
			throw new IllegalArgumentException("ERROR : the working session was not in the list");
		}
	}

	/**
	 * Save the configuration file
	 * @param configurationFile
	 */
	public void saveConfigFile(String configurationFile) {
		// Create the root of XML file with the language attribute
		Element root = new Element("provinceIdentifier");
		String language = (text.isFrenchLanguage())? "French": "English";
		Attribute languageAttribute = new Attribute("language", language);
		root.setAttribute(languageAttribute);

		// Adding the working session in order
		// tmp to save the working session list
		LinkedList<WorkingSession> tmp = new LinkedList<WorkingSession>();
		while (!workingSessions.isEmpty()) {
			WorkingSession ws = workingSessions.removeFirst();
			tmp.addFirst(ws);
			workingSessionToElement(ws);
		}
		workingSessions = tmp;

		// Write the configuration file
		try {
			new XMLOutputter(Format.getPrettyFormat()).output(new Document(root), new FileOutputStream(configurationFile));
		} catch (java.io.IOException e) {
			throw new IllegalArgumentException("ERROR : problem to write the configuration file named "
					+ configurationFile);
		}
	}
}


package config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import text.Text;
import text.TextEnglish;
import text.TextFrancais;

public class ConfigStorage {
	private static final String languageNameAttribure = "language";
	private static final String provinceRGBAttribute = "provinceCol";
	private static final String castleRGBAttribute = "castleCol";
	private static final String cityRGBAttribute = "cityCol";
	private static final String templeRGBAttribute = "templeCol";
	private static final String tribalRGBAttribute = "tribalCol";
	private static final String blackBorderAttribute = "blackBorder";
	private static final String wsNameAttribute = "name";
	private static final String wsGameDirAttribute = "gameDirectory";
	private static final String wsMapModDirAttribute = "mapModDirectory";
	private static final String wsCKGameAttribute = "ckGame";
	private static final String xSymetryAttribute = "xSymetry";
	private static final String frenchLanguage = "French";

	private static final int defaultProvinceRGBAttribute = 8421504;
	private static final int defaultCastleRGBAttribute = 255;
	private static final int defaultCityRGBAttribute = 32768;
	private static final int defaultTempleRGBAttribute = 16711680;
	private static final int defaultTribalRGBAttribute = 0;
	private static final boolean defaultBlackBorder = true;

	/**
	 * Text language of the software
	 */
	private Text text;

	/**
	 * List of working sessions
	 */
	private LinkedList<WorkingSession> workingSessions = new LinkedList<WorkingSession>();

	/**
	 * Preferences of the user for the software
	 */
	public Preferences preferences;

	/**
	 * File where is load or save the configuration of the software
	 */
	private final String configurationFile;

	public ConfigStorage(Text text, String configurationFile) {
		this.text = text;
		this.configurationFile = configurationFile;
		preferences = new Preferences(defaultProvinceRGBAttribute,
				defaultCastleRGBAttribute, defaultTempleRGBAttribute,
				defaultCityRGBAttribute, defaultTribalRGBAttribute,
				text.isFrenchLanguage(), defaultBlackBorder);
	}

	/**
	 * Construct a configuration storage from a XML file named configFile
	 * text is null in case of error
	 * @param configFile
	 */
	public ConfigStorage(String configurationFile) {
		this.configurationFile = configurationFile;
		Element root = null;
		try {
			// Build the XML tree and gather the root
			root = new SAXBuilder().build(new File(configurationFile)).getRootElement();

			// Preferences of the software
			String language = root.getAttributeValue(languageNameAttribure);
			text = (language.equals(frenchLanguage))? new TextFrancais() : new TextEnglish();
			preferences = new Preferences(Integer.valueOf(root.getAttributeValue(provinceRGBAttribute)),
					Integer.valueOf(root.getAttributeValue(castleRGBAttribute)),
					Integer.valueOf(root.getAttributeValue(templeRGBAttribute)),
					Integer.valueOf(root.getAttributeValue(cityRGBAttribute)),
					Integer.valueOf(root.getAttributeValue(tribalRGBAttribute)),
					language.equals(frenchLanguage), Boolean.valueOf(root.getAttributeValue(blackBorderAttribute)));
		} catch (JDOMException | IOException e) {
			// No configuration file
			return;
		}
		// List of working session
		List<Element> workingSessionsElem = root.getChildren();
		Iterator<Element> it = workingSessionsElem.iterator();
		while (it.hasNext()) {
			Element wsElem = it.next();
			List<Element> modDirectoriesElem = wsElem.getChildren();
			LinkedList<String> modDirectories = new LinkedList<String>();
			for (Element modDirElem : modDirectoriesElem) {
				modDirectories.addFirst(modDirElem.getText());
			}
			try {
				workingSessions.addLast(new WorkingSession(wsElem.getAttributeValue(wsNameAttribute),
						wsElem.getAttributeValue(wsGameDirAttribute),
						wsElem.getAttributeValue(wsMapModDirAttribute), modDirectories, text,
						wsElem.getAttributeValue(wsCKGameAttribute).equals("true"), false,
						Boolean.valueOf(wsElem.getAttributeValue(xSymetryAttribute)),
						preferences.hasBlackBorder));
			} catch (IOException e) {
				// The working session is now not correct
			}
		}
	}

	public Text getText() {
		return text;
	}

	/**
	 * Transform a working session to the corresponding Element
	 * @param ws
	 */
	private Element workingSessionToElement(WorkingSession ws) {
		// Create a working session XML element
		Element workingSessionElem = new Element("workingSession");
		workingSessionElem.setAttribute(new Attribute(wsNameAttribute, ws.getName()));
		workingSessionElem.setAttribute(new Attribute(wsGameDirAttribute, ws.getGameDirectory()));
		if (ws.getMapModDirectory() != null) {
			workingSessionElem.setAttribute(new Attribute(wsMapModDirAttribute, ws.getMapModDirectory()));
		}
		workingSessionElem.setAttribute(new Attribute(wsCKGameAttribute, String.valueOf(ws.isCKGame())));
		workingSessionElem.setAttribute(new Attribute(xSymetryAttribute, Boolean.toString(ws.isxSymetry())));
		if (ws.getModDirectories() != null) {
			for (String modDirectory : ws.getModDirectories()) {
				Element modDirectoryElem = new Element("anotherDirectory");
				modDirectoryElem.setText(modDirectory);
				workingSessionElem.addContent(modDirectoryElem);
			}
		}
		return workingSessionElem;
	}

	/**
	 * Say if there are working sessions in the configuration storage
	 * @return
	 */
	public boolean hasWorkingSession() {
		return !workingSessions.isEmpty();
	}

	public WorkingSession getFirst() {
		return workingSessions.getFirst();
	}

	public void addFirstWorkingSession(WorkingSession ws) {
		workingSessions.addFirst(ws);
	}

	public int getSize() {
		return workingSessions.size();
	}

	public Iterator<WorkingSession> iterator() {
		return workingSessions.iterator();
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
	public void saveConfigFile() {
		// Create the root of XML file with the language attribute
		Element root = new Element("provinceIdentifier");
		String language;
		language = (preferences.isFrench)? frenchLanguage: "English";
		Attribute languageAttribute = new Attribute(languageNameAttribure, language);
		root.setAttribute(languageAttribute);
		root.setAttribute(new Attribute(provinceRGBAttribute, String.valueOf(preferences.getProvinceRGB())));
		root.setAttribute(new Attribute(castleRGBAttribute, String.valueOf(preferences.getCastleRGB())));
		root.setAttribute(new Attribute(cityRGBAttribute, String.valueOf(preferences.getCityRGB())));
		root.setAttribute(new Attribute(templeRGBAttribute, String.valueOf(preferences.getTempleRGB())));
		root.setAttribute(new Attribute(tribalRGBAttribute, String.valueOf(preferences.getTribalRGB())));
		root.setAttribute(new Attribute(blackBorderAttribute, String.valueOf(preferences.hasBlackBorder)));

		// Adding the working session in order
		Iterator<WorkingSession> it = workingSessions.iterator();
		while (it.hasNext()) {
			root.addContent(workingSessionToElement(it.next()));
		}

		// Write the configuration file
		try {
			new XMLOutputter(Format.getPrettyFormat()).output(new Document(root), new FileOutputStream(configurationFile));
		} catch (java.io.IOException e) {
			throw new IllegalArgumentException("ERROR : problem to write the configuration file named "
					+ configurationFile);
		}
	}
}


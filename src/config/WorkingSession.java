package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

import crusaderKings2.BaroniesStorage;
import text.Text;
import base.MiniMap;
import base.Panel;
import base.ProvinceStorage;

public class WorkingSession {
	private String name;
	private String gameDirectory;
	private String mapModDirectory;
	private String mapDirectory;
	private LinkedList<String> modDirectories;
	private ProvinceStorage provinces;
	private Panel panel;
	private MiniMap miniMap;
	private Text text;
	private boolean ckGame;
	private boolean init;

	// For Crusader Kings 2
	private BaroniesStorage storedBaronies;

	/**
	 * Create the province storage, the panel and the mini map
	 * from the game and mod directories name
	 * @param mapDirectory
	 * @param modDirectories
	 * @param text
	 * @throws IOException FileNotFoundException for definition.csv not found,
	 * 					   IOException for provinces.bmp not found
	 */
	public WorkingSession(String name, String gameDirectory, String mapModDirectory,
			LinkedList<String> modDirectories, Text text, boolean ckGame, boolean init)
					throws IOException {
		this.name = name;
		this.gameDirectory = gameDirectory;
		this.mapModDirectory = mapModDirectory;
		if (mapModDirectory != null) {
			this.mapDirectory = mapModDirectory;
		} else {
			this.mapDirectory = gameDirectory;
		}
		this.modDirectories = modDirectories;
		this.text = text;
		this.ckGame = ckGame;
		this.init = false; // the working session is not yet initialised
		if (init) {
			initialize();
		}
	}

	public void initialize() throws IOException {
		if (!init) {
			init = true;
			// Map informations
			readDefinitionFile(mapDirectory + "/map/definition.csv");			
			if (ckGame) {
				panel = new Panel(mapDirectory + "/map/provinces.bmp", text, false);
				miniMap = new MiniMap(mapDirectory + "/map/provinces.bmp", text, panel, false);
			} else {
				// TODO add a field for the mirror
				// TODO In Panel, calculate the image size according the real size
				panel = new Panel(mapDirectory + "/map/provinces.bmp", 1404, 540, text, true);
				miniMap = new MiniMap(mapDirectory + "/map/provinces.bmp", text, panel, true);
			}

			// Province attributes for CK games
			if (ckGame) {
				// List the directories with province files
				// Game directory must be first, then map mod directory (if exists), finally other mods
				LinkedList<String> provinceFileNames = new LinkedList<String>();
				LinkedList<String> allDirectories;
				if (modDirectories != null) {
					allDirectories = new LinkedList<String>(modDirectories);
				} else {
					allDirectories = new LinkedList<String>();
				}
				if (mapModDirectory != null) {
					allDirectories.addFirst(mapModDirectory);
				}
				allDirectories.addFirst(gameDirectory);
				provinceFileNames = FileSorting.giveFilesByDirPriority(allDirectories, "/history/provinces/", text);
				storedBaronies = new BaroniesStorage(provinceFileNames);
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getGameDirectory() {
		return gameDirectory;
	}

	public String getMapModDirectory() {
		return mapModDirectory;
	}

	public String getMapDirectory() {
		return mapDirectory;
	}

	public LinkedList<String> getModDirectories() {
		return modDirectories;
	}

	public ProvinceStorage getProvinces() {
		return provinces;
	}

	public boolean isInit() {
		return init;
	}

	public Panel getPanel() {
		if (init) {
			return panel;
		} else {
			throw new IllegalArgumentException("Working session not initialized");
		}
	}

	public MiniMap getMiniMap() {
		if (init) {
			return miniMap;
		} else {
			throw new IllegalArgumentException("Working session not initialized");
		}
	}

	public boolean isCKGame() {
		return ckGame;
	}

	public BaroniesStorage getStoredBaronies() {
		if (init) {
			return storedBaronies;
		} else {
			throw new IllegalArgumentException("Working session not initialized");
		}
	}

	/**
	 * Read the definitionFileName and create the province storage
	 * @param definitionFileName
	 * @throws FileNotFoundException file name definitionFileName not found
	 */
	private void readDefinitionFile(String definitionFileName) throws FileNotFoundException {
		provinces = new ProvinceStorage();
		FileInputStream fichierLecture = null;
		boolean exceptionCaught = false;
		try {
			// Open file
			fichierLecture = new FileInputStream(definitionFileName);

			// Read by separating with ";"
			Scanner scanner = new Scanner(fichierLecture, "ISO-8859-1"); 
			// ISO-8859-1 for special char
			scanner.useDelimiter(Pattern.compile("[;\n]"));

			// Searching integer
			while (!scanner.hasNextInt()) {
				@SuppressWarnings("unused")
				String useless = scanner.next();
			}

			while (scanner.hasNextInt()) {
				// Reading a province
				int id = scanner.nextInt();
				if (!scanner.hasNextInt())
					break; // End of reading
				int r = scanner.nextInt();
				if (!scanner.hasNextInt())
					break; // End of reading
				int g = scanner.nextInt();
				if (!scanner.hasNextInt())
					break; // End of reading
				int b = scanner.nextInt();
				String nom = scanner.next();
				while (!scanner.hasNextInt() && scanner.hasNext()) {
					@SuppressWarnings("unused")
					String useless = scanner.next();
				}

				// Store the province
				provinces.addProvince(id, r, g, b, nom);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			exceptionCaught = true;
		} finally {
			try {
				if (fichierLecture != null)
					fichierLecture.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (exceptionCaught) {
				throw new FileNotFoundException();
			}
		}
	}
}

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
	private boolean xSymetry;
	private boolean blackBorder;
	private boolean removeSeaRiver;

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
			LinkedList<String> modDirectories, Text text, boolean ckGame, boolean init,
			boolean xSymetry, boolean blackBorder, boolean removeSeaRiver) throws IOException {
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
		this.xSymetry = xSymetry;
		this.blackBorder = blackBorder;
		this.removeSeaRiver = removeSeaRiver;
		this.init = false; // the working session is not yet initialised
		if (init) {
			initialize();
		}
	}

	public void initialize() throws IOException {
		if (!init) {
			// Map informations
			readDefinitionFile(mapDirectory + "/map/definition.csv");
			if (ckGame) {
				readDefaultMapFile(mapDirectory + "/map/default.map");
			}
			panel = new Panel(mapDirectory + "/map/provinces.bmp", text, xSymetry, blackBorder, removeSeaRiver, provinces);
			miniMap = new MiniMap(mapDirectory + "/map/provinces.bmp", text, panel, xSymetry, removeSeaRiver, provinces);

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

			// The working session is correctly initialized
			init = true;
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

	public boolean isxSymetry() {
		return xSymetry;
	}

	public BaroniesStorage getStoredBaronies() {
		if (init) {
			return storedBaronies;
		} else {
			throw new IllegalArgumentException("Working session not initialized");
		}
	}

	public void updatePan(boolean blackBorder, boolean removeSeaRiver) throws IOException {
		this.blackBorder = blackBorder;
		this.removeSeaRiver = removeSeaRiver;
		if (!init) {
			initialize();
		} else {
			panel = new Panel(mapDirectory + "/map/provinces.bmp", text, xSymetry, blackBorder, removeSeaRiver, provinces);
			miniMap = new MiniMap(mapDirectory + "/map/provinces.bmp", text, panel, xSymetry, removeSeaRiver, provinces);
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
				provinces.addProvince(id, r, g, b, nom, false);
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
				throw new FileNotFoundException(definitionFileName);
			}
		}
	}

	private void readDefaultMapFile(String defaultMapFileName) throws FileNotFoundException {
		FileInputStream fichierLecture = null;
		boolean exceptionCaught = false;
		try {
			// Open file
			fichierLecture = new FileInputStream(defaultMapFileName);

			// Read by separating with ";"
			Scanner scanner = new Scanner(fichierLecture, "ISO-8859-1"); 
			// ISO-8859-1 for special char
			scanner.useDelimiter(Pattern.compile("[ \n\t]"));

			while (scanner.hasNext()) {
				String word = scanner.next();
				if (word.regionMatches(0, "sea_zones", 0, 9)) {
					while (!scanner.hasNextInt()) {
						scanner.next();
					}
					int beginSeaZone = scanner.nextInt();
					while (!scanner.hasNextInt()) {
						scanner.next();
					}
					int endSeaZone = scanner.nextInt();
					for (int id = beginSeaZone; id <= endSeaZone; id++) {
						provinces.getProvince(id).addSeaRiver();
					}
				} else if (word.regionMatches(0, "major_rivers", 0, 12)) {
					boolean endBlockFound = false;
					while (!endBlockFound) {
						while (!endBlockFound && !scanner.hasNextInt()) {
							endBlockFound = scanner.next().contains("}");
						}
						if (!endBlockFound) {
							provinces.getProvince(scanner.nextInt()).addSeaRiver();
						}
					}
				} else if (word.regionMatches(0, "ocean_region", 0, 12)) {
					// We don't read the sea_zones in this block, so we skip this block

					// Search the beginning of the block
					while (! word.contains("{")) {
						word = scanner.next();
					}
					// Counting the number of blocks in which we are
					int nbBlock = 1;
					while (scanner.hasNext()) {
						word = scanner.next();
						if (nbBlock == 0) {
							// We have found the end of the ocean_river block
							break;
						}
						if ( word.contains("{")) {
							nbBlock++;
						}
						if (word.contains("}")) {
							nbBlock--;
						}
					}
				}
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
				throw new FileNotFoundException(defaultMapFileName);
			}
		}
	}
}

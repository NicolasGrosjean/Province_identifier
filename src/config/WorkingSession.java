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
	private String mapDirectory;
	private LinkedList<String> modDirectories;
	private ProvinceStorage provinces;
	private Panel panel;
	private MiniMap miniMap;
	private boolean ckGame;

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
	public WorkingSession(String name, String mapDirectory,
			LinkedList<String> modDirectories, Text text, boolean ckGame)
					throws IOException {
		this.name = name;
		this.mapDirectory = mapDirectory;
		this.modDirectories = modDirectories;
		this.ckGame = ckGame;
		LinkedList<String> allDirectories;
		if (modDirectories != null) {
			allDirectories = new LinkedList<String>(modDirectories);
		} else {
			allDirectories = new LinkedList<String>();
		}
		allDirectories.add(mapDirectory);

		// Map informations
		readDefinitionFile(mapDirectory + "/map/definition.csv");
		panel = new Panel(mapDirectory + "/map/provinces.bmp", text);
		miniMap = new MiniMap(mapDirectory + "/map/provinces.bmp", text, panel);

		// Province attributes for CK games
		if (ckGame) {
			LinkedList<String> provinceFileNames = new LinkedList<String>();
			LinkedList<String> mainDirectories = new LinkedList<String>();
			mainDirectories.add(mapDirectory);
			while (!modDirectories.isEmpty()) {
				mainDirectories.add(modDirectories.removeFirst());
			}
			provinceFileNames = FileSorting.sortFiles(mainDirectories, "/history/provinces/", text);
			storedBaronies = new BaroniesStorage(provinceFileNames);
		}
	}

	public String getName() {
		return name;
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

	public Panel getPanel() {
		return panel;
	}

	public MiniMap getMiniMap() {
		return miniMap;
	}

	public boolean isCKGame() {
		return ckGame;
	}

	public BaroniesStorage getStoredBaronies() {
		return storedBaronies;
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

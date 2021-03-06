package crusaderKings2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Pattern;

import base.Province;
import base.ProvinceStorage;

public class BaroniesStorage {
	private Map<Integer, LinkedList<Barony>> provinceBaronies;

	public BaroniesStorage(LinkedList<String> provinceFileNames, ProvinceStorage provinces) {
		this.provinceBaronies = new HashMap<Integer, LinkedList<Barony>>();
		while (!provinceFileNames.isEmpty()) {
			init(provinceFileNames.removeFirst(), provinces);
		}
	}

	private void init(String provinceFileName, ProvinceStorage provinces) {
		FileInputStream fichierLecture = null;
		Scanner scanner = null;
		try {
			// Remove the parent path to the provinceFileName
			File provinceFile = new File(provinceFileName);
			String provinceFileShortName = provinceFileName.substring(
					provinceFile.getParent().length() + 1, provinceFileName.length());
			// Province Id is in the name before '-' and '\space'
			int provinceID = Integer.parseInt(provinceFileShortName.split("-")[0].split(" ")[0]);

			LinkedList<Barony> baronnies = new LinkedList<Barony>();

			// Hash set to guarantee unicity of barony
			HashSet<String> baronyNameSet = new HashSet<String>();

			fichierLecture = new FileInputStream(provinceFileName);
			scanner = new Scanner(fichierLecture, "ISO-8859-1");
			scanner.useDelimiter(Pattern.compile("[=\\s\n\t]"));
			while (scanner.hasNext()) {
				String word = scanner.next();
				if (word.contains("#")) {
					// Skipping comment
					word = scanner.nextLine();
				} else if (word.regionMatches(0, "b_", 0, 2)) {
					// Barony found
					if (scanner.hasNext()) {
						// Read its type and store it
						String baronyType = scanner.next();
						while (baronyType.equals("") && scanner.hasNext()) {
							baronyType = scanner.next();
						}
						if ((baronyType.regionMatches(0, "city", 0, 4) ||
								baronyType.regionMatches(0, "castle", 0, 6) ||
								baronyType.regionMatches(0, "temple", 0, 6)||
								baronyType.regionMatches(0, "tribal", 0, 6))
							&& baronyNameSet.add(word)) {
							baronnies.add(new Barony(word,
									baronyType.regionMatches(0, "city", 0, 4),
									baronyType.regionMatches(0, "castle", 0, 6),
									baronyType.regionMatches(0, "tribal", 0, 6),
									provinceID));
						}
					}
				} else if ("terrain".equals(word)) {
					String terrain = scanner.next();
					while (terrain.equals("") && scanner.hasNext()) {
						terrain = scanner.next();
					}
					Province province = provinces.getProvince(provinceID);
					if (province != null) {
						// If the it as a game base province not in the mod, it could be null 
						province.setTerrain(terrain.split("#")[0].replace("\t", "").replace(" ", "").toLowerCase());
					}
				}
			}

			// Store baronies with the province ID
			if (!baronnies.isEmpty()) {
				// Besides save place, not is empty condition protect us of province file to erase previous content
				provinceBaronies.put(provinceID, baronnies);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} finally {
			scanner.close();
			try {
				if (fichierLecture != null)
					fichierLecture.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the list of baronies of the province of this ID
	 * @param provinceId
	 * @return
	 */
	public LinkedList<Barony> getBaronies(int provinceId) {
		return provinceBaronies.get(provinceId);
	}

	/**
	 * Give the nearest baronies in term of name from the search name
	 * @param searchName Name (approximated) of province searched
	 * @param number Number of provinces in the list, or if number is negative all the provinces
	 * @return List of the provinces
	 */
	public LinkedList<Barony> nearestBaronies(String searchName, int number) {
		PriorityQueue<Barony> nearestBaronies = new PriorityQueue<Barony>();
		for (Integer provinceId : provinceBaronies.keySet()) {
			for (Barony b : provinceBaronies.get(provinceId)) {
				b.calculateLevenshteinDistance(searchName);
				nearestBaronies.add(b);
			}
		}
		LinkedList<Barony> res = new LinkedList<Barony>();
		if (number > 0) {
			for (int i = 0; i < number ; i++) {
				res.addLast(nearestBaronies.remove());
			}
		} else {
			while (!nearestBaronies.isEmpty()) {
				res.addLast(nearestBaronies.remove());
			}
		}
		return res;
	}
}

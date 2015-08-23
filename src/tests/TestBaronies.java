package tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import text.Text;
import text.TextFrancais;
import config.FileSorting;
import crusaderKings2.BaroniesStorage;
import crusaderKings2.Barony;


public class TestBaronies {
	private static BaroniesStorage ckBaronies;
	private static BaroniesStorage swmhBaronies;
	private static BaroniesStorage lolBaronies;

	@BeforeClass
	public static void SetUp() {
		Text text = new TextFrancais();
		LinkedList<String> ckDirectory = new LinkedList<String>();
		ckDirectory.add("C:/Jeux/Steam/SteamApps/common/Crusader Kings II");
		LinkedList<String> swmhDirectories = new LinkedList<String>();
		swmhDirectories.add("C:/Jeux/Steam/SteamApps/common/Crusader Kings II");
		swmhDirectories.add("C:/Users/Nicolas/Documents/Paradox Interactive/Crusader Kings II/MOD/swmh2.854");
		LinkedList<String> lolDirectories = new LinkedList<String>();
		lolDirectories.add("C:/Jeux/Steam/SteamApps/common/Crusader Kings II");
		lolDirectories.add("C:/Users/Nicolas/Documents/Paradox Interactive/Crusader Kings II/MOD/swmh2.854");
		ckBaronies = new BaroniesStorage(FileSorting.giveFilesByDirPriority(ckDirectory, "/history/provinces/", text));
		swmhBaronies = new BaroniesStorage(FileSorting.giveFilesByDirPriority(swmhDirectories, "/history/provinces/", text));
		lolBaronies = new BaroniesStorage(FileSorting.giveFilesByDirPriority(lolDirectories, "/history/provinces/", text));
	}

	@Test
	/**
	 * Test there is only one time the barony in the province
	 */
	public void baronyUnicityInAProvince() {
		// Test for CK2 Game
		for (int i = 1; i < 1437; i++) {
			if (ckBaronies.getBaronies(i) != null) {
				HashSet<String> baronySet = new HashSet<String>();
				for (Barony b : ckBaronies.getBaronies(i)) {
					if (!baronySet.add(b.getBaronyName())) {
						Assert.assertEquals("Province d'id " + i + " a plusieurs fois le même nom de baronnie "
								+ b.getBaronyName(), false, true);
					}
				}
			}
		}

		// Test for CK2 Game + SWMH
		for (int i = 1; i < 1529; i++) {
			if (swmhBaronies.getBaronies(i) != null) {
				HashSet<String> baronySet = new HashSet<String>();
				for (Barony b : swmhBaronies.getBaronies(i)) {
					if (!baronySet.add(b.getBaronyName())) {
						Assert.assertEquals("Province d'id " + i + "a plusieurs fois le même nom de baronnie "
								+ b.getBaronyName(), false, true);
					}
				}
			}
		}
		
		// Test for CK2 Game + SWMH + special Troyes files
		for (int i = 1; i < 1529; i++) {
			if (lolBaronies.getBaronies(i) != null) {
				HashSet<String> baronySet = new HashSet<String>();
				for (Barony b : lolBaronies.getBaronies(i)) {
					if (!baronySet.add(b.getBaronyName())) {
						Assert.assertEquals("Province d'id " + i + "a plusieurs fois le même nom de baronnie "
								+ b.getBaronyName(), false, true);
					}
				}
			}
		}
	}

	@Test
	/**
	 * Test there is only one time the barony in all the provinces
	 */
	public void baronyUnicityInAllProvince() {
		// Test for CK2 Game
		HashMap<String, Integer> ckBaronyMap = new HashMap<String, Integer>();
		for (int i = 1; i < 1437; i++) {
			if (ckBaronies.getBaronies(i) != null) {
				for (Barony b : ckBaronies.getBaronies(i)) {
					Integer j = ckBaronyMap.put(b.getBaronyName(), i);
					if (j != null) {
						Assert.assertEquals("Il y a plusieurs fois le même nom de baronnie "
								+ b.getBaronyName() + " : dans les provinces d'ID "
								+ i + " et " + j, false, true);
					}
				}
			}
		}

		// Test for CK2 Game + SWMH
		HashMap<String, Integer> swmhBaronyMap = new HashMap<String, Integer>();
		for (int i = 1; i < 1529; i++) {
			if (swmhBaronies.getBaronies(i) != null) {
				for (Barony b : swmhBaronies.getBaronies(i)) {
					Integer j = swmhBaronyMap.put(b.getBaronyName(), i);
					if (j != null &&
							j != 438 && j != 446 && 
							j != 836 && j != 831 &&
							j != 941 && j != 738 &&
							j != 160 && j != 1195 &&
							j != 1212 && j != 158 &&
							j != 1520 && j != 873) { // Some duplicate baronies in SWMH
						Assert.assertEquals("Il y a plusieurs fois le même nom de baronnie "
								+ b.getBaronyName() + " : dans les provinces d'ID "
								+ i + " et " + j, false, true);
					}
				}
			}
		}
		
		// Test for CK2 Game + SWMH + special Troyes files
		HashMap<String, Integer> lolBaronyMap = new HashMap<String, Integer>();
		for (int i = 1; i < 1529; i++) {
			if (lolBaronies.getBaronies(i) != null) {
				for (Barony b : lolBaronies.getBaronies(i)) {
					Integer j = lolBaronyMap.put(b.getBaronyName(), i);
					if (j != null &&
							j != 438 && j != 446 && 
							j != 836 && j != 831 &&
							j != 941 && j != 738 &&
							j != 160 && j != 1195 &&
							j != 1212 && j != 158 &&
							j != 1520 && j != 873) { // Some duplicate baronies in SWMH
						Assert.assertEquals("Il y a plusieurs fois le même nom de baronnie "
								+ b.getBaronyName() + " : dans les provinces d'ID "
								+ i + " et " + j, false, true);
					}
				}
			}
		}
	}

	@Test
	/**
	 * Test there is 7 or less baronies by province
	 */
	public void less7BaroniesByProvince() {
		// Test for CK2 Game
		for (int i = 1; i < 1437; i++) {
			if (ckBaronies.getBaronies(i) != null &&
					ckBaronies.getBaronies(i).size() > 7) {
				Assert.assertEquals("Province d'id " + i + " a plus de 7 baronnies", false, true);
			}
		}

		// Test for CK2 Game
		for (int i = 1; i < 1437; i++) {
			if (swmhBaronies.getBaronies(i) != null &&
					swmhBaronies.getBaronies(i).size() > 7 &&
					i != 274) { // So much baronies in this province
				Assert.assertEquals("Province d'id " + i + " a plus de 7 baronnies", false, true);
			}
		}
		
		// Test for CK2 Game + SWMH + special Troyes files
		for (int i = 1; i < 1437; i++) {
			if (lolBaronies.getBaronies(i) != null &&
					lolBaronies.getBaronies(i).size() > 7 &&
					i != 274) { // So much baronies in this province
				Assert.assertEquals("Province d'id " + i + " a plus de 7 baronnies", false, true);
			}
		}
	}
}

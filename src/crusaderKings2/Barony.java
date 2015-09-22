package crusaderKings2;

import base.Province;

public class Barony implements Comparable {
	/**
	 * Name of the barony b_********
	 */
	private String baronyName;

	/**
	 * The barony is a city or not
	 */
	private boolean city;

	/**
	 * The barony is a castle or not
	 */
	private boolean castle;

	/**
	 * The barony is only tribal in the history
	 */
	private boolean tribal;

	/**
	 * ID of the province in which is the barony
	 */
	private int provinceID;

	/**
	 * Levenshtein distance between the barony name and a province name searched
	 */
	private int levenshteinDistance;

	public Barony(String baronyName, boolean city, boolean castle, boolean tribal, int provinceID) {
		super();
		this.baronyName = new String(baronyName);
		if (city && castle) {
			throw new IllegalArgumentException();
		}
		this.city = city;
		this.castle = castle;
		this.tribal = tribal;
		this.provinceID = provinceID;
	}

	public String getBaronyName() {
		return baronyName;
	}

	public boolean isCity() {
		return city;
	}

	public boolean isCastle() {
		return castle;
	}

	public boolean isTemple() {
		return !city && !castle && !tribal;
	}

	public boolean isTribal() {
		return tribal;
	}

	public int getProvinceID() {
		return provinceID;
	}

	public int getLevenshteinDistance() {
		return levenshteinDistance;
	}

	@Override
	public String toString() {
		// Remove "b_" and start with an UpperCase
		String res = baronyName.substring(2, 3).toUpperCase()
				+ baronyName.substring(3);
		// Do the same thing for all others "_" (we suppose have more than 1 letter after the "_")
		while (res.contains("_")) {
			int underscorePos = res.indexOf("_");
			res = res.substring(0, underscorePos) + " "
					+ res.substring(underscorePos + 1, underscorePos + 2).toUpperCase()
					+ res.substring(underscorePos + 2, res.length());
		}
		return res;
	}

	/**
	 * Calculate the levenshteinDistance between the barony name and the search name
	 * @param searchName
	 */
	public void calculateLevenshteinDistance(String searchName) {
		levenshteinDistance = others.Levenshtein.calculateLevenshteinDistance(searchName, baronyName);
	}

	/**
	 * Comparison in term of levenshteinDistance
	 */
	@Override
	public int compareTo(Object arg0) {
		if (arg0 instanceof Barony) {
			Barony b = (Barony)arg0;
			return levenshteinDistance - b.getLevenshteinDistance();
		} else {
			throw new IllegalArgumentException("Comparison between a Province and antoher thing");
		}
	}
}

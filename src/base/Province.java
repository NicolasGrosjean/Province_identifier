package base;
/**
 * Data province management
 * 
 * @author Mouchi
 *
 */
public class Province implements Comparable{
	/**
	 * Province identifier
	 */
	private int id;
	
	/**
	 * R in  province RGB
	 */
	private int r;
	
	/**
	 * G in province RGB
	 */
	private int g;
	
	/**
	 * B in province RGB
	 */
	private int b;
	
	/**
	 * Province name
	 */
	private String name;

	/**
	 * The province is a sea or a river province
	 */
	private boolean seaRiver;

	/**
	 * Levenshtein distance between the province name and a province name searched
	 */
	private int levenshteinDistance;
	
	public Province(int id, int r, int g, int b, String name, boolean seaRiver) {
		super();
		this.id = id;
		this.r = r;
		this.g = g;
		this.b = b;
		this.name = name;
		this.seaRiver = seaRiver;
	}
	
	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isSeaRiver() {
		return seaRiver;
	}

	public void addSeaRiver() {
		seaRiver = true;
	}

	public int getLevenshteinDistance() {
		return levenshteinDistance;
	}


	/**
	 * Give province RGB identifier
	 * @return
	 */
	public int getIdentifiantRGB(){
		return (r << 16) + (g << 8) + b;
	}
	
	/**
	 * Calculate RGB identifier from R, G and B
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	static int calculIdentifiantRGB(int r, int g, int b){
		return (r << 16) + (g << 8) + b;
	}

	@Override
	public String toString() {
		return  id + " - " + name;
	}
	
	/**
	 * Calculate the levenshteinDistance between the province name and the search name
	 * @param searchName
	 */
	public void calculateLevenshteinDistance(String searchName) {
		if (name.equals("")) {
			// Penalize if it is not a province
			levenshteinDistance = Integer.MAX_VALUE;
		} else {
			levenshteinDistance = others.Levenshtein.calculateLevenshteinDistance(searchName, name);
		}
	}

	/**
	 * Comparison in term of levenshteinDistance
	 */
	@Override
	public int compareTo(Object arg0) {
		if (arg0 instanceof Province) {
			Province p = (Province)arg0;
			return levenshteinDistance - p.getLevenshteinDistance();
		} else {
			throw new IllegalArgumentException("Comparison between a Province and antoher thing");
		}		
	}	
}

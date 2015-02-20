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
	 * Levenshtein distance between the province name and a province name searched
	 */
	private int levenshteinDistance;
	
	public Province(int id, int r, int g, int b, String name) {
		super();
		this.id = id;
		this.r = r;
		this.g = g;
		this.b = b;
		this.name = name;
	}
	
	public int getId() {
		return id;
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
	 * ADAPTED from Xavier Philippeau's algorithm 
	 * (http://www.developpez.net/forums/d653597/general-developpement/algorithme-mathematiques/contribuez/java-distance-levenshtein/)
	 * Calculate the levenshteinDistance between the province name and the search name
	 * @param searchName
	 */
	public void calculateLevenshteinDistance(String searchName) {
		int len0 = searchName.length()+1;
		int len1 = name.length()+1;
	 
		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];
	 
		// initial cost of skipping prefix in String searchName
		for(int i=0;i<len0;i++) cost[i]=i;
	 
		// dynamically computing the array of distances
	 
		// transformation cost for each letter in String name
		for(int j=1;j<len1;j++) {
	 
			// initial cost of skipping prefix in String name
			newcost[0]=j-1;
	 
			// transformation cost for each letter in String searchName
			for(int i=1;i<len0;i++) {
	 
				// matching current letters in both strings
				int match = (searchName.charAt(i-1)==name.charAt(j-1))?0:1;
	 
				// computing cost for each transformation
				int cost_replace = cost[i-1]+match;
				int cost_insert  = cost[i]+1;
				int cost_delete  = newcost[i-1]+1;
	 
				// keep minimum cost
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
			}
	 
			// swap cost/newcost arrays
			int[] swap=cost; cost=newcost; newcost=swap;
		}
	 
		// the distance is the cost for transforming all letters in both strings
		levenshteinDistance = cost[len0-1];
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

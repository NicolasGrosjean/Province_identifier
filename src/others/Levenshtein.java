package others;

public class Levenshtein {
	/**
	 * ADAPTED from Xavier Philippeau's algorithm
	 * (http://www.developpez.net/forums/d653597/general-developpement/algorithme-mathematiques/contribuez/java-distance-levenshtein/)
	 * Calculate the levenshteinDistance between the search name and another name
	 * @param searchName
	 * @param anotherName
	 * @return
	 */
	public static int calculateLevenshteinDistance(String searchName, String anotherName) {
		int len0 = searchName.length()+1;
		int len1 = anotherName.length()+1;

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
				int match = (searchName.charAt(i-1)==anotherName.charAt(j-1))?0:1;

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
		return cost[len0-1];
	}
}

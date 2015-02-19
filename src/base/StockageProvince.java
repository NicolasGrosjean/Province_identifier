package base;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Stockage des provinces
 * 
 * @author Mouchi
 *
 */
public class StockageProvince {
	/**
	 * Stockage des provinces selon leur identifiant RGB
	 * (VOIR dans Province.java comment il est calculé)
	 */
	private Map<Integer, Province> provincesRGB;
	
	/**
	 * Stockage des provinces selon leur ID
	 */
	private Map<Integer, Province> provincesID;

	/**
	 * Store provinces in a list in order to through this list
	 */
	private LinkedList<Province> provinces;

	public StockageProvince() {
		super();
		this.provincesRGB = new HashMap<Integer, Province>();
		this.provincesID = new HashMap<Integer, Province>();
		this.provinces = new LinkedList<Province>();
	}
	
	/**
	 * Ajoute une province au stockage
	 * @param id
	 * @param r
	 * @param g
	 * @param b
	 * @param nom
	 */
	public void addProvince(int id, int r, int g, int b, String nom) {
		Province newProvince = new Province(id, r, g, b, nom);
		this.provincesRGB.put(newProvince.getIdentifiantRGB(), newProvince);
		this.provincesID.put(newProvince.getId(), newProvince);
		this.provinces.add(newProvince);
	}
	
	/**
	 * Retourne la province ayant ce code RGB
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public Province getProvince(int r, int g, int b) {
		return this.provincesRGB.get(Province.calculIdentifiantRGB(r, g, b));
	}
	
	/**
	 * Retourne la province ayant cet id
	 * @param id
	 * @return
	 */
	public Province getProvince(int id) {
		return this.provincesID.get(id);
	}

	/**
	 * Give the nearest provinces in term of name from the search name
	 * @param searchName Name (approximated) of province searched
	 * @param number Number of provinces in the list
	 * @return List of the provinces
	 */
	public LinkedList<Province> nearestProvinces(String searchName, int number) {
		PriorityQueue<Province> nearestProvinces = new PriorityQueue<Province>();
		for (Province p : provinces) {
			p.calculateLevenshteinDistance(searchName);
			nearestProvinces.add(p);
		}
		LinkedList<Province> res = new LinkedList<Province>();
		for (int i = 0; i < number ; i++) {
			res.addLast(nearestProvinces.remove());
		}
		return res;
	}
}

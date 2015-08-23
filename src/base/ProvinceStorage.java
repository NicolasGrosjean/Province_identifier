package base;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Store provinces
 * 
 * @author Mouchi
 *
 */
public class ProvinceStorage {
	/**
	 * Store provinces by RGB identifier
	 * (SEE Province.java for RGB identifier calculate)
	 */
	private Map<Integer, Province> provincesRGB;
	
	/**
	 * Store provinces by ID
	 */
	private Map<Integer, Province> provincesID;

	/**
	 * Store provinces in a list in order to through this list
	 */
	private LinkedList<Province> provinces;

	public ProvinceStorage() {
		super();
		this.provincesRGB = new HashMap<Integer, Province>();
		this.provincesID = new HashMap<Integer, Province>();
		this.provinces = new LinkedList<Province>();
	}
	
	/**
	 * Store a province
	 * @param id
	 * @param r
	 * @param g
	 * @param b
	 * @param nom
	 */
	public void addProvince(int id, int r, int g, int b, String nom, boolean seaRiver) {
		Province newProvince = new Province(id, r, g, b, nom, seaRiver);
		this.provincesRGB.put(newProvince.getIdentifiantRGB(), newProvince);
		this.provincesID.put(newProvince.getId(), newProvince);
		this.provinces.add(newProvince);
	}
	
	/**
	 * Get province with this RGB
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public Province getProvince(int r, int g, int b) {
		return this.provincesRGB.get(Province.calculIdentifiantRGB(r, g, b));
	}
	
	/**
	 * Get province with this id
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

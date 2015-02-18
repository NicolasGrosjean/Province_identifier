package base;
/**
 * Gestion des données d'une province
 * 
 * @author Mouchi
 *
 */
public class Province {
	/**
	 * Identifiant de la province
	 */
	private int id;
	
	/**
	 * Code R dans RGB de la province
	 */
	private int r;
	
	/**
	 * Code G dans RGB de la province
	 */
	private int g;
	
	/**
	 * Code B dans RGB de la province
	 */
	private int b;
	
	/**
	 * Nom de la province
	 */
	private String nom;
	
	public Province(int id, int r, int g, int b, String nom) {
		super();
		this.id = id;
		this.r = r;
		this.g = g;
		this.b = b;
		this.nom = nom;
	}
	
	/**
	 * Retourne l'id de la province
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retourne l'identifiant RGB de la province
	 * @return
	 */
	public int getIdentifiantRGB(){
		return (r << 16) + (g << 8) + b;
	}
	
	/**
	 * Calcule l'identifiant pour un code RGB
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
		return  id + " - " + nom;
	}
	
}

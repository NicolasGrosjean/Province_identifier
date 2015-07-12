package crusaderKings2;

public class Barony {
	private String baronyName;
	private boolean city;
	private boolean castle;

	public Barony(String baronyName, boolean city, boolean castle) {
		super();
		this.baronyName = new String(baronyName);
		if (city && castle) {
			throw new IllegalArgumentException();
		}
		this.city = city;
		this.castle = castle;
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
		return !city && !castle;
	}
}

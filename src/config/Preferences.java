package config;

public class Preferences {
	private int provinceRGB;
	private int castleRGB;
	private int templeRGB;
	private int cityRGB;
	public boolean isFrench;
	public boolean hasBlackBorder;

	public Preferences(int provinceRGB, int castleRGB, int templeRGB,
			int cityRGB, boolean isFrench, boolean hasBlackBorder) {
		this.provinceRGB = provinceRGB;
		this.castleRGB = castleRGB;
		this.templeRGB = templeRGB;
		this.cityRGB = cityRGB;
		this.isFrench = isFrench;
		this.hasBlackBorder = hasBlackBorder;
	}

	public int getProvinceRGB() {
		return provinceRGB;
	}

	public void setProvinceRGB(int r, int g, int b) {
		provinceRGB = ((r & 0xff) << 16) + ((g & 0xff) << 8) + b;
	}

	public int getProvinceR() {
		return provinceRGB >> 16 & 0xff;
	}

	public int getProvinceG() {
		return provinceRGB >> 8 & 0xff;
	}

	public int getProvinceB() {
		return provinceRGB & 0xff;
	}

	public int getCastleRGB() {
		return castleRGB;
	}

	public void setCastleRGB(int r, int g, int b) {
		castleRGB = ((r & 0xff) << 16) + ((g & 0xff) << 8) + b;
	}

	public int getCastleR() {
		return castleRGB >> 16 & 0xff;
	}

	public int getCastleG() {
		return castleRGB >> 8 & 0xff;
	}

	public int getCastleB() {
		return castleRGB & 0xff;
	}

	public int getTempleRGB() {
		return templeRGB;
	}

	public void setTempleRGB(int r, int g, int b) {
		templeRGB = ((r & 0xff) << 16) + ((g & 0xff) << 8) + b;
	}

	public int getTempleR() {
		return templeRGB >> 16 & 0xff;
	}

	public int getTempleG() {
		return templeRGB >> 8 & 0xff;
	}

	public int getTempleB() {
		return templeRGB & 0xff;
	}

	public int getCityRGB() {
		return cityRGB;
	}

	public void setCityRGB(int r, int g, int b) {
		cityRGB = ((r & 0xff) << 16) + ((g & 0xff) << 8) + b;
	}

	public int getCityR() {
		return cityRGB >> 16 & 0xff;
	}

	public int getCityG() {
		return cityRGB >> 8 & 0xff;
	}

	public int getCityB() {
		return cityRGB & 0xff;
	}

}

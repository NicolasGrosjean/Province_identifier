package text;

public class TextEnglish extends Text {

	@Override
	protected String file() {
		return "File";
	}

	@Override
	protected String notFound() {
		return "was not found";
	}

	@Override
	public String clickedProvince() {
		return "Clicked province";
	}

	@Override
	public String windowTitle() {
		return "Province identifier";
	}

	@Override
	public String copyClipboard() {
		return "Copy to clipboard";
	}

	@Override
	public String provinceSearch() {
		return "Province ID to search";
	}
	
	@Override
	public String invalidWidth() {
		return "Displayed image has an invalid width";
	}

	@Override
	public String invalidHeight() {
		return "Displayed image has an invalid height";
	}

	@Override
	public String invalidWidthNumber() {
		return "Displayed image has an invalid width number";
	}

	@Override
	public String invalidHeightNumber() {
		return "Displayed image has an invalid height number";
	}

	@Override
	public String invalidRGB() {
		return "No province with this RGB code";
	}

}

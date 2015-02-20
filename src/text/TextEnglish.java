package text;

/**
 * Text in English
 * @author Mouchi
 *
 */
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
		return "Search a province";
	}
	
	@Override
	public String idProvinceSearchLabel() {
		return "Province ID to search";
	}

	@Override
	public  String idProvinceSearchButton() {
		return "Search the province corresponding to this ID";
	}
	
	@Override
	public  String idProvinceSearchBloc() {
		return "Searching province by ID";
	}
	
	@Override
	public String nameProvinceSearchLabel() {
		return "Name (approximate) of province to search : ";
	}
	
	@Override
	public String nameProvinceSearchButton() {
		return "Search the provinces with name near from this name";
	}
	
	@Override
	public String nameProvinceSelectionButton() {
		return "Search selected province";
	}
	
	@Override
	public String nameProvinceSearchBloc() {
		return "Searching province by name";
	}
	
	@Override
	public String cancelButton() {
		return "Cancel";
	}
	
	@Override
	public String warningMessage() {
		return "Warning";
	}
	
	@Override
	public String provinceNotFound() {
		return "Province not found";
	}
	
	@Override
	public String enterIdPlease() {
		return "Please enter an id (integer which identify the province)";
	}
	
	@Override
	public String enterNamePlease() {
		return "Please enter a province name (even if it is approximate)";
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

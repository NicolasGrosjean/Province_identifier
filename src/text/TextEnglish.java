package text;

import java.util.LinkedList;

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
		return "Name (approximate) of a province to search : ";
	}

	@Override
	public String nameProvinceSearchButton() {
		return "Search the provinces with name near from this name";
	}

	@Override
	public String nameProvinceSelectionButton() {
		return "Show in the map the selected province";
	}

	@Override
	public String baronyProvinceSearchLabel() {
		return "Name (approximate) of a barony to search : ";
	}

	@Override
	public String baronyProvinceSearchButton() {
		return "Search the baronies with name near from this name";
	}

	@Override
	public String baronyProvinceSelectionButton() {
		return "Show in the map the province of the selected barony";
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
	public String enterProvinceNamePlease() {
		return "Please enter a province name (even if it is approximate)";
	}

	@Override
	public String enterBaronyNamePlease() {
		return "Please enter a barony name (even if it is approximate)";
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

	@Override
	public String invalidDirectoryName(String directoryName) {
		return "ERROR : invalid directory named " + directoryName;
	}

	@Override
	public String missingDirectoryName() {
		return "ERROR : missing directory name";
	}

	@Override
	public String chooseLanguageTitle() {
		return "Choose the language";
	}

	@Override
	public String missingLanguage() {
		return "ERROR : the language of the software hasn't been chosen";
	}

	@Override
	public String chooseLanguageInstruction() {
		return "Please, choose the language of the software";
	}

	@Override
	public String frenchLanguageName() {
		return "French";
	}

	@Override
	public String englishLanguageName() {
		return "English";
	}

	@Override
	public String validateButton() {
		return "Ok";
	}

	@Override
	public boolean isFrenchLanguage() {
		return false;
	}

	@Override
	public boolean isEnglishLanguage() {
		return true;
	}

	@Override
	public String newWSTitle() {
		return "New working session";
	}

	@Override
	public String workingSessionName() {
		return "Name of the working session";
	}

	@Override
	public String gameDirectory() {
		return "Game directory";
	}

	@Override
	public String mapModDirectory() {
		return "[Optional] Repertory of a mod modifying the map";
	}

	@Override
	public String provincesModDirectory() {
		return "[Optional] Repertory of a mod modifying the province attributes";
	}

	@Override
	public String error() {
		return "ERROR";
	}

	@Override
	public String missingWorkingSessionName() {
		return "Please enter a working session name";
	}

	@Override
	public String missingGameDirectory() {
		return "Please select a game directory";
	}

	@Override
	public String workingSessionMenu() {
		return "Game/mod";
	}

	@Override
	public String newWorkingSessionMenuItem() {
		return "New";
	}

	@Override
	public String newWSCKMenuItem() {
		return "Crusader Kings";
	}

	@Override
	public String newWSBasicMenuItem() {
		return "Other";
	}

	@Override
	public String workingSessionOpenRecently() {
		return "Open recent";
	}

	@Override
	public String reloadWSMenu() {
		return "Reload game/mod files";
	}

	@Override
	public String provinceBaronies() {
		return "Baronies of the province:";
	}

	@Override
	public String castle() {
		return "Castle";
	}

	@Override
	public String city() {
		return "City";
	}

	@Override
	public String temple() {
		return "Temple";
	}

	@Override
	public String tribal() {
		return "Tribal";
	}

	@Override
	public String searchID() {
		return "With its ID";
	}

	@Override
	public String searchName() {
		return "With its name";
	}

	@Override
	public String searchBarony() {
		return "With one of its baronies";
	}

	@Override
	public String xSymmetry() {
		return "Apply to the map a symmetry about the X axis";
	}

	@Override
	public String color() {
		return "Color";
	}

	@Override
	public String preferencesTitle() {
		return "Preferences";
	}

	@Override
	public String provinceColor() {
		return "Province color";
	}

	@Override
	public String castleColor() {
		return "Castle color";
	}

	@Override
	public String cityColor() {
		return "City color";
	}

	@Override
	public String templeColor() {
		return "Temple color";
	}

	@Override
	public String tribalColor() {
		return "Tribal color";
	}

	@Override
	public String close() {
		return "Close";
	}

	@Override
	public String language() {
		return "Language";
	}

	@Override
	public LinkedList<String> languageChange() {
		LinkedList<String> result = new LinkedList<String>();
		result.addLast("You must restart the software");
		result.addLast("in order to benefit of language change");
		return result;
	}

	@Override
	public String other() {
		return "Other";
	}

	@Override
	public String blackBorder() {
		return "Provinces has a black border";
	}

	@Override
	public String navigation() {
		return "Navigation";
	}

	@Override
	public String moveTop() {
		return "Move top";
	}

	@Override
	public String moveLeft() {
		return "Move left";
	}

	@Override
	public String moveBottom() {
		return "Move bottom";
	}

	@Override
	public String moveRight() {
		return "Move right";
	}

	@Override
	public String zoomIn() {
		return "Zoom in";
	}

	@Override
	public String zoomOut() {
		return "Zoom out";
	}

	@Override
	public String WSInformation() {
		return "Informations on the game/mod";
	}

	@Override
	public String removeSeaRiver() {
		return "Remove sea and river provinces (only CK working session)";
	}
}

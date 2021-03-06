package text;

import java.util.LinkedList;

import base.Terrain;

/**
 * Class for the text
 * @author Mouchi
 *
 */
public abstract class Text {

	/**
	 * Word file 
	 * @return
	 */
	protected abstract String file();
	
	/**
	 * Expression "was not found"
	 * @return
	 */
	protected abstract String notFound();
	
	/**
	 * File : file was not found
	 * @param file
	 * @return
	 */
	public  String fileNotFound(String file) {
		return file() + " " + file + " " + notFound();
	}
	
	/**
	 * Text displayed before the result
	 * @return
	 */
	public abstract String selectedProvince();
	
	/**
	 * Text displayed before the result in RGB
	 * @return
	 */
	public abstract String selectedProvinceRGB();

	/**
	 * Title of the window
	 * @return
	 */
	public abstract String windowTitle();
	
	/**
	 * Copy to the clipboard
	 * @return
	 */
	public abstract String copyClipboard();
	
	/**
	 * Text in button and dialog window to search a province
	 * @return
	 */
	public abstract String provinceSearch();
	
	/**
	 * Message on the label for the search of province by id
	 * @return
	 */
	public abstract String idProvinceSearchLabel();
	
	/**
	 * Message on the button for the search of province by id
	 * @return
	 */
	public abstract String idProvinceSearchButton();
	
	/**
	 * Message on the bloc for the search of province by id
	 * @return
	 */
	public abstract String idProvinceSearchBloc();
	
	/**
	 * Message on the label for the search of province by name
	 * @return
	 */
	public abstract String nameProvinceSearchLabel();

	/**
	 * Message on the button for the search of province by name
	 * @return
	 */
	public abstract String nameProvinceSearchButton();
	
	/**
	 * Message on the button for the selection of province
	 * @return
	 */
	public abstract String nameProvinceSelectionButton();
	
	/**
	 * Message on the bloc for the search of province by name
	 * @return
	 */
	public abstract String nameProvinceSearchBloc();


	/**
	 * Message on the label for the search of province by barony
	 * @return
	 */
	public abstract String baronyProvinceSearchLabel();

	/**
	 * Message on the button for the search of province by barony
	 * @return
	 */
	public abstract String baronyProvinceSearchButton();
	
	/**
	 * Message on the button for the selection of barony
	 * @return
	 */
	public abstract String baronyProvinceSelectionButton();

	/**
	 * Message on the button to cancel
	 * @return
	 */
	public abstract String cancelButton();
	
	/**
	 * Warning message window
	 * @return
	 */
	public abstract String warningMessage();
	
	/**
	 * Warning : province not Found
	 * @return
	 */
	public abstract String provinceNotFound();
	
	/**
	 * Warning : no id typed
	 * @return
	 */
	public abstract String enterIdPlease();
	
	/**
	 * Warning : no province name typed
	 * @return
	 */
	public abstract String enterProvinceNamePlease();

	/**
	 * Warning : no barony name typed
	 * @return
	 */
	public abstract String enterBaronyNamePlease();
	
	/**
	 * Exception invalid width
	 * @return
	 */
	public abstract String invalidWidth();

	/**
	 * Exception invalid height
	 * @return
	 */
	public abstract String invalidHeight();

	/**
	 * Exception invalid width number
	 * @return
	 */
	public abstract String invalidWidthNumber();

	/**
	 * Exception invalid height number
	 * @return
	 */
	public abstract String invalidHeightNumber();

	/**
	 * Exception invalid RGB
	 * @return
	 */
	public abstract String invalidRGB();

	/**
	 * The directory name is not valid
	 * @param directoryName
	 * @return
	 */
	public abstract String invalidDirectoryName(String directoryName);

	/**
	 * The directory name is missing
	 * @return
	 */
	public abstract String missingDirectoryName();

	/**
	 * Second part of the title of the dialog to choose language
	 * @return
	 */
	public abstract String chooseLanguageTitle();

	/**
	 * The language for the software hasn't been chosen
	 * @return
	 */
	public abstract String missingLanguage();

	/**
	 * Instruction to choose the language
	 * @return
	 */
	public abstract String chooseLanguageInstruction();

	/**
	 * Name of the French language
	 * @return
	 */
	public abstract String frenchLanguageName();

	/**
	 * Name of the English language
	 * @return
	 */
	public abstract String englishLanguageName();

	/**
	 * Text for the validation button
	 * @return
	 */
	public abstract String validateButton();

	/**
	 * All texts are in French?
	 * @return
	 */
	public abstract boolean isFrenchLanguage();

	/**
	 * All texts are in English?
	 * @return
	 */
	public abstract boolean isEnglishLanguage();

	/**
	 * Second part of the title of the dialog to create a working session
	 * @return
	 */
	public abstract String newWSTitle();

	/**
	 * Name of the working session
	 * @return
	 */
	public abstract String workingSessionName();

	/**
	 * Directory of the game
	 * @return
	 */
	public abstract String gameDirectory();

	/**
	 * Optional directory for a mod modifying the map
	 * @return
	 */
	public abstract String mapModDirectory();

	/**
	 * Optional directory for a mod modifying the provinces attributes
	 * @return
	 */
	public abstract String provincesModDirectory();

	/**
	 * Error word
	 * @return
	 */
	public abstract String error();

	/**
	 * The name for the working session is missing
	 * @return
	 */
	public abstract String missingWorkingSessionName();

	/**
	 * The game directory is missing
	 * @return
	 */
	public abstract String missingGameDirectory();

	/**
	 * Text of the working session menu
	 * @return
	 */
	public abstract String workingSessionMenu();

	/**
	 * Text of the menu item for a new working session
	 * @return
	 */
	public abstract String newWorkingSessionMenuItem();

	/**
	 * Text of the menu item for a new CK working session
	 * @return
	 */
	public abstract String newWSCKMenuItem();

	/**
	 * Text of the menu item for a new basic working session
	 * @return
	 */
	public abstract String newWSBasicMenuItem();

	/**
	 * Menu open a recent working session
	 * @return
	 */
	public abstract String workingSessionOpenRecently();

	/**
	 * Menu to reload the current working session
	 * @return
	 */
	public abstract String reloadWSMenu();

	/**
	 * Text presenting that the following text is the baronies of the province
	 * @return
	 */
	public abstract String provinceBaronies();

	/**
	 * The barony type castle
	 * @return
	 */
	public abstract String castle();

	/**
	 * The barony type city
	 * @return
	 */
	public abstract String city();

	/**
	 * The barony type temple
	 * @return
	 */
	public abstract String temple();

	/**
	 * The barony type tribal
	 * @return
	 */
	public abstract String tribal();

	/**
	 * Text of the menu to search a province with its ID
	 * @return
	 */
	public abstract String searchID();

	/**
	 * Text of the menu to search a province with its name
	 * @return
	 */
	public abstract String searchName();

	/**
	 * Text of the menu to search a province with one of its baronies
	 * @return
	 */
	public abstract String searchBarony();

	/**
	 * Propose to do a symmetry about the X axis of the map
	 * @return
	 */
	public abstract String xSymmetry();

	/**
	 * Title of the preferences dialog
	 * @return
	 */
	public abstract String preferencesTitle();

	/**
	 * Tab pane for color
	 * @return
	 */
	public abstract String color();

	/**
	 * Sub-title for province color
	 * @return
	 */
	public abstract String provinceColor();

	/**
	 * Sub-title for castle color
	 * @return
	 */
	public abstract String castleColor();

	/**
	 * Sub-title for city color
	 * @return
	 */
	public abstract String cityColor();

	/**
	 * Sub-title for temple color
	 * @return
	 */
	public abstract String templeColor();

	/**
	 * Sub-title for tribal color
	 * @return
	 */
	public abstract String tribalColor();

	/**
	 * Close button text
	 * @return
	 */
	public abstract String close();

	/**
	 * Language panel
	 * @return
	 */
	public abstract String language();

	/**
	 * Order for the language change
	 * @return
	 */
	public abstract LinkedList<String> languageChange();

	/**
	 * Other tab pane
	 */
	public abstract String other();

	/**
	 * Black border check box description
	 * @return
	 */
	public abstract String blackBorder();

	/**
	 * Navigation menu
	 * @return
	 */
	public abstract String navigation();

	/**
	 * Move top menu item
	 * @return
	 */
	public abstract String moveTop();

	/**
	 * Move left menu item
	 * @return
	 */
	public abstract String moveLeft();

	/**
	 * Move bottom menu item
	 * @return
	 */
	public abstract String moveBottom();

	/**
	 * Move right menu item
	 * @return
	 */
	public abstract String moveRight();

	/**
	 * Zoom in menu item
	 * @return
	 */
	public abstract String zoomIn();

	/**
	 * Zoom out top menu item
	 * @return
	 */
	public abstract String zoomOut();

	/**
	 * WS info menu item
	 * @return
	 */
	public abstract String WSInformation();

	/**
	 * Remove sea river check box description
	 * @return
	 */
	public abstract String removeSeaRiver();

	/**
	 * Functionality only for CK2 Working Sessions
	 * @return
	 */
	public abstract String onlyForCK2WS();

	/**
	 * Province terrain type
	 * @return
	 */
	public abstract String terrainType(Terrain t);
}

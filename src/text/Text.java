package text;

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
	public abstract String clickedProvince();
	
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
	 * Warning : no name typed
	 * @return
	 */
	public abstract String enterNamePlease();
	
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
	public abstract String modDirectory();

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
}

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
	 * Element for the province research
	 * @return
	 */
	public abstract String provinceSearch();
	
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
	
}

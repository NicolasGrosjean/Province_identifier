package text;

public class TextFrancais extends Text {

	@Override
	public String file() {
		return "Fichier";
	}

	@Override
	public String notFound() {
		return "non trouvé !";
	}

	@Override
	public String clickedProvince() {
		return "Province cliquée :";
	}

	@Override
	public String windowTitle() {
		return "Identificateur de provinces";
	}
	
	@Override
	public String copyClipboard() {
		return "Copier dans le presse-papier";
	}

	@Override
	public String provinceSearch() {
		return "ID de la province à chercher : ";
	}
	
	@Override
	public String invalidWidth() {
		return "largeur de l'image incorrecte";
	}

	@Override
	public String invalidHeight() {
		return "hauteur de l'image incorrecte";
	}

	@Override
	public String invalidWidthNumber() {
		return "numéro de largeur incorrect";
	}

	@Override
	public String invalidHeightNumber() {
		return "numéro de hauteur incorrect";
	}

	@Override
	public String invalidRGB() {
		return "Accès au code RGB d'un pixel hors image";
	}
}

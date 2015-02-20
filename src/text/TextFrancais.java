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
		return "Chercher une province";
	}
	
	@Override
	public String idProvinceSearchLabel() {
		return "ID de la province à chercher : ";
	}
	
	@Override
	public  String idProvinceSearchButton() {
		return "Chercher la province correspondant à cet ID";
	}
	
	@Override
	public  String idProvinceSearchBloc() {
		return "Recherche de province par son ID";
	}
	
	@Override
	public String nameProvinceSearchLabel() {
		return "Nom (approximatif) de la province à chercher : ";
	}
	
	@Override
	public String nameProvinceSearchButton() {
		return "Chercher les provinces au nom proche de ce nom";
	}
	
	@Override
	public String nameProvinceSelectionButton() {
		return "Chercher la province sélectionnée";
	}
	
	@Override
	public String nameProvinceSearchBloc() {
		return "Recherche de province par son nom";
	}
	
	@Override
	public String cancelButton() {
		return "Annuler";
	}
	
	@Override
	public String warningMessage() {
		return "Attention";
	}
	
	@Override
	public String provinceNotFound() {
		return "Province non trouvée";
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

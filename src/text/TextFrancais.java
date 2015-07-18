package text;

/**
 * Text in french
 * @author Mouchi
 *
 */
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
	public String enterIdPlease() {
		return "Veuillez entrer un id (entier identifiant la province)";
	}
	
	@Override
	public String enterNamePlease() {
		return "Veuillez entrer un nom de province (même approximatif)";
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

	@Override
	public String invalidDirectoryName(String directoryName) {
		return "ERROR : nom de dossier " + directoryName + " incorrect";
	}

	@Override
	public String missingDirectoryName() {
		return "ERROR : nom de dossier manquant";
	}

	@Override
	public String chooseLanguageTitle() {
		return "Choisir la langue";
	}

	@Override
	public String missingLanguage() {
		return "ERROR : la langue du logiciel n'a pas été spécifiée";
	}

	@Override
	public String chooseLanguageInstruction() {
		return "Veuillez choisir la langue du logiciel";
	}

	@Override
	public String frenchLanguageName() {
		return "Français";
	}

	@Override
	public String englishLanguageName() {
		return "Anglais";
	}

	@Override
	public String validateButton() {
		return "Valider";
	}

	@Override
	public boolean isFrenchLanguage() {
		return true;
	}

	@Override
	public boolean isEnglishLanguage() {
		return false;
	}

	@Override
	public String newWSTitle() {
		return "Nouvelle session de travail";
	}

	@Override
	public String workingSessionName() {
		return "Nom de la session de travail";
	}

	@Override
	public String gameDirectory() {
		return "Répertoire du jeu";
	}

	@Override
	public String modDirectory() {
		return "[Facultatif] Répertoire d'un mod modifiant la carte";
	}

	@Override
	public String error() {
		return "ERREUR";
	}

	@Override
	public String missingWorkingSessionName() {
		return "Veuillez entrer un nom à la session de travail";
	}

	@Override
	public String missingGameDirectory() {
		return "Veuillez sélectionner le répertoire du jeu";
	}

	@Override
	public String workingSessionMenu() {
		return "Jeu/mod";
	}

	@Override
	public String newWorkingSessionMenuItem() {
		return "Nouveau";
	}
}

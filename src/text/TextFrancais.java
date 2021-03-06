package text;

import java.util.LinkedList;

import base.Terrain;

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
		return "non trouv� !";
	}

	@Override
	public String selectedProvince() {
		return "Province selectionn� :";
	}

	@Override
	public String selectedProvinceRGB() {
		return "Code RGB de la province :";
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
		return "ID de la province �chercher : ";
	}
	
	@Override
	public  String idProvinceSearchButton() {
		return "Chercher la province correspondant �cet ID";
	}
	
	@Override
	public  String idProvinceSearchBloc() {
		return "Recherche de province par son ID";
	}
	
	@Override
	public String nameProvinceSearchLabel() {
		return "Nom (approximatif) de la province �chercher : ";
	}
	
	@Override
	public String nameProvinceSearchButton() {
		return "Chercher les provinces au nom proche de ce nom";
	}
	
	@Override
	public String nameProvinceSelectionButton() {
		return "Montrer sur la carte la province s�lectionn�";
	}
	
	@Override
	public String nameProvinceSearchBloc() {
		return "Recherche de province par son nom";
	}

	@Override
	public String baronyProvinceSearchLabel() {
		return "Nom (approximatif) de la baronnie �chercher : ";
	}

	@Override
	public String baronyProvinceSearchButton() {
		return "Chercher les baronnies au nom proche de ce nom";
	}

	@Override
	public String baronyProvinceSelectionButton() {
		return "Montrer sur la carte la province de la baronnie s�lectionn�";
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
		return "Province non trouv�";
	}
	
	@Override
	public String enterIdPlease() {
		return "Veuillez entrer un id (entier identifiant la province)";
	}
	
	@Override
	public String enterProvinceNamePlease() {
		return "Veuillez entrer un nom de province (m�me approximatif)";
	}

	@Override
	public String enterBaronyNamePlease() {
		return "Veuillez entrer un nom de baronnie (m�me approximatif)";
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
		return "num�ro de largeur incorrect";
	}

	@Override
	public String invalidHeightNumber() {
		return "num�ro de hauteur incorrect";
	}

	@Override
	public String invalidRGB() {
		return "Acc�s au code RGB d'un pixel hors image";
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
		return "ERROR : la langue du logiciel n'a pas �t� sp�cifi�";
	}

	@Override
	public String chooseLanguageInstruction() {
		return "Veuillez choisir la langue du logiciel";
	}

	@Override
	public String frenchLanguageName() {
		return "Fran�ais";
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
		return "R�pertoire du jeu";
	}

	@Override
	public String mapModDirectory() {
		return "[Facultatif] R�pertoire d'un mod modifiant la carte";
	}

	@Override
	public String provincesModDirectory() {
		return "[Facultatif] R�pertoire d'un mod modifiant les attributs des provinces";
	}

	@Override
	public String error() {
		return "ERREUR";
	}

	@Override
	public String missingWorkingSessionName() {
		return "Veuillez entrer un nom � la session de travail";
	}

	@Override
	public String missingGameDirectory() {
		return "Veuillez s�lectionner le r�pertoire du jeu";
	}

	@Override
	public String workingSessionMenu() {
		return "Jeu/mod";
	}

	@Override
	public String newWorkingSessionMenuItem() {
		return "Nouveau";
	}

	@Override
	public String newWSCKMenuItem() {
		return "Crusader Kings";
	}

	@Override
	public String newWSBasicMenuItem() {
		return "Autre";
	}

	@Override
	public String workingSessionOpenRecently() {
		return "Ouvrir r�cent";
	}

	@Override
	public String reloadWSMenu() {
		return "Recharger les fichiers de jeu/mod";
	}

	@Override
	public String provinceBaronies() {
		return "Baronnies de la province:";
	}
	@Override
	public String castle() {
		return "Ch�teau";
	}

	@Override
	public String city() {
		return "Ville";
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
		return "Avec son ID";
	}

	@Override
	public String searchName() {
		return "Avec son nom";
	}

	@Override
	public String searchBarony() {
		return "Avec une de ses baronnies";
	}

	@Override
	public String xSymmetry() {
		return "Appliquer � la carte une sym�trie par rapport � l'axe X";
	}

	@Override
	public String preferencesTitle() {
		return "Options";
	}

	@Override
	public String color() {
		return "Couleur";
	}

	@Override
	public String provinceColor() {
		return "Couleur de province";
	}

	@Override
	public String castleColor() {
		return "Couleur de ch�teau";
	}

	@Override
	public String cityColor() {
		return "Couleur de ville";
	}

	@Override
	public String templeColor() {
		return "Couleur de temple";
	}

	@Override
	public String tribalColor() {
		return "Couleur de fief tribal";
	}

	@Override
	public String close() {
		return "Fermer";
	}

	@Override
	public String language() {
		return "Langue";
	}

	@Override
	public LinkedList<String> languageChange() {
		LinkedList<String> result = new LinkedList<String>();
		result.addLast("Vous devez red�marrer le logiciel");
		result.addLast("pour que le changement de langue prenne effet");
		return result;
	}

	@Override
	public String other() {
		return "Autre";
	}

	@Override
	public String blackBorder() {
		return "Bordure noire autour des provinces";
	}

	@Override
	public String navigation() {
		return "Navigation";
	}

	@Override
	public String moveTop() {
		return "D�placer haut";
	}

	@Override
	public String moveLeft() {
		return "D�placer gauche";
	}

	@Override
	public String moveBottom() {
		return "D�placer bas";
	}

	@Override
	public String moveRight() {
		return "D�placer droite";
	}

	@Override
	public String zoomIn() {
		return "Zoomer";
	}

	@Override
	public String zoomOut() {
		return "D�zoomer";
	}

	@Override
	public String WSInformation() {
		return "Informations sur le jeu/mod";
	}

	@Override
	public String removeSeaRiver() {
		return "Retirer les provinces maritimes et fluviales";
	}

	@Override
	public String onlyForCK2WS() {
		return "Seulement pour les session de travail CK";
	}

	@Override
	public String terrainType(Terrain t) {
		if (t == null)
			return "";
		switch (t) {
		case arctic:
			return "Arctique";
		case coastal_desert:
			return "Oc�an";
		case desert:
			return "D�sert";
		case farmlands:
			return "Terres arables";
		case forest:
			return "For�t";
		case hills:
			return "Collines";
		case jungle:
			return "Jungle";
		case marsh:
			return "Marais";
		case mountain:
			return "Montagne";
		case plains:
			return "Plaines";
		case steppe:
			return "Steppe";
		case savanna:
			return "Savane";
		case woods:
			return "Woods";
		default:
			throw new IllegalArgumentException("Invalid terrain!");
		}
	}
}

/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 04/02/2017
 */

package data;

import typedef.*;
import graphic.*;
import java.io.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.output.*;

public class Bibliotheque {

	// Lien avec les autres classes graphiques.
	private Interface i;

	// Attributs
	public static final String dossierRandos = "data/hikes";
	public static final String SYM_ALL = "Toutes les randonnees   ";
	private int nbRandos;
	Vector<Rando> listeRandos;

	/**
	 * CONSTRUCTEUR DE LA CLASSE BIBLIOTHEQUE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public Bibliotheque(Interface pI) throws IOException, JDOMException {
		i = pI;
		nbRandos = 0;
		listeRandos = new Vector<Rando>();
		// Parcours des fichiers dans le dossier passe en argument...
		File dossier = new File(dossierRandos);
		File[] fichiers = dossier.listFiles();
		int i = 0;
		// Pour chaque fichier trouve dans le dossier...
		for (i = 0; i < (fichiers.length); i++) {
			if (fichiers[i].isFile()) {
				String nomFichier = fichiers[i].toString();
				if (nomFichier.lastIndexOf(".") > 0) {
					// On verifie l'extention XML.
					String ext = nomFichier.substring(nomFichier.lastIndexOf("."));
					if (ext.compareTo(".xml") == 0) {
						// On cree une structure de donnees.
						Rando r = new Rando(fichiers[i]);
						listeRandos.addElement(r);
						nbRandos++;
						System.out.println("Ajout de la randonnee : " + r.getNom());
					}
				}
			}
		}
	}

	/**
	 * RETOURNE LA LISTE DES RANDONNEES.
	 * 
	 * @param Aucun.
	 * @return listeRandos Liste des randonnees de la bibliotheque de type
	 *         'Vector<Rando>'.
	 */
	public Vector<Rando> getRandos() {
		return listeRandos;
	}

	/**
	 * RETOURNE LA LISTE DES RANDONNEES SOUS FORME DE TABLEAU DE CHAINES DE
	 * CARACTERES.
	 * 
	 * @param Aucun.
	 * @return boxRandos Liste des randonnees de la bibliotheque de type
	 *         'String[]'. Utilisee pour creer la 'JComboBox' des randonnees
	 *         dans la classe 'RechercheRando'.
	 */
	public String[] getNomRandos() {
		String[] boxRandos = new String[nbRandos + 1];
		boxRandos[0] = SYM_ALL;
		Iterator<Rando> i = listeRandos.iterator();
		int indice = 1;
		while (i.hasNext()) {
			boxRandos[indice] = i.next().getNom();
			indice++;
		}
		return boxRandos;
	}

	/**
	 * RECHERCHE UNE RANDONNEE DANS LA BIBLIOTHEQUE.
	 * 
	 * @param randoCherchee
	 *            : Nom complet de la randonnee cherchee, de type 'String'.
	 * @return resultat : Randonnee trouvee de type 'Rando'. Le resultat est
	 *         unique et certain par construction.
	 */
	public Rando rechercherRando(String randoCherchee) {
		Iterator<Rando> i = listeRandos.iterator();
		Rando randoTrouvee = null;
		while (i.hasNext()) {
			Rando serieCourante = i.next();
			if (serieCourante.getNom().compareTo(randoCherchee) == 0) {
				randoTrouvee = serieCourante;
				break;
			}
		}
		return randoTrouvee;
	}

	/**
	 * CREE UN FICHIER XML VIERGE POUR UNE NOUVELLE RANDONNEE.
	 * 
	 * @param nomRando
	 *            : Nom de la nouvelle randonnee de type 'String'.
	 * @param nbEtapes
	 *            : Nombre d'etapes de la nouvelle randonnee de type 'int'.
	 * @return Aucun.
	 */
	public void creerRando(String nomRando) {
		DocType xmlFormat = new DocType(BaliseXML.XML_RACINE, "format.dtd");
		Element newRacine = new Element(BaliseXML.XML_RACINE);
		Document newDocument = new Document(newRacine, xmlFormat);
		// Nom de la randonnee
		Element nom = new Element(BaliseXML.XML_NOM);
		nom.setText(nomRando);
		newRacine.addContent(nom);
		// Creation du fichier XML
		try {
			File newFichier = new File(dossierRandos + "/" + nomRando + ".xml");
			Format f = Format.getPrettyFormat();
			f.setExpandEmptyElements(true);
			XMLOutputter sortie = new XMLOutputter(f);
			sortie.output(newDocument, new FileOutputStream(newFichier));
			Rando r = new Rando(newFichier);
			listeRandos.addElement(r);
			nbRandos++;
			// Par defaut on ajoute une etape.
			r.addXML(new Etape(), 0, true);
			r.update();
			i.menuGaucheAjouterRando(r);
			// On passe automatiquement sur la vue de la bibliotheque.
			i.afficherBibliotheque();
			System.out.println("Ajout de la randonnee : " + r.getNom());
		} catch (java.io.IOException e) {
		}
	}

	/**
	 * TESTE SI UNE CHAINE DE CARACTERE CONTIENT UNIQUEMENT DES CHIFFRES.
	 * 
	 * @param chaine
	 *            : Chaine de caractere a tester de type 'String'.
	 * @return 'true' si la chaine peut etre convertir en 'int'. 'false' sinon.
	 */
	public static boolean isNumeric(String chaine) {
		try {
			Integer.parseInt(chaine);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	/**
	 * TESTE SI UNE CHAINE DE CARACTERE CONTIENT UNIQUEMENT DES CHIFFRES ET
	 * EVENTUELLEMENT UN POINT.
	 * 
	 * @param chaine
	 *            : Chaine de caractere a tester de type 'String'.
	 * @return 'true' si la chaine peut etre convertir en 'double', 'false'
	 *         sinon.
	 */
	public static boolean isDouble(String chaine) {
		try {
			Double.parseDouble(chaine);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	/**
	 * CONVERTIT UNE CHAINE DE CARACTERES EN ENTIER SI POSSIBLE.
	 * 
	 * @param str
	 *            : Chaine de caractere a convertir de type 'String'.
	 * @return resultat : Nombre entier equivalent a 'str' si la convertion est
	 *         possible. '0' sinon.
	 */
	public static int stringToInteger(String str) {
		int resultat = 0;
		if (isNumeric(str)) {
			resultat = Integer.parseInt(str);
		}
		return resultat;
	}

	/**
	 * AFFICHE TOUTES LES RANDONNEES DE LA BIBLIOTHEQUE (DEBUG).
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public void debugAffBib() {
		Iterator<Rando> i = listeRandos.iterator();
		while (i.hasNext()) {
			i.next().afficherRando();
		}
	}
}
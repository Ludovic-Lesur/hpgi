/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 07/02/2017
 */

package typedef;

public enum Heure {

	H5M0("5h00", 5.0),
	H5M30("5h30", 5.5),
	H6M0("6h00", 6.0),
	H6M30("6h30", 6.5),
	H7M0("7h00", 7.0),
	H7M30("7h30", 7.5),
	H8M0("8h00", 8.0),
	H8M30("8h30", 8.5),
	H9M0("9h00", 9.0),
	H9M30("9h30", 9.5),
	H10M0("10h00", 10.0),
	H10M30("10h30", 10.5),
	H11M0("11h00", 11.0),
	H11M30("11h30", 11.5),
	H12M0("12h00", 12.0),
	H12M30("12h30", 12.5),
	H13M0("13h00", 13.0),
	H13M30("13h30", 13.5),
	H14M0("14h00", 14.0);

	// Attributs de chaque élément de l'énumération.
	private final String symbol;
	private final String name;
	private final double heure;

	/**
	 * CONSTRUCTEUR DE L'ENUMERATION HEURE.
	 * 
	 * @param pName
	 *            Nom de l'heure de type 'String'.
	 * @param pHeure
	 *            Heure associée au nom, de type 'double'.
	 * @return Aucun.
	 */
	private Heure(String pName, double pHeure) {
		symbol = this.toString();
		name = pName;
		heure = pHeure;
	}

	/**
	 * RENVOIE LE NOM DE L'HEURE.
	 * 
	 * @param Aucun.
	 * @return name Nom de l'heure de type 'String'.
	 */
	public String getName() {
		return name;
	}

	/**
	 * RENVOIE LE SYMBOLE DE L'HEURE.
	 * 
	 * @param Aucun.
	 * @return symbol Symbole de l'heure utilisé dans les fichiers XML, de type
	 *         'String'.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * RENVOIE L'HEURE ASSOCIEE AU SYMBOLE.
	 * 
	 * @param Aucun.
	 * @return heure Heure de type 'double'.
	 */
	public double getHeure() {
		return heure;
	}

	/**
	 * RENVOIE L'HEURE CORRESPONDANT A UN SYMBOLE DONNE.
	 * 
	 * @param pSymbol
	 *            Symbole recherché de type 'String'.
	 * @return affectation Heure associée au symbole si la recherche a donné un
	 *         résultat. 'H5M0' sinon.
	 */
	public static Heure affecter(String pSymbol) {
		Heure affectation = H5M0;
		Heure[] listeHeures = Heure.values();
		int i = 0;
		for (i = 0; i < listeHeures.length; i++) {
			if (listeHeures[i].getSymbol().compareTo(pSymbol) == 0) {
				affectation = listeHeures[i];
				break;
			}
		}
		return affectation;
	}

	/**
	 * RENVOIE LA LISTE DES ITEMS.
	 * 
	 * @param Aucun.
	 * @return resultat Liste des noms d'heures, de type 'String[]'.
	 */
	public static String[] getNames() {
		Heure[] listeHeures = Heure.values();
		String[] resultat = new String[listeHeures.length];
		int i = 0;
		for (i = 0; i < listeHeures.length; i++) {
			resultat[i] = listeHeures[i].getName();
		}
		return resultat;
	}
}

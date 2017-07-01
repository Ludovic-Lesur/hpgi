/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 08/02/2017
 */

package typedef;

public enum Pause {

	H0M0("0h00", 0.0),
	H0M5("0h05", 0.083),
	H0M10("0h10", 0.167),
	H0M20("0h20", 0.333),
	H0M30("0h30", 0.5),
	H0M45("0h45", 0.75),
	H1M0("1h00", 1.0),
	H1M30("1h30", 1.5),
	H2M0("2h00", 2.0);

	// Attributs de chaque élément de l'énumération.
	private final String symbol;
	private final String name;
	private final double duree;

	/**
	 * CONSTRUCTEUR DE L'ENUMERATION PAUSE.
	 * 
	 * @param pName
	 *            Nom de la pause de type 'String'.
	 * @param pDuree
	 *            Durée de la pause, de type 'double'.
	 * @return Aucun.
	 */
	private Pause(String pName, double pDuree) {
		symbol = this.toString();
		name = pName;
		duree = pDuree;
	}

	/**
	 * RENVOIE LE NOM DE LA PAUSE.
	 * 
	 * @param Aucun.
	 * @return name Nom de la pause de type 'String'.
	 */
	public String getName() {
		return name;
	}

	/**
	 * RENVOIE LE SYMBOLE DE LA PAUSE.
	 * 
	 * @param Aucun.
	 * @return symbol Symbole de la pause utilisé dans les fichiers XML, de type
	 *         'String'.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * RENVOIE LA DUREE DE LA PAUSE.
	 * 
	 * @param Aucun.
	 * @return duree Durée de la pause de type 'double'.
	 */
	public double getDuree() {
		return duree;
	}

	/**
	 * RENVOIE LA PAUSE CORRESPONDANT A UN SYMBOLE DONNE.
	 * 
	 * @param pSymbol
	 *            Symbole recherché de type 'String'.
	 * @return affectation Pause associée au symbole si la recherche a donné un
	 *         résultat. 'H0M0' sinon.
	 */
	public static Pause affecter(String pSymbol) {
		Pause affectation = H0M0;
		Pause[] listePauses = Pause.values();
		int i = 0;
		for (i = 0; i < listePauses.length; i++) {
			if (listePauses[i].getSymbol().compareTo(pSymbol) == 0) {
				affectation = listePauses[i];
				break;
			}
		}
		return affectation;
	}

	/**
	 * RENVOIE LA LISTE DES ITEMS.
	 * 
	 * @param Aucun.
	 * @return resultat Liste des noms de pauses, de type 'String[]'.
	 */
	public static String[] getNames() {
		Pause[] listePauses = Pause.values();
		String[] resultat = new String[listePauses.length];
		int i = 0;
		for (i = 0; i < listePauses.length; i++) {
			resultat[i] = listePauses[i].getName();
		}
		return resultat;
	}
}

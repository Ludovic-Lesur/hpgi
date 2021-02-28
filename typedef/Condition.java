/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 05/02/2017
 */

package typedef;

public enum Condition {

	R("Hotel, Gite ou Refuge"),
	B("Bivouac");

	// Attributs de chaque element de l'enumeration.
	private final String symbol;
	private final String name;

	/**
	 * CONSTRUCTEUR DE L'ENUMERATION CONDITION.
	 * 
	 * @param pName		Nom de la condition de type 'String'.
	 * @return 			Aucun.
	 */
	private Condition(String pName) {
		symbol = this.toString();
		name = pName;
	}

	/**
	 * RENVOIE LE NOM DE LA CONDITION.
	 * 
	 * @param Aucun.
	 * @return name Nom de la condition de type 'String'.
	 */
	public String getName() {
		return name;
	}

	/**
	 * RENVOIE LE SYMBOLE DE LA CONDITION.
	 * 
	 * @param Aucun.
	 * @return symbol Symbole de la condition utilise dans les fichiers XML, de
	 *         type 'String'.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * RENVOIE LA CONDITION CORRESPONDANT A UN SYMBOLE DONNE.
	 * 
	 * @param pSymbol
	 *            Symbole recherche de type 'String'.
	 * @return affectation Condition associee au symbole si la recherche a donne
	 *         un resultat. 'R' sinon.
	 */
	public static Condition affecter(String pSymbol) {
		Condition affectation = R;
		Condition[] listeRepas = Condition.values();
		int i = 0;
		for (i = 0; i < listeRepas.length; i++) {
			if (listeRepas[i].getSymbol().compareTo(pSymbol) == 0) {
				affectation = listeRepas[i];
				break;
			}
		}
		return affectation;
	}

	/**
	 * RENVOIE LA LISTE DES ITEMS.
	 * 
	 * @param Aucun.
	 * @return resultat Liste des noms de condition, de type 'String[]'.
	 */
	public static String[] getNames() {
		Condition[] listeRepas = Condition.values();
		String[] resultat = new String[listeRepas.length];
		int i = 0;
		for (i = 0; i < listeRepas.length; i++) {
			resultat[i] = listeRepas[i].getName();
		}
		return resultat;
	}
}

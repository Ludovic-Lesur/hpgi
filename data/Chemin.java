/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 11/04/2016
 */

package data;

public class Chemin {

	// Attributs.
	private PointGeo debut;
	private PointGeo fin;
	private double longueur;
	private int denivele;
	private double temps; // Temps de parcours entre 'début' et 'fin' (en
							// heures).
	private double heureFin; // Heure d'arrivée au point "fin".

	/**
	 * CONSTRUCTEUR DE LA CLASSE CHEMIN.
	 * 
	 * @param pDebut
	 *            Point de début du chemin de type 'Point'.
	 * @param pFin
	 *            Point de fin du chemin de type 'Point'.
	 * @param denivPosCumule
	 *            Dénivelé positif cumulé (en m) depuis le départ de la rando.
	 * @param denivNegCumule
	 *            Dénivelé négatif cumulé (en m) depuis le départ de la rando.
	 * @return Aucun.
	 */
	public Chemin(PointGeo pDebut, PointGeo pFin, int denivPosCumule, int denivNegCumule) {
		debut = pDebut;
		fin = pFin;
		longueur = pFin.getPK() - pDebut.getPK();
		denivele = pFin.getAltitude() - pDebut.getAltitude();
		temps = calculerTemps(longueur, denivele, debut.getPK(), denivPosCumule, denivNegCumule)
				+ pDebut.getPause().getDuree();
	}

	/**
	 * CALCULE LE TEMPS DE PARCOURS DU CHEMIN EN FONCTION DES PARAMETRES
	 * PHYSIQUES.
	 * 
	 * @param distance
	 *            Distance à parcourir (en km) de type 'double'.
	 * @param denivele
	 *            Dénivelé entre le début et la fin du chemin (en m) de type
	 *            'int'.
	 * @param distCumulee
	 *            Distance déjà parcourue (en km) depuis le départ de la rando,
	 *            de type 'double'.
	 * @param denivPosCumule
	 *            Dénivelé positif cumulé (en m) depuis le départ de la rando,
	 *            de type 'int'.
	 * @param denivNegCumule
	 *            Dénivelé négatif cumulé (en m) depuis le départ de la rando,
	 *            de type 'int'.
	 * @return Temps de parcours du chemin (en heures) de type 'double'.
	 */
	public double calculerTemps(double distance, int denivele, double distCumulee, int denivPosCumule,
			int denivNegCumule) {
		double v;
		if (denivele > 0) {
			v = Stats.vitesseMon(denivPosCumule);
		} else {
			v = Stats.vitesseDes(denivNegCumule);
		}
		double deniv = (double) (Math.abs(denivele));
		return Stats.alpha
				* (Stats.coefH * ((distance) / (Stats.vitesseHor(distCumulee))) + Stats.coefV * ((deniv) / (v)));
	}

	/**
	 * RETOURNE LE POINT DE DEBUT DU CHEMIN.
	 * 
	 * @param Aucun.
	 * @return debut Point de début du chemin de type 'Point'.
	 */
	public PointGeo getDebut() {
		return debut;
	}

	/**
	 * RETOURNE LE POINT DE FIN DU CHEMIN.
	 * 
	 * @param Aucun.
	 * @return debut Point de fin du chemin de type 'Point'.
	 */
	public PointGeo getFin() {
		return fin;
	}

	/**
	 * RETOURNE LA LONGUEUR DU CHEMIN.
	 * 
	 * @param Aucun.
	 * @return longueur Longueur du chemin (en km) de type 'double'.
	 */
	public double getLongueur() {
		return longueur;
	}

	/**
	 * RETOURNE LE DENIVELE DU CHEMIN.
	 * 
	 * @param Aucun.
	 * @return denivele Dénivelé entre les points de début et de fin (en m) de
	 *         type 'int'.
	 */
	public int getDenivele() {
		return denivele;
	}

	/**
	 * RETOURNE LE TEMPS DE PARCOURS DU CHEMIN.
	 * 
	 * @param Aucun.
	 * @return temps Temps de parcours du chemin (en heures) de type 'double'.
	 */
	public double getTemps() {
		return temps;
	}

	/**
	 * MODIFIE L'HEURE DU POINT DE DEPART DE L'ETAPE.
	 * 
	 * @param newHoraire
	 *            Heure de départ de l'étape, de type 'double'.
	 * @return Aucun.
	 */
	public void setHeureDebut(double heureDepart) {
		debut.setHeure(heureDepart);
	}

	/**
	 * MODIFIE L'HEURE D'ARRIVEE AU POINT DE FIN.
	 * 
	 * @param newHoraire
	 *            Nouvelle heure d'arrivée au point fin, de type 'double'.
	 * @return Aucun.
	 */
	public void setHeureFin(double newHeureFin) {
		heureFin = newHeureFin;
		fin.setHeure(newHeureFin);
	}

	/**
	 * RETOURNE L'HEURE D'ARRIVEE AU POINT DE FIN.
	 * 
	 * @param Aucun.
	 * @return horaire Heure d'arrivée au point fin, de type 'double'.
	 */
	public double getHeureFin() {
		return heureFin;
	}

	/**
	 * RETOURNE L'ALTITUDE MINIMUM DU CHEMIN.
	 * 
	 * @param Aucun.
	 * @return altitudeMin Altitude minimum du chemin (en m) de type 'int'.
	 */
	public int getAltitudeMin() {
		return Integer.min(debut.getAltitude(), fin.getAltitude());
	}

	/**
	 * RETOURNE L'ALTITUDE MAXIMUM DU CHEMIN.
	 * 
	 * @param Aucun.
	 * @return altitudeMax Altitude maximum du chemin (en m) de type 'int'.
	 */
	public int getAltitudeMax() {
		return Integer.max(debut.getAltitude(), fin.getAltitude());
	}
}

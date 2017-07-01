/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 02/02/2017
 */

package data;

public class Stats {

	// Seuils à partir desquels les stats de progression baissent.
	public static final double seuilH = 20.0;
	public static final int seuilM = 1000;
	public static final int seuilD = 1000;

	// Vitesses verticales.
	public static final double monMax = 300.0;
	public static final double desMax = 500.0;
	// Vitesse horizontale.
	public static final double horMax = 4.0;

	// Coefficients de calibration.
	public static final double alpha = 0.95;
	public static final double coefH = 0.7;
	public static final double coefV = 1.3;

	/**
	 * CALCULE LA VITESSE DE MONTEE EN FONCTION DU DENIVELE POSITIF CUMULE.
	 * 
	 * @param denivPosCumule
	 *            Dénivelé positif accumulé en m.
	 * @return resultat Vitesse verticale de montée en m/h.
	 */
	public static double vitesseMon(double denivPosCumule) {
		double resultat = 0.0;
		if (denivPosCumule < seuilM) {
			resultat = monMax;
		} else {
			resultat = monMax - 0.25 * (denivPosCumule - seuilM);
		}
		return resultat;
	}

	/**
	 * CALCULE LA VITESSE DE DESCENTE EN FONCTION DU DENIVELE NEGATIF CUMULE.
	 * 
	 * @param denivNegCumule
	 *            Dénivelé négatif accumulé en m.
	 * @return resultat Vitesse verticale de descente en m/h.
	 */
	public static double vitesseDes(double denivNegCumule) {
		double resultat = 0.0;
		if (denivNegCumule < seuilD) {
			resultat = desMax;
		} else {
			resultat = desMax - 0.25 * (denivNegCumule - seuilD);
		}
		return resultat;
	}

	/**
	 * CALCULE LA VITESSE HORIZONTALE EN FONCTION DE LA DISTANCE CUMULEE.
	 * 
	 * @param distCumulee
	 *            Distance cumulée en km.
	 * @return resultat Vitesse horizontale en km/h.
	 */
	public static double vitesseHor(double distCumulee) {
		double resultat = 0.0;
		if (distCumulee < seuilH) {
			resultat = horMax;
		} else {
			resultat = horMax - 0.25 * (distCumulee - seuilH);
		}
		return resultat;
	}
}

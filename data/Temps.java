/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 11/04/2016
 */

package data;

public class Temps {

	/**
	 * AFFICHE UNE HEURE AU FORMAT "--hh--".
	 * 
	 * @param t
	 *            Temps absolu en heures.
	 * @return Aucun.
	 */
	public static String afficherHeure(double t) {
		// Convertion en heures et minutes.
		int heures = (int) Math.floor(t);
		double m = (t - (double) heures) * 60.0;
		int minutes = 5 * ((int) Math.floor(Math.round(m / 5.0)));
		if (minutes == 60) {
			heures++;
			minutes = 0;
		}
		String strHeures = Integer.toString(heures);
		String strMinutes;
		// Ajout d'un 0 pour les minutes de 00 à 09.
		if (minutes < 10) {
			strMinutes = "0" + Integer.toString(minutes);
		} else {
			strMinutes = Integer.toString(minutes);
		}
		return (strHeures + "h" + strMinutes);
	}
}

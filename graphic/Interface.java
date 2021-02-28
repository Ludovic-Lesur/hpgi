/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 03/02/2017
 */

package graphic;

import data.*;
import java.io.*;
import javax.swing.*;
import org.jdom2.*;
import java.awt.*;

public class Interface {

	public final static Font police = new Font("Averia", 1, 10);
	public final static int COMBOBOX_HEIGHT = 20;
	public final static Color BACKGROUND = new Color(0, 100, 0);

	// Fenetre principale.
	private JFrame fenetre;
	private JPanel panel;
	private GridBagConstraints gbc;
	private Bibliotheque bibRandos;

	// Elements graphiques
	private MenuGauche menuGauche;
	private Tableau tableau;
	private MenuDroit menuDroit;

	/**
	 * CONSTRUCTEUR DE LA CLASSE INTERFACE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public Interface() throws IOException, JDOMException {

		bibRandos = new Bibliotheque(this);

		// Creation de l'interface
		fenetre = new JFrame();
		fenetre.setTitle("Preparation de randonnee");
		fenetre.setLocationRelativeTo(null);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Panel
		panel = new JPanel();
		panel.setBackground(BACKGROUND);
		panel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 20, 20);

		// Ajout des elements
		gbc.anchor = GridBagConstraints.WEST;
		menuGauche = new MenuGauche(this, panel, gbc, bibRandos);

		gbc.anchor = GridBagConstraints.CENTER;
		tableau = new Tableau(this, panel, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		menuDroit = new MenuDroit(this, panel, gbc);

		// Affichage de l'interface
		fenetre.setContentPane(panel);
		fenetre.setVisible(true);
	}

	/**
	 * RENVOIE LA BIBLIOTHEQUE DE RANDONNEES ASSOCIEE A L'INTERFACE.
	 * 
	 * @param Aucun.
	 * @return bibRandos Bibliotheque de randonnees de type 'Bibliotheque'.
	 */
	public Bibliotheque getBibliotheque() {
		return bibRandos;
	}

	/**
	 * DEFINIT L'AFFICHAGE GLOBAL POUR LA BIBLIOTHEQUE DE RANDONNEES.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public void afficherBibliotheque() {
		menuGauche.update(panel, gbc, bibRandos);
		tableau.update(bibRandos);
		menuDroit.update(bibRandos);
		fenetre.setContentPane(panel);
	}

	/**
	 * AJOUTE UNE RANDONNEE A L'INTERFACE.
	 * 
	 * @param r
	 *            Nouvelle randonnee a ajouter de type 'Rando'.
	 * @return Aucun.
	 */
	public void menuGaucheAjouterRando(Rando r) {
		menuGauche.ajouterRando(r);
	}

	/**
	 * DEFINIT L'AFFICHAGE GLOBAL POUR UNE RANDONNEE.
	 * 
	 * @param randoTrouvee
	 *            Randonnee dont on doit afficher les informations, de type
	 *            'Rando'.
	 * @return Aucun.
	 */
	public void afficherResultat(Rando randoTrouvee) {
		menuGauche.update(panel, gbc, randoTrouvee);
		tableau.update(randoTrouvee);
		menuDroit.update(randoTrouvee);
		fenetre.setContentPane(panel);
	}

	/**
	 * DEFINIT L'AFFICHAGE GLOBAL POUR UNE ETAPE.
	 * 
	 * @param etapeTrouvee
	 *            Etape dont on doit afficher les informations, de type 'Etape'.
	 * @return Aucun.
	 */
	public void afficherResultat(Etape etapeTrouvee) {
		menuGauche.update(panel, gbc, etapeTrouvee);
		tableau.update(etapeTrouvee);
		menuDroit.update(etapeTrouvee);
		fenetre.setContentPane(panel);
	}

	/**
	 * TRANSMETS LA RANDONNEE SELECTIONNEE DU TABLEAU AUX AUTRES CLASSES
	 * GRAPHIQUES.
	 * 
	 * @param newRando
	 *            Objet 'Rando' selectionnee dans le tableau principal.
	 * @return Aucun.
	 */
	public void setRando(Rando newRando) {
		menuDroit.setRando(newRando);
		menuGauche.setRandoCourante(newRando);
	}

	/**
	 * TRANSMETS L'ETAPE SELECTIONNEE DU TABLEAU AUX AUTRES CLASSES GRAPHIQUES.
	 * 
	 * @param newEtape
	 *            Objet 'Etape' selectionne dans le tableau principal.
	 * @return Aucun.
	 */
	public void setEtape(Etape newEtape) {
		menuDroit.setEtape(newEtape);
	}

	/**
	 * TRANSMETS LE POINT SELECTIONNE DU TABLEAU AUX AUTRES CLASSES GRAPHIQUES.
	 * 
	 * @param newPoint
	 *            Objet 'PointGeo' selectionne dans le tableau principal.
	 * @return Aucun.
	 */
	public void setPoint(PointGeo newPoint) {
		menuDroit.setPointGeo(newPoint);
	}

	/**
	 * PERMET DE SAVOIR SI UNE LIGNE DU TABLEAU EST SELECTIONNEE.
	 * 
	 * @param Aucun.
	 * @return 'true' si une ligne du tableau a ete selectionnee par un clic
	 *         souris. 'false' sinon.
	 */
	public boolean rowIsSelected() {
		return tableau.rowIsSelected();
	}

	/**
	 * MAIN FUNCTION.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public static void main(String[] args) throws IOException, JDOMException {
		// Lancement de l'interface
		new Interface();
	}
}

/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 04/02/2017
 */

package graphic;

import data.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FenetreSuppression extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Lien avec les autres classes graphiques.
	private JFrame fenetre;
	private JPanel panel;
	private GridBagConstraints gbc;
	private Interface i;
	private Rando randoCourante;
	private Etape etapeCourante;
	private PointGeo pointCourant;
	private boolean etape;

	// Elements graphiques et attributs.
	private JLabel question;
	private JLabel nom;
	private JButton oui;
	private JButton non;

	/**
	 * CONSTRUCTEUR DE LA CLASSE FENETRESUPPRESSION.
	 * 
	 * @param pI
	 *            Interface graphique mere, de type 'Interface'.
	 * @param pRando
	 *            Randonnee dont on doit supprimer un attribut (etape ou point),
	 *            de type 'Rando'.
	 * @param pEtape
	 *            Objet 'Etape a supprimer si pBoolEtape = 'true'. 'null' sinon.
	 * @param pPointGeo
	 *            Objet 'PointGeo' a supprimer si pBoolEtape = 'false'. 'null'
	 *            sinon.
	 * @param pBoolEtape
	 *            Booleen indiquant l'attribut a supprimer. 'true' = suppression
	 *            d'une etape. 'false' = suppression d'un point.
	 * @return Aucun.
	 */
	public FenetreSuppression(Interface pI, Rando pRando, Etape pEtape, PointGeo pPointGeo, boolean pBoolEtape) {

		i = pI;
		randoCourante = pRando;
		etapeCourante = pEtape;
		pointCourant = pPointGeo;
		etape = pBoolEtape;

		// Creation de l'interface
		fenetre = new JFrame();
		if (etape == true) {
			fenetre.setTitle("Supprimer etape");
		} else {
			fenetre.setTitle("Supprimer point");
		}
		fenetre.setSize(300, 200);
		fenetre.setResizable(false);
		fenetre.setLocationRelativeTo(null);

		// Panel
		panel = new JPanel();
		panel.setBackground(Interface.BACKGROUND);
		panel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 10, 10);

		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		question = new JLabel();
		if (etape == true) {
			question.setText("Supprimer l'etape ?");
		} else {
			question.setText("Supprimer le point ?");
		}
		question.setFont(Interface.police);
		question.setForeground(Color.yellow);
		panel.add(question, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		nom = new JLabel();
		if (etape == true) {
			nom.setText(etapeCourante.getNom());
		} else {
			nom.setText(pointCourant.getNom());
		}

		nom.setFont(Interface.police);
		nom.setForeground(Color.white);
		panel.add(nom, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		oui = new JButton();
		oui.setText("Oui");
		oui.setFont(Interface.police);
		oui.setForeground(new Color(100, 200, 0));
		oui.addActionListener(this);
		panel.add(oui, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 1;
		gbc.gridy = 2;
		non = new JButton();
		non.setText("Non");
		non.setFont(Interface.police);
		non.setForeground(Color.red);
		non.addActionListener(this);
		panel.add(non, gbc);

		// Affichage de la fenetre
		fenetre.setContentPane(panel);
		fenetre.setVisible(true);
	}

	/**
	 * DEFINIT LES ACTIONS DES BOUTONS.
	 * 
	 * @param e
	 *            Evenement declenche par l'appui sur un bouton.
	 * @return Aucun.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == oui) {
			if (etape == true) {
				randoCourante.deleteXML(etapeCourante.getNumero());
				randoCourante.update();
				Rando randoUpdated = i.getBibliotheque().rechercherRando(randoCourante.getNom());
				i.afficherResultat(randoUpdated);
			} else {
				randoCourante.deleteXML(etapeCourante.getNumero(), pointCourant.getPK());
				randoCourante.update();
				Etape etapeUpdated = randoCourante.rechercherEtape(etapeCourante.getNumero());
				i.afficherResultat(etapeUpdated);
				i.setEtape(etapeUpdated);
			}

			fenetre.setVisible(false);
			fenetre.dispose();
		}
		if (e.getSource() == non) {
			fenetre.setVisible(false);
			fenetre.dispose();
		}
	}
}

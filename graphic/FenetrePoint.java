/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 08/02/2017
 */

package graphic;

import data.*;
import typedef.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class FenetrePoint extends JFrame implements ActionListener, DocumentListener {

	private static final long serialVersionUID = 1L;

	// Lien avec les autres classes graphiques
	private JPanel panel;
	private GridBagConstraints gbc;
	private Interface i;
	private Rando randoCourante;
	private int numEtapeCourant;

	// Elements graphiques et attributs
	private JFrame fenetre;
	private String toponymeCourant;
	private double pkCourant;
	private int altitudeCourante;
	private Pause pauseCourante;
	private PointGeo point;

	private JLabel toponyme;
	private JTextField champToponyme;

	private JLabel pk;
	private JTextField champPk;
	private double ancienPk;

	private JLabel altitude;
	private JTextField champAltitude;

	private JLabel pause;
	private JComboBox<String> choixPause;

	private JButton ok;
	private JButton annuler;

	private boolean modif;

	/**
	 * CONSTRUCTEUR DE LA CLASSE FENETREPOINT.
	 * 
	 * @param pI
	 *            Interface graphique mère, de type 'Interface'.
	 * @param pPoint
	 *            Point dont on doit modifier les paramètres de type 'PointGeo'.
	 * @param pModif
	 *            Booléen indiquant si la fenêtre doit s'ouvrir en mode ajout ou
	 *            en mode modification. 'true' = modification d'une vue
	 *            existante. 'false' = ajout d'une nouvelle vue.
	 * @param pRando
	 *            Randonnée courante de type 'Rando'.
	 * @param pNumEtape
	 *            Numéro de l'étape à laquelle doit être ajouté le point.
	 * @return Aucun.
	 */
	public FenetrePoint(Interface pI, PointGeo pPoint, boolean pModif, Rando pRando, int pNumEtape) {

		i = pI;
		point = pPoint;
		modif = pModif;
		randoCourante = pRando;
		numEtapeCourant = pNumEtape;
		if (modif == true) {
			ancienPk = point.getPK();
		}

		// Création de l'interface
		fenetre = new JFrame();
		if (modif == true) {
			fenetre.setTitle("Modifier point");
		} else {
			fenetre.setTitle("Ajouter point");
		}
		fenetre.setSize(450, 300);
		fenetre.setResizable(false);
		fenetre.setLocationRelativeTo(null);

		// Panel
		panel = new JPanel();
		panel.setBackground(Interface.BACKGROUND);
		panel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		gbc.gridx = 0;
		gbc.gridy = 0;
		toponyme = new JLabel("Toponyme");
		toponyme.setFont(Interface.police);
		toponyme.setForeground(Color.yellow);
		panel.add(toponyme, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy = 0;
		champToponyme = new JTextField(30);
		champToponyme.setFont(Interface.police);
		champToponyme.getDocument().addDocumentListener(this);
		panel.add(champToponyme, gbc);
		selectToponyme(point);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		pk = new JLabel("P.K. (km)");
		pk.setFont(Interface.police);
		pk.setForeground(Color.yellow);
		panel.add(pk, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy = 1;
		champPk = new JTextField(10);
		champPk.setFont(Interface.police);
		champPk.getDocument().addDocumentListener(this);
		panel.add(champPk, gbc);
		selectPk(point);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		altitude = new JLabel("Altitude (m)");
		altitude.setFont(Interface.police);
		altitude.setForeground(Color.yellow);
		panel.add(altitude, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy = 2;
		champAltitude = new JTextField(10);
		champAltitude.setFont(Interface.police);
		champAltitude.getDocument().addDocumentListener(this);
		panel.add(champAltitude, gbc);
		selectAltitude(point);

		gbc.insets.bottom = 30;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		pause = new JLabel("Temps de pause");
		pause.setFont(Interface.police);
		pause.setForeground(Color.yellow);
		panel.add(pause, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy = 3;
		choixPause = new JComboBox<String>(Pause.getNames());
		choixPause.setFont(Interface.police);
		choixPause.addItemListener(new ItemState());
		panel.add(choixPause, gbc);
		selectPause(point);

		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 4;
		ok = new JButton();
		ok.setText("Enregistrer");
		ok.setFont(Interface.police);
		ok.setForeground(new Color(100, 200, 0));
		ok.addActionListener(this);
		panel.add(ok, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 2;
		gbc.gridy = 4;
		annuler = new JButton("Annuler");
		annuler.setFont(Interface.police);
		annuler.setForeground(Color.red);
		annuler.addActionListener(this);
		panel.add(annuler, gbc);

		// Affichage de la fenêtre
		checkTextFields();
		fenetre.setContentPane(panel);
		fenetre.setVisible(true);
	}

	/**
	 * SELECTIONNE L'ITEM CORRECT EN FONCTION DU TOPONYME ACTUEL.
	 * 
	 * @param p
	 *            Point dont on doit modifier les paramètres.
	 * @return Aucun.
	 */
	private void selectToponyme(PointGeo p) {
		if (modif == true) {
			toponymeCourant = p.getNom();
			champToponyme.setText(toponymeCourant);
		}
	}

	/**
	 * SELECTIONNE L'ITEM CORRECT EN FONCTION DU PK ACTUEL.
	 * 
	 * @param p
	 *            Point dont on doit modifier les paramètres.
	 * @return Aucun.
	 */
	private void selectPk(PointGeo p) {
		if (modif == true) {
			pkCourant = p.getPK();
		} else {
			pkCourant = 0.0;
		}
		champPk.setText(Double.toString(pkCourant));
	}

	/**
	 * SELECTIONNE L'ITEM CORRECT EN FONCTION DE L'ALTITUDE ACTUELLE.
	 * 
	 * @param p
	 *            Point dont on doit modifier les paramètres.
	 * @return Aucun.
	 */
	private void selectAltitude(PointGeo p) {
		if (modif == true) {
			altitudeCourante = p.getAltitude();
		} else {
			altitudeCourante = 0;
		}
		champAltitude.setText(Integer.toString(altitudeCourante));
	}

	/**
	 * SELECTIONNE L'ITEM CORRECT EN FONCTION DU TEMPS DE PAUSE ACTUEL.
	 * 
	 * @param p
	 *            Point dont on doit modifier les paramètres.
	 * @return Aucun.
	 */
	private void selectPause(PointGeo p) {
		if (modif == true) {
			pauseCourante = p.getPause();
			int j;
			for (j = 0; j < choixPause.getItemCount(); j++) {
				if (choixPause.getItemAt(j).compareTo(pauseCourante.getName()) == 0) {
					choixPause.setSelectedIndex(j);
					break;
				}
			}
		} else {
			pauseCourante = Pause.H0M0;
		}
	}

	/**
	 * DEFINIT LES ACTIONS DE LA LISTE DEROULANTE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	class ItemState implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == choixPause) {
				pauseCourante = Pause.values()[choixPause.getSelectedIndex()];
			}
		}
	}

	/**
	 * VERIFIE SI LE TOPONYME SELECTIONNE N'EST PAS VIDE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private boolean checkToponyme() {
		boolean correct = true;
		if (champToponyme.getText().isEmpty()) {
			correct = false;
		} else {
			toponymeCourant = champToponyme.getText();
		}
		return correct;
	}

	/**
	 * VERIFIE SI LE PK SELECTIONNE EST DE TYPE 'DOUBLE' ET QU'IL N'EXITE PAS
	 * ENCORE SUR L'ETAPE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private boolean checkPk() {
		boolean correct = false;
		if (champPk != null) {
			if (Bibliotheque.isDouble(champPk.getText())) {
				pkCourant = Double.parseDouble(champPk.getText());
				Etape etapeCourante = randoCourante.rechercherEtape(numEtapeCourant);
				correct = !(etapeCourante.existeDeja(pkCourant));
				if (pkCourant == ancienPk) {
					correct = true;
				}
			}
		}
		return correct;
	}

	/**
	 * VERIFIE SI L'ALTITUDE SELECTIONNEE EST DE TYPE 'INT'.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private boolean checkAltitude() {
		boolean correct = false;
		if (Bibliotheque.isNumeric(champAltitude.getText())) {
			correct = true;
			altitudeCourante = Integer.parseInt(champAltitude.getText());
		}
		return correct;
	}

	/**
	 * VERIFIE TOUS LES CHAMPS DE SAISIE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void checkTextFields() {
		if (ok != null) {
			if (checkToponyme() && checkPk() && checkAltitude()) {
				ok.setEnabled(true);
			} else {
				ok.setEnabled(false);
			}
		}
	}

	/**
	 * DEFINIT LES ACTIONS DES BOUTONS.
	 * 
	 * @param e
	 *            Evènement déclenché par l'appui sur un bouton.
	 * @return Aucun.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {

			// Mise à jour de la randonnée avant ajout du point.
			randoCourante.update();

			if (modif == true) {
				randoCourante.modifyXML(numEtapeCourant, ancienPk, toponymeCourant);
				randoCourante.modifyXML(numEtapeCourant, ancienPk, pkCourant);
				randoCourante.modifyXML(numEtapeCourant, ancienPk, altitudeCourante);
				randoCourante.modifyXML(numEtapeCourant, ancienPk, pauseCourante);
			} else {
				point = new PointGeo(toponymeCourant, pkCourant, altitudeCourante, pauseCourante);
				randoCourante.addXML(numEtapeCourant, point);
			}

			// Actualisation de l'affichage.
			randoCourante.update();
			Etape etapeUpdated = randoCourante.rechercherEtape(numEtapeCourant);
			i.afficherResultat(etapeUpdated);

			fenetre.setVisible(false);
			fenetre.dispose();
		}
		if (e.getSource() == annuler) {
			fenetre.setVisible(false);
			fenetre.dispose();
		}
	}

	/**
	 * FONCTIONS DE VERIFICATION DE SAISIE CLAVIER.
	 * 
	 * @param e
	 *            Evènement déclenché par une saisie clavier.
	 * @return Aucun.
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		checkTextFields();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		checkTextFields();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		checkTextFields();
	}
}
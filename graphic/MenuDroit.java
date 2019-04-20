/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 07/02/2017
 */

package graphic;

import data.*;
import typedef.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuDroit extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Lien avec les autres classes graphiques.
	private Interface i;
	private JPanel panel;
	private GridBagConstraints gbc;

	// Eléments graphiques.
	private JLabel titre1;

	private JLabel heureDepart;
	private JComboBox<String> choixHeureDepart;

	private JLabel petitDejeuner;
	private JComboBox<String> choixPetitDejeuner;

	private JLabel lieuMidi;
	private JComboBox<String> choixLieuMidi;

	private JLabel repasMidi;
	private JComboBox<String> choixRepasMidi;

	private JLabel ravitaillement;
	private JComboBox<String> choixRavitaillement;
	boolean enable; // Permet de désactiver l'interruption ItemChanged lorsqu'on
					// met à jour la liste des lieux.

	private JLabel soir;
	private JComboBox<String> choixSoir;

	private JLabel nuit;
	private JComboBox<String> choixNuit;

	private JLabel titre2;

	private JButton ajouter;
	private JButton modifier;
	private JButton supprimer;

	private JLabel titre3;

	private JButton navigation;

	private Rando selectedRando;
	private Etape selectedEtape;
	private PointGeo selectedPoint;
	private int mode; // 0 = affichage de toutes les randonnées.
						// 1 = affichage d'une randonnée.
						// 2 = affichage d'une étape.

	/**
	 * CONSTRUCTEUR DE LA CLASSE MENUDROIT.
	 * 
	 * @param pI
	 *            Interface graphique mère, de type 'Interface'.
	 * @param mainPanel
	 *            Panel de l'interface graphique mère, de type 'JPanel'.
	 * @param mainGbc
	 *            Contraintes de l'interface graphique mère, de type
	 *            'GridBagConstraints'.
	 * @return Aucun.
	 */
	public MenuDroit(Interface pI, JPanel mainPanel, GridBagConstraints mainGbc) {

		i = pI;

		mainGbc.gridx = 2;
		mainGbc.gridy = 0;

		panel = new JPanel(new GridBagLayout());
		panel.setBackground(Interface.BACKGROUND);
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 0;
		titre1 = new JLabel("ORGANISATION  ETAPE");
		titre1.setFont(new Font(Interface.police.getFontName(), 1, 13));
		titre1.setForeground(Color.white);
		panel.add(titre1, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		heureDepart = new JLabel("Heure de départ");
		heureDepart.setFont(Interface.police);
		heureDepart.setForeground(Color.yellow);
		panel.add(heureDepart, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		petitDejeuner = new JLabel("Petit déjeuner");
		petitDejeuner.setFont(Interface.police);
		petitDejeuner.setForeground(Color.yellow);
		panel.add(petitDejeuner, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		lieuMidi = new JLabel("Lieu midi");
		lieuMidi.setFont(Interface.police);
		lieuMidi.setForeground(Color.yellow);
		panel.add(lieuMidi, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		repasMidi = new JLabel("Repas midi");
		repasMidi.setFont(Interface.police);
		repasMidi.setForeground(Color.yellow);
		panel.add(repasMidi, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		ravitaillement = new JLabel("Ravitaillement");
		ravitaillement.setFont(Interface.police);
		ravitaillement.setForeground(Color.yellow);
		panel.add(ravitaillement, gbc);
		enable = true;

		gbc.gridx = 0;
		gbc.gridy = 6;
		soir = new JLabel("Repas soir");
		soir.setFont(Interface.police);
		soir.setForeground(Color.yellow);
		panel.add(soir, gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;
		nuit = new JLabel("Nuit");
		nuit.setFont(Interface.police);
		nuit.setForeground(Color.yellow);
		panel.add(nuit, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy = 1;
		choixHeureDepart = new JComboBox<String>(Heure.getNames());
		choixHeureDepart.setFont(Interface.police);
		choixHeureDepart.addItemListener(new ItemState());
		panel.add(choixHeureDepart, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		choixPetitDejeuner = new JComboBox<String>(Condition.getNames());
		choixPetitDejeuner.setFont(Interface.police);
		choixPetitDejeuner.addItemListener(new ItemState());
		panel.add(choixPetitDejeuner, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		choixLieuMidi = new JComboBox<String>();
		choixLieuMidi.setFont(Interface.police);
		choixLieuMidi.addItemListener(new ItemState());
		panel.add(choixLieuMidi, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		choixRepasMidi = new JComboBox<String>(Condition.getNames());
		choixRepasMidi.setFont(Interface.police);
		choixRepasMidi.addItemListener(new ItemState());
		panel.add(choixRepasMidi, gbc);

		gbc.gridx = 1;
		gbc.gridy = 5;
		choixRavitaillement = new JComboBox<String>();
		choixRavitaillement.setFont(Interface.police);
		choixRavitaillement.addItemListener(new ItemState());
		panel.add(choixRavitaillement, gbc);

		gbc.gridx = 1;
		gbc.gridy = 6;
		choixSoir = new JComboBox<String>(Condition.getNames());
		choixSoir.setFont(Interface.police);
		choixSoir.addItemListener(new ItemState());
		panel.add(choixSoir, gbc);

		gbc.gridx = 1;
		gbc.gridy = 7;
		choixNuit = new JComboBox<String>(Condition.getNames());
		choixNuit.setFont(Interface.police);
		choixNuit.addItemListener(new ItemState());
		panel.add(choixNuit, gbc);

		gbc.insets.top = 60;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 8;
		titre2 = new JLabel(" ");
		titre2.setFont(new Font(Interface.police.getFontName(), 1, 13));
		titre2.setForeground(Color.white);
		panel.add(titre2, gbc);

		gbc.insets.top = 10;
		gbc.insets.bottom = 60;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 9;
		ajouter = new JButton("Ajouter");
		ajouter.setFont(Interface.police);
		ajouter.addActionListener(this);
		panel.add(ajouter, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 1;
		gbc.gridy = 9;
		modifier = new JButton("Modifier");
		modifier.setFont(Interface.police);
		modifier.addActionListener(this);
		panel.add(modifier, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 2;
		gbc.gridy = 9;
		supprimer = new JButton("Supprimer");
		supprimer.setFont(Interface.police);
		supprimer.addActionListener(this);
		panel.add(supprimer, gbc);

		gbc.insets.bottom = 10;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 10;
		titre3 = new JLabel("NAVIGATION");
		titre3.setFont(new Font(Interface.police.getFontName(), 1, 13));
		titre3.setForeground(Color.white);
		panel.add(titre3, gbc);

		gbc.gridx = 0;
		gbc.gridy = 11;
		navigation = new JButton("Retour randonnée");
		navigation.setFont(Interface.police);
		navigation.addActionListener(this);
		panel.add(navigation, gbc);

		mode = 0;
		organisation(false);
		ajouter.setEnabled(false);
		modifier.setEnabled(false);
		supprimer.setEnabled(false);
		disableNavigation();
		mainPanel.add(panel, mainGbc);

		selectedRando = new Rando();
		selectedEtape = new Etape();
		selectedPoint = new PointGeo();
	}

	/**
	 * DESACTIVE LE BOUTON DE NAVIGATION.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void disableNavigation() {
		navigation.setEnabled(false);
		navigation.setText("Afficher");
	}

	/**
	 * ACTIVE OU DESACTIVE LES BOUTONS DE MODIFICATION ET DE SUPPRESSION.
	 * 
	 * @param mode
	 *            Booléen indiquant l'action. 'false' = désactivation des
	 *            boutons. 'true' = activation des boutons.
	 * @return Aucun.
	 */
	private void modifSuppr(boolean mode) {
		modifier.setEnabled(mode);
		supprimer.setEnabled(mode);
	}

	/**
	 * ACTIVE OU DESACTIVE LES BOUTONS D'ORGANISATION DE L'ETAPE.
	 * 
	 * @param mode
	 *            Booléen indiquant l'action. 'false' = désactivation des
	 *            boutons. 'true' = activation des boutons.
	 * @return Aucun.
	 */
	private void organisation(boolean mode) {
		choixHeureDepart.setEnabled(mode);
		choixPetitDejeuner.setEnabled(mode);
		choixLieuMidi.setEnabled(mode);
		choixRepasMidi.setEnabled(mode);
		choixRavitaillement.setEnabled(mode);
		choixSoir.setEnabled(mode);
		choixNuit.setEnabled(mode);
	}

	/**
	 * DEFINIT LA RANDONNEE COURANTE.
	 * 
	 * @param newRando
	 *            Randonnée sélectionnée dans le tableau de type 'Rando'.
	 * @return Aucun.
	 */
	public void setRando(Rando newRando) {
		selectedRando = newRando;
		navigation.setEnabled(true);
		navigation.setText("Afficher " + selectedRando.getNom().replace("_", " "));
	}

	/**
	 * DEFINIT L'ETAPE COURANTE.
	 * 
	 * @param newEtape
	 *            Etape sélectionnée dans le tableau de type 'Etape'.
	 * @return Aucun.
	 */
	public void setEtape(Etape newEtape) {
		enable = false;
		selectedEtape = newEtape;
		navigation.setEnabled(true);
		navigation.setText("Afficher étape " + selectedEtape.getNumero());
		// Choix des lieux.
		choixLieuMidi.removeAllItems();
		choixLieuMidi.addItem(" ");
		choixRavitaillement.removeAllItems();
		choixRavitaillement.addItem(" ");
		int nbLieux = selectedEtape.getPoints().size();
		int k = 0;
		for (k = 0; k < nbLieux; k++) {
			choixLieuMidi.addItem(selectedEtape.getPoints().elementAt(k).getNom());
			choixRavitaillement.addItem(selectedEtape.getPoints().elementAt(k).getNom());
		}
		// Affichage des paramètres courants.
		selectOrganisation();
		modifSuppr(true);
		if (selectedRando.getNumEtapes() <= 1) {
			supprimer.setEnabled(false);
		}
		enable = true;
	}

	/**
	 * RENSEIGNE LES INFORMATIONS D'ORGANISATION EN FONCTION DE L'ETAPE
	 * COURANTE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void selectOrganisation() {
		// Heure de départ.
		int k = 0;
		for (k = 0; k < choixHeureDepart.getItemCount(); k++) {
			if (choixHeureDepart.getItemAt(k).compareTo(selectedEtape.getHeureDepart().getName()) == 0) {
				choixHeureDepart.setSelectedIndex(k);
				break;
			}
		}
		// Petit déjeuner.
		for (k = 0; k < choixPetitDejeuner.getItemCount(); k++) {
			if (choixPetitDejeuner.getItemAt(k).compareTo(selectedEtape.getDejeuner().getName()) == 0) {
				choixPetitDejeuner.setSelectedIndex(k);
				break;
			}
		}
		// Lieu du repas de midi.
		for (k = 0; k < choixLieuMidi.getItemCount(); k++) {
			if (k == selectedEtape.getLieuMidi()) {
				choixLieuMidi.setSelectedIndex(k);
				break;
			}
		}
		// Repas de midi.
		for (k = 0; k < choixRepasMidi.getItemCount(); k++) {
			if (choixRepasMidi.getItemAt(k).compareTo(selectedEtape.getMidi().getName()) == 0) {
				choixRepasMidi.setSelectedIndex(k);
				break;
			}
		}
		// Lieu de ravitaillement.
		for (k = 0; k < choixRavitaillement.getItemCount(); k++) {
			if (k == selectedEtape.getRavitaillement()) {
				choixRavitaillement.setSelectedIndex(k);
				break;
			}
		}
		// Repas du soir.
		for (k = 0; k < choixSoir.getItemCount(); k++) {
			if (choixSoir.getItemAt(k).compareTo(selectedEtape.getSoir().getName()) == 0) {
				choixSoir.setSelectedIndex(k);
				break;
			}
		}
		// Nuit.
		for (k = 0; k < choixNuit.getItemCount(); k++) {
			if (choixNuit.getItemAt(k).compareTo(selectedEtape.getNuit().getName()) == 0) {
				choixNuit.setSelectedIndex(k);
				break;
			}
		}
	}

	/**
	 * DEFINIT LE POINT COURANT.
	 * 
	 * @param newPoint
	 *            Point sélectionné dans le tableau de type 'PointGeo'.
	 * @return Aucun.
	 */
	public void setPointGeo(PointGeo newPoint) {
		selectedPoint = newPoint;
		modifSuppr(true);
		if (selectedEtape.getNumChemins() <= 1) {
			supprimer.setEnabled(false);
		}
	}

	/**
	 * DEFINIT L'AFFICHAGE POUR UNE BIBLIOTHEQUE DE RANDONNEES.
	 * 
	 * @param b
	 *            Bibliothèque de randonnées à afficher de type 'Bibliotheque'.
	 * @return Aucun.
	 */
	public void update(Bibliotheque b) {
		disableNavigation();
		modifSuppr(false);
		titre2.setText(" ");
		organisation(false);
		ajouter.setEnabled(false);
		mode = 0;
	}

	/**
	 * DEFINIT L'AFFICHAGE POUR UNE RANDONNEE.
	 * 
	 * @param r
	 *            Randonnées à afficher de type 'Rando'.
	 * @return Aucun.
	 */
	public void update(Rando r) {
		disableNavigation();
		modifSuppr(false);
		titre2.setText("ETAPES");
		organisation(false);
		ajouter.setEnabled(true);
		mode = 1;
	}

	/**
	 * DEFINIT L'AFFICHAGE POUR UNE ETAPE.
	 * 
	 * @param e
	 *            Etape à afficher de type 'Etape'.
	 * @return Aucun.
	 */
	public void update(Etape e) {
		navigation.setEnabled(true);
		navigation.setText("Afficher " + selectedRando.getNom().replace("_", " "));
		System.out.println("Debug : " + selectedRando.getNom().replace("_", " "));
		modifSuppr(false);
		titre2.setText("POINTS");
		organisation(true);
		ajouter.setEnabled(true);
		mode = 2;
	}

	/**
	 * DEFINIT LES ACTIONS DES LISTES DEROULANTES.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	class ItemState implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == choixHeureDepart) {
				selectedRando.modifyXML(selectedEtape.getNumero(), Heure.values()[choixHeureDepart.getSelectedIndex()]);
			}

			if (e.getSource() == choixPetitDejeuner) {
				selectedRando.modifyXML(selectedEtape.getNumero(), Condition.values()[choixPetitDejeuner.getSelectedIndex()], 0);
			}

			if ((e.getSource() == choixLieuMidi) && (enable == true)) {
				selectedRando.modifyXML(selectedEtape.getNumero(), Integer.toString(choixLieuMidi.getSelectedIndex()), true);
			}

			if (e.getSource() == choixRepasMidi) {
				selectedRando.modifyXML(selectedEtape.getNumero(), Condition.values()[choixRepasMidi.getSelectedIndex()], 1);
			}

			if ((e.getSource() == choixRavitaillement) && (enable == true)) {
				selectedRando.modifyXML(selectedEtape.getNumero(), Integer.toString(choixRavitaillement.getSelectedIndex()), false);
			}

			if (e.getSource() == choixSoir) {
				selectedRando.modifyXML(selectedEtape.getNumero(), Condition.values()[choixSoir.getSelectedIndex()], 2);
			}

			if (e.getSource() == choixNuit) {
				selectedRando.modifyXML(selectedEtape.getNumero(), Condition.values()[choixNuit.getSelectedIndex()], 3);
			}
			selectedRando.update();
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

		if (e.getSource() == ajouter) {
			if (mode == 1) {
				new FenetreEtape(i, selectedRando);
			} else {
				if (mode == 2) {
					new FenetrePoint(i, null, false, selectedRando, selectedEtape.getNumero());
				}
			}
		}
		if ((e.getSource() == modifier) && (i.rowIsSelected())) {
			if (mode == 1) {
				i.afficherResultat(selectedEtape);
			} else {
				if (mode == 2) {
					new FenetrePoint(i, selectedPoint, true, selectedRando, selectedEtape.getNumero());
				}
			}
		}
		if ((e.getSource() == supprimer) && (i.rowIsSelected())) {
			if (mode == 1) {
				new FenetreSuppression(i, selectedRando, selectedEtape, selectedPoint, true);
			} else {
				if (mode == 2) {
					new FenetreSuppression(i, selectedRando, selectedEtape, selectedPoint, false);
				}
			}
		}
		if (e.getSource() == navigation) {
			if ((mode == 0) && (selectedRando != null)) {
				i.afficherResultat(selectedRando);
			} else {
				if ((mode == 1) && (selectedEtape != null)) {
					i.afficherResultat(selectedEtape);
				} else {
					if (mode == 2) {
						i.afficherResultat(selectedRando);
					}
				}
			}
		}
	}
}

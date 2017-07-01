/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 05/02/2017
 */

package graphic;

import data.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuGauche implements ActionListener {

	// Lien avec les autres classes graphiques.
	private Interface i;
	private Bibliotheque b;
	private JPanel panel;
	private GridBagConstraints gbc;

	// Liste des randonnées.
	private JLabel titre;
	private JComboBox<String> listeRandos;
	private String randoCourante;
	private Rando r;
	private JButton afficher;
	private JButton creer;

	// Partie informations.
	private JLabel infos;
	private JLabel champ11;
	private JLabel champ21;
	private JLabel champ22;
	private JLabel champ31;
	private JLabel champ32;
	private JLabel champ41;
	private JLabel champ42;
	private JLabel champ51;
	private JLabel champ52;
	private JLabel champ61;
	private JLabel champ62;
	private JLabel champ71;
	private JLabel champ72;

	// Gestion Matlab.
	private JButton creerGraphiques;
	private JButton ouvrirDossier;
	private File parametrage;

	/**
	 * CONSTRUCTEUR DE LA CLASSE MENUGAUCHE.
	 * 
	 * @param pI
	 *            Interface graphique mère, de type 'Interface'.
	 * @param mainPanel
	 *            Panel de l'interface graphique mère, de type 'JPanel'.
	 * @param mainGbc
	 *            Contraintes de l'interface graphique mère, de type
	 *            'GridBagConstraints'.
	 * @param bibRando
	 *            Bibliotheque de randonnée associe à l'interface de type
	 *            'Bibliotheque'.
	 * @return Aucun.
	 */
	public MenuGauche(Interface pI, JPanel mainPanel, GridBagConstraints mainGbc, Bibliotheque bibRandos) {

		i = pI;
		b = bibRandos;
		randoCourante = Bibliotheque.SYM_ALL;

		mainGbc.gridx = 0;
		mainGbc.gridy = 0;

		panel = new JPanel(new GridBagLayout());
		panel.setBackground(Interface.BACKGROUND);
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.CENTER;

		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		titre = new JLabel("RANDONNEES");
		titre.setFont(new Font(Interface.police.getFontName(), 1, 13));
		titre.setForeground(Color.white);
		panel.add(titre, gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets.bottom = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		listeRandos = new JComboBox<String>(b.getNomRandos());
		listeRandos.setFont(Interface.police);
		listeRandos.addItemListener(new ItemState());
		panel.add(listeRandos, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		afficher = new JButton("Afficher");
		afficher.setFont(Interface.police);
		afficher.addActionListener(this);
		panel.add(afficher, gbc);

		gbc.insets.top = 2;
		gbc.insets.bottom = 60;

		gbc.gridx = 1;
		gbc.gridy = 2;
		creer = new JButton("Creer");
		creer.setFont(Interface.police);
		creer.addActionListener(this);
		panel.add(creer, gbc);

		gbc.insets.bottom = 10;
		gbc.insets.top = 10;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 6;
		infos = new JLabel("INFOS");
		infos.setForeground(Color.white);
		infos.setFont(new Font(Interface.police.getFontName(), 1, 13));
		panel.add(infos, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 7;
		champ11 = new JLabel("11");
		champ11.setForeground(Color.white);
		champ11.setFont(new Font(Interface.police.getFontName(), 2, 20));
		panel.add(champ11, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 8;
		champ21 = new JLabel("21");
		champ21.setForeground(Color.yellow);
		champ21.setFont(Interface.police);
		panel.add(champ21, gbc);

		gbc.gridx = 1;
		gbc.gridy = 8;
		champ22 = new JLabel("22");
		champ22.setForeground(Color.white);
		champ22.setFont(Interface.police);
		panel.add(champ22, gbc);

		gbc.gridx = 0;
		gbc.gridy = 9;
		champ31 = new JLabel("31");
		champ31.setForeground(Color.yellow);
		champ31.setFont(Interface.police);
		panel.add(champ31, gbc);

		gbc.gridx = 1;
		gbc.gridy = 9;
		champ32 = new JLabel("32");
		champ32.setForeground(Color.white);
		champ32.setFont(Interface.police);
		panel.add(champ32, gbc);

		gbc.gridx = 0;
		gbc.gridy = 10;
		champ41 = new JLabel("41");
		champ41.setForeground(Color.yellow);
		champ41.setFont(Interface.police);
		panel.add(champ41, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		champ42 = new JLabel("42");
		champ42.setForeground(Color.white);
		champ42.setFont(Interface.police);
		panel.add(champ42, gbc);

		gbc.gridx = 0;
		gbc.gridy = 11;
		champ51 = new JLabel("51");
		champ51.setForeground(Color.yellow);
		champ51.setFont(Interface.police);
		panel.add(champ51, gbc);

		gbc.gridx = 1;
		gbc.gridy = 11;
		champ52 = new JLabel("52");
		champ52.setForeground(Color.white);
		champ52.setFont(Interface.police);
		panel.add(champ52, gbc);

		gbc.gridx = 0;
		gbc.gridy = 12;
		champ61 = new JLabel("61");
		champ61.setForeground(Color.yellow);
		champ61.setFont(Interface.police);
		panel.add(champ61, gbc);

		gbc.gridx = 1;
		gbc.gridy = 12;
		champ62 = new JLabel("62");
		champ62.setForeground(Color.white);
		champ62.setFont(Interface.police);
		panel.add(champ62, gbc);

		gbc.insets.bottom = 60;
		gbc.gridx = 0;
		gbc.gridy = 13;
		champ71 = new JLabel("71");
		champ71.setForeground(Color.yellow);
		champ71.setFont(Interface.police);
		panel.add(champ71, gbc);

		gbc.gridx = 1;
		gbc.gridy = 13;
		champ72 = new JLabel("72");
		champ72.setForeground(Color.white);
		champ72.setFont(Interface.police);
		panel.add(champ72, gbc);

		gbc.fill = GridBagConstraints.WEST;
		gbc.insets.bottom = 25;
		gbc.gridx = 0;
		gbc.gridy = 14;
		creerGraphiques = new JButton("Creer graphiques MATLAB");
		creerGraphiques.setFont(Interface.police);
		creerGraphiques.addActionListener(this);
		creerGraphiques.setEnabled(false);
		panel.add(creerGraphiques, gbc);

		gbc.gridx = 0;
		gbc.gridy = 15;
		ouvrirDossier = new JButton("Ouvrir dossier");
		ouvrirDossier.setFont(Interface.police);
		ouvrirDossier.addActionListener(this);
		ouvrirDossier.setEnabled(false);
		panel.add(ouvrirDossier, gbc);

		cacher();
		mainPanel.add(panel, mainGbc);
	}

	/**
	 * DEFINIT LA RANDONNEE COURANTE.
	 * 
	 * @param newRando
	 *            Randonnée sélectionnée dans le tableau de type 'Rando'.
	 * @return Aucun.
	 */
	public void setRandoCourante(Rando newRando) {
		r = newRando;
		// Ecriture de la randonnée courante dans le fichier de paramétrage du
		// script Matlab.
		parametrage = new File(Matlab.MATLAB_PATH + "randoCourante.txt");
		creerGraphiques.setEnabled(true);
		try {
			FileWriter w = new FileWriter(parametrage);
			w.write(r.getNom());
			w.close();
		} catch (IOException error) {
		}
	}

	/**
	 * AJOUTE UNE RANDONNEE AU MENU.
	 * 
	 * @param r
	 *            Randonnée à ajouter de type 'Rando'.
	 * @return Aucun.
	 */
	public void ajouterRando(Rando r) {
		listeRandos.addItem(r.getNom());
	}

	/**
	 * DEFINIT L'AFFICHAGE POUR UNE BIBLIOTHEQUE DE RANDONNEES.
	 *
	 * @param mainPanel
	 *            Panel de l'interface graphique mère, de type 'JPanel'.
	 * @param mainGbc
	 *            Contraintes de l'interface graphique mère, de type
	 *            'GridBagConstraints'.
	 * @param bibRandos
	 *            Bibliothèque de randonnées à afficher de type 'Bibliotheque'.
	 * @return Aucun.
	 */
	public void update(JPanel mainPanel, GridBagConstraints mainGbc, Bibliotheque bibRandos) {
		if (bibRandos != null) {
			afficher();
			champ11.setText("Bibliothèque");
			champ21.setText("Nombre de randos :");
			champ22.setText(Integer.toString(bibRandos.getRandos().size()));
			champ31.setForeground(Interface.BACKGROUND);
			champ32.setForeground(Interface.BACKGROUND);
			champ41.setForeground(Interface.BACKGROUND);
			champ42.setForeground(Interface.BACKGROUND);
			champ51.setForeground(Interface.BACKGROUND);
			champ52.setForeground(Interface.BACKGROUND);
			champ61.setForeground(Interface.BACKGROUND);
			champ62.setForeground(Interface.BACKGROUND);
			champ71.setForeground(Interface.BACKGROUND);
			champ72.setForeground(Interface.BACKGROUND);
		}
	}

	/**
	 * DEFINIT L'AFFICHAGE POUR UNE RANDONNEES.
	 *
	 * @param mainPanel
	 *            Panel de l'interface graphique mère, de type 'JPanel'.
	 * @param mainGbc
	 *            Contraintes de l'interface graphique mère, de type
	 *            'GridBagConstraints'.
	 * @param r
	 *            Randonnée à afficher de type 'Rando'.
	 * @return Aucun.
	 */
	public void update(JPanel mainPanel, GridBagConstraints mainGbc, Rando r) {
		if (r != null) {
			afficher();
			champ11.setText(r.getNom().replace("_", " "));
			champ21.setText("Longueur totale :");
			champ22.setText(Double.toString(r.getLongueurTotale()) + " km");
			champ31.setText("Durée :");
			champ32.setText(Integer.toString(r.getTrace().size()) + " jours");
			champ41.setText("Dénivelé + :");
			champ42.setText(Integer.toString(r.getDenivelePos()) + " m");
			champ51.setText("Dénivelé - :");
			champ52.setText(Integer.toString(r.getDeniveleNeg()) + " m");
			champ61.setText("Altitude min. :");
			if (r.getAltitudeMin() == Integer.MAX_VALUE) {
				champ62.setText("0 m");
			} else {
				champ62.setText(Integer.toString(r.getAltitudeMin()) + " m");
			}
			champ71.setText("Altitude max. :");
			if (r.getAltitudeMax() == Integer.MIN_VALUE) {
				champ72.setText("0 m");
			} else {
				champ72.setText(Integer.toString(r.getAltitudeMax()) + " m");
			}
		}
	}

	/**
	 * DEFINIT L'AFFICHAGE POUR UNE ETAPE.
	 *
	 * @param mainPanel
	 *            Panel de l'interface graphique mère, de type 'JPanel'.
	 * @param mainGbc
	 *            Contraintes de l'interface graphique mère, de type
	 *            'GridBagConstraints'.
	 * @param e
	 *            Etape à afficher de type 'Etape'.
	 * @return Aucun.
	 */
	public void update(JPanel mainPanel, GridBagConstraints mainGbc, Etape e) {
		if (e != null) {
			afficher();
			champ11.setText("Etape " + e.getNumero());
			champ21.setText("Longueur :");
			champ22.setText(Double.toString(e.getLongueur()) + " km");
			champ31.setText("Durée :");
			champ32.setText(Temps.afficherHeure(e.getTemps()));
			champ41.setText("Dénivelé + :");
			champ42.setText(Integer.toString(e.getDenivelePos()) + " m");
			champ51.setText("Dénivelé - :");
			champ52.setText(Integer.toString(e.getDeniveleNeg()) + " m");
			champ61.setText("Altitude min. :");
			if (e.getAltitudeMin() == Integer.MAX_VALUE) {
				champ62.setText("0 m");
			} else {
				champ62.setText(Integer.toString(e.getAltitudeMin()) + " m");
			}
			champ71.setText("Altitude max. :");
			if (e.getAltitudeMax() == Integer.MIN_VALUE) {
				champ72.setText("0 m");
			} else {
				champ72.setText(Integer.toString(e.getAltitudeMax()) + " m");
			}
		}
	}

	/**
	 * AFFICHE LES ITEMS DU MENU GAUCHE.
	 *
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void afficher() {
		champ11.setForeground(Color.white);
		champ21.setForeground(Color.yellow);
		champ22.setForeground(Color.white);
		champ31.setForeground(Color.yellow);
		champ32.setForeground(Color.white);
		champ41.setForeground(Color.yellow);
		champ42.setForeground(Color.white);
		champ51.setForeground(Color.yellow);
		champ52.setForeground(Color.white);
		champ61.setForeground(Color.yellow);
		champ62.setForeground(Color.white);
		champ71.setForeground(Color.yellow);
		champ72.setForeground(Color.white);
	}

	/**
	 * CACHE LES ITEMS DU MENU GAUCHE.
	 *
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void cacher() {
		champ11.setForeground(Interface.BACKGROUND);
		champ21.setForeground(Interface.BACKGROUND);
		champ22.setForeground(Interface.BACKGROUND);
		champ31.setForeground(Interface.BACKGROUND);
		champ32.setForeground(Interface.BACKGROUND);
		champ41.setForeground(Interface.BACKGROUND);
		champ42.setForeground(Interface.BACKGROUND);
		champ51.setForeground(Interface.BACKGROUND);
		champ52.setForeground(Interface.BACKGROUND);
		champ61.setForeground(Interface.BACKGROUND);
		champ62.setForeground(Interface.BACKGROUND);
		champ71.setForeground(Interface.BACKGROUND);
		champ72.setForeground(Interface.BACKGROUND);
	}

	/**
	 * DEFINIT LES ACTIONS DE LA LISTE DEROULANTE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	class ItemState implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == listeRandos) {
				randoCourante = (String) e.getItem();
				if (randoCourante.compareTo(Bibliotheque.SYM_ALL) != 0) {
					i.setRando(b.rechercherRando(randoCourante));
				}
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

		// Affichage d'une randonnée.
		if (e.getSource() == afficher) {
			if (randoCourante.compareTo(Bibliotheque.SYM_ALL) == 0) {
				i.afficherBibliotheque();
			} else {
				i.afficherResultat(b.rechercherRando(randoCourante));
			}
		}

		// Ouverture de la fenêtre de création d'une randonnée.
		if (e.getSource() == creer) {
			new FenetreRando(b);
		}

		// Start Matlab script.
		if (e.getSource() == creerGraphiques) {
			if (r != null) {
				r.update();
				Matlab.creerDossierLatex(r);
				try {
					Matlab.creerMatlab(r);
				} catch (IOException error) {
				}
			}
			Runtime runtime = Runtime.getRuntime();
			try {
				ouvrirDossier.setEnabled(true);
				runtime.exec(Matlab.MATLAB_PATH + "matlab.bat");
			} catch (IOException error) {
			}
		}

		// Ouverture du dossier contenant les graphiques.
		if (e.getSource() == ouvrirDossier) {
			try {
				Desktop.getDesktop().open(new File(Matlab.OUTPUT_PATH + r.getNom()));
			} catch (IOException error) {
			}
		}
	}
}

/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 05/02/2017
 */

package graphic;

import data.*;
import typedef.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class Tableau extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Lien avec les autres classes graphiques.
	private Interface i;
	private JScrollPane scroll;
	private JPanel panel;
	private GridBagConstraints gbc;

	// Modeles de colonnes.
	private DefaultTableModel modeleRando;
	private DefaultTableModel modeleEtape;
	private DefaultTableModel modelePoint;

	// Elements graphiques.
	private JButton precedent;
	private JButton suivant;
	private JTable tableau;
	private Rando randoCourante;
	private Etape etapeCourante;

	int mode; // 0 = liste de randos
				// 1 = liste d'etapes
				// 2 = liste de points

	private Vector<Rando> listeRandos; // Affichage de la bibliotheque.
	private Vector<Etape> listeEtapes; // Affichage d'une randonnee.
	private Vector<PointGeo> listePoints; // Affichage d'une etape.

	/**
	 * CONSTRUCTEUR DE LA CLASSE TABLEAU.
	 * 
	 * @param pI
	 *            Interface graphique mere, de type 'Interface'.
	 * @param mainPanel
	 *            Panel de l'interface graphique mere, de type 'JPanel'.
	 * @param mainGbc
	 *            Contraintes de l'interface graphique mere, de type
	 *            'GridBagConstraints'.
	 * @return Aucun.
	 */
	public Tableau(Interface pI, JPanel mainPanel, GridBagConstraints mainGbc) {

		i = pI;

		mainGbc.gridx = 1;
		mainGbc.gridy = 0;

		// Modeles de colonnes.
		modeleRando = new DefaultTableModel();
		modeleRando.addColumn("NOM");
		modeleRando.addColumn("NOMBRE D'ETAPES");
		listeRandos = new Vector<Rando>();

		modeleEtape = new DefaultTableModel();
		modeleEtape.addColumn("NUMERO");
		modeleEtape.addColumn("DEPART");
		modeleEtape.addColumn("ARRIVEE");
		modeleEtape.addColumn("LONGUEUR");
		listeEtapes = new Vector<Etape>();

		modelePoint = new DefaultTableModel();
		modelePoint.addColumn("TOPONYME");
		modelePoint.addColumn("PK");
		modelePoint.addColumn("ALTITUDE");
		modelePoint.addColumn("PAUSE");
		listePoints = new Vector<PointGeo>();

		panel = new JPanel(new GridBagLayout());
		panel.setBackground(Interface.BACKGROUND);
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.fill = GridBagConstraints.CENTER;

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		precedent = new JButton("<");
		precedent.setFont(Interface.police);
		precedent.addActionListener(this);
		panel.add(precedent, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 2;
		gbc.gridy = 0;
		suivant = new JButton(">");
		suivant.setFont(Interface.police);
		suivant.addActionListener(this);
		panel.add(suivant, gbc);

		tableau = new JTable(modeleRando) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		tableau.setFont(new Font(Interface.police.getFontName(), 0, Interface.police.getSize()));
		tableau.getTableHeader().setFont(Interface.police);
		tableau.getTableHeader().setPreferredSize(new Dimension(600, 30));

		// Hauteur des lignes
		tableau.setRowHeight(30);
		// Largeur des colonnes
		dimensionnerTableauInit();
		mode = 0;

		// Detection du clic souris
		tableau.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evnt) {
				if (evnt.getClickCount() == 1) {
					int ind = tableau.getSelectedRow();
					switch (mode) {
					// Liste des randos
					case 0:
						i.setRando(listeRandos.elementAt(ind));
						break;
					// Liste des etapes
					case 1:
						i.setEtape(listeEtapes.elementAt(ind));
						break;
					// Liste des points
					case 2:
						i.setPoint(listePoints.elementAt(ind));
						break;
					}
				}
			}
		});

		scroll = new JScrollPane(tableau);
		scroll.setPreferredSize(new Dimension(600, 550));
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(scroll, gbc);

		disableButtons();
		mainPanel.add(panel, mainGbc);
	}

	/**
	 * TESTE SI UNE LIGNE DU TABLEAU A ETE SELECTIONNEE.
	 * 
	 * @param Aucun.
	 * @return 'true' si une ligne du tableau a ete selectionnee. 'false' sinon.
	 */
	public boolean rowIsSelected() {
		return (tableau.getSelectedRow() != -1);
	}

	/**
	 * DIMENSIONNE LE TABLEAU A L'INITIALISATION DE L'INTERFACE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void dimensionnerTableauInit() {
		TableColumn colonne;
		// 1
		colonne = tableau.getColumnModel().getColumn(0);
		colonne.setPreferredWidth(100);
		colonne.setResizable(false);
		// 2
		colonne = tableau.getColumnModel().getColumn(1);
		colonne.setPreferredWidth(100);
		colonne.setResizable(false);
	}

	/**
	 * DIMENSIONNE LE TABLEAU POUR L'AFFICHAGE D'UNE BIBLIOTHEQUE DE RANDONNEES.
	 * 
	 * @param b
	 *            Bibliotheque de randonnees dont on doit afficher les
	 *            caracteristiques, de type 'Bibliotheque'.
	 * @return Aucun.
	 */
	private void dimensionnerTableau(Bibliotheque b) {
		TableColumn colonne;
		// 1
		colonne = tableau.getColumnModel().getColumn(0);
		colonne.setPreferredWidth(100);
		colonne.setResizable(false);
		// 2
		colonne = tableau.getColumnModel().getColumn(1);
		colonne.setPreferredWidth(100);
		colonne.setResizable(false);
	}

	/**
	 * DIMENSIONNE LE TABLEAU POUR L'AFFICHAGE D'UNE RANDONNEE.
	 * 
	 * @param r
	 *            Randonnee dont on doit afficher les caracteristiques, de type
	 *            'Rando'.
	 * @return Aucun.
	 */
	private void dimensionnerTableau(Rando r) {
		TableColumn colonne;
		// 1
		colonne = tableau.getColumnModel().getColumn(0);
		colonne.setPreferredWidth(1);
		colonne.setResizable(false);
		// 2
		colonne = tableau.getColumnModel().getColumn(1);
		colonne.setPreferredWidth(40);
		colonne.setResizable(false);
		// 3
		colonne = tableau.getColumnModel().getColumn(2);
		colonne.setPreferredWidth(40);
		colonne.setResizable(false);
		// 4
		colonne = tableau.getColumnModel().getColumn(3);
		colonne.setPreferredWidth(10);
		colonne.setResizable(false);
	}

	/**
	 * DIMENSIONNE LE TABLEAU POUR L'AFFICHAGE D'UNE ETAPE.
	 * 
	 * @param e
	 *            Etape dont on doit afficher les caracteristiques, de type
	 *            'Etape'.
	 * @return Aucun.
	 */
	private void dimensionnerTableau(Etape e) {
		TableColumn colonne;
		// 1
		colonne = tableau.getColumnModel().getColumn(0);
		colonne.setPreferredWidth(100);
		colonne.setResizable(false);
		// 2
		colonne = tableau.getColumnModel().getColumn(1);
		colonne.setPreferredWidth(1);
		colonne.setResizable(false);
		// 3
		colonne = tableau.getColumnModel().getColumn(2);
		colonne.setPreferredWidth(10);
		colonne.setResizable(false);
		// 4
		colonne = tableau.getColumnModel().getColumn(3);
		colonne.setPreferredWidth(10);
		colonne.setResizable(false);
	}

	/**
	 * EFFACE LE CONTENU DU TABLEAU.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public void supprimerTableau() {
		int rowCount;
		int i = 0;
		rowCount = modeleRando.getRowCount();
		for (i = rowCount - 1; i >= 0; i--) {
			modeleRando.removeRow(i);
		}
		rowCount = modeleEtape.getRowCount();
		for (i = rowCount - 1; i >= 0; i--) {
			modeleEtape.removeRow(i);
		}
		rowCount = modelePoint.getRowCount();
		for (i = rowCount - 1; i >= 0; i--) {
			modelePoint.removeRow(i);
		}
	}

	/**
	 * DEFINIT L'AFFICHAGE POUR UNE BIBLIOTHEQUE DE RANDONNEES.
	 * 
	 * @param b
	 *            Bibliotheque de randonnees dont on doit afficher les
	 *            caracteristiques, de type 'Bibliotheque'.
	 * @return Aucun.
	 */
	public void update(Bibliotheque b) {
		disableButtons();
		supprimerTableau();
		if (b != null) {
			listeRandos = b.getRandos();
			mode = 0;
			Iterator<Rando> i = listeRandos.iterator();
			while (i.hasNext()) {
				Rando randoCourante = i.next();
				String nom = randoCourante.getNom();
				String nbEtapes = Integer.toString(randoCourante.getNumEtapes());
				String[] ligne = { nom, nbEtapes };
				modeleRando.addRow(ligne);
			}
			tableau.setModel(modeleRando);
			tableau.setDefaultRenderer(Object.class, new BibliothequeRenderer(listeRandos));
		}
		dimensionnerTableau(b);
	}

	/**
	 * DEFINIT L'AFFICHAGE POUR UNE RANDONNEE.
	 * 
	 * @param r
	 *            Randonnee dont on doit afficher les caracteristiques, de type
	 *            'Rando'.
	 * @return Aucun.
	 */
	public void update(Rando r) {
		randoCourante = r;
		disableButtons();
		supprimerTableau();
		if (r != null) {
			listeEtapes = r.getTrace();
			mode = 1;
			Iterator<Etape> j = listeEtapes.iterator();
			while (j.hasNext()) {
				Etape etapeCourante = j.next();
				String num = Integer.toString(etapeCourante.getNumero());
				String depart = etapeCourante.getDepart().getNom();
				String arrivee = etapeCourante.getArrivee().getNom();
				String longueur = Double.toString(etapeCourante.getLongueur());
				String[] ligne = { num, depart, arrivee, longueur };
				modeleEtape.addRow(ligne);
			}
		}
		tableau.setModel(modeleEtape);
		tableau.setDefaultRenderer(Object.class, new RandoRenderer(listeEtapes));
		dimensionnerTableau(r);
	}

	/**
	 * DEFINIT L'AFFICHAGE POUR UNE ETAPE.
	 * 
	 * @param e
	 *            Etape dont on doit afficher les caracteristiques, de type
	 *            'Etape'.
	 * @return Aucun.
	 */
	public void update(Etape e) {
		etapeCourante = e;
		supprimerTableau();
		if (e != null) {
			listePoints = e.getPoints();
			mode = 2;
			Iterator<PointGeo> j = listePoints.iterator();
			while (j.hasNext()) {
				PointGeo pointCourant = j.next();
				String toponyme = pointCourant.getNom();
				String pk = Double.toString(pointCourant.getPK());
				String altitude = Integer.toString(pointCourant.getAltitude());
				String pause = pointCourant.getPause().getName();
				String[] ligne = { toponyme, pk, altitude, pause };
				modelePoint.addRow(ligne);
			}
		}
		tableau.setModel(modelePoint);
		tableau.setDefaultRenderer(Object.class, new EtapeRenderer(listePoints));
		dimensionnerTableau(e);
		enableButtons();
	}

	/**
	 * DEFINIT LES COULEURS POUR UNE BIBLIOTHEQUE DE RANDONNEES.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public class BibliothequeRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		// private Vector<Rando> listeRandos ;

		public BibliothequeRenderer(Vector<Rando> pListeRandos) {
			// listeRandos = pListeRandos ;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			// Rando randoCourante = listeRandos.elementAt(row) ;
			return this;
		}
	}

	/**
	 * DEFINIT LES COULEURS POUR UNE RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public class RandoRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		// private Vector<Etape> listeEtapes ;

		public RandoRenderer(Vector<Etape> pListeEtapes) {
			// listeEtapes = pListeEtapes ;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			// Etape etapeCourante = listeEtapes.elementAt(row) ;
			return this;
		}
	}

	/**
	 * DEFINIT LES COULEURS POUR UNE ETAPE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public class EtapeRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		private Vector<PointGeo> listePoints;

		public EtapeRenderer(Vector<PointGeo> pListePoints) {
			listePoints = pListePoints;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			PointGeo pointCourant = listePoints.elementAt(row);
			if (pointCourant.getPause() != Pause.H0M0) {
				setBackground(new Color(255, 246, 143));
			} else {
				setBackground(Color.white);
			}
			return this;
		}
	}

	/**
	 * ACTIVE LES BOUTONS DE NAVIGATION.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void enableButtons() {
		if (etapeCourante.getNumero() < randoCourante.getNumEtapes()) {
			suivant.setEnabled(true);
		} else {
			suivant.setEnabled(false);
		}
		if (etapeCourante.getNumero() > 1) {
			precedent.setEnabled(true);
		} else {
			precedent.setEnabled(false);
		}
	}

	/**
	 * DESACTIVE LES BOUTONS DE NAVIGATION.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void disableButtons() {
		precedent.setEnabled(false);
		suivant.setEnabled(false);
	}

	/**
	 * DEFINIT LES ACTIONS DES BOUTONS.
	 * 
	 * @param e
	 *            Evenement declenche par l'appui sur un bouton.
	 * @return Aucun.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == precedent) {
			Etape nouvelleEtape = randoCourante.rechercherEtape(etapeCourante.getNumero() - 1);
			i.setEtape(nouvelleEtape);
			i.afficherResultat(nouvelleEtape);
		}
		if (e.getSource() == suivant) {
			Etape nouvelleEtape = randoCourante.rechercherEtape(etapeCourante.getNumero() + 1);
			i.setEtape(nouvelleEtape);
			i.afficherResultat(nouvelleEtape);
		}
	}
}

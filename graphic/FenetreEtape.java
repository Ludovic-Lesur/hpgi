/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 12/02/2017
 */

package graphic;

import data.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FenetreEtape extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Lien avec les autres classes graphiques.
	private JFrame fenetre;
	private JPanel panel;
	private GridBagConstraints gbc;
	private Interface i;
	private Rando rando;

	// Elements graphiques et attributs.
	private JLabel position;
	private JRadioButton fin;
	private JRadioButton autre;
	private ButtonGroup choixPosition;
	private JComboBox<String> choixNum;
	private int numCourant;
	private boolean finBool;
	private JButton ok;
	private JButton annuler;

	/**
	 * CONSTRUCTEUR DE LA CLASSE FENETREETAPE.
	 * 
	 * @param pI
	 *            Interface graphique mere, de type 'Interface'.
	 * @param pRando
	 *            Randonnee a laquelle doit etre ajoutee l'etape.
	 * @return Aucun.
	 */
	public FenetreEtape(Interface pI, Rando pRando) {

		i = pI;
		rando = pRando;

		fenetre = new JFrame();
		fenetre.setTitle("Ajouter etape");
		fenetre.setSize(300, 250);
		fenetre.setResizable(false);
		fenetre.setLocationRelativeTo(null);

		// Panel
		panel = new JPanel();
		panel.setBackground(Interface.BACKGROUND);
		panel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridheight = 1;

		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		position = new JLabel("Position de l'etape dans la randonnee :");
		position.setFont(Interface.police);
		position.setForeground(Color.yellow);
		panel.add(position, gbc);

		gbc.gridwidth = 1;
		gbc.insets.bottom = 0;
		gbc.gridx = 0;
		gbc.gridy = 1;
		fin = new JRadioButton("A la fin");
		fin.setFont(Interface.police);
		fin.setOpaque(false);
		fin.setForeground(Color.white);
		fin.setSelected(true);
		finBool = true;
		fin.addActionListener(this);
		panel.add(fin, gbc);

		gbc.insets.bottom = 30;
		gbc.gridx = 0;
		gbc.gridy = 2;
		autre = new JRadioButton("Autre numero");
		autre.setFont(Interface.police);
		autre.setOpaque(false);
		autre.setForeground(Color.white);
		autre.setSelected(false);
		autre.addActionListener(this);
		panel.add(autre, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 2;
		String[] listeNum = new String[rando.getNumEtapes()];
		int k = 0;
		for (k = 0; k < rando.getNumEtapes(); k++) {
			listeNum[k] = Integer.toString(k + 1);
		}
		choixNum = new JComboBox<String>(listeNum);
		choixNum.setFont(Interface.police);
		choixNum.addItemListener(new ItemState());
		numCourant = 1;
		panel.add(choixNum, gbc);

		gbc.fill = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		ok = new JButton();
		ok.setText("Ajouter");
		ok.setFont(Interface.police);
		ok.setForeground(new Color(100, 200, 0));
		ok.addActionListener(this);
		panel.add(ok, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 1;
		gbc.gridy = 3;
		annuler = new JButton();
		annuler.setText("Annuler");
		annuler.setFont(Interface.police);
		annuler.setForeground(Color.red);
		annuler.addActionListener(this);
		panel.add(annuler, gbc);

		choixPosition = new ButtonGroup();
		choixPosition.add(fin);
		choixPosition.add(autre);
		disableNum();

		// Affichage de la fenetre
		fenetre.setContentPane(panel);
		fenetre.setVisible(true);
	}

	/**
	 * DEFINIT LES ACTIONS DE LA LISTE DEROULANTE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	class ItemState implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == choixNum) {
				numCourant = choixNum.getSelectedIndex() + 1;
				System.out.println("numCourant = " + numCourant);
			}
		}
	}

	/**
	 * AUTORISE LA MODIFICATION DU NUMERO DE L'ETAPE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void enableNum() {
		choixNum.setEditable(true);
		choixNum.setEnabled(true);
	}

	/**
	 * DESACTIVE LA MODIFICATION DU NUMERO DE L'ETAPE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void disableNum() {
		choixNum.setEditable(false);
		choixNum.setEnabled(false);
	}

	/**
	 * DEFINIT LES ACTIONS DES BOUTONS.
	 * 
	 * @param e
	 *            Evenement declenche par l'appui sur un bouton.
	 * @return Aucun.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == autre) {
			enableNum();
			finBool = false;
		}
		if (e.getSource() == fin) {
			disableNum();
			finBool = true;
		}
		if (e.getSource() == ok) {
			rando.addXML(new Etape(), numCourant, finBool);
			rando.update();
			i.afficherResultat(rando);
			fenetre.setVisible(false);
			fenetre.dispose();
		}
		if (e.getSource() == annuler) {
			fenetre.setVisible(false);
			fenetre.dispose();
		}
	}
}

/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 05/02/2017
 */

package graphic;

import data.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class FenetreRando extends JFrame implements ActionListener, DocumentListener {

	private static final long serialVersionUID = 1L;

	// Lien avec les autres classes graphiques.
	private JFrame fenetre;
	private JPanel panel;
	private GridBagConstraints gbc;

	// Elements graphiques et attributs.
	private Bibliotheque b;
	private JLabel nom;
	private JTextField champNom;
	private String nomRando;
	private JButton ok;
	private JButton annuler;

	/**
	 * CONSTRUCTEUR DE LA CLASSE FENETRERERANDO.
	 * 
	 * @param bibRandos
	 *            Bibliotheque à laquelle la randonnée doit être ajoutée, de
	 *            type 'Bibliotheque'.
	 * @return Aucun.
	 */
	public FenetreRando(Bibliotheque bibRandos) {

		b = bibRandos;

		// Création de l'interface
		fenetre = new JFrame();
		fenetre.setTitle("Créer randonnée");
		fenetre.setSize(350, 200);
		fenetre.setResizable(false);
		fenetre.setLocationRelativeTo(null);

		// Panel
		panel = new JPanel();
		panel.setBackground(Interface.BACKGROUND);
		panel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		gbc.gridx = 0;
		gbc.gridy = 0;
		nom = new JLabel("Nom");
		nom.setFont(Interface.police);
		nom.setForeground(Color.yellow);
		panel.add(nom, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		champNom = new JTextField(20);
		champNom.setFont(Interface.police);
		champNom.setEditable(true);
		champNom.getDocument().addDocumentListener(this);
		panel.add(champNom, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		ok = new JButton();
		ok.setText("Créer fichier XML");
		ok.setFont(Interface.police);
		ok.setForeground(new Color(100, 200, 0));
		ok.addActionListener(this);
		ok.setEnabled(false);
		panel.add(ok, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		annuler = new JButton("Annuler");
		annuler.setFont(Interface.police);
		annuler.setForeground(Color.red);
		annuler.addActionListener(this);
		panel.add(annuler, gbc);

		// Affichage de la fenêtre
		fenetre.setContentPane(panel);
		fenetre.setVisible(true);
	}

	/**
	 * VERIFIE SI LE NOM DE LA RANDONNEE N'EST PAS VIDE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void checkNomRando() {
		boolean result = false;
		if (champNom.getText().compareTo("") != 0) {
			nomRando = champNom.getText();
			result = true;
		}
		if (result == true) {
			ok.setEnabled(true);
		} else {
			ok.setEnabled(false);
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
			b.creerRando(nomRando);
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
		checkNomRando();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		checkNomRando();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		checkNomRando();
	}
}

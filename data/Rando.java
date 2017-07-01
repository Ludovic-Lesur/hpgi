/**
 * Javadoc
 * 
 * @author Ludovic Lesur
 * @since 11/04/2016
 */

package data;

import typedef.*;
import java.io.*;
import java.text.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.*;
import org.jdom2.output.Format;

public class Rando {

	// Attributs
	private String nom;
	private Vector<Etape> trace;
	private double longueur;
	private int denivelePos;
	private int deniveleNeg;
	// Parseur XML
	private File fichierXML;
	private Document document;
	private Element racine;
	private static final int OFFSET_INFOS_ETAPE = 12;
	private static final int OFFSET_ELEMENT = 2;
	private static final int OFFSET_INFOS_RANDO = 3;

	/**
	 * CONSTRUCTEUR SANS ARGUMENT DE LA CLASSE RANDO.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public Rando() {
	}

	/**
	 * CONSTRUCTEUR DE LA CLASSE RANDO.
	 * 
	 * @param sourceXML
	 *            Chemin absolu de type 'String' du fichier XML contenant les
	 *            données de la rando.
	 * @return Aucun.
	 */
	public Rando(File sourceXML) {
		// Parseur XML
		fichierXML = sourceXML;
		update();
	}

	/**
	 * AJOUTE UNE ETAPE A LA RANDONNEE.
	 * 
	 * @param newEtape
	 *            Nouvelle étape à ajouter de type 'Etape'.
	 * @return Aucun.
	 */
	public void ajouterEtape(Etape newEtape) {
		trace.addElement(newEtape);
		trace.lastElement().setNumero(trace.size());
		// Mise à jour des stats de la traversée
		longueur = longueur + (newEtape.getLongueur());
		denivelePos = denivelePos + (newEtape.getDenivelePos());
		deniveleNeg = deniveleNeg + (newEtape.getDeniveleNeg());
	}

	/**
	 * RECHERCHE UNE ETAPE DE LA RANDONNEE.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape à chercher de type 'int'.
	 * @return resultat Objet 'Etape' trouvé ('null' sinon).
	 */
	public Etape rechercherEtape(int numEtape) {
		Etape resultat = null;
		if (numEtape <= trace.size()) {
			resultat = trace.elementAt(numEtape - 1);
		}
		return resultat;
	}

	/**
	 * MET A JOUR LES DONNEES DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public void update() {
		// On crée un nouveau document JDOM avec en argument le fichier XML
		SAXBuilder sxb = new SAXBuilder();
		try {
			document = sxb.build(fichierXML);
			racine = document.getRootElement();
			// Update.
			nom = "Inconnu";
			trace = new Vector<Etape>();
			longueur = 0.0;
			denivelePos = 0;
			deniveleNeg = 0;
			nom = racine.getChild(BaliseXML.XML_NOM).getText();
			// Boucle des étapes...
			List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
			Iterator<Element> itEtapes = listeEtapes.iterator();
			while (itEtapes.hasNext()) {
				Element etapeCourante = (Element) itEtapes.next();
				Etape nouvelleEtape = new Etape();
				// Heure de départ
				nouvelleEtape
						.setHeureDepart(Heure.affecter((etapeCourante.getChild(BaliseXML.XML_HEUREDEPART).getText())));
				// Ravitaillement
				nouvelleEtape.setRavitaillement(etapeCourante.getChild(BaliseXML.XML_RAVITAILLEMENT).getText());
				// Dejeuner
				nouvelleEtape.setDejeuner(etapeCourante.getChild(BaliseXML.XML_DEJEUNER).getText());
				// Midi
				Element midi = etapeCourante.getChild(BaliseXML.XML_MIDI);
				nouvelleEtape.setLieuMidi(midi.getChild(BaliseXML.XML_LIEUMIDI).getText());
				nouvelleEtape.setMidi(midi.getChild(BaliseXML.XML_REPASMIDI).getText());
				// Soir
				nouvelleEtape.setSoir(etapeCourante.getChild(BaliseXML.XML_SOIR).getText());
				// Nuit
				nouvelleEtape.setNuit(etapeCourante.getChild(BaliseXML.XML_NUIT).getText());
				// Boucle des points...
				List<Element> listePoints = etapeCourante.getChildren(BaliseXML.XML_POINT);
				Iterator<Element> itPoints = listePoints.iterator();
				PointGeo precedent = null;
				int nbPoints = 0;
				while (itPoints.hasNext()) {
					Element pointCourant = (Element) itPoints.next();
					// Nouveau point
					nbPoints++;
					String toponyme = pointCourant.getChild(BaliseXML.XML_TOPONYME).getText();
					double pk = Double.parseDouble(pointCourant.getChild(BaliseXML.XML_PK).getText());
					int altitude = Integer.parseInt(pointCourant.getChild(BaliseXML.XML_ALTITUDE).getText());
					Pause pause = Pause.affecter(pointCourant.getChild(BaliseXML.XML_PAUSE).getText());
					PointGeo actuel = new PointGeo(toponyme, pk, altitude, pause);
					nouvelleEtape.ajouterPoint(actuel);
					// Création d'un chemin avec le point précédent
					if (nbPoints > 1) {
						Chemin nouveauChemin = new Chemin(precedent, actuel, denivelePos, deniveleNeg);
						// Ajout du chemin à l'étape
						nouvelleEtape.ajouterChemin(nouveauChemin);
					}
					precedent = new PointGeo(actuel);
				}
				// Ajout de l'étape à la rando
				nouvelleEtape.majHoraire();
				ajouterEtape(nouvelleEtape);
			}
		} catch (IOException e) {
			System.out.println("Fichier non trouvé.");
		} catch (JDOMException e) {
			System.out.println("Erreur de parsing.");
		}
	}

	/**
	 * MODIFIE LE NOM DE LA RANDONNEE.
	 * 
	 * @param newNom
	 *            Nouveau nom de la randonnée de type 'String'.
	 * @return Aucun.
	 */
	public void setNom(String newNom) {
		nom = newNom;
	}

	/**
	 * RENVOIE LE NOM DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return nom Nom de la randonnée (avec espaces).
	 */
	public String getNom() {
		return nom.replace(" ", "_");
	}

	/**
	 * RENVOIE LA LONGUEUR TOTALE DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return longueur Longueur totale de la randonnée en km (arrondie au
	 *         centième).
	 */
	public double getLongueurTotale() {
		DecimalFormat df = new DecimalFormat("#.##");
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		longueur = Double.valueOf(df.format(longueur));
		return longueur;
	}

	/**
	 * RENVOIE LE DENIVELE POSITIF TOTAL DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return denivelPos Dénivelé positif total de la randonnée en m.
	 */
	public int getDenivelePos() {
		return denivelePos;
	}

	/**
	 * RENVOIE LE DENIVELE NEGATIF TOTAL DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return denivelNeg Dénivelé négatif total de la randonnée en m.
	 */
	public int getDeniveleNeg() {
		return deniveleNeg;
	}

	/**
	 * RENVOIE L'ALTITUDE MINIMALE DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return altitudeMin Altitude minimale de la randonnée en m.
	 */
	public int getAltitudeMin() {
		int altitudeMin = Integer.MAX_VALUE;
		Iterator<Etape> itEtapes = trace.iterator();
		while (itEtapes.hasNext()) {
			Etape etapeCourante = itEtapes.next();
			if (etapeCourante.getAltitudeMin() < altitudeMin) {
				altitudeMin = etapeCourante.getAltitudeMin();
			}
		}
		return altitudeMin;
	}

	/**
	 * RENVOIE L'ALTITUDE MAXIMALE DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return altitudeMax Altitude maximale de la randonnée en m.
	 */
	public int getAltitudeMax() {
		int altitudeMax = Integer.MIN_VALUE;
		Iterator<Etape> itEtapes = trace.iterator();
		while (itEtapes.hasNext()) {
			Etape etapeCourante = itEtapes.next();
			if (etapeCourante.getAltitudeMin() > altitudeMax) {
				altitudeMax = etapeCourante.getAltitudeMax();
			}
		}
		return altitudeMax;
	}

	/**
	 * RENVOIE LA LISTE DES ETAPES DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return trace Liste des étapes de la randonnée de type Vector<Etape>.
	 */
	public Vector<Etape> getTrace() {
		return trace;
	}

	/**
	 * RENVOIE LE NOMBRE D'ETAPE(S) DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return Nombre d'étapes constituant la randonnée.
	 */
	public int getNumEtapes() {
		return trace.size();
	}

	/**
	 * ENREGISTRE LE FICHIER XML DE LA RANDONNEE.
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	private void saveXML() {
		try {
			DocType xmlFormat = new DocType(BaliseXML.XML_RACINE, "format.dtd");
			document.setDocType(xmlFormat);
			Format f = Format.getPrettyFormat();
			f.setExpandEmptyElements(true);
			XMLOutputter sortie = new XMLOutputter(f);
			sortie.output(document, new FileOutputStream(fichierXML));
		} catch (java.io.IOException e) {
		}
	}

	/**
	 * AJOUTE UNE NOUVELLE ETAPE DANS LE FICHIER XML.
	 * 
	 * @param newEtape
	 *            Etape à ajouter de type 'Etape'.
	 * @param numEtape
	 *            Numéro de la nouvelle étape de type 'int'.
	 * @param fin
	 *            Indique où l'étape doit être placée. true = l'étape est placée
	 *            à la suite des étapes actuelles (en fin de fichier). false =
	 *            l'étape est placée au rang 'numEtape'.
	 * @return Aucun.
	 */
	public void addXML(Etape newEtape, int numEtape, boolean fin) {
		Element etape = new Element(BaliseXML.XML_ETAPE);
		// Création des champs.
		Element heureDepart = new Element(BaliseXML.XML_HEUREDEPART);
		heureDepart.setText(newEtape.getHeureDepart().getSymbol());
		Element ravitaillement = new Element(BaliseXML.XML_RAVITAILLEMENT);
		ravitaillement.setText(newEtape.getRavitaillement());
		Element dejeuner = new Element(BaliseXML.XML_DEJEUNER);
		dejeuner.setText(newEtape.getDejeuner().getSymbol());
		Element midi = new Element(BaliseXML.XML_MIDI);
		Element lieuMidi = new Element(BaliseXML.XML_LIEUMIDI);
		lieuMidi.setText(newEtape.getLieuMidi());
		Element repasMidi = new Element(BaliseXML.XML_REPASMIDI);
		repasMidi.setText(newEtape.getMidi().getSymbol());
		midi.addContent(lieuMidi);
		midi.addContent(repasMidi);
		Element soir = new Element(BaliseXML.XML_SOIR);
		soir.setText(newEtape.getSoir().getSymbol());
		Element nuit = new Element(BaliseXML.XML_NUIT);
		nuit.setText(newEtape.getNuit().getSymbol());
		// Ajout des attributs.
		etape.addContent(heureDepart);
		etape.addContent(ravitaillement);
		etape.addContent(dejeuner);
		etape.addContent(midi);
		etape.addContent(soir);
		etape.addContent(nuit);
		// Ajout de l'étape
		String pos;
		if (fin == true) {
			racine.addContent(etape);
			pos = "à la fin";
		} else {
			// Recherche de l'index souhaité.
			int index = OFFSET_INFOS_RANDO + 2 * (numEtape - 1);
			System.out.println("Index = " + index);
			racine.addContent(index, etape);
			pos = Integer.toString(numEtape + 1);
		}
		// On ajoute par défaut un point de départ un point d'arrivée.
		PointGeo depart = new PointGeo("Depart", 0.0, 0, Pause.H0M0);
		PointGeo arrivee = new PointGeo("Arrivee", 20.0, 0, Pause.H0M0);
		System.out.println("Ajout d'une étape à la fin de la randonnée " + nom + " (position = " + pos + ")");
		saveXML();
		update();
		if (fin == true) {
			addXML(getNumEtapes(), depart);
			addXML(getNumEtapes(), arrivee);
		} else {
			addXML(numEtape, depart);
			addXML(numEtape, arrivee);
		}
	}

	/**
	 * AJOUTE UN NOUVEAU POINT DANS LE FICHIER XML.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape où le point doit être ajouter, de type
	 *            'int'.
	 * @param newPoint
	 *            Nouveau point de l'étape
	 * @param newPause
	 *            Nouveau temps de pause au point de type 'Pause'.
	 * @return Aucun.
	 */
	public void addXML(int numEtape, PointGeo newPoint) {
		List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
		Element etapeCourante = listeEtapes.get(numEtape - 1);
		// Création des champs.
		Element point = new Element(BaliseXML.XML_POINT);
		Element toponyme = new Element(BaliseXML.XML_TOPONYME);
		toponyme.setText(newPoint.getNom());
		Element pk = new Element(BaliseXML.XML_PK);
		pk.setText(Double.toString(newPoint.getPK()));
		Element altitude = new Element(BaliseXML.XML_ALTITUDE);
		altitude.setText(Integer.toString(newPoint.getAltitude()));
		Element pause = new Element(BaliseXML.XML_PAUSE);
		pause.setText(newPoint.getPause().getSymbol());
		point.addContent(toponyme);
		point.addContent(pk);
		point.addContent(altitude);
		point.addContent(pause);
		// Recherche de l'index d'insertion pour que les points soient classés
		// par PK croissant.
		int index = OFFSET_INFOS_ETAPE;
		double pkCourant = 0.0;
		Element pointCourant;
		List<Element> listePoints = etapeCourante.getChildren(BaliseXML.XML_POINT);
		Iterator<Element> k = listePoints.iterator();
		while (k.hasNext()) {
			pointCourant = k.next();
			pkCourant = Double.parseDouble(pointCourant.getChild(BaliseXML.XML_PK).getText());
			if (newPoint.getPK() < pkCourant) {
				break;
			} else {
				index = index + OFFSET_ELEMENT;
			}
		}
		// Ajout du point.
		etapeCourante.addContent(index, point);
		System.out.println("Ajout d'un point sur l'étape " + numEtape + " de la randonnée " + nom);
		saveXML();
	}

	/**
	 * MODIFIE L'HEURE DE DEPART D'UNE ETAPE DANS LE FICHIER XML.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape à modifier, de type 'int'.
	 * @param newHeureDepart
	 *            Nouvelle heure de départ de type 'Heure'.
	 * @return Aucun.
	 */
	public void modifyXML(int numEtape, Heure newHeureDepart) {
		List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
		Element etapeCourante = listeEtapes.get(numEtape - 1);
		Element heureDepart = etapeCourante.getChild(BaliseXML.XML_HEUREDEPART);
		heureDepart.setText(newHeureDepart.getSymbol());
		System.out.println("Modification de l'heure de départ de l'étape " + numEtape + " de la randonnée " + nom);
		saveXML();
	}

	/**
	 * MODIFIE LES ATTRIBUTS GEOGRAPHIQUES D'UNE ETAPE.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape à modifier, de type 'int'.
	 * @param newLieu
	 *            Nouveau lieu de type 'String'.
	 * @param mode
	 *            Booléen indiquant le lieu à modifier. 'true' = lieu du repas
	 *            de midi. 'false' = lieu de ravitaillement.
	 * @return Aucun.
	 */
	public void modifyXML(int numEtape, String newLieu, boolean mode) {
		List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
		Element etapeCourante = listeEtapes.get(numEtape - 1);
		if (mode == true) {
			Element midi = etapeCourante.getChild(BaliseXML.XML_MIDI);
			Element lieuMidi = midi.getChild(BaliseXML.XML_LIEUMIDI);
			lieuMidi.setText(newLieu);
			System.out.println(
					"Modification du lieu du repas de midi de l'étape " + numEtape + " de la randonnée " + nom);
		} else {
			Element ravitaillement = etapeCourante.getChild(BaliseXML.XML_RAVITAILLEMENT);
			ravitaillement.setText(newLieu);
			System.out.println(
					"Modification du lieu de ravitaillement de l'étape " + numEtape + " de la randonnée " + nom);
		}
		saveXML();
	}

	/**
	 * MODIFIE LES ATTRIBUTS DE CONDITION D'UNE ETAPE.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape à modifier, de type 'int'.
	 * @param newCondition
	 *            Nouvelle condition de type 'Condition'.
	 * @param item
	 *            Entier indiquant l'item à modifier. '0' = petit déjeuner. '1'
	 *            = repas du midi. '2' = repas du soir. '3' = nuit.
	 * @return Aucun.
	 */
	public void modifyXML(int numEtape, Condition newCondition, int item) {
		List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
		Element etapeCourante = listeEtapes.get(numEtape - 1);
		switch (item) {
		case 0:
			Element petitDejeuner = etapeCourante.getChild(BaliseXML.XML_DEJEUNER);
			petitDejeuner.setText(newCondition.getSymbol());
			System.out.println("Modification du petit déjeuner de l'étape " + numEtape + " de la randonnée " + nom);
			break;
		case 1:
			Element midi = etapeCourante.getChild(BaliseXML.XML_MIDI);
			Element repasMidi = midi.getChild(BaliseXML.XML_REPASMIDI);
			repasMidi.setText(newCondition.getSymbol());
			System.out.println("Modification du repas de midi de l'étape " + numEtape + " de la randonnée " + nom);
			break;
		case 2:
			Element repasSoir = etapeCourante.getChild(BaliseXML.XML_SOIR);
			repasSoir.setText(newCondition.getSymbol());
			System.out.println("Modification du repas du soir de l'étape " + numEtape + " de la randonnée " + nom);
			break;
		case 3:
			Element nuit = etapeCourante.getChild(BaliseXML.XML_NUIT);
			nuit.setText(newCondition.getSymbol());
			System.out.println("Modification de la nuit de l'étape " + numEtape + " de la randonnée " + nom);
			break;
		}
		saveXML();
	}

	/**
	 * MODIFIE LE TOPONYME D'UN POINT DANS LE FICHIER XML.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape où se trouve le point à modifier, de type
	 *            'int'.
	 * @param pk
	 *            PK du point à modifier (unique paramètre distinguant tous les
	 *            points).
	 * @param newToponyme
	 *            Nouveau toponyme du point de type 'String'.
	 * @return Aucun.
	 */
	public void modifyXML(int numEtape, double pk, String newToponyme) {
		List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
		Element etapeCourante = listeEtapes.get(numEtape - 1);
		// Recherche du point via son PK.
		List<Element> listePoints = etapeCourante.getChildren(BaliseXML.XML_POINT);
		Iterator<Element> itPoints = listePoints.iterator();
		while (itPoints.hasNext()) {
			Element pointCourant = itPoints.next();
			double pkCourant = Double.parseDouble(pointCourant.getChild(BaliseXML.XML_PK).getText());
			if (pkCourant == pk) {
				// Point trouvé.
				pointCourant.getChild(BaliseXML.XML_TOPONYME).setText(newToponyme);
				break;
			}
		}
		System.out.println("Modification du toponyme d'un point sur l'étape " + numEtape + " de la randonnée " + nom);
		saveXML();
	}

	/**
	 * MODIFIE LE PK D'UN POINT DANS LE FICHIER XML.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape où se trouve le point à modifier, de type
	 *            'int'.
	 * @param pk
	 *            PK du point à modifier (unique paramètre distinguant tous les
	 *            points).
	 * @param newPk
	 *            Nouveau PK du point de type 'double'.
	 * @return Aucun.
	 */
	public void modifyXML(int numEtape, double pk, double newPk) {
		List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
		Element etapeCourante = listeEtapes.get(numEtape - 1);
		// Recherche du point via son PK.
		List<Element> listePoints = etapeCourante.getChildren(BaliseXML.XML_POINT);
		Iterator<Element> itPoints = listePoints.iterator();
		while (itPoints.hasNext()) {
			Element pointCourant = itPoints.next();
			double pkCourant = Double.parseDouble(pointCourant.getChild(BaliseXML.XML_PK).getText());
			if (pkCourant == pk) {
				// Point trouvé.
				pointCourant.getChild(BaliseXML.XML_PK).setText(Double.toString(newPk));
				break;
			}
		}
		System.out.println("Modification du PK d'un point sur l'étape " + numEtape + " de la randonnée " + nom);
		saveXML();
	}

	/**
	 * MODIFIE L'ALTITUDE D'UN POINT DANS LE FICHIER XML.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape où se trouve le point à modifier, de type
	 *            'int'.
	 * @param pk
	 *            PK du point à modifier (unique paramètre distinguant tous les
	 *            points).
	 * @param newAltitude
	 *            Nouvelle altitude du point de type 'int'.
	 * @return Aucun.
	 */
	public void modifyXML(int numEtape, double pk, int newAltitude) {
		List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
		Element etapeCourante = listeEtapes.get(numEtape - 1);
		// Recherche du point via son PK.
		List<Element> listePoints = etapeCourante.getChildren(BaliseXML.XML_POINT);
		Iterator<Element> itPoints = listePoints.iterator();
		while (itPoints.hasNext()) {
			Element pointCourant = itPoints.next();
			double pkCourant = Double.parseDouble(pointCourant.getChild(BaliseXML.XML_PK).getText());
			if (pkCourant == pk) {
				// Point trouvé.
				pointCourant.getChild(BaliseXML.XML_ALTITUDE).setText(Integer.toString(newAltitude));
				break;
			}
		}
		System.out.println("Modification de l'altitude d'un point sur l'étape " + numEtape + " de la randonnée " + nom);
		saveXML();
	}

	/**
	 * MODIFIE LE TEMPS DE PAUSE D'UN POINT DANS LE FICHIER XML.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape où se trouve le point à modifier, de type
	 *            'int'.
	 * @param pk
	 *            PK du point à modifier (unique paramètre distinguant tous les
	 *            points).
	 * @param newPause
	 *            Nouveau temps de pause au point de type 'Pause'.
	 * @return Aucun.
	 */
	public void modifyXML(int numEtape, double pk, Pause newPause) {
		List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
		Element etapeCourante = listeEtapes.get(numEtape - 1);
		// Recherche du point via son PK.
		List<Element> listePoints = etapeCourante.getChildren(BaliseXML.XML_POINT);
		Iterator<Element> itPoints = listePoints.iterator();
		while (itPoints.hasNext()) {
			Element pointCourant = itPoints.next();
			double pkCourant = Double.parseDouble(pointCourant.getChild(BaliseXML.XML_PK).getText());
			if (pkCourant == pk) {
				// Point trouvé.
				pointCourant.getChild(BaliseXML.XML_PAUSE).setText(newPause.getSymbol());
				break;
			}
		}
		System.out.println(
				"Modification du temps de pause d'un point sur l'étape " + numEtape + " de la randonnée " + nom);
		saveXML();
	}

	/**
	 * SUPPRIME UNE ETAPE DE LA RANDONNEE DANS LE FICHIER XML.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape à suppprimer.
	 * @return Aucun.
	 */
	public void deleteXML(int numEtape) {
		if (numEtape <= trace.size()) {
			List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
			System.out.println("Suppression de l'étape " + numEtape + " de la randonnée " + nom);
			racine.removeContent(listeEtapes.get(numEtape - 1));
			saveXML();
		}
	}

	/**
	 * SUPPRIME UNE ETAPE DE LA RANDONNEE DANS LE FICHIER XML.
	 * 
	 * @param numEtape
	 *            Numéro de l'étape à suppprimer.
	 * @return Aucun.
	 */
	public void deleteXML(int numEtape, double pk) {
		// On se place à l'étape concernée si elle existe.
		if (numEtape <= trace.size()) {
			List<Element> listeEtapes = racine.getChildren(BaliseXML.XML_ETAPE);
			Element etape = listeEtapes.get(numEtape - 1);
			// On cherche le point ayant le PK donné en argument.
			List<Element> listePoints = etape.getChildren(BaliseXML.XML_POINT);
			Iterator<Element> itPoints = listePoints.iterator();
			while (itPoints.hasNext()) {
				Element pointCourant = (Element) itPoints.next();
				double pkCourant = Double.parseDouble(pointCourant.getChild(BaliseXML.XML_PK).getText());
				if (pkCourant == pk) {
					System.out.println("Suppression d'un point sur l'étape " + numEtape + " de la randonnée " + nom);
					etape.removeContent(pointCourant);
					break;
				}
			}
			saveXML();
		}
	}

	/**
	 * AFFICHE UNE RANDONNEE (DEBUG).
	 * 
	 * @param Aucun.
	 * @return Aucun.
	 */
	public void afficherRando() {
		Iterator<Etape> i = trace.iterator();
		while (i.hasNext()) {
			Etape e = i.next();
			System.out.println("Etape : " + Temps.afficherHeure(e.getHeureDepart().getHeure()) + " " + e.getLongueur()
					+ " " + Temps.afficherHeure(e.getTemps()) + " " + e.getRavitaillement() + " " + e.getDejeuner()
					+ " " + e.getLieuMidi() + " " + e.getMidi() + " " + e.getSoir() + " " + e.getNuit() + " "
					+ e.getDeniveleNeg() + " " + e.getDenivelePos());
			Iterator<Chemin> j = e.getItineraire().iterator();
			while (j.hasNext()) {
				Chemin c = j.next();
				System.out.println("     Chemin : ");
				System.out.println("          Point : " + c.getDebut().getNom() + " " + c.getDebut().getPK() + " "
						+ c.getDebut().getAltitude() + " " + c.getDebut().getPause() + " "
						+ Temps.afficherHeure(c.getDebut().getHeure()));
				System.out.println("          Point : " + c.getFin().getNom() + " " + c.getFin().getPK() + " "
						+ c.getFin().getAltitude() + " " + c.getFin().getPause() + " "
						+ Temps.afficherHeure(c.getFin().getHeure()));
			}
		}
	}
}

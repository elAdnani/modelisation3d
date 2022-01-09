package modele;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import math.Vecteur;

/**
 * Cette classe sert à générer les faces qui sont un ensemble de points. <br>
 * Elle réalise le degré de luminosité par rapport à une source lumineuse.
 *
 * @author <a href="mailto:leo.benhatat.etu@univ-lille.fr">Leo BEN HATAT</a>
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnan KOUAKOUA</a>
 *
 *         IUT-A Informatique, Universite de Lille.
 */
public class Face {

	private List<Point> points;
	private static final Vecteur SOURCELUMINEUSE = new Vecteur(0, 0, 1);
	private List<Face> division;

	public Face() {
		this(new ArrayList<>());
	}

	/**
	 * 
	 * @param points Liste de point que compose la face
	 */
	public Face(List<Point> points) {
		this.points = points;
		division = new ArrayList<>();
	}

	/**
	 * Permet de diviser la face actuel en 4 faces
	 */
	public void divisionTriangulaire() {
		Point a = this.points.get(0);
		Point b = this.points.get(1);
		Point c = this.points.get(2);
		Point milieuAB = new Point((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2, (a.getZ() + b.getZ()) / 2);
		Point milieuAC = new Point((a.getX() + c.getX()) / 2, (a.getY() + c.getY()) / 2, (a.getZ() + c.getZ()) / 2);
		Point milieuBC = new Point((b.getX() + c.getX()) / 2, (b.getY() + c.getY()) / 2, (b.getZ() + c.getZ()) / 2);

		this.division = new ArrayList<>();
		division.add(new Face(Arrays.asList(milieuAB, milieuAC, milieuBC)));
		division.add(new Face(Arrays.asList(a, milieuAB, milieuAC)));
		division.add(new Face(Arrays.asList(b, milieuAB, milieuBC)));
		division.add(new Face(Arrays.asList(c, milieuBC, milieuAC)));
	}

	/**
	 * Permet de diviser la face actuel en nombre de fois spécifié en parametre.
	 * 
	 * @param x représente le nombre de division triangulaire réalisée sur chaque
	 *          faces.
	 */
	public void divisionTriangulaireX(int x) {
		if (x > 0) {
			divisionTriangulaire();
			for (Face f : division)
				f.divisionTriangulaireX(x - 1);
		}
	}

	/**
	 * Récupère l'entièreté des divisions de la face actuel
	 * 
	 * @return
	 */
	public List<Face> getFaces() {
		List<Face> res = new ArrayList<>();
		if(this.division.isEmpty())
			res.add(this);
		for (Face e : this.division) {
			res.addAll(e.getFaces());
		}
		return res;
	}

	public boolean isEmpty() {
		return this.division.isEmpty();
	}

	public List<Point> getPoints() {
		return points;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[Face");
		for (Point p : points) {
			sb.append(p + "---");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Permet d'obtenir la somme des coordonnées Z de la face
	 * 
	 * @return somme des coordonnées Z
	 */
	public double getAverageZ() {
		double sum = 0;
		for (Point p : points) {
			sum += p.getZ();
		}
		return sum / points.size();
	}

	/**
	 * Permet d'obtenir la somme des coordonnées Y de la face
	 * 
	 * @return somme des coordonnées Y
	 */
	public double getAverageY() {
		double sum = 0;
		for (Point p : points) {
			sum += p.getY();
		}

		return sum / points.size();
	}

	/**
	 * Permet d'obtenir la somme des coordonnées X de la face
	 * 
	 * @return somme des coordonnées X
	 */
	public double getAverageX() {
		double sum = 0;
		for (Point p : points) {
			sum += p.getX();
		}
		return sum / points.size();
	}

	/**
	 * Calcul le degré de couleur d'une face.<br>
	 * En fonction de la source lumineuse indique le rapport de la projection.
	 * 
	 * @return le degré de couleur de la face
	 */
	public double degreDeCouleur() {
		double degreDeCouleur = -1;
		Vecteur normalUnitaire;
		if (this.points.size() >= 3) {
			normalUnitaire = Vecteur.normalUnitaire(this.points.get(0), this.points.get(1), this.points.get(2));
			degreDeCouleur = (normalUnitaire.getX() * SOURCELUMINEUSE.getX())
					+ (normalUnitaire.getY() * SOURCELUMINEUSE.getY())
					+ (normalUnitaire.getZ() * SOURCELUMINEUSE.getZ());
			degreDeCouleur = Math.abs(degreDeCouleur);
			if (degreDeCouleur > 255) {
				degreDeCouleur = 255;
			}
		}
		return degreDeCouleur;

	}

	public void add(Point p) {
		this.points.add(p);

	}

}
package math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import modele.geometrique.FigureFabrique;
import modele.geometrique.Point;
import modele.geometrique.Vecteur;

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

	private List<Point> vertices;
	private static final Vecteur LIGHTSOURCE = FigureFabrique.getInstance().vector(0, 0, 1);;
	private List<Face> division;

	public Face() {
		this(new ArrayList<>());
	}

	/**
	 * @param points Liste de point que compose la face
	 */
	public Face(List<Point> vertices) {
		this.vertices = vertices;
		division = new ArrayList<>();
	}

	/**
	 * Permet de diviser la face actuel en 4 faces
	 */
	public void triangularDivision() {
		FigureFabrique fabric = FigureFabrique.getInstance();
		Point a = this.vertices.get(0);
		Point b = this.vertices.get(1);
		Point c = this.vertices.get(2);
		Point middleAB = fabric.vertex((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2, (a.getZ() + b.getZ()) / 2);
		Point middleAC = fabric.vertex((a.getX() + c.getX()) / 2, (a.getY() + c.getY()) / 2, (a.getZ() + c.getZ()) / 2);
		Point middleBC = fabric.vertex((b.getX() + c.getX()) / 2, (b.getY() + c.getY()) / 2, (b.getZ() + c.getZ()) / 2);

		this.division = new ArrayList<>();
		division.add(new Face(Arrays.asList(middleAB, middleAC, middleBC)));
		division.add(new Face(Arrays.asList(a, middleAB, middleAC)));
		division.add(new Face(Arrays.asList(b, middleAB, middleBC)));
		division.add(new Face(Arrays.asList(c, middleBC, middleAC)));
	}

	/**
	 * Permet de diviser la face actuel en nombre de fois spécifié en parametre.
	 * 
	 * @param x représente le nombre de division triangulaire réalisée sur chaque
	 *          faces.
	 */
	public void triangularDivisionX(int degree) {
		if (degree > 0) {
			triangularDivision();
			for (Face f : division)
				f.triangularDivisionX(degree - 1);
		}
	}

	/**
	 * Récupère l'entièreté des divisions de la face actuel
	 * 
	 * @return
	 */
	public List<Face> getFaces() {
		List<Face> res = new ArrayList<>();
		if (this.division.isEmpty())
			res.add(this);
		for (Face e : this.division) {
			res.addAll(e.getFaces());
		}
		return res;
	}

	public boolean isEmpty() {
		return this.division.isEmpty();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[Face");
		for (Point p : vertices) {
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
		for (Point p : vertices) {
			sum += p.getZ();
		}
		return sum / vertices.size();
	}

	/**
	 * Permet d'obtenir la somme des coordonnées Y de la face
	 * 
	 * @return somme des coordonnées Y
	 */
	public double getAverageY() {
		double sum = 0;
		for (Point p : vertices) {
			sum += p.getY();
		}

		return sum / vertices.size();
	}

	/**
	 * Permet d'obtenir la somme des coordonnées X de la face
	 * 
	 * @return somme des coordonnées X
	 */
	public double getAverageX() {
		double sum = 0;
		for (Point p : vertices) {
			sum += p.getX();
		}
		return sum / vertices.size();
	}

	public List<Point> getPoints() {
		return vertices;
	}

	/**
	 * Calcul le degré de couleur d'une face.<br>
	 * En fonction de la source lumineuse indique le rapport de la projection.
	 * 
	 * @return le degré de couleur de la face
	 */
	public double colorDegree() {
		double colorDegree = -1;
		if (this.vertices.size() >= 3) {
			Vecteur normalUnitaire = VecteurOutil.unitNormal(this.vertices.get(0), this.vertices.get(1),
					this.vertices.get(2));
			colorDegree = (normalUnitaire.getX() * LIGHTSOURCE.getX()) + (normalUnitaire.getY() * LIGHTSOURCE.getY())
					+ (normalUnitaire.getZ() * LIGHTSOURCE.getZ());
			colorDegree = Math.abs(colorDegree);
			if (colorDegree > 255) {
				colorDegree = 255;
			}
		}
		return colorDegree;

	}

	public void add(Point p) {
		this.vertices.add(p);
	}

}
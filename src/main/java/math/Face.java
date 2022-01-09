package math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import modele.geometrique.FigureFabric;
import modele.geometrique.Vertex;
import modele.geometrique.Vector;

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

	private List<Vertex> vertices;
	private static final Vector LIGHTSOURCE = FigureFabric.getInstance().vector(0, 0, 1);;
	private List<Face> division;

	public Face() {
		this(new ArrayList<>());
	}

	/**
	 * @param points Liste de point que compose la face
	 */
	public Face(List<Vertex> vertices) {
		this.vertices = vertices;
		division = new ArrayList<>();
	}

	/**
	 * Permet de diviser la face actuel en 4 faces
	 */
	public void triangularDivision() {
		FigureFabric fabric = FigureFabric.getInstance();
		Vertex pointA = this.vertices.get(0);
		Vertex pointB = this.vertices.get(1);
		Vertex pointC = this.vertices.get(2);
		Vertex middleAB = fabric.vertex((pointA.getX() + pointB.getX()) / 2, (pointA.getY() + pointB.getY()) / 2, (pointA.getZ() + pointB.getZ()) / 2);
		Vertex middleAC = fabric.vertex((pointA.getX() + pointC.getX()) / 2, (pointA.getY() + pointC.getY()) / 2, (pointA.getZ() + pointC.getZ()) / 2);
		Vertex middleBC = fabric.vertex((pointB.getX() + pointC.getX()) / 2, (pointB.getY() + pointC.getY()) / 2, (pointB.getZ() + pointC.getZ()) / 2);

		this.division = new ArrayList<>();
		division.add(new Face(Arrays.asList(middleAB, middleAC, middleBC)));
		division.add(new Face(Arrays.asList(pointA, middleAB, middleAC)));
		division.add(new Face(Arrays.asList(pointB, middleAB, middleBC)));
		division.add(new Face(Arrays.asList(pointC, middleBC, middleAC)));
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
		StringBuilder builder = new StringBuilder("[Face");
		for (Vertex p : vertices) {
			builder.append(p + "---");
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Permet d'obtenir la somme des coordonnées Z de la face
	 * 
	 * @return somme des coordonnées Z
	 */
	public double getAverageZ() {
		double sum = 0;
		for (Vertex p : vertices) {
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
		for (Vertex p : vertices) {
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
		for (Vertex p : vertices) {
			sum += p.getX();
		}
		return sum / vertices.size();
	}

	public List<Vertex> getPoints() {
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
		double nbVertices = 3;
		double maxColor = 255;
		if (this.vertices.size() >= nbVertices) {
			Vector normalUnitaire = VectorTool.unitNormal(this.vertices.get(0), this.vertices.get(1),
					this.vertices.get(2));
			colorDegree = (normalUnitaire.getX() * LIGHTSOURCE.getX()) + (normalUnitaire.getY() * LIGHTSOURCE.getY())
					+ (normalUnitaire.getZ() * LIGHTSOURCE.getZ());
			colorDegree = Math.abs(colorDegree);
			if (colorDegree > maxColor) {
				colorDegree = 255;
			}
		}
		return colorDegree;

	}

	public void add(Vertex point) {
		this.vertices.add(point);
	}

}
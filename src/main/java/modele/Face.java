package modele;

import java.util.ArrayList;
import java.util.List;

public class Face {

	private List<Point> points;
	private static final   Vecteur SOURCELUMINEUSE = new Vecteur(0, 0, 1);
	private Vecteur normalUnitaire;

	// List<Integer> reference; // indice de la matrice des listes de point
	// Matrice METTRE EN PARAMETRE

	public Face() {
		this(new ArrayList<>());
	}

	public Face(List<Point> points) {
		this.points = points;
		if(points.size()>=3) {
			this.normalUnitaire = Vecteur.normalUnitaire(this.points.get(0),this.points.get(1),this.points.get(2));
		}
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

	public double getAverageZ() {
		double sum = 0;
		for (Point p : points) {
			sum += p.getZ();
		}

		return sum / points.size();
	}

	public double getAverageY() {
		double sum = 0;
		for (Point p : points) {
			sum += p.getY();
		}

		return sum / points.size();
	}

	public double getAverageX() {
		double sum = 0;
		for (Point p : points) {
			sum += p.getX();
		}

		return sum / points.size();
	}
	
	
	public double degreDeCouleur() {
		if(this.points.size() >=3 && this.normalUnitaire == null) {
			this.normalUnitaire = Vecteur.normalUnitaire(this.points.get(0),this.points.get(1),this.points.get(2));
		}
		double degreDeCouleur =	(double)(this.normalUnitaire.getX() * SOURCELUMINEUSE.getX()) 
								+ (double)(this.normalUnitaire.getY() * SOURCELUMINEUSE.getY()) 
								+ (double)(this.normalUnitaire.getZ() * SOURCELUMINEUSE.getZ());
		if(degreDeCouleur<0) {
			degreDeCouleur=0;
		}
		if(degreDeCouleur>255) {
			degreDeCouleur=255;
		}

		return degreDeCouleur;
		
	}
	
/*	public static void main(String[] args) {
		
		Face f = new Face();
		f.add(new Point(-2, 1, 3));
		f.add(new Point(-3, 2, 3));
		f.add(new Point(-2, 2, 5));
		
		System.out.println(f.degreDeCouleur());
	}*/

	public void add(Point p) {
		this.points.add(p);
		if(points.size()>=3) {
			this.normalUnitaire = Vecteur.normalUnitaire(this.points.get(0),this.points.get(2),this.points.get(3));
		}
	}


}
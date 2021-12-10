package modele;

import java.util.ArrayList;
import java.util.List;

public class Face {

	private List<Point> points;
	private static  Vecteur sourceLumineuse = new Vecteur(1, 1, 1);
	private Vecteur normalUnitaire;
	private double degreDeCouleur;

	// List<Integer> reference; // indice de la matrice des listes de point
	// Matrice METTRE EN PARAMETRE

	public Face() {
		this(new ArrayList<>());
	}

	public Face(List<Point> points) {
		this.points = points;
		
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
	
	public void normalUnitaire() {
		int x=0,y=0,z=0;
		
		Point A = points.get(0);
		Point B = points.get(1);
		Point C = points.get(2);
		
		Vecteur AB  = new Vecteur(B.getX()-A.getX(), B.getY()-A.getY(), B.getZ()-A.getZ());
		Vecteur AC  = new Vecteur(C.getX()-A.getX(), C.getY()-A.getY(), C.getZ()-A.getZ());
		
		double detX = (AB.getY()*AC.getZ()) - (AC.getY()*AB.getZ());
		double detY = (AC.getX()*AB.getZ()) - (AC.getZ()* AB.getX());
		double detZ =(AB.getX()*AC.getY()) - (AC.getX()*AB.getY());
		
		Vecteur ABvAC = new Vecteur(detX, detY, detZ);
		
		double normeABvAC = Math.sqrt(detX*detX+ detY*detY + detZ*detZ);
		
		this.normalUnitaire = new Vecteur(detX/normeABvAC, detY/normeABvAC, detZ/normeABvAC);
	}
	
	public double normalDegreDeCouleur() {
		return (this.normalUnitaire.getX() * sourceLumineuse.getX()) + (this.normalUnitaire.getY() * sourceLumineuse.getY()) + (this.normalUnitaire.getZ() * sourceLumineuse.getZ());
		
	}
	
	public static void main(String[] args) {
		
		Face f = new Face();
		f.add(new Point(-2, 1, 3));
		f.add(new Point(-3, 2, 3));
		f.add(new Point(-2, 2, 5));
		
		f.normalUnitaire();
	}

	public void add(Point p) {
		this.points.add(p);
		
		
	}

}
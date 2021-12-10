package modele;

import java.util.ArrayList;
import java.util.List;

public class Face {

	private List<Point> points;
	private static final   Vecteur SOURCELUMINEUSE = new Vecteur(1, 1, 1);
	private Vecteur normalUnitaire;

	// List<Integer> reference; // indice de la matrice des listes de point
	// Matrice METTRE EN PARAMETRE

	public Face() {
		this(new ArrayList<>());
	}

	public Face(List<Point> points) {
		this.points = points;
		if(points.size()>=3) {
			normalUnitaire();
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
	
	private void normalUnitaire() {
		
		Point A = points.get(0);
		Point B = points.get(1);
		Point C = points.get(2);
		
		Vecteur AB  = new Vecteur(B.getX()-A.getX(), B.getY()-A.getY(), B.getZ()-A.getZ());
		Vecteur AC  = new Vecteur(C.getX()-A.getX(), C.getY()-A.getY(), C.getZ()-A.getZ());
		
		double detX = (AB.getY()*AC.getZ()) - (AC.getY()*AB.getZ());
		double detY = (AC.getX()*AB.getZ()) - (AC.getZ()* AB.getX());
		double detZ =(AB.getX()*AC.getY()) - (AC.getX()*AB.getY());
	//	Vecteur ABvAC = new Vecteur(detX, detY, detZ);
		
		double normeABvAC = Math.sqrt(detX*detX+ detY*detY + detZ*detZ);
		
		this.normalUnitaire = new Vecteur(detX/normeABvAC, detY/normeABvAC, detZ/normeABvAC);
	}
	
	public double degreDeCouleur() {
		if(this.normalUnitaire == null) {
			normalUnitaire();
		}
		double res =(this.normalUnitaire.getX() * SOURCELUMINEUSE.getX()) + (this.normalUnitaire.getY() * SOURCELUMINEUSE.getY()) + (this.normalUnitaire.getZ() * SOURCELUMINEUSE.getZ());
		if(res<0) {
			res=0;
		}
		return res;
		
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
			normalUnitaire();
		}
	}


}
package modele;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Face {

	private List<Point>          points;
	private static final Vecteur SOURCELUMINEUSE = new Vecteur(0, 0, 1);
	private Vecteur              normalUnitaire;
	private List<Face> division ;

	// List<Integer> reference; // indice de la matrice des listes de point
	// Matrice METTRE EN PARAMETRE

	public Face() {
		this(new ArrayList<>());
	}

	public Face(List<Point> points) {
		this.points = points;
		if (points.size() >= 3)
		{
			this.normalUnitaire = Vecteur.normalUnitaire(this.points.get(0), this.points.get(1), this.points.get(2));
		}
		division = new ArrayList<>();
	}
	
	// DIVISION : 1 triangle en 4 triangles
	public void divisionTriangulaire() {
		Point a = this.points.get(0);
		Point b = this.points.get(1);
		Point c = this.points.get(2);
		Point milieuAB = new Point((a.getX()+b.getX())/2,(a.getY()+b.getY())/2,(a.getZ()+b.getZ())/2);
		Point milieuAC=new Point((a.getX()+c.getX())/2,(a.getY()+c.getY())/2,(a.getZ()+c.getZ())/2);
		Point milieuBC=new Point((b.getX()+c.getX())/2,(b.getY()+c.getY())/2,(b.getZ()+c.getZ())/2);

		this.division = new ArrayList<>();//Face(points);
		division.add(new Face(new ArrayList<Point>(Arrays.asList(milieuAB, milieuAC, milieuBC))));
		division.add(new Face(new ArrayList<Point>(Arrays.asList(a, milieuAB, c))));
		division.add(new Face(new ArrayList<Point>(Arrays.asList(b, milieuAB, milieuBC))));
		division.add(new Face(new ArrayList<Point>(Arrays.asList(c, milieuBC, milieuAC))));
	}
	
	/* TEST division triangulaire en 2
	public void divisionTriangulaire() {
		Point a = this.points.get(0);
		Point b = this.points.get(1);
		Point c = this.points.get(2);
		Point milieuAB = new Point((a.getX()+b.getX())/2,(a.getY()+b.getY())/2,(a.getZ()+b.getZ())/2);
		Point milieuAC=new Point((a.getX()+c.getX())/2,(a.getY()+c.getY())/2,(a.getZ()+c.getZ())/2);
		Point milieuBC=new Point((b.getX()+c.getX())/2,(b.getY()+c.getY())/2,(b.getZ()+c.getZ())/2);

		this.division = new ArrayList<>();//Face(points);
		division.add(new Face(new ArrayList<>(Arrays.asList(a, milieuBC, b))));
		division.add(new Face(new ArrayList<>(Arrays.asList(a, milieuBC, c))));
	}*/
	/**
	 * x > 0
	 * @param x
	 */
	public void divisionTriangulaireX( int x) {
		if(x>0) {
			divisionTriangulaire();
			for(Face f : division)
				f.divisionTriangulaireX(x-1);
		}
	}
	
	private List<Face> getFace() {
	        List<Face> res = new ArrayList<>();
	        res.add(this);
	        
	        for (Face e : this.division) {
	        	res.add(e);
	        	if(!e.division.isEmpty()) {
	        		res.addAll(e.getFace());
	        	}
	        }

	        return res;
	 }
	
	public List<Face> getFaces(){
		 List<Face> res = new ArrayList<>();
		for (Face e : this.division) {
        	res.addAll(e.getFace());
        }
		return res;
	}
	
	
	 
	 
	 public boolean isEmpty(){
		return this.division.isEmpty();
	 }
	 
	
	public List<Point> getPoints() {
		return points;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[Face");
		for (Point p : points)
		{
			sb.append(p + "---");
		}
		sb.append("]");
		return sb.toString();
	}

	public double getAverageZ() {
		double sum = 0;
		for (Point p : points)
		{
			sum += p.getZ();
		}

		return sum / points.size();
	}

	public double getAverageY() {
		double sum = 0;
		for (Point p : points)
		{
			sum += p.getY();
		}

		return sum / points.size();
	}

	public double getAverageX() {
		double sum = 0;
		for (Point p : points)
		{
			sum += p.getX();
		}

		return sum / points.size();
	}

	public double degreDeCouleur() {
		double degreDeCouleur = -1;
		System.out.println("DEGRES COULEUR DEBUT");
		if (this.points.size() >= 3)
		{
			System.out.println(points);

			this.normalUnitaire = Vecteur.normalUnitaire(this.points.get(0), this.points.get(1), this.points.get(2));
			
			degreDeCouleur = (double) (this.normalUnitaire.getX() * SOURCELUMINEUSE.getX())
					+ (double) (this.normalUnitaire.getY() * SOURCELUMINEUSE.getY())
					+ (double) (this.normalUnitaire.getZ() * SOURCELUMINEUSE.getZ());
			System.out.println(degreDeCouleur);
			degreDeCouleur = Math.abs(degreDeCouleur);
			/*
			if (degreDeCouleur < 0)
			{
				degreDeCouleur = 0;
			}*/
			if (degreDeCouleur > 255)
			{
				degreDeCouleur = 255;
			}
		}
		System.out.println("DEGRES COULEUR FIN");
		return degreDeCouleur;

	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * Face f = new Face(); f.add(new Point(-2, 1, 3)); f.add(new Point(-3, 2, 3));
	 * f.add(new Point(-2, 2, 5));
	 * 
	 * System.out.println(f.degreDeCouleur()); }
	 */

	public void add(Point p) {
		this.points.add(p);
		if (points.size() >= 3)
		{
			this.normalUnitaire = Vecteur.normalUnitaire(this.points.get(0), this.points.get(2), this.points.get(3));
		}
	}

}
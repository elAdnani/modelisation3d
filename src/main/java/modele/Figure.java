package modele;

import math.Matrice;

/**
 * 
 *  Un point possède trois éléments :<br>
 * x pour l'axe des abscisse <br>
 * y pour l'axe des ordonnées <br>
 * z pour l'axe des côtés <br>
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 */
public abstract class Figure {

	protected Matrice coordonnees;
	
	
	protected Figure(double x, double y, double z, double element) {
		
		this.coordonnees= new Matrice(new double[][]{{x},{y},{z},{element}});
		
	}
	
	public double getX() {
		return this.coordonnees.lire(0, 0);
	}

	public void setX(double x) {
		this.coordonnees.ecrire(0, 0, x);
	}

	public double getY() {
		return this.coordonnees.lire(1, 0);
	}

	public void setY(double y) {
		this.coordonnees.ecrire(1, 0, y);
	}

	public double getZ() {
		return this.coordonnees.lire(2, 0);
	}

	public void setZ(double z) {
		this.coordonnees.ecrire(2, 0, z);
	}
}

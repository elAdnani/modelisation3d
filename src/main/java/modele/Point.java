package modele;

/**
 * 
 *  * Un point possède trois éléments :<br>
 * x pour l'axe des abscisse <br>
 * y pour l'axe des ordonnées <br>
 * z pour l'axe des côtés <br>
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 */
public class Point {

	Matrice coordonnees;

	/**
	 * Par défaut le point est de coordonnée (0,0,0)
	 */
	public Point() {
		this(0.0, 0.0, 0.0);
	}

	public Point(double x, double y, double z) {
		
		this.coordonnees= new Matrice(new double[][]{{x},{y},{z},{0}});
		
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

	public void setPoint(double x, double y, double z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	

	@Override
	public String toString() {
		return "[" + this.getX() + "," + this.getY() + "," + this.getZ() + "]";
	}

}
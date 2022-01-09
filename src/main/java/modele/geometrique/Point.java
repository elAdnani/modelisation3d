package modele.geometrique;

import math.Matrice;
import math.OutilMatriciel;

/**
 * 
 *   Un point possède trois éléments :<br>
 * x pour l'axe des abscisse <br>
 * y pour l'axe des ordonnées <br>
 * z pour l'axe des côtés <br>
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 */
public class Point extends Figure {

	/**
	 * Par défaut le point est de coordonnée (0,0,0)
	 */
	public Point() {
		this(0.0, 0.0, 0.0);
	}

	public Point(double cooX, double cooY, double cooZ) {

		super(cooX, cooY, cooZ, 0);

	}

	public void setPoint(double cooX, double cooY, double cooZ) {
		this.setX(cooX);
		this.setY(cooY);
		this.setZ(cooZ);
	}
	
	public Matrice getCoordonnee() {
		return super.getCoordonnee();
	}
	/**
	 * Permet de faire une transformation géométrique du point
	 * @param newCoo
	 */
	public void modifyCoordonnee(Matrice outilMatrice) {
		System.out.println(outilMatrice.multiplication(this.getCoordonnee()));
		super.setCoordonnee(outilMatrice.multiplication(this.getCoordonnee()));
	}

	@Override
	public String toString() {
		return "Point[" + this.getX() + "," + this.getY() + "," + this.getZ() + "]";
	}

}
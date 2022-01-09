package modele.geometrique;

import math.Matrix;

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
public class Vertex extends Figure {

	/**
	 * Par défaut le point est de coordonnée (0,0,0)
	 */
	public Vertex() {
		this(0.0, 0.0, 0.0);
	}

	public Vertex(double cooX, double cooY, double cooZ) {

		super(cooX, cooY, cooZ, 0);

	}

	public void setVertex(double cooX, double cooY, double cooZ) {
		this.setX(cooX);
		this.setY(cooY);
		this.setZ(cooZ);
	}
	
	@Override
	public Matrix getCoordinates() {
		return super.getCoordinates();
	}
	
	@Override
	public double getX() {
		return super.getX();
	}
	
	@Override
	public double getY() {
		return super.getY();
	}
	
	@Override
	public double getZ() {
		return super.getZ();
	}
	/**
	 * Permet de faire une transformation géométrique du point
	 * @param newCoo
	 */
	public void modifyCoordinates(Matrix outilMatrice) {
		super.setCoordinates(outilMatrice.multiplication(this.getCoordinates()));
	}

	@Override
	public String toString() {
		return "Point[" + this.getX() + "," + this.getY() + "," + this.getZ() + "]";
	}

}
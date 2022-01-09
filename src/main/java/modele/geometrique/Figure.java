package modele.geometrique;

import math.Matrix;

/**
 * 
 * Est une classe abstraite qui représentante les figures concrètes de
 * représentation géométrique.
 *
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 */
public abstract class Figure {

	private Matrix coordinates;

	/**
	 * Attribue en coordonnée une colonne d'une matrice.<br>
	 * Pour son construire, elle nécessite premièrement les coordonnées <br>
	 * puis l'élément qui permet de le distinguer.
	 * 
	 * @param cooX
	 * @param cooY
	 * @param cooZ
	 * @param element Si sa valeur est 0 alors c'est un point, si c'est 1 alors
	 *                c'est un vecteur.
	 */
	protected Figure(double cooX, double cooY, double cooZ, double element) {

		this.coordinates = new Matrix(new double[][] { { cooX }, { cooY }, { cooZ }, { element } });

	}

	public double getX() {
		return this.coordinates.read(0, 0);
	}

	public void setX(double cooX) {
		this.coordinates.write(0, 0, cooX);
	}

	public double getY() {
		return this.coordinates.read(1, 0);
	}

	public void setY(double cooY) {
		this.coordinates.write(1, 0, cooY);
	}

	public double getZ() {
		return this.coordinates.read(2, 0);
	}

	public void setZ(double cooZ) {
		this.coordinates.write(2, 0, cooZ);
	}

	protected void setCoordinates(Matrix newCoordonnee) {
		this.coordinates = newCoordonnee;
	}

	protected Matrix getCoordinates() {
		return this.coordinates;
	}

}

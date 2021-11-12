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

	private double x;
	private double y;
	private double z;

	/**
	 * Par défaut le point est de coordonnée (0,0,0)
	 */
	public Point() {
		this(0.0, 0.0, 0.0);
	}

	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Renvoie un tableau de coordonnée correspondant au point
	 * 
	 * @return
	 */
	public double[] getTableau() {
		return new double[] { x, y, z };
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "," + z + "]";
	}

}
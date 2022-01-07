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
public class Point extends Figure{


	/**
	 * Par défaut le point est de coordonnée (0,0,0)
	 */
	public Point() {
		this(0.0, 0.0, 0.0);
	}

	public Point(double x, double y, double z) {
		
		super(x,y,z,0);
		
	}

	public void setPoint(double x, double y, double z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	

	@Override
	public String toString() {
		return "Point[" + this.getX() + "," + this.getY() + "," + this.getZ() + "]";
	}

}
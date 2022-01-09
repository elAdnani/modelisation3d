package modele.geometrique;
/**
 * 
 * * Un vecteur est un segment orienté. Elle se définit par trois éléments qui sont sa direction, son sens et sa norme.<br>
 * Elle a nécéssairement besoin de trois éléments : <br>
 * x pour l'axe des abscisse <br>
 * y pour l'axe des ordonnées <br>
 * z pour l'axe des côtés <br>
 *
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 */
public class Vecteur extends Figure{

		public Vecteur() {
			this(0.0, 0.0, 0.0);
		}

		public Vecteur(double cooX, double cooY, double cooZ) {
			
			super(cooX,cooY,cooZ,1);
			
		}
		
		public void setVecteur(double cooX, double cooY, double cooZ) {
			this.setX(cooX);
			this.setY(cooY);
			this.setZ(cooZ);
		}
		

		@Override
		public String toString() {
			return "Vecteur[" + this.getX() + "," + this.getY() + "," + this.getZ() + "]";
		}

	
}

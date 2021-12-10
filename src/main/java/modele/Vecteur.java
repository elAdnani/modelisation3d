package modele;

public class Vecteur {

		Matrice coordonnees;

		public Vecteur() {
			this(0.0, 0.0, 0.0);
		}

		public Vecteur(double x, double y, double z) {
			
			this.coordonnees= new Matrice(new double[][]{{x},{y},{z},{1}});
			
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

		public void setVecteur(double x, double y, double z) {
			this.setX(x);
			this.setY(y);
			this.setZ(z);
		}
		

		@Override
		public String toString() {
			return "[" + this.getX() + "," + this.getY() + "," + this.getZ() + "]";
		}

	
}

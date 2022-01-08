package modele;

public class Vecteur extends Figure{

		public Vecteur() {
			this(0.0, 0.0, 0.0);
		}

		public Vecteur(double x, double y, double z) {
			
			super(x,y,z,1);
			
		}
		
		/**
		 * 
		 * @param A
		 * @param B
		 * @return le vecteur AB
		 */
		public static Vecteur pointEnVecteur (Point A, Point B) {
			
			return new Vecteur(B.getX()-A.getX(), B.getY()-A.getY(), B.getZ()-A.getZ());
			
		}
		
		public static Vecteur normalUnitaire(Point A, Point B, Point C) {
			Vecteur ABvAC = produitVectoriel(Vecteur.pointEnVecteur(A, B), Vecteur.pointEnVecteur(A, C));
			
			double normeABvAC = Math.sqrt((ABvAC.getX()*ABvAC.getX())+ (ABvAC.getY()*ABvAC.getY()) + (ABvAC.getZ()*ABvAC.getZ()));
		
			return new Vecteur(ABvAC.getX()/normeABvAC, ABvAC.getY()/normeABvAC, ABvAC.getZ()/normeABvAC);
		}
		
		
		public static Vecteur produitVectoriel(Vecteur AB, Vecteur AC) {
			
			return new Vecteur((AB.getY()*AC.getZ()) - (AC.getY()*AB.getZ())
								, (AB.getZ()*AC.getX()) - (AC.getZ()* AB.getX())
								, (AB.getX()*AC.getY()) - (AC.getX()*AB.getY()));
		}



		public void setVecteur(double x, double y, double z) {
			this.setX(x);
			this.setY(y);
			this.setZ(z);
		}
		

		@Override
		public String toString() {
			return "Vecteur[" + this.getX() + "," + this.getY() + "," + this.getZ() + "]";
		}

	
}

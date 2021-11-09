package modele;
/**
 * 
 * Cette classe sert à l'obtention de matrice permettant de modifier une matrice. <br>
 * Elle permet plus précisément de réaliser un mouvement voulu sur une matrice.<br>
 * Le mouvement correspond utilisait lors de la matrice sont au nombres de quatre : <br>
 *  - Un cadrage <br>
 *  - Une rotation <br>
 *  - Une homothetie <br>
 *  - Une translation <br>
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 9 nov. 2021
 * @version XX
 */
public abstract class OutilMatriciel {
	
	/**
	 * Donne la matrice de cadrage par rapport à une matrice m
	 */
	public static Matrice getCadrage(double k1, double k2, double k3) {
		Matrice cadrageDeCoefficientKi = new Matrice(new double[][] {
			{k1,0,0,0},
			{0,k2,0,0},
			{0,0,k3,0},
			{0,0,0,1},
		});
		
		return cadrageDeCoefficientKi;
	}
	
	/**
	 * Donne la matrice de cadrage par rapport à une matrice m
	 */
	public static Matrice getRotation(double x) {
		Matrice rotationAngleX = new Matrice(new double[][] {
			{1,0,0,0},
			{0,Math.cos(x),-Math.sin(x),0},
			{0,Math.sin(x),Math.cos(x),0},
			{0,0,0,1},
		});
		
		return rotationAngleX;
	}
	
	/**
	 * Donne la matrice homothetie par rapport à une matrice m
	 */
	public static Matrice getHomothetie(double x) {
		Matrice homothesieRapportX = new Matrice(new double[][] {
			{x,0,0,0},
			{0,x,0,0},
			{0,0,x,0},
			{0,0,0,1},
		});
		
		return homothesieRapportX;
	}
	
	/**
	 * Donne la matrice de translation par rapport à une matrice m
	 */
	public static Matrice getTranslation(double tx1, double tx2, double tx3) {
		Matrice translationDeVecteur = new Matrice(new double[][] {
			{1,0,0,tx1},
			{0,1,0,tx2},
			{0,0,1,tx3},
			{0,0,0,1},
		});
		
		return translationDeVecteur;
	}

}

package math;

/**
 * 
 * Cette classe permet d'obtenir des matrices servant aux transformation géométrique. <br>
 * Elle permet plus précisément de réaliser un mouvement voulu sur un objet donné.<br>
 * Les mouvements sont au nombres de quatre : <br>
 *  - Un cadrage <br>
 *  - Une rotation <br>
 *  - Une homothetie <br>
 *  - Une translation <br>
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 9 nov. 2021
 * 
 */
public abstract class OutilMatriciel {
	
	/**
	 * Donne la matrice de cadrage de taille 4x4.<br>
	 * C'est une transformation géométrique permettant la modification ou d'altération de la taille d'un objet.
	 */
	public static Matrice getCadrage(double k1, double k2, double k3) {
		
		return new Matrice(new double[][] {
			{k1,0,0,0},
			{0,k2,0,0},
			{0,0,k3,0},
			{0,0,0,1},
		});
	}
	
	/**
	 * Donne la matrice de rotation de taille 4x4.<br>
	 * C'est une transformation géométrique permettant la rotation (autour de X) d’un objet par rapport à un angle.
	 * @param delta angle de rotation
	 */
	public static Matrice getRotation(double delta) {
		
		return new Matrice(new double[][] {
			{1,0,0,0},
			{0,Math.cos(delta),-Math.sin(delta),0},
			{0,Math.sin(delta),Math.cos(delta),0},
			{0,0,0,1},
		});
	}
	
	/**
	 * Donne la matrice homothetie de taille 4x4. <br>
	 * C'est une transformation géométrique qui correspond à l'idée de modification de rétrécissement ou d'aggrandissement en fonction de k.<br>
	 * Et ceci tout en gardant des rapports identiques.
	 * @param k rapport
	 */
	public static Matrice getHomothetie(double k) {
		
		return new Matrice(new double[][] {
			{k,0,0,0},
			{0,k,0,0},
			{0,0,k,0},
			{0,0,0,1},
		});
	}
	
	/**
	 * Donne la matrice de translation de taille 4x4.<br>
	 * C'est une transformation géométrique qui correspond à l'idée de « glissement » d'un objet. <br>
	 * Et ceci en fonction d'un vecteur (x,y,z).
	 * @param x
	 * @param y
	 * @param z
	 */
	public static Matrice getTranslation(double x, double y, double z) {
		
		return new Matrice(new double[][] {
			{1,0,0,x},
			{0,1,0,y},
			{0,0,1,z},
			{0,0,0,1},
		});
	}

}

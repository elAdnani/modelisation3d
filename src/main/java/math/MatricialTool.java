package math;

/**
 * 
 * Cette classe permet d'obtenir des matrices servant aucooX transformation
 * géométrique. <br>
 * Elle permet plus précisément de réaliser un mouvement voulu sur un objet
 * donné.<br>
 * Les mouvements sont au nombres de quatre : <br>
 * - Un cadrage <br>
 * - Une rotation <br>
 * - Une homothetie <br>
 * - Une translation <br>
 *
 * @author <a href="mailto:adnan.Kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 * @date 9 nov. 2021
 * 
 */
public class MatricialTool {

	private MatricialTool() {

	}

	/**
	 * Donne la matrice de cadrage de taille 4X4.<br>
	 * C'est une transformation géométrique permettant la modification ou
	 * d'altération de la taille d'un objet.
	 */
	public static Matrix getCadrage(final double coefK1, final double coefK2, final double coefK3) {

		return new Matrix(
				new double[][] { { coefK1, 0, 0, 0 }, { 0, coefK2, 0, 0 }, { 0, 0, coefK3, 0 }, { 0, 0, 0, 1 }, });
	}

	/**
	 * Donne la matrice de rotation de taille 4X4.<br>
	 * C'est une transformation géométrique permettant la rotation (autour de x)
	 * d’un objet par rapport à un angle.
	 * 
	 * @param delta angle de rotation
	 */
	public static Matrix getXRotation(final double delta) {
		double cosDelta = Math.cos(delta);
		double sinDelta = Math.sin(delta);
		return new Matrix(new double[][] { { 1, 0, 0, 0 }, { 0, cosDelta, -sinDelta, 0 }, { 0, sinDelta, cosDelta, 0 },
				{ 0, 0, 0, 1 }, });
	}

	/**
	 * Donne la matrice de rotation de taille 4X4.<br>
	 * C'est une transformation géométrique permettant la rotation (autour de y)
	 * d’un objet par rapport à un angle.
	 * 
	 * @param delta angle de rotation
	 */
	public static Matrix getYRotation(final double delta) {
		double cosDelta = Math.cos(delta);
		double sinDelta = Math.sin(delta);
		return new Matrix(new double[][] { { cosDelta, 0, -sinDelta, 0 }, { 0, 1, 0, 0 }, { sinDelta, 0, cosDelta, 0 },
				{ 0, 0, 0, 1 }, });
	}

	/**
	 * Donne la matrice de rotation de taille 4X4.<br>
	 * C'est une transformation géométrique permettant la rotation (autour de y)
	 * d’un objet par rapport à un angle.
	 * 
	 * @param delta angle de rotation
	 */
	public static Matrix getZRotation(final double delta) {
		double cosDelta = Math.cos(delta);
		double sinDelta = Math.sin(delta);
		return new Matrix(new double[][] { { cosDelta, -sinDelta, 0, 0 }, { sinDelta, cosDelta, 0, 0 }, { 0, 0, 1, 0 },
				{ 0, 0, 0, 1 }, });
	}

	/**
	 * Donne la matrice homothetie de taille 4cooX4. <br>
	 * C'est une transformation géométrique qui correspond à l'idée de modification
	 * de rétrécissement ou d'aggrandissement en fonction de k.<br>
	 * Et ceci tout en gardant des rapports identiques.
	 * 
	 * @param coefK rapport
	 */
	public static Matrix getHomothety(final double coefK) {

		return new Matrix(
				new double[][] { { coefK, 0, 0, 0 }, { 0, coefK, 0, 0 }, { 0, 0, coefK, 0 }, { 0, 0, 0, 1 }, });
	}

	/**
	 * Donne la matrice de translation de taille 4X4.<br>
	 * C'est une transformation géométrique qui correspond à l'idée de « glissement
	 * » d'un objet. <br>
	 * Et ceci en fonction d'un vecteur (x,y,z).
	 * 
	 * @param cooX
	 * @param cooY
	 * @param cooZ
	 */
	public static Matrix getTranslation(final double cooX, final double cooY, final double cooZ) {

		return new Matrix(new double[][] { { 1, 0, 0, cooX }, { 0, 1, 0, cooY }, { 0, 0, 1, cooZ }, { 0, 0, 0, 1 }, });
	}

}

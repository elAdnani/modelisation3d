package math;

import java.util.Arrays;

/**
 * <p>
 * Gestion d'une matrice contenant des nombres.
 * </p>
 * <p>
 * Cette classe permet de créer et utiliser une matrice comme le permettent les
 * mathématiques.<br>
 * <p>
 * Les opérations basiques telles que l'addition, la soustraction et la
 * multiplication sont possibles.
 * </p>
 * 
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 * @date 11 nov. 2021
 */
public class Matrice {

	/* ATTRIBUTS ______________________________ */

	private int nbLine;
	private int nbColumn;

	/**
	 * <p>
	 * La matrice.
	 * </p>
	 * <p>
	 * Elle est représenté sous forme de tableau à deux entrées
	 * </p>
	 * <p>
	 * La première entrée est considérée comme contenant les lignes alors que la
	 * seconde contient les colonnes
	 * </p>
	 */
	private double[][] matrixCoordinates;

	/* CONSTRUCTEURS ______________________________ */
	/**
	 * Création d'une matrice à partir du tableau de double qui lui est passé en
	 * paramètre.
	 * 
	 * @param matrix Un tableau de nombres contenant les valeurs de la future
	 *               matrice.
	 */
	public Matrice(double[][] matrix) {
		this.matrixCoordinates = matrix;
		if (!this.isSquare()) {
			this.matrixCoordinates = new double[matrix.length][matrix.length];
		} else {
			this.nbColumn = matrix[0].length;
			this.nbLine = matrix.length;
		}
	}

	/**
	 * Création d'une matrice nulle dont le nombre de lignes et le nombre de
	 * colonnes est passé en paramètres.
	 * 
	 * @param nbLignes   Le nombre de lignes de la future matrice
	 * @param nbColonnes Le nombre de colonnes de la future matrice
	 */
	public Matrice(int nbLignes, int nbColonnes) {
		// On évite les valeurs négatives ou nulles !
		if (nbLignes <= 0) {
			nbLignes = 1;
		}
		if (nbColonnes <= 0) {
			nbColonnes = 1;
		}
		this.nbLine = nbLignes;
		this.nbColumn = nbColonnes;
		this.matrixCoordinates = new double[nbLignes][nbColonnes];
	}

	/**
	 * Création d'une matrice nulle carrée de taille n supérieur à 0.
	 * 
	 * @param nbLine Le nombre de lignes et de colonnes de la future matrice.
	 */
	public Matrice(int nbLine) {
		this(nbLine, nbLine);
	}

	/**
	 * Création d'une matrice nulle carrée de taille n supérieur à 0.
	 * 
	 * @param matrix Le nombre de lignes et de colonnes de la future matrice.
	 */
	public Matrice(Matrice matrix) {
		this(matrix.matrixCoordinates);
	}

	/* GETTER ______________________________ */
	/**
	 * Récupère le tableau contenant les valeurs de la matrice.
	 * 
	 * @return Un tableau à deux entrées contenant les valeurs de la matrice
	 */
	public double[][] getMatrix() {
		return matrixCoordinates;
	}

	/**
	 * Nombre de lignes.
	 * 
	 * @return Un entier contenant le nombre de lignes
	 */
	public int getNbLines() {
		return this.nbLine;
	}

	/**
	 * Nombre de colonnes.
	 * 
	 * @return Un entier contenant le nombre de colonnes
	 */
	public int getNbColumn() {
		return this.nbColumn;
	}

	/* TOSTRING ______________________________ */
	/**
	 * Affichage du contenu de la matrice.
	 * 
	 * @return une chaîne de caractères représentant la matrice souhaitée.
	 */
	@Override
	public String toString() {
		// Initialisation
		StringBuilder result = new StringBuilder(); // variable à laquelle ajouter l'affichage
		int[] columnDimension = new int[this.getNbColumn()]; // dimensions nécessaires pour chaque colonne

		// pour paramétrer chaque dimension,
		for (int column = 0; column < columnDimension.length; column++) {
			// on explore chaque ligne
			for (int line = 0; line < this.getNbLines(); line++) {
				// on regarde la taille d'affichage du contenu de la cellule
				int contentSize = String.valueOf(this.read(line, column)).length();

				// si c'est la première cellule ou la plus grande,
				if (contentSize > columnDimension[column]) {
					// alors on la conserve pour la suite
					columnDimension[column] = contentSize;
				}
			}
		}
		// pour chaque ligne et chaque colonne,
		for (int line = 0; line < this.getNbLines(); line++) {
			for (int column = 0; column < this.getNbColumn(); column++) {
				// on place devant chaque affichage le nombre d'espaces nécessaires pour aligner
				// verticalement sur la droite.
				int contentSize = String.valueOf(this.read(line, column)).length();

				for (int nbEspace = 0; nbEspace < columnDimension[column] - contentSize; nbEspace++) {
					result.append(' ');
				}
				result.append(this.read(line, column));
			}
			result.append("\n\n");
		}

		// on retourne le résultat attendu
		return result.toString();
	}

	/* MÉTHODES ______________________________ */
	/**
	 * Vérifie si la case aux coordonnées données existe
	 * 
	 * @param line   La ligne visée
	 * @param column La colonne visée
	 * 
	 * @return - true si la case appartient au tableau<br>
	 *         - false sinon
	 */
	public boolean canRead(int line, int column) {
		return line >= 0 && line < this.getNbLines() && column >= 0 && column < this.getNbColumn();
	}

	/**
	 * Lis le contenu d'une case aux coordonnées données à partir des indices
	 * 
	 * @param line   Ligne de la case à lire
	 * @param column Colonne de la case à lire
	 * 
	 * @return Ce que contient la case
	 */
	public double read(int line, int column) {
		double res = Double.NaN;
		if (canRead(line, column)) {
			res = this.matrixCoordinates[line][column];
		}
		return res;
	}

	/**
	 * Écris une valeur aux coordonnées précisées.
	 * 
	 * @param line   La ligne de la case
	 * @param column La colonne de la case
	 * @param value  La valeur à attribuer
	 * 
	 * @return - true si la valeur a été modifiée<br>
	 *         - false s'il y a une erreur dans les coordonnées
	 */
	public boolean write(int line, int column, double value) {
		boolean res = false;
		if (!Double.isNaN(value) && this.canRead(line, column)) {
			this.matrixCoordinates[line][column] = value;
			res = true;
		}
		return res;
	}

	/**
	 * Addition de deux matrices.
	 * 
	 * @param plus La matrice à ajouter.
	 * 
	 * @return Le résultat de l'addition.
	 */
	public Matrice addition(Matrice plus) {
		Matrice result = null;

		if (this.isSameFormat(plus)) {
			result = new Matrice(this);

			// alors, pour chaque case, ajouter à notre matrice la valeur de la deuxième.
			for (int ligne = 0; ligne < this.getNbLines(); ligne++) {
				for (int colonne = 0; colonne < this.getNbColumn(); colonne++) {
					result.write(ligne, colonne, this.read(ligne, colonne) + plus.read(ligne, colonne));
				}
			}
		}

		return result;
	}

	/**
	 * Soustraction de deux matrices.
	 * 
	 * @param moins La matrice à enlever.
	 * 
	 * @return Le résultat de la soustraction.
	 */
	public Matrice substraction(Matrice moins) {
		Matrice result = null;
		// additionne l'opposé du deuxième terme au premier terme, ce qui revient à
		// soustraire au premier terme le second.
		if (this.isSameFormat(moins)) {
			result = new Matrice(this);

			// alors, pour chaque case, ajouter à notre matrice la valeur de la deuxième.
			for (int line = 0; line < this.getNbLines(); line++) {
				for (int column = 0; column < this.getNbColumn(); column++) {
					result.write(line, column, this.read(line, column) - moins.read(line, column));
				}
			}
		}

		return result;
	}

	/**
	 * Multiplication d'une matrice par un réel.
	 * 
	 * @param facteur Le facteur du produit
	 * 
	 * @return Le résultat de la multiplication
	 */
	public Matrice multiplication(double facteur) {
		Matrice result = new Matrice(this);
		// on multiplie chaque case par l'entier passé en paramètre
		for (int line = 0; line < this.getNbLines(); line++) {
			for (int column = 0; column < this.getNbColumn(); column++) {
				result.write(line, column, this.read(line, column) * facteur);
			}
		}
		return result;
	}

	/**
	 * Multiplication de deux matrices. Une multiplication de la matrice courante
	 * avec celle en paramètre
	 * 
	 * @param facteur La matrice servant à la multiplication.
	 * 
	 * @return Le résultat de la multiplication.
	 */
	public Matrice multiplication(Matrice matriceMultiplic) {
		Matrice resultat = null;

		// si la matrice passée en paramètres peut être multipliée avec notre matrice
		if (this.getNbColumn() == matriceMultiplic.getNbLines()) {
			resultat = new Matrice(this.getNbLines(), matriceMultiplic.getNbColumn());

			// pour chaque case de la matrice résultat
			for (int ligneResultat = 0; ligneResultat < resultat.getNbLines(); ligneResultat++) {
				for (int colonneResultat = 0; colonneResultat < resultat.getNbColumn(); colonneResultat++) {

					// on effectue la somme des multiplications de chaque termes "liés" que l'on
					// stock dans le résultat
					for (int n = 0; n < this.getNbColumn(); n++) {
						resultat.write(ligneResultat, colonneResultat, resultat.read(ligneResultat, colonneResultat)
								+ this.read(ligneResultat, n) * matriceMultiplic.read(n, colonneResultat));
					}
				}
			}
		}
		return resultat;
	}

	/**
	 * Vérifie si la matrice est nulle.
	 * 
	 * @return - true si la matrice donnée est nulle<br>
	 *         - false si elle ne l'est pas
	 */
	public boolean isNull() {
		boolean verifie = true;

		int ligne = 0;
		while (verifie && ++ligne < this.getNbLines()) {
			int colonne = 0;
			while (verifie && colonne < this.getNbColumn()) {
				verifie = this.read(ligne, colonne) == 0;
				colonne++;
			}
		}

		return verifie;
	}

	/**
	 * Vérifie si la matrice est carrée.
	 * 
	 * @return - true si la matrice est carrée<br>
	 *         - false elle ne l'est pas
	 */
	public boolean isSquare() {
		return this.getNbLines() == this.getNbColumn();
	}

	/**
	 * Compare le format des matrices
	 * 
	 * @param matrix
	 * @return - true si la matrice a le même nombre de ligne et de colonne <br>
	 *         - false sinon
	 */
	public boolean isSameFormat(Matrice matrix) {
		return matrix.getNbLines() == this.nbLine && matrix.getNbColumn() == this.nbColumn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getNbColumn();
		result = prime * result + getNbLines();
		result = prime * result + Arrays.deepHashCode(matrixCoordinates);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Matrice other = (Matrice) obj;
		if (this.getNbColumn() != other.getNbColumn()) {
			return false;
		}
		if (this.getNbLines() != other.getNbLines()) {
			return false;
		}
		for (int i = 0; i < this.getNbLines(); i++) {
			for (int j = 0; j < this.getNbColumn(); j++) {
				if (matrixCoordinates[i][j] != other.matrixCoordinates[i][j]) {

					return false;
				}
			}
		}
		return true;
	}

}

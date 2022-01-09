package ply;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import math.Face;
import modele.geometrique.FigureFabrique;
import modele.geometrique.Point;
import ply.exceptions.FormatPlyException;

/**
 * 
 * Cette classe sert à la récupération des fichiers PLY, elle permet ainsi
 * d'obtenir les données tri-dimensionnelles provenant de scanners 3D. <br>
 * 
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 * @date 28 sept. 2021
 */
public class RecuperationPly {
	
	private static int nbVertex;
	private static int nbFace;
	private static List<Point> points;
	private static List<Face> faces;

	private RecuperationPly() {
	}

	/**
	 * Réinitialise les données
	 */
	private static void resetDonnnee() {
		nbVertex = -1;
		nbFace = -1;
	}

	/**
	 * Attribue les informations que contient un fichier ply aux attribues de la
	 * classe
	 * 
	 * @param fichier
	 * @throws FormatPlyException    Si le fichier n'est pas du format PLY
	 * @throws FileNotFoundException Si le fichier n'existe pas
	 */
	public static void recuperationFichier(String fichier) throws FormatPlyException, FileNotFoundException {
		BufferedReader reader = verifierFichier(fichier);
		resetDonnnee();
		try {
			readHeadFile(reader);
			recuperationPoints(reader);
			recuperationFaces(points, reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Vérifie le format du fichier voulant être lu
	 * 
	 * @param Nom du fichier
	 * @return Un flux d'entrée permettant la lecture du fichier
	 * @throws FormatPlyException    Si le fichier n'est pas du format PLY
	 * @throws FileNotFoundException Si le fichier n'existe pas
	 */
	private static BufferedReader verifierFichier(String fichier) throws FormatPlyException, FileNotFoundException {
		checkFormat(fichier);
		BufferedReader reader = new BufferedReader(new FileReader(fichier));
		resetDonnnee();
		return reader;

	}

	/**
	 * Récupère la liste des points du fichier
	 * 
	 * @param reader Lecteur du fichier
	 * @throws IOException une opération d'entrée du fichier échoue
	 */
	protected static void recuperationPoints(BufferedReader reader) throws IOException {
		FigureFabrique fabriquePoint = FigureFabrique.getInstance();
		List<Point> res = new ArrayList<>();

		String line;
		String[] tab;

		int idx = 0;
		while (idx < nbVertex) {
			line = reader.readLine();

			if (line != null && !line.isEmpty()) {
				tab = line.split(" ");
				res.add(fabriquePoint.point(Double.valueOf(tab[0]), Double.valueOf(tab[1]), Double.valueOf(tab[2])));
			} else {
				idx--;
			}

			idx++;
		}
		points = res;

	}

	/**
	 * Recupere la liste des faces dans un fichier PLY donné en paramètre
	 * 
	 * @param points Liste des points du fichier
	 * @param reader Lecteur du fichier
	 * @throws IOException une opération d'entrée du fichier échoue
	 */
	protected static void recuperationFaces(List<Point> points, BufferedReader reader) throws IOException {

		List<Face> res = new ArrayList<>();

		String line;
		String[] tab;

		Face pointsDeLaFace;
		for (int idx = 0; idx < nbFace; idx++) {
			line = reader.readLine();

			tab = line.split(" ");

			pointsDeLaFace = new Face();
			for (int jdx = 1; jdx < tab.length; jdx++) {
				pointsDeLaFace.add(points.get(Integer.valueOf(tab[jdx])));
			}

			res.add(pointsDeLaFace);
		}

		reader.close();

		faces = res;
	}

	/**
	 * Lis la l'entête du fichier. <br>
	 * Permet à partir de cela d'obtenir le nombre de vertex et le nombre de face
	 * 
	 * @param raf - RandomAccessFile Le flux d'entrée représentant le fichier à lire
	 * @throws FormatPlyException Si le fichier n'est pas du format PLY
	 * @throws IOException        une opération d'entrée du fichier échoue
	 */
	protected static void readHeadFile(BufferedReader raf) throws FormatPlyException, IOException {

		String line = raf.readLine();

		if (!line.toLowerCase().contains("ply")) {
			throw new FormatPlyException("Fichier invalide. Le fichier n'est pas au format ply.");
		}

		while (!line.contains("end_header")) {

			if (line.contains("element vertex")) {
				String[] lineV = line.split(" ");
				nbVertex = Integer.parseInt(lineV[lineV.length - 1]);
			}
			if (line.contains("element face")) {
				String[] lineF = line.split(" ");
				nbFace = Integer.parseInt(lineF[lineF.length - 1]);
			}

			line = raf.readLine();
		}
		if (nbVertex == -1 || nbFace == -1) {
			throw new FormatPlyException("Le fichier ne respecte pas la convention PLY");
		}

		// on se replace tout au début du fichier
	}

	/**
	 * Verifie si le fichier passé en paramètre est un fichier ply
	 *
	 * @param file - Chemin du fichier
	 * @throws FormatPlyException
	 */
	public static void checkFormat(String file) throws FormatPlyException {
		if (file == null || !file.endsWith(".ply")) {
			throw new FormatPlyException("(Fichier \"" + file + "\" invalide. Le fichier n'est pas au format ply.)");
		}
	}

	/**
	 * Récupère le nombre de point que contient un fichier en parcourant son entête
	 * 
	 * @param fichier
	 * @return le nombre de point
	 * @throws FormatPlyException
	 * @throws IOException
	 */
	public static int getNbVertices(String fichier) throws FormatPlyException, IOException {
		BufferedReader reader = verifierFichier(fichier);
		readHeadFile(reader);

		return nbVertex;
	}

	/**
	 * Récupérer le nombre de face que contient un fichier en parcourant son entête
	 * 
	 * @param fichier
	 * @return le nombre de face
	 * @throws FormatPlyException
	 * @throws IOException
	 */
	public static int getNbFaces(String fichier) throws FormatPlyException, IOException {
		BufferedReader reader = verifierFichier(fichier);
		readHeadFile(reader);

		return nbFace;
	}

	/**
	 * 
	 * @return the points
	 */
	public static List<Point> getPoints() {
		return points;
	}

	/**
	 * 
	 * @return the faces
	 */
	public static List<Face> getFaces() {
		return faces;
	}

}

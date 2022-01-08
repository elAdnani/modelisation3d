package modele;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ply.exceptions.FormatPlyException;

/**
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
	 * Réinitialise les données du fichier
	 */
	private static void resetDonnnee() {
		nbVertex=-1;
		nbFace=-1;
	}
	/**
	 * Attribue les informations que contient un fichier ply aux attribues de la classe
	 * @param fichier
	 * @throws FormatPlyException	Si le fichier n'est pas du format PLY
	 * @throws FileNotFoundException Si le fichier n'existe pas
	 */
	public static void recuperationFichier(String fichier) throws FormatPlyException, FileNotFoundException {
		BufferedReader reader = verificationDuFichier(fichier);
		resetDonnnee();
		try {
			lectureEnteteDuFichier(reader);
			recuperationPoints(reader);
			recuperationFaces(points,reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	/**
	 * Vérifie le format du fichier voulant être lu
	 * @param Nom du fichier
	 * @return Un flux d'entrée permettant la lecture du fichier
	 * @throws FormatPlyException	Si le fichier n'est pas du format PLY
	 * @throws FileNotFoundException Si le fichier n'existe pas
	 */
	  private static BufferedReader verificationDuFichier(String fichier) throws FormatPlyException, FileNotFoundException{
			checkFormat(fichier);
			return new BufferedReader(new FileReader(fichier));
		  
	  }

	  /**
	   * Récupère la liste des points du fichier
	   * @param reader Lecteur du fichier
	   * @throws IOException une opération d'entrée du fichier échoue
	   */
	protected static void recuperationPoints(BufferedReader reader) throws IOException {

		List<Point> res = new ArrayList<>();

			String ligne;
			String[] tab;

			int i = 0;
			while (i < nbVertex) {
				ligne = reader.readLine();

				if (ligne != null && !ligne.isEmpty()) {
					
					tab = ligne.split(" ");
					res.add(new Point(Double.valueOf(tab[0]), Double.valueOf(tab[1]), Double.valueOf(tab[2])));
				} else {
					i--;
				}

				i++;
			}
			points = res;

	}

	/**
	 * Recupere la liste des faces dans un fichier PLY donné en paramètre
	 * @param points Liste des points du fichier
	 * @param reader Lecteur du fichier
	 * @throws IOException une opération d'entrée du fichier échoue
	 */
	protected static void recuperationFaces(List<Point> points,BufferedReader reader) throws IOException {

		List<Face> res = null;
			res = new ArrayList<>();

			String ligne;
			String[] tab;

			for (int j = 0; j < nbFace; j++) {
				ligne = reader.readLine();

				tab = ligne.split(" ");
				
				List<Point> pointsDeLaFace = new ArrayList<>();
				for (int i = 1; i < tab.length; i++) {
					pointsDeLaFace.add(points.get(Integer.valueOf(tab[i])));
				}
				Face face = new Face(pointsDeLaFace);
				res.add(face);
			}

			reader.close();

		faces = res;
	}

	/**
	 * Lis la l'entête du fichier. <br>
	 * Permet à partir de cela d'obtenir le nombre de vertex et le nombre de face
	 * @param raf - RandomAccessFile Le flux d'entrée représentant le fichier à lire
	 * @throws FormatPlyException	Si le fichier n'est pas du format PLY
	 * @throws IOException une opération d'entrée du fichier échoue
	 */
	protected static void lectureEnteteDuFichier(BufferedReader raf) throws FormatPlyException, IOException {

		String ligne;
			ligne = raf.readLine();

			if (!ligne.toLowerCase().contains("ply")) { 
				throw new FormatPlyException("Fichier invalide. Le fichier n'est pas au format ply.");
			}
			
			while(!ligne.contains("end_header")) {
				
				if(ligne.contains("element vertex")) {
					String[] lineV = ligne.split(" ");
					nbVertex = Integer.parseInt(lineV[lineV.length - 1]);
				}
				if(ligne.contains("element face")) {
					String[] lineF = ligne.split(" ");
					nbFace = Integer.parseInt(lineF[lineF.length - 1]);
				}
				
				
				ligne = raf.readLine();
			}
			if(nbVertex==-1 || nbFace==-1) {
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
		if (file!=null && !file.endsWith(".ply")) {
			throw new FormatPlyException(file + " (Fichier invalide. Le fichier n'est pas au format ply.)");
		} 
	}
	
	/**
	 * Récupère le nombre de point que contient un fichier en parcourant son entête
	 * @param fichier
	 * @return	le nombre de point
	 * @throws FormatPlyException
	 * @throws IOException
	 */
	public static int getNBVertices(String fichier) throws FormatPlyException, IOException {
		BufferedReader reader = verificationDuFichier(fichier);
		lectureEnteteDuFichier(reader);
		
		return nbVertex;
	}

	/**
	 * Récupérer le nombre de face que contient un fichier en parcourant son entête
	 * @param fichier
	 * @return le nombre de face
	 * @throws FormatPlyException
	 * @throws IOException
	 */
	public static int getNBFaces(String fichier) throws FormatPlyException, IOException {
		BufferedReader reader = verificationDuFichier(fichier);
		lectureEnteteDuFichier(reader);
		
		return nbFace;
	}

	/**
	 * Récupére les points du dernier fichier lu
	 * @return les points
	 */
	public static List<Point> getPoints() {
		return points;
	}

	/**
	 * Récupére les faces du dernier fichier lu
	 * @return les faces
	 */
	public static List<Face> getFaces() {
		return faces;
	}

}

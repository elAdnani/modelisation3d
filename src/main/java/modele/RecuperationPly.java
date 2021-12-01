package modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	public static void main(String[] args) throws FormatPlyException {
		final String PATH = System.getProperty("user.dir") + File.separator + "exemples" + File.separator;
		try {
			RecuperationPly.recuperationFichier(PATH+"vache.ply");
		} catch (FileNotFoundException | FormatPlyException e) {}
		System.out.println("nbVertex :" +nbVertex);
		System.out.println(points.size());
		for(Face p : faces) {
			System.out.println(p.toString()+"\n");
		}
//		System.out.println("nbFace : "+nbFace);
//		System.out.println(faces.size());
		
	}

	
	private static void resetDonnnee() {
		nbVertex=-1;
		nbFace=-1;
	}
	/**
	 * 
	 * @param fichier
	 * @throws FormatPlyException
	 * @throws FileNotFoundException
	 */
	public static void recuperationFichier(String fichier) throws FormatPlyException, FileNotFoundException {
		BufferedReader reader = verificationDuFichier(fichier);
		try {
			lectureEnteteDuFichier(reader);
			recuperationPoints(fichier,reader);
			recuperationFaces(fichier, points,reader );
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	/**
	 * 
	 * @param fichier
	 * @return
	 * @throws FormatPlyException
	 * @throws FileNotFoundException
	 */
	  private static BufferedReader verificationDuFichier(String fichier) throws FormatPlyException, FileNotFoundException{
			checkFormat(fichier);
			BufferedReader reader = new BufferedReader(new FileReader(fichier));
			resetDonnnee();
			return reader;
		  
	  }

	/**
	 * 
	 * @param fichier
	 * @param reader
	 * @throws FormatPlyException
	 * @throws IOException
	 */
	protected static void recuperationPoints(String fichier, BufferedReader reader) throws FormatPlyException,IOException {

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
	 * 
	 * @param fichier  - Chemin vers le fichier
	 * @param Point3Ds - {@link List} de {@link Point}
	 * @return {@link List} de {@link Face}
	 * @throws IOException 
	 */
	protected static void recuperationFaces(String fichier, List<Point> points,BufferedReader reader) throws IOException {

		List<Face> res = null;
			res = new ArrayList<>();

			String ligne;
			String[] tab;

			for (int j = 0; j < nbFace; j++) {
				ligne = reader.readLine();

				tab = ligne.split(" ");
				Face ensembleDePoint = new Face();
				for (int i = 1; i < tab.length; i++) {
					ensembleDePoint.add(points.get(Integer.valueOf(tab[i])));
				}
				res.add(ensembleDePoint);
			}

			reader.close();

		faces = res;
	}

	/**
	 * Lis le nombre de vertex et le nombre de face, puis se place à la fin du
	 * header
	 * 
	 * @param raf - RandomAccessFile
	 * @throws FormatPlyException
	 * @throws IOException
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
		if (!file.endsWith(".ply")) {
			throw new FormatPlyException(file + " (Fichier invalide. Le fichier n'est pas au format ply.)");
		} // TODO REALISER UNE CLASSE ERREUR
	}
	public static int getNBVertices(String fichier) throws FormatPlyException, IOException {
		BufferedReader reader = verificationDuFichier(fichier);
		lectureEnteteDuFichier(reader);
		
		return nbVertex;
	}

	public static int getNBFaces(String fichier) throws FormatPlyException, IOException {
		BufferedReader reader = verificationDuFichier(fichier);
		lectureEnteteDuFichier(reader);
		
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

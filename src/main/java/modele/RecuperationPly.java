package modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * Cette classe sert à la récupération des fichiers PLY, elle permet ainsi d'obtenir les données tri-dimensionnelles provenant de scanners 3D. <br>
 * 
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 28 sept. 2021
 */
public abstract class RecuperationPly {

	private static int nbVertex;
	private static int nbFace;

	private RecuperationPly() {

	}

	/**
	 * Recupere la liste des points dans un fichier PLY donné en parametre
	 * 
	 * @param fichier - Chemin vers le fichier
	 * @return {@link List} de {@link Point}
	 * @throws Exception
	 */
	public static List<Point> recuperationPoints(String fichier) throws Exception {
		checkFormat(fichier);

		List<Point> res = new ArrayList<>();

		FileReader fichierPly = null;

		try
		{

			fichierPly = new FileReader(fichier);

			String ligne;

			BufferedReader reader = new BufferedReader(fichierPly);
			placerApresLaTeteDuFichier(reader);
			String tab[];
			int i = 0;
			while (i < nbVertex)
			{
				ligne = reader.readLine();

				if (ligne != null && !ligne.isEmpty())
				{

					tab = ligne.split(" ");

					res.add(new Point(Double.valueOf(tab[0]), Double.valueOf(tab[1]), Double.valueOf(tab[2])));
				} else
				{
					i--;
				}

				i++;

			}

			// reader.mark(i);
			// reader.reset();
			reader.close();

		} catch (IOException o)
		{
			o.printStackTrace();
		}

		return res;
	}

	public static final int NBLIGNEPOINT3D = 3;
	
	
	/**
	 * Recupere la liste des points dans un fichier PLY donné en parametre directement dans une matrice
	 * 
	 * @param fichier - Chemin vers le fichier
	 * @return {@link List} de {@link Point}
	 * @throws Exception Le fichier n'est pas alors en format PLY ou sa composition n'est 
	 */
	public static Matrice recuperationMatrice(String fichier) throws Exception {
		checkFormat(fichier);

		double[][] donnees = null;

		FileReader fichierPly = null;

		try
		{

			fichierPly = new FileReader(fichier);
			String ligneLectureFichier = "";

			BufferedReader reader = new BufferedReader(fichierPly);
			placerApresLaTeteDuFichier(reader);
			String[] tab;

			donnees = new double[3][nbVertex];

			int colonne = 0;
			while (colonne < nbVertex)
			{
				ligneLectureFichier = reader.readLine();

				if (!ligneLectureFichier.isEmpty())
				{
					tab = ligneLectureFichier.split(" ");

					for (int ligne = 0; ligne < 3; ligne++)
					{
						donnees[ligne][colonne] = Double.valueOf(tab[ligne]);
					}
						donnees[3][colonne]=0;

				} else
					colonne--;

				colonne++;
			}

			// reader.mark(colonne);
			// reader.read();
			reader.close();
		} catch (IOException o)
		{

			o.printStackTrace();
		}

		return new Matrice(donnees);
	}

	/**
	 * Recupere la liste des faces dans un fichier PLY donné en paramètre
	 * 
	 * @param fichier  - Chemin vers le fichier
	 * @param Point3Ds - {@link List} de {@link Point}
	 * @return {@link List} de {@link Face}
	 * @throws Exception
	 */
	public static List<Face> recuperationFaces(String fichier, List<Point> points) {
		
		List<Face> res= null;
		FileReader fichierPly;
		try
		{
			checkFormat(fichier);
			res=new ArrayList<>();

			fichierPly = new FileReader(fichier);

			String ligne;

			BufferedReader reader = new BufferedReader(fichierPly);
			placerApresLaTeteDuFichier(reader);

			String[] tab;

			for (int i = 1; i <= nbVertex; i++)
			{
				ligne = reader.readLine();
				if (ligne.isEmpty())
					i--;
			}

			for (int j = 0; j < nbFace; j++)
			{
				ligne = reader.readLine();

				tab = ligne.split(" ");
				Face ensembleDePoint = new Face();
				for (int i = 1; i < tab.length; i++)
				{
					ensembleDePoint.add(points.get(Integer.valueOf(tab[i])));
				}
				res.add(ensembleDePoint);
			}

			// reader.mark(nbLigneLue+nbVertex+nbFace);
			// reader.reset();
			fichierPly.close();
			reader.close();

		} catch (Exception o)
		{

			o.printStackTrace();
		} 
		
		
		return res;
	}

	/**
	 *  Recupère les faces les indices des points de la matrice d'un fichier PLY.
	 * @param fichier
	 * @return {@link FaceMatrice}
	 * @throws Exception
	 */
	public static List<FaceMatrice> recuperationFacesMatrice(String fichier) throws Exception {
		checkFormat(fichier);

		List<FaceMatrice> res = new ArrayList<>();

		FileReader fichierPly = null;

		try
		{

			fichierPly = new FileReader(fichier);

			String ligne;

			BufferedReader reader = new BufferedReader(fichierPly);
			placerApresLaTeteDuFichier(reader);

			String[] tab;

			for (int i = 1; i <= nbVertex; i++)
			{
				ligne = reader.readLine();
				if (ligne.isEmpty())
					i--;
			}

			for (int j = 0; j < nbFace; j++)
			{
				ligne = reader.readLine();
				tab = ligne.split(" ");

				FaceMatrice indiceDesEnsemblesDePoint = new FaceMatrice();
				for (int i = 1; i < tab.length; i++)
				{
					indiceDesEnsemblesDePoint.add(Integer.valueOf(tab[i]));
				}
				res.add(indiceDesEnsemblesDePoint);
			}
			fichierPly.close();
			// reader.mark(nbLigneLue+nbVertex+nbFace);
			// reader.reset();
			reader.close();
		} catch (IOException o)
		{

			o.printStackTrace();
		}

		return res;
	}

	/**
	 * Recherche le nombre d'occurence d'un caractere dans un String donné
	 * 
	 * @param mes - le {@link String} a parcourir
	 * @param c   - le caractere à chercher
	 * @return Le nombre d'occurence du caractere dans le String
	 */
	public static int nombreOccurence(String mes, char c) {
		int cpt = 0;
		for (int i = 0; i < mes.length(); i++)
		{
			if (mes.charAt(i) == c)
			{
				cpt++;
			}
		}
		return cpt;
	}

	/**
	 * Lis le nombre de vertex et le nombre de face, puis se place à la fin du
	 * header
	 * 
	 * @param raf - RandomAccessFile
	 * @throws Exception Le fichier ne convient pas comme fichier ply
	 */
	private static int placerApresLaTeteDuFichier(BufferedReader raf) {
		int cpt = 0;
		try
		{

			String ligne;
			ligne = raf.readLine();
			if (!ligne.toLowerCase().contains("ply"))
			{ // TODO ajouter une classe exception
				throw new Exception(" (Fichier invalide. Le fichier n'est pas au format ply.)");
			}

			while (!ligne.contains("element vertex"))
			{
				ligne = raf.readLine();
				cpt++;
			}
			String[] lineV = ligne.split(" ");
			nbVertex = Integer.parseInt(lineV[lineV.length - 1]);

			while (!ligne.contains("element face"))
			{
				ligne = raf.readLine();
				cpt++;
			}
			String[] lineF = ligne.split(" ");
			nbFace = Integer.parseInt(lineF[lineF.length - 1]);

			while (!ligne.contains("end_header"))
			{
				ligne = raf.readLine();
				cpt++;

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// on se replace tout au début du fichier
		return cpt + nbVertex + nbFace;
	}

	/**
	 * Verifie si le fichier passé en paramètre est un fichier ply
	 *
	 * @param file - Chemin du fichier
	 * @throws Exception
	 */
	public static void checkFormat(String file) throws Exception {
		if (!file.endsWith(".ply"))
		{
			throw new Exception(file + " (Fichier invalide. Le fichier n'est pas au format ply.)");
		} // TODO REALISER UNE CLASSE ERREUR
	}

}

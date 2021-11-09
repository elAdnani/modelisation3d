package modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class RecuperationPly {

	private static String myPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
			+ File.separator + "resources" + File.separator + "models" + File.separator;
	private static int    nbVertex;
	private static int    nbFace;

	public static void main(String[] args) {
		String file = "vache.ply";

		Matrice res = null;
		List<Point> res2 = null;
		// List<Face> ensembleDePoint3D = null;

		try
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < 100; i++)
			{
				res = recuperationMatrice(myPath + file);

			}
			long endTime = System.nanoTime();
			System.out
					.println("Temps de chargement moyen : " + (float) (endTime - startTime) / 100 / 1000 / 1000 + "ms");
			System.out.println("Nb Point3Ds : " + res.getNbColonnes());
			// System.out.println("Nb faces : " + ensembleDePoint3D.size());

			startTime = System.nanoTime();
			for (int i = 0; i < 100; i++)
			{
				res2 = recuperationPoints(myPath + file);

			}
			endTime = System.nanoTime();
			System.out
					.println("Temps de chargement moyen : " + (float) (endTime - startTime) / 100 / 1000 / 1000 + "ms");
			System.out.println("Nb Point3Ds : " + res2.size());

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("DEBUT");
		/*
		 * for(Face p : ensembleDePoint3D) { System.out.println(p); try {
		 * Thread.sleep(10); } catch (InterruptedException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
		// }

	}

	/**
	 * Recupere la liste des Point3Ds dans un fichier PLY donné en parametre
	 * 
	 * @param fichier - Chemin vers le fichier
	 * @return {@link List} de {@link Point}
	 * @throws Exception
	 */
	public static List<Point> recuperationPoints(String fichier) throws Exception {
		checkFormat(fichier);

		List<Point> res = new ArrayList<Point>();

		FileReader FichierPly = null;

		try
		{
			
			FichierPly = new FileReader(fichier);

			String ligne = " ";

			BufferedReader reader = new BufferedReader(FichierPly);
			placerApresLaTeteDuFichier(reader);
			String tab[] = new String[0];

			for (int i = 0; i < nbVertex; i++)
			{
				ligne = reader.readLine();

				if(ligne != null && !ligne.isEmpty()) {					

					tab = ligne.split(" ");

					// System.out.println("" + (i+1) + "/" + (nbVertex));

					res.add(new Point(Double.valueOf(tab[0]), Double.valueOf(tab[1]), Double.valueOf(tab[2])));

					// System.out.println(res.get(i));
				} else
					i--;

			}

			reader.close();
		} catch (IOException o)
		{

			o.printStackTrace();
		}

		return res;
	}

	final static public int NBLIGNEPOINT3D = 3;

	public static Matrice recuperationMatrice(String fichier) throws Exception {
		checkFormat(fichier);

//		Matrice res = null;
		double[][] res2 = null;


		FileReader FichierPly = null;

		try
		{
			
			FichierPly = new FileReader(fichier);
			String ligneLectureFichier="";

			BufferedReader reader = new BufferedReader(FichierPly);
			placerApresLaTeteDuFichier(reader);
			String tab[] = new String[0];
//			res = new Matrice(NBLIGNEPOINT3D, nbVertex);
			res2 = new double[3][nbVertex];

			for (int colonne = 0; colonne < nbVertex; colonne++)
			{
				ligneLectureFichier = reader.readLine();
				if (!ligneLectureFichier.isEmpty())
				{
					tab = ligneLectureFichier.split(" ");
					// System.out.println("" + (colonne+1) + "/" + (nbVertex));

					for (int ligne = 0; ligne < 3; ligne++)
					{
//						res.ecrire(ligne, colonne, Double.valueOf(tab[ligne]));
						res2[ligne][colonne] = Double.valueOf(tab[ligne]);
					}

					// System.out.println(res.getColonne(colonne));
				} else
					colonne--;
			}

			reader.close();
		} catch (IOException o)
		{

			o.printStackTrace();
		}

		return new Matrice(res2);
	}

	/**
	 * Recupere la liste des Plans dans un fichier PLY donné en paramètre
	 * 
	 * @param fichier  - Chemin vers le fichier
	 * @param Point3Ds - {@link List} de {@link Point}
	 * @return {@link List} de {@link Face}
	 * @throws Exception
	 */
	public static List<Face> recuperationFaces(String fichier, List<Point> Point3Ds) throws Exception {
		checkFormat(fichier);

		List<Face> res = new ArrayList<Face>();

		FileReader FichierPly = null;

		try
		{
			
			FichierPly = new FileReader(fichier);

			String ligne = " ";

			BufferedReader reader = new BufferedReader(FichierPly);
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
//				System.out.println(ligne);
				tab = ligne.split(" ");
				Face ensembleDePoint3D = new Face();
				for (int i = 1; i < tab.length; i++)
				{
					ensembleDePoint3D.add(Point3Ds.get(Integer.valueOf(tab[i])));
				}
				res.add(ensembleDePoint3D);
			}
			reader.close();
		} catch (IOException o)
		{

			o.printStackTrace();
		}

		return res;
	}

	public static List<FaceMatrice> recuperationFacesMatrice(String fichier) throws Exception {
		checkFormat(fichier);

		List<FaceMatrice> res = new ArrayList<FaceMatrice>();

		FileReader FichierPly = null;

		try
		{
			
			FichierPly = new FileReader(fichier);

			String ligne = " ";

			BufferedReader reader = new BufferedReader(FichierPly);
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
//				System.out.println(ligne);
				tab = ligne.split(" ");
				FaceMatrice indiceDesEnsemblesDePoint = new FaceMatrice();
				for (int i = 1; i < tab.length; i++)
				{
					indiceDesEnsemblesDePoint.add(Integer.valueOf(tab[i]));
				}
				res.add(indiceDesEnsemblesDePoint);
			}
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
	 */
	public static void placerApresLaTeteDuFichier(BufferedReader raf) {
		try
		{
			
			String ligne;
			ligne = raf.readLine();

			while (!ligne.contains("element vertex"))
			{
				ligne = raf.readLine();
			}
			String[] lineV = ligne.split(" ");
			nbVertex = Integer.parseInt(lineV[lineV.length - 1]);

			while (!ligne.contains("element face"))
			{
				ligne = raf.readLine();
			}
			String[] lineF = ligne.split(" ");
			nbFace = Integer.parseInt(lineF[lineF.length - 1]);

			while (!ligne.contains("end_header"))
			{
				ligne = raf.readLine();

			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// on se replace tout au début du fichier

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
		}
	}

}

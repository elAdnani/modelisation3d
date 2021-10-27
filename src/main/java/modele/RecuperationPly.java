package modele;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class RecuperationPly {

	private static String myPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
			+ File.separator + "resources" + File.separator;
	private static int nbVertex;
	private static int nbFace;

//	public static void main(String[] args) {
//		file = "vache.ply";
//
//		List<Point> res = null;
//		List<Trace> ensembleDePoint = null;
//
//		try {
//			long startTime = System.nanoTime();
//			for (int i = 0; i < 100; i++) {
//				res = recuperationCoordonnee();
//				ensembleDePoint = recuperationTracerDesPoint(res);
//			}
//			long endTime = System.nanoTime();
//			System.out
//					.println("Temps de chargement moyen : " + (float) (endTime - startTime) / 100 / 1000 / 1000 + "ms");
//			System.out.println("Nb points : " + res.size());
//			System.out.println("Nb faces : " + ensembleDePoint.size());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("DEBUT");
//		
//		for(Trace p : ensembleDePoint) {
//			System.out.println(p);
//			try
//			{
//				Thread.sleep(10);
//			} catch (InterruptedException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}

	/**
	 * Recupere la liste des points dans un fichier PLY donné en parametre
	 * 
	 * @param fichier - Chemin vers le fichier
	 * @return {@link List} de {@link Point}
	 * @throws Exception
	 */
	public static List<Point> recuperationCoordonnee(String fichier) throws Exception {
		checkFormat(fichier);

		List<Point> res = new ArrayList<Point>();

		File FichierPly = null;

		try {

			FichierPly = new File(fichier);

			String ligne = " ";

			RandomAccessFile raf = new RandomAccessFile(FichierPly, "r");
			placerApresLaTeteDuFichier(raf);
			String tab[] = new String[0];
			for (int i = 0; i < nbVertex; i++) {
				ligne = raf.readLine();
				if(ligne != null && !ligne.isEmpty()) {					
					tab = ligne.split(" ");
//					System.out.println("" + (i+1) + "/" + (nbVertex));
					res.add(new Point(Double.valueOf(tab[0]), Double.valueOf(tab[1]), Double.valueOf(tab[2])));
//					System.out.println(res.get(i));
				} else i--;
			}

			raf.close();
		} catch (IOException o) {

			o.printStackTrace();
		}

		return res;
	}

	/**
	 * Recupere la liste des traces dans un fichier PLY donné en paramètre
	 * 
	 * @param fichier - Chemin vers le fichier
	 * @param points - {@link List} de {@link Point}
	 * @return {@link List} de {@link Trace}
	 * @throws Exception
	 */
	public static List<Trace> recuperationTracerDesPoint(String fichier, List<Point> points) throws Exception {
		checkFormat(fichier);

		List<Trace> res = new ArrayList<Trace>();

		File FichierPly = null;

		try {

			FichierPly = new File(fichier);

			String ligne = " ";

			RandomAccessFile raf = new RandomAccessFile(FichierPly, "r");
			placerApresLaTeteDuFichier(raf);

			String[] tab;

			for (int i = 1; i <= nbVertex; i++) {
				ligne = raf.readLine();
				if(ligne.isEmpty()) i--;
			}

			for (int j = 0; j < nbFace; j++) {
				ligne = raf.readLine();
//				System.out.println(ligne);
				tab = ligne.split(" ");
				Trace ensembleDePoint = new Trace();
				for (int i = 1; i < tab.length; i++) {
					ensembleDePoint.add(points.get(Integer.valueOf(tab[i])));
				}
				res.add(ensembleDePoint);
			}
			raf.close();
		} catch (IOException o) {

			o.printStackTrace();
		}

		return res;
	}

	/**
	 * Recherche le nombre d'occurence d'un caractere dans un String donné
	 * 
	 * @param mes - le {@link String} a parcourir
	 * @param c - le caractere à chercher
	 * @return Le nombre d'occurence du caractere dans le String
	 */
	public static int nombreOccurence(String mes, char c) {
		int cpt = 0;
		for (int i = 0; i < mes.length(); i++) {
			if (mes.charAt(i) == c) {
				cpt++;
			}
		}
		return cpt;
	}

	/**
	 * Lis le nombre de vertex et le nombre de face, puis se place à la fin du header
	 * 
	 * @param raf - RandomAccessFile
	 */
	public static void placerApresLaTeteDuFichier(RandomAccessFile raf) {
		try {
			raf.seek(0);
			String ligne;
			ligne = raf.readLine();

			while (!ligne.contains("element vertex")) {
				ligne = raf.readLine();
			}
			String[] lineV = ligne.split(" ");
			nbVertex = Integer.parseInt(lineV[lineV.length - 1]);

			while (!ligne.contains("element face")) {
				ligne = raf.readLine();
			}
			String[] lineF = ligne.split(" ");
			nbFace = Integer.parseInt(lineF[lineF.length - 1]);

			while (!ligne.contains("end_header")) {
				ligne = raf.readLine();

			}
		} catch (IOException e) {
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
		if (!file.endsWith(".ply")) {
			throw new Exception(file + " (Fichier invalide. Le fichier n'est pas au format ply.)");
		}
	}
}

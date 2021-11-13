package modele;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * Cette classe sert à mesurer les capacités de récupération des points.<br>
 * Il nous permettra également de passer de la liste des points aux matrices.
 * <br>
 * Il nous permet également de tester plusieurs fichiers ply à notre guise. ( Et
 * voir la conformité d'un fichier, s'il récupère bien les points et les faces)
 * <br>
 * 
 *
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 * @date 11 nov. 2021
 */
public class RecuperationPlyTest {

	/**
	 * Chemin pour arriver jusqu'au dossier de .ply
	 */
	private final static String myPath = System.getProperty("user.dir") + File.separator + "exemples" + File.separator;
	/**
	 * Attribut de calcul de temps
	 */
	private long startTime;
	private long endTime;
	private int repetition;
	/**
	 * Exemple de récupération d'un fichier PLY
	 */
	private final String fichier = "vache.ply";
	private final int VERTEX = 2903;
	private final int FACE = 5804;
	/**
	 * Liste des points et faces qui sera calculés
	 */
	private List<Point> points;
	private List<Face> faces;
	private Matrice vertexMatriciel;
	private List<FaceMatrice> facesMatriciel;

	/**
	 * Cette méthode est utile pour nos testes de temps. Comme il n'y a pas d'autres
	 * classes qui calcule le temps, elle est placée ici.<br>
	 * 
	 * @param repetition (nombre d'itération du test)
	 * @param nanoTime   (temps en nanoTime)
	 * @return temps en milliseconds
	 */
	public double nanotimeToMiliseconds(int repetition, double nanoTime) {
		return nanoTime / repetition / 1000 / 1000;
	}

	/**
	 * Cette méthode est utile pour nos testes de temps. Comme il n'y a pas d'autres
	 * classes qui calcule le temps, elle est placée ici.<br>
	 * Par défaut le nombre de répétition est égal à 1
	 * 
	 * @param nanoTime (temps en nanoTime)
	 * @return temps en milliseconds
	 */
	public double nanotimeToMiliseconds(double nanoTime) {
		return nanotimeToMiliseconds(1, nanoTime);
	}

	// TESTS

	@BeforeAll
	public static void beforeAllTests() {
		System.out.println("Début des tests pour la classe RecuperationPlyTest\n");

	}

	@AfterAll
	public static void afterAllTests() {
		System.out.println("Fin des tests de RecuperationPlyTest");

	}

	@BeforeEach
	public void setUp() {
		points = null;
		vertexMatriciel = null;
		faces = null;
		facesMatriciel = null;

		this.repetition = 100;

	}

	// ---- RECUPERATION DES POITNS/FACES A PARTIR D'UNE LISTE

	@Test
	void recuperationTotaleDesPoints() {

		try {
			for (int i = 0; i < repetition; i++) {
				points = null;
				points = RecuperationPly.recuperationPoints(myPath + fichier);

				assertEquals(points.size(), VERTEX);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void afficher(Number x) {
		System.out.println(x);
	}

	@Test
	void recuperationTotaleDesFaces() {

		try {
			points = RecuperationPly.recuperationPoints(myPath + fichier);

			for (int i = 0; i < repetition; i++) {
				faces = null;
				faces = RecuperationPly.recuperationFaces(myPath + fichier, points);
				assertEquals(faces.size(), FACE);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ---- RECUPERATION DES POITNS/FACES A PARTIR D'UNE MATRICE

	@Test
	void recuperationTotaleDesPointsMatriciel() {
		try {
			for (int i = 0; i < repetition; i++) {
				vertexMatriciel = null;
				vertexMatriciel = RecuperationPly.recuperationMatrice(myPath + fichier);
				assertEquals(vertexMatriciel.getNbColonnes(), VERTEX);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void recuperationTotaleDesFacesMatriciel() {

		try {

			for (int i = 0; i < repetition; i++) {
				facesMatriciel = null;
				facesMatriciel = RecuperationPly.recuperationFacesMatrice(myPath + fichier);
				assertEquals(facesMatriciel.size(), FACE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ---- CALCUL DE TEMPS A PARTIR D'UNE LISTE

//	@Test
//	void tempsRecuperationDesPoints() {
//		double objectif=1.5; // à définir
//		try 
//		{
//			
//			/*
//			 * ______________
//			 * REPETITION=100
//			 * ‾‾‾‾‾‾‾‾‾‾‾‾‾‾
//			 */
//			startTime = System.nanoTime();
//			for (int i = 0; i < repetition; i++)
//			{
//				points = RecuperationPly.recuperationPoints(myPath+fichier);
//			}
//			endTime = System.nanoTime();
//		
//		assertTrue( nanotimeToMiliseconds(repetition, (float) (endTime - startTime))<objectif);
//		
//		
//		}
//	catch(Exception e){
//		e.printStackTrace();
//	}
//		
//	}

//	@Test
//	public void tempsRecuperationDesFaces() {
//		double objectif=3; // à définir
//		try 
//		{
//			points = RecuperationPly.recuperationPoints(myPath+fichier);
//			/*
//			 * ______________
//			 * REPETITION=100
//			 * ‾‾‾‾‾‾‾‾‾‾‾‾‾‾
//			 */
//			startTime = System.nanoTime();
//			for (int i = 0; i < repetition; i++)
//			{
//				
//				faces = RecuperationPly.recuperationFaces(myPath+fichier,points);
//			}
//			endTime = System.nanoTime();
//		float temps = endTime - startTime;
//
//		assertTrue( nanotimeToMiliseconds(repetition, temps )<objectif);
//		
//		
//		
//		
//		}
//	catch(Exception e){
//		e.printStackTrace();
//	}
//		
//	}

	// ---- CALCUL DE TEMPS A PARTIR D'UNE MATRICE

	@Test
	void tempsRecuperationDesPointsMatriciels() {
		double objectif = 10; // à définir
		try {
			/*
			 * ______________ REPETITION=100 ‾‾‾‾‾‾‾‾‾‾‾‾‾‾
			 */
			startTime = System.nanoTime();
			for (int i = 0; i < repetition; i++) {
				vertexMatriciel = RecuperationPly.recuperationMatrice(myPath + fichier);
			}
			endTime = System.nanoTime();

			assertTrue(nanotimeToMiliseconds(repetition, (float) (endTime - startTime)) < objectif);
			/*
			 * Note : 1.80~1.85 est un nombre plus sûr
			 */
			/*
			 * _______________ REPETITION=1000 ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
			 */
			repetition = 1000;
			objectif = 1.6; // à définir
			startTime = System.nanoTime();
			for (int i = 0; i < repetition; i++) {
				vertexMatriciel = RecuperationPly.recuperationMatrice(myPath + fichier);
			}
			endTime = System.nanoTime();

			assertTrue(nanotimeToMiliseconds(repetition, (float) (endTime - startTime)) < objectif);
			/*
			 * _________________ REPETITION=100000 ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
			 */
			/*
			 * repetition=10000; startTime = System.nanoTime(); for (int i = 0; i <
			 * repetition; i++) { matrice=null; matrice =
			 * RecuperationPly.recuperationMatrice(myPath+"vache.ply");
			 * 
			 * } endTime = System.nanoTime();
			 * 
			 * assertTrue( nanotimeToMiliseconds(repetition, (float) (endTime -
			 * startTime))<1.6);
			 * 
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
//	void tempsRecuperationDesFacesMatriciel() {
//		double objectif=5; // à définir
//		try 
//		{
//			/*
//			 * ______________
//			 * REPETITION=100
//			 * ‾‾‾‾‾‾‾‾‾‾‾‾‾‾
//			 */
//			startTime = System.nanoTime();
//			for (int i = 0; i < repetition; i++)
//			{
//				facesMatriciel = RecuperationPly.recuperationFacesMatrice(myPath+fichier);
//			}
//			endTime = System.nanoTime();
//		
//		assertTrue( nanotimeToMiliseconds(repetition, (float) (endTime - startTime))<objectif);
//		
//		
//		}
//	catch(Exception e){
//		e.printStackTrace();
//	}
//		
//	}

}

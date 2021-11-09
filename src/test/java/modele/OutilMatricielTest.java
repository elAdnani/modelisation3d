package modele;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * Cette classe sert à .........
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 9 nov. 2021
 * @version XX
 */
public class OutilMatricielTest {

	
	Matrice A ;
	Matrice B ;
	Matrice C ;
	
		@BeforeAll
		public static void beforeAllTests() {
			System.out.println("Début des tests pour la classe Matrice\n");

		}
		
		
		
		@AfterAll
		public static void afterAllTests() {
			System.out.println("Fin des tests de Matrice");

		}
		
		
		
		@BeforeEach
		public void setUp() {
			
			double[][] donneesMatriceA= new double[][] {
				{1,2},
				{1,1},
				{0,3}
			};
			double[][] donneesMatriceB=new double[][] {
				{-1,1,0,1},
				{2,1,0,0}
			};
			double[][] donneesMatriceC=new double[][]
					{
				{-5,3},
				{7,-3},
				{2,9},
				{1,2}
					};
			
			A = new Matrice(donneesMatriceA);
			B = new Matrice(donneesMatriceB);
			C = new Matrice(donneesMatriceC);
			
		}
		@Test
		public void testCadrage() {
			
		}
		@Test
		public void testRotation() {
			
		}
		@Test
		public void testHomothetie() {
			
		}
		@Test
		public void testTranslation() {
			
		}

		
}

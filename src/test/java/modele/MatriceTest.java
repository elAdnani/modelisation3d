package modele;
	import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
	import org.junit.jupiter.api.AfterEach;
	import org.junit.jupiter.api.BeforeAll;
	import org.junit.jupiter.api.BeforeEach;
	import org.junit.jupiter.api.Test;
/**
 * 
 * Cette classe test
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 9 nov. 2021
 * @version XX
 */
public class MatriceTest {

	
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
		
		@AfterEach
		public void afterATest() {
		}
		
		
		@Test
		public void testLecture() {
			double[][] donneesMatriceA= new double[][] {
				{1,2},
				{1,1},
				{0,3}};
				
				assertEquals(A.lire(0, 0), donneesMatriceA[0][0]);
				assertEquals(A.lire(0, 1), donneesMatriceA[0][1]);
				assertEquals(A.lire(1, 0), donneesMatriceA[1][0]);
				assertEquals(A.lire(1, 1), donneesMatriceA[1][1]);
				
				assertTrue(Double.isNaN(A.lire(100, 100)));
				assertTrue(Double.isNaN(A.lire(-10, -10)));
				
				
		}
		
		
		@Test
		public void testEcriture() {
			double[][] donneesMatriceD= new double[][] {
				{1,2},
				{1,1},
				{0,3}};
				Matrice D = new Matrice(donneesMatriceD);
				final double ALTERATION = Double.MIN_NORMAL;
				
				assertEquals(D.lire(0, 0), donneesMatriceD[0][0]);
				D.ecrire(0, 0, ALTERATION);
				assertEquals(D.lire(0, 0), ALTERATION);
				
				assertEquals(D.lire(0, 1), donneesMatriceD[0][1]);
				D.ecrire(0, 0, ALTERATION);
				assertEquals(D.lire(0, 0), ALTERATION);
				
				assertEquals(D.lire(1, 0), donneesMatriceD[1][0]);
				D.ecrire(1, 0, ALTERATION);
				assertEquals(D.lire(1, 0), ALTERATION);

				
				assertTrue(Double.isNaN(A.lire(100, 100)));
				assertTrue(Double.isNaN(A.lire(-10, -10)));
			
		}

		@Test
		public void testMatriceIdentite() {
			
		}
		
		@Test
		public void testAddition() {
			Matrice neFonctionnePas = null;
			assertEquals(neFonctionnePas, A.addition(B)); // a et b ne sont pas de même format ainsi ils ne peuvent s'additionner

			Matrice matriceD = new Matrice(	new double[][] {
				{1,2},
				{1,1},
				{0,3}
			});

			Matrice APlusD = new Matrice(new double[][] {
				{1+1,2+2},
				{1+1,1+1},
				{0+0,3+3}
			});
			
			assertTrue(APlusD.equals(A.addition(matriceD))	);
			
		}
		

		
		@Test
		public void testSoustraction() {
			Matrice matriceD = new Matrice(new double[][] {
				{2,6},
				{8,-1},
				{-5,2}
			});
			Matrice AMoinsD = new Matrice(new double[][] {
				{(1-2),(2-6)	},
				{(1-8),(1-(-1))	},
				{(0-(-5)),(3-2)	}
			});
			
			assertEquals(null, A.soustraction(B)				); // pas le même format
			assertEquals(null, B.soustraction(A)				);
			assertTrue(AMoinsD.equals(A.soustraction(matriceD))	);
			
		}
		
		@Test
		public void testMultiplication() {
			Matrice AB = new Matrice(new double[][] {
				{3,3,0,1},
				{1,2,0,1},
				{6,3,0,0}
			});
			Matrice BC = new Matrice(new double[][] {
				{13,-4},
				{-3,3}
			});
			Matrice CB = new Matrice(new double[][] {
				{11,-2,0,-5},
				{-13,4,0,7},
				{16,11,0,2},
				{3,3,0,1}
			});
			Matrice matriceNulle = new Matrice(0);
			
			
			assertTrue(	AB.equals(A.multiplication(B))  );
			assertTrue(	BC.equals(B.multiplication(C))	);
			assertTrue( CB.equals(C.multiplication(B))	);
			
			assertFalse( BC.equals(C.multiplication(B))	);
			assertFalse( AB.equals(null)				);
			assertFalse( AB.equals(matriceNulle)			);
			
			
		}
		
		@Test
		public void testMatriceNulle() {
			double[][] donneesMatriceNulleUne = new double[][] {
				{0,0},
				{0,0},
				{0,0}
			};
			Matrice matriceNulleUne=new Matrice(donneesMatriceNulleUne);
			double[][] donneesMatriceNulleDeux = new double[][] {
				{0}
			}; 
			Matrice matriceNulleDeux=new Matrice(donneesMatriceNulleDeux);
			
			assertTrue(matriceNulleUne.estNulle()	);
			assertTrue(matriceNulleDeux.estNulle()	);
			assertFalse(A.estNulle()				);
			assertFalse(B.estNulle()				);
		}
		
		@Test
		public void testEstCarre() {
			assertTrue(	new Matrice(
					new double[][] {{0}}
							).estCarre()
			  );
			assertTrue(	new Matrice(
					new double[][] 	   {{0,0},
										{0,0}}
							).estCarre()
			  );
			assertTrue( new Matrice(
					new double[][]     { {0,0,0},
										 {0,0,0},
										 {0,0,0}
										}
							).estCarre()
			  );
			
			assertFalse(A.estCarre());
			assertFalse(B.estCarre());
			assertFalse(C.estCarre());
				
		}
		
		@Test
		public void testInverse() {
			
			
		}
		
		
		@Test
		public void testEstSymetrique() {
			
		}
		

	
}

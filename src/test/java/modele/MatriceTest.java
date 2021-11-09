package modele;
	import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
	import org.junit.jupiter.api.AfterEach;
	import org.junit.jupiter.api.BeforeAll;
	import org.junit.jupiter.api.BeforeEach;
	import org.junit.jupiter.api.Test;

public class MatriceTest {

	
	Matrice A ;
	Matrice B ;
	
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
			
			
			A = new Matrice(donneesMatriceA);
			B = new Matrice(donneesMatriceB);
		}
		
		@AfterEach
		public void afterATest() {
			
		}
		
		/*
		@Test
		public void testRead() {
			
		}
		
		
		@Test
		public void testWrite() {
			
		}
		*/
		
		@Test
		public void testMatriceNulle() {
			double[][] donneesMatriceNulleUne = new double[][] {
				{0,0},
				{0,0},
				{0,0}
			}; 
			Matrice MatriceNulleUne=new Matrice(donneesMatriceNulleUne);
			double[][] donneesMatriceNulleDeux = new double[][] {
				{0}
			}; 
			Matrice MatriceNulleDeux=new Matrice(donneesMatriceNulleDeux);
			
			
			assertTrue(MatriceNulleUne.estNulle()	);
			assertTrue(MatriceNulleDeux.estNulle()	);
			assertFalse(A.estNulle()				);
			assertFalse(B.estNulle()				);
		}
		
		
		@Test
		public void testMatriceIdentite() {
			
		}
		
		@Test
		public void testAddition() {
			Matrice neFonctionnePas = null;
			assertEquals(neFonctionnePas, A.addition(B)); // a et b ne sont pas de même format ainsi ils ne peuvent s'additionner
			
			
			Matrice MatriceC = new Matrice(new double[][] {
				{2,6},
				{8,-1},
				{-5,2}
			});
			
			Matrice APlusC = new Matrice(new double[][] {
				{3,8},
				{9,0},
				{-5,5}
			});
			
			
			assertTrue(APlusC.equals(A.addition(MatriceC))	);
			
			
			
		
		}
		

		
		@Test
		public void testSoustraction() {
			Matrice C = new Matrice(new double[][] {
				{-5,3},
				{7,-3},
				{2,9}
			});
			Matrice AMoinsC = new Matrice(new double[][] {
				{(1-(-5)),(2-3)	},
				{(1-7),(1-(-3))	},
				{(0-2),(3-9)	}
			});
			
			assertEquals(null, A.soustraction(B)		); // pas le même format
			assertEquals(null, B.soustraction(A)		);
			assertTrue(AMoinsC.equals(A.soustraction(C)));
			
			
			
			
		}
		
		/*
	double[][] donnéesMatriceA = new double[][] {
		{1,2},
		{1,1},
		{0,3}
	};
	double[][] donnéesMatriceB = new double[][] {
		{-1,1,0,1},
		{2,1,0,0}
	};
		 */
		@Test
		public void testMultiplication() {
			Matrice AB = new Matrice(new double[][] {
				{3,3,0,1},
				{1,2,0,1},
				{6,3,0,0}
			});
			Matrice BA = new Matrice(new double[][] {
				{-1,2,1},
				{-1,-5,-3}
			});
			
			
			assertTrue(	AB.equals(A.multiplication(B))  );
			
			assertTrue(	BA.equals(B.multiplication(A))	);
			System.out.println(B.multiplication(A));
			
		}
		
		
		@Test
		public void testPuissance() {
			
		}
		
		
		@Test
		public void testInverse() {
			
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
			
			assertFalse(A.estCarre());
			assertFalse(B.estCarre());
			
			
		}
		
		
		@Test
		public void testEstSymetrique() {
			
		}
		
		
		@Test
		public void testEstTriangulaire() {
			
		}
	

	
}

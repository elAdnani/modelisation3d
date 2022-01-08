package modele;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import math.Matrice;

/**
 * 
 * Cette classe regroupe les testes confirmant les fonctionnalités des méthodes
 * réalisées dans {@link Matrice}. <br>
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 * @date 9 nov. 2021
 */
public class MatriceTest {

	Matrice A;
	Matrice B;
	Matrice C;

	@BeforeAll
	public static void beforeAllTests() {
		System.out.println("Début des tests pour la classe Matrice\n");

	}

	@AfterAll
	public static void afterAllTests() {
		System.out.println("Fin des tests de Matrice");

	}

	@Before
	public void setUp() {
		
		double[][] donneesMatriceA = new double[][] { { 1, 2 }, { 1, 1 }, { 0, 3 } };
		double[][] donneesMatriceB = new double[][] { { -1, 1, 0, 1 }, { 2, 1, 0, 0 } };
		double[][] donneesMatriceC = new double[][] { { -5, 3 }, { 7, -3 }, { 2, 9 }, { 1, 2 } };

		A = new Matrice(donneesMatriceA);
		B = new Matrice(donneesMatriceB);
		C = new Matrice(donneesMatriceC);

	}

	@Test
	public void testLecture() {
		double[][] donneesMatriceA = new double[][] { { 1, 2 }, { 1, 1 }, { 0, 3 } };

		assertEquals(A.lire(0, 0), donneesMatriceA[0][0]);
		assertEquals(A.lire(0, 1), donneesMatriceA[0][1]);
		assertEquals(A.lire(1, 0), donneesMatriceA[1][0]);
		assertEquals(A.lire(1, 1), donneesMatriceA[1][1]);

		assertTrue(Double.isNaN(A.lire(100, 100)));
		assertTrue(Double.isNaN(A.lire(-10, -10)));

	}

	@Test
	public void testEcriture() {
		double[][] donneesMatriceD = new double[][] { { 1, 2 }, { 1, 1 }, { 0, 3 } };
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
	public void testAddition() {
		Matrice neFonctionnePas = null;
		assertEquals(neFonctionnePas, A.addition(B)); // a et b ne sont pas de même format ainsi ils ne peuvent
														// s'additionner

		Matrice matriceD = new Matrice(new double[][] { { 1, 2 }, { 1, 1 }, { 0, 3 } });

		Matrice APlusD = new Matrice(new double[][] { { 1 + 1, 2 + 2 }, { 1 + 1, 1 + 1 }, { 0 + 0, 3 + 3 } });

		Matrice matriceE = new Matrice(new double[][] { { 5, 2, -7, 4 }, { -1, -2, 6, -1 } });
		Matrice EPlusB = new Matrice(
				new double[][] { { -1 + 5, 1 + 2, 0 + (-7), 1 + 4 }, { 2 + (-1), 1 + (-2), 0 + 6, 0 + (-1) } });

		Matrice vide = new Matrice(new double[][] { { 0 } });
		Matrice un = new Matrice(new double[][] { { 1 } });

		assertEquals(APlusD, A.addition(matriceD));
		assertEquals(EPlusB, matriceE.addition(B));

		// assertEquals(APlusD, matriceD.addition(A) );
		assertEquals(un, vide.addition(un));

	}

	@Test
	public void testSoustraction() {
		Matrice matriceD = new Matrice(new double[][] { { 2, 6 }, { 8, -1 }, { -5, 2 } });
		Matrice AMoinsD = new Matrice(
				new double[][] { { (1 - 2), (2 - 6) }, { (1 - 8), (1 - (-1)) }, { (0 - (-5)), (3 - 2) } });

		assertEquals(null, A.soustraction(B)); // pas le même format
		assertEquals(null, B.soustraction(A));
		assertEquals(AMoinsD, A.soustraction(matriceD));

	}

	@Test
	public void testMultiplication() {
		Matrice AB = new Matrice(new double[][] { { 3, 3, 0, 1 }, { 1, 2, 0, 1 }, { 6, 3, 0, 0 } });
		Matrice BC = new Matrice(new double[][] { { 13, -4 }, { -3, 3 } });
		Matrice CB = new Matrice(
				new double[][] { { 11, -2, 0, -5 }, { -13, 4, 0, 7 }, { 16, 11, 0, 2 }, { 3, 3, 0, 1 } });
		Matrice matriceNulle = new Matrice(0);

		assertEquals(AB, A.multiplication(B));
		assertEquals(BC, B.multiplication(C));
		assertEquals(CB, C.multiplication(B));

		assertEquals(new Matrice(new double[][] { { 13 * (10), -4 * (10) }, { -3 * (10), 3 * (10) } }),
				BC.multiplication(10));
		assertEquals(
				new Matrice(new double[][] { { 3 * (-2), 3 * (-2), 0 * (-2), 1 * (-2) },
						{ 1 * (-2), 2 * (-2), 0 * (-2), 1 * (-2) }, { 6 * (-2), 3 * (-2), 0 * (-2), 0 * (-2) } }),
				AB.multiplication(-2));

		assertNull(BC.multiplication(CB));
		assertNull(AB.multiplication(matriceNulle));

	}

	@Test
	public void testMatriceNulle() {
		double[][] donneesMatriceNulleUne = new double[][] { { 0, 0 }, { 0, 0 }, { 0, 0 } };
		Matrice matriceNulleUne = new Matrice(donneesMatriceNulleUne);
		double[][] donneesMatriceNulleDeux = new double[][] { { 0 } };
		Matrice matriceNulleDeux = new Matrice(donneesMatriceNulleDeux);

		assertTrue(matriceNulleUne.estNulle());
		assertTrue(matriceNulleDeux.estNulle());
		assertFalse(A.estNulle());
		assertFalse(B.estNulle());
	}

	@Test
	public void testEstCarre() {
		assertTrue(new Matrice(new double[][] { { 0 } }).estCarre());
		assertTrue(new Matrice(new double[][] { { 0, 0 }, { 0, 0 } }).estCarre());
		assertTrue(new Matrice(new double[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }).estCarre());

		assertFalse(A.estCarre());
		assertFalse(B.estCarre());
		assertFalse(C.estCarre());

	}
	
	/*
	  @Test 
	  public void testInverse() { 
		  Matrice matriceIdentite = new Matrice( new
	  double[][] { {1,0}, {0,1} }); 
		  Matrice m = new Matrice(new double[][] {
	  {1,3,3}, {1,4,3}, {1,3,4} }); 
		  Matrice mInverse = new Matrice(new double[][] {
	  {7,-3,-3}, {-1,1,0}, {-1,0,1} });
	  
	  assertEquals(matriceIdentite,matriceIdentite.getInverse());
	  assertEquals(matriceIdentite, Matrice.getMatriceInverse(2));
	  assertEquals(m,mInverse);
	  
	  
	  
	  assertNull(A.getInverse()); assertNull(B.getInverse());
	  assertNull(C.getInverse());
	  
	  }
	 */

}

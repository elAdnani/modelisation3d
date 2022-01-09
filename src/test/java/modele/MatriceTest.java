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

import math.Matrix;

/**
 * 
 * Cette classe regroupe les testes confirmant les fonctionnalités des méthodes
 * réalisées dans {@link Matrix}. <br>
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 * @date 9 nov. 2021
 */
public class MatriceTest {

	Matrix A;
	Matrix B;
	Matrix C;

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

		A = new Matrix(donneesMatriceA);
		B = new Matrix(donneesMatriceB);
		C = new Matrix(donneesMatriceC);

	}

	@Test
	public void testLecture() {
		double[][] donneesMatriceA = new double[][] { { 1, 2 }, { 1, 1 }, { 0, 3 } };

		assertEquals(A.read(0, 0), donneesMatriceA[0][0]);
		assertEquals(A.read(0, 1), donneesMatriceA[0][1]);
		assertEquals(A.read(1, 0), donneesMatriceA[1][0]);
		assertEquals(A.read(1, 1), donneesMatriceA[1][1]);

		assertTrue(Double.isNaN(A.read(100, 100)));
		assertTrue(Double.isNaN(A.read(-10, -10)));

	}

	@Test
	public void testEcriture() {
		double[][] donneesMatriceD = new double[][] { { 1, 2 }, { 1, 1 }, { 0, 3 } };
		Matrix D = new Matrix(donneesMatriceD);
		final double ALTERATION = Double.MIN_NORMAL;

		assertEquals(D.read(0, 0), donneesMatriceD[0][0]);
		D.write(0, 0, ALTERATION);
		assertEquals(D.read(0, 0), ALTERATION);

		assertEquals(D.read(0, 1), donneesMatriceD[0][1]);
		D.write(0, 0, ALTERATION);
		assertEquals(D.read(0, 0), ALTERATION);

		assertEquals(D.read(1, 0), donneesMatriceD[1][0]);
		D.write(1, 0, ALTERATION);
		assertEquals(D.read(1, 0), ALTERATION);

		assertTrue(Double.isNaN(A.read(100, 100)));
		assertTrue(Double.isNaN(A.read(-10, -10)));

	}

	@Test
	public void testAddition() {
		Matrix neFonctionnePas = null;
		assertEquals(neFonctionnePas, A.addition(B)); // a et b ne sont pas de même format ainsi ils ne peuvent
														// s'additionner

		Matrix matriceD = new Matrix(new double[][] { { 1, 2 }, { 1, 1 }, { 0, 3 } });

		Matrix APlusD = new Matrix(new double[][] { { 1 + 1, 2 + 2 }, { 1 + 1, 1 + 1 }, { 0 + 0, 3 + 3 } });

		Matrix matriceE = new Matrix(new double[][] { { 5, 2, -7, 4 }, { -1, -2, 6, -1 } });
		Matrix EPlusB = new Matrix(
				new double[][] { { -1 + 5, 1 + 2, 0 + (-7), 1 + 4 }, { 2 + (-1), 1 + (-2), 0 + 6, 0 + (-1) } });

		Matrix vide = new Matrix(new double[][] { { 0 } });
		Matrix un = new Matrix(new double[][] { { 1 } });

		assertEquals(APlusD, A.addition(matriceD));
		assertEquals(EPlusB, matriceE.addition(B));

		// assertEquals(APlusD, matriceD.addition(A) );
		assertEquals(un, vide.addition(un));

	}

	@Test
	public void testSoustraction() {
		Matrix matriceD = new Matrix(new double[][] { { 2, 6 }, { 8, -1 }, { -5, 2 } });
		Matrix AMoinsD = new Matrix(
				new double[][] { { (1 - 2), (2 - 6) }, { (1 - 8), (1 - (-1)) }, { (0 - (-5)), (3 - 2) } });

		assertEquals(null, A.substraction(B)); // pas le même format
		assertEquals(null, B.substraction(A));
		assertEquals(AMoinsD, A.substraction(matriceD));

	}

	@Test
	public void testMultiplication() {
		Matrix AB = new Matrix(new double[][] { { 3, 3, 0, 1 }, { 1, 2, 0, 1 }, { 6, 3, 0, 0 } });
		Matrix BC = new Matrix(new double[][] { { 13, -4 }, { -3, 3 } });
		Matrix CB = new Matrix(
				new double[][] { { 11, -2, 0, -5 }, { -13, 4, 0, 7 }, { 16, 11, 0, 2 }, { 3, 3, 0, 1 } });
		Matrix matriceNulle = new Matrix(0);

		assertEquals(AB, A.multiplication(B));
		assertEquals(BC, B.multiplication(C));
		assertEquals(CB, C.multiplication(B));

		assertEquals(new Matrix(new double[][] { { 13 * (10), -4 * (10) }, { -3 * (10), 3 * (10) } }),
				BC.multiplication(10));
		assertEquals(
				new Matrix(new double[][] { { 3 * (-2), 3 * (-2), 0 * (-2), 1 * (-2) },
						{ 1 * (-2), 2 * (-2), 0 * (-2), 1 * (-2) }, { 6 * (-2), 3 * (-2), 0 * (-2), 0 * (-2) } }),
				AB.multiplication(-2));

		assertNull(BC.multiplication(CB));
		assertNull(AB.multiplication(matriceNulle));

	}

	@Test
	public void testMatriceNulle() {
		double[][] donneesMatriceNulleUne = new double[][] { { 0, 0 }, { 0, 0 }, { 0, 0 } };
		Matrix matriceNulleUne = new Matrix(donneesMatriceNulleUne);
		double[][] donneesMatriceNulleDeux = new double[][] { { 0 } };
		Matrix matriceNulleDeux = new Matrix(donneesMatriceNulleDeux);

		assertTrue(matriceNulleUne.isNull());
		assertTrue(matriceNulleDeux.isNull());
		assertFalse(A.isNull());
		assertFalse(B.isNull());
	}

	@Test
	public void testEstCarre() {
		assertTrue(new Matrix(new double[][] { { 0 } }).isSquare());
		assertTrue(new Matrix(new double[][] { { 0, 0 }, { 0, 0 } }).isSquare());
		assertTrue(new Matrix(new double[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }).isSquare());

		assertFalse(A.isSquare());
		assertFalse(B.isSquare());
		assertFalse(C.isSquare());

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

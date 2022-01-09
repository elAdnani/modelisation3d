package modele;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import math.VecteurOutil;
import modele.geometrique.FigureFabrique;
import modele.geometrique.Point;
import modele.geometrique.Vecteur;


/**
 * 
 * Cette classe regroupe les testes confirmant les fonctionnalités des formules mathématiques
 *
 * @author <a href="mailto:leo.benhatat.etu@univ-lille.fr">Leo BENHATAT</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 25 dec. 2021
 * 
 */
public class FormulesTest {
	public Point a;
	public Point b;
	public Point c;
	
	
	
	@Before
	public void setUp() {
		FigureFabrique fb = FigureFabrique.getInstance();
		
		a = fb.vertex(1, 2, 3);
		b = fb.vertex(2, 3, 3);
		c = fb.vertex(2, 2, 5);
	}
	
	/**
	 * Calcul un vecteur à partir de deux points
	 */
	@Test
	public void testPointEnVecteur() {
		
		Vecteur vecteurAB = VecteurOutil.vertexToVector(a, b);

		assertEquals(2-1, vecteurAB.getX());
		assertEquals(3-2, vecteurAB.getY());
		assertEquals(3-3,vecteurAB.getZ());
		
		Vecteur vecteurAC = VecteurOutil.vertexToVector(a, c);
		
		assertEquals(2-1, vecteurAC.getX());
		assertEquals(2-2, vecteurAC.getY());
		assertEquals(5-3,vecteurAC.getZ());
	}
	

	
	/**
	 * Calcul un produit vectoriel
	 */
	@Test
	public void testProduitVectoriel() {
		
		
		Vecteur vec = VecteurOutil.vectorialProduct(VecteurOutil.vertexToVector(a, b), VecteurOutil.vertexToVector(a, c));
				
		assertEquals(1*1,vec.getX());
		assertEquals(1*0,vec.getY());
		assertEquals(-1.0,vec.getZ());
	}
	
	/**
	 * Calcul la normal unittaire à un vecteur
	 */
	@Test
	public void testNormalUnitaire() {
		
		Vecteur abvac = VecteurOutil.vectorialProduct(VecteurOutil.vertexToVector(a, b), VecteurOutil.vertexToVector(a, c));
		
		double normeAbvac = 3.0;
		
		Vecteur vec = VecteurOutil.unitNormal(a, b, c);
		
		assertEquals(vec.getX(), abvac.getX()/normeAbvac);
		assertEquals(vec.getY(), abvac.getY()/normeAbvac);
		assertEquals(vec.getZ(), abvac.getZ()/normeAbvac);
	}
	
}

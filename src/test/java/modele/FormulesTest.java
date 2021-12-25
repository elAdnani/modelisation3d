package modele;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

/**
 * 
 * Cette classe regroupe les testes confirmant les fonctionnalités des formules mathématiques
 *
 */

public class FormulesTest {
	public Point a;
	public Point b;
	public Point c;
	
	@Test
	public void testPointEnVecteur() {
		a = new Point(1, 2, 3);
		b = new Point(2, 3, 3);
		
		Vecteur vec = Vecteur.pointEnVecteur(a, b);
		Vecteur res = new Vecteur(1.0, 1.0, 0.0);	

		assertTrue(vec.getX() == res.getX());
		assertTrue(vec.getY() == res.getY());
		assertTrue(vec.getZ() == res.getZ());
	}
	
	@Test
	public void testProduitVectoriel() {
		a = new Point(1, 2, 3);
		b = new Point(2, 3, 3);
		c = new Point(2, 2, 5);
		
		Vecteur ab = Vecteur.pointEnVecteur(a, b);
		Vecteur ac = Vecteur.pointEnVecteur(a, c);
		
		Vecteur vec = Vecteur.produitVectoriel(ab, ac);
		Vecteur res = new Vecteur(2.0, -2.0, -1.0);
				
		assertTrue(vec.getX() == res.getX());
		assertTrue(vec.getY() == res.getY());
		assertTrue(vec.getZ() == res.getZ());
	}
	
	@Test
	public void testNormalUnitaire() {
		a = new Point(1, 2, 3);
		b = new Point(2, 3, 3);
		c = new Point(2, 2, 5);
		
		Vecteur abvac = Vecteur.produitVectoriel(Vecteur.pointEnVecteur(a, b), Vecteur.pointEnVecteur(a, c));
		
		double normeAbvac = 3.0;
		
		Vecteur vec = Vecteur.normalUnitaire(a, b, c);
		Vecteur res = new Vecteur(0.6666666666666666, -0.6666666666666666, -0.3333333333333333);
		
		assertTrue(vec.getX() == res.getX());
		assertTrue(vec.getY() == res.getY());
		assertTrue(vec.getZ() == res.getZ());
	}
	
}

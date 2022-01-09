package math;

import modele.geometrique.FigureFabrique;
import modele.geometrique.Point;
import modele.geometrique.Vecteur;

/**
 * 
 * Cette classe sert à .........
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 9 janv. 2022
 * @version XX
 */
public class VecteurOutil {
	private static FigureFabrique fabriqueVecteur = FigureFabrique.getInstance();
	
	private VecteurOutil() {}

	/**
	 * Calcul la normal unitaire à un plan ABC
	 * @param pointA
	 * @param pointB
	 * @param pointC
	 * @return la normal unitaire
	 */
	public static Vecteur normalUnitaire(final Point pointA,final Point pointB,final Point pointC) {
		Vecteur abVac = VecteurOutil.produitVectoriel(VecteurOutil.pointEnVecteur(pointA, pointB), VecteurOutil.pointEnVecteur(pointA, pointC));
		
		double normeABvAC = Math.sqrt(abVac.getX()*abVac.getX()+ abVac.getY()*abVac.getY() + abVac.getZ()*abVac.getZ());
	
		return fabriqueVecteur.vecteur(abVac.getX()/normeABvAC, abVac.getY()/normeABvAC, abVac.getZ()/normeABvAC);
	}

	/**
	 * Réalise un produit de deux vecteurs
	 * @param vectAb
	 * @param vectAc
	 * @return
	 */
	public static Vecteur produitVectoriel(Vecteur vectAb, Vecteur vectAc) {
		
		return fabriqueVecteur.vecteur(vectAb.getY()*vectAc.getZ() - vectAc.getY()*vectAb.getZ()
							, vectAb.getZ()*vectAc.getX() - vectAc.getZ()* vectAb.getX()
							, vectAb.getX()*vectAc.getY() - vectAc.getX()*vectAb.getY());
	}

	/**
	 * Réalise la transformation de deux points en un vecteur
	 * @param un point A
	 * @param un point B
	 * @return le vecteur AB
	 */
	public static Vecteur pointEnVecteur (Point pointA, Point pointB) {
		
		return fabriqueVecteur.vecteur(pointB.getX()-pointA.getX(), pointB.getY()-pointA.getY(), pointB.getZ()-pointA.getZ());
		
	}

}

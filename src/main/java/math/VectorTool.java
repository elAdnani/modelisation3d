package math;

import modele.geometrique.FigureFabric;
import modele.geometrique.Vertex;
import modele.geometrique.Vector;

/**
 * 
 * Cette classe sert d'intermédiaire entre les classes points et vecteurs avec
 * les classes extérieurs au package
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 * @date 7 janv. 2022
 * 
 */
public class VectorTool {
	private static FigureFabric vectorFabric = FigureFabric.getInstance();

	private VectorTool() {
	}

	/**
	 * Calcul la normal unitaire à un plan ABC
	 * 
	 * @param pointA
	 * @param pointB
	 * @param pointC
	 * @return la normal unitaire
	 */
	public static Vector unitNormal(final Vertex pointA, final Vertex pointB, final Vertex pointC) {
		Vector abVac = VectorTool.vectorialProduct(VectorTool.vertexToVector(pointA, pointB),
				VectorTool.vertexToVector(pointA, pointC));

		double normeABvAC = Math
				.sqrt(abVac.getX() * abVac.getX() + abVac.getY() * abVac.getY() + abVac.getZ() * abVac.getZ());

		return vectorFabric.vector(abVac.getX() / normeABvAC, abVac.getY() / normeABvAC, abVac.getZ() / normeABvAC);
	}

	/**
	 * Réalise un produit de deux vecteurs
	 * 
	 * @param vectAb
	 * @param vectAc
	 * @return
	 */
	public static Vector vectorialProduct(Vector vectAb, Vector vectAc) {

		return vectorFabric.vector(vectAb.getY() * vectAc.getZ() - vectAc.getY() * vectAb.getZ(),
				vectAb.getZ() * vectAc.getX() - vectAc.getZ() * vectAb.getX(),
				vectAb.getX() * vectAc.getY() - vectAc.getX() * vectAb.getY());
	}

	/**
	 * Réalise la transformation de deux points en un vecteur
	 * 
	 * @param un point A
	 * @param un point B
	 * @return le vecteur AB
	 */
	public static Vector vertexToVector(Vertex pointA, Vertex pointB) {

		return vectorFabric.vector(pointB.getX() - pointA.getX(), pointB.getY() - pointA.getY(),
				pointB.getZ() - pointA.getZ());

	}

}

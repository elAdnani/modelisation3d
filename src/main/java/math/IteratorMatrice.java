package math;

import java.util.Iterator;

/**
 * 
 * Cette classe permet l'itération de la matrice. <br>
 * Il itère et se déplace entre les colonnes d'une matrice. <br>
 *
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 * @date 9 nov. 2021
 * 
 */
public class IteratorMatrice implements Iterator<Matrice> {

	private Matrice matrice;
	private int indiceElement;

	public IteratorMatrice(Matrice maMatrice) {
		this.matrice = maMatrice;
		this.indiceElement = -1;
		this.findNextElement();
	}

	private void findNextElement() {
		indiceElement++;
		while (this.hasNext())
			indiceElement++;

	}

	@Override
	public boolean hasNext() {
		return indiceElement < matrice.getNbColonnes();
	}

	@Override
	public Matrice next() {
		Matrice res = matrice.getColonne(indiceElement);
		this.findNextElement();
		return res;
	}

}

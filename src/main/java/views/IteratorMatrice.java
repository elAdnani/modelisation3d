package views;

import java.util.Iterator;

import modele.Matrice;
/**
 * 
 * Cette classe 
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 9 nov. 2021
 * @version XX
 */
public class IteratorMatrice implements Iterator<Matrice> {
	
	private Matrice matrice;
	private int idxElement;
	
	public IteratorMatrice(Matrice myList) {
		this.matrice = myList;
		this.idxElement = -1;
		this.findNextElement();
	}
	
	private void findNextElement() {
		idxElement++;
		while(this.hasNext() ) 
			idxElement++;
		
		
	}
	
	@Override
	public boolean hasNext() {
		return idxElement < matrice.getNbColonnes();
	}
	
	@Override
	public Matrice next() {
		Matrice res = matrice.getColonne(idxElement);
		this.findNextElement();
		return res;
	}
	

}

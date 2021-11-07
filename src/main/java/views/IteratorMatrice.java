package views;

import java.util.Iterator;

import modele.Matrice;

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

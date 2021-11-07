package views;

import java.util.Iterator;
import java.util.List;

public class Sup10Iterator implements Iterator<Double> {
	
	private List<Double> myList;
	private int idxElement;
	
	public Sup10Iterator(List<Double> myList) {
		this.myList = myList;
		this.idxElement = -1;
		this.findNextElement();
	}
	
	private void findNextElement() {
		idxElement++;
		while(this.hasNext() && myList.get(idxElement)>10 ) 
			idxElement++;
		
		
	}
	
	@Override
	public boolean hasNext() {
		return idxElement < myList.size();
	}
	
	@Override
	public Double next() {
		Double res = myList.get(idxElement);
		this.findNextElement();
		return res;
	}
	

}

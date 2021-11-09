package modele;

import java.util.ArrayList;
import java.util.List;

import util.ConnectableProperty;
import util.Subject;



public class Composition extends ConnectableProperty{
	
	
	public Composition(List<Face> faces, List<Point> points) {
		this.setValue( new Data(faces, points) );
	}
	
	public Composition() {
		this(new ArrayList<Face>(), new ArrayList<Point>());
	} 
	//////////////////////////// je regarde pour faire le update au tout début de la view
	
	@Override
	public void update(Subject other, Object value) {

		setData((Data)value);
	}

	/**
	 * 
	 * @return the faces
	 */
	
	public List<Face> getFaces() {
		return (( Data ) value).getFaces();
	}

	/**
	 * 
	 * @param faces the faces to set
	 */
	public void setData(Composition comp) {
		if(comp==null) {
			comp = new Composition();
		}
		setValue(new Data(comp.getFaces(), comp.getPoints()));
	}
	public void setData(Data donnee) {
		if(donnee==null) {
			donnee = new Data();
		}
		setValue(donnee);
	}


	/**
	 * 
	 * @return the points
	 */
	public List<Point> getPoints() {
		return (( Data ) value).getPoints();
	}


	@Override
	public String toString() {
		return "Composition ["+ value + "]";
	}
	
	
	
	
}

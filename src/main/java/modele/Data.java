package modele;

import java.util.ArrayList;
import java.util.List;

public class Data {
	private List<Face> faces;
	private List<Point> points;
	
	public Data(List<Face> faces, List<Point> points) {
		this.faces = faces;
		this.points = points;
	}

	public Data() {
		this.faces= new ArrayList<Face>();
		this.points= new ArrayList<Point>();
	}

	/**
	 * 
	 * @return the faces
	 */
	public List<Face> getFaces() {
		return faces;
	}

	/**
	 * 
	 * @param faces the faces to set
	 */
	public void setFaces(List<Face> faces) {
		this.faces = faces;
	}

	/**
	 * 
	 * @return the points
	 */
	public List<Point> getPoints() {
		return points;
	}

	/**
	 * 
	 * @param points the points to set
	 */
	public void setPoints(List<Point> points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "[faces=" + this.faces + ", points=" + this.points + "]";
	}
	
	
}

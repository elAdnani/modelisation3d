package modele;

import utils.Subject;

public class ProjectionRepresentation extends Subject{
	
	final protected Matrice POINTS;
	final protected FaceMatrice faces;
	
	public ProjectionRepresentation( Matrice points, FaceMatrice faces) {
		this.POINTS=points;
		this.faces=faces;
	}
	
	public void cadrage(double x, double y, double z) {
		POINTS.multiplication(Matrice.getCadrage(x, y, z));
	}	
	
	public void homothetie(double x) {
		POINTS.multiplication(Matrice.getHomothetie(x));
		
	}	
	
	public void translate(double x, double y, double z) {
		POINTS.multiplication(Matrice.getTranslation(x, y, z));
		
	}	

	public void rotation(double x) {
		POINTS.multiplication(Matrice.getRotation(x));
	}
}

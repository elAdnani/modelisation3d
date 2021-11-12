package modele;

import java.util.ArrayList;
import java.util.List;

public class Face {

	private List<Point> points;

	// List<Integer> reference; // indice de la matrice des listes de point
	// Matrice METTRE EN PARAMETRE

	public Face() {
		this(new ArrayList<>());
	}

	public Face(List<Point> points) {
		this.points = points;
	}

	public void add(Point p) {
		this.points.add(p);
	}

	public List<Point> getPoints() {
		return points;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[Face");
		for (Point p : points) {
			sb.append(p + "---");
		}
		sb.append("]");
		return sb.toString();
	}

	public double getAverageZ() {
		double sum = 0;
		for (Point p : points) {
			sum += p.getZ();
		}

		return sum / points.size();
	}

	public double getAverageY() {
		double sum = 0;
		for (Point p : points) {
			sum += p.getY();
		}

		return sum / points.size();
	}

	public double getAverageX() {
		double sum = 0;
		for (Point p : points) {
			sum += p.getX();
		}

		return sum / points.size();
	}

}
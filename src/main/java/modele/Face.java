package modele;

import java.util.ArrayList;
import java.util.List;

public class Face {

	private List<Point> lien;

	public Face() {
		this(new ArrayList<Point>());
	}

	public Face(List<Point> lien) {
		this.lien = lien;
	}

	public void add(Point p) {
		this.lien.add(p);
	}

	public List<Point> getPoints() {
		return lien;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[TRACE");
		for (Point p : lien) {
			sb.append(p + "---");
		}
		sb.append("]");
		return sb.toString();
	}

}
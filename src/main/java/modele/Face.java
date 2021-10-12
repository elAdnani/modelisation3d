package modele;

import java.util.ArrayList;
import java.util.List;

public class Face {

	private List<Point3D> lien;

	public Face() {
		this(new ArrayList<Point3D>());
	}

	public Face(List<Point3D> lien) {
		this.lien = lien;
	}

	public void add(Point3D p) {
		this.lien.add(p);
	}

	public List<Point3D> getPoints() {
		return lien;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[TRACE");
		for (Point3D p : lien) {
			sb.append(p + "---");
		}
		sb.append("]");
		return sb.toString();
	}

}
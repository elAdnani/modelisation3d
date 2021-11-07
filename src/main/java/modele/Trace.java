package modele;

import java.util.ArrayList;
import java.util.List;

public class Trace {

	private List<Point> lien;

	public Trace() {
		this(new ArrayList<Point>());
	}

	public Trace(List<Point> lien) {
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

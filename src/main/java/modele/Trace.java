package modele;

import java.util.ArrayList;
import java.util.List;

public class Trace {
	
	private List<Point> lien;
	public Trace() {
		this(new ArrayList<>());
	}
	
	public Trace(List<Point> lien) {
		this.lien=lien;
	}
	
	
	public void add(Point p) {
		this.lien.add(p);
	}


	
	public String toString() {
		StringBuilder sb = new StringBuilder("[TRACE");
		for(Point p : lien) {
			sb.append(p+"---");
		}
		sb.append("]");
		return sb.toString();
	}
	
}

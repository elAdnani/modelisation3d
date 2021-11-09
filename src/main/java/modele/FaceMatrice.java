package modele;

import java.util.ArrayList;
import java.util.List;

public class FaceMatrice {
	private List<Integer> indice;

	// List<Integer> reference; // indice de la matrice des listes de point
	// Matrice METTRE EN PARAMETRE

	public FaceMatrice() {
		this(null);
	}

	public FaceMatrice(List<Integer> listeIndice) {
		if (listeIndice == null) {
			listeIndice = new ArrayList<>();
		}
		this.indice = listeIndice;
	}

	public void add(int p) {
		this.indice.add(p);
	}

	/**
	 * Donne les points appartenant à la face sous forme de matrice.
	 * 
	 * @param m
	 * @return
	 */
	public List<Matrice> getPoints(Matrice m) {
		List<Matrice> res = new ArrayList<Matrice>();
		for (int i : indice) {
			res.add(m.getColonne(i));
		}
		return res;
	}

	public String toString(Matrice m) {
		StringBuilder sb = new StringBuilder("(");
		for (int i = 0; i < indice.size(); i++) {
			sb.append(m.lire(0, i) + ",");
			sb.append(m.lire(1, i) + ",");
			sb.append(m.lire(2, i));
		}
		sb.append(")");
		return sb.toString();
	}
}

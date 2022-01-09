package modele.geometrique;

/**
 * Cette classe sert à récupérer des figures géométriques sous forme de modele de fabrique.</br>
 * - point </br>
 * - vecteur </br>
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 7 janv. 2022
 */
public  class FigureFabric {
	/**
	 * Singleton
	 */
    private static FigureFabric instance;
    
    private FigureFabric() {}

    public static FigureFabric getInstance() {
        if (instance == null) {
            instance = new FigureFabric();
        }
        return instance;
    }
    
    
    public Vector vector(double cooX, double cooY, double cooZ) {
        return new Vector(cooX, cooY, cooZ);
    }
    
    public Vector vector() {
        return new Vector();
    }
    
    public Vertex vertex(double cooX, double cooY, double cooZ) {
        return new Vertex(cooX, cooY, cooZ);
    }

    public Vertex vertex() {
        return new Vertex();
    }


}

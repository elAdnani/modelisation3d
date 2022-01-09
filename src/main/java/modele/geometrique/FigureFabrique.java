package modele.geometrique;

/**
 * Cette classe sert à récupérer des figures géométriques sous forme de modele de fabrique.</br>
 * - point </br>
 * - vecteur </br>
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 7 janv. 2022
 */
public  class FigureFabrique {
	/**
	 * Singleton
	 */
    private static FigureFabrique instance;
    
    private FigureFabrique() {}

    public static FigureFabrique getInstance() {
        if (instance == null) {
            instance = new FigureFabrique();
        }
        return instance;
    }
    
    
    public Vecteur vector(double cooX, double cooY, double cooZ) {
        return new Vecteur(cooX, cooY, cooZ);
    }
    
    public Vecteur vector() {
        return new Vecteur();
    }
    
    public Point vertex(double cooX, double cooY, double cooZ) {
        return new Point(cooX, cooY, cooZ);
    }

    public Point vertex() {
        return new Point();
    }


}

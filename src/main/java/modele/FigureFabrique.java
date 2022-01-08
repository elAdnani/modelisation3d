package modele;

/**
 * Cette classe sert à récupérer des figures géométriques sous forme de modele de fabrique.</br>
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 7 janv. 2022
 */
public class FigureFabrique {
	/**
	 * Singleton
	 */
    private static FigureFabrique instance;

    public static FigureFabrique getInstance() {
        if (instance == null) {
            instance = new FigureFabrique();
        }
        return instance;
    }
    
    
    public Vecteur vecteur(double x, double y, double z) {
        return new Vecteur(x, y, z);
    }
    
    public Point point(double x, double y, double z) {
        return new Point(x, y, z);
    }

    private FigureFabrique() {
    }

}

package App;

import javafx.application.Application;
import javafx.stage.Stage;
import modele.Matrice;
//import modele.ProjectionRepresentation;
import views.Affichage;

public class Main extends Application{

	protected static double rotationDeMoitie= Math.PI /2;
	@Override
	public void start(Stage arg0) throws Exception {
		new Affichage().start(arg0);
		
		
		Matrice B;
		/*
		ProjectionRepresentation center = new ProjectionRepresentation(B);
		ProjectionRepresentation right = new ProjectionRepresentation(B.multiplication(Matrice.getRotation(rotationDeMoitie)));
		ProjectionRepresentation left = new ProjectionRepresentation(B.multiplication(Matrice.getRotation(-rotationDeMoitie)));
		
		
		center.biconnectTo(right);

		tempN.biconnectTo(tempF);
		new TextView( tempC);
		new SliderView( tempN);
		new TextView( tempF);
		 */
	}

}

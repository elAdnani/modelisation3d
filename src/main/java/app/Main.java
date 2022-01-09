package app;

import javafx.application.Application;
import javafx.stage.Stage;
import views.View;
/**
 * 
 * Cette classe sert à lancer le menu du jeu.
 *
 */
public class Main extends Application {

	@Override
	public void start(Stage arg0) throws Exception {
		new View();
	}
}
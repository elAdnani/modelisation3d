package controlleur;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Affichage {

	@FXML
	Canvas Canvas;
	
	
	GraphicsContext gc;
	
	
	public void initialize() {
		 gc=Canvas.getGraphicsContext2D();
	//	 gc.
	}
	
	
}

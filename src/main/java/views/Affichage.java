package views;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.Point;
import modele.RecuperationPly;
import modele.Trace;

public class Affichage extends Application {

	@FXML
	Canvas canvas;
	GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modélisateur 3D");

        
        /* CREATION DU MENU */
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("Fichier"); 
		Menu helpMenu = new Menu("Aide");
		
		menuBar = new MenuBar();
			
		MenuItem openFileItem= new MenuItem("Open File");
		MenuItem exitItem = new MenuItem("Exit");
		
		fileMenu.getItems().add(openFileItem);
		fileMenu.getItems().add(exitItem);

		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(helpMenu);
        
		
		/* CREATION DE LA FENETRE */
        VBox vBox = new VBox(menuBar);
        Scene scene = new Scene(vBox, 1300, 790);
        		
		canvas = new Canvas(1300,790);
		BorderPane root = new BorderPane(canvas);
		
		vBox.getChildren().addAll(canvas, root);
		
		
		/* AFFICHAGE DE LA FIGURE 3D */
		gc=canvas.getGraphicsContext2D();
		ArrayList<Point> points = (ArrayList<Point>) RecuperationPly.recuperationCoordonnee("/vache.ply");
		ArrayList<Trace> trace = (ArrayList<Trace>) RecuperationPly.recuperationTracerDesPoint("/vache.ply", points);
		double oldX =0 ,oldY =0;
		for (Trace t: trace) {
			 Iterator<Point> it = t.getPoints().iterator();
			 int cpt=0;
			 while(it.hasNext()) {
				 Point pt = it.next();
				 if(cpt==0) {
					oldX = pt.getX()*120+500;
					oldY = pt.getY()*120+500;
				 }
				 gc.strokeLine(oldX, oldY, pt.getX()*120+500,pt.getY()*120+500);
				 oldX = pt.getX()*120+500;
				 oldY = pt.getY()*120+500;
				 cpt++;
			 }
			
		 }
        
		
		/* AFFICHAGE */
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

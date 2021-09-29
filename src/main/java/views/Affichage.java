package views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import modele.*;

public class Affichage extends Stage{

	@FXML
	Canvas canvas;
	
	
	GraphicsContext gc;
	
	public Affichage() {
		 canvas = new Canvas(1000,1000);
		
		 gc=canvas.getGraphicsContext2D();
		 ArrayList<Point> points = (ArrayList<Point>) RecuperationPly.recuperationCoordonnee("/vache.ply");
		 ArrayList<Trace> trace = (ArrayList<Trace>) RecuperationPly.recuperationTracerDesPoint("/vache.ply", points);
		 double oldX =500 ,oldY =500;
		 for (Trace t: trace) {
//			 int i ;
//			 for(i=0; i< t.getPoints().size();i++) {
//				 if(i==0) {
//					 oldX=t.getPoints().get(i).getX()*120+500;
//					 oldY=t.getPoints().get(i).getY()*120+500;
//					 i++;
//				 }
//				 gc.strokeLine(oldX, oldY, t.getPoints().get(i).getX()*120+500, t.getPoints().get(i).getY()*120+500);
//				 oldX =t.getPoints().get(i).getX()*120+500;
//				 oldY = t.getPoints().get(i).getY()*120+500;
//			 }
			 Iterator<Point> it = t.getPoints().iterator();
			 int cpt=0;
			 while(it.hasNext()) {
				 Point pt = it.next();
				 if(cpt==0) {
					oldX =pt.getX()*120+500;
					oldY= pt.getY()*120+500;
				 }
				 gc.strokeLine(oldX, oldY, pt.getX()*120+500,pt.getY()*120+500);
				 oldX =pt.getX()*120+500;
				 oldY =pt.getY()*120+500;
				 cpt++;
			 }
			
			
			
		 }
		 Scene s = new Scene(new StackPane(canvas));
		 super.setScene(s);
		 super.show();
	}
	
	
	
}

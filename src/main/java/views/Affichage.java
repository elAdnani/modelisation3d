package views;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;
import modele.Face;
import modele.Point3D;
import modele.RecuperationPly;

public class Affichage extends Application {

	@FXML
	Canvas canvas;
	GraphicsContext gc;
	Point3D plan = new Point3D();
	String file;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Modélisateur 3D");

		file = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "cube.ply";
		/* CREATION DU MENU */
		MenuBar menuBar = new MenuBar();

		Menu fileMenu = new Menu("Fichier");
		Menu helpMenu = new Menu("Aide");

		menuBar = new MenuBar();

		MenuItem openFileItem = new MenuItem("Open File");
		MenuItem exitItem = new MenuItem("Exit");

		fileMenu.getItems().add(openFileItem);
		fileMenu.getItems().add(exitItem);

		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(helpMenu);

		/* INTERACTION AVEC LE MENU */
		openFileItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open File");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Ply File", "*.ply"),
						new ExtensionFilter("All Files", "*.*"));
				File file = fileChooser.showOpenDialog(null);

				if (file != null) {
					Affichage.this.file = file.getAbsolutePath();
					chargeFichier();
					affichagePly();
				}

			};
		});

		exitItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Platform.exit();
			};
		});

		/* CREATION DE LA FENETRE */
		VBox vBox = new VBox(menuBar);
		Scene scene = new Scene(vBox, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

		canvas = new Canvas(Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
		BorderPane root = new BorderPane(canvas);

		vBox.getChildren().addAll(canvas, root);

		chargeFichier();
		affichagePly();

		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				System.out.println(event.getEventType());

				String msg = "(x: " + event.getX() + ", y: " + event.getY() + ") -- ";
				System.out.println(msg);

				setZ((event.getX() - plan.getX()) + event.getSceneY() - plan.getY());
				setX(event.getX());

				affichagePly();

			}

		});

		/* AFFICHAGE DE LA FIGURE 3D */
		/*
		 * gc=canvas.getGraphicsContext2D(); ArrayList<Point3D> Point3Ds =
		 * (ArrayList<Point3D>) RecuperationPly.recuperationCoordonnee("/vache.ply");
		 * ArrayList<Trace> trace = (ArrayList<Trace>)
		 * RecuperationPly.recuperationTracerDesPoint3D("/vache.ply", Point3Ds); double oldX
		 * =0 ,oldY =0; for (Trace t: trace) { Iterator<Point3D> it =
		 * t.getPoint3Ds().iterator(); int cpt=0; while(it.hasNext()) { Point3D pt =
		 * it.next(); if(cpt==0) { oldX = pt.getX()*120+500; oldY = pt.getY()*120+500; }
		 * gc.strokeLine(oldX, oldY, pt.getX()*120+500,pt.getY()*120+500); oldX =
		 * pt.getX()*120+500; oldY = pt.getY()*120+500; cpt++; }
		 * 
		 * }
		 */

		/* AFFICHAGE */
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	ArrayList<Point3D> Point3Ds = null;
	ArrayList<Face> trace = null;
	
	private void chargeFichier() {
		try {
			Point3Ds = (ArrayList<Point3D>) RecuperationPly.recuperationCoordonnee(file);
			trace = (ArrayList<Face>) RecuperationPly.recuperationPlanDesPoints(file, Point3Ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Efface le canvas, lis le fichier et trace la figure
	 */
	private void affichagePly() {

		gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		double oldX = 0, oldY = 0;
		double X = 0;
		double Y = 0;
		double Z = 0;
		for (Face t : trace) {
			Iterator<Point3D> it = t.getPoints().iterator();
			int cpt = 0;
			while (it.hasNext()) {
				Point3D pt = it.next();
				Z = (pt.getZ() + plan.getZ());

				if (cpt == 0) {
					oldX = pt.getX() * 120 + 550 + plan.getX() * Z;
					oldY = pt.getY() * 120 + 550 + plan.getY();

				}

				X = pt.getX() * 120 + 550 + plan.getX() * Z;
				Y = (pt.getY() * 120 + 550 + plan.getY());

				gc.strokeLine(oldX, oldY, X, Y);

				oldX = X;
				oldY = Y;
				cpt++;
			}

		}

	}

	// Controller du plan
	protected void setX(double value) {
		this.plan.setX(value);
	}

	public void setY(double value) {
		this.plan.setX(value);
	}

	public void setZ(double value) {
		this.plan.setX(value);
	}

}

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
import javafx.stage.Stage;
import modele.Plan;
import modele.Point;
import modele.RecuperationPly;
import modele.Trace;

public class Affichage extends Application {

	@FXML
	Canvas canvas;
	GraphicsContext gc;
	Plan plan = new Plan();
	String file;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Modélisateur 3D");

		file = "cube.ply";
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
					Affichage.this.file = file.getName();
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
		Scene scene = new Scene(vBox, 1300, 790);

		canvas = new Canvas(1300, 1300);
		BorderPane root = new BorderPane(canvas);

		vBox.getChildren().addAll(canvas, root);

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
		 * gc=canvas.getGraphicsContext2D(); ArrayList<Point> points =
		 * (ArrayList<Point>) RecuperationPly.recuperationCoordonnee("/vache.ply");
		 * ArrayList<Trace> trace = (ArrayList<Trace>)
		 * RecuperationPly.recuperationTracerDesPoint("/vache.ply", points); double oldX
		 * =0 ,oldY =0; for (Trace t: trace) { Iterator<Point> it =
		 * t.getPoints().iterator(); int cpt=0; while(it.hasNext()) { Point pt =
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

	protected void affichagePly() {

		gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, 1300, 1300);
		ArrayList<Point> points = null;
		ArrayList<Trace> trace = null;
		try {
			points = (ArrayList<Point>) RecuperationPly.recuperationCoordonnee(file);
			trace = (ArrayList<Trace>) RecuperationPly.recuperationTracerDesPoint(file, points);
			System.out.println(trace);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double oldX = 0, oldY = 0;
		double X = 0;
		double Y = 0;
		double Z = 0;
		for (Trace t : trace) {
			Iterator<Point> it = t.getPoints().iterator();
			int cpt = 0;
			while (it.hasNext()) {
				Point pt = it.next();
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

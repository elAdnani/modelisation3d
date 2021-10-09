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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;
import modele.Plan;
import modele.Point;
import modele.RecuperationPly;
import modele.Trace;

public class Affichage extends Application {

	private static final int DEGREE_DE_ZOOM = 100;
	@FXML
	Canvas canvas;
	GraphicsContext gc;
	Plan plan = new Plan();
	String file;
	ArrayList<Point> points = null;
	ArrayList<Trace> trace = null;
	double oldMouseX = 0;
	double oldMouseY = 0;
	double theta = toRadian(1);

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Modélisateur 3D");

		file = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "vache.ply";
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
		Scene scene = new Scene(vBox, Screen.getPrimary().getBounds().getWidth(),
				Screen.getPrimary().getBounds().getHeight());

		canvas = new Canvas(Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
		BorderPane root = new BorderPane(canvas);

		vBox.getChildren().addAll(canvas, root);

		chargeFichier();

		affichagePly();

		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {

				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();
				rotate3DX(toRadian(mouseY - oldMouseY));
				rotate3DY(toRadian(mouseX - oldMouseX));
				oldMouseX = mouseX;
				oldMouseY = mouseY;

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
		scene.setOnKeyPressed(e -> {
			System.out.println(e.getCode().toString());
			System.out.println(theta);
			if (e.getCode() == KeyCode.RIGHT) {
				rotate3DY(theta);
				affichagePly();
			}
			if (e.getCode() == KeyCode.LEFT) {
				rotate3DY(-theta);
				affichagePly();
			
			}
			if (e.getCode() == KeyCode.UP) {
				rotate3DX(theta);
				affichagePly();
			}
			if (e.getCode() == KeyCode.DOWN) {
				rotate3DX(-theta);
				affichagePly();
			
			}
		});
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void chargeFichier() {
		try {
			points = (ArrayList<Point>) RecuperationPly.recuperationCoordonnee(file);
			trace = (ArrayList<Trace>) RecuperationPly.recuperationTracerDesPoint(file, points);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Efface le canvas, lis le fichier et trace la figure
	 * 
	 */

	private void affichagePly() {

		gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		double middle_screen_x = canvas.getWidth() / 2;
		double middle_screen_y = canvas.getHeight() / 2;
		double oldX = 0, oldY = 0;
		double X = 0;
		double Y = 0;
		double Z = 0;

		gc.setStroke(Color.RED);
		gc.strokeLine(middle_screen_x, 0, middle_screen_x, canvas.getHeight());
		gc.setStroke(Color.GREEN);
		gc.strokeLine(0, middle_screen_y, canvas.getWidth(), middle_screen_y);
		gc.setStroke(Color.BLACK);
		
		for (Trace t : trace) {
			Iterator<Point> it = t.getPoints().iterator();
			int cpt = 0;
			while (it.hasNext()) {
				Point pt = it.next();

				if (cpt == 0) {
					oldX = pt.getX() * DEGREE_DE_ZOOM + middle_screen_x;
					oldY = pt.getY() * DEGREE_DE_ZOOM + middle_screen_y;

				}

				X = pt.getX() * DEGREE_DE_ZOOM + middle_screen_x;
				Y = pt.getY() * DEGREE_DE_ZOOM + middle_screen_y;

				gc.strokeLine(oldX, oldY, X, Y);

				oldX = X;
				oldY = Y;
				cpt++;
			}

		}

	}

	private void rotate3DX(double tetha) {
		double sinTheta = Math.sin(tetha);
		double cosTheta = Math.cos(tetha);
		for (Point p : points) {
			double newY = (p.getY() * cosTheta) - (p.getZ() * sinTheta);
			double newZ = (p.getY() * sinTheta) + (p.getZ() * cosTheta);
			p.setY(newY);
			p.setZ(newZ);
		}

	}

	private void rotate3DY(double tetha) {
		double sinTheta = Math.sin(tetha);
		double cosTheta = Math.cos(tetha);
		for (Point p : points) {
			double newX = (p.getX() * cosTheta) + (p.getZ() * sinTheta);
			double newZ = (p.getZ() * cosTheta) - (p.getX()*sinTheta);
			p.setX(newX);
			p.setZ(newZ);
		}

	}

	private void rotate3DZ(double ztetha) {

	}
	
	private double toRadian(double degree) {
		return degree * Math.PI/180;
	}

}

package views;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
		openFileItem.setOnAction( new EventHandler<ActionEvent>() {
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

		/* CREATION DES OUTILS */
		VBox outils = new VBox();
		outils.addEventFilter(KeyEvent.KEY_PRESSED, ek ->{ 
			if(ek.getCode() == KeyCode.UP || ek.getCode() == KeyCode.DOWN || ek.getCode() == KeyCode.RIGHT || ek.getCode() == KeyCode.LEFT)
				ek.consume();					
		});
		Label nom = new Label("Menu");
		Label nomZoom = new Label ("Zoom");
		Button face = new Button("Vue de face");
		Button droite = new Button("Vue de droite");
		Button dessus = new Button("Vue de haut");
		Button up = new Button("↑");
		Button down = new Button("↓");
		Button right = new Button("→");
		Button left = new Button("←");
		Button plus = new Button("+");
		Button moins = new Button("-");
		Slider zoomSlider = new Slider();

		zoomSlider.setMin(0);
		zoomSlider.setMax(1000);
		zoomSlider.setShowTickLabels(true);
		
		VBox position = new VBox(4);
		position.getChildren().addAll(face, droite, dessus);
		VBox.setMargin(dessus, new Insets(0,0,90,0));
		
		HBox hbUp = new HBox();
		hbUp.getChildren().add(up);
		HBox.setMargin(up, new Insets(0,0,5,106));

		HBox leftRight = new HBox(60);
		leftRight.getChildren().addAll(left, right);
		HBox.setMargin(left, new Insets(0,0,0,45));
		
		HBox hbDown = new HBox();
		hbDown.getChildren().add(down);
		HBox.setMargin(down, new Insets(5,0,90,106));

		HBox zoom = new HBox(5);
		zoom.getChildren().addAll(moins, zoomSlider, plus);
		HBox.setMargin(moins, new Insets(0,0,0,50));
				
		outils.getChildren().addAll(nom, position, hbUp, leftRight, hbDown, nomZoom, zoom);
		
		nom.setPadding(new Insets(10,10,30,115));
		nomZoom.setPadding(new Insets(10,10,30,120));

		face.setPadding(new Insets(20,100,20,100));
		droite.setPadding(new Insets(20,90,20,100));
		dessus.setPadding(new Insets(20,98,20,100));
		
		up.setPadding(new Insets(20,27,20,27));
		down.setPadding(new Insets(20,27,20,27));
		right.setPadding(new Insets(20,25,20,25));
		left.setPadding(new Insets(20,25,20,25));
		
		plus.setPadding(new Insets(3,5,3,5));
		moins.setPadding(new Insets(3,8,3,8));

		right.setOnAction(e ->{
			rotate3DY(theta*4);
			affichagePly();
		});
		
		left.setOnAction (e ->{
			rotate3DY(-theta*4);
			affichagePly();
		});
		
		up.setOnAction (e ->{
			rotate3DX(theta*4);
			affichagePly();
		});
		
		down.setOnAction (e ->{
			rotate3DX(-theta*4);
			affichagePly();
		});
		
		face.setOnAction(e ->{
			chargeFichier();
			affichagePly();
		});
		
		/* CREATION DE LA FENETRE */
		VBox vBox = new VBox(menuBar);
		Scene scene = new Scene(vBox, 1500, 790);
				//new Scene(vBox, Screen.getPrimary().getBounds().getWidth(),
				//Screen.getPrimary().getBounds().getHeight());
		canvas = new Canvas(1200,790);
				//new Canvas(Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());		
		
		/* INITIALISATION DU BORDERPANE */
		BorderPane root = new BorderPane();
		BorderPane.setMargin(outils, new Insets(0,10,10,0));
		root.setLeft(canvas);
		root.setRight(outils);
		vBox.getChildren().addAll(root);

		chargeFichier();
		affichagePly();
		
		
		/* DEPLACEMENT SOURIS */
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
		/*scene.setOnKeyPressed(e -> {
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
		});*/
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
		    public void handle(KeyEvent e) {
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

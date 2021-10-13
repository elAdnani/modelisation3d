package views;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;
import modele.Point;
import modele.RecuperationPly;
import modele.Trace;

public class Affichage extends Application {

	private static double DEGREE_DE_ZOOM = 500;
	private double theta = toRadian(1); // Angle de rotation en radian
	
	private ArrayList<Point> points = null; // Liste des points
	private ArrayList<Trace> trace = null; // Liste des faces

	private double offSetY; // Décalage sur l'axe Y
	private double offSetX; // Décalage sur l'axe X

	Canvas canvas;
	GraphicsContext gc;
	String file;
	double oldMouseX = 0;
	double oldMouseY = 0;
	double amplitude = 1;
	double amplitudeX = 1;
	double amplitudeY = 1;


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
		Scene scene = new Scene(vBox, Screen.getPrimary().getBounds().getWidth(),
				Screen.getPrimary().getBounds().getHeight());

		canvas = new Canvas(Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
		canvas.setOnScroll(new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				double deltaY = event.getDeltaY();
				System.out.println("deltaY = " + deltaY);
				System.out.println("DEGREE_DE_ZOOM = " + DEGREE_DE_ZOOM);
				if (deltaY < 0 && DEGREE_DE_ZOOM > 0) {
					DEGREE_DE_ZOOM -= 15;
					
				} else {
					DEGREE_DE_ZOOM += 15;
					
				}
				affichagePly();
			}

		});
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
//			System.out.println(e.getCode().toString());
//			System.out.println(theta);
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
			if (e.getCode() == KeyCode.ADD) {
				DEGREE_DE_ZOOM += 0.1;
				affichagePly();
			}
			if (e.getCode() == KeyCode.SUBTRACT) {
				DEGREE_DE_ZOOM -= 0.1;
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
			centerCoord = getCenter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sortByZ();
	}

	// TODO A supprimer si ne marche pas
	double[] centerCoord;

	/**
	 * Efface le canvas, lis le fichier et trace la figure
	 * 
	 */

	private void affichagePly() {
//		MergeSort sort = new MergeSort(points);
//		sort.sortGivenArray();
//		this.points = sort.getSortedArray();
		double middle_screen_x = canvas.getWidth() / 2;
		double middle_screen_y = canvas.getHeight() / 2;
		double oldX = 0, oldY = 0;
		double X = 0;
		double Y = 0;
		double Z = 0;
		double[] Xcoord = new double[4];
		double[] Ycoord = new double[4];
		long start = System.nanoTime();
		
		offSetX = middle_screen_x;
		offSetY = middle_screen_y;
		
		gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		gc.setStroke(Color.RED);
		gc.strokeLine(middle_screen_x, 0, middle_screen_x, canvas.getHeight());
		gc.setStroke(Color.GREEN);
		gc.strokeLine(0, middle_screen_y, canvas.getWidth(), middle_screen_y);
		
		gc.setStroke(Color.BLACK);
		gc.setFill(Color.GRAY);
		System.out.println("Drawing started...");
		for (Trace t : trace) {
			Iterator<Point> it = t.getPoints().iterator();
			int cpt = 0;
			while (it.hasNext()) {
				Point pt = it.next();

				if (cpt == 0) {
					oldX = ((pt.getX() - centerCoord[0]) / amplitude) * DEGREE_DE_ZOOM + offSetX;
					oldY = ((pt.getY() - centerCoord[1]) / amplitude) * DEGREE_DE_ZOOM + offSetY;
//					oldX = (pt.getX()) * DEGREE_DE_ZOOM + offSetX;
//					oldY = (pt.getY()) * DEGREE_DE_ZOOM + offSetY;
//					gc.setStroke(Color.BLUE);
//					gc.strokeOval((pt.getX() * DEGREE_DE_ZOOM + offSetX),(pt.getY() * DEGREE_DE_ZOOM + offSetY), 1, 1);
//					gc.setStroke(Color.BLACK);
				}

				X = ((pt.getX() - centerCoord[0]) / amplitude) * DEGREE_DE_ZOOM + offSetX;
				Y = ((pt.getY() - centerCoord[1]) / amplitude) * DEGREE_DE_ZOOM + offSetY;

//				X = (pt.getX()) * DEGREE_DE_ZOOM + offSetX;
//				Y = (pt.getY()) * DEGREE_DE_ZOOM + offSetY;

				Xcoord[cpt]= X;
				Ycoord[cpt]=Y;

//				gc.strokeLine(oldX, oldY, X, Y);

				oldX = X;
				oldY = Y;
				cpt++;
			}
			gc.strokePolygon(Xcoord, Ycoord, t.getPoints().size());
			gc.fillPolygon(Xcoord, Ycoord, t.getPoints().size());

		}
		long end = System.nanoTime();
		System.out.println("Drawing done in " + (end-start) + " nanosecondes ("+ (end-start)/1_000_000.0 +" ms)");
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
		sortByZ();

	}

	private void rotate3DY(double tetha) {
		double sinTheta = Math.sin(tetha);
		double cosTheta = Math.cos(tetha);
		for (Point p : points) {
			double newX = (p.getX() * cosTheta) + (p.getZ() * sinTheta);
			double newZ = (p.getZ() * cosTheta) - (p.getX() * sinTheta);
			p.setX(newX);
			p.setZ(newZ);
		}
		sortByZ();

	}

	private void rotate3DZ(double tetha) {

	}

	private double[] getCenter() {
		double[] centerCoord = new double[3];
		Double xMin = null;
		Double xMax = null;
		Double yMin = null;
		Double yMax = null;
		Double zMin = null;
		Double zMax = null;

		for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
			Point point = iterator.next();
			double currX = point.getX();
			double currY = point.getY();
			double currZ = point.getZ();
			if (xMin == null || currX < xMin)
				xMin = currX;
			if (xMax == null || currX > xMax)
				xMax = currX;
			if (yMin == null || currY < yMin)
				yMin = currY;
			if (yMax == null || currY > yMax)
				yMax = currY;
			if (zMin == null || currZ < zMin)
				zMin = currZ;
			if (zMax == null || currZ > zMax)
				zMax = currZ;
		}

		Double ampX = xMax - xMin;
		Double ampY = yMax - yMin;
		Double ampZ = zMax - zMin;
		if (ampX > ampY)
			amplitude = ampX;
		else
			amplitude = ampY;

		amplitudeX = ampX;
		amplitudeY = ampY;
		System.out.println("ampX : " + ampX + "; ampY : " + ampY + "; amplitude : " + amplitude);

		centerCoord[0] = (xMax + xMin) / 2;
		centerCoord[1] = (yMax + yMin) / 2;
		centerCoord[2] = (zMax + zMin) / 2;
		System.out.println("xMax : " + xMax + "; xMin : " + xMin + "; MilieuX : " + centerCoord[0]);
		System.out.println("yMax : " + yMax + "; yMin : " + yMin + "; MilieuY : " + centerCoord[1]);
		System.out.println("zMax : " + zMax + "; zMin : " + zMin + "; MilieuZ : " + centerCoord[2]);
		return centerCoord;
	}

	private double toRadian(double degree) {
		return degree * Math.PI / 180;
	}

	private void sortByZ() {
		long start = System.nanoTime();
		System.out.println("Sorting started...");
		Collections.sort(trace, new Comparator<Trace>() {
			@Override
			public int compare(Trace o1, Trace o2) {
				return getAverageZ(o1) - getAverageZ(o2) < 0 ? 1 : -1;
			}
		});
		long end = System.nanoTime();
		System.out.println("Sorting done in " + (end-start) + " nanoseconds (" + (end-start)/1_000_000.0 +" ms)");
	}
	
	private static double getAverageZ(Trace t) {
		double sum = 0;
		for ( Point p : t.getPoints())
		{
			sum += p.getZ();
		}

		return sum / t.getPoints().size();
	}
	
	private String numberWithSpaces(Double x) {
	    String[] parts = x.toString().split(".");
parts[0] = parts[0].replace("/\\B(?=(\\d{3})+(?!\\d))/g", " ");
	    return String.join(".", parts);
	}
	
}

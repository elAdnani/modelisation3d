package views;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
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

	private static double DEGREE_DE_ZOOM = 100;
	private double theta = toRadian(1); // Angle de rotation en radian

	private ArrayList<Point> points = null; // Liste des points
	private ArrayList<Trace> trace = null; // Liste des faces

	private double offSetY; // Décalage sur l'axe Y
	private double offSetX; // Décalage sur l'axe X

	/*
	 * TESTS
	 */
	private final long[] frameTimes = new long[100];
	private int frameTimeIndex = 0;
	private boolean arrayFilled = false;

	Canvas canvas;
	GraphicsContext gc;
	String file;
	double oldMouseX = 0;
	double oldMouseY = 0;
	double amplitude = 1;
	double amplitudeX = 1;
	double amplitudeY = 1;
	double amplitudeZ = 1;

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
					DEGREE_DE_ZOOM -= 0.1;
					affichagePly();
				} else {
					DEGREE_DE_ZOOM += 0.1;
					affichagePly();
				}
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

		/**
		 * TEST FRAME RATE
		 */

		AnimationTimer frameRateMeter = new AnimationTimer() {

			@Override
			public void handle(long now) {
				long oldFrameTime = frameTimes[frameTimeIndex];
				frameTimes[frameTimeIndex] = now;
				frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
				if (frameTimeIndex == 0) {
					arrayFilled = true;
				}
				if (arrayFilled) {
					long elapsedNanos = now - oldFrameTime;
					long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
					double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
					canvas.getGraphicsContext2D().clearRect(canvas.getWidth() - 150, 0, 150, 10);
					canvas.getGraphicsContext2D().fillText(String.format("Current frame rate: %.3f", frameRate),
							canvas.getWidth() - 150, 10);
				}
			}
		};

		frameRateMeter.start();

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

	// Permet de centrer un object
	private void test() {
		double length = Math.pow(centerCoord[0] * 2, 2);
		double height = Math.pow(centerCoord[1] * 2, 2);
		double breadth = Math.pow(centerCoord[2] * 2, 2);
		double diagonal = Math.sqrt(length + height + breadth);
		for (Point p : points) {
			p.setX((p.getX() - centerCoord[0]) / (amplitude));
			p.setY((p.getY() - centerCoord[1]) / (amplitude));
			p.setZ((p.getZ() - centerCoord[2]) / (amplitude));
		}
	}

	private void chargeFichier() {
		try {
			points = (ArrayList<Point>) RecuperationPly.recuperationCoordonnee(file);
			trace = (ArrayList<Trace>) RecuperationPly.recuperationTracerDesPoint(file, points);
			centerCoord = getCenter();
			test();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sortByZ();
		calculateAutoScale();
		affichagePly();
	}

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

//		centerCoord = getCenter();

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

//				if (cpt == 0) {
//					oldX = ((pt.getX() - centerCoord[0]) / amplitude) * DEGREE_DE_ZOOM + offSetX;
//					oldY = ((pt.getY() - centerCoord[1]) / amplitude) * DEGREE_DE_ZOOM + offSetY;
//					oldX = (pt.getX()) * DEGREE_DE_ZOOM + offSetX;
//					oldY = (pt.getY()) * DEGREE_DE_ZOOM + offSetY;
//					gc.setStroke(Color.BLUE);
//					gc.strokeOval((pt.getX() * DEGREE_DE_ZOOM + offSetX),(pt.getY() * DEGREE_DE_ZOOM + offSetY), 1, 1);
//					gc.setStroke(Color.BLACK);
//				}

				X = (pt.getX() * DEGREE_DE_ZOOM) + offSetX;
				Y = (pt.getY() * DEGREE_DE_ZOOM) + offSetY;

//				gc.setFill(Color.BLACK);
//				gc.fillText("(" + pt.getX() + "," + pt.getY() + "," + pt.getZ() + ")", X, Y);
//				gc.setFill(Color.GRAY);
//				X = (pt.getX()) * DEGREE_DE_ZOOM + offSetX;
//				Y = (pt.getY()) * DEGREE_DE_ZOOM + offSetY;

				Xcoord[cpt] = X;
				Ycoord[cpt] = Y;

//				gc.strokeLine(oldX, oldY, X, Y);

				oldX = X;
				oldY = Y;
				cpt++;
			}
			gc.strokePolygon(Xcoord, Ycoord, t.getPoints().size());
			gc.fillPolygon(Xcoord, Ycoord, t.getPoints().size());
		}
		long end = System.nanoTime();
		System.out
				.println("Drawing done in " + (end - start) + " nanosecondes (" + (end - start) / 1_000_000.0 + " ms)");
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

		amplitude = Math.max(ampX, Math.max(ampY, ampZ));

		amplitudeX = ampX;
		amplitudeY = ampY;
		amplitudeZ = ampZ;
		System.out.println("ampX : " + ampX + "; ampY : " + ampY + "; ampZ : " + ampZ + " amplitude : " + amplitude);

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
		System.out
				.println("Sorting done in " + (end - start) + " nanoseconds (" + (end - start) / 1_000_000.0 + " ms)");
	}

	private static double getAverageZ(Trace t) {
		double sum = 0;
		for (Point p : t.getPoints()) {
			sum += p.getZ();
		}

		return sum / t.getPoints().size();
	}

	private void calculateAutoScale() {
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

		Double xLength = Math.abs(xMax - xMin);
		Double yLength = Math.abs(yMax - yMin);
		Double zLength = Math.abs(zMax - zMin);
		
		Double canvasWidth = canvas.getWidth();
		Double canvasHeight = canvas.getHeight();
		
		Double smallestSize = Math.min(canvasWidth, canvasHeight);

		System.out.println("xMax " + xMax + " - xMin " + xMin + " = xLength " + xLength);
		System.out.println("yMax " + yMax + " - yMin " + yMin + " = yLength " + yLength);
		System.out.println("zMax " + zMax + " - zMin " + zMin + " = zLength " + zLength);

		Double xZoom = (smallestSize - 100) / xLength;
		Double yZoom = (smallestSize - 100) / yLength;
		Double zZoom = (smallestSize - 100) / zLength;
//		System.out.println("CanvasWidth " + (canvas.getWidth() - 100) + "/ xLength " + xLength + " = xZoom "
//				+ xZoom);
//		System.out.println("CanvasHeight " + (canvas.getHeight() - 100) + "/ yLength " + yLength + " = yZoom "
//				+ yZoom);
		
		DEGREE_DE_ZOOM = Math.min(xZoom, Math.min(yZoom, zZoom));
	}

}

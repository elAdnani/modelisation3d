package views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import modele.Model;
import util.Axis;
import util.DrawingMethod;

public class Affichage {

	private Model model = null;
	private Canvas canvas = null;
	private Axis axis = Axis.ZAXIS;

	public Double zoom = null; // Calcul
	public static double theta = toRadian(1); // Angle de rotation en radian // Calcul

	// private final long[] frameTimes = new long[100]; // Interface
//	private int frameTimeIndex = 0; // Interface
//	private boolean arrayFilled = false; // Interface

	public Affichage(Canvas canvas) {
		this.zoom = 100.0;
		this.canvas = canvas;
		drawAxis();
	}

	public void loadFile(String path) {
		if (this.model == null)
			model = new Model();
		this.model.loadFile(path);
	}

	/**
	 * Efface le canvas, lis le fichier et trace la figure
	 * 
	 */
	public void drawModel(DrawingMethod method) {
		if (model == null)
			return;
		clearCanvas();
		drawAxis();
		this.model.sortPoints(axis);
		this.model.draw(canvas, method, zoom);
	}

	public void drawAxis() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.RED);
		gc.strokeLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight());
		gc.setStroke(Color.GREEN);
		gc.strokeLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2);
		gc.setStroke(Color.BLACK);
	}

	public void rotateModel(Axis axis, double theta) {
		this.model.rotate(axis, toRadian(theta));
	}

	// Calcul
	private static double toRadian(double degree) {
		return degree * Math.PI / 180;
	}

	public void clearCanvas() {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	/**
	 * GETTERS AND SETTERS
	 */

	/**
	 * Return the canvas of the Affichage
	 * 
	 * @return canvas of the Affichage
	 */
	public Canvas getCanvas() {
		return this.canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
}
package views;

import connectable.ConnectableProperty;
import connectable.Observer;
import connectable.Subject; 
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import modele.Model;
import util.Axis;
import util.DrawingMethod;

public class Affichage extends ConnectableProperty {

	private Model model = null;
	private Canvas canvas = null;
	private View view = null;
	private Axis axis = Axis.ZAXIS;

	public Double zoom = null; // Calcul
	public static double theta = toRadian(1); // Angle de rotation en radian // Calcul

	// private final long[] frameTimes = new long[100]; // Interface
//	private int frameTimeIndex = 0; // Interface
//	private boolean arrayFilled = false; // Interface

	public Affichage(View view) {
		this(view, Axis.ZAXIS);
	}

	public Affichage(View view, Axis axis) {
		this.zoom = 100.0;
		this.view = view;
		this.canvas = view.getCanvas();
		this.axis = axis;
	}

	public void loadFile(String path) {
		if (this.model == null)
			model = new Model();
		this.model.loadFile(path);
		this.view.zoomSlider.setValue(zoom/100);
//		this.zoom = this.model.calculateAutoScale(canvas);
	}

	/**
	 * Efface le canvas, lis le fichier et trace la figure
	 * 
	 */
	public void drawModel(DrawingMethod method) {
		if (model == null)
			return;
		clearCanvas();
//		drawAxis();
		this.model.sortPoints(this.axis);
		this.model.draw(canvas, this.axis, zoom, method);
		System.out.println(this.attached.size());
		notifyObservers();
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
		if (this.model == null)
			return;
		this.model.rotate(axis, toRadian(theta));
	}

	public void translateModel(Axis axis, double distance) {
		if (this.model == null)
			return;
		this.model.translate(axis, distance);
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
		if (!propagating) {
			propagating = true;
			this.zoom = zoom;
			for (Observer obs : attached) {
				Affichage af = (Affichage) obs;
				af.setZoom(zoom);
				af.view.zoomSlider.setValue(zoom / 100);
			}
			propagating = false;
		}
	}

	public Axis getAxis() {
		return axis;
	}

	public void setAxis(Axis axis) {
		this.axis = axis;
	}

	@Override
	public void setValue(Object val) {
		if (val == null)
			return;
		if (!propagating) {
			propagating = true;
			this.model = new Model();
			this.model.copy((Model) val);
			drawModel(view.getDrawMethod());
			propagating = false;
		}
	}

	@Override
	public void update(Subject subject) {
		setValue(((Affichage) subject).getModel());
	}

	public void updateTheme(String theme) {
		if (!propagating) {
			propagating = true;
			for (Observer obs : attached) {
				Affichage af = (Affichage) obs;
				af.view.changeTheme(theme);
			}
			propagating = false;
		}
	}
}

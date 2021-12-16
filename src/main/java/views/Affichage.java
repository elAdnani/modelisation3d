package views;

import java.io.FileNotFoundException;

import connectable.ConnectableProperty;
import connectable.Observer;
import connectable.Subject;
import javafx.scene.canvas.Canvas;
import modele.Model;
import ply.exceptions.FormatPlyException;
import util.Axis;
import util.Theme;

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
		// TODO attach de la ou des vues au modèle
	}

	public void loadLight() {

	}

	public void loadFile(String path) throws FileNotFoundException, FormatPlyException {
		if (this.model == null)
			model = new Model();
		if (path != null) {
			this.model.loadFile(path);
			this.view.zoomSlider.setValue(zoom / 100);
			notifyObservers();
		}

//		this.zoom = this.model.calculateAutoScale(canvas);
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
			this.model = (Model)val;
			view.drawModel();
			propagating = false;
		}
	}

	@Override
	public void update(Subject subject) {
		setValue(((Affichage) subject).getModel());
	}

	public void updateTheme(Theme theme) {
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

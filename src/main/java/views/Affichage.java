package views;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import connectable.ConnectableProperty;
import connectable.Observer;
import connectable.Subject;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import modele.Face;
import modele.Model;
import modele.Point;
import ply.exceptions.FormatPlyException;
import util.Axis;
import util.DrawingMethod;
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
			drawModel(view.getDrawMethod());
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

	/**
	 * Efface le canvas, lis le fichier et trace la figure
	 * 
	 */
	public void drawModel(DrawingMethod method) {
		if (model == null)
			return;
		clearCanvas();
		model.sortPoints(this.axis);
		draw(canvas, this.axis, zoom, method);
		System.out.println(this.attached.size());
		notifyObservers();
	}

	/**
	 * Draw the model onto a given canvas
	 * 
	 * @param canvas - The canvas to draw the model
	 */
	public void draw(Canvas canvas, Axis axis, double zoom, DrawingMethod method) {

		long start = System.nanoTime();
		System.out.println("Drawing started...");

		switch (method) {
		case WIREFRAME:
			drawWireframe(canvas, axis, zoom);
			break;
		case SOLID:
			drawSolid(canvas, axis, zoom);
			break;
		case BOTH:
			drawBoth(canvas, axis, zoom);
			break;
		default:
			drawWireframe(canvas, axis, zoom);
			break;
		}

		long end = System.nanoTime();
		System.out
				.println("Drawing done in " + (end - start) + " nanosecondes (" + (end - start) / 1_000_000.0 + " ms)");

	}

	private void drawWireframe(Canvas canvas, Axis axis, double zoom) {
		double middlescreenx = canvas.getWidth() / 2;
		double middlescreeny = canvas.getHeight() / 2;

		double x = 0;
		double y = 0;
		double z = 0;

		double[] xCoord = null;
		double[] yCoord = null;
		double[] zCoord = null;

		double offSetX;
		double offSetY;
		double offSetZ;

		double offsetX = model.getOffsetX();
		double offsetY = model.getOffsetY();
		double offsetZ = model.getOffsetZ();

		List<Face> faces = model.getFaces();

		GraphicsContext gc = canvas.getGraphicsContext2D();

		switch (axis) {
		case XAXIS:
			offSetZ = middlescreenx + offsetZ;
			offSetY = middlescreeny + offsetY;
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				zCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					z = (pt.getZ() * zoom) + offSetZ;
					y = (pt.getY() * zoom) + offSetY;

					zCoord[cpt] = z;
					yCoord[cpt] = y;

					cpt++;
				}
				gc.strokePolygon(zCoord, yCoord, nbPoints);
				System.out.println(canvas.getGraphicsContext2D().getStroke());
			}
			break;
		case YAXIS:
			offSetZ = middlescreeny + offsetZ;
			offSetX = middlescreenx + offsetX;
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				zCoord = new double[nbPoints];
				xCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					z = (pt.getZ() * zoom) + offSetZ;
					x = (pt.getX() * zoom) + offSetX;

					zCoord[cpt] = z;
					xCoord[cpt] = x;

					cpt++;
				}
				gc.strokePolygon(xCoord, zCoord, nbPoints);
				System.out.println(canvas.getGraphicsContext2D().getStroke());
			}
			break;
		case ZAXIS:
			offSetX = middlescreenx + offsetX;
			offSetY = middlescreeny + offsetY;
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				xCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					x = (pt.getX() * zoom) + offSetX;
					y = (pt.getY() * zoom) + offSetY;

					xCoord[cpt] = x;
					yCoord[cpt] = y;

					cpt++;
				}
				gc.strokePolygon(xCoord, yCoord, nbPoints);
				System.out.println(canvas.getGraphicsContext2D().getStroke());
			}
			break;
		}
	}

	private void drawSolid(Canvas canvas, Axis axis, double zoom) {
		double middlescreenx = canvas.getWidth() / 2;
		double middlescreeny = canvas.getHeight() / 2;

		double x = 0;
		double y = 0;
		double z = 0;

		double[] xCoord = null;
		double[] yCoord = null;
		double[] zCoord = null;

		double offSetX;
		double offSetY;
		double offSetZ;

		double offsetX = model.getOffsetX();
		double offsetY = model.getOffsetY();
		double offsetZ = model.getOffsetZ();

		List<Face> faces = model.getFaces();

		GraphicsContext gc = canvas.getGraphicsContext2D();

		switch (axis) {
		case XAXIS:
			offSetZ = middlescreenx + offsetZ;
			offSetY = middlescreeny + offsetY;
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				zCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					z = (pt.getZ() * zoom) + offSetZ;
					y = (pt.getY() * zoom) + offSetY;

					zCoord[cpt] = z;
					yCoord[cpt] = y;

					cpt++;
				}
				gc.fillPolygon(zCoord, yCoord, nbPoints);
			}
			break;
		case YAXIS:
			offSetZ = middlescreeny + offsetZ;
			offSetX = middlescreenx + offsetX;
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				zCoord = new double[nbPoints];
				xCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					z = (pt.getZ() * zoom) + offSetZ;
					x = (pt.getX() * zoom) + offSetX;

					zCoord[cpt] = z;
					xCoord[cpt] = x;

					cpt++;
				}
				gc.fillPolygon(xCoord, zCoord, nbPoints);
			}
			break;
		case ZAXIS:
			offSetX = middlescreenx + offsetX;
			offSetY = middlescreeny + offsetY;
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				xCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					x = (pt.getX() * zoom) + offSetX;
					y = (pt.getY() * zoom) + offSetY;

					xCoord[cpt] = x;
					yCoord[cpt] = y;

					cpt++;
				}
				gc.fillPolygon(xCoord, yCoord, nbPoints);
			}
			break;
		}
	}

	private void drawBoth(Canvas canvas, Axis axis, double zoom) {
		double middlescreenx = canvas.getWidth() / 2;
		double middlescreeny = canvas.getHeight() / 2;

		double x = 0;
		double y = 0;
		double z = 0;

		double[] xCoord = null;
		double[] yCoord = null;
		double[] zCoord = null;

		double offSetX;
		double offSetY;
		double offSetZ;

		double offsetX = model.getOffsetX();
		double offsetY = model.getOffsetY();
		double offsetZ = model.getOffsetZ();

		List<Face> faces = model.getFaces();

		GraphicsContext gc = canvas.getGraphicsContext2D();

		switch (axis) {
		case XAXIS:
			offSetZ = middlescreenx + offsetZ;
			offSetY = middlescreeny + offsetY;
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				zCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					z = (pt.getZ() * zoom) + offSetZ;
					y = (pt.getY() * zoom) + offSetY;

					zCoord[cpt] = z;
					yCoord[cpt] = y;

					cpt++;
				}
				gc.setFill(Paint.valueOf(String.format("#%02x%02x%02x", (int) (128 * t.degreDeCouleur()),
						(int) (10 * t.degreDeCouleur()), (int) (10 * t.degreDeCouleur()))));
				gc.strokePolygon(zCoord, yCoord, nbPoints);
				gc.fillPolygon(zCoord, yCoord, nbPoints);
				System.out.println(String.format("#%02x%02x%02x", (int) (128 * t.degreDeCouleur()),
						(int) (1 * t.degreDeCouleur()), (int) (1 * t.degreDeCouleur())));
				System.out.println((int) (128 * t.degreDeCouleur()) + " " + (int) (1 * t.degreDeCouleur()) + " "
						+ (int) (1 * t.degreDeCouleur()));

			}
			break;
		case YAXIS:
			offSetZ = middlescreeny + offsetZ;
			offSetX = middlescreenx + offsetX;
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				zCoord = new double[nbPoints];
				xCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					z = (pt.getZ() * zoom) + offSetZ;
					x = (pt.getX() * zoom) + offSetX;

					zCoord[cpt] = z;
					xCoord[cpt] = x;

					cpt++;
				}
				gc.setFill(Paint.valueOf(String.format("#%02x%02x%02x", (int) (128 * t.degreDeCouleur()),
						(int) (10 * t.degreDeCouleur()), (int) (10 * t.degreDeCouleur()))));
				gc.strokePolygon(xCoord, zCoord, nbPoints);
				gc.fillPolygon(xCoord, zCoord, nbPoints);

			}
			break;
		case ZAXIS:
			offSetX = middlescreenx + offsetX;
			offSetY = middlescreeny + offsetY;
			int i = 0;
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				xCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					x = (pt.getX() * zoom) + offSetX;
					y = (pt.getY() * zoom) + offSetY;

					xCoord[cpt] = x;
					yCoord[cpt] = y;

					cpt++;
				}
				gc.setFill(Paint.valueOf(String.format("#%02x%02x%02x", (int) (128 * t.degreDeCouleur()),
						(int) (10 * t.degreDeCouleur()), (int) (10 * t.degreDeCouleur()))));
				gc.strokePolygon(xCoord, yCoord, nbPoints);

				gc.fillPolygon(xCoord, yCoord, nbPoints);

				System.out.println("COLOR FACE" + i + " " + t.degreDeCouleur());
				i++;

			}
			break;
		}
	}

	public void drawAxis() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.RED);
		gc.strokeLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight());
		gc.setStroke(Color.GREEN);
		gc.strokeLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2);
		gc.setStroke(Color.BLACK);
	}
}

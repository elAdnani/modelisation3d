package views;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import connectable.Observer;
import connectable.Subject;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import modele.Face;
import modele.Model;
import modele.Point;
import util.Axis;
import util.DrawingMethod;

public class ModelisationCanvas extends Canvas implements Observer {

	private Axis axis;
	private DrawingMethod method;
	double zoom;
	private Model lastDrawnModel;

	public ModelisationCanvas(double width, double height) {
		this(width, height, Axis.ZAXIS, DrawingMethod.WIREFRAME);
	}

	public ModelisationCanvas(double width, double height, Axis axis) {
		this(width, height, axis, DrawingMethod.WIREFRAME);
	}

	public ModelisationCanvas(double width, double height, DrawingMethod method) {
		this(width, height, Axis.ZAXIS, method);
	}

	public ModelisationCanvas(double width, double height, Axis axis, DrawingMethod method) {
		super(width, height);
		this.axis = axis;
		this.method = method;
		this.zoom = 1;

	}

	public void drawModel(Model model) {
		if (model == null)
			return;
		clearCanvas();
		if(!model.getLastSortedAxis().equals(axis) || !model.isAlreadySorted())
			model.sortPoints(this.axis);
		draw(model);
	}

	public void drawModelAndAxisName(Model model) {
		drawModel(model);
		drawAxisName();
	}

	public void drawAxisName() {
		Paint previousFill = getGraphicsContext2D().getFill();
		getGraphicsContext2D().setFill(Color.DARKGRAY);
		getGraphicsContext2D().fillText(axis.toString(), 10, 15);
		getGraphicsContext2D().setFill(previousFill);
	}

	public void clearCanvas() {
		getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * Draw the model onto a given canvas
	 * 
	 * @param canvas - The canvas to draw the model
	 */
	public void draw(Model model) {
		this.lastDrawnModel = model;
		System.out.println("Drawing started...");
		long start = System.nanoTime();
		if (model != null) {
			switch (method) {
			case WIREFRAME:
				drawWireframe(model, axis, zoom);
				break;
			case SOLID:
				drawSolid(model, axis, zoom);
				break;
			case BOTH:
				drawBoth(model, axis, zoom);
				break;
			default:
				drawWireframe(model, axis, zoom);
				break;
			}
		} else {
			System.err.println("Canvas null");
		}

		long end = System.nanoTime();
		System.out
				.println("Drawing done in " + (end - start) + " nanosecondes (" + (end - start) / 1_000_000.0 + " ms)");

	}

	private void drawWireframe(Model model, Axis axis, double zoom) {
		double middlescreenx = this.getWidth() / 2;
		double middlescreeny = this.getHeight() / 2;

		double x = 0;
		double y = 0;

		double[] xCoord = null;
		double[] yCoord = null;

		double offSetX = 0;
		double offSetY = 0;

		double offsetX = model.getOffsetX();
		double offsetY = model.getOffsetY();
		double offsetZ = model.getOffsetZ();

		String xmethod = "";
		String ymethod = "";

		List<Face> faces = model.getFaces();

		GraphicsContext gc = this.getGraphicsContext2D();

		switch (axis) {
		case XAXIS:
			offSetX = middlescreenx + offsetZ;
			offSetY = middlescreeny + offsetY;
			xmethod = "getZ";
			ymethod = "getY";
			break;
		case YAXIS:
			offSetX = middlescreenx + offsetX;
			offSetY = middlescreeny + offsetZ;
			xmethod = "getX";
			ymethod = "getZ";
			break;
		case ZAXIS:
			offSetX = middlescreenx + offsetX;
			offSetY = middlescreeny + offsetY;
			xmethod = "getX";
			ymethod = "getY";
			break;
		}

		try {
			Method methodGetX = Point.class.getDeclaredMethod(xmethod);
			Method methodGetY = Point.class.getDeclaredMethod(ymethod);
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				xCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					x = ((Double) methodGetX.invoke(pt) * zoom) + offSetX;
					y = ((Double) methodGetY.invoke(pt) * zoom) + offSetY;

					xCoord[cpt] = x;
					yCoord[cpt] = y;

					cpt++;
				}
				gc.strokePolygon(xCoord, yCoord, nbPoints);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void drawSolid(Model model, Axis axis, double zoom) {
		double middlescreenx = this.getWidth() / 2;
		double middlescreeny = this.getHeight() / 2;

		double x = 0;
		double y = 0;

		double[] xCoord = null;
		double[] yCoord = null;

		double offSetX = 0;
		double offSetY = 0;

		double offsetX = model.getOffsetX();
		double offsetY = model.getOffsetY();
		double offsetZ = model.getOffsetZ();

		String xmethod = "";
		String ymethod = "";

		List<Face> faces = model.getFaces();

		GraphicsContext gc = this.getGraphicsContext2D();

		switch (axis) {
		case XAXIS:
			offSetX = middlescreenx + offsetZ;
			offSetY = middlescreeny + offsetY;
			xmethod = "getZ";
			ymethod = "getY";
			break;
		case YAXIS:
			offSetX = middlescreenx + offsetX;
			offSetY = middlescreeny + offsetZ;
			xmethod = "getX";
			ymethod = "getZ";
			break;
		case ZAXIS:
			offSetX = middlescreenx + offsetX;
			offSetY = middlescreeny + offsetY;
			xmethod = "getX";
			ymethod = "getY";
			break;
		}

		try {
			Method methodGetX = Point.class.getDeclaredMethod(xmethod);
			Method methodGetY = Point.class.getDeclaredMethod(ymethod);
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				xCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					x = ((Double) methodGetX.invoke(pt) * zoom) + offSetX;
					y = ((Double) methodGetY.invoke(pt) * zoom) + offSetY;

					xCoord[cpt] = x;
					yCoord[cpt] = y;

					cpt++;
				}
				gc.fillPolygon(xCoord, yCoord, nbPoints);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void drawBoth(Model model, Axis axis, double zoom) {
		double middlescreenx = this.getWidth() / 2;
		double middlescreeny = this.getHeight() / 2;

		double x = 0;
		double y = 0;

		double[] xCoord = null;
		double[] yCoord = null;

		double offSetX = 0;
		double offSetY = 0;

		double offsetX = model.getOffsetX();
		double offsetY = model.getOffsetY();
		double offsetZ = model.getOffsetZ();

		String xmethod = "";
		String ymethod = "";

		List<Face> faces = model.getFaces();

		GraphicsContext gc = this.getGraphicsContext2D();

		switch (axis) {
		case XAXIS:
			offSetX = middlescreenx + offsetZ;
			offSetY = middlescreeny + offsetY;
			xmethod = "getZ";
			ymethod = "getY";
			break;
		case YAXIS:
			offSetX = middlescreenx + offsetX;
			offSetY = middlescreeny + offsetZ;
			xmethod = "getX";
			ymethod = "getZ";
			break;
		case ZAXIS:
			offSetX = middlescreenx + offsetX;
			offSetY = middlescreeny + offsetY;
			xmethod = "getX";
			ymethod = "getY";
			break;
		}

		try {
			Method methodGetX = Point.class.getDeclaredMethod(xmethod);
			Method methodGetY = Point.class.getDeclaredMethod(ymethod);
			for (Face t : faces) {
				Iterator<Point> it = t.getPoints().iterator();
				int nbPoints = t.getPoints().size();
				xCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext()) {
					Point pt = it.next();

					x = ((Double) methodGetX.invoke(pt) * zoom) + offSetX;
					y = ((Double) methodGetY.invoke(pt) * zoom) + offSetY;

					xCoord[cpt] = x;
					yCoord[cpt] = y;

					cpt++;
				}
				gc.fillPolygon(xCoord, yCoord, nbPoints);
				gc.strokePolygon(xCoord, yCoord, nbPoints);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	public DrawingMethod getMethod() {
		return method;
	}

	public void setMethod(DrawingMethod method) {
		this.method = method;
		drawModel(this.lastDrawnModel);
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public Axis getAxis() {
		return axis;
	}

	public void setAxis(Axis axis) {
		this.axis = axis;
	}

	@Override
	public void update(Subject subj) {
		drawModelAndAxisName((Model) subj);
	}

	@Override
	public void update(Subject subj, Object data) {
		update(subj);
	}

}

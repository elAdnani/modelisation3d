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
import math.Face;
import modele.Model;
import modele.geometrique.Figure;
import modele.geometrique.FigureFabrique;
import modele.geometrique.Point;
import util.Axis;
import util.DrawingMethod;

@SuppressWarnings("PMD.LawOfDemeter")
public class ModelisationCanvas extends Canvas implements Observer {

	private Axis          axis;
	private DrawingMethod method;
	private Color figure = Color.rgb(128, 30, 30);

	double                zoom;

	private Model         lastDrawnModel;

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
		if (!model.getLastSortedAxis().equals(axis) || !model.isAlreadySorted())
			model.sortPoints(this.axis);
		draw(model);
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
		if (model != null)
		{
			switch (method) {
			case SOLID:
				DrawCanvas.drawSolid(model, this, zoom);
				break;
			case BOTH:
				DrawCanvas.drawBoth(model, this, zoom);
				break;
			default:
				DrawCanvas.drawWireframe(model, this, zoom);
				break;
			}
		} else
		{
			System.err.println("Canvas null");
		}

		long end = System.nanoTime();
		System.out
				.println("Drawing done in " + (end - start) + " nanosecondes (" + (end - start) / 1_000_000.0 + " ms)");

	}


	public void fillPolygon(double[] xCoord, double[] yCoord, int nbPoints, double degree) {
		
		GraphicsContext gc = this.getGraphicsContext2D();
		int maxColor =255;
		gc.setFill(Paint.valueOf(String.format("#%02x%02x%02x", 
				(int) (this.figure.getRed()*maxColor * degree),
				(int) (this.figure.getGreen()*maxColor * degree), 
				(int) (this.figure.getBlue()*maxColor * degree))));
		gc.fillPolygon(xCoord, yCoord, nbPoints);
	}
	
	public void strokePolygon(double[] xCoord, double[] yCoord, int nbPoints) {
		this.getGraphicsContext2D().strokePolygon(xCoord, yCoord, nbPoints);
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
		drawModel((Model) subj);
		drawAxisName();
	}

	@Override
	public void update(Subject subj, Object data) {
		update(subj);
	}

}

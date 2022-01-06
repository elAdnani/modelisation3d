package views;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import connectable.Observer;
import connectable.Subject;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
		if(model == null) return;
		clearCanvas();
		model.sortPoints(this.axis);
		draw(model);
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

		GraphicsContext gc = this.getGraphicsContext2D();

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
			}
			break;
		}
		gc.strokeOval(middlescreenx-2.5, middlescreeny-2.5, 5, 5);
	}

	private void drawSolid(Model model, Axis axis, double zoom) {
		double middlescreenx = this.getWidth() / 2;
		double middlescreeny = this.getHeight() / 2;

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

		GraphicsContext gc = this.getGraphicsContext2D();

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

	private void drawBoth(Model model, Axis axis, double zoom) {
		double middlescreenx = this.getWidth() / 2;
		double middlescreeny = this.getHeight() / 2;

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

		GraphicsContext gc = this.getGraphicsContext2D();

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

				System.out.println("xcoords:" + Arrays.toString(xCoord));
				System.out.println("ycoords:" + Arrays.toString(yCoord));
				
				System.out.println("COLOR FACE" + i + " " + t.degreDeCouleur());
				i++;

			}
			break;
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
		drawModel((Model) subj);
	}

	@Override
	public void update(Subject subj, Object data) {
		update(subj);
	}

}

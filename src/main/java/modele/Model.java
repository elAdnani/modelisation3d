package modele;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import connectable.ConnectableProperty;
import connectable.ObservableProperty;
import connectable.Subject;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import util.Axis;
import util.DrawingMethod;

public class Model {

	private List<Face> faces;
	private List<Point> points;
	private Point center;

	private double offsetX = 0.0;
	private double offsetY = 0.0;
	private double offsetZ = 0.0;

	public Model() {
		super();
		faces = new ArrayList<Face>();
		points = new ArrayList<Point>();
	}

	/**
	 * Draw the model onto a given canvas
	 * 
	 * @param canvas - The canvas to draw the model
	 */
	public void draw(Canvas canvas, Axis axis, double zoom, DrawingMethod method) {
		// TODO Ajouter homothetie pour le zoom
		// TODO Ajouter translation pour l'offset

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
		double offSetZ;
		double offSetY;

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
		double offSetZ;
		double offSetY;

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
		double offSetZ;
		double offSetY;

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
				gc.strokePolygon(xCoord, zCoord, nbPoints);
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
				gc.strokePolygon(xCoord, yCoord, nbPoints);
				gc.fillPolygon(xCoord, yCoord, nbPoints);
			}
			break;
		}
	}

	/**
	 * Load .ply file for current Model
	 * @throws FormatPlyException 
	 * @throws FileNotFoundException 
	 */
	public void loadFile(String path) throws FileNotFoundException, FormatPlyException {
		
			RecuperationPly.recuperationFichier(path);
			points = RecuperationPly.getPoints();
			faces = RecuperationPly.getFaces();
			center = calculateCenter();
			centerModel();
		
//		calculateAutoScale(); TODO
	}

	/**
	 * Center and put the x coordinate between -1 and 1 and the y coordinate between
	 * -1 and 1
	 */
	private void centerModel() {
		double length = Math.pow(center.getX() * 2, 2);
		double height = Math.pow(center.getY() * 2, 2);
		double breadth = Math.pow(center.getZ() * 2, 2);
		double diagonal = Math.sqrt(length + height + breadth);
		for (Point p : points) {
			p.setX((p.getX() - center.getX()) / diagonal);
			p.setY((p.getY() - center.getY()) / diagonal);
			p.setZ((p.getZ() - center.getZ()) / diagonal);
		}
	}

	public void sortPoints(Axis axis) {
		long start = System.nanoTime();
		System.out.println("Sorting by " + axis.name() + " started...");
		Comparator<Face> comp = null;
		switch (axis) {
		case XAXIS:
			comp = new XAxisComparator();
			break;
		case YAXIS:
			comp = new YAxisComparator();
			break;
		case ZAXIS:
			comp = new ZAxisComparator();
			break;
		default:
			comp = new XAxisComparator();
			break;
		}

		Collections.sort(faces, comp);

		long end = System.nanoTime();
		System.out
				.println("Sorting done in " + (end - start) + " nanoseconds (" + (end - start) / 1_000_000.0 + " ms)");
	}

	private Point calculateCenter() {
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

		centerCoord[0] = (xMax + xMin) / 2;
		centerCoord[1] = (yMax + yMin) / 2;
		centerCoord[2] = (zMax + zMin) / 2;

		System.out.println("xMax : " + xMax + "; xMin : " + xMin + "; MilieuX : " + centerCoord[0]);
		System.out.println("yMax : " + yMax + "; yMin : " + yMin + "; MilieuY : " + centerCoord[1]);
		System.out.println("zMax : " + zMax + "; zMin : " + zMin + "; MilieuZ : " + centerCoord[2]);

		return new Point(centerCoord[0], centerCoord[1], centerCoord[2]);
	}

	public double calculateAutoScale(Canvas canvas) {
		Double calculatedZoom = null;

		System.out.println("Autoscale started...");
		long start = System.nanoTime();

		double length = Math.pow(center.getX() * 2, 2);
		double height = Math.pow(center.getY() * 2, 2);
		double breadth = Math.pow(center.getZ() * 2, 2);
		double diagonal = Math.sqrt(length + height + breadth);

		Double canvasWidth = canvas.getWidth();
		Double canvasHeight = canvas.getHeight();

		Double smallestSize = Math.min(canvasWidth, canvasHeight);

		calculatedZoom = smallestSize / (diagonal + 1);
		System.out.println("Diagonal : " + diagonal);
		long end = System.nanoTime();
		System.out.println(
				"Autoscale done in " + (end - start) + " nanoseconds (" + ((end - start) / 1_000_000.0) + " ms)");
		System.out.println("Resulted zoom from autoscale is " + calculatedZoom.doubleValue());

		return calculatedZoom;
	}

	public void rotate(Axis axis, double theta) {
		switch (axis) {
		case XAXIS:
			rotateX(theta);
			break;
		case YAXIS:
			rotateY(theta);
			break;
		case ZAXIS:
			rotateZ(theta);
			break;
		default:
			rotateX(theta);
			break;
		}
	}

	// Calcul
	private void rotateX(double tetha) {
		double sinTheta = Math.sin(tetha);
		double cosTheta = Math.cos(tetha);
		for (Point p : points) {
			double newY = (p.getY() * cosTheta) - (p.getZ() * sinTheta);
			double newZ = (p.getY() * sinTheta) + (p.getZ() * cosTheta);
			p.setY(newY);
			p.setZ(newZ);
		}
	}

	// Calcul
	private void rotateY(double tetha) {
		double sinTheta = Math.sin(tetha);
		double cosTheta = Math.cos(tetha);
		for (Point p : points) {
			double newX = (p.getX() * cosTheta) + (p.getZ() * sinTheta);
			double newZ = (p.getZ() * cosTheta) - (p.getX() * sinTheta);
			p.setX(newX);
			p.setZ(newZ);
		}
	}

	// Calcul
	private void rotateZ(double tetha) {
		double sinTheta = Math.sin(tetha);
		double cosTheta = Math.cos(tetha);
		for (Point p : points) {
			double newX = (p.getX() * cosTheta) - (p.getY() * sinTheta);
			double newY = (p.getX() * sinTheta) + (p.getY() * cosTheta);
			p.setX(newX);
			p.setY(newY);
		}
	}

	public void translate(Axis axis, double distance) {
		switch (axis) {
		case XAXIS:
			this.offsetX += distance;
			break;
		case YAXIS:
			this.offsetY += distance;
			break;
		case ZAXIS:
			this.offsetZ += distance;
			break;
		}
	}
	
	public void copy(Model model) {
		if(model == null) return;
		this.center = model.center;
		this.faces = model.faces;
		this.points = model.points;
		this.offsetX = model.offsetX;
		this.offsetY = model.offsetY;
		this.offsetZ = model.offsetZ;
	}

	/**
	 * GETTERS AND SETTERS
	 */

	/**
	 * Return the center of the model
	 * 
	 * @return center of the model
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * Return the list of faces of the model
	 * 
	 * @return list of faces of the model
	 */
	public List<Face> getFaces() {
		return faces;
	}

	/**
	 * 
	 * COMPARATORS
	 *
	 */

	/**
	 * Comparator for the X axis of points
	 *
	 */
	private class XAxisComparator implements Comparator<Face> {
		@Override
		public int compare(Face o1, Face o2) {
			return o2.getAverageX() - o1.getAverageX() < 0 ? 1 : -1;
		}
	}

	/**
	 * Comparator for the Y axis of points
	 *
	 */
	private class YAxisComparator implements Comparator<Face> {
		@Override
		public int compare(Face o1, Face o2) {
			return o1.getAverageY() - o2.getAverageY() < 0 ? 1 : -1;
		}
	}

	/**
	 * Comparator for the Z axis of points
	 *
	 */
	private class ZAxisComparator implements Comparator<Face> {
		@Override
		public int compare(Face o1, Face o2) {
			return o1.getAverageZ() - o2.getAverageZ() < 0 ? 1 : -1;
		}
	}

}

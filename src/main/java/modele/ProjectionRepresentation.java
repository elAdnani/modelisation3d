package modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import App.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import utils.Axis;
import utils.DrawingMethod;
import utils.Subject;


public class ProjectionRepresentation extends Subject{
	
	//final protected Matrice POINTS;
	private Composition compo;
	private Point center;

	public ProjectionRepresentation() {
		compo = new Composition(new ArrayList<Face>(), new ArrayList<Point>());
	}

	public void setComposant(Composition comp) {
		compo.setData(comp);
		notifyObservers((Data)compo.getValue());
	}
	public void setComposant(Data donnee) {
		compo.setData(donnee);
		notifyObservers((Data)compo.getValue());
	}
	
	/**
	 * Draw the model onto a given canvas
	 * 
	 * @param canvas - The canvas to draw the model
	 */
	public void draw(Canvas canvas, DrawingMethod method, double zoom) {
		// TODO Ajouter homothetie pour le zoom
		// TODO Ajouter translation pour l'offset

		long start = System.nanoTime();
	//	System.out.println("Drawing started...");

		switch (method) {
		case WIREFRAME:
			drawWireframe(canvas, zoom);
			break;
		case SOLID:
			drawSolid(canvas, zoom);
			break;
		case BOTH:
			drawBoth(canvas, zoom);
			break;
		default:
			drawWireframe(canvas, zoom);
			break;
		}

		long end = System.nanoTime();
		/* TODO
		System.out
				.println("Drawing done in " + (end - start) + " nanosecondes (" + (end - start) / 1_000_000.0 + " ms)");
		*/
	}

	private void drawWireframe(Canvas canvas, double zoom) {
		double middlescreenx = canvas.getWidth() / 2;
		double middlescreeny = canvas.getHeight() / 2;
		double x = 0;
		double y = 0;
		double[] xCoord = new double[4];
		double[] yCoord = new double[4];

		double offSetX = middlescreenx;
		double offSetY = middlescreeny;

		GraphicsContext gc = canvas.getGraphicsContext2D();

		for (Face t : compo.getFaces()) {
			Iterator<Point> it = t.getPoints().iterator();
			int cpt = 0;
			while (it.hasNext()) {
				Point pt = it.next();

				x = (pt.getX() * zoom) + offSetX;
				y = (pt.getY() * zoom) + offSetY;

				xCoord[cpt] = x;
				yCoord[cpt] = y;

				cpt++;
			}
			gc.strokePolygon(xCoord, yCoord, t.getPoints().size());
		}
		
	}

	private void drawSolid(Canvas canvas, double zoom) {
		double middlescreenx = canvas.getWidth() / 2;
		double middlescreeny = canvas.getHeight() / 2;
		double x = 0;
		double y = 0;
		double[] xCoord = new double[4];
		double[] yCoord = new double[4];

		double offSetX = middlescreenx;
		double offSetY = middlescreeny;

		GraphicsContext gc = canvas.getGraphicsContext2D();

		for (Face t : compo.getFaces()) {
			Iterator<Point> it = t.getPoints().iterator();
			int cpt = 0;
			while (it.hasNext()) {
				Point pt = it.next();

				x = (pt.getX() * zoom) + offSetX;
				y = (pt.getY() * zoom) + offSetY;

				xCoord[cpt] = x;
				yCoord[cpt] = y;

				cpt++;
			}
			gc.fillPolygon(xCoord, yCoord, t.getPoints().size());
		}
		
	}

	private void drawBoth(Canvas canvas, double zoom) {
		double middlescreenx = canvas.getWidth() / 2;
		double middlescreeny = canvas.getHeight() / 2;
		double x = 0;
		double y = 0;
		double[] xCoord = null;
		double[] yCoord = null;

		double offSetX = middlescreenx;
		double offSetY = middlescreeny;

		GraphicsContext gc = canvas.getGraphicsContext2D();

		for (Face t : compo.getFaces()) {
			Iterator<Point> it = t.getPoints().iterator();
			xCoord = new double[t.getPoints().size()];
			yCoord = new double[t.getPoints().size()];
			int cpt = 0;
			while (it.hasNext()) {
				Point pt = it.next();

				x = (pt.getX() * zoom) + offSetX;
				y = (pt.getY() * zoom) + offSetY;

				xCoord[cpt] = x;
				yCoord[cpt] = y;

				cpt++;
			}
			gc.strokePolygon(xCoord, yCoord, t.getPoints().size());
			gc.fillPolygon(xCoord, yCoord, t.getPoints().size());
		}
		
	}

	/**
	 * Load .ply file for current Model
	 */
	public void loadFile(String path) {
		if (path != null ){// || path.isBlank())
			//return; // TODO A remplacer par une exception
		try {
			/**
			 * TODO CLEAN CODE ?
			 */
			List<Point> point = RecuperationPly.recuperationPoints(path);
			List<Face> faces = RecuperationPly.recuperationFaces(path, point);
			
			compo.setData(new Composition(faces, point));
			
			center = calculateCenter();

			centerModel();
		} catch (Exception e) {
			// TODO Remplacer par une exception
			System.out.println("Une exception est arrivé lors du chargement du fichier!");
			e.printStackTrace();
		}
		}
	//	calculateAutoScale(null);
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
		for (Point p : compo.getPoints() ){
			p.setX((p.getX() - center.getX()) / diagonal);
			p.setY((p.getY() - center.getY()) / diagonal);
			p.setZ((p.getZ() - center.getZ()) / diagonal);
		}
		
	}

	public void sortPoints(Axis axis) {
		long start = System.nanoTime();
	//	System.out.println("Sorting by " + axis.name() + " started..."); TODO
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

		Collections.sort(compo.getFaces(), comp);
		
		long end = System.nanoTime();
		/* TODO
		System.out
				.println("Sorting done in " + (end - start) + " nanoseconds (" + (end - start) / 1_000_000.0 + " ms)");
		*/
	}

	private Point calculateCenter() {
		double[] centerCoord = new double[3];
		Double xMin = null;
		Double xMax = null;
		Double yMin = null;
		Double yMax = null;
		Double zMin = null;
		Double zMax = null;

		for (Iterator<Point> iterator = compo.getPoints().iterator(); iterator.hasNext();) {
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
/* TODO
		System.out.println("xMax : " + xMax + "; xMin : " + xMin + "; MilieuX : " + centerCoord[0]);
		System.out.println("yMax : " + yMax + "; yMin : " + yMin + "; MilieuY : " + centerCoord[1]);
		System.out.println("zMax : " + zMax + "; zMin : " + zMin + "; MilieuZ : " + centerCoord[2]);
*/
		return new Point(centerCoord[0], centerCoord[1], centerCoord[2]);
	}

	private double calculateAutoScale(Canvas canvas) {
		Double calculatedZoom = null;

	//	System.out.println("Autoscale started..."); TODO
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
		/* TODO
		System.out.println(
				"Autoscale done in " + (end - start) + " nanoseconds (" + ((end - start) / 1_000_000.0) + " ms)");
		System.out.println("Resulted zoom from autoscale is " + calculatedZoom.doubleValue());
		*/
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
		
		notifyObservers(this.compo.getValue());
	}

	// Calcul
	private void rotateX(double tetha) {
		double sinTheta = Math.sin(tetha);
		double cosTheta = Math.cos(tetha);
		for (Point p : compo.getPoints()) {
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
		for (Point p : compo.getPoints()) {
			double newX = (p.getX() * cosTheta) + (p.getZ() * sinTheta);
			double newZ = (p.getZ() * cosTheta) - (p.getX() * sinTheta);
			p.setX(newX);
			p.setZ(newZ);
		}
		
	}

	// Calcul
	private void rotateZ(double tetha) {
		
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
		return compo.getFaces();
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
			return o1.getAverageX() - o2.getAverageX() < 0 ? 1 : -1;
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

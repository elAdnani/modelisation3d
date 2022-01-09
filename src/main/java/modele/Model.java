package modele;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import connectable.Subject;
import javafx.scene.canvas.Canvas;
import math.Face;
import math.Matrice;
import math.OutilMatriciel;
import modele.geometrique.FigureFabrique;
import modele.geometrique.Point;
import ply.RecuperationPly;
import ply.exceptions.FormatPlyException;
import util.Axis;
/**
 * 
 * Cette classe est la représentation du 
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 9 janv. 2022
 * 
 */
public class Model extends Subject {

	private List<Face> faces;
	private List<Point> points;
	private Point center;

	private double offsetX = 0.0;
	private double offsetY = 0.0;
	private double offsetZ = 0.0;

	private Axis lastSortedAxis = Axis.ZAXIS;
	private boolean isAlreadySorted = false;

	public Model() {
		super();
		faces = new ArrayList<>();
		points = new ArrayList<>();
	}

	/**
	 * Load .ply file for current Model
	 * 
	 * @throws FormatPlyException
	 * @throws FileNotFoundException
	 */
	public void loadFile(String path) throws FileNotFoundException, FormatPlyException {
		RecuperationPly.readFile(path);
		points = RecuperationPly.getPoints();
		faces = RecuperationPly.getFaces();
		center = calculateCenter();
		centerModel();
		notifyObservers();
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
		this.lastSortedAxis = axis;
		long start = System.nanoTime();
		Comparator<Face> comp = new AxisComparator(axis);

		Collections.sort(faces, comp);
		this.isAlreadySorted = true;

		long end = System.nanoTime();
		System.out.println("Sorting done in " + (end - start) + " nanoseconds (" + (end - start) / 1_000_000.0 + " ms)");
	}

	private Point calculateCenter() {
		FigureFabrique fabriquePoint = FigureFabrique.getInstance();
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
		
		return fabriquePoint.vertex(centerCoord[0], centerCoord[1], centerCoord[2]);
	}

	public Model rotate(Axis axis, double theta) {
		switch (axis) {
		case YAXIS:
			rotateEach(OutilMatriciel.getYRotation(theta));
			break;
		case ZAXIS:
			rotateEach(OutilMatriciel.getZRotation(theta));
			break;
		default:
			rotateEach(OutilMatriciel.getXRotation(theta));
			break;
		}
		this.isAlreadySorted = false;
		return this;
	}
	
	private void rotateEach(Matrice rotation) {
		for (Point p : points) {
			p.modifyCoordinates(rotation);
		}
	}


	public Model translate(Axis axis, double distance) {
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
		return this;
	}

	/*
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
		List<Face> facesDivision = new ArrayList<>();
		for (Face face : this.faces) {
			facesDivision.addAll(face.getFaces());
		}
		return faces;
	}

	public double getOffsetX() {
		return offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public double getOffsetZ() {
		return offsetZ;
	}

	public Axis getLastSortedAxis() {
		return lastSortedAxis;
	}

	public boolean isAlreadySorted() {
		return isAlreadySorted;
	}

	/**
	 * Comparator for the X axis of points
	 *
	 */
	private class AxisComparator implements Comparator<Face> {
		Axis axis;
		public AxisComparator(Axis axis) {
			this.axis=axis;
		}
		@Override
		public int compare(Face o1, Face o2) {
			double av1;
			double av2;
			switch (this.axis) {
			case YAXIS:
				av1 = o1.getAverageY();
				av2 = o2.getAverageY();
				break;
			case ZAXIS:
				av1 = o1.getAverageZ();
				av2 = o2.getAverageZ();
				break;
			default:
				av1 = o1.getAverageX();
				av2 = o2.getAverageX();
				break;
			}
			if (av1 > av2) {
				return 1;
			} else if (av1 < av2) {
				return -1;
			}
			return 0;
		}
	}


}

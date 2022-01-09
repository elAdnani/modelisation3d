package views;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import math.Face;
import modele.Model;
import modele.geometrique.Figure;
import modele.geometrique.Vertex;
import util.DrawingMethod;

/**
 * 
 * Cette classe sert à définir les différentes manières de dessin appartenant à
 * {@link Axis}
 *
 */
public class DrawCanvas {
	private static String xmethod;
	private static String ymethod;
	private static double offSetX;
	private static double offSetY;
	
	private DrawCanvas() {}

	public static void draw(Model model, ModelisationCanvas canvas, double zoom, DrawingMethod method) {
		findMethod(model, canvas);

		List<Face> faces = model.getFaces();
		try {
			Method methodGetX = Vertex.class.getDeclaredMethod(xmethod);
			Method methodGetY = Vertex.class.getDeclaredMethod(ymethod);
			for (Face face : faces) {
				List<Vertex> points = face.getPoints();
				Iterator<Vertex> iterator = points.iterator();
				int nbPoints = points.size();
				double[] xCoord = new double[nbPoints];
				double[] yCoord = new double[nbPoints];
				int cpt = 0;
				while (iterator.hasNext()) {
					Vertex point = iterator.next();

					double xCoordinate = ((Double) methodGetX.invoke(point) * zoom) + offSetX;
					double yCoordinate = ((Double) methodGetY.invoke(point) * zoom) + offSetY;

					xCoord[cpt] = xCoordinate;
					yCoord[cpt] = yCoordinate;

					cpt++;
				}
				if(method.equals(DrawingMethod.BOTH) || method.equals(DrawingMethod.WIREFRAME)) {
					canvas.strokePolygon(xCoord, yCoord, nbPoints);
				}
				if(method.equals(DrawingMethod.BOTH) || method.equals(DrawingMethod.SOLID) ) {
					canvas.fillPolygon(xCoord, yCoord, nbPoints, face.colorDegree());
				}
				
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private static void findMethod(Model model, ModelisationCanvas canvas) {
		double middlescreenx = canvas.getWidth() / 2;
		double middlescreeny = canvas.getHeight() / 2;
		
		switch (canvas.getAxis()) {
		case XAXIS:
			offSetX = middlescreenx + model.getOffsetZ();
			offSetY = middlescreeny + model.getOffsetY();
			xmethod = "getZ";
			ymethod = "getY";
			break;
		case YAXIS:
			offSetX = middlescreenx + model.getOffsetX();
			offSetY = middlescreeny + model.getOffsetZ();
			xmethod = "getX";
			ymethod = "getZ";
			break;
		case ZAXIS:
			offSetX = middlescreenx + model.getOffsetX();
			offSetY = middlescreeny + model.getOffsetY();
			xmethod = "getX";
			ymethod = "getY";
			break;
		}
		
	}
}

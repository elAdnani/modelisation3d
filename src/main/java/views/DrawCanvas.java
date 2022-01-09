package views;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import math.Face;
import modele.Model;
import modele.geometrique.Figure;
import modele.geometrique.Point;
/**
 * 
 * Cette classe sert à définir les différentes manières de dessin appartenant à {@link Axis}
 *
 */
public class DrawCanvas {
	private static String xmethod ;
	private static String ymethod;
	private static double offSetX;
	private static double offSetY;
	
	

	public static void drawWireframe(Model model, ModelisationCanvas canvas, double zoom) {
		findMethod( model, canvas);

		double x = 0;
		double y = 0;

		double[] xCoord = null;
		double[] yCoord = null;

		List<Face> faces = model.getFaces();


		try
		{
			Method methodGetX = Point.class.getDeclaredMethod(xmethod);
			Method methodGetY = Point.class.getDeclaredMethod(ymethod);
			for (Face face : faces)
			{
				Iterator<Point> it = face.getPoints().iterator();
				int nbPoints = face.getPoints().size();
				xCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext())
				{
					Point pt = it.next();

					x = ((Double) methodGetX.invoke(pt) * zoom) + offSetX;
					y = ((Double) methodGetY.invoke(pt) * zoom) + offSetY;

					xCoord[cpt] = x;
					yCoord[cpt] = y;

					cpt++;
				}
				canvas.strokePolygon(xCoord, yCoord, nbPoints);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	public static void drawSolid(Model model, ModelisationCanvas canvas, double zoom) {
		findMethod( model, canvas);
		double x = 0;
		double y = 0;

		double[] xCoord = null;
		double[] yCoord = null;

		
		List<Face> faces = model.getFaces();


		try
		{
			Method methodGetX = Figure.class.getDeclaredMethod(xmethod);
			Method methodGetY = Figure.class.getDeclaredMethod(ymethod);
			for (Face face : faces)
			{
				Iterator<Point> it = face.getPoints().iterator();
				int nbPoints = face.getPoints().size();
				xCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext())
				{
					Point pt = it.next();

					x = ((Double) methodGetX.invoke(pt) * zoom) + offSetX;
					y = ((Double) methodGetY.invoke(pt) * zoom) + offSetY;

					xCoord[cpt] = x;
					yCoord[cpt] = y;

					cpt++;
				}
				canvas.fillPolygon(xCoord, yCoord, nbPoints, face.degreDeCouleur());
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e)
		{
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
	public static void drawBoth(Model model, ModelisationCanvas canvas, double zoom) {
		findMethod( model, canvas);

		double x = 0;
		double y = 0;

		double[] xCoord = null;
		double[] yCoord = null;

		List<Face> faces = model.getFaces();
		try
		{
			Method methodGetX = Figure.class.getDeclaredMethod(xmethod);
			Method methodGetY = Figure.class.getDeclaredMethod(ymethod);
			for (Face face : faces)
			{
				Iterator<Point> it = face.getPoints().iterator();
				int nbPoints = face.getPoints().size();
				xCoord = new double[nbPoints];
				yCoord = new double[nbPoints];
				int cpt = 0;
				while (it.hasNext())
				{
					Point pt = it.next();

					x = ((Double) methodGetX.invoke(pt) * zoom) + offSetX;
					y = ((Double) methodGetY.invoke(pt) * zoom) + offSetY;

					xCoord[cpt] = x;
					yCoord[cpt] = y;

					cpt++;
				}

				canvas.fillPolygon(xCoord, yCoord, nbPoints, face.degreDeCouleur());
				canvas.strokePolygon(xCoord, yCoord, nbPoints);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e)
		{
			e.printStackTrace();
		}

	}
}

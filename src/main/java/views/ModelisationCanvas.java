package views;

import connectable.Observer;
import connectable.Subject;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import modele.Model;
import util.Axis;
import util.DrawingMethod;
/**
 * ModelisationCanvas dérivant de {@link Canvas} est une image qui peut être dessinée à l’aide d’un ensemble de commandes graphiques fournies par un {@link GraphicsContext}.</br>
 * Elle permet de dessiner par plusieurs moyens {@link DrawingMethod} sur une couleur unie. </br>
 * 
 */
@SuppressWarnings("PMD.LawOfDemeter")
public class ModelisationCanvas extends Canvas implements Observer {
/**
 * - Elle possède l'axe dans laquelle il est représenté </br>
 * - La méthode sous laquelle elle est représenté</br>
 * - La couleur sur laquelle elle est affiché</br>
 * - Le zoom pour augmenter la taille du modele</br>
 * - Le dernier modele pour pouvoir réafficher le même modèle mais sur une autre méthode d'affichage
 */
	private Axis          axis;
	private DrawingMethod method;
	private Color figure = Color.rgb(128, 30, 30);

	double                zoom;
	private Model         lastDrawnModel;

	/**
	 * Récupérer une instance de canvas de type {@link ModelisationCanvas}. </br>
	 * Par défaut, la méthode de trie et sur le ZAXIS et la méthode et en fil de fer
	 * @param width longueur
	 * @param height largeur
	 */
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
		this.zoom = 10;

	}

	public ModelisationCanvas(double width, double height, Axis axis, DrawingMethod method, double zoom) {
		super(width, height);
		this.axis = axis;
		this.method = method;
		this.zoom = zoom;

	}

	/**
	 * Supprime tous les points du canvas en vérifiant que les points sont
	 * @param model
	 */
	public void drawModel(Model model) {
		if (model == null)
			return;
		clearCanvas();
		if (!model.getLastSortedAxis().equals(axis) || !model.isSorted())
			model.sortPoints(this.axis);
		draw(model);
	}

	/**
	 * Permet d'afficher le nom des axes sous lesquels ils sont dessiné.
	 */
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
	 * Permet de dessiner sous plusieurs manière {@link DrawingMethod} selon un modele
	 * 
	 * @param model modele sous lequel le canvas est dessiné
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

	/**
	 * Dessine un polygone sur l'image par rapport à un degree et couleur bien proprement spécifié au {@link ModelisationCanvas}
	 * @param xCoord tableau contenant les coordonnées x des points du polygone
	 * @param yCoord tableau contenant les coordonnées y des points du polygone
	 * @param nbPoints le nombre de points qui composent le polygone
	 * @param degree degré de couleur définie entre 0 et 1
	 */
	public void fillPolygon(double[] xCoord, double[] yCoord, int nbPoints, double degree) {
		GraphicsContext context = this.getGraphicsContext2D();
		int maxColor =255;
		context.setFill(Paint.valueOf(String.format("#%02x%02x%02x", 
				(int) (this.figure.getRed()*maxColor * degree),
				(int) (this.figure.getGreen()*maxColor * degree), 
				(int) (this.figure.getBlue()*maxColor * degree))));
		context.fillPolygon(xCoord, yCoord, nbPoints);
	}
	
	/**
	 *  Dessine les contours d'un polygone sur l'image
	 * @param xCoord tableau contenant les coordonnées x des points du polygone
	 * @param yCoord tableau contenant les coordonnées y des points du polygone
	 * @param nbPoints le nombre de points qui composent le polygone
	 */
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

	/**
	 * trie le modele et affiche son axes
	 */
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

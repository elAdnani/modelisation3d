package views;

import java.io.File;
import java.io.FileNotFoundException;

import connectable.ConnectableProperty;
import connectable.Observer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import modele.Model;
import ply.exceptions.FormatPlyException;
import util.Axis;
import util.DrawingMethod;
import util.Theme;

public class Controller extends ConnectableProperty {

	private Model model = null;
	private View view = null;

	private double oldMouseX;
	private double oldMouseY;

	public double zoom = 0.0;
	public static double theta = toRadian(1); // Angle de rotation en radian // Calcul

	// private final long[] frameTimes = new long[100]; // Interface
	// private int frameTimeIndex = 0; // Interface
	// private boolean arrayFilled = false; // Interface

	public Controller(View view) {
		this(view, Axis.ZAXIS);
	}

	public Controller(View view, Axis axis) {
		this.view = view;
		this.zoom = 100.0;
	}

	public void loadFile(String path) throws FileNotFoundException, FormatPlyException {
		if (this.model == null)
			model = new Model();
		if (path != null) {
			this.model.loadFile(path);
			this.view.zoomSlider.setValue(zoom / 100);
			notifyObservers();
			this.model.attach(getCanvas());
			this.model.notifyObservers();
		}

		// this.zoom = this.model.calculateAutoScale(canvas);
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

	/**
	 * GETTERS AND SETTERS
	 */

	/**
	 * Return the canvas of the Affichage
	 * 
	 * @return canvas of the Affichage
	 */
	public ModelisationCanvas getCanvas() {
		return this.view.getCanvas();
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
			this.getCanvas().zoom = zoom;
			model.notifyObservers();
			for (Observer obs : attached) {
				Controller af = (Controller) obs;
				af.setZoom(zoom);
				af.view.zoomSlider.setValue(zoom / 100);
			}
			propagating = false;
		}
	}

	public void updateTheme(Theme theme) {
		if (!propagating) {
			propagating = true;
			for (Observer obs : attached) {
				Controller af = (Controller) obs;
				af.view.changeTheme(theme);
			}
			propagating = false;
		}
	}

	/**
	 * @param openFileItem
	 */
	public void setFileChooserEvent(MenuItem openFileItem) {
		openFileItem.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open File");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Ply File", "*.ply"),
					new ExtensionFilter("All Files", "*.*"));
			File choosedfile = fileChooser.showOpenDialog(null);

			if (choosedfile != null) {

				String path = choosedfile.getAbsolutePath();
				try {
					loadFile(path);
				} catch (FormatPlyException | FileNotFoundException e) {
					// erreur(e);
				}
			}

		});
	}
	
	/**
	 * @param down
	 */
	public void setOnDown(Button down) {
		down.setOnAction(e -> {
			switch (getCanvas().getAxis()) {
			case XAXIS:
				rotateModel(Axis.ZAXIS, -4);
				break;
			case YAXIS:
				rotateModel(Axis.ZAXIS, -4);
				break;
			case ZAXIS:
				rotateModel(Axis.XAXIS, -4);
				break;
			}
			this.model.notifyObservers();
		});
	}

	/**
	 * @param up
	 */
	public void setOnUp(Button up) {
		up.setOnAction(e -> {
			switch (getCanvas().getAxis()) {
			case XAXIS:
				rotateModel(Axis.ZAXIS, 4);
				break;
			case YAXIS:
				rotateModel(Axis.ZAXIS, 4);
				break;
			case ZAXIS:
				rotateModel(Axis.XAXIS, 4);
				break;
			}
			this.model.notifyObservers();
		});
	}

	/**
	 * @param left
	 */
	public void setOnLeft(Button left) {
		left.setOnAction(e -> {
			switch (getCanvas().getAxis()) {
			case XAXIS:
				rotateModel(Axis.YAXIS, -4);
				break;
			case YAXIS:
				rotateModel(Axis.XAXIS, -4);
				break;
			case ZAXIS:
				rotateModel(Axis.YAXIS, -4);
				break;
			}

			this.model.notifyObservers();
		});
	}

	/**
	 * @param right
	 */
	public void setOnRight(Button right) {
		right.setOnAction(e -> {
			switch (view.getCanvas().getAxis()) {
			case XAXIS:
				rotateModel(Axis.YAXIS, 4);
				break;
			case YAXIS:
				rotateModel(Axis.XAXIS, 4);
				break;
			case ZAXIS:
				rotateModel(Axis.YAXIS, 4);
				break;
			}
			this.model.notifyObservers();
		});
	}
	

	public void setOnDroit(RadioMenuItem droit) {
		droit.setOnAction(event -> {
			this.getCanvas().setAxis(Axis.ZAXIS);
			this.model.notifyObservers();
		});
	}

	public void setOnFace(RadioMenuItem face) {
		face.setOnAction(event -> {
			this.getCanvas().setAxis(Axis.XAXIS);
			this.model.notifyObservers();
		});
	}

	public void setOnHaut(RadioMenuItem haut) {
		haut.setOnAction(event -> {
			this.getCanvas().setAxis(Axis.YAXIS);
			this.model.notifyObservers();
		});
	}


	/**
	 * @param node
	 */
	public void addKeyPressedEvent(Node node) {
		node.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.RIGHT) {
					rotateModel(Axis.YAXIS, toRadian(1));
					model.notifyObservers();
				}
				if (e.getCode() == KeyCode.LEFT) {
					rotateModel(Axis.YAXIS, toRadian(-1));
					model.notifyObservers();
				}
				if (e.getCode() == KeyCode.UP) {
					rotateModel(Axis.XAXIS, toRadian(1));
					model.notifyObservers();
				}
				if (e.getCode() == KeyCode.DOWN) {
					rotateModel(Axis.XAXIS, toRadian(-1));
					model.notifyObservers();
				}
			}
		});
	}

	/**
	 * @param exitItem
	 */
	public void setExitAction(MenuItem exitItem) {
		exitItem.setOnAction(event -> Platform.exit());
	}

	/**
	 * @param helpItem
	 */
	public void setHelpAction(MenuItem helpItem) {
		helpItem.setOnAction(event -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Aide");
			alert.setContentText(
					"Bienvenue dans notre modélisateur 3d !\nJe vous invite à naviguer dans le menu Fichier pour ouvrir un fichier .ply.\nUne fois sélectionné, rendez-vous dans le menu View pour choisir comment vous voulez visualiser votre modèle.\nSi le thème par défaut vous pique un peu les yeux, essayez nos autres thèmes accessibles dans le menu du même nom.\n\nA votre droite vous trouverez différents boutons.\nLes boutons \"Vue de face\", \"Vue de droite\" et \"Vue de haut\" vous ouvrira une nouvelle page synchronisé avec la première sauf pour la vue des modèles qui est au choix.\nEn dessous, nous trouvons les boutons liés à la rotation du modèle.\nPlus bas nous avons le zoom allant de x0 à x10.");
			alert.showAndWait();
		});
	}

	/**
	 * @param m
	 * @param radioItem
	 */
	public void setOnMethodChangerAction(DrawingMethod m, RadioMenuItem radioItem) {
		radioItem.setOnAction(event -> {
			this.view.setMethod(m);
			this.model.notifyObservers();
		});
	}

	/**
	 * @param f
	 * @param item
	 */
	public void setLoadFileItem(File f, MenuItem item) {
		item.setOnAction(event -> {
			try {
				loadFile(f.getPath());
			} catch (FileNotFoundException | FormatPlyException e) {
				e.printStackTrace();
			}
			this.view.getCanvas().clearCanvas();
			this.model.notifyObservers();
		});
	}
	
	/**
	 * 
	 */
	public void setMouseDragging(Canvas canvas) {
		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();
				double xDistance = mouseX - oldMouseX;
				double yDistance = mouseY - oldMouseY;
				if (event.getButton().equals(MouseButton.SECONDARY)) {
					switch (getCanvas().getAxis()) {
					case XAXIS:
						model.translate(Axis.ZAXIS, xDistance);
						model.translate(Axis.YAXIS, yDistance);
						break;
					case YAXIS:
						model.translate(Axis.XAXIS, xDistance);
						model.translate(Axis.ZAXIS, yDistance);
						break;
					case ZAXIS:
						model.translate(Axis.XAXIS, xDistance);
						model.translate(Axis.YAXIS, yDistance);
						break;
					}
					model.notifyObservers();
				} else if (event.getButton().equals(MouseButton.PRIMARY)) {
					switch (getCanvas().getAxis()) {
					case XAXIS:
						model.rotate(Axis.ZAXIS, toRadian(yDistance));
						model.rotate(Axis.YAXIS, toRadian(xDistance));
						break;
					case YAXIS:
						model.rotate(Axis.XAXIS, toRadian(yDistance));
						model.rotate(Axis.ZAXIS, toRadian(xDistance));
						break;
					case ZAXIS:
						model.rotate(Axis.XAXIS, toRadian(yDistance));
						model.rotate(Axis.YAXIS, toRadian(xDistance));
						break;
					}
					model.notifyObservers();
				}
				oldMouseX = mouseX;
				oldMouseY = mouseY;
			}

		});
	}
	
	/**
	 * 
	 */
	public void setMousePressed(Canvas canvas) {
		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();
				oldMouseX = mouseX;
				oldMouseY = mouseY;
			}
		});
	}
}

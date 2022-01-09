package controler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import connectable.Observer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import modele.Model;
import ply.exceptions.FormatPlyException;
import util.Axis;
import util.Conversion;
import util.DrawingMethod;
import util.Theme;
import views.ModelisationCanvas;
import views.View;
/**
 * 
 * Cette classe sert à gérer les évenements utilisant le {@link Model}. Il réalise les changements 
 *
 * 
 */
public class Controller {

	private Model model = null;
	private View view = null;

	private double oldMouseX;
	private double oldMouseY;

	public Controller(View view) {
		this.view = view;
		this.model = new Model();
	}
	/**
	 * Charge un fichier et notifie l'affichage qui est associé
	 * @param path chemin vers le ficheir
	 * @throws FileNotFoundException Le fichier n'existe pas
	 * @throws FormatPlyException Le format n'est pas celui accepté (ply)
	 */
	public void loadFile(String path) throws FileNotFoundException, FormatPlyException {
		if (this.model == null)
			model = new Model();
		if (path != null) {
			this.model.loadFile(path);
			for (Observer observer : getCanvases()) {
				this.model.attach(observer);
			}
			this.model.notifyObservers();
		}

	}

	/**
	 * Réalise une rotation du model en fonction d'un axe 
	 * @param axis axe de rotation
	 * @param theta 
	 */
	public void rotateModel(Axis axis, double theta) {
		if (this.model == null)
			return;
		this.model.rotate(axis, Conversion.toRadian(theta));
	}

	/**
	 * Réalise une rotation du model en fonction d'un axe 
	 * @param axis axe de rotation
	 * @param distance 
	 */
	public void translateModel(Axis axis, double distance) {
		if (this.model == null)
			return;
		this.model.translate(axis, distance);
	}


	/*
	 * GETTERS AND SETTERS
	 */
	
	public List<ModelisationCanvas> getCanvases() {
		return this.view.getCanvases();
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setZoom(double zoom) {
		for (ModelisationCanvas canvas : getCanvases()) {
			canvas.setZoom(zoom);
		}
		model.notifyObservers();
	}
	

	/**
	 * Est l'évènement qui permet à l'utilisateur de choisir un fichier parmi toute une selection
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
					view.erreur(e);
				}
			}

		});
	}

	/**
	 * Est l'évènement qui permet de faire une rotation  vers le bas du modele visiualisé par l'utilisateur
	 * @param down
	 */
	public void setOnDown(Button down) {
		down.setOnAction(e -> {
			double theta = -4.0;
			setOnDownAndUp(theta);

			this.model.notifyObservers();
		});
	}

	/**
	 * Est l'évènement qui permet de faire une rotation vers le haut du modele visiualisé par l'utilisateur
	 * @param up
	 */
	public void setOnUp(Button up) {
		up.setOnAction(e -> {
			double theta = 4.0;
			setOnDownAndUp(theta);

			this.model.notifyObservers();
		});
	}

	/**
	 * Est l'évènement qui permet de faire une rotation vers la gauche du modele visiualisé par l'utilisateur
	 * @param left
	 */
	public void setOnLeft(Button left) {
		left.setOnAction(e -> {
			double theta = -4.0;
			setOnRightAndLeft(theta);

			this.model.notifyObservers();
		});
	}

	/**
	 * Est l'évènement qui permet de faire une rotation vers la droite du modele visiualisé par l'utilisateur
	 * @param right
	 */
	public void setOnRight(Button right) {
		right.setOnAction(e -> {
			double theta = 4.0;
			setOnRightAndLeft(theta);
			this.model.notifyObservers();
		});
	}
	
	/**
	 * Permet la rotation vers la droite et la gauche de l'utilisateur
	 * @param theta
	 * TODO mettre en private + observeur/observé
	 */
	public void setOnRightAndLeft(double theta) {
		for (ModelisationCanvas canvas : getCanvases()) {
			switch (canvas.getAxis()) {
			case XAXIS:
				rotateModel(Axis.YAXIS, theta);
				break;
			case YAXIS:
				rotateModel(Axis.XAXIS, theta);
				break;
			case ZAXIS:
				rotateModel(Axis.YAXIS, theta);
				break;
			}
		}
	}
	
	/**
	 * Permet la rotation vers le haut et le bas de l'utilisateur
	 * @param theta
	 */
	public void setOnDownAndUp(double theta) {
		for (ModelisationCanvas canvas : getCanvases()) {
			switch (canvas.getAxis()) {
			case XAXIS:
				rotateModel(Axis.ZAXIS, theta);
				break;
			case YAXIS:
				rotateModel(Axis.ZAXIS, theta);
				break;
			case ZAXIS:
				rotateModel(Axis.XAXIS, theta);
				break;
			}
		}
	}
	
	
	/**
	 * Fait une rotation du modèle lorsqu'on 
	 * @param node
	 */
	public void addKeyPressedEvent(Node node) {
		node.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			double theta = 1.0;
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.RIGHT) {
					rotateModel(Axis.YAXIS, theta);
					model.notifyObservers();
				}
				if (e.getCode() == KeyCode.LEFT) {
					rotateModel(Axis.YAXIS, -theta);
					model.notifyObservers();
				}
				if (e.getCode() == KeyCode.UP) {
					rotateModel(Axis.XAXIS, theta);
					model.notifyObservers();
				}
				if (e.getCode() == KeyCode.DOWN) {
					rotateModel(Axis.XAXIS, -theta);
					model.notifyObservers();
				}
			}
		});
	}

	/**
	 * Quitte l'application lorsque le bouton "exit" est cliqué
	 * @param exitItem
	 */
	public void setExitAction(MenuItem exitItem) {
		exitItem.setOnAction(event -> Platform.exit());
	}

	/**
	 * Fait apparaitre un menu dans lequel il y a une explication de comment fonctionne l'application
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
	 * Permet de changer la méthode de dessin lorsque l'utilisateur le demande
	 * @param method
	 * @param radioItem
	 */
	public void setOnMethodChangerAction(DrawingMethod method, RadioMenuItem radioItem) {
		radioItem.setOnAction(event -> {
			this.view.setMethod(method);
			this.model.notifyObservers();
		});
	}

	/**
	 * Permet de changer la charger un fichier/modele ply lorsque l'utilisateur le demande
	 * @param f
	 * @param item
	 * TODO erreur ply récupéré ?
	 */
	public void setLoadFileItem(File f, MenuItem item) {
		item.setOnAction(event -> {
			try {
				loadFile(f.getPath());
			} catch (FileNotFoundException | FormatPlyException error) {
				error.printStackTrace();
			}
			for (ModelisationCanvas canvas : getCanvases())
				canvas.clearCanvas();
			this.model.notifyObservers();
		});
	}

	/**
	 * Réalise le déplacement du modèle lorsque l'utilisateur fait un mouvement de "glissement"
	 */
	public void setMouseDragging(ModelisationCanvas canvas) {
		canvas.setOnMouseDragged(event ->{
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();
				double xDistance = mouseX - oldMouseX;
				double yDistance = mouseY - oldMouseY;
				if (event.getButton().equals(MouseButton.SECONDARY)) {
					switch (canvas.getAxis()) {
					case XAXIS:
						model.translate(Axis.ZAXIS, xDistance).translate(Axis.YAXIS, yDistance);
						break;
					case YAXIS:
						model.translate(Axis.XAXIS, xDistance).translate(Axis.ZAXIS, yDistance);
						break;
					case ZAXIS:
						model.translate(Axis.XAXIS, xDistance).translate(Axis.YAXIS, yDistance);
						break;
					}
					model.notifyObservers();
				} else if (event.getButton().equals(MouseButton.PRIMARY)) {
					switch (canvas.getAxis()) {
					case XAXIS:
						model.rotate(Axis.ZAXIS, Conversion.toRadian(yDistance)).rotate(Axis.YAXIS, Conversion.toRadian(xDistance));
						break;
					case YAXIS:
						model.rotate(Axis.XAXIS, Conversion.toRadian(yDistance)).rotate(Axis.ZAXIS, Conversion.toRadian(xDistance));
						break;
					case ZAXIS:
						model.rotate(Axis.XAXIS, Conversion.toRadian(yDistance)).rotate(Axis.YAXIS, Conversion.toRadian(xDistance));
						break;
					}
					model.notifyObservers();
				}
				oldMouseX = mouseX;
				oldMouseY = mouseY;
			}

		);
	}

	/**
	 * Met à jour les coordonnées de la souris, lorsque celle-ci est pressée
	 * @param canvas
	 */
	public void setMousePressed(ModelisationCanvas canvas) {
		canvas.setOnMousePressed(event ->{
				oldMouseX = event.getSceneX();
				oldMouseY = event.getSceneY();
		});
	}

}

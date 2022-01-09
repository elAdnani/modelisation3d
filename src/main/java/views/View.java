package views;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import controler.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ply.PlyProperties;
import ply.RecuperationPly;
import ply.exceptions.FormatPlyException;
import util.Axis;
import util.Conversion;
import util.DrawingMethod;
import util.PlyFileFilter;
import util.Theme;
/**
 * 
 * Cette classe sert est l'affichage de la fenêtre du projet de modélisation.</br>
 * Elle est composé : </br>
 * - D'une barre d'outil pour divers option </br>
 * - Une liste d'image dans laquelle sont affichées la géométrie d'un modele </br>
 * - D'une liste de proposition de fichier pouvant être affiché sur les images </br>
 * - Des boutons et un glisseur pour modifier la structure géométrique du modele </br>
 */
@SuppressWarnings("PMD.LawOfDemeter")
public class View extends Stage {

	private List<ModelisationCanvas> canvases = null;
	private Slider zoomSlider = null;

	private double zoom = 10;
	
	private double defaultWidthPerc = 0.80;

	private static String path = System.getProperty("user.dir") + File.separator + "exemples" + File.separator;

	private Controller controller = null;

	/**
	 * Réalise l'interface
	 */
	public View() {
		super();
		setTitle("Modélisateur 3D");

		controller = new Controller(this);

		/* INITIALISATION DU CANVAS */
		canvases = new ArrayList<>();
		canvases.add(createCanvas(Screen.getPrimary().getBounds().getWidth() / 2 * defaultWidthPerc,
				Screen.getPrimary().getBounds().getHeight() / 2, Axis.ZAXIS));
		canvases.add(createCanvas(Screen.getPrimary().getBounds().getWidth() / 2 * defaultWidthPerc,
				Screen.getPrimary().getBounds().getHeight() / 2, Axis.XAXIS));
		canvases.add(createCanvas(Screen.getPrimary().getBounds().getWidth() * defaultWidthPerc,
				Screen.getPrimary().getBounds().getHeight() / 2 - 50, Axis.YAXIS));

		GridPane canvasPane = new GridPane();
		setupCanvasGridpane(canvasPane, canvases);

		zoomSlider = createSlider();

		/* CREATION DE LA FENETRE */
		VBox vBox = new VBox();
		Scene scene = new Scene(vBox);

		MenuBar menuBar = ToolView.createMenuBar(controller, canvases.get(0), this,path);
		vBox.getChildren().add(menuBar);

		/* CREATION DES OUTILS */
		VBox outils = createToolBox();
		TabPane searchModelBar = createSearchModel();
		outils.getChildren().add(searchModelBar);

		/* INITIALISATION DU BORDERPANE */
		HBox root = new HBox();
		root.getChildren().addAll(canvasPane, outils);
		vBox.getChildren().addAll(root);

		controller.addKeyPressedEvent(vBox);
		vBox.getStyleClass().add("scene");

		setScene(scene);
		setMaximized(true);
		show();

		searchModelBar.lookup(".tab-pane .tab-header-area .tab-header-background")
				.setStyle("-fx-background-color: transparent;");
	}

	/**
	 * Réalise la barre de menu affichant les fichiers lisibles dans un dossier défini
	 * @return
	 */
	private TabPane createSearchModel() {
		return  ToolView.createSearchModel(controller, this);
	}
	/**
	 * Positionne les images ({@link Canvas}) dans une grille fléxible ({@link GridPane})
	 * @param canvasPane 
	 * @param canvases
	 */
	private void setupCanvasGridpane(GridPane canvasPane, List<ModelisationCanvas> canvases) {
		canvasPane.add(canvases.get(0), 0, 0);
		canvasPane.add(canvases.get(1), 1, 0);
		canvasPane.add(canvases.get(2), 0, 1, 2, 1);
	}

	/**
	 * Réalise la barre d'outil permettant le control des images {@link Canvas}
	 * 
	 * @return
	 */
	private VBox createToolBox() {
		VBox outils = new VBox();

		outils.setPrefWidth(Screen.getPrimary().getBounds().getWidth() * (1 - defaultWidthPerc));
		outils.addEventFilter(KeyEvent.KEY_PRESSED, ek -> {
			if (ek.getCode() == KeyCode.UP || ek.getCode() == KeyCode.DOWN || ek.getCode() == KeyCode.RIGHT
					|| ek.getCode() == KeyCode.LEFT) {
				ek.consume();
			}
		});

		Label nom = new Label("Menu");
		Label nomZoom = new Label("Zoom");

		Button upButton = new Button("↑");
		Button downButton = new Button("↓");
		Button rightButton = new Button("→");
		Button leftButton = new Button("←");

		Button plusButton = new Button("+");
		Button minusButton = new Button("-");

		GridPane moveButtons = new GridPane();

		moveButtons.add(leftButton, 0, 1);
		moveButtons.add(upButton, 1, 0);
		moveButtons.add(downButton, 1, 2);
		moveButtons.add(rightButton, 2, 1);

		HBox zoomBox = new HBox(5);
		zoomBox.getChildren().addAll(minusButton, zoomSlider, plusButton);

		zoomBox.setAlignment(Pos.CENTER);
		outils.setAlignment(Pos.TOP_CENTER);
		moveButtons.setAlignment(Pos.TOP_CENTER);

		outils.getChildren().addAll(nom, moveButtons, nomZoom, zoomBox);

		nom.setPadding(new Insets(10, 0, 30, 0));
		nomZoom.setPadding(new Insets(80, 0, 30, 0));

		upButton.setPrefSize(60, 60);
		downButton.setPrefSize(60, 60);
		rightButton.setPrefSize(60, 60);
		leftButton.setPrefSize(60, 60);

		plusButton.setPrefSize(35, 35);
		minusButton.setPrefSize(35, 35);

		controller.setOnRight(rightButton);

		controller.setOnLeft(leftButton);

		controller.setOnUp(upButton);

		controller.setOnDown(downButton);
		double zoomIncrement = 10.0;
		double maxzoom = 100;
		plusButton.setOnAction(e -> {
			if (this.zoom + zoomIncrement < zoomSlider.maxProperty().doubleValue() * maxzoom) {
				zoomSlider.setValue(zoomSlider.getValue() + zoomIncrement / maxzoom);
				controller.setZoom(this.zoom + zoomIncrement);
			} else {
				zoomSlider.setValue(zoomSlider.maxProperty().doubleValue());
				controller.setZoom(zoomSlider.maxProperty().doubleValue() * maxzoom);
			}
			this.zoom = canvases.get(0).getZoom();
		});

		minusButton.setOnAction(e -> {
			if (this.zoom - zoomIncrement > 0) {
				zoomSlider.setValue(zoomSlider.getValue() - zoomIncrement / maxzoom);
				controller.setZoom(this.zoom - zoomIncrement);
			} else {
				zoomSlider.setValue(0);
				controller.setZoom(0);
			}
			this.zoom = canvases.get(0).getZoom();
		});
		outils.getStyleClass().add("outils");
		return outils;
	}
	/**
	 * Réalise le slider modifiant la taille du modèle
	 * @return le slider
	 */
	private Slider createSlider() {
		double maxzoom = 100;
		Slider slider = configureSlider();
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.setZoom(zoomSlider.getValue() * maxzoom);
				zoom = zoomSlider.getValue() * maxzoom;
			}
		});
		this.setZoom(slider.getValue()*maxzoom);
		return slider;
	}
	/**
	 * Définie un slider
	 * @return
	 */
	private Slider configureSlider() {
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(10);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(1);
		slider.setValue(zoom);
		return slider;
	}
	
	
	private ModelisationCanvas createCanvas(double width, double height, Axis axis) {
		return createCanvas(width, height, axis, DrawingMethod.WIREFRAME);
	}

	/**
	 * Créer un canvas {@link ModelisationCanvas} 
	 * @param width
	 * @param height
	 * @param axis
	 * @param method
	 * @return
	 */
	private ModelisationCanvas createCanvas(double width, double height, Axis axis, DrawingMethod method) {
		ModelisationCanvas canvas = new ModelisationCanvas(width, height, axis, method);

		canvas.getGraphicsContext2D().setStroke(Color.BLACK);
		canvas.getGraphicsContext2D().setFill(Color.GREY);

		controller.setMousePressed(canvas);
		controller.setMouseDragging(canvas);

		return canvas;
	}


	/**
	 * Récupère une liste d'information sur les fichiers ply dans un dossier défini
	 * @param regex lien vers le dossier
	 * @return une liste de {@link PlyProperties}
	 */
	public List<PlyProperties> getModels(String regex) {
		List<PlyProperties> models = new ArrayList<>();
		File directory = new File(path);
		FileFilter filter = new PlyFileFilter();
		File[] liste = directory.listFiles(filter);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Pattern pattern = null;
		if (regex != null) {
			pattern = Pattern.compile(regex);
		}
		for (File f : liste) {
			try {
				if (regex == null || pattern.matcher(f.getName()).find()) {
					Path filepath = Path.of(f.getPath());
					FileOwnerAttributeView owner = Files.getFileAttributeView(filepath, FileOwnerAttributeView.class);
					BasicFileAttributes attributes = Files.readAttributes(filepath, BasicFileAttributes.class);
					models.add(new PlyProperties(f.getPath(), f.getName(), Conversion.getSize(attributes.size()),
							RecuperationPly.getNbFaces(filepath.toString()),
							RecuperationPly.getNbVertices(filepath.toString()),
							formatter.format(new Date(attributes.creationTime().toMillis())),
							owner.getOwner().getName()));
				}
			} catch (IOException | FormatPlyException e) {
				e.printStackTrace();
			}
		}

		return models;

	}

	/**
	 * Affiche une fenêtre informant l'utilisateur qu'une erreur s'est produite
	 */
	public void error(Exception  exception) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(exception.getClass().getSimpleName());
		alert.setHeaderText("Vous avez rencontrez un problème");
		alert.setContentText(exception.getMessage());
		alert.showAndWait();

	}

	public List<ModelisationCanvas> getCanvases() {
		return canvases;
	}

	/**
	 * Change le {@link Theme} de la fenêtre
	 * @param theme thème de la fenêtre
	 */
	public void changeTheme(Theme theme) {
		if (theme != null) {
			if (theme.equals(Theme.DEFAULT)) {
				this.getScene().getStylesheets().clear();
			} else {
				URL resourcecss = getClass().getResource(theme.getPath());
				if (resourcecss != null) {
					String css = resourcecss.toExternalForm();
					this.getScene().getStylesheets().clear();
					this.getScene().getStylesheets().add(css);
				}
			}
		}
	}

	/**
	 * Modifie la manière de dessiner sur le canvas
	 * @param m
	 */
	public void setMethod(DrawingMethod m) {
		for (ModelisationCanvas modelCanvas : canvases) {
			modelCanvas.setMethod(m);
		}
	}
	
	/**
	 * Modifie la manière de dessiner sur le canvas
	 * @param m
	 */
	public void setZoom(double zoom) {
		for (ModelisationCanvas modelCanvas : canvases) {
			modelCanvas.setZoom(zoom);
		}
	}
}

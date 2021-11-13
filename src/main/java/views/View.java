package views;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;
import modele.Model;
import modele.RecuperationPly;
import util.Axis;
import util.DrawingMethod;
import util.PlyFileFilter;

public class View extends Stage {

	private Canvas canvas = null;

	public Slider zoomSlider = null;
	public double zoomIncrement = 10.0;

	private double defaultCanvasWidthPercentile = 0.85;

	private String file = null;
	private static String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
			+ File.separator + "resources" + File.separator + "models" + File.separator;

	private Affichage affichage = null;
	private DrawingMethod method = null;
	protected double oldMouseX;
	protected double oldMouseY;

	public View() {
		this(Axis.ZAXIS);
	}

	public View(Axis axis) {
		this(axis, DrawingMethod.BOTH);
	}

	public View(DrawingMethod method) {
		this(Axis.ZAXIS, method);
	}

	public View(Axis axis, DrawingMethod method) {
		this.method = method;

		setTitle("Modélisateur 3D");

		/* INITIALISATION DU CANVAS */
		createCanvas(Screen.getPrimary().getBounds().getWidth() * defaultCanvasWidthPercentile,
				Screen.getPrimary().getBounds().getHeight());

		affichage = new Affichage(this, axis);

		zoomSlider = createSlider();

		MenuBar menuBar = createMenuBar();

		/* CREATION DES OUTILS */
		VBox outils = createToolBox();

		/* CREATION DE LA FENETRE */
		VBox vBox = new VBox(menuBar);

		/* INITIALISATION DU BORDERPANE */
		HBox root = new HBox();
		root.getChildren().addAll(canvas, outils);
		vBox.getChildren().addAll(root);

		loadFile(file);
		drawModel();

		vBox.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.RIGHT) {
					affichage.rotateModel(Axis.YAXIS, 1);
					drawModel();
				}
				if (e.getCode() == KeyCode.LEFT) {
					affichage.rotateModel(Axis.YAXIS, -1);
					drawModel();

				}
				if (e.getCode() == KeyCode.UP) {
					affichage.rotateModel(Axis.XAXIS, 1);
					drawModel();
				}
				if (e.getCode() == KeyCode.DOWN) {
					affichage.rotateModel(Axis.XAXIS, -1);
					drawModel();

				}
			}
		});
		vBox.getStyleClass().add("scene");
		Scene scene = new Scene(vBox);
		URL resourcecss = getClass().getResource("../styles/darkmode.css");
		if (resourcecss != null) {
			String css = resourcecss.toExternalForm();
			scene.getStylesheets().add(css);
		}
		setScene(scene);
		show();
	}

	private MenuBar createMenuBar() {
		// TODO Ajouter Aide

		MenuBar menuBar = new MenuBar();

		Menu fileMenu = new Menu("Fichier");
		Menu ressourceMenu = new Menu("Ressources");
		Menu viewMenu = new Menu("View");
		Menu helpMenu = new Menu("Aide");

		MenuItem openFileItem = new MenuItem("Open File...");
		MenuItem exitItem = new MenuItem("Exit");

		RadioMenuItem haut = new RadioMenuItem("Haut");
		RadioMenuItem face = new RadioMenuItem("Face");
		RadioMenuItem droit = new RadioMenuItem("Droit");

		SeparatorMenuItem sep = new SeparatorMenuItem();
		SeparatorMenuItem sepView = new SeparatorMenuItem();

		ToggleGroup grp = new ToggleGroup();
		ToggleGroup grpView = new ToggleGroup();

		fileMenu.getItems().add(openFileItem);
		fileMenu.getItems().add(ressourceMenu);
		fileMenu.getItems().add(sep);
		fileMenu.getItems().add(exitItem);

		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(viewMenu);
		menuBar.getMenus().add(helpMenu);

		ressourceMenu.getItems().addAll(createRessourcePlyMenu());

		openFileItem.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open File");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Ply File", "*.ply"),
					new ExtensionFilter("All Files", "*.*"));
			File choosedfile = fileChooser.showOpenDialog(null);

			if (file != null) {
				file = choosedfile.getAbsolutePath();
				affichage.getModel().loadFile(file);
				drawModel();
			}

		});

		exitItem.setOnAction(event -> {
			Platform.exit();
		});

		for (DrawingMethod m : DrawingMethod.values()) {
			RadioMenuItem radioItem = new RadioMenuItem(m.name());
			radioItem.setToggleGroup(grp);
			radioItem.setOnAction(event -> {
				this.method = m;
				drawModel();
			});
			if (m.equals(method))
				radioItem.setSelected(true);
			viewMenu.getItems().add(radioItem);
		}

		viewMenu.getItems().add(sepView);

		haut.setToggleGroup(grpView);
		haut.setOnAction(event -> {
			this.affichage.setAxis(Axis.YAXIS);
			drawModel();
		});

		face.setToggleGroup(grpView);
		face.setOnAction(event -> {
			this.affichage.setAxis(Axis.XAXIS);
			drawModel();
		});
		droit.setToggleGroup(grpView);
		droit.setOnAction(event -> {
			this.affichage.setAxis(Axis.ZAXIS);
			drawModel();
		});

		if (this.affichage.getAxis().equals(Axis.XAXIS))
			face.setSelected(true);
		else if (this.affichage.getAxis().equals(Axis.YAXIS))
			haut.setSelected(true);
		else if (this.affichage.getAxis().equals(Axis.ZAXIS))
			droit.setSelected(true);

		viewMenu.getItems().addAll(haut, face, droit);

		return menuBar;
	}

	private VBox createToolBox() {
		VBox outils = new VBox();

		outils.setPrefWidth(Screen.getPrimary().getBounds().getWidth() * (1 - defaultCanvasWidthPercentile));
		outils.addEventFilter(KeyEvent.KEY_PRESSED, ek -> {
			if (ek.getCode() == KeyCode.UP || ek.getCode() == KeyCode.DOWN || ek.getCode() == KeyCode.RIGHT
					|| ek.getCode() == KeyCode.LEFT)
				ek.consume();
		});

		Label nom = new Label("Menu");
		Label nomZoom = new Label("Zoom");
		Button face = new Button("Vue de face");
		Button droite = new Button("Vue de droite");
		Button dessus = new Button("Vue de haut");

		Button up = new Button("↑");
		Button down = new Button("↓");
		Button right = new Button("→");
		Button left = new Button("←");

		Button plus = new Button("+");
		Button moins = new Button("-");

		GridPane deplacementsButtons = new GridPane();

		deplacementsButtons.add(left, 0, 1);
		deplacementsButtons.add(up, 1, 0);
		deplacementsButtons.add(down, 1, 2);
		deplacementsButtons.add(right, 2, 1);

		VBox position = new VBox(4);
		position.getChildren().addAll(face, droite, dessus);

		VBox.setMargin(dessus, new Insets(0, 0, 90, 0));

		HBox zoom = new HBox(5);
		zoom.getChildren().addAll(moins, zoomSlider, plus);

		zoom.setAlignment(Pos.CENTER);
		outils.setAlignment(Pos.TOP_CENTER);
		position.setAlignment(Pos.TOP_CENTER);
		deplacementsButtons.setAlignment(Pos.TOP_CENTER);

		outils.getChildren().addAll(nom, position, deplacementsButtons, nomZoom, zoom);

		nom.setPadding(new Insets(10, 0, 30, 0));
		nomZoom.setPadding(new Insets(80, 0, 30, 0));

		face.setPrefSize(280, 60);
		droite.setPrefSize(280, 60);
		dessus.setPrefSize(280, 60);

		up.setPrefSize(60, 60);
		down.setPrefSize(60, 60);
		right.setPrefSize(60, 60);
		left.setPrefSize(60, 60);

		plus.setPrefSize(35, 35);
		moins.setPrefSize(35, 35);

		right.setOnAction(e -> {
			affichage.rotateModel(Axis.YAXIS, 4);
			drawModel();
		});

		left.setOnAction(e -> {
			affichage.rotateModel(Axis.YAXIS, -4);
			drawModel();
		});

		up.setOnAction(e -> {
			affichage.rotateModel(Axis.XAXIS, 4);
			drawModel();
		});

		down.setOnAction(e -> {
			affichage.rotateModel(Axis.XAXIS, -4);
			drawModel();
		});

		face.setOnAction(e -> {
			View fv = new View(Axis.XAXIS, this.method);
			fv.affichage.setModel(new Model());
			fv.affichage.getModel().copy(this.affichage.getModel());
			fv.affichage.setZoom(this.affichage.getZoom());
			fv.affichage.biconnectTo(this.affichage);
			fv.drawModel();
		});

		dessus.setOnAction(e -> {
			View dv = new View(Axis.YAXIS, this.method);
			dv.affichage.setModel(new Model());
			dv.affichage.getModel().copy(this.affichage.getModel());
			dv.affichage.setZoom(this.affichage.getZoom());
			dv.affichage.biconnectTo(this.affichage);
			dv.drawModel();
		});

		droite.setOnAction(e -> {
			View drv = new View(Axis.ZAXIS, this.method);
			drv.affichage.setModel(new Model());
			drv.affichage.getModel().copy(this.affichage.getModel());
			drv.affichage.setZoom(this.affichage.getZoom());
			drv.affichage.biconnectTo(this.affichage);
			drv.drawModel();
		});

		plus.setOnAction(e -> {
			if (affichage.zoom + zoomIncrement < zoomSlider.maxProperty().doubleValue() * 100) {
				zoomSlider.setValue(zoomSlider.getValue() + zoomIncrement / 100);
				affichage.setZoom(affichage.getZoom() + zoomIncrement);
				drawModel();
			} else {
				zoomSlider.setValue(zoomSlider.maxProperty().doubleValue());
				affichage.setZoom(zoomSlider.maxProperty().doubleValue() * 100);
			}
		});

		moins.setOnAction(e -> {
			if (affichage.zoom - zoomIncrement > 0) {
				zoomSlider.setValue(zoomSlider.getValue() - zoomIncrement / 100);
				affichage.setZoom(affichage.getZoom() - zoomIncrement);
				drawModel();
			} else {
				zoomSlider.setValue(0);
				affichage.setZoom(0);
			}
		});
		outils.getStyleClass().add("outils");
		return outils;
	}

	private Slider createSlider() {
		Slider slider = configureSlider();
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				affichage.setZoom(zoomSlider.getValue() * 100);
				drawModel();
			}
		});
		return slider;
	}

	private Slider configureSlider() {
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(10);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(1);
		slider.setValue(affichage.getZoom());
		return slider;
	}

	/**
	 * Create and replace (if already existing) a canvas of given width and height
	 * 
	 * @param width
	 * @param height
	 */
	private void createCanvas(double width, double height) {
		canvas = new Canvas(width, height);

		canvas.getGraphicsContext2D().setStroke(Color.BLACK);
		canvas.getGraphicsContext2D().setFill(Color.GREY);

		/* DEPLACEMENT SOURIS */
		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();
				oldMouseX = mouseX;
				oldMouseY = mouseY;
			}
		});

		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();
				double xDistance = mouseX - oldMouseX;
				double yDistance = mouseY - oldMouseY;
				System.out.println(event.getButton().name());
				if (event.getButton().equals(MouseButton.SECONDARY)) {
					switch (affichage.getAxis()) {
					case XAXIS:
						affichage.translateModel(Axis.ZAXIS, xDistance);
						affichage.translateModel(Axis.YAXIS, yDistance);
						break;
					case YAXIS:
						affichage.translateModel(Axis.XAXIS, xDistance);
						affichage.translateModel(Axis.ZAXIS, yDistance);
						break;
					case ZAXIS:
						affichage.translateModel(Axis.XAXIS, xDistance);
						affichage.translateModel(Axis.YAXIS, yDistance);
						break;
					}
				} else if (event.getButton().equals(MouseButton.PRIMARY)) {
					affichage.rotateModel(Axis.XAXIS, yDistance);
					affichage.rotateModel(Axis.YAXIS, xDistance);
				}
				oldMouseX = mouseX;
				oldMouseY = mouseY;
				drawModel();
			}

		});
	}

	/**
	 * Replace the current canvas by a new sized one and call the
	 * {@link #drawModel()}
	 * 
	 * @param width  Width of the new canvas
	 * @param height Height of the new canvas
	 */
	private void resizeCanvas(double width, double height) {
		createCanvas(width, height);
		drawModel();
	}

	/**
	 * Call the method {@link Affichage#clearCanvas(Canvas)} if the canvas is not
	 * null
	 */
	private void clearCanvas() {
		if (canvas != null)
			this.affichage.clearCanvas();
	}

	/**
	 * Create a {@link Collection<MenuItem>} of {@link MenuItem}
	 * 
	 * @return {@link Collection<MenuItem>}<{@link MenuItem}>
	 */
	private Collection<MenuItem> createRessourcePlyMenu() {
		File directory = new File(path);
		FileFilter filter = new PlyFileFilter();
		File[] liste = directory.listFiles(filter);
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		for (File f : liste) {
			MenuItem item = new MenuItem("Erreur!");
			try {
				Path filepath = Path.of(f.getPath());
				System.out.println("SizeOf " + f.getName() + ": " + (Files.size(filepath)) + " bytes");
				item = new MenuItem(f.getName() + "(" + getSize(Files.size(filepath)) + ")"+
				"\nfaces:" + RecuperationPly.getNBFaces(filepath.toString()) +
				"; points:"+RecuperationPly.getNBVertices(filepath.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			item.setOnAction(event -> {
				loadFile(f.getPath());
				clearCanvas();
				drawModel();
			});

			menuItems.add(item);
		}

		return menuItems;
	}

	/**
	 * Call the {@link Affichage#drawModel(Canvas)} method if the canvas is not null
	 */
	public void drawModel() {
		if (canvas != null)
			affichage.drawModel(method);
	}

	public void loadFile(String file) {
		if (file == null || file.isEmpty())
			return; // TODO Remplacer le return par une erreur
		affichage.loadFile(file);
	}

	public static String getSize(long size) {
		String s = "";
		long kilo = 1024;
		long mega = kilo * kilo;
		long giga = mega * kilo;
		long tera = giga * kilo;

		double kb = (double) size / kilo;
		double mb = kb / kilo;
		double gb = mb / kilo;
		double tb = gb / kilo;
		if (size < kilo) {
			s = size + " Bytes";
		} else if (size >= kilo && size < mega) {
			s = String.format("%.2f", kb) + " KB";
		} else if (size >= mega && size < giga) {
			s = String.format("%.2f", mb) + " MB";
		} else if (size >= giga && size < tera) {
			s = String.format("%.2f", gb) + " GB";
		} else if (size >= tera) {
			s = String.format("%.2f", tb) + " TB";
		}
		return s;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public DrawingMethod getDrawMethod() {
		return method;
	}
}
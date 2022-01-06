package views;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import modele.Model;
import ply.RecuperationPly;
import ply.exceptions.FormatPlyException;
import util.Axis;
import util.DrawingMethod;
import util.PlyFileFilter;
import util.Theme;

@SuppressWarnings("PMD.LawOfDemeter")
public class View extends Stage {

	private ModelisationCanvas canvas = null;

	public Slider zoomSlider = null;
	public double zoomIncrement = 10.0;

	private double defaultCanvasWidthPercentile = 0.85;

	private String file = null;
	private static String path = System.getProperty("user.dir") + File.separator + "exemples" + File.separator;

	private Controller controller = null;
	private DrawingMethod method = null;

	protected double oldMouseX;
	protected double oldMouseY;

	private Theme theme = Theme.DEFAULT;

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

		controller = new Controller(this, axis);

		/* INITIALISATION DU CANVAS */
		createCanvas(Screen.getPrimary().getBounds().getWidth() * defaultCanvasWidthPercentile,
				Screen.getPrimary().getBounds().getHeight());


		zoomSlider = createSlider();

		/* CREATION DE LA FENETRE */
		VBox vBox = new VBox();
		Scene scene = new Scene(vBox);

		MenuBar menuBar = createMenuBar();
		vBox.getChildren().add(menuBar);

		/* CREATION DES OUTILS */
		VBox outils = createToolBox();

		/* INITIALISATION DU BORDERPANE */
		HBox root = new HBox();
		root.getChildren().addAll(canvas, outils);
		vBox.getChildren().addAll(root);

		controller.addKeyPressedEvent(vBox);
		vBox.getStyleClass().add("scene");
		// URL resourcecss = getClass().getResource("../styles/darkmode.css");
		// if (resourcecss != null) {
		// String css = resourcecss.toExternalForm();
		// scene.getStylesheets().add(css);
		// }
		setScene(scene);
		setMaximized(true);
		show();
	}

	private MenuBar createMenuBar() {

		MenuBar menuBar = new MenuBar();

		Menu fileMenu = new Menu("Fichier");
		Menu ressourceMenu = new Menu("Ressources");
		Menu viewMenu = new Menu("View");
		Menu helpMenu = new Menu("Help");
		Menu themeMenu = new Menu("Themes");

		MenuItem openFileItem = new MenuItem("Open File...");
		MenuItem exitItem = new MenuItem("Exit");
		MenuItem helpItem = new MenuItem("Show Help");

		RadioMenuItem haut = new RadioMenuItem("Haut");
		RadioMenuItem face = new RadioMenuItem("Face");
		RadioMenuItem droit = new RadioMenuItem("Droit");

		RadioMenuItem defaultTheme = new RadioMenuItem(Theme.DEFAULT.toString());
		RadioMenuItem darkTheme = new RadioMenuItem(Theme.DARK.toString());
		RadioMenuItem solarisTheme = new RadioMenuItem(Theme.SOLARIS.toString());

		SeparatorMenuItem sep = new SeparatorMenuItem();
		SeparatorMenuItem sepView = new SeparatorMenuItem();

		ToggleGroup grp = new ToggleGroup();
		ToggleGroup grpView = new ToggleGroup();
		ToggleGroup themeView = new ToggleGroup();

		fileMenu.getItems().add(openFileItem);
		fileMenu.getItems().add(ressourceMenu);
		fileMenu.getItems().add(sep);
		fileMenu.getItems().add(exitItem);

		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(viewMenu);
		menuBar.getMenus().add(themeMenu);
		menuBar.getMenus().add(helpMenu);

		helpMenu.getItems().add(helpItem);

		ressourceMenu.getItems().addAll(createRessourcePlyMenu());

		controller.setFileChooserEvent(openFileItem);

		controller.setExitAction(exitItem);

		controller.setHelpAction(helpItem);

		for (DrawingMethod m : DrawingMethod.values()) {
			RadioMenuItem radioItem = new RadioMenuItem(m.name());
			radioItem.setToggleGroup(grp);
			controller.setOnMethodChangerAction(m, radioItem);
			if (m.equals(method))
				radioItem.setSelected(true);
			viewMenu.getItems().add(radioItem);
		}

		viewMenu.getItems().add(sepView);

		haut.setToggleGroup(grpView);
		controller.setOnHaut(haut);

		face.setToggleGroup(grpView);
		controller.setOnFace(face);

		droit.setToggleGroup(grpView);
		controller.setOnDroit(droit);

		//
		// if (this.controller.getAxis().equals(Axis.XAXIS))
		// face.setSelected(true);
		// else if (this.controller.getAxis().equals(Axis.YAXIS))
		// haut.setSelected(true);
		// else if (this.controller.getAxis().equals(Axis.ZAXIS))
		// droit.setSelected(true);

		viewMenu.getItems().addAll(haut, face, droit);

		defaultTheme.setToggleGroup(themeView);
		darkTheme.setToggleGroup(themeView);
		solarisTheme.setToggleGroup(themeView);

		defaultTheme.setOnAction(event -> {
			changeTheme(Theme.DEFAULT);
		});

		darkTheme.setOnAction(event -> {
			changeTheme(Theme.DARK);
		});
		
		solarisTheme.setOnAction(event -> {
			changeTheme(Theme.SOLARIS);
		});

		defaultTheme.setSelected(true);

		themeMenu.getItems().add(defaultTheme);
		themeMenu.getItems().add(darkTheme);
		themeMenu.getItems().add(solarisTheme);

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

		controller.setOnRight(right);

		controller.setOnLeft(left);

		controller.setOnUp(up);

		controller.setOnDown(down);

		// TODO A remplacer par l'ajout d'un nouveau canvas à view
		face.setOnAction(e -> {
			View fv = new View(Axis.XAXIS, this.method);
			fv.controller.setModel(new Model());
			fv.controller.getModel().copy(this.controller.getModel());
			fv.controller.setZoom(this.controller.getZoom());
			fv.controller.biconnectTo(this.controller);
			// fv.drawModel();
		});

		// TODO A remplacer par l'ajout d'un nouveau canvas à view
		dessus.setOnAction(e -> {
			View dv = new View(Axis.YAXIS, this.method);
			dv.controller.setModel(new Model());
			dv.controller.getModel().copy(this.controller.getModel());
			dv.controller.setZoom(this.controller.getZoom());
			dv.controller.biconnectTo(this.controller);
			// dv.drawModel();
		});


		// TODO A remplacer par l'ajout d'un nouveau canvas à view
		droite.setOnAction(e -> {
			View drv = new View(Axis.ZAXIS, this.method);
			drv.controller.setModel(new Model());
			drv.controller.getModel().copy(this.controller.getModel());
			drv.controller.setZoom(this.controller.getZoom());
			drv.controller.biconnectTo(this.controller);
			// drv.drawModel();
		});

		// TODO
		plus.setOnAction(e -> {
			if (controller.zoom + zoomIncrement < zoomSlider.maxProperty().doubleValue() * 100) {
				zoomSlider.setValue(zoomSlider.getValue() + zoomIncrement / 100);
				controller.setZoom(controller.getZoom() + zoomIncrement);
			} else {
				zoomSlider.setValue(zoomSlider.maxProperty().doubleValue());
				controller.setZoom(zoomSlider.maxProperty().doubleValue() * 100);
			}
		});

		// TODO
		moins.setOnAction(e -> {
			if (controller.zoom - zoomIncrement > 0) {
				zoomSlider.setValue(zoomSlider.getValue() - zoomIncrement / 100);
				controller.setZoom(controller.getZoom() - zoomIncrement);
			} else {
				zoomSlider.setValue(0);
				controller.setZoom(0);
			}
		});
		outils.getStyleClass().add("outils");
		return outils;
	}

	private Slider createSlider() {
		Slider slider = configureSlider();
		// TODO A voir si c'est necessaire de bouger
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.setZoom(zoomSlider.getValue() * 100);
				// drawModel();
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
		slider.setValue(controller.getZoom());
		return slider;
	}

	/**
	 * Create and replace (if already existing) a canvas of given width and height
	 * 
	 * @param width
	 * @param height
	 */
	private void createCanvas(double width, double height) {
		canvas = new ModelisationCanvas(width, height, Axis.ZAXIS, DrawingMethod.BOTH);

		canvas.getGraphicsContext2D().setStroke(Color.BLACK);
		canvas.getGraphicsContext2D().setFill(Color.GREY);

		/* DEPLACEMENT SOURIS */
		controller.setMousePressed(canvas);
		controller.setMouseDragging(canvas);
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
		// drawModel();
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
		List<MenuItem> menuItems = new ArrayList<>();
		for (File f : liste) {
			System.out.println(f.getAbsolutePath());
			MenuItem item = new MenuItem("Erreur!");
			try {

				Path filepath = Path.of(f.getPath());
				System.out.println(Files.getFileAttributeView(filepath, PosixFileAttributeView.class));
				AclFileAttributeView att = Files.getFileAttributeView(filepath, AclFileAttributeView.class);
				BasicFileAttributes attributes = Files.readAttributes(filepath, BasicFileAttributes.class);
				item = new MenuItem(
						f.getName() + "\nAuthor: " + att.getOwner().getName() + " (" + getSize(attributes.size()) + ")"
								+ "\nfaces:" + RecuperationPly.getNBFaces(filepath.toString()) + "; points:"
								+ RecuperationPly.getNBVertices(filepath.toString()));

				controller.setLoadFileItem(f, item);
				menuItems.add(item);
			} catch (FormatPlyException | IOException e) {
				// on ne renvoie pas vers un message d'erreur. Si un fichier n'est pas du bon
				// format, on l'ignore simplement.
				e.printStackTrace();
			}

		}

		return menuItems;
	}

	/**
	 * Recupération de l'erreur PLY
	 */
	public void erreur(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(e.getClass().getSimpleName());
		alert.setHeaderText("Vous avez rencontrez un problème");
		alert.setContentText(e.getMessage());
		alert.showAndWait();

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

	public ModelisationCanvas getCanvas() {
		return canvas;
	}

	public DrawingMethod getDrawMethod() {
		return method;
	}

	public void changeTheme(Theme theme) {
		if (theme == null)
			return;
		else if (theme.equals(Theme.DEFAULT)) {
			this.getScene().getStylesheets().clear();
		} else {
			URL resourcecss = getClass().getResource(theme.getPath());
			if (resourcecss != null) {
				this.theme = theme;
				String css = resourcecss.toExternalForm();
				this.getScene().getStylesheets().clear();
				this.getScene().getStylesheets().add(css);
			}
		}
		controller.updateTheme(theme);
	}

	public void setMethod(DrawingMethod m) {
		canvas.setMethod(m);
	}
}

package views;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ply.PlyProperties;
import ply.RecuperationPly;
import ply.exceptions.FormatPlyException;
import util.Axis;
import util.DrawingMethod;
import util.PlyFileFilter;
import util.Theme;

@SuppressWarnings("PMD.LawOfDemeter")
public class View extends Stage {

	private List<ModelisationCanvas> canvases = null;

	private Slider zoomSlider = null;
	private double zoomIncrement = 10.0;
	private double zoom = 100;
	private double maxzoom = 1000;

	private double defaultCanvasWidthPercentile = 0.80;

	private static String path = System.getProperty("user.dir") + File.separator + "exemples" + File.separator;

	private Controller controller = null;

	protected double oldMouseX;
	protected double oldMouseY;

	public View() {

		setTitle("Modélisateur 3D");

		controller = new Controller(this);

		/* INITIALISATION DU CANVAS */
		canvases = new ArrayList<>();
		canvases.add(createCanvas(Screen.getPrimary().getBounds().getWidth() / 2 * defaultCanvasWidthPercentile,
				Screen.getPrimary().getBounds().getHeight() / 2, Axis.ZAXIS));
		canvases.add(createCanvas(Screen.getPrimary().getBounds().getWidth() / 2 * defaultCanvasWidthPercentile,
				Screen.getPrimary().getBounds().getHeight() / 2, Axis.XAXIS));
		canvases.add(createCanvas(Screen.getPrimary().getBounds().getWidth() * defaultCanvasWidthPercentile,
				Screen.getPrimary().getBounds().getHeight() / 2 - 50, Axis.YAXIS));

		GridPane canvasPane = new GridPane();
		setupCanvasGridpane(canvasPane, canvases);

		zoomSlider = createSlider();

		/* CREATION DE LA FENETRE */
		VBox vBox = new VBox();
		Scene scene = new Scene(vBox);

		MenuBar menuBar = createMenuBar();
		vBox.getChildren().add(menuBar);

		/* CREATION DES OUTILS */
		VBox outils = createToolBox();

		TableView<PlyProperties> tableview = new TableView<>();
		tableview.setEditable(true);

		TableColumn<PlyProperties, String> nameColumn = new TableColumn<>("name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<PlyProperties, String> pathColumn = new TableColumn<>("path");
		pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
		TableColumn<PlyProperties, String> sizeColumn = new TableColumn<>("size");
		sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
		TableColumn<PlyProperties, Integer> facesColumn = new TableColumn<>("faces");
		facesColumn.setCellValueFactory(new PropertyValueFactory<>("faces"));
		TableColumn<PlyProperties, Integer> pointsColumn = new TableColumn<>("points");
		pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
		TableColumn<PlyProperties, String> createdColumn = new TableColumn<>("created");
		createdColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
		TableColumn<PlyProperties, String> authorColumn = new TableColumn<>("author");
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

		tableview.getColumns().addAll(nameColumn, pathColumn, sizeColumn, facesColumn, pointsColumn, createdColumn,
				authorColumn);

		tableview.getItems().addAll(getModels(null));

		tableview.setRowFactory(tv -> {
			TableRow<PlyProperties> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					PlyProperties rowData = row.getItem();
					try {
						controller.loadFile(rowData.getPath());
					} catch (FileNotFoundException|FormatPlyException e) {
						e.printStackTrace();
					}
				}
			});
			return row;
		});

		TextField searchField = new TextField();
		searchField.setPromptText("Search a model here");
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			tableview.getItems().clear();
			tableview.getItems().addAll(getModels(newValue));
		});

		VBox.setVgrow(tableview, Priority.ALWAYS);

		VBox plyList = new VBox();
		plyList.getChildren().addAll(searchField, tableview);

		Tab tab = new Tab("PlyList");
		tab.setContent(plyList);

		TabPane tabpane = new TabPane(tab);
		tabpane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		VBox.setVgrow(tabpane, Priority.ALWAYS);

		outils.getChildren().add(tabpane);

		/* INITIALISATION DU BORDERPANE */
		HBox root = new HBox();
		root.getChildren().addAll(canvasPane, outils);
		vBox.getChildren().addAll(root);

		controller.addKeyPressedEvent(vBox);
		vBox.getStyleClass().add("scene");

		setScene(scene);
		setMaximized(true);
		show();

		tabpane.lookup(".tab-pane .tab-header-area .tab-header-background")
				.setStyle("-fx-background-color: transparent;");
	}

	private void setupCanvasGridpane(GridPane canvasPane, List<ModelisationCanvas> canvases) {
		canvasPane.add(canvases.get(0), 0, 0);
		canvasPane.add(canvases.get(1), 1, 0);
		canvasPane.add(canvases.get(2), 0, 1, 2, 1);
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

		RadioMenuItem defaultTheme = new RadioMenuItem(Theme.DEFAULT.toString());
		RadioMenuItem darkTheme = new RadioMenuItem(Theme.DARK.toString());
		RadioMenuItem solarisTheme = new RadioMenuItem(Theme.SOLARIS.toString());

		SeparatorMenuItem sep = new SeparatorMenuItem();

		ToggleGroup grp = new ToggleGroup();
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
			ModelisationCanvas firstCanvas = canvases.get(0);
			if (m.equals(firstCanvas.getMethod()))
				radioItem.setSelected(true);
			viewMenu.getItems().add(radioItem);
		}

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

		HBox zoomBox = new HBox(5);
		zoomBox.getChildren().addAll(moins, zoomSlider, plus);

		zoomBox.setAlignment(Pos.CENTER);
		outils.setAlignment(Pos.TOP_CENTER);
		deplacementsButtons.setAlignment(Pos.TOP_CENTER);

		outils.getChildren().addAll(nom, deplacementsButtons, nomZoom, zoomBox);

		nom.setPadding(new Insets(10, 0, 30, 0));
		nomZoom.setPadding(new Insets(80, 0, 30, 0));

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

		plus.setOnAction(e -> {
			if (this.zoom + zoomIncrement < zoomSlider.maxProperty().doubleValue() * maxzoom) {
				zoomSlider.setValue(zoomSlider.getValue() + zoomIncrement / maxzoom);
				controller.setZoom(this.zoom + zoomIncrement);
			} else {
				zoomSlider.setValue(zoomSlider.maxProperty().doubleValue());
				controller.setZoom(zoomSlider.maxProperty().doubleValue() * maxzoom);
			}
			this.zoom = canvases.get(0).getZoom();
		});

		moins.setOnAction(e -> {
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

	private Slider createSlider() {
		Slider slider = configureSlider();
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.setZoom(zoomSlider.getValue() * maxzoom);
				zoom = zoomSlider.getValue() * maxzoom;
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
		slider.setValue(zoom);
		return slider;
	}

	private ModelisationCanvas createCanvas(double width, double height) {
		return createCanvas(width, height, Axis.ZAXIS, DrawingMethod.WIREFRAME);
	}

	private ModelisationCanvas createCanvas(double width, double height, DrawingMethod method) {
		return createCanvas(width, height, Axis.ZAXIS, method);
	}

	private ModelisationCanvas createCanvas(double width, double height, Axis axis) {
		return createCanvas(width, height, axis, DrawingMethod.WIREFRAME);
	}

	private ModelisationCanvas createCanvas(double width, double height, Axis axis, DrawingMethod method) {
		ModelisationCanvas canvas = new ModelisationCanvas(width, height, axis, method);

		canvas.getGraphicsContext2D().setStroke(Color.BLACK);
		canvas.getGraphicsContext2D().setFill(Color.GREY);

		controller.setMousePressed(canvas);
		controller.setMouseDragging(canvas);
		controller.setMouseClicked(canvas);

		return canvas;
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

	public List<PlyProperties> getModels(String regex) {
		List<PlyProperties> models = new ArrayList<>();
		File directory = new File(path);
		FileFilter filter = new PlyFileFilter();
		File[] liste = directory.listFiles(filter);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Pattern pattern = null;
		if (regex != null)
			pattern = Pattern.compile(regex);

		for (File f : liste) {
			try {
				if (regex == null || pattern.matcher(f.getName()).find()) {
					Path filepath = Path.of(f.getPath());
					FileOwnerAttributeView owner = Files.getFileAttributeView(filepath, FileOwnerAttributeView.class);
					BasicFileAttributes attributes = Files.readAttributes(filepath, BasicFileAttributes.class);
					models.add(new PlyProperties(f.getPath(), f.getName(), getSize(attributes.size()),
							RecuperationPly.getNBFaces(filepath.toString()),
							RecuperationPly.getNBVertices(filepath.toString()),
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
	 * RecupÃ©ration de l'erreur PLY
	 */
	public void erreur(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(e.getClass().getSimpleName());
		alert.setHeaderText("Vous avez rencontrez un problÃ¨me");
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

	public List<ModelisationCanvas> getCanvases() {
		return canvases;
	}

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

	public void setMethod(DrawingMethod m) {
		for (ModelisationCanvas modelisationCanvas : canvases) {
			modelisationCanvas.setMethod(m);
		}
	}
}

package views;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import controler.Controller;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ply.PlyProperties;
import ply.RecuperationPly;
import ply.exceptions.FormatPlyException;
import util.Conversion;
import util.DrawingMethod;
import util.PlyFileFilter;
import util.Theme;

/**
 * 
 * Cette classe est un menu d'outil. Il permet d'obtenir les onglets de la
 * fenetre principale.
 *
 * @author <a href="mailto:adnan.kouakoua@univ-lille1.fr">Adnân KOUAKOUA</a>
 *         IUT-A Informatique, Universite de Lille.
 */
public class ToolView {

	private ToolView() {}
	
	/**
	 * Réalise la barre d'outil de la fenêtre
	 * @param controller
	 * @param canvases 
	 * @param view
	 * @param path
	 * @return
	 */
	public static  MenuBar createMenuBar(Controller controller, ModelisationCanvas firstCanva, View view, String path) {
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

		ToggleGroup grp = new ToggleGroup();
		ToggleGroup themeView = new ToggleGroup();

		fileMenu.getItems().addAll(openFileItem, ressourceMenu, new SeparatorMenuItem(), exitItem);
		menuBar.getMenus().addAll(fileMenu, viewMenu, themeMenu, helpMenu);
		helpMenu.getItems().add(helpItem);

		controller.setFileChooserEvent(openFileItem);
		controller.setExitAction(exitItem);
		controller.setHelpAction(helpItem);

		RadioMenuItem radioItem;

		for (DrawingMethod m : DrawingMethod.values()) {
			radioItem = new RadioMenuItem(m.name());
			radioItem.setToggleGroup(grp);
			controller.setOnMethodChangerAction(m, radioItem);

			if (m.equals(firstCanva.getMethod())) {
				radioItem.setSelected(true);
			}
			viewMenu.getItems().add(radioItem);
		}
		ressourceMenu.getItems().addAll(createRessourcePlyMenu(path, controller));
		defaultTheme.setToggleGroup(themeView);
		darkTheme.setToggleGroup(themeView);
		solarisTheme.setToggleGroup(themeView);

		defaultTheme.setOnAction(event -> view.changeTheme(Theme.DEFAULT));
		darkTheme.setOnAction(event -> view.changeTheme(Theme.DARK));
		solarisTheme.setOnAction(event -> view.changeTheme(Theme.SOLARIS));

		defaultTheme.setSelected(true);

		themeMenu.getItems().add(defaultTheme);
		themeMenu.getItems().add(darkTheme);
		themeMenu.getItems().add(solarisTheme);

		return menuBar;
	}

	/**
	 * Donne les informations des fichiers ply dans un tableau et permet de réaliser une recherche
	 * @param controller
	 * @param view
	 * @return
	 */
	public static TabPane createSearchModel(Controller controller,View view) {
		TableView<PlyProperties> columns = columnsView();
		columns.getItems().addAll(view.getModels(null));

		columns.setRowFactory(tv -> {
			TableRow<PlyProperties> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && !row.isEmpty()) {
					PlyProperties rowData = row.getItem();
					try {
						controller.loadFile(rowData.getPath());
					} catch (FileNotFoundException | FormatPlyException error) {
						view.error(error);
					}

				}
			});
			return row;
		});

		TextField searchField = new TextField();
		searchField.setPromptText("Search a model here");
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			columns.getItems().clear();
			columns.getItems().addAll(view.getModels(newValue));
		});

		VBox.setVgrow(columns, Priority.ALWAYS);

		VBox plyList = new VBox();
		plyList.getChildren().addAll(searchField, columns);

		Tab tab = new Tab("PlyList");
		tab.setContent(plyList);

		TabPane tabpane = new TabPane(tab);
		tabpane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		VBox.setVgrow(tabpane, Priority.ALWAYS);
		return tabpane;
	}

	/**
	 * Réalise le tableau des fichiers ply de plusieurs colonnes
	 * @return le tableau
	 */
	private static TableView<PlyProperties> columnsView() {
		TableView<PlyProperties> columns = new TableView<>();

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

		columns.getColumns().addAll(nameColumn, pathColumn, sizeColumn, facesColumn, pointsColumn, createdColumn,
				authorColumn);

		return columns;
	}

	/**
	 * Réalise le menu ressource dans lequel il est possible de selectionner les fichiers ply que l'on souhaite modéliser
	 * @param path chemin du dossier
	 * @param controller controlleur qui permettant d'avoir
	 * @return le menu ressource
	 */
	private static Collection<MenuItem> createRessourcePlyMenu(String path, Controller controller) {
		File directory = new File(path);
		FileFilter filter = new PlyFileFilter();
		File[] liste = directory.listFiles(filter);
		List<MenuItem> menuItems = new ArrayList<>();
		for (File f : liste) {
			try {
				Path filepath = Path.of(f.getPath());
				System.out.println(Files.getFileAttributeView(filepath, PosixFileAttributeView.class));
				AclFileAttributeView att = Files.getFileAttributeView(filepath, AclFileAttributeView.class);
				BasicFileAttributes attributes = Files.readAttributes(filepath, BasicFileAttributes.class);
				MenuItem item = new MenuItem(f.getName() + "\nAuthor: " + att.getOwner().getName() + " ("
						+ Conversion.getSize(attributes.size()) + ")" + "\nfaces:"
						+ RecuperationPly.getNbFaces(filepath.toString()) + "; points:"
						+ RecuperationPly.getNbVertices(filepath.toString()));

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

}

package demo;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class FxmlDemo extends Application {

	// java --module-path 'path-to-javafx-binaries/lib' --add-modules javafx.controls,javafx.fxml -jar .\projetmode.jar
	public void start(Stage stage) throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/simpleFxmlDemo.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("FXML demo");
            stage.show();
    }

    public static void main(String[] args) {
            Application.launch(args);
    }
}

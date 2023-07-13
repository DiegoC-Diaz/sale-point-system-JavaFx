package com.mycompany.sale_point;

import servicios.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private DataBase mysql;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        mysql = new DataBase();
        try {

            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            // FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("reportes.gui.fxml"));
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("venta.gui.fxml"));
            Parent root = fxmlloader.load();
             ventaController NewOptController = fxmlloader.<ventaController>getController();

             NewOptController.setDatabase(mysql);
            Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}

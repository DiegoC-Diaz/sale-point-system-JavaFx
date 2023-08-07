package com.mycompany.sale_point;

import servicios.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Screen;
import servicios.SqlCredentials;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private DataBase mysql;
    private Parent dialog;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("sqlSettings.fxml"));
        dialog = fxmlloader.load();
        

        try {
             SqlCredentials credentials = new SqlCredentials("","","","","");
            credentials.readCredentialsFromFile("credentials.txt");
            
            mysql = new DataBase(credentials.getHost(),
            credentials.getUser(),credentials.getPassword(),credentials.getDatabase(),
            credentials.getPort());

            create(stage, "venta.gui.fxml");
        } catch (Exception e) {

            alertaConexion(e.getMessage(),stage);
            
  
        }

    }

    private void alertaConexion(String Error,Stage stage) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Error de configuracion");
        alert.setHeaderText("¿Desea configurar la conexion?");
        alert.setContentText("Mensaje:"+ Error);

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
      
        } else if (option.get() == ButtonType.OK) {
            Scene scene = new Scene(dialog);
            
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else if (option.get() == ButtonType.CANCEL) {
            stage.close();
        } else {
 
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void create(Stage stage, String fxml) throws IOException {
        stage.setResizable(true);
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        // FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("reportes.gui.fxml"));
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlloader.load();
        ventaController NewOptController = fxmlloader.<ventaController>getController();
        NewOptController.setDatabase(mysql);
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}

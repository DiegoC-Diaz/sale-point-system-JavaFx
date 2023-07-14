/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sale_point;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.SVGPath;
import javafx.stage.Screen;
import javafx.stage.Stage;
import servicios.DataBase;
import servicios.SqlCredentials;

/**
 * FXML Controller class
 *
 * @author Diego Carcamo
 */
public class SqlSettings implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TextField user_field;
    @FXML
    private TextField host_field;
    @FXML
    private TextField port_field;
    @FXML
    private TextField password_field_unmask;
    @FXML
    private PasswordField password_field_mask;
    @FXML
    private TextField database_field;
    @FXML
    private SVGPath eye_icon;
    @FXML
    private Button eye_btn;

    boolean mostrar;
    String content_hide;
    String content_shown;
    Stage stage = new Stage();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mostrar = false;
        password_field_unmask.textProperty().bindBidirectional(password_field_mask.textProperty());
        password_field_unmask.setVisible(false);
        password_field_unmask.setManaged(false);
        content_hide = eye_icon.getContent();
        content_shown = "M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5."
                + "5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 "
                + "4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 "
                + "0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465"
                + " 1.755C11.879 11.332 10.119 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-"
                + "2.457A13.134 13.134 0 0 1 1.172 8z M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 "
                + "0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z";
        password_field_unmask.setManaged(false);

        eye_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            //Accion para esconder la contrasena
            public void handle(ActionEvent t) {

                eye_icon.setContent(mostrar ? content_hide : content_shown);

                mostrar = mostrar ? false : true;
                password_field_unmask.setVisible(mostrar);
                password_field_unmask.setManaged(mostrar);

            }
        });
    }

    @FXML
    void ok_btn_pressed(ActionEvent event) {

        try {
            //Cargar Escena
            
            String Host =host_field.getText();
            String User=user_field.getText();
            String Password=password_field_unmask.getText();
            String Port =port_field.getText();
            String Database=database_field.getText();
            DataBase mysql = new DataBase(host_field.getText(), user_field.getText(), password_field_unmask.getText(),
                    database_field.getText(), port_field.getText());

            stage.setResizable(true);
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            // FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("reportes.gui.fxml"));

            //En caso de Error revisar ->fxmlloader .getSource("ruta de FXML");
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("venta.gui.fxml"));
            Parent root;

            root = fxmlloader.load();
            //En caso de Error revisar ->que el controlador sea el correcto 
            // ventaController es un controlador por ejemplo NewOptController ->
            ventaController NewOptController = fxmlloader.<ventaController>getController();
            NewOptController.setDatabase(mysql);
            Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
            //De aqui en adelante no puede suceder mas errores
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            SqlCredentials credentials = new SqlCredentials(User,Database, Port, Password,
                    Host);
            
            credentials.writeCredentialsToFile("credentials.txt");
            
            
            ((Stage)user_field.getScene().getWindow()).close();
            
            
           

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No se pudo conectar con el servidor de MySQL");
            alert.setHeaderText("Error de conexion");
            alert.setContentText("Mensaje:" + ex.getMessage());

            alert.show();

        }

    }

}

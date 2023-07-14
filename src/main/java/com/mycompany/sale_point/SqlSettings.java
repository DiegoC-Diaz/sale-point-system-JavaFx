/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sale_point;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.SVGPath;

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
            public void handle(ActionEvent t) {

                eye_icon.setContent(mostrar ? content_hide : content_shown);

                mostrar = mostrar ? false : true;
                password_field_unmask.setVisible(mostrar);
                password_field_unmask.setManaged(mostrar);

            }
        });
    }

}

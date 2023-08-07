/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sale_point;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import servicios.DataBase;
import servicios.reportes;

/**
 * FXML Controller class
 *
 * @author Diego Carcamo
 */
public class utilidadController implements Initializable,reportes {

    /**
     * Initializes the controller class.
     */
    @FXML
    AnchorPane panelPrincipal;

    DataBase database;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    void setDataBase(DataBase database){
        this.database=database;
    
    }

    @FXML
    private void ir_facturar(ActionEvent event) {
        //Reutilizamos el componente agregar o quitar productos ya que este ya posee una barra de busqueda
        //ademas de poseer varias utilidades lo que nos permite ahorrar mucho codigo y ademas 
        Stage stage = (Stage) panelPrincipal.getScene().getWindow();
        try {
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("venta.gui.fxml"));
            Parent root = fxmlloader.load();

            ventaController NewOptController = fxmlloader.<ventaController>getController();
           
            NewOptController.setDatabase(database);

            Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());

            stage.setScene(scene);
            //stage.setMaximized(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
   

    @FXML
    private void mostrar_ventas(ActionEvent event) {
        panelPrincipal.getChildren().clear();
        
        reporteCierreController rpController = cargarEscena("reporteCierre.fxml")
                .<reporteCierreController>getController();
        
        
        
        reportes.finalDia(database,rpController.getProuductos());
       rpController.cargarDatos();
        System.out.println("intentnado cargar datos");
        
       

    }

    @FXML
    private void agergar_editar_producto(ActionEvent event) {
        //Reutilizamos el componente agregar o quitar productos ya que este ya posee una barra de busqueda
        //ademas de poseer varias utilidades lo que nos permite ahorrar mucho codigo y ademas 
        try {
            editorAvanzadoController NewOptController = cargarEscena("editorAvanzado.gui.fxml").<editorAvanzadoController>getController();
            NewOptController.mostrarBotones(false);//false 
            NewOptController.setDatabase(database);

        } catch (Exception e) {
        }

    }

    private FXMLLoader cargarEscena(String fxmlFile) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane panel = fxmlloader.load();

            panelPrincipal.getChildren().setAll(panel.getChildren());
            return fxmlloader;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}

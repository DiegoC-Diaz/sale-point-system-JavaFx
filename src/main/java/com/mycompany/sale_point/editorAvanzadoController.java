/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sale_point;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;


import servicios.producto;

/**
 * FXML Controller class
 *
 * @author Diego Carcamo
 */
enum Modos {
    CREAR, EDITAR
};

public class editorAvanzadoController extends buscadorProducto implements Initializable, producto {

  @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
    
    }

  
    

}

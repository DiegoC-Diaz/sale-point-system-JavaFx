package com.mycompany.sale_point;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import models.productModel;

/**
 * FXML Controller class
 *
 * @author Diego Carcamo
 */
public class reporteCierreController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private BarChart grafico_barras;
    
    ObservableList<productModel> prouductos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
        
    }    
    
}

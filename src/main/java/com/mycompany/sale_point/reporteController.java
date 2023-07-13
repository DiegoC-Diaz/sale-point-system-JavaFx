/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sale_point;

import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

/**
 * FXML Controller class
 *
 * @author Diego Carcamo
 */
public class reporteController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ComboBox<Integer> year_picker;
    @FXML
    private DatePicker month_picker;
    @FXML 
    ComboBox<String> modo;

    Calendar calendario;

    
    enum MODOS{
        MES,AÑO
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
      
        setModos();
        generateYearList();
        month_picker.setManaged(false);
        
        
        

    }
    
    private void  setModos(){
    
        ObservableList<String> Modos=modo.getItems();
        for(MODOS m :MODOS.values()){
            Modos.add(m.toString());
        }
    
    }
    
    private void generateYearList() {
        //Este meotod obtendra un rango de años en referencia al año actual y al inicial
      
        
        ObservableList<Integer> years= year_picker.getItems();

        int currentYear = Year.now().getValue();
        int startYear = 2021; // Customize the range of years as per your requirement
        int endYear = currentYear + 50;

        for (int year = startYear; year <= endYear; year++) {
            years.add(year);
        }

    }

}

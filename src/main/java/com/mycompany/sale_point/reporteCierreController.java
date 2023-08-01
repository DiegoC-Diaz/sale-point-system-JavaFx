package com.mycompany.sale_point;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.productModel;
import models.ventaModel;

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
    private BarChart<String, Number> grafico_barras;
    @FXML
    private TableView<ventaModel> tablaResultado;
    @FXML
    private AnchorPane panel_1;
    @FXML
    private CategoryAxis categoriaX;
    @FXML
    private NumberAxis numeroY;

    private ObservableList<ventaModel> productos;

    private Series series1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        productos = observableArrayList();

        TableColumn<ventaModel, String> nombre_column = new TableColumn<>("nombre");
        nombre_column.prefWidthProperty().bind(tablaResultado.widthProperty().multiply(0.5));
        nombre_column.setCellValueFactory(new PropertyValueFactory<ventaModel, String>("nombre"));

        TableColumn<ventaModel, Double> cantidad_column = new TableColumn<>("cantidad");
        cantidad_column.prefWidthProperty().bind(tablaResultado.widthProperty().multiply(0.2));
        cantidad_column.setCellValueFactory(new PropertyValueFactory<ventaModel, Double>("cantidad"));

        TableColumn<ventaModel, Double> total_bruto = new TableColumn<>("totalBruto");
        total_bruto.prefWidthProperty().bind(tablaResultado.widthProperty().multiply(0.2));
        total_bruto.setCellValueFactory(new PropertyValueFactory<ventaModel, Double>("totalBruto"));

        TableColumn<ventaModel, Double> total_neto = new TableColumn<>("totalNeto");
        total_neto.prefWidthProperty().bind(tablaResultado.widthProperty().multiply(0.2));
        total_neto.setCellValueFactory(new PropertyValueFactory<ventaModel, Double>("totalNeto"));

        tablaResultado.getColumns().addAll(nombre_column, cantidad_column, total_bruto, total_neto);
        tablaResultado.setItems(productos);


    }

    public ObservableList<ventaModel> getProuductos() {
        return productos;
    }

    public ObservableList<XYChart.Data<String, Number>> getEstadisticas() {
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        
        for(ventaModel m:productos){
           data.add(new XYChart.Data<>(m.getNombre(),m.getCantidad()));

       
        }
      return data;

    }

    public void cargarDatos() {
        tablaResultado.refresh();

        ObservableList<XYChart.Data<String, Number>> data =getEstadisticas();
        XYChart.Series<String, Number> series = new XYChart.Series<>("Series Name", data);
        series.setName("ventas");
        grafico_barras.getData().setAll(series);
     

    }

    @FXML
    private void performAnalysisOperation(ActionEvent event) {
        //Performing analysis operation

    }
    
    @FXML
    private void mostrarTabla(){
        panel_1.setVisible((panel_1.isVisible())?false:true);
    
    }

}

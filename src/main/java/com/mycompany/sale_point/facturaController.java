/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sale_point;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.facturaModel;
import models.productModel;
import servicios.DataBase;
import servicios.factura;
import servicios.formatos;
import servicios.impresion;

/**
 * FXML Controller class
 *
 * @author Diego Carcamo
 */
public class facturaController implements Initializable, formatos, factura {

    @FXML
    private ListView<String> facturaFx;

    private DataBase database;

    private facturaModel facturaCliente;

    ObservableList<String> datosFactura;

    @FXML
    private TextField nombres_form;
    @FXML
    private TextField apellidos_form;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        datosFactura = FXCollections.observableArrayList();

    }

    void setDataBase(DataBase database) {
        this.database = database;
    }

    void crearFactura(ObservableList<productModel> productos) {
        facturaCliente = new facturaModel(nombres_form.getText(),
                "", apellidos_form.getText(), "", productos);

        crearLista();
    }

    void crearLista() {
        datosFactura.clear();
        double total = 0, subtotal = 0;
        datosFactura.add("\t" + "ESTA NO ES UNA FACTURA SOLO UN COMPROBANTE");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        datosFactura.add("Fecha:\t" + now.format(formatter));

        if (nombres_form.getText().length() > 0 && apellidos_form.getText().length() > 0) {
            datosFactura.add("Consumidor:\t" + nombres_form.getText() + " " + apellidos_form.getText());
        } else {
            datosFactura.add("Consumidor:\t" + "CF");
        }

        datosFactura.add("Productos" + "\t" + "Subtotales");

        for (productModel pm : facturaCliente.getProductos()) {

            String productoTotal = formatos.getFormatoLempira(pm.getTotal());

            String nuevoDetalle = pm.getNombre() + "\t\t" + productoTotal;
            //por ahoira no manda a pedir el subtotal esta duncion solo desplega el
            //Total incluyendo el desceunto del producto;
            subtotal += pm.getSubtotal();
            total += pm.getTotal();
            datosFactura.add(nuevoDetalle);
        }

        facturaCliente.setTotal(total);
        facturaCliente.setSubtotal(subtotal);

        datosFactura.add("---------------------------------");
        datosFactura.add("subtotal:\t\t" + formatos.getFormatoLempira(subtotal));
        datosFactura.add("total:\t\t" + formatos.getFormatoLempira(total));

        facturaFx.setItems(datosFactura);
    }

    @FXML
    void facturar(ActionEvent event) {
        impresion imprimir = new impresion();

        String fact = "";

        for (String s : facturaFx.getItems()) {
            fact += s + "\n";
        }
        TextArea textArea = new TextArea(fact);

        facturaCliente.setNombre(nombres_form.getText());
        facturaCliente.setApellido(apellidos_form.getText());
        crearLista();

        factura.crearFactura(database, facturaCliente, facturaCliente.getProductos());
        imprimir.pageSetup(textArea, (Stage) facturaFx.getScene().getWindow());

    }

}

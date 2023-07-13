/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import javafx.collections.ObservableList;

/**
 *
 * @author Diego Carcamo
 */
public class facturaModel {

    private ObservableList<productModel> productos;
    private String primNombre, segNombre, primApellido, segApellido;
    double total, subtotal, descuento;

    public facturaModel() {
        this.productos = null;
        this.primNombre = "";
        this.segNombre = "";
        this.primApellido = "";
        this.segApellido = "";
        this.descuento = 0;
        this.subtotal = 0;
        this.total = 0;
    }

    public facturaModel(String primNombre, String segNombre, String primApellido,
            String segApellido, ObservableList<productModel> productos) {
        this.productos = productos;
        this.primNombre = primNombre;
        this.segNombre = segNombre;
        this.segApellido = segApellido;
        this.primApellido = primApellido;

    }

    public ObservableList<productModel> getProductos() {
        return productos;
    }

    public String getPrimNombre() {
        return primNombre;
    }

    public String getSegNombre() {
        return segNombre;
    }

    public String getPrimApellido() {
        return primApellido;
    }

    public String getSegApellido() {
        return segApellido;
    }

    public double getTotal() {
        return total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getConsumidor() {
        return primNombre +" "+ primApellido;

    }

    public void setNombre(String nombre) {

        primNombre = nombre;

    }

    public void setApellido(String apellido) {
        primApellido = apellido;

    }

}

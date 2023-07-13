/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Diego Carcamo
 */
public class productModel {

    public String nombre, descripcion;
    public double precio, descuento;
    private String id;
    private int cantidad;//no confunidr cantidad con la cantidad disponible del producto,
    //cantidad hace refrencia a una variable usada en factura para saber cuantas unidades del producto se llevan.
    boolean estado;
    String tipo;
    String imagen;

    public productModel() {

    }

    public productModel(String id, String nombre, String descripcion, double precio, double descuento, int cantidad,boolean estado,String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.descuento = descuento;
        this.estado=estado;
        this.cantidad = cantidad;
        this.tipo=tipo;
        this.imagen="";

    }
    
    public double getTotalDescuento(){
        return -getSubtotal()*descuento;
    
    }
    
    public void setImagen(String ruta){
    
        this.imagen=ruta;
    }
    
    public String getImagen(){
    
       return this.imagen;
    }
    public void setTipo(String tipo){
    
        this.tipo=tipo;
    }
    
    
    public String getTipo(){
        return tipo;
    }

    public void setId(String id) {
        this.id = id;

    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public double getDescuento() {
        return descuento;
    }

    public String getId() {
        return id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setCantidad(int cantidad) {
        if (cantidad > 0) {
            this.cantidad = cantidad;
        }

    }
    public boolean getEstado(){
        return estado;
    }

    public void sumarCantidad(int cantidad) {
        if ((this.cantidad + cantidad) > 0) {
            this.cantidad += cantidad;
        }

    }

    public double getSubtotal() {
        return cantidad * precio;
    }

    public double getTotal() {
        //Se aplica el descuento del producto.
        return getSubtotal()-(getSubtotal()*descuento);
        
    }

}

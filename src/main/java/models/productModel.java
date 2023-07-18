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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import servicios.formatos;

/**
 *
 * @author Diego Carcamo
 */
public class productModel implements formatos{

    public String nombre, descripcion;
    public double precio, descuento;
    private String id;
    private int cantidad;//no confunidr cantidad con la cantidad disponible del producto,
    //cantidad hace refrencia a una variable usada en factura para saber cuantas unidades del producto se llevan.
    private boolean estado;
    private String tipo;
    private String imagen;
    private int pos;

    public productModel() {

    }

    public productModel(String id, String nombre, String descripcion, double precio, double descuento, int cantidad, boolean estado, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.descuento = descuento;
        this.estado = estado;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.imagen = "";
        pos = 0;

    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;

    }
    public String getDescuentoStr(){
    
        return descuento+"";
    
    };

    public double getTotalDescuento() {
        return -getSubtotal() * descuento;

    }
    
    public String getTotalDescuentoFormatted() {
        return ("-"+formatos.getFormatoLempira(getSubtotal() * descuento));

    }

    public void setImagen(String ruta) {

        this.imagen = ruta;
    }

    public String getImagen() {

        return this.imagen;
    }

    public void setTipo(String tipo) {

        this.tipo = tipo;
    }
    
    public void setEstado(boolean estado){
        this.estado=estado;
        
    }

    public Rectangle getColor() {
        Color color = estado ? Color.web("#3cba13", 1.0) : Color.web("#d1321d", 1.0);

        Rectangle r = new Rectangle();
        r.setWidth(40);
        r.setHeight(40);
        r.setFill(color);

        return r;
    
    }

    public String getTipo() {
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

    public boolean getEstado() {
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
        return getSubtotal() - (getSubtotal() * descuento);

    }

}

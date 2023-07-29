/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Diego Carcamo
 */
public class ventaModel {
 
    
    private String nombre;
    
    private int cantidad;
    
    private double totalBruto;
    private double totalNeto;
    
   
    
    public ventaModel(double totalBruto,double totalNeto,String nombre){
        this.nombre=nombre;
        this.totalBruto=totalBruto;
        this.totalNeto=totalNeto;
        
    
    
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getTotalBruto() {
        return totalBruto;
    }

    public double getTotalNeto() {
        return totalNeto;
    }
    
    
    
    
    
}

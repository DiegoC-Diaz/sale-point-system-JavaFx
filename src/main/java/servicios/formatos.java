/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package servicios;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 *
 * @author Diego Carcamo
 */
public interface formatos {
    
    public static DecimalFormatSymbols dfs=getLempira();
    
    
    static DecimalFormatSymbols getLempira(){
        
        DecimalFormatSymbols dfs;
          ///Metodo paraq cusotmizar el simbolo de nuestra moneda que en facturas
        //se representa como L.
        dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("L");
        dfs.setGroupingSeparator(',');
        dfs.setMonetaryDecimalSeparator('.');
        
        
        return dfs;
    
    }
    
    static String getFormatoLempira(double dinero){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
         ((DecimalFormat) formatter).setDecimalFormatSymbols(dfs);
        return formatter.format(dinero);
               
    }
    
    
    static boolean isNumber(String num){
        try {
            double number=Double.parseDouble(num);
            return true;
        } catch (Exception e) {
            System.out.println("No es un numero ");
        }
        return false;
        
    
    }
    
}

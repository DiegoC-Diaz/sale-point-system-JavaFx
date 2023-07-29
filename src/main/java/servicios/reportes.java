/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package servicios;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import models.ventaModel;

/**
 *
 * @author Diego Carcamo
 */
public interface reportes {

    static void finalDia(DataBase database, ObservableList<ventaModel> ventas) {

        try {
            CallableStatement stm = database.getCon().prepareCall("{CALL ventas_dia_p(?)}");
        
            Date date = new Date(System.currentTimeMillis()  );
            stm.setDate(1,date);
            ResultSet rs=stm.executeQuery();
       
            buildList(ventas, rs);
            
        } catch (SQLException ex) {

            ex.printStackTrace();
        }

    }
    
    
    static void buildList( ObservableList<ventaModel> ventas,ResultSet rs) throws SQLException{
        while(rs.next()){
        
            ventaModel venta= new ventaModel(rs.getDouble("valor_bruto"),rs.getDouble("valor_neto"),
                     rs.getString("nombre"));
            
            ventas.add(venta);
        
        }
    }

}

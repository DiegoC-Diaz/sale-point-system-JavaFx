/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package servicios;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import models.facturaModel;
import models.productModel;

/**
 *
 * @author Diego Carcamo
 */
public interface factura {

    static void crearFactura(DataBase db, facturaModel factura, ObservableList<productModel> detalles) {
        String facturaUUID = UUID.randomUUID().toString();

        CallableStatement stmt2;
        CallableStatement stmt ;

        try {

            stmt= db.getCon().prepareCall("{CALL crear_factura_p(?,?,?,?)}");
            stmt2 = db.getCon().prepareCall("{CALL agregarDetalles(?, ?, ?, ?, ?)}");
            /*El AutoCommit viene activado por defecto, este permite
            que los queries se ejecuten tras que esten listos.Osea si tenemos dos
            queries se ejecutaran tras estar listos y no todos a la vez.
            al desactivar esta opcion podemos elegir en que memomento deseamos 
            enviarlos lo cambios a base de datos
             */

            db.getCon().setAutoCommit(false);
            stmt.setString(1, facturaUUID);
            stmt.setDouble(2, factura.getDescuento());
            stmt.setDouble(3, factura.getTotal());
            stmt.setString(4, factura.getConsumidor());
            stmt.execute();

            int counter = 1;
            for (productModel p : detalles) {
                //creamos un batch para agregar cada detalle a la factura.
                stmt2.setString(1, facturaUUID);
                stmt2.setInt(2, counter);
                stmt2.setString(3, p.getId());
                stmt2.setDouble(4, p.getCantidad());
                stmt2.setDouble(5, p.getTotal());

                stmt2.addBatch();
                counter++;
                if (counter - 1 % 1000 == 0 || counter - 1 == detalles.size()) {
                    stmt2.executeBatch(); // Se Ejecute 1000 inserciones
                }
            }
            //Cuando termine la ejecucion activamos el autocommit
            db.getCon().commit();//mandamos los cambios
            db.getCon().setAutoCommit(true);

        } catch (SQLException ex) {
            Logger.getLogger(factura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

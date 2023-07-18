/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package servicios;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Pagination;
import models.productModel;

/**
 *
 * @author Diego Carcamo
 */
public interface producto {
    //Tamaño de las paginas
    static double PageSize = 100.0;

    static ResultSet getProducto_Id(String id_producto, DataBase database) {

        PreparedStatement stm = database.getPreparedStatement("SELECT `producto`.`id_producto`,\n"
                + "    `producto`.`nombre`,\n"
                + "    `producto`.`descripcion`,\n"
                + "    `producto`.`precio`,\n"
                + "    `producto`.`descuento`,\n"
                + "    `producto`.`estado`,\n"
                + "`tipo`\n"
                + "FROM `tiendas_bethel`.`producto` where producto.id_producto =?;");

        try {
            System.out.println("EJECUTNANDO get_Producto_ID\n");

            stm.setString(1, id_producto);
            return stm.executeQuery();

        } catch (SQLException ex) {
            System.out.println("FATAL ERROE EXECUTING getProducto_Id");
            ex.printStackTrace();
            return null;
        }

    }

    static void actualizarProducto(productModel producto, DataBase database) {
        //recordar agregar editar si el prducto esta activo o no 

        LocalDateTime currentDateTime = LocalDateTime.now();

        // Create a Timestamp object from the current LocalDateTime
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);
        System.out.println("EJECUTNANDO actualizarProducto()\n");
        PreparedStatement stm = database.getPreparedStatement("UPDATE `tiendas_bethel`.`producto`\n"
                + "SET\n"
                + "`nombre` = ?,\n"
                + "`descripcion` =?,\n"
                + "`precio` = ?,\n"
                + "`descuento` = ?,\n"
                + "`fecha_modificacion` = ?,\n"
                + "`estado` = ?,\n"
                + "`tipo` = ?\n"
                + "WHERE `id_producto` =?;");

        try {
            stm.setString(1, producto.getNombre());
            stm.setString(2, producto.getDescripcion());
            stm.setDouble(3, producto.getPrecio());
            stm.setDouble(4, producto.getDescuento());
            stm.setTimestamp(5, timestamp);
            stm.setBoolean(6, producto.getEstado());
            stm.setString(7, producto.getTipo());
            stm.setString(8, producto.getId());
            stm.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("FATAL ERROE EXECUTING getProducto_Id");
            ex.printStackTrace();

        }

    }

    static void cambiarEstado(DataBase database, String productoUUID, boolean estado) {
        //CALL cambiar_estado_p ("c724e139-178e-11ee-be6f-076e120f153f",false)
        System.out.println("EJECUTNANDO cambiarEstado()\n");
        try {
            CallableStatement stm = database.getCon().prepareCall("{CALL cambiar_estado_p (?,?)}");
            stm.setString(1, productoUUID);
            stm.setBoolean(2, estado);
            stm.execute();
            System.out.println("Se modifico el estado del producto exitosamente");

        } catch (SQLException ex) {
            System.out.println("ERROR: Error al intentar cambiar el estado del producto");
            ex.printStackTrace();

        }

    }

    static void crearProducto(DataBase database, productModel producto) {

        try {
            System.out.println("EJECUTNANDO creaProducto()\n");
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Create a Timestamp object from the current LocalDateTime
            Timestamp timestamp = Timestamp.valueOf(currentDateTime);

            CallableStatement stm = database.getCon().prepareCall("{CALL insertar_producto_p(UUID()"
                    + ",?,?,?, ?, ?,?, ?, ?,null)}");

            stm.setString(1, producto.getNombre());
            stm.setString(2, producto.getDescripcion());
            stm.setDouble(3, producto.getPrecio());
            stm.setDouble(4, producto.getDescuento());
            stm.setBoolean(5, producto.getEstado());
            stm.setString(6, producto.getTipo());
            stm.setInt(7, 0);
            stm.setTimestamp(8, timestamp);

            stm.execute();

        } catch (SQLException ex) {
            Logger.getLogger(producto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static ResultSet getTipos(DataBase database) {

        PreparedStatement stm = database.getPreparedStatement("SELECT nombre FROM tiendas_bethel.tipo;");
        System.out.println("EJECUTNANDO getTipos\n");

        try {

            stm.executeQuery();

            return stm.getResultSet();

        } catch (SQLException ex) {
            System.out.println("FATAL ERROE EXECUTING buscar_producto F");
            ex.printStackTrace();

        }

        return null;

    }

    static ResultSet buscar_productos(DataBase database, String nombre, Pagination pag) {

        ResultSet rs = buscar_productos(database, nombre, pag.getCurrentPageIndex());
        //Se esta ejecutando dos veces
        int res=database.getResultados(nombre);
        System.out.println("Resultados de la tabal prodducto:"+res);
        

        System.out.println("EJECUTNANDO buscarProductos PAG\n");
        int paginas = (int) Math.ceil(res/ PageSize);
        Platform.runLater(() -> {
            try {
                //an event with a button maybe
                System.out.println("button is clicked");
                System.out.println("Index:" + pag.getCurrentPageIndex());
      
                pag.setPageCount(paginas);

                pag.setMaxPageIndicatorCount(paginas);
                pag.setCurrentPageIndex(0);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        return rs;

    }

    static ResultSet buscar_productos(DataBase database, String nombre, int offset) {

        try {
            System.out.println("EJECUTNANDO buscarProductos OFFSET\n");
            CallableStatement stm = database.getCon().prepareCall("{CALL buscar_producto_nombre(?,?)}");

            stm.setString(1, nombre);
            stm.setInt(2,(int) (offset*PageSize));
            ResultSet rs = stm.executeQuery();

            return rs;

        } catch (SQLException ex) {
            System.out.println("FATAL ERROE EXECUTING buscar_producto ");
            ex.printStackTrace();

        }
        return null;

    }

    static void buscar_producto(DataBase database, String barCode) {

    }

}

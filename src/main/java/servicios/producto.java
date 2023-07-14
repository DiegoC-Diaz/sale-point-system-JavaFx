/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package servicios;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.productModel;

/**
 *
 * @author Diego Carcamo
 */
public interface producto {

    static ResultSet getProducto_Id(String id_producto, DataBase database) {

        PreparedStatement stm = database.getPreparedStatement("SELECT `producto`.`id_producto`,\n"
                + "    `producto`.`nombre`,\n"
                + "    `producto`.`descripcion`,\n"
                + "    `producto`.`precio`,\n"
                + "    `producto`.`descuento`,\n"
                + "    `producto`.`foto`,\n"
                + "    `producto`.`estado`,\n"
                + "`tipo`\n"
                + "FROM `tiendas_bethel`.`producto` where producto.id_producto =?;");

        try {
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
        PreparedStatement stm = database.getPreparedStatement("UPDATE `tiendas_bethel`.`producto`\n"
                + "SET\n"
                + "`nombre` = ?,\n"
                + "`descripcion` =?,\n"
                + "`precio` = ?,\n"
                + "`descuento` = ?,\n"
                + "`foto` = ?,\n"
                + "`estado` = ?,\n"
                + "`tipo` = ?\n"
                + "WHERE `id_producto` =?;");

        try {
            stm.setString(1, producto.getNombre());
            stm.setString(2, producto.getDescripcion());
            stm.setDouble(3, producto.getPrecio());
            stm.setDouble(4, producto.getDescuento());
            stm.setString(5, producto.getImagen());
            stm.setBoolean(6, producto.getEstado());
            stm.setString(7, producto.getTipo());
            stm.setString(8, producto.getId());
            stm.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("FATAL ERROE EXECUTING getProducto_Id");
            ex.printStackTrace();

        }

    }

    static void cambiarEstado(DataBase database, String productoUUID,boolean estado) {
        //CALL cambiar_estado_p ("c724e139-178e-11ee-be6f-076e120f153f",false)
        try {
            CallableStatement stm = database.getCon().prepareCall("{CALL cambiar_estado_p (?,?)}");
            stm.setString(1,productoUUID);
            stm.setBoolean(2, estado);
            stm.execute();
            System.out.println("Se modifico el estado del producto exitosamente");
           

        } catch (SQLException ex) {
            System.out.println("ERROR: Error al intentar cambiar el estado del producto");
            ex.printStackTrace();

        }
    
    }

    static void crearProducto(DataBase database, productModel producto) {
        PreparedStatement stm = database.getPreparedStatement("INSERT INTO `tiendas_bethel`.`producto`\n"
                + "(id_producto,`nombre`,\n"
                + "`descripcion`,\n"
                + "`precio`,\n"
                + "`descuento`,\n"
                + "`foto`,\n"
                + "`estado`,\n"
                + "`tipo`)\n"
                + "VALUES\n"
                + "(UUID(),?,?,?,?,?,?,?)");

        try {
            stm.setString(1, producto.getNombre());
            stm.setString(2, producto.getDescripcion());
            stm.setDouble(3, producto.getPrecio());
            stm.setDouble(4, producto.getDescuento());
            stm.setString(5, producto.getImagen());
            stm.setBoolean(6, producto.getEstado());
            stm.setString(7, producto.getTipo());

            stm.execute();
        } catch (SQLException ex) {
            Logger.getLogger(producto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static ResultSet getTipos(DataBase database) {

        PreparedStatement stm = database.getPreparedStatement("SELECT nombre FROM tiendas_bethel.tipo;");

        try {

            stm.executeQuery();

            return stm.getResultSet();

        } catch (SQLException ex) {
            System.out.println("FATAL ERROE EXECUTING buscar_producto F");
            ex.printStackTrace();

        }

        return null;

    }

    static ResultSet buscar_productos(DataBase database, String nombre) {

        try {
            CallableStatement stm = database.getCon().prepareCall("{CALL tiendas_bethel.buscar_producto_nombre(?)}");
            stm.setString(1, nombre);
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

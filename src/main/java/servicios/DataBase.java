/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author Diego Carcamo
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.productModel;

public class DataBase {

    private String Host;
    private String User;
    private String Password;
    private String Database;
    private String Port;
    private Connection con;

    private ResultSet productRs;

    /*
    
       Host =  "192.168.1.24";
        User =  "caja";
        Password =  "Caja8787";
        Database = "tiendas_bethel";
        Port =  "3306";
     */
    public DataBase() throws SQLException {
        // Password =  System.getenv("PASSWORD");
        /* Host = "localhostly";
        User = System.getenv("USER");
        Password = System.getenv("PASSWORD");
        Database = System.getenv("DATABASE");
        Port = System.getenv("PORT");
         */
        //jdbc:mysql://localhost:3306/sonoo
        String link = "jdbc:mysql://" + Host + ":" + Port + "/" + Database;

        con = DriverManager.getConnection(link, User, Password);
        System.out.println(link);

    }

    public DataBase(String Host, String User, String Password, String Database, String Port) throws SQLException {
        // Password =  System.getenv("PASSWORD");
        this.Host = Host;
        this.User = User;
        this.Password = Password;
        this.Database = Database;
        this.Port = Port;
        //jdbc:mysql://localhost:3306/sotiendas_bethelnoo
        String link = "jdbc:mysql://" + Host + ":" + Port + "/" + Database + "?useSSL=false&allowPublicKeyRetrieval=true";
        con = DriverManager.getConnection(link, User, Password);
        System.out.println(link);

    }

    public Connection getCon() {
        return con;

    }

    public ResultSet getProductosRs() {
        return productRs;

    }

    public int getResultados(String nombre) {
        PreparedStatement rows = getPreparedStatement("SELECT COUNT(*)  as count FROM producto p WHERE locate(?,p.nombre)>0 limit 300;");

        try {
            //campos:

            //Agragremos elementos a una lista para irla actualizando.
            rows.setString(1, nombre);
            ResultSet res = rows.executeQuery();
            res.next();
            return res.getInt("count");

            //set Paaramters
        } catch (SQLException ex) {

            ex.printStackTrace();

            System.out.println("NO SE PUEDE");
        }

        return 0;

    }
        public int getResultados() {
        PreparedStatement rows = getPreparedStatement("SELECT FOUND_ROWS() as count;");

        try {
            //campos:

            //Agragremos elementos a una lista para irla actualizando.
           
            ResultSet res = rows.executeQuery();
            res.next();
            return res.getInt("count");

            //set Paaramters
        } catch (SQLException ex) {

            ex.printStackTrace();

            System.out.println("NO SE PUEDE");
        }

        return 0;

    }

    public PreparedStatement getPreparedStatement(String Query) {
        System.out.println(Query + "\n");
        try {
            return con.prepareStatement(Query, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            System.out.println("Errror in getPReparStatement unable to return prepar");
            ex.printStackTrace();
            return null;
        }
    }

    public void cargarProductos() {
        System.out.println("EJECUTANDO CARGARPRODUCTOS()");
        //Esta fucnion permite cargar todos los productos en base de datso a memoria
        //Esto aunque pordria ser contra producente a medida crece la base de datos
        //Este o pretenda manejar infromacion de forma si no mas bien un registro local.
        //cargar en memoria hace a la aplicacion menos tosca a la hora de las busquedas
        //Sin embargo otra solucion es utilizar hilos para las ocnsultas de mysql y evitar
        //congelamientos por microsegundos.
        PreparedStatement stmt = getPreparedStatement("SELECT `producto`.`id_producto`,\n"
                + "    `producto`.`nombre`,\n"
                + "    `producto`.`descripcion`,\n"
                + "    `producto`.`precio`,\n"
                + "    `producto`.`descuento`,\n"
                + " `producto`.`estado`,\n"
                + "`tipo`\n"
                + "FROM `producto` limit 100;");
        //Agregaremos paginacion 
        try {
            //campos:

            //Agragremos elementos a una lista para irla actualizando.
            productRs = stmt.executeQuery();

            //set Paaramters
        } catch (SQLException ex) {
            Logger.getLogger(producto.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();

            System.out.println("NO SE PUEDE");
        }

    }

}

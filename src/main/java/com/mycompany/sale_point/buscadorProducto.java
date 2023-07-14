/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sale_point;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Callback;
import models.productModel;

import servicios.DataBase;
import servicios.Wait;
import servicios.producto;

/**
 * FXML Controller class
 *
 * @author Diego Carcamo
 */
public class buscadorProducto implements Initializable, producto {

    @FXML
    private TextField barra_busqueda;
    @FXML
    private Button buscar_btn;

    @FXML
    private TableView<ObservableList> tableview;
    @FXML
    private AnchorPane agregar_producto_form;
    @FXML
    private Button editar_btn;
    @FXML
    private Button cambiar_estado;
    @FXML
    private CheckBox estado_form;
    @FXML
    private TextField nombre_form;
    @FXML
    private TextField precio_form;
    @FXML
    private TextField descuento_form;
    @FXML
    private SplitMenuButton tipo_form;
    @FXML
    private TextArea descripcion_form;
    @FXML
    private Button agregar_btn;
    @FXML
    private ImageView imagen;

    private Modos modoActual;

    private String selectedProductId;
    private File rutaImagen;

    private ventaController ventaControl;
    /**
     * Initializes the controller class.
     */
    private DataBase database;
    private ObservableList<productModel> listaProductos;
    Thread hiloBusqueda;
    Wait espera;
    Runnable run;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        modoActual = Modos.CREAR;
        //Esta no es la implementacion mas apropiada pues
        //java Fx cuenta con su propio metodo de concurrencia
        espera = new Wait(1, this);
        hiloBusqueda = new Thread(espera);

        //Este perimitir realizar debouncing osea limitar la accion de
        //del usuario y tener mejor control del evento de busqueda.
        //Si hay almenos un elemnto seleccionado en la lista permiitiremos al usuario la opcion de editar
        tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editar_btn.setDisable(false);
                agregar_btn.setDisable(false);
                cambiar_estado.setDisable(false);

                /*Asignamos la ruta de la imgen a rutaImagen que es una clase File
                 Que nuestro Imageview imagen utiliza para poder mostrar la imagen
                del producto
                 */
                rutaImagen = new File("images/" + newSelection.get(7).toString());

                imagen.setImage(new Image(rutaImagen.toURI().toString()));

            }
        });

        tipo_form.setOnMouseClicked(event -> {
            if (tipo_form.isShowing()) {
                tipo_form.hide();
            } else {
                tipo_form.show();
            }
        });

        rutaImagen = new File("");

    }

    public void setDatabase(DataBase instance) throws SQLException {
        database = instance;
        System.out.println("SETTING!!!");
        database.cargarProductos();
        buildTable(database.getProductosRs(), "");

    }

    public void setListaProductos(ObservableList<productModel> listaProductos) {
        this.listaProductos = listaProductos;

    }

    public void setVentaController(ventaController ventaControl) {
        this.ventaControl = ventaControl;

    }

    public void busquedaAutomatica() {
        ResultSet rs = producto.buscar_productos(database, barra_busqueda.getText());
        try {
            if (rs != null) {
                System.out.println("Building Our Table");
                buildTable(rs, "");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQL Error in buscar btn_pressed");
        }
    }

    @FXML
    private void buscar_btn_pressed(ActionEvent event) {

        ResultSet rs = producto.buscar_productos(database, barra_busqueda.getText());
        try {
            if (rs != null) {
                System.out.println("Building Our Table");
                buildTable(rs, "");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQL Error in buscar btn_pressed");
        }

    }

    private void crearProducto() {

    }

    public void mostrarBotones(boolean bool) {
        //Se esoncde el boton de agregar
        agregar_btn.setVisible(bool);
        agregar_btn.setManaged(bool);

    }

    @FXML
    private void agregar_producto(ActionEvent event) {

        //producto.getProducto_Id(, database)
        try {
            if (tableview.getSelectionModel().getSelectedItem() != null) {
                String producto_id = tableview.getSelectionModel().getSelectedItem().get(0).toString();
                ResultSet rs = producto.getProducto_Id(producto_id, database);
                rs.next();
                String id = rs.getString("id_producto");
                if (!contieneProducto(id)) {
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    double descuento = rs.getDouble("descuento");
                    double precio = rs.getDouble("precio");
                    boolean estado = rs.getBoolean("estado");
                    String tipo = rs.getString("tipo");
                    listaProductos.add(new productModel(id, nombre, descripcion, precio, descuento,
                            1, estado, tipo));
                    //Es operacion toma el id del producto en la tabla y hace un query 
                    //para agregarlo a la tabla de factura. cabe destacar que la tabla para buscar productos esta cargada en 
                    //memoria y solo hasta que se cambia de pantalla (se cierra o se va alguna de las opciones
                    //como editar y agregar un nuevo producto la tabla manda otro query )
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FATAL ERROE EXECUTING getProducto_Id Query");
        }

    }

    void cargarTipo() {
        tipo_form.getItems().clear();
        //Query para obtener tdoos los elementos de la tabla tipos
        ResultSet rs = producto.getTipos(database);
        try {
            while (rs.next()) {
                //agergamos un estilo a cada MenuItem. (solo aumenta el tamaño
                //de la letra)
                String estilo = "-fx-font-size: 20px;";
                MenuItem tipo = new MenuItem(rs.getString("nombre"));
                tipo.setOnAction(event -> {
                    tipo_form.setText(tipo.getText());
                });

                tipo.setStyle(estilo);
                tipo_form.getItems().add(tipo);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(buscadorProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean contieneProducto(String id) {
        for (productModel p : listaProductos) {
            if (p.getId().equals(id)) {

                return true;
            }
        }
        return false;
    }

    @FXML
    void searchItem() throws SQLException {
        espera.increment();
        if (!hiloBusqueda.isAlive()) {
            espera.reset();
            hiloBusqueda = new Thread(espera);
            hiloBusqueda.start();
        }
    }

    @FXML
    private void cambiar_estado(ActionEvent event) {

        if (tableview.getSelectionModel().getSelectedItem() != null) {
            String producto_id = tableview.getSelectionModel().getSelectedItem().get(0).toString();
            // el 5 indiica la columna donde se encuentra el nuestro ID
            String estado = tableview.getSelectionModel().getSelectedItem().get(5).toString();
            producto.cambiarEstado(database, producto_id, estado.equals("0"));

        }

    }

    void actualizarProducto(productModel productoData) {
        //Esta funcion permie actualizar los productos ubicados en 
        //en el area de facturacion
        try {
            for (int i = 0; i < listaProductos.size(); i++) {
                productModel proudcto = listaProductos.get(i);
                if (proudcto.getId().equals(selectedProductId)) {
                    productoData.setCantidad(proudcto.getCantidad());
                    listaProductos.set(i, productoData);
                    //Conservaremos la cantidad pero actualizamos lo demas
                }
            }

            if (ventaControl != null) {

                System.out.println("Refrescando la tabla");
                ventaControl.getTabla_productos().refresh();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("La tabla lista de productos esta vacia!"
                    + "\npor ende estamos en una pagina diferente de facturar.");

        }

    }

    @FXML
    //Este es un boton que se encuentra en el form de este componente.
    private void confirmar_form(ActionEvent event) {
        //se confirma y se crea o actualiza el producto.

        String nombre = nombre_form.getText();
        double precio = Double.parseDouble(precio_form.getText());

        double descuento = Double.parseDouble(descuento_form.getText());
        String tipo = tipo_form.getText();
        String descripcion = descripcion_form.getText();
        boolean estado = estado_form.isSelected();
        String foto = rutaImagen.getAbsoluteFile().getName();
        /*Cuando confirmamos solo quereumnosguardar el nombre del archivo
         y no la ruta. Ya que  lo que queremos es que varias computadoas
         puedan trabajr con el mismo set de imagenes.*/

 /*ProductoData es el objeto que conteine la informacion
            para actualizacion e insercion den Mysql
         */
        productModel productoData = new productModel("", nombre, descripcion, precio, descuento, 0, estado, tipo);
        /*El constructor de product model no incluye la foto y es necesario usar un set
        Modificaremos el producto que se edito en memoria ya que solo se hacen edicone
        inddividuales no es necesario recargar......*/
        actualizarProducto(productoData);
        productoData.setImagen(foto);

        if (modoActual == Modos.EDITAR) {
            //Aqui hacemos uso de la variablek selecetedProductoId que asignamos en abrir form
            //Contiene el Id del producto que se esta editando
            productoData.setId(selectedProductId);
            //ejecutar Update

            producto.actualizarProducto(productoData, database);

            agregar_producto_form.setVisible(false);

        } else {
            //Ejecutar Insert.
            producto.crearProducto(database, productoData);

            agregar_producto_form.setVisible(false);

        }

        //Recargamos el resultset que estaba en memeoria 
        database.cargarProductos();

        //Agregar aqui el build Table
        try {
            buildTable(database.getProductosRs(), "");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void seleccionar_imagen(ActionEvent event) {
        Stage stage = (Stage) tableview.getScene().getWindow();

        String rutaActual = Paths.get(".").toAbsolutePath().normalize().toString();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(rutaActual));

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));

        rutaImagen = fileChooser.showOpenDialog(stage);

        if (rutaImagen != null) {
            System.out.println(rutaImagen.toURI());
            imagen.setImage(new Image(rutaImagen.toURI().toString()));
        }
    }

    @FXML
    private void cerrar_form(ActionEvent event) {
        agregar_producto_form.setVisible(false);
        imagen.setImage(null);

    }

    @FXML
    private void abrir_form(ActionEvent event) {
        agregar_producto_form.setVisible(true);
        cargarTipo();
        //caragr tipo rellena el splitter que esta en el formulaaario de
        //agreagr/editar

        if (event.getSource().equals(editar_btn)) {
            //si se presiono el boton de editar ejecutar lo siguiente!
            modoActual = Modos.EDITAR;

            //obtenemos la seleccion de la tabla
            ObservableList seleccion = tableview.getSelectionModel().getSelectedItems().get(0);
            //si hay un objeto seleccionado en la tabla entonces

            //hara unn query para obtener datos de un prooducto ;
            ResultSet rs = producto.getProducto_Id(seleccion.get(0).toString(), database);
            //cargaremos los tipo en el Spliter tipo_form.
            selectedProductId = seleccion.get(0).toString();
            //la varaible selectedProductId que es un entero se usara
            //en al funcion confirmar_form();

            try {
                rs.next();
                nombre_form.setText(rs.getString("nombre"));
                descuento_form.setText(rs.getDouble("descuento") + "");
                precio_form.setText(rs.getString("precio"));
                descripcion_form.setText(rs.getString("descripcion"));
                tipo_form.setText(rs.getString("tipo"));
                estado_form.setSelected(rs.getBoolean("estado"));

            } catch (SQLException ex) {
                System.out.println(ex + "ha fallado la ejecucion de abrir_form por que se hizo mal el query");

            }
        } else {
            //Modificcacion de estado de campos al darle al boton creaar
            modoActual = Modos.CREAR;
            nombre_form.clear();
            descripcion_form.clear();
            precio_form.clear();
            descripcion_form.clear();
            estado_form.setSelected(true);

        }

    }

    void ActualizarProductos() {
        //Actualizar los productos que estan en facturar.

        database.cargarProductos();

    }

    void buildTable(ResultSet rs, String item) throws SQLException {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        tableview.getColumns().clear();

        try {
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;

                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableview.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");

            }
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    String cell = rs.getString(i);
                    if (cell != null) {
                        row.add(cell);
                    } else {
                        row.add(cell);
                    }

                }
                System.out.println("Row [1] added " + row);
                if (item.equals("")) {
                    data.add(row);
                } else {
                    if (row.get(1).toLowerCase().contains(item.toLowerCase())) {
                        data.add(row);
                    }

                }

            }
            //FINALLY ADDED TO TableView

            tableview.setItems(data);
            tableview.refresh();
            //rs.beforeFirst();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

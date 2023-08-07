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


import javafx.collections.ObservableList;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;

import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import models.productModel;
import servicios.AutoCompleteComboBoxListener;

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
    protected ComboBox<String> filtros;
    @FXML
    private TableView<productModel> tableview;
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
    protected ComboBox<String> tipo_form;
    @FXML
    private TextArea descripcion_form;
    @FXML
    private Button agregar_btn;
    @FXML
    private ImageView imagen;
    @FXML
    private Pagination paginacion;
    @FXML
    private RadioButton activar_filtros;

    private Modos modoActual;

    private String selectedProductId;
    private File rutaImagen;

    private ventaController ventaControl;
    /**
     * Initializes the controller class.
     */
    private DataBase database;
    private ObservableList<productModel> listaProductos;

    private boolean estaBuscando = false;
    private Thread hiloBusqueda;
    private Wait espera;
    
    protected AutoCompleteComboBoxListener comboBoxFiltrado;
    protected AutoCompleteComboBoxListener comboBoxFiltrado2;
    Runnable run;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTabla();
        
        espera = new Wait(1, this);
        hiloBusqueda = new Thread(espera);
        modoActual = Modos.CREAR;
        //Esta no es la implementacion mas apropiada pues
        //java Fx cuenta con su propio metodo de concurrencia
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
                rutaImagen = new File("images/" + newSelection.getImagen());

                imagen.setImage(new Image(rutaImagen.toURI().toString()));

            }
        });
        try {
                 comboBoxFiltrado=new AutoCompleteComboBoxListener<>(tipo_form);
                 comboBoxFiltrado2=new AutoCompleteComboBoxListener<>(filtros);
        } catch (Exception e) {
            System.out.println("OCURRIO UN ERROR!!");
            e.printStackTrace();
        }
   

        filtros.setVisible(false);
        activar_filtros.setOnAction((t) -> {

            filtros.setVisible((activar_filtros.isSelected()) ? true : false);
        });

        rutaImagen = new File("");

        paginacion.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int currentPageIndex = newValue.intValue();
            /* Para realiza la paginacion y no hacer el query dos veces debemos revisar
                el booleano estaBuscando que seteamos verdadero en la funcion 
                buscar_btn_pressed(ActionEvent event); si y solo el indice de
                paginacion es>0
                
             */

            if (!estaBuscando) {
                //Si acabamos de presionar el boton de buscar resutlado s de pagina actual
                ResultSet rs;
                if (filtros.isVisible() && filtros.getSelectionModel().getSelectedItem() != null) {
                    rs = producto.buscar_productos(database, barra_busqueda.getText(), filtros.getSelectionModel().getSelectedItem(), currentPageIndex);
                } else {
                    rs = producto.buscar_productos(database, barra_busqueda.getText(), currentPageIndex);
                }

                try {
                    buildTable(rs, "");
                } catch (SQLException ex) {
                    Logger.getLogger(buscadorProducto.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                //si el indice cambia pero hemos buscado recientemente 
                //no volvemos a buscar.
                estaBuscando = false;
            }

        });
 

    }

    public void initTabla() {
        //cmabiar el ancho de la columna
        ////  nombre_column.prefWidthProperty().bind(tableview.widthProperty().multiply(0.2));

        //pos
        TableColumn<productModel, Integer> pos_column = new TableColumn<>(" ");

        pos_column.setCellValueFactory(new PropertyValueFactory<productModel, Integer>("pos"));
        pos_column.prefWidthProperty().bind(tableview.widthProperty().multiply(0.03));
        //nombre
        TableColumn<productModel, String> nombre_column = new TableColumn<>("nombre");
        nombre_column.setCellValueFactory(new PropertyValueFactory<productModel, String>("nombre"));
        nombre_column.prefWidthProperty().bind(tableview.widthProperty().multiply(0.5));

        //estado
        TableColumn<productModel, Rectangle> estado_column = new TableColumn<>("estado");
        estado_column.setCellValueFactory(new PropertyValueFactory<productModel, Rectangle>("color"));
        //precio
        TableColumn<productModel, Double> precio_column = new TableColumn<>("precio");
        precio_column.setCellValueFactory(new PropertyValueFactory<productModel, Double>("precio"));
        //Descripcion
        TableColumn<productModel, String> desc_column = new TableColumn<>("descripcion");
        desc_column.setCellValueFactory(new PropertyValueFactory<productModel, String>("descripcion"));
        desc_column.prefWidthProperty().bind(tableview.widthProperty().multiply(0.2));
        tableview.getColumns().addAll(pos_column, nombre_column, estado_column, precio_column, desc_column
        );

    }

    public void setDatabase(DataBase instance) throws SQLException {
        database = instance;
        cargarTipo();
        System.out.println("SETTING!!!");
        buildTable(producto.buscar_productos(database, "", paginacion), "");

    }

    public void setListaProductos(ObservableList<productModel> listaProductos) {
        this.listaProductos = listaProductos;

    }

    public void setVentaController(ventaController ventaControl) {
        this.ventaControl = ventaControl;

    }

    public void busquedaAutomatica() {
        ResultSet rs;
        if (filtros.isVisible() && filtros.getSelectionModel().getSelectedItem() != null) {
            rs = producto.buscar_productos(database, barra_busqueda.getText(), filtros.getSelectionModel().getSelectedItem(), paginacion);
        } else {
            rs = producto.buscar_productos(database, barra_busqueda.getText(), paginacion);

        }

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

        if (paginacion.getCurrentPageIndex() > 0) {
            //Si nuestro indice es 0 quiere decir que al hacer otra
            //bsuqeuda no se accionara el listener de paginacion.
            //puesot que todas las busquedas setean el index en 0
            estaBuscando = true;
        }
        busquedaAutomatica();

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
                String producto_id = tableview.getSelectionModel().getSelectedItem().getId();
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
        filtros.getItems().clear();

        //Query para obtener tdoos los elementos de la tabla tipos
        ResultSet rs = producto.getTipos(database);
        try {
            while (rs.next()) {
                //agergamos un estilo a cada MenuItem. (solo aumenta el tamaño
                //de la letra)
               
                String nombre = (rs.getString("nombre"));

                filtros.getItems().add(nombre);
                tipo_form.getItems().add(nombre);

            }
            rs.close();
        } catch (SQLException ex) {
           ex.printStackTrace();
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

        //  Platform.runLater(espera);
        espera.increment();
        if (!hiloBusqueda.isAlive()) {
            espera.reset();

            try {
                //an event with a button maybe
                hiloBusqueda = new Thread(espera);
                hiloBusqueda.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    @FXML
    private void cambiar_estado(ActionEvent event) {

        if (tableview.getSelectionModel().getSelectedItem() != null) {
            String producto_id = tableview.getSelectionModel().getSelectedItem().getId();
            // el 5 indiica la columna donde se encuentra el nuestro ID
            boolean estado = tableview.getSelectionModel().getSelectedItem().getEstado();
            tableview.getSelectionModel().getSelectedItem().setEstado(!estado);
            producto.cambiarEstado(database, producto_id, !estado);
            tableview.refresh();

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
        String tipo = tipo_form.getValue();
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
        /*El constructor de product model no incluye la ", foto y es necesario usar un set
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
            filtros.setVisible(false);
            activar_filtros.setSelected(false);
            buildTable(producto.buscar_productos(database, "", 0), "");
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
            String id = tableview.getSelectionModel().getSelectedItem().getId();
            //si hay un objeto seleccionado en la tabla entonces

            //hara unn query para obtener datos de un prooducto ;
            ResultSet rs = producto.getProducto_Id(id, database);
            //cargaremos los tipo en el Spliter tipo_form.
            selectedProductId = id;
            //la varaible selectedProductId que es un entero se usara
            //en al funcion confirmar_form();

            try {
                rs.next();
                nombre_form.setText(rs.getString("nombre"));
                descuento_form.setText(rs.getDouble("descuento") + "");
                precio_form.setText(rs.getString("precio"));
                descripcion_form.setText(rs.getString("descripcion"));
                tipo_form.setValue(rs.getString("tipo"));
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

    }
    ///Esta funcion se ecnarga de construi las tablas en el menu de agreagar y en 

    void buildTable(ResultSet rs, String item) throws SQLException {

        tableview.getItems().clear();
        int counter = 1;
        while (rs.next()) {
            System.out.println("Building");
            String id = rs.getString("id_producto");
            double precio = rs.getDouble("precio");
            double descuento = rs.getDouble("descuento");
            String descripcion = rs.getString("descripcion");
            String nombre = rs.getString("nombre");
            boolean estado = rs.getBoolean("estado");
            String tipo = rs.getString("tipo");

            productModel producto = new productModel(id, nombre, descripcion, precio, descuento,
                    0, estado, tipo);

            producto.setPos(counter);
            tableview.getItems().add(producto);
            counter++;

        }

        tableview.refresh();
        //rs.beforeFirst();

    }

}

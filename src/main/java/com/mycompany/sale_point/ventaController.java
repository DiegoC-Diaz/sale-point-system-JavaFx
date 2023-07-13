/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sale_point;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.productModel;
import servicios.DataBase;
import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.binding.IntegerExpression;
import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.util.Callback;
import servicios.formatos;

/**
 * FXML Controller class
 *
 * @author Diego Carcamo
 */
public class ventaController implements Initializable, formatos {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<productModel> tabla_productos;
    @FXML
    private TextField precio_und_field;
    @FXML
    private TextField producto_und_field;
    @FXML
    private TextField subtotal_field;
    @FXML
    private TextField total_field;

    private DataBase database;

    private ObservableList<productModel> listaProductos;

    private Currency lempira;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        try {

            producto_und_field.setTextFormatter(new TextFormatter<>(crearFiltroEnteros()));
            listaProductos = tabla_productos.getItems();
            tabla_productos.setFixedCellSize(60.0);
          
            //ID 
            TableColumn<productModel, String> id_column = new TableColumn<>("id");
            id_column.prefWidthProperty().bind(tabla_productos.widthProperty().multiply(0.05));
            id_column.setCellValueFactory(new PropertyValueFactory<productModel, String>("id"));
            //NOMBRE
            TableColumn<productModel, String> nombre_column = new TableColumn<>("nombre");
            nombre_column.prefWidthProperty().bind(tabla_productos.widthProperty().multiply(0.2));
            nombre_column.setCellValueFactory(new PropertyValueFactory<productModel, String>("nombre"));
            //DESCRIPCION
            TableColumn<productModel, String> descripcion_column = new TableColumn<>("descripcion");
            descripcion_column.setCellValueFactory(new PropertyValueFactory<productModel, String>("descripcion"));
            descripcion_column.prefWidthProperty().bind(tabla_productos.widthProperty().multiply(0.2));
            //PRECIO
            TableColumn<productModel, Double> precio_column = new TableColumn<>("precio");
            precio_column.prefWidthProperty().bind(tabla_productos.widthProperty().multiply(0.05));
            precio_column.setCellValueFactory(new PropertyValueFactory<productModel, Double>("precio"));
            //DESCEUNTO
            TableColumn<productModel, Double> descuento_column = new TableColumn<>("descuento");
            descuento_column.prefWidthProperty().bind(tabla_productos.widthProperty().multiply(0.05));
            descuento_column.setCellValueFactory(new PropertyValueFactory<productModel, Double>("descuento"));
            //CANTIDAD
            TableColumn<productModel, Integer> cantidad_column = new TableColumn<>("cantidad");
            cantidad_column.prefWidthProperty().bind(tabla_productos.widthProperty().multiply(0.05));
            cantidad_column.setCellValueFactory(new PropertyValueFactory<productModel, Integer>("cantidad"));
            //TOTAL_DESCEUNTO
            TableColumn<productModel, Double> total_descuento_column = new TableColumn<>("total_descuento");
            total_descuento_column.setCellValueFactory(new PropertyValueFactory<productModel, Double>("totalDescuento"));
            total_descuento_column.prefWidthProperty().bind(tabla_productos.widthProperty().multiply(0.2));
            
            //Boton
            TableColumn<productModel, Void> colBtn = new TableColumn("Button Column");
            preparedButton(colBtn);
            tabla_productos.getColumns().addAll(id_column, nombre_column, descripcion_column,
                    precio_column, cantidad_column,total_descuento_column,colBtn);

            //listener para la lsita de producgos en factura: se activara cada vez que se agregue uno nuevo.
            listaProductos.addListener(new ListChangeListener<productModel>() {

                @Override
                public void onChanged(ListChangeListener.Change<? extends productModel> change) {
                    while (change.next()) {
                        //dentro calcularmos el subtotal 
                        calcSubtotal();
                        calcTotal(0, 0);

                    }
                }

            });

            //Action Listener
            lempira = Currency.getInstance("HNL");
            //moneda lempira para hacer formato.
            //Cada vez qye se seleccione un elemetno realizar:
            tabla_productos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    calcPrecioUnd(newSelection.getTotal());
                    producto_und_field.setText(Integer.toString(newSelection.getCantidad()));

                }
            });

            //Queremois inicilizar un componente de factura en esta ventana//
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setDatabase(DataBase instance) {
        database = instance;

    }
     private void preparedButton(TableColumn<productModel, Void> colBtn  ) {
        

        Callback<TableColumn<productModel, Void>,
        TableCell<productModel, Void>> cellFactory = new Callback<TableColumn<productModel, Void>, TableCell<productModel, Void>>() {
            @Override
            public TableCell<productModel, Void> call(final TableColumn<productModel, Void> param) {
                final TableCell<productModel, Void> cell = new TableCell<productModel, Void>() {

                    private final Button btn = new Button("Borrar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            getTableView().getItems().remove(getIndex());
                           
                          
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        
        colBtn.setCellFactory(cellFactory);

       
     }

    //made with AI
    private UnaryOperator<TextFormatter.Change> crearFiltroEnteros() {
        Pattern integerPattern = Pattern.compile("-?\\d*");
        System.out.println("ACA ESTA EL EERRO");
        return change -> {
            String newText = change.getControlNewText();
            if (integerPattern.matcher(newText).matches()) {
                return change;
            } else {
                return null;
            }
        };
    }

    public void calcPrecioUnd(double costo) {
        try {

            precio_und_field.setText(formatos.getFormatoLempira(costo));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calcSubtotal() {
        try {
            double subtotal = 0;
            for (productModel pm : tabla_productos.getItems()) {
                subtotal += pm.getSubtotal();

            }

            subtotal_field.setText(formatos.getFormatoLempira(subtotal));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calcTotal(double subtotal, double descuento) {
        try {
            //Preguntar a los propietarios si el descuento enla factura es general o por producto en todo caso ser general
            //colocar el total calculando descuentos individuales mas descuento normal//
            // A todo caso se aplicara el descuento 
            //por producto de los productos en el subtotal y luego se aplicara uno genrela en el total
            double total = 0;
            for (productModel pm : listaProductos) {
                total += pm.getTotal();
            }

            total_field.setText(formatos.getFormatoLempira(total));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sumar_cantidad(ActionEvent event) {
        alterarCantidad(1);

    }

    @FXML
    private void restar_cantidad() {
        alterarCantidad(-1);
    }

    public TableView<productModel> getTabla_productos() {
        return tabla_productos;
    }
    
    

    @FXML
    private void facturar_btn(ActionEvent event) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("factura.gui.fxml"));
            Parent root = fxmlloader.load();
            facturaController NewOptController = fxmlloader.<facturaController>getController();

            Stage stage = new Stage();
            NewOptController.setDataBase(database);

            NewOptController.crearFactura(listaProductos);

            Scene scene = new Scene(root, 757, 436);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void cambio_producto_und() {
        try {

            String Entrada = producto_und_field.getText();

            if (!Entrada.equals("") && tabla_productos.getSelectionModel().getSelectedItem() != null) {

                productModel producto = tabla_productos.getSelectionModel().getSelectedItem();
                producto.setCantidad(Integer.parseInt(Entrada));
                calcPrecioUnd(producto.getTotal());
                calcSubtotal();
                calcTotal(0, 0);
                //se calculan los precios, total y subtotal de la factura.
                tabla_productos.refresh();

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    void alterarCantidad(int cantidad) {
        productModel producto = tabla_productos.getSelectionModel().getSelectedItem();
        producto.sumarCantidad(cantidad);
        producto_und_field.setText(Integer.toString(producto.getCantidad()));
        calcPrecioUnd(producto.getTotal());
        tabla_productos.refresh();
        calcSubtotal();
        calcTotal(0, 0);

    }

    @FXML
    private void ir_utilidad(ActionEvent event) {
        Stage stage = (Stage) tabla_productos.getScene().getWindow();
        try {
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("utilidad.gui.fxml"));
            Parent root = fxmlloader.load();
            utilidadController NewOptController = fxmlloader.<utilidadController>getController();
            Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(ventaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    

    @FXML
    private void agregar_producto(ActionEvent event) {

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("buscadorProducto.gui.fxml"));
            Parent root = fxmlloader.load();
            buscadorProducto NewOptController = fxmlloader.<buscadorProducto>getController();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
            //le pasamos el controlador para leizar actualciones
            NewOptController.setVentaController(this);
            NewOptController.setDatabase(database);
            NewOptController.setListaProductos(listaProductos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

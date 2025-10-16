package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Producto;
import service.ProductoService;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;

public class ProductoController {

    @FXML
    private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    private final ProductoService productoService = new ProductoService();
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();



}

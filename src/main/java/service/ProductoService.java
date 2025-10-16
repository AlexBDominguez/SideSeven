package service;

import dao.ProductoDAO;
import model.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    private ProductoDAO productoDAO = new ProductoDAO();
    private List<Producto> productos = new ArrayList<>();

    public ProductoService(){
        productos = productoDAO.leerProductos();
    }

    public List<Producto> listarProductos(){
        return productos;
    }

    public void agregarProducto(Producto p){
        productos.add(p);
        productoDAO.guardarProductos(productos);
    }

    public void eliminarProducto(int id){
        productos.removeIf(p -> p.getId() == id);
        productoDAO.guardarProductos(productos);
    }

    public Producto buscarPorId(int id) {
        return productos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

    }

    public void actualizarProducto(Producto pNuevo) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == pNuevo.getId()) {
                productos.set(i, pNuevo);
                break;
            }
        }
        productoDAO.guardarProductos(productos);
    }


}

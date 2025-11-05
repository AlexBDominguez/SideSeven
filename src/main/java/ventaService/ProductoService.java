package ventaService;

import dao.ProductoDAO;
import dao.ProductoDAODB;
import model.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ProductoDAODB productoDAODB = new ProductoDAODB();
    private final List<Producto> productos = new ArrayList<>();

    public ProductoService() {
        productos.addAll(productoDAO.leerProductos());
    }

    public List<Producto> listarProductos() {
        return new ArrayList<>(productos);
    }

    private int generarNuevoId() {
        if (productos.isEmpty()) {
            return 1;
        }
        return productos.stream()
                .mapToInt(Producto::getId)
                .max()
                .orElse(0) + 1;
    }

    public void agregarProducto(String nombre, String categoria, double precio, int stock) {

        List<String> errores = new ArrayList<>();

        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("❌ Error: El nombre del producto no puede estar vacío.");
        }

        if(precio <= 0) {
            errores.add("❌ Error: El precio debe ser mayor que 0.");
        }

        if(stock < 0) {
            errores.add("❌ Error: El stock no puede ser negativo.");
        }

        if (!errores.isEmpty()) {
            System.out.println("❌ No se pudo agregar el producto por los siguientes errores:");
            errores.forEach(e-> System.out.println("  - " + e));
            return;
        }

        int nuevoId = generarNuevoId();
        Producto p = new Producto(nuevoId, nombre, categoria, precio, stock);
        productos.add(p);
        productoDAO.guardarProductos(productos);
        System.out.println("✓ Producto agregado con ID: " + nuevoId);
    }

    public void agregarProducto(Producto p) {
        if (buscarPorId(p.getId()) != null) {
            System.out.println("❌ Error: Ya existe un producto con ese ID.");
            return;
        }
        productos.add(p);
        productoDAO.guardarProductos(productos);
    }

    public void eliminarProducto(int id) {
        boolean eliminado = productos.removeIf(p -> p.getId() == id);
        if (eliminado) {
            productoDAO.guardarProductos(productos);
        } else {
            System.out.println("⚠️ No se encontró producto con ID: " + id);
        }
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
                productoDAO.guardarProductos(productos);
                return;
            }
        }
        System.out.println("⚠️ No se encontró producto con ID: " + pNuevo.getId());
    }

    public List<Producto> listarProductosCSV(){
        return productoDAO.leerProductos();
    }

    public List<Producto> listarProductosDB(){
        return productoDAODB.leerProductos();
    }
}

package service;

import dao.ProductoDAO;
import dao.ProductoDAODB;
import model.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ProductoDAODB productoDAODB = new ProductoDAODB();

    private boolean usarBD = false;

    public void setUsarBD(boolean usarBD) {
        this.usarBD = usarBD;
    }

    public List<Producto> listarProductos() {
        return usarBD ? productoDAODB.leerProductos() : productoDAO.leerProductos();
    }

    public void agregarProducto(Producto producto) {
        List<String> errores = validarProducto(producto, false);

        if (!errores.isEmpty()) {
            System.err.println("❌ No se pudo agregar el producto. Se encontraron los siguientes errores:");
            errores.forEach(e -> System.err.println("  - " + e));
            return;
        }

        if (usarBD) {
            productoDAODB.guardarProducto(producto);
        } else {
            List<Producto> lista = productoDAO.leerProductos();
            lista.add(producto);
            productoDAO.guardarProductos(lista);
        }

        System.out.println("✅ Producto agregado correctamente: " + producto.getNombre());
    }

    public void actualizarProducto(Producto producto) {
        List<String> errores = validarProducto(producto, true);

        if (!errores.isEmpty()) {
            System.err.println("❌ No se pudo actualizar el producto. Se encontraron los siguientes errores:");
            errores.forEach(e -> System.err.println("  - " + e));
            return;
        }

        if (usarBD) {
            productoDAODB.guardarProducto(producto);
        } else {
            List<Producto> lista = productoDAO.leerProductos();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId() == producto.getId()) {
                    lista.set(i, producto);
                    break;
                }
            }
            productoDAO.guardarProductos(lista);
        }

        System.out.println("✅ Producto actualizado correctamente (ID " + producto.getId() + ")");
    }

    public void eliminarProducto(int id) {
        Producto existente = buscarProductoPorId(id);
        if (existente == null) {
            System.err.println("❌ No existe un producto con ID " + id);
            return;
        }

        if (usarBD) {
            productoDAODB.eliminarProducto(id);
        } else {
            List<Producto> lista = productoDAO.leerProductos();
            lista.removeIf(p -> p.getId() == id);
            productoDAO.guardarProductos(lista);
        }

        System.out.println("Producto eliminado: " + existente.getNombre());
    }

    public Producto buscarProductoPorId(int id) {
        if (usarBD) {
            return productoDAODB.buscarPorId(id);
        } else {
            return productoDAO.leerProductos()
                    .stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
    }

    private List<String> validarProducto(Producto producto, boolean esActualizacion) {
        List<String> errores = new ArrayList<>();

        if (producto == null) {
            errores.add("El producto no puede ser nulo.");
            return errores;
        }

        if (esActualizacion && buscarProductoPorId(producto.getId()) == null) {
            errores.add("No existe un producto con el ID especificado (" + producto.getId() + ").");
        }

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            errores.add("El nombre del producto no puede estar vacío.");
        }

        if (producto.getPrecio() < 0) {
            errores.add("El precio no puede ser negativo.");
        }

        if (producto.getStock() < 0) {
            errores.add("El stock no puede ser negativo.");
        }

        if (!esActualizacion) {
            boolean nombreDuplicado = listarProductos().stream()
                    .anyMatch(p -> p.getNombre().equalsIgnoreCase(producto.getNombre()));
            if (nombreDuplicado) {
                errores.add("Ya existe un producto con el nombre '" + producto.getNombre() + "'.");
            }
        }

        return errores;
    }
}

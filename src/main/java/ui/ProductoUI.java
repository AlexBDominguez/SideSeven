package ui;

import model.Producto;
import ventaService.ProductoService;

import java.util.List;
import java.util.Scanner;

public class ProductoUI {

    private final ProductoService productoService;
    private final Scanner scanner;

    public ProductoUI(ProductoService productoService, Scanner scanner) {
        this.productoService = productoService;
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE PRODUCTOS ===");
            System.out.println("1. Listar productos");
            System.out.println("2. Agregar producto");
            System.out.println("3. Actualizar producto");
            System.out.println("4. Eliminar producto");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> listarProductos();
                case 2 -> agregarProducto();
                case 3 -> actualizarProducto();
                case 4 -> eliminarProducto();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void listarProductos() {
        List<Producto> productos = productoService.listarProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            System.out.println("\n--- LISTA DE PRODUCTOS ---");
            productos.forEach(System.out::println);
        }
    }

    private void agregarProducto() {
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        System.out.print("Precio: ");
        double precio = leerDouble();
        System.out.print("Stock: ");
        int stock = leerEntero();

        Producto producto = new Producto(0, nombre, categoria, precio, stock);
        productoService.agregarProducto(producto);
    }

    private void actualizarProducto() {
        System.out.print("ID del producto a actualizar: ");
        int id = leerEntero();
        Producto existente = productoService.buscarProductoPorId(id);

        if (existente == null) {
            System.out.println("❌ No se encontró el producto con ID " + id);
            return;
        }

        System.out.print("Nuevo nombre (" + existente.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (nombre.isEmpty()) nombre = existente.getNombre();

        System.out.print("Nueva categoría (" + existente.getCategoria() + "): ");
        String categoria = scanner.nextLine();
        if (categoria.isEmpty()) categoria = existente.getCategoria();

        System.out.print("Nuevo precio (" + existente.getPrecio() + "): ");
        String precioStr = scanner.nextLine();
        double precio = precioStr.isEmpty() ? existente.getPrecio() : Double.parseDouble(precioStr);

        System.out.print("Nuevo stock (" + existente.getStock() + "): ");
        String stockStr = scanner.nextLine();
        int stock = stockStr.isEmpty() ? existente.getStock() : Integer.parseInt(stockStr);

        existente.setNombre(nombre);
        existente.setCategoria(categoria);
        existente.setPrecio(precio);
        existente.setStock(stock);

        productoService.actualizarProducto(existente);
        System.out.println("✅ Producto actualizado correctamente.");
    }

    private void eliminarProducto() {
        System.out.print("ID del producto a eliminar: ");
        int id = leerEntero();
        productoService.eliminarProducto(id);
        System.out.println("✅ Producto eliminado correctamente (si existía).");
    }

    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Introduce un número válido: ");
            }
        }
    }

    private double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Introduce un número válido: ");
            }
        }
    }
}

package ui;

import model.Producto;
import service.ProductoService;

import java.util.Scanner;

public class ProductoUI {

    private final ProductoService productoService = new ProductoService();
    private final Scanner scanner = new Scanner(;


    public void mostrarMenu(){
        int opcion;

        do{
            System.out.println("\n--- Productos ---");
            System.out.println("1. Listar");
            System.out.println("2. Agregar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Elige una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 -> productoService.listarProductos().forEach(System.out::println);
                case 2 -> agregarProducto();
                case 3 -> buscarProducto();
                case 4 -> actualizarProducto();
                case 5 -> eliminarProducto();
            }

        }while(opcion!=0);

    }

    private void agregarProducto(){
        System.out.println("ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.println("Categoría: ");
        String categoria = scanner.nextLine();
        System.out.println("Precio: ");
        double precio = Double.parseDouble(scanner.nextLine());
        System.out.println("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        productoService.agregarProducto(new Producto(id, nombre, categoria, precio, stock));
        System.out.println("Producto agregado.");
    }

    private void buscarProducto() {
        System.out.println("ID del producto a buscar: ");
        int id = leerEntero();
        Producto p = productoService.buscarPorId(id);
        if (p != null) {
            System.out.println(p);
        } else {
            System.out.println("No se encontró producto con ID: " + id);
        }

    }

    private void actualizarProducto() {
        System.out.println("ID del producto a actualizar: ");
        int id = leerEntero();
        Producto pExistente = productoService.buscarPorId(id);
        if (pExistente == null) {
            System.out.println("No se encontró producto con ID: " + id);
            return;
        }
    }
    private void eliminarProducto () {
        System.out.println("ID del producto a eliminar: ");
        int id = leerEntero();
        productoService.eliminarProducto(id);
        System.out.println("Producto eliminado si existía.");
    }

    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Introduce un número válido: ");
            }
        }
    }

    private double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Introduce un número válido: ");
            }
        }
    }

}



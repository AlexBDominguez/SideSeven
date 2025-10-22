package main;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import model.Producto;
import service.ProductoService;

import java.util.Scanner;

public class Main{
    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);
        ProductoService ps = new ProductoService();
        int opcion;

        do {
            System.out.println("=== SideSeven - Gestión de Productos ===");
            System.out.println("1. Listar productos");
            System.out.println("2. Añadir producto");
            System.out.println("3. Buscar producto por ID");
            System.out.println("4. Eliminar producto");
            System.out.println("5. Actualizar producto");
            System.out.println("6. Salir");
            System.out.print("Elige una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> ps.listarProductos().forEach(System.out::println);
                case 2 -> {
                    System.out.print("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Categoría: ");
                    String cat = sc.nextLine();
                    System.out.print("Precio: ");
                    double precio = sc.nextDouble();
                    System.out.print("Stock: ");
                    int stock = sc.nextInt();
                    ps.agregarProducto(new Producto(id, nombre, cat, precio, stock));
                }
                case 3 -> {
                    System.out.print("ID a buscar: ");
                    int id = sc.nextInt();
                    Producto p = ps.buscarPorId(id);
                    System.out.println(p != null ? p : "Producto no encontrado.");
                }
                case 4 -> {
                    System.out.print("ID a eliminar: ");
                    int id = sc.nextInt();
                    ps.eliminarProducto(id);
                }
                case 5 -> {
                    System.out.print("ID a actualizar: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    Producto p = ps.buscarPorId(id);
                    if (p != null) {
                        System.out.print("Nombre [" + p.getNombre() + "]: ");
                        String nombre = sc.nextLine();
                        System.out.print("Categoría [" + p.getCategoria() + "]: ");
                        String cat = sc.nextLine();
                        System.out.print("Precio [" + p.getPrecio() + "]: ");
                        double precio = sc.nextDouble();
                        System.out.print("Stock [" + p.getStock() + "]: ");
                        int stock = sc.nextInt();
                        ps.actualizarProducto(new Producto(id,
                                nombre.isEmpty() ? p.getNombre() : nombre,
                                cat.isEmpty() ? p.getCategoria() : cat,
                                precio,
                                stock));
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                }
            }
        } while (opcion != 6);


    }
}




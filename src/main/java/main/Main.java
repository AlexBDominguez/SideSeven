package main;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Producto;
import service.ProductoService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;


import java.util.Scanner;

public class Main extends Application {

    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProductosView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("SideSeven - Gestión de Geek Store");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }






















    /*
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductoService ps = new ProductoService();
        int opcion;


        do {
            System.out.println("----- SideSeven - Gestión de Productos -----");
            System.out.println("1. Listar productos");
            System.out.println("2. Agregar producto");
            System.out.println("3. Buscar producto por otro ID");
            System.out.println("4. Eliminar producto");
            System.out.println("5. Actualizar producto");
            System.out.println("6. Salir");
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> ps.listarProductos().forEach(System.out::println);
                case 2 -> {
                    System.out.println("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Nombre: ");
                    String nombre = sc.nextLine();

                    System.out.println("Categoria: ");
                    String categoria = sc.nextLine();

                    System.out.println("Precio: ");
                    double precio = sc.nextDouble();
                    sc.nextLine();

                    System.out.println("Stock: ");
                    int stock = sc.nextInt();
                    sc.nextLine();

                    ps.agregarProducto(new Producto(id, nombre, categoria, precio, stock));
                }
                case 3 -> {
                    System.out.println("ID a buscar");
                    int id = sc.nextInt();
                    Producto p = ps.buscarPorId(id);
                    System.out.println(p != null ? p : "No encontrado");
                }
                case 4 -> {
                    System.out.println("ID a eliminar: ");
                    int id = sc.nextInt();
                    ps.eliminarProducto(id);
                }
                case 5 -> {
                    System.out.println("ID del producto a actualizar: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    Producto existente = ps.buscarPorId(id);
                    if (existente != null) {
                        System.out.println("Nuevo nombre (" + existente.getNombre() + "): ");
                        String nombre = sc.nextLine();
                        System.out.println("Nueva categoria (" + existente.getCategoria() + "): ");
                        String categoria = sc.nextLine();
                        System.out.println("Nuevo precio (" + existente.getPrecio() + "): ");
                        double precio = sc.nextDouble();
                        sc.nextLine();
                        System.out.println("Nuevo stock (" + existente.getStock() + "): ");
                        int stock = sc.nextInt();
                        sc.nextLine();

                        Producto nuevo = new Producto(id, nombre, categoria, precio, stock);
                        ps.actualizarProducto(nuevo);
                    } else {
                        System.out.println("Producto no encontrado");
                    }
                }
            }
        } while (opcion != 6);


    }
*/

}




package app;

import ui.ClienteUI;
import ui.ProductoUI;

import java.util.Scanner;

public class consolaApp {

    private final ProductoUI productoUI = new ProductoUI();
    private final ClienteUI clienteUI = new ClienteUI();
    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n=== SideSeven - Tienda Geek ===");
            System.out.println("1. Gestión de Productos");
            System.out.println("2. Gestión de Clientes");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> productoUI.mostrarMenu();
                case 2 -> clienteUI.mostrarMenu();
                case 3 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
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
}

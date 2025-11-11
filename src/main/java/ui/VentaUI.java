package ui;

import model.Venta;
import service.VentaService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class VentaUI {

    private final VentaService ventaService;
    private final Scanner scanner;

    public VentaUI(VentaService ventaService, Scanner scanner) {
        this.ventaService = ventaService;
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE VENTAS ===");
            System.out.println("1. Listar ventas");
            System.out.println("2. Registrar venta");
            System.out.println("3. Eliminar venta");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> listarVentas();
                case 2 -> registrarVenta();
                case 3 -> eliminarVenta();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void listarVentas() {
        List<Venta> ventas = ventaService.listarVentas();
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
        } else {
            System.out.println("\n--- LISTA DE VENTAS ---");
            ventas.forEach(System.out::println);
        }
    }

    private void registrarVenta() {
        System.out.print("ID del cliente: ");
        int idCliente = leerEntero();

        System.out.print("ID del producto: ");
        int idProducto = leerEntero();

        System.out.print("Total de la venta (€): ");
        double total = leerDouble();

        Venta venta = new Venta(0, idCliente, idProducto, new Date(), total);
        ventaService.registrarVenta(venta);
    }


    private void eliminarVenta() {
        System.out.print("ID de la venta a eliminar: ");
        int id = leerEntero();
        ventaService.eliminarVenta(id);
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

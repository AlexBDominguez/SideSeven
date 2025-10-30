package ui;

import model.Venta;
import service.VentaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VentaUI {

    private final VentaService service;
    private final Scanner scanner = new Scanner(System.in);

    public VentaUI(VentaService service) {
        this.service = service;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n--- Ventas ---");
            System.out.println("1. Listar");
            System.out.println("2. Registrar");
            System.out.println("3. Buscar venta por ID");
            System.out.println("0. Volver");
            System.out.print("Elige una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> listarVentas();
                case 2 -> registrarVenta();
                case 3 -> buscarVenta();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void listarVentas() {
        service.listarVentas().forEach(System.out::println);
    }

    private void registrarVenta() {

        System.out.print("ID del cliente: "); int idCliente = leerEntero();

        List<Integer> idsProductos = new ArrayList<>();
        while (true) {
            System.out.print("ID de producto a añadir (0 para terminar): ");
            int idProd = leerEntero();
            if (idProd == 0) break;
            idsProductos.add(idProd);
        }

        if (idsProductos.isEmpty()) {
            System.out.println("No se puede registrar una venta sin productos.");
            return;
        }

        service.registrarVenta(idCliente, idsProductos);
    }

    private void buscarVenta() {
        System.out.print("ID de la venta a buscar: "); int id = leerEntero();
        Venta v = service.buscarPorId(id);
        if (v != null) System.out.println(v);
        else System.out.println("Venta no encontrada.");
    }

    private int leerEntero() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine()); }
            catch (NumberFormatException e) { System.out.print("Introduce un número válido: "); }
        }
    }
}
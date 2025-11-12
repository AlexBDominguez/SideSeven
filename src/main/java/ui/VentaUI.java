package ui;

import model.Cliente;
import model.Producto;
import model.Venta;
import service.ClienteService;
import service.ProductoService;
import service.VentaService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class VentaUI {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final Scanner scanner;

    public VentaUI(VentaService ventaService, ClienteService clienteService, ProductoService productoService, Scanner scanner) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
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
            return;
        }

        System.out.println("\n--- LISTA DE VENTAS ---");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Venta v : ventas) {
            Cliente cliente = clienteService.buscarClientePorId(v.getIdCliente());
            Producto producto = productoService.buscarProductoPorId(v.getIdProducto());

            String nombreCliente = (cliente != null) ? cliente.getNombre() : "Desconocido";
            String nombreProducto = (producto != null) ? producto.getNombre() : "Desconocido";

            System.out.printf("[Venta #%d] Cliente: %s | Producto: %s | Fecha: %s | Total: %.2f€%n",
                    v.getId(),
                    nombreCliente,
                    nombreProducto,
                    sdf.format(v.getFecha()),
                    v.getTotal());
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

package ui;

import model.Cliente;
import ventaService.ClienteService;

import java.util.List;
import java.util.Scanner;

public class ClienteUI {

    private final ClienteService clienteService;
    private final Scanner scanner;

    public ClienteUI(ClienteService clienteService, Scanner scanner) {
        this.clienteService = clienteService;
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE CLIENTES ===");
            System.out.println("1. Listar clientes");
            System.out.println("2. Agregar cliente");
            System.out.println("3. Actualizar cliente");
            System.out.println("4. Eliminar cliente");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> listarClientes();
                case 2 -> agregarCliente();
                case 3 -> actualizarCliente();
                case 4 -> eliminarCliente();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.println("\n--- LISTA DE CLIENTES ---");
            clientes.forEach(System.out::println);
        }
    }

    private void agregarCliente() {
        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();

        Cliente cliente = new Cliente(0, nombre, direccion);
        clienteService.agregarCliente(cliente);
    }

    private void actualizarCliente() {
        System.out.print("ID del cliente a actualizar: ");
        int id = leerEntero();
        Cliente existente = clienteService.buscarClientePorId(id);

        if (existente == null) {
            System.out.println("❌ No se encontró el cliente con ID " + id);
            return;
        }

        System.out.print("Nuevo nombre (" + existente.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (nombre.isEmpty()) nombre = existente.getNombre();

        System.out.print("Nueva dirección (" + existente.getDireccion() + "): ");
        String direccion = scanner.nextLine();
        if (direccion.isEmpty()) direccion = existente.getDireccion();

        existente.setNombre(nombre);
        existente.setDireccion(direccion);

        clienteService.actualizarCliente(existente);
    }

    private void eliminarCliente() {
        System.out.print("ID del cliente a eliminar: ");
        int id = leerEntero();
        clienteService.eliminarCliente(id);
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
}

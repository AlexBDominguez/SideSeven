package ui;

import ventaService.ClienteService;

import java.util.Scanner;

public class ClienteUI {

    private final ClienteService clienteService;
    private final Scanner scanner = new Scanner(System.in);

    public ClienteUI(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n--- Clientes ---");
            System.out.println("1. Listar");
            System.out.println("2. Agregar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Elige una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 -> {
                    System.out.println("\n¿Desde dónde quieres listar los clientes?");
                    System.out.println("1. CSV");
                    System.out.println("2. Base de Datos");
                    System.out.print("Elige una opción: ");
                    int fuente = leerEntero();

                    if (fuente == 1) {
                        var clientes = clienteService.listarClientesCSV();
                        if (clientes.isEmpty()) System.out.println("No hay clientes en CSV.");
                        else clientes.forEach(System.out::println);

                    } else if (fuente == 2) {
                        var clientes = clienteService.listarClientesDB();
                        if (clientes.isEmpty()) System.out.println("No hay clientes en Base de Datos.");
                        else clientes.forEach(System.out::println);

                    } else {
                        System.out.println("Opción no válida.");
                    }
                }
                case 2 -> agregarCliente();
                case 3 -> buscarCliente();
                case 4 -> actualizarCliente();
                case 5 -> eliminarCliente();
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

    }

    private void agregarCliente() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();

        clienteService.agregarCliente(nombre, direccion);
    }

    private void buscarCliente() {
        System.out.println("ID del cliente a buscar: ");
        int id = Integer.parseInt(scanner.nextLine());
        model.Cliente c = clienteService.buscarPorId(id);
        if (c != null) {
            System.out.println(c);
        } else {
            System.out.println("Cliente no encontrado.");
        }

    }

    private void actualizarCliente() {
        System.out.println("ID del cliente a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());
        model.Cliente cExistente = clienteService.buscarPorId(id);
        if (cExistente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.println("Cliente actual: " + cExistente);
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) cExistente.setNombre(nombre);

        System.out.print("Nueva dirección (Enter para mantener): ");
        String direccion = scanner.nextLine();
        if (!direccion.isEmpty()) cExistente.setDireccion(direccion);

        clienteService.actualizarCliente(cExistente);
        System.out.println("Cliente actualizado.");
    }

    private void eliminarCliente() {
        System.out.println("ID del cliente a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());
        clienteService.eliminarCliente(id);
        System.out.println("Cliente eliminado.");
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

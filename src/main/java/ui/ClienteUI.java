package ui;

import service.ClienteService;

import java.util.Scanner;

public class ClienteUI {

    private final ClienteService clienteService = new ClienteService();
    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu(){
        int opcion;

        do{
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
                case 1 -> clienteService.listarClientes().forEach(System.out::println);
                case 2 -> agregarCliente();
                case 3 -> buscarCliente();
                case 4 -> actualizarCliente();
                case 5 -> eliminarCliente();
                default -> System.out.println("Opción no válida.");
            }

        }while(opcion!=0);

    }

    private void agregarCliente(){
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

package app;

import db.MigracionDatos;
import ventaService.ClienteService;
import ventaService.ProductoService;
import ventaService.VentaService;
import ui.ClienteUI;
import ui.ProductoUI;
import ui.VentaUI;

import java.util.Scanner;

public class consolaApp {

    private final ProductoService productoService = new ProductoService();
    private final ClienteService clienteService = new ClienteService();
    private final VentaService ventaService = new VentaService(productoService, clienteService);

    private final ProductoUI productoUI = new ProductoUI(productoService);
    private final ClienteUI clienteUI = new ClienteUI(clienteService);
    private final VentaUI ventaUI = new VentaUI(ventaService);

    private final Scanner scanner = new Scanner(System.in);
    private boolean usarBD = false; // control global

    public void iniciar() {
        elegirFuenteDeDatos();

        int opcion;
        do {
            System.out.println("\n=== SideSeven - Tienda Geek ===");
            System.out.println("Fuente actual: " + (usarBD ? "Base de Datos" : "CSV"));
            System.out.println("1. Gestión de Productos");
            System.out.println("2. Gestión de Clientes");
            System.out.println("3. Gestión de Ventas");
            System.out.println("4. Cambiar Fuente de Datos");
            System.out.println("5. Migrar Datos a Base de Datos (Fase 2)");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> productoUI.mostrarMenu();
                case 2 -> clienteUI.mostrarMenu();
                case 3 -> ventaUI.mostrarMenu();
                case 4 -> elegirFuenteDeDatos();
                case 5 -> ejecutarMigracion();
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void elegirFuenteDeDatos() {
        System.out.println("\n¿Con qué fuente de datos deseas trabajar?");
        System.out.println("1. CSV");
        System.out.println("2. Base de Datos");
        System.out.print("Elige una opción: ");
        int fuente = leerEntero();

        usarBD = (fuente == 2);
        productoService.setUsarBD(usarBD);
        clienteService.setUsarBD(usarBD);
        ventaService.setUsarBD(usarBD);

        System.out.println("Fuente seleccionada: " + (usarBD ? "Base de Datos" : "CSV"));
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

    private void ejecutarMigracion() {
        System.out.println("\n=== MIGRACIÓN DE DATOS ===");
        System.out.println("Esta opción migrará todos los datos desde los archivos CSV a la base de datos MySQL.");
        System.out.println("Asegúrate de tener MySQL configurado y la base de datos 'sideseven_db' creada.");
        System.out.print("\n¿Deseas continuar? (S/N): ");

        String respuesta = scanner.nextLine().trim().toUpperCase();

        if (respuesta.equals("S") || respuesta.equals("SI")) {
            try {
                MigracionDatos.migrarDatos();
            } catch (Exception e) {
                System.out.println("\n⚠️ Verifica:");
                System.out.println("1. MySQL está corriendo");
                System.out.println("2. La base de datos 'sideseven_db' existe");
                System.out.println("3. Usuario y contraseña en DatabaseConnection.java son correctos");
            }
        } else {
            System.out.println("Migración cancelada.");
        }
    }
}

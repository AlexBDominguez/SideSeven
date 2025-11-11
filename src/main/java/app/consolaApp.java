package app;

import db.MigracionDatos;
import service.ClienteService;
import service.ProductoService;
import service.VentaService;
import ui.ClienteUI;
import ui.ProductoUI;
import ui.VentaUI;

import java.util.Scanner;

public class consolaApp {

    private final Scanner scanner = new Scanner(System.in);

    private final ProductoService productoService = new ProductoService();
    private final ClienteService clienteService = new ClienteService();
    private final VentaService ventaService = new VentaService(productoService, clienteService);

    private final ProductoUI productoUI = new ProductoUI(productoService, scanner);
    private final ClienteUI clienteUI = new ClienteUI(clienteService, scanner);
    private final VentaUI ventaUI = new VentaUI(ventaService, scanner);

    private boolean usarBD = false; // control global

    public void iniciar() {
        System.out.println("¡Bienvenido a SideSeven, tu tienda de cómics, mangas, manuales de rol, figuras de acción y más!");

        boolean salir = false;

        while (!salir) {
            // Si el usuario elige salir, terminamos el programa
            if (!elegirFuenteDeDatos()) {
                salir = true;
                System.out.println("¡Hasta luego!");
                break;
            }

            int opcion;
            do {
                System.out.println("\n=== SideSeven - Tienda Geek ===");
                System.out.println("Fuente actual: " + (usarBD ? "Base de Datos" : "CSV"));
                System.out.println("1. Gestión de Productos");
                System.out.println("2. Gestión de Clientes");
                System.out.println("3. Gestión de Ventas");
                System.out.println("4. Cambiar Fuente de Datos");
                System.out.println("5. Migrar Datos a Base de Datos (Fase 2)");
                System.out.println("0. Volver a elegir fuente de datos");
                System.out.print("Elige una opción: ");
                opcion = leerEntero();

                switch (opcion) {
                    case 1 -> productoUI.mostrarMenu();
                    case 2 -> clienteUI.mostrarMenu();
                    case 3 -> ventaUI.mostrarMenu();
                    case 4 -> elegirFuenteDeDatos();
                    case 5 -> ejecutarMigracion();
                    case 0 -> System.out.println("Volviendo al menú de selección de fuente...");
                    default -> System.out.println("Opción no válida.");
                }
            } while (opcion != 0);
        }

        scanner.close();
    }

    private boolean elegirFuenteDeDatos() {
        System.out.println("\n¿Con qué fuente de datos deseas trabajar?");
        System.out.println("1. CSV");
        System.out.println("2. Base de Datos");
        System.out.println("0. Salir del programa");
        System.out.print("Elige una opción: ");
        int fuente = leerEntero();

        switch (fuente) {
            case 1 -> {
                usarBD = false;
                productoService.setUsarBD(false);
                clienteService.setUsarBD(false);
                ventaService.setUsarBD(false);
                System.out.println("Fuente seleccionada: CSV");
                return true;
            }
            case 2 -> {
                usarBD = true;
                productoService.setUsarBD(true);
                clienteService.setUsarBD(true);
                ventaService.setUsarBD(true);
                System.out.println("Fuente seleccionada: Base de Datos");
                return true;
            }
            case 0 -> {
                return false; // indica al método iniciar() que debe salir del programa
            }
            default -> {
                System.out.println("Opción no válida.");
                return elegirFuenteDeDatos(); // vuelve a preguntar hasta que elija bien
            }
        }
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

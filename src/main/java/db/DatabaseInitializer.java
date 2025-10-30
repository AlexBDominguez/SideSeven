package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String createProductosTable = """
                CREATE TABLE IF NOT EXISTS productos (
                    id INT PRIMARY KEY,
                    nombre VARCHAR(255) NOT NULL,
                    categoria VARCHAR(100),
                    precio DOUBLE NOT NULL,
                    stock INT NOT NULL
                )
                """;
            stmt.execute(createProductosTable);
            System.out.println("Tabla 'productos' verificada/creada.");

            String createClientesTable = """
                CREATE TABLE IF NOT EXISTS clientes (
                    id INT PRIMARY KEY,
                    nombre VARCHAR(255) NOT NULL,
                    direccion VARCHAR(500)
                )
                """;
            stmt.execute(createClientesTable);
            System.out.println("Tabla 'clientes' verificada/creada.");

            String createVentasTable = """
                CREATE TABLE IF NOT EXISTS ventas (
                    id INT PRIMARY KEY,
                    id_cliente INT NOT NULL,
                    fecha BIGINT NOT NULL,
                    total DOUBLE NOT NULL,
                    FOREIGN KEY (id_cliente) REFERENCES clientes(id)
                )
                """;
            stmt.execute(createVentasTable);
            System.out.println("Tabla 'ventas' verificada/creada.");

            String createDetalleVentasTable = """
                CREATE TABLE IF NOT EXISTS detalle_ventas (
                    id_venta INT NOT NULL,
                    id_producto INT NOT NULL,
                    PRIMARY KEY (id_venta, id_producto),
                    FOREIGN KEY (id_venta) REFERENCES ventas(id),
                    FOREIGN KEY (id_producto) REFERENCES productos(id)
                )
                """;
            stmt.execute(createDetalleVentasTable);
            System.out.println("Tabla 'detalle_ventas' verificada/creada.");

            String createHistorialTable = """
                CREATE TABLE IF NOT EXISTS historial_compras (
                    id_cliente INT NOT NULL,
                    id_venta INT NOT NULL,
                    PRIMARY KEY (id_cliente, id_venta),
                    FOREIGN KEY (id_cliente) REFERENCES clientes(id),
                    FOREIGN KEY (id_venta) REFERENCES ventas(id)
                )
                """;
            stmt.execute(createHistorialTable);
            System.out.println("Tabla 'historial_compras' verificada/creada.");

            System.out.println("\n✓ Base de datos inicializada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

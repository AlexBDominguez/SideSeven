package db;

import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import model.Cliente;
import model.Producto;
import model.Venta;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MigracionDatos {

    private static final Map<Integer, Integer> mapeoProductos = new HashMap<>();
    private static final Map<Integer, Integer> mapeoClientes = new HashMap<>();

    public static void migrarDatos() throws Exception {
        System.out.println("\n=== INICIANDO MIGRACIÓN DE DATOS ===\n");

        try {
            DatabaseInitializer.initializeDatabase();
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar la base de datos: " + e.getMessage());
            throw e;
        }

        try {
            migrarProductos();
            migrarClientes();
            migrarVentas();
            System.out.println("\n✓ MIGRACIÓN COMPLETADA EXITOSAMENTE\n");
        } catch (Exception e) {
            System.err.println("\n❌ MIGRACIÓN FALLIDA: " + e.getMessage() + "\n");
            throw e;
        }
    }

    private static void migrarProductos() {
        System.out.println("Migrando productos...");
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> productos = productoDAO.leerProductos();

        String checkSQL = "SELECT COUNT(*) FROM productos WHERE nombre = ? AND categoria = ?";
        String insertSQL = "INSERT INTO productos (nombre, categoria, precio, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
             PreparedStatement insertStmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);
            int count = 0;

            for (Producto p : productos) {
                checkStmt.setString(1, p.getNombre());
                checkStmt.setString(2, p.getCategoria());
                ResultSet rs = checkStmt.executeQuery();
                rs.next();

                if (rs.getInt(1) > 0) continue;

                insertStmt.setString(1, p.getNombre());
                insertStmt.setString(2, p.getCategoria());
                insertStmt.setDouble(3, p.getPrecio());
                insertStmt.setInt(4, p.getStock());
                insertStmt.executeUpdate();

                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mapeoProductos.put(p.getId(), generatedKeys.getInt(1));
                    }
                }

                count++;
            }

            conn.commit();
            System.out.println("✓ " + count + " productos nuevos migrados correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al migrar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void migrarClientes() {
        System.out.println("Migrando clientes...");
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientes = clienteDAO.leerClientes();

        String checkSQL = "SELECT COUNT(*) FROM clientes WHERE nombre = ? AND direccion = ?";
        String insertSQL = "INSERT INTO clientes (nombre, direccion) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
             PreparedStatement insertStmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);
            int count = 0;

            for (Cliente c : clientes) {
                checkStmt.setString(1, c.getNombre());
                checkStmt.setString(2, c.getDireccion());
                ResultSet rs = checkStmt.executeQuery();
                rs.next();

                if (rs.getInt(1) > 0) continue;

                insertStmt.setString(1, c.getNombre());
                insertStmt.setString(2, c.getDireccion());
                insertStmt.executeUpdate();

                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mapeoClientes.put(c.getId(), generatedKeys.getInt(1));
                    }
                }

                count++;
            }

            conn.commit();
            System.out.println("✓ " + count + " clientes nuevos migrados correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al migrar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void migrarVentas() {
        System.out.println("Migrando ventas...");
        VentaDAO ventaDAO = new VentaDAO();
        List<Venta> ventas = ventaDAO.leerVentas();

        String checkSQL = "SELECT COUNT(*) FROM ventas WHERE id_cliente = ? AND id_producto = ? AND fecha = ? AND total = ?";
        String insertSQL = "INSERT INTO ventas (id_cliente, id_producto, fecha, total) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
             PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {

            conn.setAutoCommit(false);
            int count = 0;

            for (Venta v : ventas) {
                Integer nuevoIdCliente = mapeoClientes.get(v.getIdCliente());
                Integer nuevoIdProducto = mapeoProductos.get(v.getIdProducto());
                if (nuevoIdCliente == null || nuevoIdProducto == null) continue;

                checkStmt.setInt(1, nuevoIdCliente);
                checkStmt.setInt(2, nuevoIdProducto);
                checkStmt.setLong(3, v.getFecha().getTime());
                checkStmt.setDouble(4, v.getTotal());
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) continue;

                insertStmt.setInt(1, nuevoIdCliente);
                insertStmt.setInt(2, nuevoIdProducto);
                insertStmt.setLong(3, v.getFecha().getTime());
                insertStmt.setDouble(4, v.getTotal());
                insertStmt.executeUpdate();

                count++;
            }

            conn.commit();
            System.out.println("✓ " + count + " ventas nuevas migradas correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al migrar ventas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

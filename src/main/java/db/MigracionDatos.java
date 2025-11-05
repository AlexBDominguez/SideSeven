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
    private static final Map<Integer, Integer> mapeoVentas = new HashMap<>();

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
                if (rs.getInt(1) > 0) {
                    continue;
                }

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
                if (rs.getInt(1) > 0) {
                    continue;
                }

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

        String checkSQL = "SELECT COUNT(*) FROM ventas WHERE id_cliente = ? AND fecha = ? AND total = ?";
        String sqlVenta = "INSERT INTO ventas (id_cliente, fecha, total) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto) VALUES (?, ?)";
        String sqlHistorial = "INSERT INTO historial_compras (id_cliente, id_venta) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
             PreparedStatement pstmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtDetalle = conn.prepareStatement(sqlDetalle);
             PreparedStatement pstmtHistorial = conn.prepareStatement(sqlHistorial)) {

            conn.setAutoCommit(false);
            int count = 0;

            for (Venta v : ventas) {
                Integer nuevoIdCliente = mapeoClientes.get(v.getIdCliente());
                if (nuevoIdCliente == null) continue;

                // Verificar si la venta ya existe
                checkStmt.setInt(1, nuevoIdCliente);
                checkStmt.setLong(2, v.getFecha().getTime());
                checkStmt.setDouble(3, v.getTotal());
                ResultSet checkRs = checkStmt.executeQuery();
                checkRs.next();
                if (checkRs.getInt(1) > 0) {
                    continue;
                }

                pstmtVenta.setInt(1, nuevoIdCliente);
                pstmtVenta.setLong(2, v.getFecha().getTime());
                pstmtVenta.setDouble(3, v.getTotal());
                pstmtVenta.executeUpdate();

                int nuevoIdVenta;
                try (ResultSet generatedKeys = pstmtVenta.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new SQLException("No se pudo obtener el ID de la venta insertada");
                    }
                    nuevoIdVenta = generatedKeys.getInt(1);
                    mapeoVentas.put(v.getId(), nuevoIdVenta);
                }

                for (Integer idProductoAntiguo : v.getIdsProductos()) {
                    Integer nuevoIdProducto = mapeoProductos.get(idProductoAntiguo);
                    if (nuevoIdProducto == null) continue;

                    pstmtDetalle.setInt(1, nuevoIdVenta);
                    pstmtDetalle.setInt(2, nuevoIdProducto);
                    pstmtDetalle.executeUpdate();
                }

                pstmtHistorial.setInt(1, nuevoIdCliente);
                pstmtHistorial.setInt(2, nuevoIdVenta);
                pstmtHistorial.executeUpdate();

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
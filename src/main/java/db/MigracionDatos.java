package db;

import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import model.Cliente;
import model.Producto;
import model.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MigracionDatos {

    public static void migrarDatos() throws Exception {
        System.out.println("\n=== INICIANDO MIGRACIÓN DE DATOS ===\n");

        boolean exito = true;

        try {

            DatabaseInitializer.initializeDatabase();

        } catch (Exception e) {
            System.err.println("❌ Error al inicializar la base de datos: " + e.getMessage());
            exito = false;
            throw e; 
        }

        try {

            migrarProductos();
        } catch (Exception e) {
            System.err.println("❌ Error al migrar productos: " + e.getMessage());
            exito = false;
        }

        try {

            migrarClientes();
        } catch (Exception e) {
            System.err.println("❌ Error al migrar clientes: " + e.getMessage());
            exito = false;
        }

        try {

            migrarVentas();
        } catch (Exception e) {
            System.err.println("❌ Error al migrar ventas: " + e.getMessage());
            exito = false;
        }

        if (exito) {
            System.out.println("\n✓ MIGRACIÓN COMPLETADA EXITOSAMENTE\n");
        } else {
            System.out.println("\n❌ MIGRACIÓN COMPLETADA CON ERRORES\n");
            System.out.println("Revisa los mensajes de error anteriores.");
            throw new Exception("La migración no se completó correctamente");
        }
    }

    private static void migrarProductos() {
        System.out.println("Migrando productos...");
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> productos = productoDAO.leerProductos();

        String sql = "INSERT INTO productos (id, nombre, categoria, precio, stock) VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nombre=?, categoria=?, precio=?, stock=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int count = 0;
            for (Producto p : productos) {
                pstmt.setInt(1, p.getId());
                pstmt.setString(2, p.getNombre());
                pstmt.setString(3, p.getCategoria());
                pstmt.setDouble(4, p.getPrecio());
                pstmt.setInt(5, p.getStock());

                pstmt.setString(6, p.getNombre());
                pstmt.setString(7, p.getCategoria());
                pstmt.setDouble(8, p.getPrecio());
                pstmt.setInt(9, p.getStock());

                pstmt.executeUpdate();
                count++;
            }

            System.out.println("✓ " + count + " productos migrados correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al migrar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void migrarClientes() {
        System.out.println("Migrando clientes...");
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientes = clienteDAO.leerClientes();

        String sqlCliente = "INSERT INTO clientes (id, nombre, direccion) VALUES (?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE nombre=?, direccion=?";
        String sqlHistorial = "INSERT IGNORE INTO historial_compras (id_cliente, id_venta) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente);
             PreparedStatement pstmtHistorial = conn.prepareStatement(sqlHistorial)) {

            conn.setAutoCommit(false); 

            int count = 0;
            for (Cliente c : clientes) {

                pstmtCliente.setInt(1, c.getId());
                pstmtCliente.setString(2, c.getNombre());
                pstmtCliente.setString(3, c.getDireccion());
                pstmtCliente.setString(4, c.getNombre());
                pstmtCliente.setString(5, c.getDireccion());
                pstmtCliente.executeUpdate();

                for (Integer idVenta : c.getHistorialCompras()) {
                    pstmtHistorial.setInt(1, c.getId());
                    pstmtHistorial.setInt(2, idVenta);
                    pstmtHistorial.executeUpdate();
                }

                count++;
            }

            conn.commit(); 
            conn.setAutoCommit(true);

            System.out.println("✓ " + count + " clientes migrados correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al migrar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void migrarVentas() {
        System.out.println("Migrando ventas...");
        VentaDAO ventaDAO = new VentaDAO();
        List<Venta> ventas = ventaDAO.leerVentas();

        String sqlVenta = "INSERT INTO ventas (id, id_cliente, fecha, total) VALUES (?, ?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE id_cliente=?, fecha=?, total=?";
        String sqlDetalle = "INSERT IGNORE INTO detalle_ventas (id_venta, id_producto) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtVenta = conn.prepareStatement(sqlVenta);
             PreparedStatement pstmtDetalle = conn.prepareStatement(sqlDetalle)) {

            conn.setAutoCommit(false); 

            int count = 0;
            for (Venta v : ventas) {

                pstmtVenta.setInt(1, v.getId());
                pstmtVenta.setInt(2, v.getIdCliente());
                pstmtVenta.setLong(3, v.getFecha().getTime());
                pstmtVenta.setDouble(4, v.getTotal());
                pstmtVenta.setInt(5, v.getIdCliente());
                pstmtVenta.setLong(6, v.getFecha().getTime());
                pstmtVenta.setDouble(7, v.getTotal());
                pstmtVenta.executeUpdate();

                for (Integer idProducto : v.getIdsProductos()) {
                    pstmtDetalle.setInt(1, v.getId());
                    pstmtDetalle.setInt(2, idProducto);
                    pstmtDetalle.executeUpdate();
                }

                count++;
            }

            conn.commit(); 
            conn.setAutoCommit(true);

            System.out.println("✓ " + count + " ventas migradas correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al migrar ventas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

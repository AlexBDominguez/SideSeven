package dao;

import db.DatabaseConnection;
import model.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaDAODB {

    public List<Venta> leerVentas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Venta v = new Venta(
                        rs.getInt("id"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_producto"),
                        new Date(rs.getLong("fecha")),
                        rs.getDouble("total")
                );
                lista.add(v);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer ventas de BD: " + e.getMessage());
        }

        return lista;
    }

    public void guardarVenta(Venta v) {
        String sql = "INSERT INTO ventas (id_cliente, id_producto, fecha, total) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, v.getIdCliente());
            pstmt.setInt(2, v.getIdProducto());
            pstmt.setLong(3, v.getFecha().getTime());
            pstmt.setDouble(4, v.getTotal());
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                v.setId(keys.getInt(1));
            }

            System.out.println("✅ Venta guardada correctamente (ID: " + v.getId() + ")");

        } catch (SQLException e) {
            System.err.println("Error al guardar venta en BD: " + e.getMessage());
        }
    }

    public Venta buscarPorId(int id) {
        String sql = "SELECT * FROM ventas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Venta(
                        rs.getInt("id"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_producto"),
                        new Date(rs.getLong("fecha")),
                        rs.getDouble("total")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar venta: " + e.getMessage());
        }

        return null;
    }

    public void eliminarVenta(int id) {
        String sql = "DELETE FROM ventas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();

            if (filas > 0)
                System.out.println("Venta eliminada (ID " + id + ")");
            else
                System.out.println("No se encontró la venta con ID " + id);

        } catch (SQLException e) {
            System.err.println("Error al eliminar venta: " + e.getMessage());
        }
    }
}

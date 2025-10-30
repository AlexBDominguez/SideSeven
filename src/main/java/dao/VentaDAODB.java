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
                int idVenta = rs.getInt("id");

                List<Integer> idsProductos = cargarProductosVenta(idVenta);

                Venta v = new Venta(
                    idVenta,
                    rs.getInt("id_cliente"),
                    new Date(rs.getLong("fecha")),
                    idsProductos,
                    rs.getDouble("total")
                );
                lista.add(v);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer ventas de BD: " + e.getMessage());
        }

        return lista;
    }

    private List<Integer> cargarProductosVenta(int idVenta) {
        List<Integer> idsProductos = new ArrayList<>();
        String sql = "SELECT id_producto FROM detalle_ventas WHERE id_venta = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idVenta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                idsProductos.add(rs.getInt("id_producto"));
            }

        } catch (SQLException e) {
            System.err.println("Error al cargar productos de venta: " + e.getMessage());
        }

        return idsProductos;
    }

    public void guardarVenta(Venta v) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

            String sqlVenta = "INSERT INTO ventas (id, id_cliente, fecha, total) VALUES (?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE id_cliente=?, fecha=?, total=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlVenta)) {
                pstmt.setInt(1, v.getId());
                pstmt.setInt(2, v.getIdCliente());
                pstmt.setLong(3, v.getFecha().getTime());
                pstmt.setDouble(4, v.getTotal());
                pstmt.setInt(5, v.getIdCliente());
                pstmt.setLong(6, v.getFecha().getTime());
                pstmt.setDouble(7, v.getTotal());
                pstmt.executeUpdate();
            }

            String sqlDeleteDetalle = "DELETE FROM detalle_ventas WHERE id_venta = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDetalle)) {
                pstmt.setInt(1, v.getId());
                pstmt.executeUpdate();
            }

            String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDetalle)) {
                for (Integer idProducto : v.getIdsProductos()) {
                    pstmt.setInt(1, v.getId());
                    pstmt.setInt(2, idProducto);
                    pstmt.executeUpdate();
                }
            }

            conn.commit(); 

        } catch (SQLException e) {
            System.err.println("Error al guardar venta en BD: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); 
                    System.out.println("Transacción revertida.");
                } catch (SQLException ex) {
                    System.err.println("Error al revertir transacción: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error al restaurar autocommit: " + e.getMessage());
                }
            }
        }
    }

    public Venta buscarPorId(int id) {
        String sql = "SELECT * FROM ventas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                List<Integer> idsProductos = cargarProductosVenta(id);

                return new Venta(
                    rs.getInt("id"),
                    rs.getInt("id_cliente"),
                    new Date(rs.getLong("fecha")),
                    idsProductos,
                    rs.getDouble("total")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar venta en BD: " + e.getMessage());
        }

        return null;
    }
}

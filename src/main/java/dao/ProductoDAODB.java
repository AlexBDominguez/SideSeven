package dao;

import db.DatabaseConnection;
import model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAODB {

    public List<Producto> leerProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer productos de BD: " + e.getMessage());
        }

        return lista;
    }

    public void guardarProducto(Producto p) {
        String sql = "INSERT INTO productos (id, nombre, categoria, precio, stock) VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nombre=?, categoria=?, precio=?, stock=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

        } catch (SQLException e) {
            System.err.println("Error al guardar producto en BD: " + e.getMessage());
        }
    }

    public void eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Producto eliminado de la BD.");
            } else {
                System.out.println("No se encontró producto con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto de BD: " + e.getMessage());
        }
    }

    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto en BD: " + e.getMessage());
        }

        return null;
    }

    public void actualizarStock(int id, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar stock en BD: " + e.getMessage());
        }
    }
}

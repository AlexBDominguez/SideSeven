package dao;

import db.DatabaseConnection;
import model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAODB {

    public List<Cliente> leerClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion")
                );

                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer clientes de BD: " + e.getMessage());
        }

        return lista;
    }

    public void guardarCliente(Cliente c) {
        String sql = "INSERT INTO clientes (id, nombre, direccion) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE nombre=?, direccion=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, c.getId());
            pstmt.setString(2, c.getNombre());
            pstmt.setString(3, c.getDireccion());
            pstmt.setString(4, c.getNombre());
            pstmt.setString(5, c.getDireccion());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al guardar cliente en BD: " + e.getMessage());
        }
    }

    public void eliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Cliente eliminado de la BD.");
            } else {
                System.out.println("No se encontró cliente con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente de BD: " + e.getMessage());
        }
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar cliente en BD: " + e.getMessage());
        }

        return null;
    }
}

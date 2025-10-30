package service;

import dao.ClienteDAO;
import model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final List<Cliente> clientes = new ArrayList<>();

    public ClienteService() {
        clientes.addAll(clienteDAO.leerClientes());
    }

    public List<Cliente> listarClientes() {
        return clientes;
    }

    private int generarNuevoId() {
        if (clientes.isEmpty()) {
            return 1;
        }
        return clientes.stream()
                .mapToInt(Cliente::getId)
                .max()
                .orElse(0) + 1;
    }

    public void agregarCliente(String nombre, String direccion) {
        int nuevoId = generarNuevoId();
        Cliente c = new Cliente(nuevoId, nombre, direccion);
        clientes.add(c);
        clienteDAO.guardarClientes(clientes);
        System.out.println("✓ Cliente agregado con ID: " + nuevoId);
    }

    public void agregarCliente(Cliente c) {
        clientes.add(c);
        clienteDAO.guardarClientes(clientes);
    }

    public Cliente buscarPorId(int id) {
        return clientes.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void actualizarCliente(Cliente cNuevo) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == cNuevo.getId()) {
                clientes.set(i, cNuevo);
                break;
            }
        }
        clienteDAO.guardarClientes(clientes);
    }

    public void eliminarCliente(int id) {
        clientes.removeIf(c -> c.getId() == id);
        clienteDAO.guardarClientes(clientes);
    }

    public void agregarVentaCliente(int idCliente, int idVenta) {
        Cliente c = buscarPorId(idCliente);
        if (c != null) {
            c.agregarCompra(idVenta);
            clienteDAO.guardarClientes(clientes);
        }
    }
}

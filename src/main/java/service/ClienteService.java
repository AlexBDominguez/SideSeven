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

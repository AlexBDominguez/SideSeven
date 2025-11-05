package ventaService;

import dao.ClienteDAO;
import dao.ClienteDAODB;
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
        List<String> errores = new ArrayList<>();
        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("❌ Error: El nombre del cliente no puede estar vacío.");
        }

        if (direccion == null || direccion.trim().isEmpty()) {
            errores.add("❌ Error: La dirección no puede estar vacía.");
        }

        if(!errores.isEmpty()){
            System.out.println("❌ No se pudo agregar el cliente por los siguientes errores:");
            errores.forEach(e-> System.out.println("  - " + e));
            return;
        }

        int nuevoId = generarNuevoId();
        Cliente c = new Cliente(nuevoId, nombre.trim(), direccion.trim());
        clientes.add(c);
        clienteDAO.guardarClientes(clientes);
        System.out.println("✓ Cliente agregado con ID: " + nuevoId);
    }

    public void agregarCliente(Cliente c) {
        if (c == null) {
            System.out.println("❌ Error: El cliente no puede ser nulo.");
            return;
        }
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
        if (cNuevo == null) {
            System.out.println("❌ Error: Cliente no válido.");
            return;
        }

        boolean encontrado = false;
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == cNuevo.getId()) {
                clientes.set(i, cNuevo);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("⚠️ No se encontró ningún cliente con el ID especificado.");
            return;
        }

        clienteDAO.guardarClientes(clientes);
        System.out.println("✓ Cliente actualizado correctamente.");
    }

    public void eliminarCliente(int id) {
        boolean eliminado = clientes.removeIf(c -> c.getId() == id);
        if (eliminado) {
            clienteDAO.guardarClientes(clientes);
            System.out.println("✓ Cliente eliminado con éxito.");
        } else {
            System.out.println("⚠️ No se encontró el cliente con ID: " + id);
        }
    }

    public void agregarVentaCliente(int idCliente, int idVenta) {
        Cliente c = buscarPorId(idCliente);
        if (c != null) {
            c.agregarCompra(idVenta);
            clienteDAO.guardarClientes(clientes);
        } else {
            System.out.println("⚠️ No se encontró el cliente con ID: " + idCliente);
        }
    }

    public List<Cliente> listarClientesCSV() {
        return clienteDAO.leerClientes();
    }

    public List<Cliente> listarClientesDB() {
        ClienteDAODB clienteDAODB = new ClienteDAODB();
        return clienteDAODB.leerClientes();
    }

}

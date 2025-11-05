package ventaService;

import dao.ClienteDAO;
import dao.ClienteDAODB;
import model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ClienteDAODB clienteDAODB = new ClienteDAODB();

    private boolean usarBD = false;

    public void setUsarBD(boolean usarBD) {
        this.usarBD = usarBD;
    }

    public List<Cliente> listarClientes() {
        return usarBD ? clienteDAODB.leerClientes() : clienteDAO.leerClientes();
    }

    public void agregarCliente(Cliente cliente) {
        List<String> errores = validarCliente(cliente, false);

        if (!errores.isEmpty()) {
            System.err.println("❌ No se pudo agregar el cliente. Se encontraron los siguientes errores:");
            errores.forEach(e -> System.err.println("  - " + e));
            return;
        }

        if (usarBD) {
            clienteDAODB.guardarCliente(cliente);
        } else {
            List<Cliente> lista = clienteDAO.leerClientes();
            lista.add(cliente);
            clienteDAO.guardarClientes(lista);
        }

        System.out.println("✅ Cliente agregado correctamente: " + cliente.getNombre());
    }

    public void actualizarCliente(Cliente cliente) {
        List<String> errores = validarCliente(cliente, true);

        if (!errores.isEmpty()) {
            System.err.println("❌ No se pudo actualizar el cliente. Se encontraron los siguientes errores:");
            errores.forEach(e -> System.err.println("  - " + e));
            return;
        }

        if (usarBD) {
            clienteDAODB.guardarCliente(cliente);
        } else {
            List<Cliente> lista = clienteDAO.leerClientes();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId() == cliente.getId()) {
                    lista.set(i, cliente);
                    break;
                }
            }
            clienteDAO.guardarClientes(lista);
        }

        System.out.println("✅ Cliente actualizado correctamente (ID " + cliente.getId() + ")");
    }

    public void eliminarCliente(int id) {
        Cliente existente = buscarClientePorId(id);
        if (existente == null) {
            System.err.println("❌ No existe un cliente con ID " + id);
            return;
        }

        if (usarBD) {
            clienteDAODB.eliminarCliente(id);
        } else {
            List<Cliente> lista = clienteDAO.leerClientes();
            lista.removeIf(c -> c.getId() == id);
            clienteDAO.guardarClientes(lista);
        }

        System.out.println("Cliente eliminado: " + existente.getNombre());
    }

    public Cliente buscarClientePorId(int id) {
        if (usarBD) {
            return clienteDAODB.buscarPorId(id);
        } else {
            return clienteDAO.leerClientes()
                    .stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
    }

    private List<String> validarCliente(Cliente cliente, boolean esActualizacion) {
        List<String> errores = new ArrayList<>();

        if (cliente == null) {
            errores.add("El cliente no puede ser nulo.");
            return errores;
        }

        if (esActualizacion && buscarClientePorId(cliente.getId()) == null) {
            errores.add("No existe un cliente con el ID especificado (" + cliente.getId() + ").");
        }

        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            errores.add("El nombre del cliente no puede estar vacío.");
        }

        if (cliente.getDireccion() == null || cliente.getDireccion().isBlank()) {
            errores.add("La dirección del cliente no puede estar vacía.");
        }

        if (!esActualizacion) {
            boolean duplicado = listarClientes().stream()
                    .anyMatch(c -> c.getNombre().equalsIgnoreCase(cliente.getNombre()));
            if (duplicado) {
                errores.add("Ya existe un cliente con el nombre '" + cliente.getNombre() + "'.");
            }
        }

        return errores;
    }
}

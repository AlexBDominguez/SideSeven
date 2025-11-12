package service;

import dao.VentaDAO;
import dao.VentaDAODB;
import model.Venta;

import java.util.ArrayList;
import java.util.List;

public class VentaService {

    private final VentaDAO ventaDAO = new VentaDAO();
    private final VentaDAODB ventaDAODB = new VentaDAODB();
    private final ProductoService productoService;
    private final ClienteService clienteService;

    private boolean usarBD = false;

    public VentaService(ProductoService productoService, ClienteService clienteService) {
        this.productoService = productoService;
        this.clienteService = clienteService;
    }

    public void setUsarBD(boolean usarBD) {
        this.usarBD = usarBD;
    }

    public List<Venta> listarVentas() {
        return usarBD ? ventaDAODB.leerVentas() : ventaDAO.leerVentas();
    }

    public void registrarVenta(Venta venta) {
        List<String> errores = validarVenta(venta);

        if (!errores.isEmpty()) {
            System.err.println("❌ No se pudo registrar la venta. Se encontraron los siguientes errores:");
            errores.forEach(e -> System.err.println("  - " + e));
            return;
        }

        if (usarBD) {
            ventaDAODB.guardarVenta(venta);
        } else {
            List<Venta> lista = ventaDAO.leerVentas();

            int nuevoId = lista.isEmpty() ? 1 : lista.getLast().getId() + 1;
            venta.setId(nuevoId);

            lista.add(venta);
            ventaDAO.guardarVentas(lista);
        }

        System.out.println("✅ Venta registrada correctamente (Cliente ID: " + venta.getIdCliente() + ")");
    }


    public void eliminarVenta(int id) {
        Venta existente = buscarVentaPorId(id);
        if (existente == null) {
            System.err.println("❌ No existe una venta con ID " + id);
            return;
        }

        if (usarBD) {
            ventaDAODB.eliminarVenta(id);

        } else {
            List<Venta> lista = ventaDAO.leerVentas();
            lista.removeIf(v -> v.getId() == id);
            ventaDAO.guardarVentas(lista);
        }

        System.out.println("Venta eliminada (ID " + id + ")");
    }

    public Venta buscarVentaPorId(int id) {
        if (usarBD) {
            return ventaDAODB.buscarPorId(id);
        } else {
            return ventaDAO.leerVentas()
                    .stream()
                    .filter(v -> v.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
    }

    private List<String> validarVenta(Venta venta) {
        List<String> errores = new ArrayList<>();

        if (venta == null) {
            errores.add("La venta no puede ser nula.");
            return errores;
        }

        if (clienteService.buscarClientePorId(venta.getIdCliente()) == null) {
            errores.add("El cliente con ID " + venta.getIdCliente() + " no existe.");
        }

        if (productoService.buscarProductoPorId(venta.getIdProducto()) == null) {
            errores.add("El producto con ID " + venta.getIdProducto() + " no existe.");
        }

        if (venta.getTotal() <= 0) {
            errores.add("El total de la venta debe ser mayor a 0.");
        }

        return errores;
    }

}

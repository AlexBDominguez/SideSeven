package ventaService;

import dao.VentaDAO;
import dao.VentaDAODB;
import model.Producto;
import model.Venta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaService {

    private final VentaDAO ventaDAO = new VentaDAO();
    private final ProductoService productoService;
    private final ClienteService clienteService;
    private final List<Venta> ventas = new ArrayList<>();

    public VentaService(ProductoService productoService, ClienteService clienteService) {
        this.productoService = productoService;
        this.clienteService = clienteService;
        ventas.addAll(ventaDAO.leerVentas());
    }

    public List<Venta> listarVentas() {
        return new ArrayList<>(ventas);
    }

    private int generarNuevoId() {
        if (ventas.isEmpty()) {
            return 1;
        }
        return ventas.stream()
                .mapToInt(Venta::getId)
                .max()
                .orElse(0) + 1;
    }

    public void registrarVenta(int idCliente, List<Integer> idsProductos) {
        List<String> errores = new ArrayList<>();

        if(clienteService.buscarPorId(idCliente)==null){
            errores.add("❌ Error: El cliente con ID " + idCliente + " no existe.");
        }
        if(idsProductos == null || idsProductos.isEmpty()){
            errores.add("❌ Error: Debes seleccionar al menos un producto para la venta.");
        }else{
            for (int idProd : idsProductos) {
                Producto p = productoService.buscarPorId(idProd);
                if (p == null) {
                    errores.add("❌ Error: El producto con ID " + idProd + " no existe.");
                } else if (p.getStock() <= 0) {
                    errores.add("❌ Error: No hay stock disponible para el producto: " + p.getNombre());
                }
            }
        }
        if(!errores.isEmpty()){
            System.out.println("❌ No se pudo registrar la venta por los siguientes errores:");
            errores.forEach(e-> System.out.println("  - " + e));
            return;
        }

        double total = 0.0;
        for (int idProd : idsProductos) {
            Producto p = productoService.buscarPorId(idProd);
            total += p.getPrecio();
            p.setStock(p.getStock() - 1);
            productoService.actualizarProducto(p);
        }

        int nuevoId = generarNuevoId();
        Venta v = new Venta(nuevoId, idCliente, new Date(), idsProductos, total);
        ventas.add(v);
        ventaDAO.guardarVentas(ventas);
        clienteService.agregarVentaCliente(idCliente, nuevoId);
        System.out.println("✓ Venta registrada con ID: " + nuevoId + " - Total: " + total + "€");

    }


    public Venta buscarPorId(int id) {
        for (Venta v : ventas) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public List<Venta> listarVentasCSV() {
        return ventaDAO.leerVentas();
    }

    public List<Venta> listarVentasDB() {
        VentaDAODB ventaDAODB = new VentaDAODB();
        return ventaDAODB.leerVentas();
    }
}

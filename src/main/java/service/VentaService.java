package service;

import dao.VentaDAO;
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

    public List<Venta> listarVentas() { return ventas; }

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
        int nuevoId = generarNuevoId();
        registrarVentaConId(nuevoId, idCliente, idsProductos);
    }

    private void registrarVentaConId(int idVenta, int idCliente, List<Integer> idsProductos){
        double total = 0.0;
        for(int idProd: idsProductos){
            Producto p = productoService.buscarPorId(idProd);
            if(p != null && p.getStock() > 0){
                total += p.getPrecio();
                p.setStock(p.getStock() - 1);
                productoService.actualizarProducto(p);
            }else{
                System.out.println("No hay stock disponible para el producto con id: " + idProd);
            }
        }

        Venta v = new Venta(idVenta, idCliente, new Date(), idsProductos, total);
        ventas.add(v);
        ventaDAO.guardarVentas(ventas);
        clienteService.agregarVentaCliente(idCliente, idVenta);
        System.out.println("✓ Venta registrada con ID: " + idVenta + " - Total: " + total + "€");
    }

    public Venta buscarPorId(int id) {
        for (Venta v : ventas) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

}

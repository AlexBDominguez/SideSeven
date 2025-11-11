package model;

import java.io.Serializable;
import java.util.Date;

public class Venta implements Serializable {

    private int id;
    private int idCliente;
    private int idProducto;
    private Date fecha;
    private double total;

    public Venta(int id, int idCliente, int idProducto, Date fecha, double total) {
        this.id = id;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.fecha = fecha;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return String.format("[Venta #%d] Cliente ID: %d | Producto ID: %d | Fecha: %s | Total: %.2f€",
                id, idCliente, idProducto, sdf.format(fecha), total);
    }
}

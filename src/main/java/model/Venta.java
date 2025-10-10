package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Venta implements Serializable {

    private int id;
    private int idCliente;
    private Date fecha;
    private List<Integer> idsProductos;
    private double total;

    public Venta(int id, int idCliente, Date fecha, List<Integer> idsProductos, double total) {
        this.id = id;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.idsProductos = idsProductos;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Integer> getIdsProductos() {
        return idsProductos;
    }

    public void setIdsProductos(List<Integer> idsProductos) {
        this.idsProductos = idsProductos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", fecha=" + fecha +
                ", idsProductos=" + idsProductos +
                ", total=" + total +
                '}';
    }
}

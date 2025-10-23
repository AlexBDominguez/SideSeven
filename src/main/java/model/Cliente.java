package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cliente implements Serializable {

    private int id;
    private String nombre;
    private String direccion;
    private List<Integer> historialCompras;

    public Cliente(int id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.historialCompras = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public List<Integer> getHistorialCompras() { return historialCompras; }
    public void setHistorialCompras(List<Integer> historialCompras) { this.historialCompras = historialCompras; }

    public void agregarCompra(int idVenta) { historialCompras.add(idVenta); }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", historialCompras=" + historialCompras +
                '}';
    }
}
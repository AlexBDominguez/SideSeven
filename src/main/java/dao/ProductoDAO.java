package dao;

import model.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private static final String FILE_PATH = "data/productos.csv";

    public List<Producto> leerProductos() {
        List<Producto> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                Producto p = new Producto(
                        Integer.parseInt(datos[0]),
                        datos[1],
                        datos[2],
                        Double.parseDouble(datos[3]),
                        Integer.parseInt(datos[4])
                );
                lista.add(p);
            }
        } catch (IOException e) {
            System.out.println("Error al leer productos: " + e.getMessage());
        }
        return lista;
    }

    public void guardarProductos(List<Producto> lista) {
        try(PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Producto p : lista) {
                pw.println(p.getId() + "," + p.getNombre() + "," + p.getCategoria() + "," + p.getPrecio() + "," + p.getStock());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar productos: " + e.getMessage());
        }
    }
}

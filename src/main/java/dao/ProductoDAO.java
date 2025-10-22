package dao;

import model.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private static final String FILE_PATH = "data/productos.csv";

    public List<Producto> leerProductos() {
        List<Producto> lista = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) { // si no existe, lo crea
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de productos: " + e.getMessage());
                return lista;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null && !linea.isEmpty()) {
                String[] datos = linea.split(",");
                if (datos.length < 5) continue; // evita errores si la línea está mal
                Producto p = new Producto(
                        Integer.parseInt(datos[0]),
                        datos[1],
                        datos[2],
                        Double.parseDouble(datos[3]),
                        Integer.parseInt(datos[4])
                );
                lista.add(p);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al leer productos: " + e.getMessage());
        }
        return lista;
    }

    public void guardarProductos(List<Producto> lista) {
        File file = new File(FILE_PATH);
        try {
            file.getParentFile().mkdirs();
            try(PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (Producto p : lista) {
                    pw.println(p.getId() + "," + p.getNombre() + "," + p.getCategoria() + "," + p.getPrecio() + "," + p.getStock());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar productos: " + e.getMessage());
        }
    }
}

package dao;

import model.Venta;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaDAO {

    private static final String FILE_PATH = "data/ventas.csv";

    public List<Venta> leerVentas() {
        List<Venta> lista = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de ventas: " + e.getMessage());
                return lista;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null && !linea.isEmpty()) {
                String[] datos = linea.split(",");
                if (datos.length < 5) continue;

                List<Integer> productosIds = new ArrayList<>();
                String[] ids = datos[3].split(";");
                for (String s : ids) {
                    productosIds.add(Integer.parseInt(s));
                }

                Venta v = new Venta(
                        Integer.parseInt(datos[0]),
                        Integer.parseInt(datos[1]),
                        new Date(Long.parseLong(datos[2])),
                        productosIds,
                        Double.parseDouble(datos[4])
                );
                lista.add(v);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al leer ventas: " + e.getMessage());
        }
        return lista;
    }

    public void guardarVentas(List<Venta> lista) {
        File file = new File(FILE_PATH);
        try {
            file.getParentFile().mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (Venta v : lista) {
                    String productosIds = String.join(";", v.getIdsProductos().stream().map(String::valueOf).toList());
                    pw.println(v.getId() + "," + v.getIdCliente() + "," + v.getFecha().getTime() + "," + productosIds + "," + v.getTotal());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar ventas: " + e.getMessage());
        }
    }
}

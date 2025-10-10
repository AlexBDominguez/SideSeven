package dao;

import model.Venta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class VentaDAO {

    private static final String FILE_PATH = "data/ventas.csv";

    public List<Venta> leerVentas(){
        List<Venta> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                Venta v = new Venta(
                        Integer.parseInt(datos[0]),
                        Integer.parseInt(datos[1]),
                        new Date(Long.parseLong(datos[2])),
                        List.of(Integer.parseInt(datos[3])),
                        Double.parseDouble(datos[4])
                );
                lista.add(v);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
}

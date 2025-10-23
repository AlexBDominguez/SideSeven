package dao;

import model.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String FILE_PATH = "data/clientes.csv";

    public List<Cliente> leerClientes() {
        List<Cliente> lista = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de clientes: " + e.getMessage());
                return lista;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null && !linea.isEmpty()) {
                String[] datos = linea.split(",", -1);
                if (datos.length < 4) continue;

                Cliente c = new Cliente(
                        Integer.parseInt(datos[0]),
                        datos[1],
                        datos[2]
                );

                List<Integer> historial = new ArrayList<>();
                if (!datos[3].isEmpty()) {
                    String[] compras = datos[3].split(";");
                    for (String s : compras) {
                        try {
                            historial.add(Integer.parseInt(s));
                        } catch (NumberFormatException ignored) {}
                    }
                }
                c.setHistorialCompras(historial);

                lista.add(c);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al leer clientes: " + e.getMessage());
        }

        return lista;
    }

    public void guardarClientes(List<Cliente> lista) {
        File file = new File(FILE_PATH);
        try {
            file.getParentFile().mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (Cliente c : lista) {
                    String historial = String.join(";", c.getHistorialCompras().stream().map(String::valueOf).toList());
                    pw.println(c.getId() + "," + c.getNombre() + "," + c.getDireccion() + "," + historial);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }
}

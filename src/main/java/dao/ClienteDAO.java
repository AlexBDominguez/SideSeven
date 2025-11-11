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
                if (datos.length < 3) continue; // ahora solo id, nombre, direccion

                Cliente c = new Cliente(
                        Integer.parseInt(datos[0]),
                        datos[1],
                        datos[2]
                );

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
                    pw.println(c.getId() + "," + c.getNombre() + "," + c.getDireccion());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }
}

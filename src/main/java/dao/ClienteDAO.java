package dao;

import model.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private static final String FILE_PATH = "data/clientes.csv";

    public List<Cliente> leerClientes() throws FileNotFoundException {
        List<Cliente> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                Cliente c = new Cliente(
                        Integer.parseInt(datos[0]),
                        datos[1],
                        datos[2],
                        datos[3]
                );
                lista.add(c);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lista;

    }

    public void guardarClientes(List<Cliente> lista){
        try(PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Cliente c : lista) {
                pw.println(c.getId() + "," + c.getNombre() + "," + c.getCorreo() + "," + c.getDireccion());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }

}

package persistencia;

import clases.EstadoProducto;
import clases.CategoriaProducto;
import clases.Smartphone;
import clases.Laptop;
import clases.Accesorio;
import clases.Producto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SerializadorCSV {

    // header:
    // id;nombre;precio;stock;categoria;estado;tipoClase;ram;almacenamiento;so;tipoAccesorio;colorAccesorio
    public static void guardar(List<? extends Producto> lista, Path ruta) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(ruta)) {
            bw.write("id;nombre;precio;stock;categoria;estado;tipoClase;ram;almacenamiento;so;tipoAccesorio;colorAccesorio");
            bw.newLine();

            for (Producto p : lista) {

                String tipoClase = "ACCESORIO";
                String ram = "";
                String alm = "";
                String so = "";
                String tipoAcc = "";
                String colorAcc = "";

                if (p instanceof Laptop) {
                    Laptop lap = (Laptop) p;
                    tipoClase = "LAPTOP";
                    ram = String.valueOf(lap.getRamGb());
                    alm = String.valueOf(lap.getAlmacenamientoGb());
                } else if (p instanceof Smartphone) {
                    Smartphone sp = (Smartphone) p;
                    tipoClase = "SMARTPHONE";
                    so = sp.getSistemaOperativo();
                    alm = String.valueOf(sp.getAlmacenamientoGb());
                } else if (p instanceof Accesorio) {
                    Accesorio ac = (Accesorio) p;
                    tipoClase = "ACCESORIO";
                    tipoAcc = ac.getTipo();
                    colorAcc = ac.getColor();
                }

                bw.write(p.getId() + ";" +
                         p.getNombre() + ";" +
                         p.getPrecio() + ";" +
                         p.getStock() + ";" +
                         p.getCategoria().name() + ";" +
                         p.getEstado().name() + ";" +
                         tipoClase + ";" +
                         ram + ";" +
                         alm + ";" +
                         so + ";" +
                         tipoAcc + ";" +
                         colorAcc);
                bw.newLine();
            }
        }
    }

    public static List<Producto> cargar(Path ruta) throws IOException {
        List<Producto> resultado = new ArrayList<>();

        if (!Files.exists(ruta)) {
            return resultado;
        }

        try (BufferedReader br = Files.newBufferedReader(ruta)) {
            String linea = br.readLine();

            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;

                String[] partes = linea.split(";");
                if (partes.length < 6) continue;

                int idx = 0;
                int id = Integer.parseInt(partes[idx++]);
                String nombre = partes[idx++];
                double precio = Double.parseDouble(partes[idx++]);
                int stock = Integer.parseInt(partes[idx++]);
                CategoriaProducto categoria = CategoriaProducto.valueOf(partes[idx++]);
                EstadoProducto estado = EstadoProducto.valueOf(partes[idx++]);

                String tipoClase = (partes.length > idx) ? partes[idx++] : "ACCESORIO";
                String ramStr = (partes.length > idx) ? partes[idx++] : "";
                String almStr = (partes.length > idx) ? partes[idx++] : "";
                String so = (partes.length > idx) ? partes[idx++] : "";
                String tipoAcc = (partes.length > idx) ? partes[idx++] : "";
                String colorAcc = (partes.length > idx) ? partes[idx++] : "";

                Producto p;
                switch (tipoClase) {
                    case "LAPTOP":
                        int ram = ramStr.isBlank() ? 8 : Integer.parseInt(ramStr);
                        int alm = almStr.isBlank() ? 256 : Integer.parseInt(almStr);
                        p = new Laptop(id, nombre, precio, stock, categoria, estado, ram, alm);
                        break;
                    case "SMARTPHONE":
                        int almSp = almStr.isBlank() ? 64 : Integer.parseInt(almStr);
                        String soFinal = so.isBlank() ? "Android" : so;
                        p = new Smartphone(id, nombre, precio, stock, categoria, estado, soFinal, almSp);
                        break;
                    case "ACCESORIO":
                    default:
                        String tipoFinal = tipoAcc.isBlank() ? "Genérico" : tipoAcc;
                        String colorFinal = colorAcc.isBlank() ? "Negro" : colorAcc;
                        p = new Accesorio(id, nombre, precio, stock, categoria, estado, tipoFinal, colorFinal);
                        break;
                }

                resultado.add(p);
            }
        }

        return resultado;
    }
}

package persistencia;

import clases.Accesorio;
import clases.Laptop;
import clases.Producto;
import clases.Smartphone;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExportadorTxt {

    public static void exportarListaFiltrada(Iterable<? extends Producto> productos, Path ruta, String encabezado)
            throws IOException {

        try (BufferedWriter bw = Files.newBufferedWriter(ruta)) {
            bw.write(encabezado);
            bw.newLine();
            bw.newLine();

            bw.write(String.format(
                    "%-4s %-15s %-10s %-7s %-10s %-18s %-6s %-10s %-12s %-15s %-10s",
                    "ID",
                    "Nombre",
                    "Precio",
                    "Stock",
                    "Categoría",
                    "Estado",
                    "RAM",
                    "Alm(GB)",
                    "SO",
                    "TipoAccesorio",
                    "Color"
            ));
            bw.newLine();
            bw.write("=".repeat(125));
            bw.newLine();

            for (Producto p : productos) {

                String ram = "-";
                String alm = "-";
                String so = "-";
                String tipoAcc = "-";
                String color = "-";

                if (p instanceof Laptop) {
                    Laptop lap = (Laptop) p;
                    ram = String.valueOf(lap.getRamGb());
                    alm = String.valueOf(lap.getAlmacenamientoGb());

                } else if (p instanceof Smartphone) {
                    Smartphone sp = (Smartphone) p;
                    so = sp.getSistemaOperativo();
                    alm = String.valueOf(sp.getAlmacenamientoGb());

                } else if (p instanceof Accesorio) {
                    Accesorio ac = (Accesorio) p;
                    tipoAcc = ac.getTipo();
                    color = ac.getColor();
                }

                bw.write(String.format(
                        "%-4d %-15s %-10.2f %-7d %-10s %-18s %-6s %-10s %-12s %-15s %-10s",
                        p.getId(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getCategoria().name(),
                        p.getEstado().name(),
                        ram,
                        alm,
                        so,
                        tipoAcc,
                        color
                ));
                bw.newLine();
            }
        }
    }
}

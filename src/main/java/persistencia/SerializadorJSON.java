package persistencia;

import clases.EstadoProducto;
import clases.Smartphone;
import clases.Laptop;
import clases.CategoriaProducto;
import clases.Accesorio;
import clases.Producto;
import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SerializadorJSON {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void guardar(List<? extends Producto> lista, Path ruta) throws IOException {
        try (Writer writer = Files.newBufferedWriter(ruta)) {
            gson.toJson(lista, writer);
        }
    }

   public List<Producto> cargar(Path ruta) throws IOException {
    List<Producto> resultado = new ArrayList<>();

    if (!Files.exists(ruta)) {
        return resultado;
    }

    try (Reader reader = Files.newBufferedReader(ruta)) {
        JsonElement root = JsonParser.parseReader(reader);

        JsonArray array = root.getAsJsonArray();
        for (JsonElement elem : array) {
            if (!elem.isJsonObject()) continue;

            JsonObject obj = elem.getAsJsonObject();

            int id = obj.get("id").getAsInt();
            String nombre = obj.get("nombre").getAsString();
            double precio = obj.get("precio").getAsDouble();
            int stock = obj.get("stock").getAsInt();

            CategoriaProducto categoria = CategoriaProducto.valueOf(obj.get("categoria").getAsString());
            EstadoProducto estado = EstadoProducto.valueOf(obj.get("estado").getAsString());

            Producto p;

            switch (categoria) {
                case LAPTOP: {
                    int ram = obj.has("ramGb") ? obj.get("ramGb").getAsInt() : 8;
                    int alm = obj.has("almacenamientoGb") ? obj.get("almacenamientoGb").getAsInt() : 256;
                    p = new Laptop(id, nombre, precio, stock, categoria, estado, ram, alm);
                    break;
                }

                case SMARTPHONE: {
                    String so = obj.has("sistemaOperativo") ? obj.get("sistemaOperativo").getAsString() : "Android";
                    int alm = obj.has("almacenamientoGb") ? obj.get("almacenamientoGb").getAsInt() : 64;
                    p = new Smartphone(id, nombre, precio, stock, categoria, estado, so, alm);
                    break;
                }

                case ACCESORIO: {
                    String tipo = obj.has("tipo") ? obj.get("tipo").getAsString() : "Genérico";
                    String color = obj.has("color") ? obj.get("color").getAsString() : "Negro";
                    p = new Accesorio(id, nombre, precio, stock, categoria, estado, tipo, color);
                    break;
                }

                default: {
                    p = new Accesorio(id, nombre, precio, stock, categoria, estado, "Genérico", "Negro");
                    break;
                }
            }

            resultado.add(p);
        }
    }

    return resultado;
}

}

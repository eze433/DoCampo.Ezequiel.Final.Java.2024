package comparadores;

import clases.Producto;
import java.util.Comparator;

public class ComparadorStockDesc implements Comparator<Producto> {
    @Override
    public int compare(Producto o1, Producto o2) {
        return Integer.compare(o2.getStock(), o1.getStock());
    }
}
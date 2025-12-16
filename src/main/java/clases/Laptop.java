package clases;

public class Laptop extends Producto implements Enviable {
    private int ramGb;
    private int almacenamientoGb;

    // constructor completo
    public Laptop(int id, String nombre, double precio, int stock, CategoriaProducto categoria, EstadoProducto estado, int ramGb, int almacenamientoGb) {
        super(id, nombre, precio, stock, categoria, estado);
        this.ramGb = ramGb;
        this.almacenamientoGb = almacenamientoGb;
    }

    // constructor sin estado
    public Laptop(int id, String nombre, double precio, int stock, CategoriaProducto categoria, int ramGb, int almacenamientoGb) {
        super(id, nombre, precio, stock, categoria);
        this.ramGb = ramGb;
        this.almacenamientoGb = almacenamientoGb;
    }
    
    // constructor sin caracteristicas especificas
    public Laptop(int id, String nombre, double precio, int stock, CategoriaProducto categoria, EstadoProducto estado) {
        super(id, nombre, precio, stock, categoria, estado);
        this.ramGb = 8;
        this.almacenamientoGb = 256;
    }

    public int getRamGb() {
        return ramGb;
    }
    public int getAlmacenamientoGb() {
        return almacenamientoGb;
    }

    public void setRamGb(int ramGb) {
        this.ramGb = ramGb;
    }
    
    public void setAlmacenamientoGb(int almacenamientoGb) {
        this.almacenamientoGb = almacenamientoGb;
    }

    @Override
    public double calcularCostoEnvio() {
        return 3000.0;
    }
}
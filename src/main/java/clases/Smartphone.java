package clases;

public class Smartphone extends Producto implements Enviable {
    private String sistemaOperativo;
    private int almacenamientoGb;

    // constructor completo
    public Smartphone(int id, String nombre, double precio, int stock, CategoriaProducto categoria, EstadoProducto estado, String sistemaOperativo, int almacenamientoGb) {
        super(id, nombre, precio, stock, categoria, estado);
        this.sistemaOperativo = sistemaOperativo;
        this.almacenamientoGb = almacenamientoGb;
    }

    // construcot sin estado
    public Smartphone(int id, String nombre, double precio, int stock, CategoriaProducto categoria, String sistemaOperativo, int almacenamientoGb) {
        super(id, nombre, precio, stock, categoria);
        this.sistemaOperativo = sistemaOperativo;
        this.almacenamientoGb = almacenamientoGb;
    }
    
    // constructor sin caracteristicas especificas
    public Smartphone(int id, String nombre, double precio, int stock, CategoriaProducto categoria, EstadoProducto estado) {
        super(id, nombre, precio, stock, categoria, estado);
        this.sistemaOperativo = "Android";
        this.almacenamientoGb = 64;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }
    
    public int getAlmacenamientoGb() {
        return almacenamientoGb;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }
    
    public void setAlmacenamientoGb(int almacenamientoGb) {
        this.almacenamientoGb = almacenamientoGb;
    }

    @Override
    public double calcularCostoEnvio() {
        return 1500.0;
    }
}
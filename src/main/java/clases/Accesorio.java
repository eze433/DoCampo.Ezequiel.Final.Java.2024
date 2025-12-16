package clases;

public class Accesorio extends Producto {
    private String tipo;
    private String color;
    
    // constructor completo
    public Accesorio(int id, String nombre, double precio, int stock, CategoriaProducto categoria, EstadoProducto estado, String tipo, String color) {
        super(id, nombre, precio, stock, categoria, estado);
        this.tipo = tipo;
        this.color = color;
    }

    // constructor sin estado
    public Accesorio(int id, String nombre, double precio, int stock, CategoriaProducto categoria, String tipo, String color) {
        super(id, nombre, precio, stock, categoria);
        this.tipo = tipo;
        this.color = color;
    }
    
    // constructor sin caracteristicas especificas
    public Accesorio(int id, String nombre, double precio, int stock, CategoriaProducto categoria, EstadoProducto estado) {
        super(id, nombre, precio, stock, categoria, estado);
        this.tipo = "Generico";
        this.color = "Negro";
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public String getColor() {
        return color;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
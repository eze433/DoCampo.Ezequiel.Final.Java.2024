package clases;

import java.util.Objects;

public abstract class Producto implements Comparable<Producto> {
    protected int id;
    protected String nombre;
    protected double precio;
    protected int stock;
    protected CategoriaProducto categoria;
    protected EstadoProducto estado;

    // constructor producto
    public Producto(int id, String nombre, double precio, int stock, CategoriaProducto categoria, EstadoProducto estado) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.estado = estado;
    }
    
    // constructor sin estado
    public Producto(int id, String nombre, double precio, int stock, CategoriaProducto categoria) {
        this(id, nombre, precio, stock, categoria, EstadoProducto.NUEVO);
    }

    // solo nombre
    public Producto(int id, String nombre) {
        this(id, nombre, 0.0, 0, CategoriaProducto.ACCESORIO, EstadoProducto.NUEVO);
    }

    // getters y setters
    public int getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public int getStock() {
        return stock;
    }
    
    public CategoriaProducto getCategoria() {
        return categoria;
    }
    
    public EstadoProducto getEstado() {
        return estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }
    
    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }

    public void aplicarDescuento(double porcentaje) {
        if (porcentaje > 0 && porcentaje < 100) {
            this.precio = this.precio * (1 - porcentaje / 100.0);
        }
    }

    // criterio natural por nombre
    @Override
    public int compareTo(Producto otro) {
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", categoria=" + categoria +
                ", estado=" + estado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return id == producto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
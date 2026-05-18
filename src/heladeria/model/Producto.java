package heladeria.model;

/**
 * Representa un producto (helado, insumo, etc.) en el inventario.
 */
public class Producto {
    // Atributo 'final' para garantizar que la clave sea inmutable tras el registro
    private final String clave; 
    private String nombre;
    private int existencia;
    private String ubicacion; // Estante o refrigerador
    private double precio;
    private byte[] foto; // La foto se guarda en bytes para la base de datos

    public Producto(String clave, String nombre, int existencia, String ubicacion, double precio, byte[] foto) {
        this.clave = clave;
        this.nombre = nombre;
        this.existencia = existencia;
        this.ubicacion = ubicacion;
        this.precio = precio;
        this.foto = foto;
    }

    // Getters para todos los atributos

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getClave() {
        return clave;
    }
    
}
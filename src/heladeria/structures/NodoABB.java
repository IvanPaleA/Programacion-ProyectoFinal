package heladeria.structures;

import heladeria.model.Producto;

/**
 * Nodo para el Árbol Binario de Búsqueda (ABB) de productos.
 */
public class NodoABB {
    private Producto producto;
    private NodoABB izquierdo;
    private NodoABB derecho;

    /**
     * Constructor del NodoABB.
     * 
     * @param producto El producto que almacenará este nodo
     */
    public NodoABB(Producto producto) {
        this.producto = producto;
        this.izquierdo = null;
        this.derecho = null;
    }


    /**
     * Obtiene el producto almacenado en el nodo.
     * 
     * @return El producto del nodo
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece o modifica el producto del nodo.
     * 
     * @param producto El nuevo producto a asignar
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene el hijo izquierdo del nodo.
     * 
     * @return El nodo izquierdo
     */
    public NodoABB getIzquierdo() {
        return izquierdo;
    }

    /**
     * Establece el hijo izquierdo del nodo.
     * 
     * @param izquierdo El nodo que será el hijo izquierdo
     */
    public void setIzquierdo(NodoABB izquierdo) {
        this.izquierdo = izquierdo;
    }

    /**
     * Obtiene el hijo derecho del nodo.
     * 
     * @return El nodo derecho
     */
    public NodoABB getDerecho() {
        return derecho;
    }

    /**
     * Establece el hijo derecho del nodo.
     * 
     * @param derecho El nodo que será el hijo derecho
     */
    public void setDerecho(NodoABB derecho) {
        this.derecho = derecho;
    }
    
    
    
}
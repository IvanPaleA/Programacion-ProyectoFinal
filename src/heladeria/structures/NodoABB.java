package heladeria.structures;

import heladeria.model.Producto;

/**
 * Nodo para el Árbol Binario de Búsqueda (ABB) de productos.
 */
public class NodoABB {
    private Producto producto;
    private NodoABB izquierdo;
    private NodoABB derecho;

    public NodoABB(Producto producto) {
        this.producto = producto;
        this.izquierdo = null;
        this.derecho = null;
    }


    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public NodoABB getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoABB izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoABB getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoABB derecho) {
        this.derecho = derecho;
    }
    
    
    
}
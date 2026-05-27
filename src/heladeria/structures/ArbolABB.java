package heladeria.structures;

import heladeria.model.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * Controla la estructura y operaciones del Árbol Binario de Búsqueda.
 */
public class ArbolABB {
    private NodoABB raiz;

    public ArbolABB() {
        this.raiz = null;
    }

    public void insertar(Producto producto) {
        raiz = insertarRec(raiz, producto);
    }

    private NodoABB insertarRec(NodoABB actual, Producto producto) {
        if (actual == null) {
            return new NodoABB(producto);
        }

        long claveActual = Long.parseLong(actual.getProducto().getClave());
        long claveNueva = Long.parseLong(producto.getClave());

        if (claveNueva < claveActual) {
            actual.setIzquierdo(insertarRec(actual.getIzquierdo(), producto));
        } else if (claveNueva > claveActual) {
            actual.setDerecho(insertarRec(actual.getDerecho(), producto));
        }
        return actual;
    }

    public Producto buscarPorClave(String clave) {
        return buscarPorClaveRec(raiz, Long.parseLong(clave));
    }

    private Producto buscarPorClaveRec(NodoABB actual, long clave) {
        if (actual == null) return null;

        long claveActual = Long.parseLong(actual.getProducto().getClave());

        if (clave == claveActual) return actual.getProducto();
        
        return clave < claveActual 
            ? buscarPorClaveRec(actual.getIzquierdo(), clave) 
            : buscarPorClaveRec(actual.getDerecho(), clave);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> resultados = new ArrayList<>();
        buscarPorNombreRec(raiz, nombre.toLowerCase(), resultados);
        return resultados;
    }

    private void buscarPorNombreRec(NodoABB actual, String nombre, List<Producto> resultados) {
        if (actual == null) return;

        buscarPorNombreRec(actual.getIzquierdo(), nombre, resultados);
        if (actual.getProducto().getNombre().toLowerCase().contains(nombre)) {
            resultados.add(actual.getProducto());
        }
        buscarPorNombreRec(actual.getDerecho(), nombre, resultados);
    }

    public List<Producto> getListaInorden() {
        List<Producto> lista = new ArrayList<>();
        inordenRec(raiz, lista);
        return lista;
    }

    private void inordenRec(NodoABB actual, List<Producto> lista) {
        if (actual != null) {
            inordenRec(actual.getIzquierdo(), lista);
            lista.add(actual.getProducto()); 
            inordenRec(actual.getDerecho(), lista);
        }
    }

    public void limpiar() {
        this.raiz = null;
    }
}
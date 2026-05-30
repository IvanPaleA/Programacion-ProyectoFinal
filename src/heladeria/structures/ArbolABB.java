package heladeria.structures;

import heladeria.model.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * Controla la estructura y operaciones del Árbol Binario de Búsqueda.
 */
public class ArbolABB {
    private NodoABB raiz;

    /**
     * Constructor de la clase ArbolABB.
     * Inicializa la raíz del árbol como nula.
     */
    public ArbolABB() {
        this.raiz = null;
    }

    /**
     * Inserta un nuevo producto en el árbol binario.
     * 
     * @param producto El producto que se desea insertar
     */
    public void insertar(Producto producto) {
        raiz = insertarRec(raiz, producto);
    }

    /**
     * Método recursivo auxiliar para insertar un nodo en el árbol.
     * 
     * @param actual El nodo actual en el recorrido
     * @param producto El producto a insertar
     * @return El nodo actualizado
     */
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

    /**
     * Busca un producto utilizando su clave.
     * 
     * @param clave La clave del producto a buscar
     * @return El producto encontrado, o null si no existe
     */
    public Producto buscarPorClave(String clave) {
        return buscarPorClaveRec(raiz, Long.parseLong(clave));
    }

    /**
     * Método recursivo para buscar un producto por su clave.
     * 
     * @param actual El nodo actual en la búsqueda
     * @param clave La clave numérica a comparar
     * @return El producto encontrado, o null
     */
    private Producto buscarPorClaveRec(NodoABB actual, long clave) {
        if (actual == null) return null;

        long claveActual = Long.parseLong(actual.getProducto().getClave());

        if (clave == claveActual) return actual.getProducto();
        
        return clave < claveActual 
            ? buscarPorClaveRec(actual.getIzquierdo(), clave) 
            : buscarPorClaveRec(actual.getDerecho(), clave);
    }

    /**
     * Busca productos que coincidan o contengan el nombre indicado.
     * 
     * @param nombre El nombre o texto a buscar
     * @return Una lista con los productos encontrados
     */
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> resultados = new ArrayList<>();
        buscarPorNombreRec(raiz, nombre.toLowerCase(), resultados);
        return resultados;
    }

    /**
     * Método recursivo auxiliar para la búsqueda por nombre.
     * 
     * @param actual El nodo actual en el recorrido
     * @param nombre El texto a buscar en los nombres
     * @param resultados La lista donde se guardan las coincidencias
     */
    private void buscarPorNombreRec(NodoABB actual, String nombre, List<Producto> resultados) {
        if (actual == null) return;

        buscarPorNombreRec(actual.getIzquierdo(), nombre, resultados);
        if (actual.getProducto().getNombre().toLowerCase().contains(nombre)) {
            resultados.add(actual.getProducto());
        }
        buscarPorNombreRec(actual.getDerecho(), nombre, resultados);
    }

    /**
     * Obtiene todos los productos del árbol ordenados (recorrido inorden).
     * 
     * @return Lista de productos ordenados por clave
     */
    public List<Producto> getListaInorden() {
        List<Producto> lista = new ArrayList<>();
        inordenRec(raiz, lista);
        return lista;
    }

    /**
     * Método recursivo para recorrer el árbol en inorden y llenar la lista.
     * 
     * @param actual El nodo actual
     * @param lista La lista donde se van agregando los productos
     */
    private void inordenRec(NodoABB actual, List<Producto> lista) {
        if (actual != null) {
            inordenRec(actual.getIzquierdo(), lista);
            lista.add(actual.getProducto()); 
            inordenRec(actual.getDerecho(), lista);
        }
    }

    /**
     * Limpia o vacía el árbol por completo.
     */
    public void limpiar() {
        this.raiz = null;
    }
}
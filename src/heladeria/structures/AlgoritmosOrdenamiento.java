package heladeria.structures;

import heladeria.model.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria que implementa los algoritmos de ordenamiento usando Listas.
 */
public class AlgoritmosOrdenamiento {

    //QUICKSORT: Ordenar por Nombre (Alfabético)
    /**
     * Ordena una lista de productos por su nombre alfabéticamente usando QuickSort.
     * 
     * @param lista La lista de productos a ordenar
     */
    public static void ordenarPorNombre(List<Producto> lista) {
        if (lista == null || lista.size() <= 1) return;
        quickSort(lista, 0, lista.size() - 1);
    }

    /**
     * Método recursivo para el algoritmo QuickSort.
     * 
     * @param lista La lista a ordenar
     * @param bajo El índice inferior
     * @param alto El índice superior
     */
    private static void quickSort(List<Producto> lista, int bajo, int alto) {
        if (bajo < alto) {
            int indicePivote = particion(lista, bajo, alto);
            quickSort(lista, bajo, indicePivote - 1);  
            quickSort(lista, indicePivote + 1, alto); 
        }
    }

    /**
     * Método auxiliar de QuickSort para encontrar el pivote y acomodar elementos.
     * 
     * @param lista La lista de productos
     * @param bajo El índice inferior
     * @param alto El índice superior
     * @return El índice del pivote
     */
    private static int particion(List<Producto> lista, int bajo, int alto) {
        //nombre del último producto como pivote
        String pivote = lista.get(alto).getNombre();
        int i = (bajo - 1);

        for (int j = bajo; j < alto; j++) {
            if (lista.get(j).getNombre().compareToIgnoreCase(pivote) <= 0) {
                i++;
                Producto temp = lista.get(i);
                lista.set(i, lista.get(j));
                lista.set(j, temp);
            }
        }

        //cambiar el pivote
        Producto temp = lista.get(i + 1);
        lista.set(i + 1, lista.get(alto));
        lista.set(alto, temp);

        return i + 1;
    }

    //MERGESORT: Ordenar por Precio (Numérico)
    /**
     * Ordena una lista de productos por su precio de menor a mayor usando MergeSort.
     * 
     * @param lista La lista de productos a ordenar
     */
    public static void ordenarPorPrecio(List<Producto> lista) {
        if (lista == null || lista.size() <= 1) return;
        
        List<Producto> listaOrdenada = mergeSort(lista);
        lista.clear();
        lista.addAll(listaOrdenada);
    }

    /**
     * Método recursivo para el algoritmo MergeSort que divide la lista.
     * 
     * @param lista La lista a dividir y ordenar
     * @return La lista ordenada
     */
    private static List<Producto> mergeSort(List<Producto> lista) {
        if (lista.size() <= 1) return lista;

        int medio = lista.size() / 2;
        List<Producto> izquierda = new ArrayList<>(lista.subList(0, medio));
        List<Producto> derecha = new ArrayList<>(lista.subList(medio, lista.size()));

        izquierda = mergeSort(izquierda);
        derecha = mergeSort(derecha);

        return mezclar(izquierda, derecha);
    }

    /**
     * Método auxiliar de MergeSort que une dos listas ordenadas en una sola.
     * 
     * @param izquierda La mitad izquierda de la lista
     * @param derecha La mitad derecha de la lista
     * @return La lista combinada y ordenada
     */
    private static List<Producto> mezclar(List<Producto> izquierda, List<Producto> derecha) {
        List<Producto> resultado = new ArrayList<>();
        int i = 0, d = 0;

        while (i < izquierda.size() && d < derecha.size()) {
            if (izquierda.get(i).getPrecio() <= derecha.get(d).getPrecio()) {
                resultado.add(izquierda.get(i));
                i++;
            } else {
                resultado.add(derecha.get(d));
                d++;
            }
        }

        while (i < izquierda.size()) {
            resultado.add(izquierda.get(i));
            i++;
        }
        while (d < derecha.size()) {
            resultado.add(derecha.get(d));
            d++;
        }

        return resultado;
    }
}
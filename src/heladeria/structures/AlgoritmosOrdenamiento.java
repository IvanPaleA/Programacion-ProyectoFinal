package heladeria.structures;

import heladeria.model.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria que implementa los algoritmos de ordenamiento usando Listas.
 */
public class AlgoritmosOrdenamiento {

    // ==========================================
    // 1. QUICKSORT: Ordenar por Nombre (Alfabético)
    // ==========================================
    public static void ordenarPorNombre(List<Producto> lista) {
        if (lista == null || lista.size() <= 1) return;
        quickSort(lista, 0, lista.size() - 1);
    }

    private static void quickSort(List<Producto> lista, int bajo, int alto) {
        if (bajo < alto) {
            int indicePivote = particion(lista, bajo, alto);
            quickSort(lista, bajo, indicePivote - 1);  // Lado izquierdo
            quickSort(lista, indicePivote + 1, alto); // Lado derecho
        }
    }

    private static int particion(List<Producto> lista, int bajo, int alto) {
        // Tomamos el nombre del último producto como pivote
        String pivote = lista.get(alto).getNombre();
        int i = (bajo - 1);

        for (int j = bajo; j < alto; j++) {
            // Comparamos alfabéticamente ignorando mayúsculas/minúsculas
            if (lista.get(j).getNombre().compareToIgnoreCase(pivote) <= 0) {
                i++;
                // Intercambiar lista[i] y lista[j]
                Producto temp = lista.get(i);
                lista.set(i, lista.get(j));
                lista.set(j, temp);
            }
        }

        // Intercambiar el pivote con el elemento en i+1
        Producto temp = lista.get(i + 1);
        lista.set(i + 1, lista.get(alto));
        lista.set(alto, temp);

        return i + 1;
    }

    // ==========================================
    // 2. MERGESORT: Ordenar por Precio (Numérico)
    // ==========================================
    public static void ordenarPorPrecio(List<Producto> lista) {
        if (lista == null || lista.size() <= 1) return;
        
        List<Producto> listaOrdenada = mergeSort(lista);
        // Copiamos los elementos de vuelta a la lista original para modificarla directamente
        lista.clear();
        lista.addAll(listaOrdenada);
    }

    private static List<Producto> mergeSort(List<Producto> lista) {
        if (lista.size() <= 1) return lista;

        int medio = lista.size() / 2;
        List<Producto> izquierda = new ArrayList<>(lista.subList(0, medio));
        List<Producto> derecha = new ArrayList<>(lista.subList(medio, lista.size()));

        // Recursión: dividir la lista en mitades
        izquierda = mergeSort(izquierda);
        derecha = mergeSort(derecha);

        // Mezclar las mitades ya ordenadas
        return mezclar(izquierda, derecha);
    }

    private static List<Producto> mezclar(List<Producto> izquierda, List<Producto> derecha) {
        List<Producto> resultado = new ArrayList<>();
        int i = 0, d = 0;

        // Comparar precios de menor a mayor
        while (i < izquierda.size() && d < derecha.size()) {
            if (izquierda.get(i).getPrecio() <= derecha.get(d).getPrecio()) {
                resultado.add(izquierda.get(i));
                i++;
            } else {
                resultado.add(derecha.get(d));
                d++;
            }
        }

        // Agregar los elementos sobrantes si quedan
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
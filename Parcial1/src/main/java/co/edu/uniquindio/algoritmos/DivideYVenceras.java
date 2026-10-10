package co.edu.uniquindio.algoritmos;

import co.edu.uniquindio.modelo.Paquete;

import java.util.List;

/**
 * Algoritmos con la estrategia divide y venceras.
 */
public final class DivideYVenceras {

    /** Constructor privado: esta clase solo tiene metodos static. */
    private DivideYVenceras() {
    }

    /**
     * Encuentra el paquete de mayor peso.
     * Lista nula da error; lista vacia devuelve null.
     */
    public static Paquete paqueteMasPesado(List<Paquete> paquetes) {
        if (paquetes == null) {
            throw new IllegalArgumentException("La lista de paquetes no puede ser nula");
        }
        if (paquetes.isEmpty()) {
            return null;
        }
        return paqueteMasPesado(paquetes, 0, paquetes.size() - 1);
    }

    // Trabaja sobre el pedazo de la lista que va de inicio a fin (ambos incluidos).
    private static Paquete paqueteMasPesado(List<Paquete> paquetes, int inicio, int fin) {
        // CASO BASE: un solo paquete, ese es el mas pesado de su pedazo
        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        // DIVISION: partimos el pedazo por la mitad
        int medio = inicio + (fin - inicio) / 2;

        // SUBPROBLEMA IZQUIERDO: el mas pesado de la primera mitad
        Paquete masPesadoIzquierda = paqueteMasPesado(paquetes, inicio, medio);

        // SUBPROBLEMA DERECHO: el mas pesado de la segunda mitad
        Paquete masPesadoDerecha = paqueteMasPesado(paquetes, medio + 1, fin);

        // COMBINACION: nos quedamos con el mas pesado de los dos (si empatan, el de la izquierda)
        if (masPesadoDerecha.getPeso() > masPesadoIzquierda.getPeso()) {
            return masPesadoDerecha;
        }
        return masPesadoIzquierda;
    }
}
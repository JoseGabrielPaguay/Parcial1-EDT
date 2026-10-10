package co.edu.uniquindio.algoritmos;

import co.edu.uniquindio.modelo.Paquete;

import java.util.List;

/**
 * Busqueda binaria de paquetes por codigo.
 * PRECONDICION: la lista debe venir ordenada por codigo (ascendente).
 * Si la lista no esta ordenada, el resultado no es confiable.
 */
public final class BusquedaBinaria {

    /** Constructor privado: esta clase solo tiene metodos static. */
    private BusquedaBinaria() {
    }

    /**
     * Devuelve la posicion del paquete con ese codigo, o -1 si no esta.
     * Cada vuelta compara con el del medio y descarta la mitad que no sirve.
     */
    public static int buscarPosicion(List<Paquete> ordenadosPorCodigo, String codigo) {
        if (ordenadosPorCodigo == null) {
            throw new IllegalArgumentException("La lista de paquetes no puede ser nula");
        }
        if (codigo == null) {
            throw new IllegalArgumentException("El codigo no puede ser nulo");
        }
        int inicio = 0;
        int fin = ordenadosPorCodigo.size() - 1;
        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            int comparacion = ordenadosPorCodigo.get(medio).getCodigo().compareTo(codigo);
            if (comparacion == 0) {
                return medio;
            } else if (comparacion < 0) {
                inicio = medio + 1; // el del medio es menor: buscamos a la derecha
            } else {
                fin = medio - 1;    // el del medio es mayor: buscamos a la izquierda
            }
        }
        return -1;
    }

    /** Devuelve el paquete con ese codigo, o null si no esta. */
    public static Paquete buscar(List<Paquete> ordenadosPorCodigo, String codigo) {
        int posicion = buscarPosicion(ordenadosPorCodigo, codigo);
        return posicion == -1 ? null : ordenadosPorCodigo.get(posicion);
    }
}

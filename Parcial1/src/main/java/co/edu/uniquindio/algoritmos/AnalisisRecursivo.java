package co.edu.uniquindio.algoritmos;

import co.edu.uniquindio.modelo.Paquete;

import java.util.List;

/**
 * Analisis sobre listas de paquetes resueltos con recursividad.
 * No se usan ciclos (for, while, do-while) en ningun metodo.
 */
public final class AnalisisRecursivo {

    /** Constructor privado: esta clase solo tiene metodos static. */
    private AnalisisRecursivo() {
    }

    /**
     * Suma el peso de los paquetes que van a un municipio.
     * Ejemplo: Armenia con 4.5, 7.2 y 2.3 da 14.0.
     */
    public static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio) {
        if (paquetes == null) {
            throw new IllegalArgumentException("La lista de paquetes no puede ser nula");
        }
        if (municipio == null) {
            throw new IllegalArgumentException("El municipio no puede ser nulo");
        }
        return pesoTotalPorMunicipio(paquetes, municipio, 0);
    }

    // Metodo privado: va mirando la lista posicion por posicion.
    private static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio, int indice) {
        // CASO BASE: ya no quedan paquetes por revisar
        if (indice == paquetes.size()) {
            return 0;
        }
        // CASO RECURSIVO: peso de este paquete (si va a ese municipio) mas el resto
        Paquete actual = paquetes.get(indice);
        double pesoActual = actual.getDestino().equals(municipio) ? actual.getPeso() : 0;
        return pesoActual + pesoTotalPorMunicipio(paquetes, municipio, indice + 1);
    }

    /**
     * Cuenta cuantos paquetes tienen prioridad igual o mayor a la minima.
     * Ejemplo: prioridades 5,2,4,1,5,3,4 con minima 4 da 4.
     */
    public static int contarConPrioridadMinima(List<Paquete> paquetes, int prioridadMinima) {
        if (paquetes == null) {
            throw new IllegalArgumentException("La lista de paquetes no puede ser nula");
        }
        return contarConPrioridadMinima(paquetes, prioridadMinima, 0);
    }

    // Metodo privado: va mirando la lista posicion por posicion.
    private static int contarConPrioridadMinima(List<Paquete> paquetes, int prioridadMinima, int indice) {
        // CASO BASE: ya no quedan paquetes por revisar
        if (indice == paquetes.size()) {
            return 0;
        }
        // CASO RECURSIVO: 1 si este paquete cumple (0 si no) mas el conteo del resto
        int cuenta = paquetes.get(indice).getPrioridad() >= prioridadMinima ? 1 : 0;
        return cuenta + contarConPrioridadMinima(paquetes, prioridadMinima, indice + 1);
    }
}

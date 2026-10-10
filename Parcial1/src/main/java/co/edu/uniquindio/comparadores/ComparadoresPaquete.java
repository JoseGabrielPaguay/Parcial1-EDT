package co.edu.uniquindio.comparadores;


import co.edu.uniquindio.modelo.Paquete;

import java.util.Comparator;

/**
 * Criterios para ordenar paquetes. Son independientes del orden natural
 * del Paquete (que sigue siendo por codigo, definido con Comparable).
 */
public final class ComparadoresPaquete {

    /** Constructor privado: esta clase solo tiene constantes, no se crean objetos. */
    private ComparadoresPaquete() {
    }

    /** Mayor prioridad primero. */
    public static final Comparator<Paquete> POR_PRIORIDAD_DESC =
            (a, b) -> Integer.compare(b.getPrioridad(), a.getPrioridad());

    /** Mayor peso primero. Se usa Double.compare porque restar doubles es peligroso. */
    public static final Comparator<Paquete> POR_PESO_DESC =
            (a, b) -> Double.compare(b.getPeso(), a.getPeso());

    /** Menor tiempo estimado primero. */
    public static final Comparator<Paquete> POR_TIEMPO_ASC =
            (a, b) -> Integer.compare(a.getTiempoEstimado(), b.getTiempoEstimado());

    /**
     * Orden de despacho: primero mayor prioridad, si empatan menor tiempo
     * estimado, y si siguen empatados el codigo (orden natural del Paquete).
     */
    public static final Comparator<Paquete> DESPACHO =
            POR_PRIORIDAD_DESC
                    .thenComparing(POR_TIEMPO_ASC)
                    .thenComparing(Comparator.naturalOrder());
}


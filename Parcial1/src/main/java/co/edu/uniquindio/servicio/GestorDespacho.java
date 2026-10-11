package co.edu.uniquindio.servicio;

import co.edu.uniquindio.comparadores.ComparadoresPaquete;
import co.edu.uniquindio.modelo.Paquete;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Maneja los paquetes que estan pendientes por salir.
 *
 * Tiene dos colas con los MISMOS paquetes, vistos de dos formas:
 * - colaLlegada: en el orden en que llegaron (la primera que llega es la primera que sale).
 * - colaPrioridad: ordenada con el comparador DESPACHO (mayor prioridad, luego menor
 *   tiempo y luego el codigo).
 *
 * Cada vez que un paquete sale de una cola, tambien se quita de la otra,
 * para que las dos siempre tengan los mismos paquetes.
 */
public class GestorDespacho {

    private final Queue<Paquete> colaLlegada = new ArrayDeque<>();
    private final PriorityQueue<Paquete> colaPrioridad =
            new PriorityQueue<>(ComparadoresPaquete.DESPACHO);

    /**
     * Agrega un paquete pendiente a las dos colas.
     * No acepta nulos ni paquetes que ya esten pendientes.
     */
    public void agregar(Paquete paquete) {
        if (paquete == null) {
            throw new IllegalArgumentException("No se puede agregar un paquete nulo");
        }
        if (contiene(paquete)) {
            throw new IllegalStateException("El paquete " + paquete.getCodigo()
                    + " ya esta pendiente");
        }
        colaLlegada.add(paquete);
        colaPrioridad.add(paquete);
    }

    /** Dice si el paquete esta pendiente (se compara por codigo). */
    public boolean contiene(Paquete paquete) {
        return colaLlegada.contains(paquete);
    }

    /** Muestra el siguiente por orden de llegada sin sacarlo. Si no hay, devuelve null. */
    public Paquete verSiguientePorLlegada() {
        return colaLlegada.peek();
    }

    /**
     * Saca el siguiente por orden de llegada y tambien lo quita de la cola de prioridad.
     * Si no hay pendientes, devuelve null.
     */
    public Paquete atenderSiguientePorLlegada() {
        Paquete paquete = colaLlegada.poll();
        if (paquete != null) {
            colaPrioridad.remove(paquete);
        }
        return paquete;
    }

    /** Muestra el siguiente por prioridad sin sacarlo. Si no hay, devuelve null. */
    public Paquete verSiguientePorPrioridad() {
        return colaPrioridad.peek();
    }

    /**
     * Saca el siguiente por prioridad y tambien lo quita de la cola de llegada.
     * Si no hay pendientes, devuelve null.
     */
    public Paquete despacharSiguientePorPrioridad() {
        Paquete paquete = colaPrioridad.poll();
        if (paquete != null) {
            colaLlegada.remove(paquete);
        }
        return paquete;
    }

    /**
     * Quita un paquete de las dos colas. Devuelve true si estaba.
     * remove() tiene que buscarlo dentro de cada cola, por eso cuesta O(n).
     */
    public boolean retirar(Paquete paquete) {
        boolean estaba = colaLlegada.remove(paquete);
        if (estaba) {
            colaPrioridad.remove(paquete);
        }
        return estaba;
    }

    /** Devuelve cuantos paquetes hay pendientes. */
    public int cantidadPendientes() {
        return colaLlegada.size();
    }

    /** Devuelve true si no hay paquetes pendientes. */
    public boolean estaVacio() {
        return colaLlegada.isEmpty();
    }

    /** Devuelve una copia de los pendientes en orden de llegada. */
    public List<Paquete> pendientesPorLlegada() {
        return new ArrayList<>(colaLlegada);
    }

    /**
     * Devuelve una copia de los pendientes en orden de despacho.
     * Ojo: recorrer la PriorityQueue no da los paquetes en orden, por eso
     * se copia a una lista y se ordena con el mismo comparador.
     */
    public List<Paquete> pendientesPorPrioridad() {
        List<Paquete> copia = new ArrayList<>(colaPrioridad);
        copia.sort(ComparadoresPaquete.DESPACHO);
        return copia;
    }
}
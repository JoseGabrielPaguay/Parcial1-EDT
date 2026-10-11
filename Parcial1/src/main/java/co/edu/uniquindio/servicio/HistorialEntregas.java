package co.edu.uniquindio.servicio;

import co.edu.uniquindio.estructuras.ColeccionGenerica;
import co.edu.uniquindio.estructuras.ListaSimplementeEnlazada;
import co.edu.uniquindio.modelo.Paquete;
import java.util.Iterator;

/**
 * Historial de los paquetes que ya se entregaron.
 * Guarda los paquetes en el mismo orden en que se fueron entregando,
 * usando nuestra lista enlazada propia.
 *
 * El atributo es de tipo ColeccionGenerica (la interfaz) y no de la clase
 * ListaSimplementeEnlazada. Asi el historial solo depende del contrato.
 */
public class HistorialEntregas implements Iterable<Paquete> {

    private final ColeccionGenerica<Paquete> entregas = new ListaSimplementeEnlazada<>();

    /**
     * Registra un paquete entregado al final del historial.
     * No acepta nulos ni paquetes que ya esten en el historial.
     */
    public void registrar(Paquete paquete) {
        if (paquete == null) {
            throw new IllegalArgumentException("No se puede registrar un paquete nulo");
        }
        if (contiene(paquete.getCodigo())) {
            throw new IllegalStateException("El paquete " + paquete.getCodigo()
                    + " ya esta en el historial");
        }
        entregas.agregar(paquete);
    }

    /**
     * Dice si ya hay un paquete con ese codigo en el historial.
     * Recorre toda la lista comparando codigos, por eso es O(n).
     */
    public boolean contiene(String codigo) {
        for (Paquete paquete : entregas) {
            if (paquete.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    /** Devuelve el paquete que esta en esa posicion (desde 0). */
    public Paquete obtener(int posicion) {
        return entregas.obtener(posicion);
    }

    /** Devuelve cuantos paquetes se han entregado. */
    public int cantidad() {
        return entregas.tamanio();
    }

    /** Devuelve true si todavia no hay entregas. */
    public boolean estaVacio() {
        return entregas.estaVacia();
    }

    /**
     * Devuelve el iterador de la lista, para poder usar el for-each.
     * El codigo de afuera nunca ve los nodos.
     */
    @Override
    public Iterator<Paquete> iterator() {
        return entregas.iterator();
    }

    /** Muestra los codigos en orden: PQ105 -> PQ942 -> PQ731, o (sin entregas). */
    @Override
    public String toString() {
        if (estaVacio()) {
            return "(sin entregas)";
        }
        StringBuilder texto = new StringBuilder();
        for (Paquete paquete : entregas) {
            if (texto.length() > 0) {
                texto.append(" -> ");
            }
            texto.append(paquete.getCodigo());
        }
        return texto.toString();
    }
}
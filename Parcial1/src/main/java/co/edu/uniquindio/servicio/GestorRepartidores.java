package co.edu.uniquindio.servicio;

import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.modelo.Repartidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Maneja los repartidores: los registra, los consulta y les asigna paquetes.
 * Un repartidor queda ocupado desde que se le asigna un paquete hasta que se entrega.
 */
public class GestorRepartidores {

    // Repartidores por identificacion, en el orden en que se registraron
    private final Map<String, Repartidor> repartidoresPorId = new LinkedHashMap<>();
    // Codigo del paquete -> repartidor que lo lleva
    private final Map<String, Repartidor> asignaciones = new HashMap<>();

    /** Registra un repartidor. Nulo da IllegalArgumentException; identificacion repetida da IllegalStateException. */
    public void registrar(Repartidor r) {
        if (r == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo");
        }
        if (repartidoresPorId.containsKey(r.getIdentificacion())) {
            throw new IllegalStateException("Ya existe un repartidor con la identificacion "
                    + r.getIdentificacion());
        }
        repartidoresPorId.put(r.getIdentificacion(), r);
    }

    /** Dice si existe un repartidor con esa identificacion. */
    public boolean existe(String identificacion) {
        return identificacion != null && repartidoresPorId.containsKey(identificacion.trim());
    }

    /** Devuelve el repartidor con esa identificacion, o null si no existe. */
    public Repartidor buscar(String identificacion) {
        if (identificacion == null) {
            return null;
        }
        return repartidoresPorId.get(identificacion.trim());
    }

    /** Devuelve una copia de la lista de repartidores, en orden de registro. */
    public List<Repartidor> listar() {
        return new ArrayList<>(repartidoresPorId.values());
    }

    /** Devuelve el primer repartidor disponible de esa zona (en orden de registro), o null si no hay. */
    public Repartidor buscarDisponiblePorZona(String zona) {
        if (zona == null) {
            return null;
        }
        String zonaBuscada = zona.trim();
        for (Repartidor r : repartidoresPorId.values()) {
            if (r.isDisponible() && r.getZona().equals(zonaBuscada)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Asigna el paquete al repartidor. Valida en este orden:
     * paquete nulo, repartidor inexistente, paquete ya asignado, repartidor no disponible, zona distinta.
     */
    public void asignar(Paquete p, String identificacion) {
        if (p == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }
        Repartidor r = buscar(identificacion);
        if (r == null) {
            throw new IllegalArgumentException("No existe el repartidor " + identificacion);
        }
        if (asignaciones.containsKey(p.getCodigo())) {
            throw new IllegalStateException("El paquete " + p.getCodigo() + " ya tiene repartidor");
        }
        if (!r.isDisponible()) {
            throw new IllegalStateException("El repartidor " + r.getIdentificacion() + " no esta disponible");
        }
        if (!r.getZona().equals(p.getDestino())) {
            throw new IllegalStateException("La zona del repartidor (" + r.getZona()
                    + ") es distinta del destino del paquete (" + p.getDestino() + ")");
        }
        r.marcarOcupado();
        asignaciones.put(p.getCodigo(), r);
    }

    /** Busca un repartidor disponible en la zona del destino y le asigna el paquete. Sin repartidor da IllegalStateException. */
    public Repartidor asignarAutomaticamente(Paquete p) {
        if (p == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }
        Repartidor r = buscarDisponiblePorZona(p.getDestino());
        if (r == null) {
            throw new IllegalStateException("No hay repartidor disponible en " + p.getDestino());
        }
        asignar(p, r.getIdentificacion());
        return r;
    }

    /** Devuelve el repartidor que lleva ese paquete, o null si no tiene. */
    public Repartidor obtenerAsignado(String codigoPaquete) {
        if (codigoPaquete == null) {
            return null;
        }
        return asignaciones.get(codigoPaquete.trim());
    }

    /** Quita la asignacion, deja al repartidor disponible y lo devuelve. Si no estaba asignado da IllegalStateException. */
    public Repartidor liberar(String codigoPaquete) {
        Repartidor r = obtenerAsignado(codigoPaquete);
        if (r == null) {
            throw new IllegalStateException("El paquete " + codigoPaquete + " no tiene repartidor asignado");
        }
        asignaciones.remove(codigoPaquete.trim());
        r.marcarDisponible();
        return r;
    }
}
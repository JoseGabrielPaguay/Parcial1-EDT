package co.edu.uniquindio.servicio;

import co.edu.uniquindio.algoritmos.AnalisisRecursivo;
import co.edu.uniquindio.algoritmos.BusquedaBinaria;
import co.edu.uniquindio.algoritmos.DivideYVenceras;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.modelo.Repartidor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Une todas las partes del sistema. No repite la logica de las otras clases:
 * solo las llama en el orden correcto.
 */
public class CentroDistribucion {

    private final RegistroPaquetes registro = new RegistroPaquetes();
    private final GestorDespacho despacho = new GestorDespacho();
    private final GestorRepartidores repartidores = new GestorRepartidores();
    private final HistorialEntregas historial = new HistorialEntregas();

    // ---------------- Registro y consultas ----------------

    /** Registra el paquete y lo deja pendiente de despacho. Si el codigo esta repetido, no entra a ninguna parte. */
    public void registrarPaquete(Paquete p) {
        registro.registrar(p);
        despacho.agregar(p);
    }

    /** Registra un repartidor. */
    public void registrarRepartidor(Repartidor r) {
        repartidores.registrar(r);
    }

    /** Dice si existe un paquete con ese codigo. */
    public boolean existePaquete(String codigo) {
        return registro.existe(codigo);
    }

    /** Busca un paquete por codigo; null si no existe. */
    public Paquete buscarPaquete(String codigo) {
        return registro.buscar(codigo);
    }

    /** Busca un repartidor por identificacion; null si no existe. */
    public Repartidor buscarRepartidor(String identificacion) {
        return repartidores.buscar(identificacion);
    }

    /** Devuelve los paquetes en el orden en que llegaron. */
    public List<Paquete> paquetesEnOrdenDeRegistro() {
        return registro.listarEnOrdenDeRegistro();
    }

    /** Devuelve los repartidores en orden de registro. */
    public List<Repartidor> listarRepartidores() {
        return repartidores.listar();
    }

    /** Devuelve los municipios sin repetir. */
    public Set<String> municipios() {
        return registro.obtenerMunicipios();
    }

    /** Devuelve los municipios sin repetir y en orden alfabetico. */
    public SortedSet<String> municipiosOrdenados() {
        return registro.obtenerMunicipiosOrdenados();
    }

    /** Devuelve los paquetes de un municipio. */
    public List<Paquete> paquetesDeMunicipio(String municipio) {
        return registro.obtenerPaquetesDe(municipio);
    }

    /** Devuelve la relacion municipio -> paquetes. */
    public Map<String, List<Paquete>> agrupacionPorMunicipio() {
        return registro.obtenerAgrupacion();
    }

    // ---------------- Ordenamientos ----------------

    /** Devuelve una copia ordenada con el criterio dado; el registro no cambia. */
    public List<Paquete> ordenarPor(Comparator<Paquete> criterio) {
        if (criterio == null) {
            throw new IllegalArgumentException("El criterio no puede ser nulo");
        }
        List<Paquete> copia = new ArrayList<>(registro.listarEnOrdenDeRegistro());
        copia.sort(criterio);
        return copia;
    }

    /** Devuelve una copia ordenada por codigo (orden natural). */
    public List<Paquete> ordenarPorCodigo() {
        return ordenarPor(Comparator.naturalOrder());
    }

    // ---------------- Despacho y entregas ----------------

    /** Devuelve los pendientes en orden de llegada. */
    public List<Paquete> pendientesPorLlegada() {
        return despacho.pendientesPorLlegada();
    }

    /** Devuelve los pendientes en orden de despacho (prioridad, tiempo, codigo). */
    public List<Paquete> pendientesPorPrioridad() {
        return despacho.pendientesPorPrioridad();
    }

    /**
     * Despacha el siguiente por prioridad: busca un repartidor de su zona, LO ASIGNA y solo despues
     * saca el paquete de la cola. Si no hay repartidor, lanza IllegalStateException y el paquete se queda.
     * Si no hay pendientes, devuelve null.
     */
    public Paquete despacharSiguientePorPrioridad() {
        Paquete p = despacho.verSiguientePorPrioridad();
        if (p == null) {
            return null;
        }
        Repartidor r = repartidores.buscarDisponiblePorZona(p.getDestino());
        if (r == null) {
            throw new IllegalStateException("No hay repartidor disponible en " + p.getDestino());
        }
        repartidores.asignar(p, r.getIdentificacion());
        despacho.despacharSiguientePorPrioridad();
        return p;
    }

    /** Igual que el anterior, pero por orden de llegada. */
    public Paquete atenderSiguientePorLlegada() {
        Paquete p = despacho.verSiguientePorLlegada();
        if (p == null) {
            return null;
        }
        Repartidor r = repartidores.buscarDisponiblePorZona(p.getDestino());
        if (r == null) {
            throw new IllegalStateException("No hay repartidor disponible en " + p.getDestino());
        }
        repartidores.asignar(p, r.getIdentificacion());
        despacho.atenderSiguientePorLlegada();
        return p;
    }

    /**
     * Asigna un paquete pendiente a un repartidor elegido. El paquete debe existir y estar pendiente;
     * la disponibilidad y la zona las valida GestorRepartidores. Solo despues se retira de las colas.
     */
    public void asignarManualmente(String codigoPaquete, String identificacion) {
        Paquete p = registro.buscar(codigoPaquete);
        if (p == null) {
            throw new IllegalArgumentException("No existe el paquete " + codigoPaquete);
        }
        if (!despacho.contiene(p)) {
            throw new IllegalStateException("El paquete " + codigoPaquete + " no esta pendiente");
        }
        repartidores.asignar(p, identificacion);
        despacho.retirar(p);
    }

    /** Devuelve el repartidor que lleva el paquete, o null. */
    public Repartidor repartidorAsignado(String codigoPaquete) {
        return repartidores.obtenerAsignado(codigoPaquete);
    }

    /** Registra la entrega: libera al repartidor y pasa el paquete al historial. Si no esta en ruta, IllegalStateException. */
    public void registrarEntrega(String codigoPaquete) {
        if (repartidores.obtenerAsignado(codigoPaquete) == null) {
            throw new IllegalStateException("El paquete " + codigoPaquete + " no esta en ruta");
        }
        repartidores.liberar(codigoPaquete);
        historial.registrar(registro.buscar(codigoPaquete));
    }

    /** Devuelve el historial de entregas. */
    public HistorialEntregas getHistorial() {
        return historial;
    }

    // ---------------- Analisis ----------------

    /** Suma el peso de los paquetes de un municipio (recursivo). */
    public double pesoTotalPorMunicipio(String municipio) {
        return AnalisisRecursivo.pesoTotalPorMunicipio(
                new ArrayList<>(registro.listarEnOrdenDeRegistro()), municipio);
    }

    /** Cuenta los paquetes con prioridad igual o mayor a la dada (recursivo). */
    public int contarConPrioridadMinima(int prioridadMinima) {
        return AnalisisRecursivo.contarConPrioridadMinima(
                new ArrayList<>(registro.listarEnOrdenDeRegistro()), prioridadMinima);
    }

    /** Busca por codigo con busqueda binaria; antes ordena por codigo, que es lo que esa busqueda necesita. */
    public Paquete buscarPorCodigoBinaria(String codigo) {
        List<Paquete> ordenados = ordenarPorCodigo();
        return BusquedaBinaria.buscar(ordenados, codigo);
    }

    /** Devuelve el paquete mas pesado (divide y venceras). */
    public Paquete paqueteMasPesado() {
        return DivideYVenceras.paqueteMasPesado(new ArrayList<>(registro.listarEnOrdenDeRegistro()));
    }
}
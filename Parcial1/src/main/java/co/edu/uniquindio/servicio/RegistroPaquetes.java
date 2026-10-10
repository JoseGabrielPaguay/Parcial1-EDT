package co.edu.uniquindio.servicio;

import co.edu.uniquindio.modelo.Paquete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Registro de todos los paquetes de la jornada.
 * Guarda el orden de llegada, permite buscar por codigo y agrupa por municipio.
 * Los paquetes nunca se borran de aqui (sirve para auditoria).
 */
public class RegistroPaquetes {

    // Paquetes en el orden exacto en que se registraron
    private final List<Paquete> enOrdenDeRegistro = new ArrayList<>();
    // Busqueda rapida por codigo
    private final Map<String, Paquete> porCodigo = new HashMap<>();
    // Municipios sin repetir
    private final Set<String> municipios = new HashSet<>();
    // Municipios sin repetir y en orden alfabetico
    private final SortedSet<String> municipiosOrdenados = new TreeSet<>();
    // Municipio -> paquetes que van a ese municipio
    private final Map<String, List<Paquete>> paquetesPorMunicipio = new HashMap<>();

    /**
     * Registra un paquete. Nulo da IllegalArgumentException; codigo repetido da IllegalStateException.
     * El codigo se revisa antes de tocar cualquier estructura, para que nada quede a medias.
     */
    public void registrar(Paquete p) {
        if (p == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo");
        }
        if (porCodigo.containsKey(p.getCodigo())) {
            throw new IllegalStateException("Ya existe un paquete con el codigo " + p.getCodigo());
        }
        enOrdenDeRegistro.add(p);
        porCodigo.put(p.getCodigo(), p);
        municipios.add(p.getDestino());
        municipiosOrdenados.add(p.getDestino());
        paquetesPorMunicipio.computeIfAbsent(p.getDestino(), k -> new ArrayList<>()).add(p);
    }

    /** Dice si ya hay un paquete registrado con ese codigo. */
    public boolean existe(String codigo) {
        return codigo != null && porCodigo.containsKey(codigo.trim());
    }

    /** Busca un paquete por codigo (rapido). Devuelve null si no existe. */
    public Paquete buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return porCodigo.get(codigo.trim());
    }

    /** Devuelve los paquetes en el orden en que llegaron. No se puede modificar. */
    public List<Paquete> listarEnOrdenDeRegistro() {
        return Collections.unmodifiableList(enOrdenDeRegistro);
    }

    /** Devuelve los municipios sin repetir. No se puede modificar. */
    public Set<String> obtenerMunicipios() {
        return Collections.unmodifiableSet(municipios);
    }

    /** Devuelve los municipios sin repetir y ordenados alfabeticamente. No se puede modificar. */
    public SortedSet<String> obtenerMunicipiosOrdenados() {
        return Collections.unmodifiableSortedSet(municipiosOrdenados);
    }

    /** Devuelve los paquetes de un municipio, en orden de registro. Si no tiene, una lista vacia. */
    public List<Paquete> obtenerPaquetesDe(String municipio) {
        if (municipio == null) {
            return Collections.emptyList();
        }
        List<Paquete> lista = paquetesPorMunicipio.get(municipio.trim());
        if (lista == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(lista);
    }

    /** Devuelve la relacion municipio -> paquetes. Ni el mapa ni sus listas se pueden modificar. */
    public Map<String, List<Paquete>> obtenerAgrupacion() {
        Map<String, List<Paquete>> copia = new HashMap<>();
        for (Map.Entry<String, List<Paquete>> entrada : paquetesPorMunicipio.entrySet()) {
            copia.put(entrada.getKey(), Collections.unmodifiableList(entrada.getValue()));
        }
        return Collections.unmodifiableMap(copia);
    }

    /** Devuelve cuantos paquetes hay registrados. */
    public int cantidad() {
        return enOrdenDeRegistro.size();
    }
}
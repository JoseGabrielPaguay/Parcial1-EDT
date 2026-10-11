package co.edu.uniquindio.prueba;

import co.edu.uniquindio.comparadores.ComparadoresPaquete;
import co.edu.uniquindio.datos.DatosIniciales;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.modelo.Repartidor;
import co.edu.uniquindio.servicio.CentroDistribucion;
import co.edu.uniquindio.util.Prueba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Prueba que une todo: carga los datos quemados en el centro y revisa que
 * registro, consultas, algoritmos, despacho y entregas funcionen juntos.
 */
public class PruebaIntegral {

    // Cuantos paquetes se despachan en la prueba de despacho
    private static final int CANTIDAD_A_DESPACHAR = 5;

    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    public static void ejecutar() {
        Prueba.titulo("PruebaIntegral");
        pruebaRegistroYMunicipios();
        pruebaAlgoritmos();
        pruebaValidaciones();
        pruebaDespachoYEntregas();
    }

    // Crea un centro nuevo y le carga los datos quemados que le pasen
    private static CentroDistribucion cargarCentro(List<Paquete> paquetes, List<Repartidor> repartidores) {
        CentroDistribucion centro = new CentroDistribucion();
        for (Repartidor repartidor : repartidores) {
            centro.registrarRepartidor(repartidor);
        }
        for (Paquete paquete : paquetes) {
            centro.registrarPaquete(paquete);
        }
        return centro;
    }

    // Convierte una lista de paquetes en "PQ1,PQ2,..." para compararlas facil
    private static String codigos(Iterable<Paquete> paquetes) {
        StringBuilder sb = new StringBuilder();
        for (Paquete paquete : paquetes) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(paquete.getCodigo());
        }
        return sb.toString();
    }

    private static boolean casiIgual(double a, double b) {
        return Math.abs(a - b) < 0.0001;
    }

    private static void pruebaRegistroYMunicipios() {
        List<Paquete> datos = DatosIniciales.crearPaquetes();
        CentroDistribucion centro = cargarCentro(datos, DatosIniciales.crearRepartidores());

        Prueba.verificar(datos.size() == 32 && centro.paquetesEnOrdenDeRegistro().size() == 32,
                "El centro tiene los 32 paquetes registrados");
        Prueba.verificar(codigos(centro.paquetesEnOrdenDeRegistro()).equals(codigos(datos)),
                "Los paquetes quedan en el mismo orden en que se registraron");

        // Municipios sin repetir y en orden alfabetico
        List<String> ordenados = new ArrayList<>(centro.municipiosOrdenados());
        List<String> esperados = Arrays.asList("Armenia", "Calarca", "Circasia",
                "Montenegro", "Quimbaya", "Salento");
        Prueba.verificar(ordenados.equals(esperados), "Los municipios salen sin repetir y en orden alfabetico");
        Prueba.verificar(centro.municipios().size() == esperados.size(),
                "El conjunto de municipios tiene " + esperados.size() + " elementos");

        // Agrupacion: la suma de paquetes por municipio es el total
        int totalAgrupado = 0;
        for (String municipio : centro.municipios()) {
            totalAgrupado += centro.paquetesDeMunicipio(municipio).size();
        }
        Prueba.verificar(totalAgrupado == datos.size(), "Los paquetes agrupados por municipio suman el total");

        // Ordenar devuelve una copia: el orden de registro no cambia
        List<Paquete> porPeso = centro.ordenarPor(ComparadoresPaquete.POR_PESO_DESC);
        Prueba.verificar(porPeso.get(0).getCodigo().equals("PQ583"), "Ordenado por peso, el primero es el mas pesado");
        Prueba.verificar(codigos(centro.paquetesEnOrdenDeRegistro()).equals(codigos(datos)),
                "Ordenar no cambia el orden de registro");
    }

    private static void pruebaAlgoritmos() {
        List<Paquete> datos = DatosIniciales.crearPaquetes();
        CentroDistribucion centro = cargarCentro(datos, DatosIniciales.crearRepartidores());

        // La suma de pesos por municipio debe dar el peso total de todos los paquetes
        double pesoTotal = 0;
        for (Paquete paquete : datos) {
            pesoTotal += paquete.getPeso();
        }
        double sumaPorMunicipio = 0;
        for (String municipio : centro.municipios()) {
            sumaPorMunicipio += centro.pesoTotalPorMunicipio(municipio);
        }
        Prueba.verificar(casiIgual(sumaPorMunicipio, pesoTotal),
                "La suma de pesos por municipio es igual al peso total");

        // Conteo por prioridad: se compara contra un conteo hecho con ciclo
        int minima = 4;
        int esperado = 0;
        for (Paquete paquete : datos) {
            if (paquete.getPrioridad() >= minima) {
                esperado++;
            }
        }
        Prueba.verificar(centro.contarConPrioridadMinima(minima) == esperado,
                "El conteo recursivo por prioridad coincide con el conteo con ciclo (" + esperado + ")");

        // Busqueda binaria: encuentra los 32 codigos
        boolean encontroTodos = true;
        for (Paquete paquete : datos) {
            Paquete hallado = centro.buscarPorCodigoBinaria(paquete.getCodigo());
            if (hallado == null || !hallado.getCodigo().equals(paquete.getCodigo())) {
                encontroTodos = false;
            }
        }
        Prueba.verificar(encontroTodos, "La busqueda binaria encuentra los " + datos.size() + " codigos");
        Prueba.verificar(centro.buscarPorCodigoBinaria("PQ000") == null,
                "La busqueda binaria devuelve null con un codigo que no existe");

        // Divide y venceras: coincide con el maximo calculado con ciclo
        Paquete mayor = datos.get(0);
        for (Paquete paquete : datos) {
            if (paquete.getPeso() > mayor.getPeso()) {
                mayor = paquete;
            }
        }
        Prueba.verificar(centro.paqueteMasPesado().getCodigo().equals(mayor.getCodigo()),
                "El paquete mas pesado coincide con el maximo (" + mayor.getCodigo() + ")");
    }

    private static void pruebaValidaciones() {
        List<Paquete> datos = DatosIniciales.crearPaquetes();
        List<Repartidor> repartidores = DatosIniciales.crearRepartidores();
        CentroDistribucion centro = cargarCentro(datos, repartidores);

        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> centro.registrarPaquete(datos.get(0)),
                "Un paquete con codigo repetido no se acepta");
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> centro.registrarRepartidor(repartidores.get(0)),
                "Un repartidor con identificacion repetida no se acepta");
        Prueba.verificar(centro.paquetesEnOrdenDeRegistro().size() == datos.size(),
                "Despues de los rechazos no cambia la cantidad de paquetes");
    }

    private static void pruebaDespachoYEntregas() {
        List<Paquete> datos = DatosIniciales.crearPaquetes();
        List<Repartidor> repartidores = DatosIniciales.crearRepartidores();
        CentroDistribucion centro = cargarCentro(datos, repartidores);

        // Los repartidores que no estan disponibles al comenzar nunca deben recibir paquetes
        Set<String> idsNoDisponibles = new HashSet<>();
        for (Repartidor repartidor : repartidores) {
            if (!repartidor.isDisponible()) {
                idsNoDisponibles.add(repartidor.getIdentificacion());
            }
        }

        // Orden esperado: los 5 primeros segun la regla de despacho
        List<Paquete> copia = new ArrayList<>(datos);
        copia.sort(ComparadoresPaquete.DESPACHO);
        List<Paquete> esperados = new ArrayList<>(copia.subList(0, CANTIDAD_A_DESPACHAR));

        List<Paquete> despachados = new ArrayList<>();
        Map<String, Repartidor> asignados = new LinkedHashMap<>();
        boolean zonaCorrecta = true;
        boolean ningunoNoDisponible = true;
        for (int i = 0; i < CANTIDAD_A_DESPACHAR; i++) {
            Paquete paquete = centro.despacharSiguientePorPrioridad();
            Repartidor repartidor = centro.repartidorAsignado(paquete.getCodigo());
            despachados.add(paquete);
            asignados.put(paquete.getCodigo(), repartidor);
            if (repartidor == null || !repartidor.getZona().equals(paquete.getDestino())) {
                zonaCorrecta = false;
            }
            if (repartidor == null || idsNoDisponibles.contains(repartidor.getIdentificacion())) {
                ningunoNoDisponible = false;
            }
        }

        Prueba.verificar(codigos(despachados).equals(codigos(esperados)),
                "Al despachar " + CANTIDAD_A_DESPACHAR + " salen en el orden de DESPACHO: " + codigos(despachados));
        Prueba.verificar(zonaCorrecta, "Cada repartidor asignado es de la zona del paquete");
        Prueba.verificar(ningunoNoDisponible, "El repartidor no disponible nunca recibe un paquete");

        // Un paquete que sigue pendiente no esta en ruta: no se puede entregar
        Paquete pendiente = centro.pendientesPorPrioridad().get(0);
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> centro.registrarEntrega(pendiente.getCodigo()),
                "Entregar un paquete que no esta en ruta da error");

        // Se entregan en orden inverso al despacho para comprobar que el historial guarda el orden de entrega
        List<Paquete> entregados = new ArrayList<>();
        for (int i = despachados.size() - 1; i >= 0; i--) {
            centro.registrarEntrega(despachados.get(i).getCodigo());
            entregados.add(despachados.get(i));
        }

        Prueba.verificar(codigos(centro.getHistorial()).equals(codigos(entregados)),
                "El historial queda en el mismo orden de las entregas: " + codigos(centro.getHistorial()));

        boolean todosLiberados = true;
        for (Repartidor repartidor : asignados.values()) {
            if (!repartidor.isDisponible()) {
                todosLiberados = false;
            }
        }
        Prueba.verificar(todosLiberados, "Al entregar, los repartidores quedan disponibles otra vez");
    }
}
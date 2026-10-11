package co.edu.uniquindio.prueba;

import co.edu.uniquindio.comparadores.ComparadoresPaquete;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.modelo.Repartidor;
import co.edu.uniquindio.servicio.CentroDistribucion;
import co.edu.uniquindio.util.Prueba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Pruebas de CentroDistribucion con un escenario pequeno: 4 paquetes y 3 repartidores.
 */
public class PruebaCentroDistribucion {

    /** Corre las pruebas y muestra el resumen. */
    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    /** Devuelve los codigos de una lista de paquetes. */
    private static List<String> codigos(List<Paquete> paquetes) {
        List<String> resultado = new ArrayList<>();
        for (Paquete p : paquetes) {
            resultado.add(p.getCodigo());
        }
        return resultado;
    }

    /** Crea un centro con los 4 paquetes del ejemplo y los 3 repartidores. */
    private static CentroDistribucion crearCentro() {
        CentroDistribucion centro = new CentroDistribucion();
        centro.registrarPaquete(new Paquete("PQ420", "Armenia", 4.5, 3, 30));
        centro.registrarPaquete(new Paquete("PQ731", "Armenia", 7.2, 5, 45));
        centro.registrarPaquete(new Paquete("PQ105", "Salento", 3.0, 5, 25));
        centro.registrarPaquete(new Paquete("PQ942", "Calarca", 2.3, 4, 20));
        centro.registrarRepartidor(new Repartidor("R1", "Ana", "Armenia"));
        centro.registrarRepartidor(new Repartidor("R2", "Luis", "Salento"));
        centro.registrarRepartidor(new Repartidor("R3", "Marta", "Calarca"));
        return centro;
    }

    /** Contiene todos los casos de prueba de CentroDistribucion. */
    public static void ejecutar() {
        Prueba.titulo("CentroDistribucion");

        final CentroDistribucion centro = crearCentro();
        Repartidor r1 = centro.buscarRepartidor("R1");
        Repartidor r2 = centro.buscarRepartidor("R2");
        Repartidor r3 = centro.buscarRepartidor("R3");

        // Registro y consultas
        Prueba.verificar(codigos(centro.paquetesEnOrdenDeRegistro()).equals(
                        Arrays.asList("PQ420", "PQ731", "PQ105", "PQ942")),
                "los paquetes quedan en orden de registro");
        Prueba.verificar(centro.existePaquete("PQ105") && !centro.existePaquete("PQ000"),
                "existePaquete responde bien");
        Prueba.verificar(centro.buscarPaquete("PQ942").getDestino().equals("Calarca"),
                "buscarPaquete devuelve el paquete correcto");
        Prueba.verificar(centro.listarRepartidores().size() == 3, "hay 3 repartidores");
        Prueba.verificar(new ArrayList<>(centro.municipiosOrdenados()).equals(
                        Arrays.asList("Armenia", "Calarca", "Salento")),
                "los municipios salen ordenados");
        Prueba.verificar(centro.municipios().size() == 3, "hay 3 municipios sin repetir");
        Prueba.verificar(codigos(centro.paquetesDeMunicipio("Armenia")).equals(
                        Arrays.asList("PQ420", "PQ731")),
                "Armenia tiene PQ420 y PQ731");
        Prueba.verificar(centro.agrupacionPorMunicipio().size() == 3, "la agrupacion tiene 3 municipios");

        // Analisis (no dependen del despacho)
        Prueba.verificar(Math.abs(centro.pesoTotalPorMunicipio("Armenia") - 11.7) < 1e-9,
                "el peso total de Armenia es 11.7");
        Prueba.verificar(centro.contarConPrioridadMinima(4) == 3,
                "hay 3 paquetes con prioridad 4 o mas");
        Prueba.verificar(centro.paqueteMasPesado().getCodigo().equals("PQ731"),
                "el paquete mas pesado es PQ731");

        // 1. Repetido: no entra a ninguna parte
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> centro.registrarPaquete(new Paquete("PQ105", "Salento", 1.0, 3, 10)),
                "registrar un codigo repetido");
        Prueba.verificar(centro.pendientesPorLlegada().size() == 4,
                "tras el repetido siguen 4 pendientes");
        Prueba.verificar(codigos(centro.pendientesPorPrioridad()).equals(
                        Arrays.asList("PQ105", "PQ731", "PQ942", "PQ420")),
                "los pendientes por prioridad salen PQ105, PQ731, PQ942, PQ420");

        // 2. Despachar por prioridad
        Paquete despachado = centro.despacharSiguientePorPrioridad();
        Prueba.verificar(despachado.getCodigo().equals("PQ105"), "se despacha primero PQ105");
        Prueba.verificar(centro.repartidorAsignado("PQ105") == r2, "PQ105 queda con R2");
        Prueba.verificar(!r2.isDisponible(), "R2 queda ocupado");
        Prueba.verificar(centro.pendientesPorLlegada().size() == 3, "PQ105 salio de los pendientes");

        // 3. Entrega
        centro.registrarEntrega("PQ105");
        Prueba.verificar(centro.getHistorial().cantidad() == 1, "el historial tiene 1 paquete");
        Prueba.verificar(r2.isDisponible(), "R2 vuelve a estar disponible");
        Prueba.verificar(centro.repartidorAsignado("PQ105") == null, "PQ105 ya no tiene repartidor");

        // 4. Entregar uno que no esta en ruta
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> centro.registrarEntrega("PQ420"), "entregar un paquete que no esta en ruta");
        Prueba.verificar(centro.getHistorial().cantidad() == 1, "el historial no cambio");

        // 5. Asignacion manual con zona incorrecta
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> centro.asignarManualmente("PQ420", "R3"), "asignar PQ420 a R3 (zona distinta)");
        Prueba.verificar(codigos(centro.pendientesPorLlegada()).contains("PQ420"),
                "PQ420 sigue pendiente despues del rechazo");
        Prueba.verificar(r3.isDisponible(), "R3 sigue disponible");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> centro.asignarManualmente("PQ000", "R1"), "asignar un paquete que no existe");

        // 6. Asignacion manual a un repartidor no disponible
        centro.asignarManualmente("PQ731", "R1");
        Prueba.verificar(centro.repartidorAsignado("PQ731") == r1 && !r1.isDisponible(),
                "PQ731 queda con R1 y R1 ocupado");
        Prueba.verificar(!codigos(centro.pendientesPorLlegada()).contains("PQ731"),
                "PQ731 salio de los pendientes");
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> centro.asignarManualmente("PQ420", "R1"), "asignar PQ420 a R1 (no disponible)");
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> centro.asignarManualmente("PQ731", "R1"), "asignar un paquete que ya no esta pendiente");
        Prueba.verificar(codigos(centro.pendientesPorLlegada()).contains("PQ420"),
                "PQ420 sigue pendiente despues del segundo rechazo");

        // 7. Ordenar no cambia el registro
        List<Paquete> porPeso = centro.ordenarPor(ComparadoresPaquete.POR_PESO_DESC);
        Prueba.verificar(codigos(porPeso).equals(Arrays.asList("PQ731", "PQ420", "PQ105", "PQ942")),
                "ordenar por peso da PQ731, PQ420, PQ105, PQ942");
        Prueba.verificar(codigos(centro.paquetesEnOrdenDeRegistro()).equals(
                        Arrays.asList("PQ420", "PQ731", "PQ105", "PQ942")),
                "el orden de registro no cambio");
        Prueba.verificar(codigos(centro.ordenarPorCodigo()).equals(
                        Arrays.asList("PQ105", "PQ420", "PQ731", "PQ942")),
                "ordenarPorCodigo da el orden natural");

        // 8. Busqueda binaria
        Prueba.verificar(centro.buscarPorCodigoBinaria("PQ942") != null
                        && centro.buscarPorCodigoBinaria("PQ942").getCodigo().equals("PQ942"),
                "la busqueda binaria encuentra PQ942");
        Prueba.verificar(centro.buscarPorCodigoBinaria("PQ000") == null,
                "la busqueda binaria devuelve null si no existe");

        // 9. Primero se asigna y despues se saca de la cola
        Paquete segundo = centro.despacharSiguientePorPrioridad();
        Prueba.verificar(segundo.getCodigo().equals("PQ942") && centro.repartidorAsignado("PQ942") == r3,
                "se despacha PQ942 con R3");
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> centro.despacharSiguientePorPrioridad(),
                "despachar PQ420 sin repartidor libre en Armenia");
        Prueba.verificar(centro.pendientesPorPrioridad().size() == 1
                        && centro.pendientesPorPrioridad().get(0).getCodigo().equals("PQ420"),
                "PQ420 no se perdio: sigue en la cola");

        // 10. Entregas y orden del historial
        centro.registrarEntrega("PQ731");
        centro.registrarEntrega("PQ942");
        Prueba.verificar(r1.isDisponible() && r3.isDisponible(), "R1 y R3 quedan libres");
        Paquete ultimo = centro.despacharSiguientePorPrioridad();
        Prueba.verificar(ultimo.getCodigo().equals("PQ420") && centro.repartidorAsignado("PQ420") == r1,
                "ahora PQ420 se despacha con R1");
        centro.registrarEntrega("PQ420");
        Prueba.verificar(centro.getHistorial().toString().equals("PQ105 -> PQ731 -> PQ942 -> PQ420"),
                "el historial conserva el orden real de las entregas");
        Prueba.verificar(centro.despacharSiguientePorPrioridad() == null,
                "sin pendientes, despachar devuelve null");
        Prueba.verificar(centro.paquetesEnOrdenDeRegistro().size() == 4,
                "el registro conserva los 4 paquetes (auditoria)");

        // 11. Atencion por orden de llegada
        CentroDistribucion otro = crearCentro();
        Paquete primero = otro.atenderSiguientePorLlegada();
        Prueba.verificar(primero.getCodigo().equals("PQ420") && otro.repartidorAsignado("PQ420") != null,
                "por llegada se atiende primero PQ420 y recibe repartidor");
        Prueba.verificar(codigos(otro.pendientesPorPrioridad()).equals(Arrays.asList("PQ105", "PQ731", "PQ942")),
                "al atender por llegada, ya no sale por prioridad");
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> otro.atenderSiguientePorLlegada(),
                "atender PQ731 sin repartidor libre (R1 esta con PQ420)");
        Prueba.verificar(otro.pendientesPorLlegada().size() == 3, "PQ731 sigue pendiente");
    }
}
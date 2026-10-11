package co.edu.uniquindio.prueba;

import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.servicio.RegistroPaquetes;
import co.edu.uniquindio.util.Prueba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Pruebas de RegistroPaquetes: orden de registro, codigos unicos, municipios y agrupacion.
 */
public class PruebaRegistroPaquetes {

    /** Corre las pruebas y muestra el resumen. */
    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    /** Devuelve los codigos de una lista de paquetes, para comparar mas facil. */
    private static List<String> codigos(List<Paquete> paquetes) {
        List<String> resultado = new ArrayList<>();
        for (Paquete p : paquetes) {
            resultado.add(p.getCodigo());
        }
        return resultado;
    }

    /** Contiene todos los casos de prueba de RegistroPaquetes. */
    public static void ejecutar() {
        Prueba.titulo("RegistroPaquetes");

        RegistroPaquetes registro = new RegistroPaquetes();
        Prueba.verificar(registro.cantidad() == 0, "un registro nuevo esta vacio");

        registro.registrar(new Paquete("PQ731", "Armenia", 4.0, 3, 20));
        registro.registrar(new Paquete("PQ105", "Salento", 3.0, 5, 25));
        registro.registrar(new Paquete("PQ942", "Armenia", 2.0, 4, 20));
        registro.registrar(new Paquete("PQ318", "Calarca", 5.0, 2, 30));
        registro.registrar(new Paquete("PQ567", "Salento", 1.5, 3, 15));
        registro.registrar(new Paquete("PQ829", "Armenia", 6.0, 1, 40));

        // 1. Orden de registro
        Prueba.verificar(codigos(registro.listarEnOrdenDeRegistro()).equals(
                        Arrays.asList("PQ731", "PQ105", "PQ942", "PQ318", "PQ567", "PQ829")),
                "se conserva el orden de registro");

        // 2. Codigo repetido: no cambia nada
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> registro.registrar(new Paquete("PQ942", "Filandia", 1.0, 3, 10)),
                "registrar un codigo repetido");
        Prueba.verificar(registro.cantidad() == 6, "tras el repetido, la cantidad sigue en 6");
        Prueba.verificar(registro.obtenerPaquetesDe("Armenia").size() == 3,
                "tras el repetido, Armenia sigue con 3 paquetes");
        Prueba.verificar(registro.obtenerMunicipios().size() == 3
                        && registro.obtenerPaquetesDe("Filandia").isEmpty(),
                "el repetido no agrego su municipio");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> registro.registrar(null), "registrar un paquete nulo");

        // 3. Consultas por codigo
        Prueba.verificar(registro.buscar("PQ318").getDestino().equals("Calarca"),
                "buscar PQ318 da destino Calarca");
        Prueba.verificar(registro.buscar("PQ000") == null, "buscar un codigo inexistente da null");
        Prueba.verificar(registro.existe("PQ731") && !registro.existe("PQ000"), "existe responde bien");
        Prueba.verificar(registro.buscar(" PQ105 ").getCodigo().equals("PQ105"),
                "buscar ignora espacios sobrantes");

        // 4. Municipios
        Prueba.verificar(registro.obtenerMunicipios().size() == 3, "hay 3 municipios sin repetir");
        Prueba.verificar(new ArrayList<>(registro.obtenerMunicipiosOrdenados()).equals(
                        Arrays.asList("Armenia", "Calarca", "Salento")),
                "los municipios ordenados son Armenia, Calarca, Salento");

        // 5. Paquetes por municipio
        Prueba.verificar(codigos(registro.obtenerPaquetesDe("Armenia")).equals(
                        Arrays.asList("PQ731", "PQ942", "PQ829")),
                "Armenia tiene PQ731, PQ942, PQ829 en ese orden");
        Prueba.verificar(codigos(registro.obtenerPaquetesDe("Salento")).equals(
                        Arrays.asList("PQ105", "PQ567")),
                "Salento tiene PQ105, PQ567");
        Prueba.verificar(registro.obtenerPaquetesDe("Filandia").isEmpty(),
                "un municipio sin paquetes da lista vacia");
        Prueba.verificar(registro.obtenerPaquetesDe(null).isEmpty(),
                "un municipio nulo da lista vacia");

        // Agrupacion completa
        Map<String, List<Paquete>> agrupacion = registro.obtenerAgrupacion();
        Prueba.verificar(agrupacion.size() == 3, "la agrupacion tiene 3 municipios");
        Prueba.verificar(codigos(agrupacion.get("Calarca")).equals(Arrays.asList("PQ318")),
                "la agrupacion de Calarca tiene PQ318");

        // 6. Nada se puede modificar desde afuera
        Prueba.esperarExcepcion(UnsupportedOperationException.class,
                () -> registro.listarEnOrdenDeRegistro().add(new Paquete("PQ999", "Armenia", 1.0, 3, 10)),
                "no se puede agregar a la lista de registro");
        Prueba.esperarExcepcion(UnsupportedOperationException.class,
                () -> registro.obtenerMunicipios().add("Filandia"),
                "no se puede agregar a los municipios");
        Prueba.esperarExcepcion(UnsupportedOperationException.class,
                () -> registro.obtenerMunicipiosOrdenados().add("Filandia"),
                "no se puede agregar a los municipios ordenados");
        Prueba.esperarExcepcion(UnsupportedOperationException.class,
                () -> registro.obtenerAgrupacion().put("Filandia", new ArrayList<>()),
                "no se puede modificar la agrupacion");
        Prueba.esperarExcepcion(UnsupportedOperationException.class,
                () -> registro.obtenerPaquetesDe("Armenia").clear(),
                "no se puede modificar la lista de un municipio");
        Prueba.verificar(registro.cantidad() == 6, "al final siguen los 6 paquetes");
    }
}
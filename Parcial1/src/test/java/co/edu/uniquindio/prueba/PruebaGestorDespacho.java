package co.edu.uniquindio.prueba;

import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.servicio.GestorDespacho;
import co.edu.uniquindio.util.Prueba;
import java.util.List;

/**
 * Pruebas del gestor de despacho.
 * Revisamos el orden por llegada, el orden por prioridad y que las dos
 * colas nunca se desincronicen.
 */
public class PruebaGestorDespacho {

    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    public static void ejecutar() {
        Prueba.titulo("Gestor de despacho");

        // 1. Por orden de llegada
        GestorDespacho llegada = new GestorDespacho();
        llegada.agregar(new Paquete("PQ731", "Armenia", 4.5, 5, 45));
        llegada.agregar(new Paquete("PQ105", "Salento", 3.0, 5, 25));
        llegada.agregar(new Paquete("PQ942", "Calarca", 4.6, 4, 20));
        llegada.agregar(new Paquete("PQ318", "Circasia", 2.0, 2, 15));
        String ordenLlegada = llegada.atenderSiguientePorLlegada().getCodigo() + " "
                + llegada.atenderSiguientePorLlegada().getCodigo() + " "
                + llegada.atenderSiguientePorLlegada().getCodigo() + " "
                + llegada.atenderSiguientePorLlegada().getCodigo();
        Prueba.verificar(ordenLlegada.equals("PQ731 PQ105 PQ942 PQ318"),
                "Por llegada salen PQ731, PQ105, PQ942, PQ318");
        Prueba.verificar(llegada.atenderSiguientePorLlegada() == null,
                "La quinta vez por llegada da null");

        // 2. Por prioridad (ejemplo del enunciado)
        GestorDespacho prioridad = crearEjemplo();
        String ordenPrioridad = prioridad.despacharSiguientePorPrioridad().getCodigo() + " "
                + prioridad.despacharSiguientePorPrioridad().getCodigo() + " "
                + prioridad.despacharSiguientePorPrioridad().getCodigo() + " "
                + prioridad.despacharSiguientePorPrioridad().getCodigo();
        Prueba.verificar(ordenPrioridad.equals("PQ105 PQ731 PQ942 PQ420"),
                "Por prioridad salen PQ105, PQ731, PQ942, PQ420");
        Prueba.verificar(prioridad.despacharSiguientePorPrioridad() == null,
                "La quinta vez por prioridad da null");

        // 3. Las colas no se desincronizan
        GestorDespacho mixto = crearEjemplo();
        Paquete primero = mixto.atenderSiguientePorLlegada();
        Prueba.verificar(primero.getCodigo().equals("PQ420"), "Por llegada sale primero PQ420");
        Prueba.verificar(mixto.cantidadPendientes() == 3, "Despues quedan 3 pendientes");
        boolean salioPQ420 = false;
        Paquete siguiente = mixto.despacharSiguientePorPrioridad();
        while (siguiente != null) {
            if (siguiente.getCodigo().equals("PQ420")) {
                salioPQ420 = true;
            }
            siguiente = mixto.despacharSiguientePorPrioridad();
        }
        Prueba.verificar(!salioPQ420, "PQ420 ya no sale por prioridad");

        // 4. Ver no saca nada
        GestorDespacho ver = crearEjemplo();
        Paquete visto = ver.verSiguientePorPrioridad();
        Prueba.verificar(visto.getCodigo().equals("PQ105") && ver.cantidadPendientes() == 4,
                "verSiguientePorPrioridad muestra PQ105 y no saca nada");
        Prueba.verificar(ver.verSiguientePorLlegada().getCodigo().equals("PQ420")
                        && ver.cantidadPendientes() == 4,
                "verSiguientePorLlegada muestra PQ420 y no saca nada");

        // 5. pendientesPorPrioridad da el orden y no vacia las colas
        List<Paquete> pendientes = ver.pendientesPorPrioridad();
        String ordenLista = "";
        for (Paquete paquete : pendientes) {
            ordenLista = ordenLista + paquete.getCodigo() + " ";
        }
        Prueba.verificar(ordenLista.equals("PQ105 PQ731 PQ942 PQ420 "),
                "pendientesPorPrioridad da PQ105, PQ731, PQ942, PQ420");
        Prueba.verificar(ver.cantidadPendientes() == 4, "pendientesPorPrioridad no vacia las colas");

        // 6. Retirar uno que no esta y agregar repetidos o nulos
        Paquete noEsta = new Paquete("PQ999", "Quimbaya", 1.0, 1, 10);
        Prueba.verificar(!ver.retirar(noEsta), "Retirar un paquete que no esta devuelve false");
        Paquete pq942 = ver.pendientesPorLlegada().get(3);
        Prueba.verificar(ver.retirar(pq942) && ver.cantidadPendientes() == 3,
                "Retirar PQ942 devuelve true y quedan 3");
        Paquete repetido = new Paquete("PQ777", "Montenegro", 2.0, 3, 30);
        GestorDespacho otro = new GestorDespacho();
        otro.agregar(repetido);
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> otro.agregar(repetido), "Agregar el mismo paquete dos veces lanza IllegalStateException");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> otro.agregar(null), "Agregar null lanza IllegalArgumentException");

        // 7. Gestor vacio
        GestorDespacho vacio = new GestorDespacho();
        Prueba.verificar(vacio.estaVacio() && vacio.verSiguientePorPrioridad() == null,
                "Un gestor nuevo esta vacio y ver da null");
    }

    // Crea el ejemplo del enunciado, en este orden de llegada
    private static GestorDespacho crearEjemplo() {
        GestorDespacho gestor = new GestorDespacho();
        gestor.agregar(new Paquete("PQ420", "Armenia", 3.2, 3, 30));
        gestor.agregar(new Paquete("PQ731", "Armenia", 4.5, 5, 45));
        gestor.agregar(new Paquete("PQ105", "Salento", 3.0, 5, 25));
        gestor.agregar(new Paquete("PQ942", "Calarca", 4.6, 4, 20));
        return gestor;
    }
}
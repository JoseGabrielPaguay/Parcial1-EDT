package co.edu.uniquindio.app;

import co.edu.uniquindio.comparadores.ComparadoresPaquete;
import co.edu.uniquindio.datos.DatosIniciales;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.modelo.Repartidor;
import co.edu.uniquindio.servicio.CentroDistribucion;
import java.util.ArrayList;
import java.util.List;

/**
 * Programa principal de QuindioExpress.
 * Carga los datos quemados en el centro de distribucion y muestra, seccion por
 * seccion, cada una de las operaciones del sistema.
 */
public class Main {

    public static void main(String[] args) {
        CentroDistribucion centro = new CentroDistribucion();

        // Aqui guardamos los paquetes que van saliendo a ruta, para entregarlos al final
        List<Paquete> enRuta = new ArrayList<>();

        cargarDatos(centro);
        mostrarValidaciones(centro);
        mostrarOrdenDeRegistro(centro);
        mostrarConsultaPorCodigo(centro);
        mostrarMunicipios(centro);
        mostrarAgrupacion(centro);
        mostrarOrdenamientos(centro);
        mostrarRecursion(centro);
        mostrarBusquedaBinaria(centro);
        mostrarDivideYVenceras(centro);
        mostrarDespachoPorPrioridad(centro, enRuta);
        mostrarAtencionPorLlegada(centro, enRuta);
        mostrarAsignacionesRechazadas(centro);
        mostrarEntregas(centro, enRuta);
        mostrarEntregaRechazada(centro);
    }

    /** 1. Registra todos los repartidores y paquetes de DatosIniciales. */
    private static void cargarDatos(CentroDistribucion centro) {
        titulo("1. Cargar datos");
        for (Repartidor repartidor : DatosIniciales.crearRepartidores()) {
            centro.registrarRepartidor(repartidor);
        }
        for (Paquete paquete : DatosIniciales.crearPaquetes()) {
            centro.registrarPaquete(paquete);
        }
        System.out.println("Repartidores registrados: " + centro.listarRepartidores().size());
        System.out.println("Paquetes registrados: " + centro.paquetesEnOrdenDeRegistro().size());
    }

    /** 2. Muestra que el sistema rechaza datos invalidos o repetidos. */
    private static void mostrarValidaciones(CentroDistribucion centro) {
        titulo("2. Validaciones rechazadas");

        try {
            centro.registrarPaquete(new Paquete("PQ731", "Armenia", 2.0, 3, 20));
            System.out.println("ERROR: se acepto un codigo repetido");
        } catch (IllegalStateException e) {
            System.out.println("Codigo repetido rechazado: " + e.getMessage());
        }

        try {
            centro.registrarPaquete(new Paquete("PQ999", "Salento", -1, 3, 20));
            System.out.println("ERROR: se acepto un peso negativo");
        } catch (IllegalArgumentException e) {
            System.out.println("Peso negativo rechazado: " + e.getMessage());
        }

        try {
            centro.registrarRepartidor(new Repartidor("R01", "Otro Repartidor", "Armenia"));
            System.out.println("ERROR: se acepto un repartidor repetido");
        } catch (IllegalStateException e) {
            System.out.println("Repartidor repetido rechazado: " + e.getMessage());
        }

        System.out.println("Siguen habiendo " + centro.paquetesEnOrdenDeRegistro().size() + " paquetes");
    }

    /** 3. Muestra los paquetes en el orden en que llegaron. */
    private static void mostrarOrdenDeRegistro(CentroDistribucion centro) {
        titulo("3. Paquetes en orden de registro");
        int numero = 1;
        for (Paquete paquete : centro.paquetesEnOrdenDeRegistro()) {
            System.out.println(numero + ". " + paquete);
            numero++;
        }
    }

    /** 4. Busca un paquete que existe y uno que no. */
    private static void mostrarConsultaPorCodigo(CentroDistribucion centro) {
        titulo("4. Consulta por codigo");
        System.out.println("Buscar PQ318: " + centro.buscarPaquete("PQ318"));
        System.out.println("Buscar PQ000: " + centro.buscarPaquete("PQ000"));
    }

    /** 5. Muestra los municipios sin repetir y en orden alfabetico. */
    private static void mostrarMunicipios(CentroDistribucion centro) {
        titulo("5. Municipios");
        System.out.println("Sin repetir: " + centro.municipios());
        System.out.println("Ordenados: " + centro.municipiosOrdenados());
    }

    /** 6. Muestra los paquetes de cada municipio. */
    private static void mostrarAgrupacion(CentroDistribucion centro) {
        titulo("6. Paquetes agrupados por municipio");
        for (String municipio : centro.municipiosOrdenados()) {
            List<Paquete> paquetes = centro.agrupacionPorMunicipio().get(municipio);
            String codigos = "";
            for (Paquete paquete : paquetes) {
                codigos = codigos + paquete.getCodigo() + " ";
            }
            System.out.println(municipio + " (" + paquetes.size() + "): " + codigos);
        }
    }

    /** 7. Ordena por prioridad, peso y tiempo, y muestra que el registro no cambio. */
    private static void mostrarOrdenamientos(CentroDistribucion centro) {
        titulo("7. Ordenamientos");
        System.out.println("Primeros 5 por prioridad (mayor primero):");
        mostrarPrimeros(centro.ordenarPor(ComparadoresPaquete.POR_PRIORIDAD_DESC), 5);
        System.out.println("Primeros 5 por peso (mayor primero):");
        mostrarPrimeros(centro.ordenarPor(ComparadoresPaquete.POR_PESO_DESC), 5);
        System.out.println("Primeros 5 por tiempo (menor primero):");
        mostrarPrimeros(centro.ordenarPor(ComparadoresPaquete.POR_TIEMPO_ASC), 5);
        System.out.println("Primeros 5 en orden de registro (no cambio):");
        mostrarPrimeros(centro.paquetesEnOrdenDeRegistro(), 5);
    }

    /** 8. Usa los metodos recursivos. */
    private static void mostrarRecursion(CentroDistribucion centro) {
        titulo("8. Recursion");
        // Se redondea a un decimal para que no salga 45.10000000000001
        double pesoArmenia = Math.round(centro.pesoTotalPorMunicipio("Armenia") * 10) / 10.0;
        System.out.println("Peso total de Armenia: " + pesoArmenia + " kg");
        System.out.println("Paquetes con prioridad 4 o mas: " + centro.contarConPrioridadMinima(4));
    }

    /** 9. Busca por codigo con busqueda binaria. */
    private static void mostrarBusquedaBinaria(CentroDistribucion centro) {
        titulo("9. Busqueda binaria");
        System.out.println("Buscar PQ583: " + centro.buscarPorCodigoBinaria("PQ583"));
        System.out.println("Buscar PQ000: " + centro.buscarPorCodigoBinaria("PQ000"));
    }

    /** 10. Encuentra el paquete mas pesado con divide y venceras. */
    private static void mostrarDivideYVenceras(CentroDistribucion centro) {
        titulo("10. Divide y venceras");
        System.out.println("Paquete mas pesado: " + centro.paqueteMasPesado());
    }

    /** 11. Despacha 3 paquetes por prioridad y muestra a quien quedo cada uno. */
    private static void mostrarDespachoPorPrioridad(CentroDistribucion centro, List<Paquete> enRuta) {
        titulo("11. Despacho por prioridad");
        for (int i = 0; i < 3; i++) {
            Paquete paquete = centro.despacharSiguientePorPrioridad();
            enRuta.add(paquete);
            System.out.println("Sale " + paquete + " -> " + centro.repartidorAsignado(paquete.getCodigo()));
        }
    }

    /** 12. Atiende 2 paquetes por orden de llegada. */
    private static void mostrarAtencionPorLlegada(CentroDistribucion centro, List<Paquete> enRuta) {
        titulo("12. Atencion por llegada");
        for (int i = 0; i < 2; i++) {
            Paquete paquete = centro.atenderSiguientePorLlegada();
            enRuta.add(paquete);
            System.out.println("Sale " + paquete + " -> " + centro.repartidorAsignado(paquete.getCodigo()));
        }
        System.out.println("Quedan " + centro.pendientesPorLlegada().size() + " paquetes pendientes");
    }

    /** 13. Intenta asignar a un repartidor no disponible y a uno de otra zona. */
    private static void mostrarAsignacionesRechazadas(CentroDistribucion centro) {
        titulo("13. Asignaciones rechazadas");

        try {
            centro.asignarManualmente("PQ942", "R02");
            System.out.println("ERROR: se asigno a un repartidor no disponible");
        } catch (IllegalStateException e) {
            System.out.println("PQ942 a R02 rechazado: " + e.getMessage());
        }

        try {
            centro.asignarManualmente("PQ942", "R05");
            System.out.println("ERROR: se asigno a un repartidor de otra zona");
        } catch (IllegalStateException e) {
            System.out.println("PQ942 a R05 rechazado: " + e.getMessage());
        }
    }

    /** 14. Entrega los 5 paquetes que salieron y recorre el historial con for-each. */
    private static void mostrarEntregas(CentroDistribucion centro, List<Paquete> enRuta) {
        titulo("14. Entregas");
        for (Paquete paquete : enRuta) {
            centro.registrarEntrega(paquete.getCodigo());
            System.out.println("Entregado " + paquete.getCodigo());
        }
        System.out.println("Historial: " + centro.getHistorial());
        System.out.println("Recorrido con for-each:");
        for (Paquete paquete : centro.getHistorial()) {
            System.out.println("  " + paquete);
        }
    }

    /** 15. Intenta entregar un paquete que no esta en ruta. */
    private static void mostrarEntregaRechazada(CentroDistribucion centro) {
        titulo("15. Entrega rechazada");
        try {
            centro.registrarEntrega("PQ942");
            System.out.println("ERROR: se entrego un paquete que no estaba en ruta");
        } catch (IllegalStateException e) {
            System.out.println("Entregar PQ942 rechazado: " + e.getMessage());
        }
    }

    // Imprime el titulo de cada seccion
    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    // Imprime los primeros elementos de una lista
    private static void mostrarPrimeros(List<Paquete> paquetes, int cantidad) {
        for (int i = 0; i < cantidad && i < paquetes.size(); i++) {
            System.out.println("  " + paquetes.get(i));
        }
    }
}
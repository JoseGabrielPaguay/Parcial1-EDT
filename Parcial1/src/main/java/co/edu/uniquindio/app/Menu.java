package co.edu.uniquindio.app;

import co.edu.uniquindio.datos.DatosIniciales;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.modelo.Repartidor;
import co.edu.uniquindio.servicio.CentroDistribucion;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

/**
 * Menu por consola de QuindioExpress.
 * Los datos siguen siendo quemados (se cargan desde DatosIniciales).
 * El Scanner solo se usa para leer el numero de la opcion que se elige.
 * Para mostrar la informacion se reutilizan los metodos que ya tiene Main.
 */
public class Menu {

    private static CentroDistribucion centro;

    // Paquetes que salieron a ruta, en el orden en que salieron.
    // Es una cola: al entregar, sale primero el que lleva mas tiempo en ruta.
    private static Queue<Paquete> enRuta;

    // Lee lo que se escribe en la consola (opciones, codigos, municipios...)
    private static Scanner lector;

    /** Carga los datos y muestra el menu hasta que se elija 0. */
    public static void iniciar() {
        lector = new Scanner(System.in);
        reiniciarDatos();

        int opcion;
        // Si en la pausa escriben un numero, se guarda aqui para usarlo como la siguiente opcion
        int siguienteOpcion = -1;
        do {
            if (siguienteOpcion == -1) {
                mostrarOpciones();
                opcion = leerOpcion(lector);
            } else {
                opcion = siguienteOpcion;
                siguienteOpcion = -1;
            }
            ejecutarOpcion(opcion);
            if (opcion != 0) {
                siguienteOpcion = pausa();
            }
        } while (opcion != 0);

        System.out.println("Fin del programa");
        lector.close();
    }

    // Espera un Enter para volver al menu.
    // Si en vez de solo Enter escriben un numero, se devuelve para usarlo como opcion.
    // Si no escriben nada (o escriben letras), devuelve -1 y se vuelve a mostrar el menu.
    private static int pausa() {
        System.out.println();
        System.out.print("Presione Enter para volver al menu (o escriba la siguiente opcion)...");
        String texto = lector.nextLine().trim();
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Crea un centro nuevo y vuelve a cargar los datos quemados
    private static void reiniciarDatos() {
        centro = new CentroDistribucion();
        enRuta = new ArrayDeque<>();
        Main.cargarDatos(centro);
    }

    private static void mostrarOpciones() {
        System.out.println();
        System.out.println("===== QuindioExpress =====");
        System.out.println("1. Ver paquetes en orden de registro");
        System.out.println("2. Consultar paquete por codigo");
        System.out.println("3. Ver municipios y paquetes por municipio");
        System.out.println("4. Ver ordenamientos (prioridad, peso, tiempo)");
        System.out.println("5. Analisis: recursion, busqueda binaria, divide y venceras");
        System.out.println("6. Despachar siguiente por prioridad");
        System.out.println("7. Atender siguiente por llegada");
        System.out.println("8. Registrar entrega");
        System.out.println("9. Ver historial de entregas");
        System.out.println("10. Ver validaciones rechazadas");
        System.out.println("11. Demo completa (todo en orden)");
        System.out.println("12. Reiniciar datos");
        System.out.println("0. Salir");
        System.out.print("Elija una opcion: ");
    }

    // Lee la opcion. Si no escriben un numero, devuelve -1 (opcion no valida).
    // Los Enter vacios se ignoran: asi un Enter de mas no cuenta como opcion.
    private static int leerOpcion(Scanner lector) {
        String texto = lector.nextLine().trim();
        while (texto.isEmpty()) {
            texto = lector.nextLine().trim();
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                Main.mostrarOrdenDeRegistro(centro);
                break;
            case 2:
                consultarPorCodigo();
                break;
            case 3:
                Main.mostrarMunicipios(centro);
                Main.mostrarAgrupacion(centro);
                break;
            case 4:
                Main.mostrarOrdenamientos(centro);
                break;
            case 5:
                analizar();
                break;
            case 6:
                despacharPorPrioridad();
                break;
            case 7:
                atenderPorLlegada();
                break;
            case 8:
                registrarEntrega();
                break;
            case 9:
                verHistorial();
                break;
            case 10:
                verValidaciones();
                break;
            case 11:
                // La demo usa su propio centro, asi no cambia lo que se lleva en el menu
                Main.demoCompleta();
                break;
            case 12:
                reiniciarDatos();
                System.out.println("Datos reiniciados");
                break;
            case 0:
                break;
            default:
                System.out.println("Opcion no valida");
        }
    }

    // Opcion 2: se escribe el codigo y se busca en el registro (HashMap)
    private static void consultarPorCodigo() {
        Main.titulo("Consulta por codigo");
        String codigo = leerTexto("Escriba el codigo del paquete (ej. PQ905): ").toUpperCase();
        Paquete paquete = centro.buscarPaquete(codigo);
        if (paquete == null) {
            System.out.println("No existe un paquete con el codigo " + codigo);
        } else {
            System.out.println(paquete);
        }
    }

    // Opcion 5: se escriben los datos para la recursion y la busqueda binaria
    private static void analizar() {
        Main.titulo("Recursion: peso total por municipio");
        String escrito = leerTexto("Escriba el municipio (ej. Armenia): ");
        String municipio = buscarMunicipio(escrito);
        if (municipio == null) {
            System.out.println("No hay paquetes para " + escrito + ". Peso total: 0.0 kg");
        } else {
            // Se redondea a un decimal para que no salga 45.10000000000001
            double peso = Math.round(centro.pesoTotalPorMunicipio(municipio) * 10) / 10.0;
            System.out.println("Peso total de " + municipio + ": " + peso + " kg");
        }

        Main.titulo("Recursion: paquetes con prioridad minima");
        int minima = leerNumero("Escriba la prioridad minima (" + Paquete.PRIORIDAD_MINIMA
                + " a " + Paquete.PRIORIDAD_MAXIMA + "): ");
        System.out.println("Paquetes con prioridad " + minima + " o mas: "
                + centro.contarConPrioridadMinima(minima));

        Main.titulo("Busqueda binaria por codigo");
        String codigo = leerTexto("Escriba el codigo a buscar (ej. PQ583): ").toUpperCase();
        Paquete encontrado = centro.buscarPorCodigoBinaria(codigo);
        if (encontrado == null) {
            System.out.println("No existe un paquete con el codigo " + codigo);
        } else {
            System.out.println("Encontrado: " + encontrado);
        }

        Main.mostrarDivideYVenceras(centro);
    }

    // Busca el municipio como esta guardado, sin importar mayusculas o minusculas.
    // Devuelve null si ningun paquete va a ese municipio.
    private static String buscarMunicipio(String escrito) {
        for (String municipio : centro.municipios()) {
            if (municipio.equalsIgnoreCase(escrito)) {
                return municipio;
            }
        }
        return null;
    }

    // Pide un texto. Si se deja vacio, lo vuelve a pedir.
    private static String leerTexto(String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = lector.nextLine().trim();
        }
        return texto;
    }

    // Pide un numero entero. Si se escriben letras, lo vuelve a pedir.
    private static int leerNumero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un numero");
            }
        }
    }

    // Opcion 6: saca un solo paquete de la cola de prioridad
    private static void despacharPorPrioridad() {
        Main.titulo("Despacho por prioridad");
        try {
            Paquete paquete = centro.despacharSiguientePorPrioridad();
            if (paquete == null) {
                System.out.println("No hay paquetes pendientes");
            } else {
                enRuta.add(paquete);
                System.out.println("Sale " + paquete + " -> " + centro.repartidorAsignado(paquete.getCodigo()));
            }
        } catch (IllegalStateException e) {
            // Pasa si no hay repartidor disponible en la zona del paquete
            System.out.println("No se pudo despachar: " + e.getMessage());
        }
        mostrarEstado();
    }

    // Opcion 7: saca un solo paquete por orden de llegada
    private static void atenderPorLlegada() {
        Main.titulo("Atencion por llegada");
        try {
            Paquete paquete = centro.atenderSiguientePorLlegada();
            if (paquete == null) {
                System.out.println("No hay paquetes pendientes");
            } else {
                enRuta.add(paquete);
                System.out.println("Sale " + paquete + " -> " + centro.repartidorAsignado(paquete.getCodigo()));
            }
        } catch (IllegalStateException e) {
            System.out.println("No se pudo atender: " + e.getMessage());
        }
        mostrarEstado();
    }

    // Opcion 8: entrega el paquete que lleva mas tiempo en ruta
    private static void registrarEntrega() {
        Main.titulo("Registrar entrega");
        if (enRuta.isEmpty()) {
            System.out.println("No hay paquetes en ruta");
        } else {
            Paquete paquete = enRuta.poll();
            Repartidor repartidor = centro.repartidorAsignado(paquete.getCodigo());
            centro.registrarEntrega(paquete.getCodigo());
            System.out.println("Entregado " + paquete.getCodigo() + " (" + repartidor.getIdentificacion()
                    + " " + repartidor.getNombre() + " queda disponible otra vez)");
        }
        mostrarEstado();
    }

    // Opcion 9: muestra el historial con toString y con for-each
    private static void verHistorial() {
        Main.titulo("Historial de entregas");
        System.out.println("Historial: " + centro.getHistorial());
        if (!centro.getHistorial().estaVacio()) {
            System.out.println("Recorrido con for-each:");
            for (Paquete paquete : centro.getHistorial()) {
                System.out.println("  " + paquete);
            }
        }
    }

    // Opcion 10: muestra los rechazos sin afectar lo que se lleva en el menu
    private static void verValidaciones() {
        Main.mostrarValidaciones(centro);
        Main.mostrarAsignacionesRechazadas(centro);
        Main.titulo("Entrega rechazada");
        try {
            centro.registrarEntrega("PQ000");
            System.out.println("ERROR: se entrego un paquete que no estaba en ruta");
        } catch (IllegalStateException e) {
            System.out.println("Entregar PQ000 rechazado: " + e.getMessage());
        }
    }

    // Resumen corto de como van los paquetes
    private static void mostrarEstado() {
        System.out.println("Pendientes: " + centro.pendientesPorLlegada().size()
                + " | En ruta: " + enRuta.size()
                + " | Entregados: " + centro.getHistorial().cantidad());
    }
}
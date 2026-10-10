package co.edu.uniquindio.prueba;

import co.edu.uniquindio.comparadores.ComparadoresPaquete;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.util.Prueba;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Pruebas de los comparadores de paquetes. */
public class PruebaComparadores {

    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    public static void ejecutar() {
        Prueba.titulo("ComparadoresPaquete");
        pruebaDespacho();
        pruebaDesempateDespacho();
        pruebaPorPeso();
        pruebaPorPrioridad();
        pruebaPorTiempo();
        pruebaOrdenNaturalNoCambia();
    }

    // Atajo para crear un paquete de prueba
    private static Paquete p(String codigo, double peso, int prioridad, int tiempo) {
        return new Paquete(codigo, "Armenia", peso, prioridad, tiempo);
    }

    private static String codigos(List<Paquete> lista) {
        StringBuilder sb = new StringBuilder();
        for (Paquete paquete : lista) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(paquete.getCodigo());
        }
        return sb.toString();
    }

    private static void pruebaDespacho() {
        List<Paquete> lista = new ArrayList<>();
        lista.add(p("PQ420", 2.0, 3, 30));
        lista.add(p("PQ731", 2.0, 5, 45));
        lista.add(p("PQ105", 2.0, 5, 25));
        lista.add(p("PQ942", 2.0, 4, 20));
        lista.sort(ComparadoresPaquete.DESPACHO);
        Prueba.verificar(codigos(lista).equals("PQ105,PQ731,PQ942,PQ420"),
                "DESPACHO ordena PQ105, PQ731, PQ942, PQ420");
    }

    private static void pruebaDesempateDespacho() {
        List<Paquete> lista = new ArrayList<>();
        lista.add(p("PQ900", 1.0, 4, 20));
        lista.add(p("PQ200", 1.0, 4, 20));
        lista.sort(ComparadoresPaquete.DESPACHO);
        Prueba.verificar(codigos(lista).equals("PQ200,PQ900"),
                "DESPACHO con todo igual desempata por codigo (PQ200 antes de PQ900)");
    }

    private static void pruebaPorPeso() {
        List<Paquete> lista = new ArrayList<>();
        lista.add(p("PQ101", 4.5, 3, 10));
        lista.add(p("PQ102", 12.0, 3, 10));
        lista.add(p("PQ103", 3.2, 3, 10));
        lista.add(p("PQ104", 4.6, 3, 10));
        lista.sort(ComparadoresPaquete.POR_PESO_DESC);
        Prueba.verificar(codigos(lista).equals("PQ102,PQ104,PQ101,PQ103"),
                "POR_PESO_DESC deja 12.0, 4.6, 4.5, 3.2");
    }

    private static void pruebaPorPrioridad() {
        List<Paquete> lista = new ArrayList<>();
        lista.add(p("PQ101", 1.0, 2, 10));
        lista.add(p("PQ102", 1.0, 5, 10));
        lista.add(p("PQ103", 1.0, 4, 10));
        lista.sort(ComparadoresPaquete.POR_PRIORIDAD_DESC);
        Prueba.verificar(codigos(lista).equals("PQ102,PQ103,PQ101"),
                "POR_PRIORIDAD_DESC pone la mayor prioridad primero");
    }

    private static void pruebaPorTiempo() {
        List<Paquete> lista = new ArrayList<>();
        lista.add(p("PQ101", 1.0, 3, 45));
        lista.add(p("PQ102", 1.0, 3, 10));
        lista.add(p("PQ103", 1.0, 3, 30));
        lista.sort(ComparadoresPaquete.POR_TIEMPO_ASC);
        Prueba.verificar(codigos(lista).equals("PQ102,PQ103,PQ101"),
                "POR_TIEMPO_ASC pone el menor tiempo primero");
    }

    private static void pruebaOrdenNaturalNoCambia() {
        List<Paquete> lista = new ArrayList<>();
        lista.add(p("PQ731", 4.5, 3, 10));
        lista.add(p("PQ105", 12.0, 3, 10));
        lista.add(p("PQ942", 3.2, 3, 10));
        lista.sort(ComparadoresPaquete.POR_PESO_DESC);
        Collections.sort(lista); // sin comparador: usa el orden natural (codigo)
        Prueba.verificar(codigos(lista).equals("PQ105,PQ731,PQ942"),
                "Despues de ordenar por peso, Collections.sort sigue ordenando por codigo");
    }
}
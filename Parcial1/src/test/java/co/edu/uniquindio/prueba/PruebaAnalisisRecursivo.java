package co.edu.uniquindio.prueba;

import co.edu.uniquindio.algoritmos.AnalisisRecursivo;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.util.Prueba;

import java.util.ArrayList;
import java.util.List;

/** Pruebas de los metodos recursivos. */
public class PruebaAnalisisRecursivo {

    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    public static void ejecutar() {
        Prueba.titulo("AnalisisRecursivo");
        pruebaPesoTotal();
        pruebaPrioridadMinima();
        pruebaErrores();
    }

    private static Paquete p(String codigo, String destino, double peso, int prioridad) {
        return new Paquete(codigo, destino, peso, prioridad, 10);
    }

    private static boolean casiIgual(double a, double b) {
        return Math.abs(a - b) < 0.0001;
    }

    private static void pruebaPesoTotal() {
        List<Paquete> lista = new ArrayList<>();
        lista.add(p("PQ101", "Armenia", 4.5, 3));
        lista.add(p("PQ203", "Salento", 3.0, 3));
        lista.add(p("PQ415", "Armenia", 7.2, 3));
        lista.add(p("PQ730", "Armenia", 2.3, 3));
        Prueba.verificar(casiIgual(AnalisisRecursivo.pesoTotalPorMunicipio(lista, "Armenia"), 14.0),
                "Peso total de Armenia es 14.0");
        Prueba.verificar(casiIgual(AnalisisRecursivo.pesoTotalPorMunicipio(lista, "Salento"), 3.0),
                "Peso total de Salento es 3.0");
        Prueba.verificar(casiIgual(AnalisisRecursivo.pesoTotalPorMunicipio(lista, "Filandia"), 0.0),
                "Municipio sin paquetes da 0.0");
        Prueba.verificar(casiIgual(AnalisisRecursivo.pesoTotalPorMunicipio(new ArrayList<>(), "Armenia"), 0.0),
                "Lista vacia da 0.0");
    }

    private static void pruebaPrioridadMinima() {
        int[] prioridades = {5, 2, 4, 1, 5, 3, 4};
        List<Paquete> lista = new ArrayList<>();
        for (int i = 0; i < prioridades.length; i++) {
            lista.add(p("PQ" + (100 + i), "Armenia", 1.0, prioridades[i]));
        }
        Prueba.verificar(AnalisisRecursivo.contarConPrioridadMinima(lista, 4) == 4,
                "Prioridades 5,2,4,1,5,3,4 con minimo 4 da 4");
        Prueba.verificar(AnalisisRecursivo.contarConPrioridadMinima(lista, Paquete.PRIORIDAD_MINIMA) == 7,
                "Con la prioridad minima permitida cuentan los 7");
        Prueba.verificar(AnalisisRecursivo.contarConPrioridadMinima(new ArrayList<>(), 3) == 0,
                "Lista vacia da 0");
    }

    private static void pruebaErrores() {
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> AnalisisRecursivo.pesoTotalPorMunicipio(null, "Armenia"),
                "Lista nula en pesoTotalPorMunicipio da error");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> AnalisisRecursivo.pesoTotalPorMunicipio(new ArrayList<>(), null),
                "Municipio nulo da error");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> AnalisisRecursivo.contarConPrioridadMinima(null, 3),
                "Lista nula en contarConPrioridadMinima da error");
    }
}
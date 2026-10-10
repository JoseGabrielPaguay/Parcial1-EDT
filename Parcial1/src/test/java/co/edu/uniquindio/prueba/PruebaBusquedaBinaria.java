package co.edu.uniquindio.prueba;

import co.edu.uniquindio.algoritmos.BusquedaBinaria;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.util.Prueba;

import java.util.ArrayList;
import java.util.List;

/** Pruebas de la busqueda binaria por codigo. */
public class PruebaBusquedaBinaria {

    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    public static void ejecutar() {
        Prueba.titulo("BusquedaBinaria");
        pruebaListaOrdenada();
        pruebaCasosLimite();
        pruebaListaDesordenada();
        pruebaErrores();
    }

    private static List<Paquete> crearLista(String... codigos) {
        List<Paquete> lista = new ArrayList<>();
        for (String codigo : codigos) {
            lista.add(new Paquete(codigo, "Armenia", 1.0, 3, 10));
        }
        return lista;
    }

    private static void pruebaListaOrdenada() {
        List<Paquete> lista = crearLista("PQ105", "PQ214", "PQ318", "PQ421",
                "PQ567", "PQ731", "PQ842", "PQ942");
        Prueba.verificar(BusquedaBinaria.buscarPosicion(lista, "PQ731") == 5, "PQ731 esta en la posicion 5");
        Prueba.verificar(BusquedaBinaria.buscarPosicion(lista, "PQ105") == 0, "El primero esta en la posicion 0");
        Prueba.verificar(BusquedaBinaria.buscarPosicion(lista, "PQ942") == 7, "El ultimo esta en la posicion 7");
        Prueba.verificar(BusquedaBinaria.buscarPosicion(lista, "PQ500") == -1, "PQ500 no existe: da -1");
        Prueba.verificar(BusquedaBinaria.buscar(lista, "PQ318").getCodigo().equals("PQ318"),
                "buscar devuelve el paquete PQ318");
        Prueba.verificar(BusquedaBinaria.buscar(lista, "PQ500") == null, "buscar devuelve null si no existe");
    }

    private static void pruebaCasosLimite() {
        Prueba.verificar(BusquedaBinaria.buscarPosicion(new ArrayList<>(), "PQ105") == -1,
                "Lista vacia da -1");
        List<Paquete> uno = crearLista("PQ105");
        Prueba.verificar(BusquedaBinaria.buscarPosicion(uno, "PQ105") == 0, "Un elemento: lo encuentra");
        Prueba.verificar(BusquedaBinaria.buscarPosicion(uno, "PQ999") == -1, "Un elemento: no encuentra otro");
    }

    private static void pruebaListaDesordenada() {
        // Muestra la precondicion: sin orden, la busqueda binaria falla aunque el codigo si este
        List<Paquete> desordenada = crearLista("PQ942", "PQ105", "PQ731");
        Prueba.verificar(BusquedaBinaria.buscarPosicion(desordenada, "PQ942") == -1,
                "Lista desordenada: PQ942 no se encuentra aunque esta (hay que ordenar antes)");
    }

    private static void pruebaErrores() {
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> BusquedaBinaria.buscarPosicion(null, "PQ105"),
                "Lista nula da error");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> BusquedaBinaria.buscarPosicion(new ArrayList<>(), null),
                "Codigo nulo da error");
    }
}
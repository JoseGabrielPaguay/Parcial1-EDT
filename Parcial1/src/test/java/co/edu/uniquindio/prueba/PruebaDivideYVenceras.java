package co.edu.uniquindio.prueba;

import co.edu.uniquindio.algoritmos.DivideYVenceras;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.util.Prueba;

import java.util.ArrayList;
import java.util.List;

/** Pruebas del algoritmo divide y venceras (paquete mas pesado). */
public class PruebaDivideYVenceras {

    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    public static void ejecutar() {
        Prueba.titulo("DivideYVenceras");
        pruebaEjemploDelEnunciado();
        pruebaPosicionesDelMaximo();
        pruebaCasosLimite();
    }

    private static List<Paquete> crearLista(double... pesos) {
        List<Paquete> lista = new ArrayList<>();
        for (int i = 0; i < pesos.length; i++) {
            lista.add(new Paquete("PQ" + (100 + i), "Armenia", pesos[i], 3, 10));
        }
        return lista;
    }

    private static void pruebaEjemploDelEnunciado() {
        List<Paquete> lista = crearLista(4.5, 12.0, 3.2, 18.7, 7.4, 5.1);
        Prueba.verificar(DivideYVenceras.paqueteMasPesado(lista).getPeso() == 18.7,
                "Pesos 4.5, 12.0, 3.2, 18.7, 7.4, 5.1: el mas pesado es 18.7");
    }

    private static void pruebaPosicionesDelMaximo() {
        Prueba.verificar(DivideYVenceras.paqueteMasPesado(crearLista(20.0, 1.0, 2.0, 3.0)).getPeso() == 20.0,
                "El maximo al inicio se encuentra");
        Prueba.verificar(DivideYVenceras.paqueteMasPesado(crearLista(1.0, 2.0, 3.0, 20.0)).getPeso() == 20.0,
                "El maximo al final se encuentra");
        Prueba.verificar(DivideYVenceras.paqueteMasPesado(crearLista(1.0, 2.0, 3.0, 4.0, 5.0)).getPeso() == 5.0,
                "Con cantidad impar de paquetes tambien funciona");
        // Empate: gana el de la izquierda (PQ100 va antes que PQ101)
        Paquete empate = DivideYVenceras.paqueteMasPesado(crearLista(9.0, 9.0, 1.0));
        Prueba.verificar(empate.getCodigo().equals("PQ100"), "Si empatan, gana el de la izquierda");
    }

    private static void pruebaCasosLimite() {
        Prueba.verificar(DivideYVenceras.paqueteMasPesado(new ArrayList<>()) == null,
                "Lista vacia devuelve null");
        List<Paquete> uno = crearLista(7.5);
        Prueba.verificar(DivideYVenceras.paqueteMasPesado(uno).getPeso() == 7.5,
                "Un solo elemento: es el mas pesado");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> DivideYVenceras.paqueteMasPesado(null),
                "Lista nula da error");
    }
}

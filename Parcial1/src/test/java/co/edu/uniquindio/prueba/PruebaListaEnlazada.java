package co.edu.uniquindio.prueba;

import co.edu.uniquindio.estructuras.ListaSimplementeEnlazada;
import java.util.Iterator;
import java.util.NoSuchElementException;
import co.edu.uniquindio.util.Prueba;

/**
 * Pruebas de la lista simplemente enlazada.
 * Revisamos los casos normales y sobre todo los casos limite
 * (lista vacia, un solo elemento, borrar el primero, el del medio y el ultimo).
 */
public class PruebaListaEnlazada {

    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    public static void ejecutar() {
        Prueba.titulo("Lista simplemente enlazada");

        // 1. Lista nueva
        ListaSimplementeEnlazada<String> vacia = new ListaSimplementeEnlazada<>();
        Prueba.verificar(vacia.estaVacia(), "Una lista nueva esta vacia");
        Prueba.verificar(vacia.tamanio() == 0, "Una lista nueva tiene tamanio 0");
        Prueba.esperarExcepcion(IndexOutOfBoundsException.class,
                () -> vacia.obtener(0), "obtener(0) en lista vacia lanza excepcion");
        Prueba.verificar(!vacia.eliminar("x"), "eliminar en lista vacia devuelve false");

        // 2. Un solo elemento: agregar y eliminar, y luego volver a agregar
        ListaSimplementeEnlazada<String> uno = new ListaSimplementeEnlazada<>();
        uno.agregar("A");
        uno.eliminar("A");
        Prueba.verificar(uno.estaVacia(), "Al agregar A y eliminar A la lista queda vacia");
        uno.agregar("B");
        Prueba.verificar(uno.obtener(0).equals("B") && uno.tamanio() == 1,
                "Despues de quedar vacia se puede agregar B y obtener(0) es B");

        // 3. Eliminar el primero, uno del medio, el ultimo y uno que no existe
        ListaSimplementeEnlazada<String> lista = new ListaSimplementeEnlazada<>();
        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");
        lista.agregar("D");

        lista.eliminar("A");
        Prueba.verificar(lista.toString().equals("[B -> C -> D]"),
                "Eliminar el primero (A) deja [B -> C -> D]");

        lista.eliminar("C");
        Prueba.verificar(lista.toString().equals("[B -> D]"),
                "Eliminar uno del medio (C) deja [B -> D]");

        lista.eliminar("D");
        lista.agregar("E");
        Prueba.verificar(lista.toString().equals("[B -> E]"),
                "Eliminar el ultimo (D) y agregar E deja [B -> E] (la cola quedo bien)");

        int tamanioAntes = lista.tamanio();
        Prueba.verificar(!lista.eliminar("Z"), "Eliminar Z (no existe) devuelve false");
        Prueba.verificar(lista.tamanio() == tamanioAntes, "Al no eliminar nada el tamanio no cambia");

        // 4. Posiciones invalidas
        Prueba.esperarExcepcion(IndexOutOfBoundsException.class,
                () -> lista.obtener(-1), "obtener(-1) lanza IndexOutOfBoundsException");
        Prueba.esperarExcepcion(IndexOutOfBoundsException.class,
                () -> lista.obtener(lista.tamanio()), "obtener(tamanio()) lanza IndexOutOfBoundsException");

        // 5. El for-each recorre en el mismo orden en que se agrego
        ListaSimplementeEnlazada<String> orden = new ListaSimplementeEnlazada<>();
        orden.agregar("X");
        orden.agregar("Y");
        orden.agregar("Z");
        String recorrido = "";
        for (String elemento : orden) {
            recorrido = recorrido + elemento;
        }
        Prueba.verificar(recorrido.equals("XYZ"), "El for-each recorre en el orden en que se agregaron");

        // 6. next() cuando ya no hay mas elementos
        Iterator<String> iterador = orden.iterator();
        iterador.next();
        iterador.next();
        iterador.next();
        Prueba.verificar(!iterador.hasNext(), "Despues del ultimo, hasNext() es false");
        Prueba.esperarExcepcion(NoSuchElementException.class,
                () -> iterador.next(), "next() sin mas elementos lanza NoSuchElementException");

        // 7. Tambien funciona con numeros (es generica)
        ListaSimplementeEnlazada<Integer> numeros = new ListaSimplementeEnlazada<>();
        numeros.agregar(10);
        numeros.agregar(20);
        numeros.agregar(30);
        numeros.eliminar(20);
        Prueba.verificar(numeros.toString().equals("[10 -> 30]") && numeros.obtener(1) == 30,
                "La lista tambien funciona con Integer");

        // 8. No se puede agregar null
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> lista.agregar(null), "agregar(null) lanza IllegalArgumentException");
    }
}

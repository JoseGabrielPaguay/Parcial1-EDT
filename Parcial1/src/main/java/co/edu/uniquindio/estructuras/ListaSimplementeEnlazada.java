package co.edu.uniquindio.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista simplemente enlazada hecha por nosotros, sin usar las listas de Java.
 *
 * Cada nodo guarda un valor y la referencia al siguiente nodo.
 * Guardamos la cabeza (el primero) y la cola (el ultimo).
 * Tener la cola nos sirve para agregar al final sin tener que recorrer
 * toda la lista.
 */
public class ListaSimplementeEnlazada<T> implements ColeccionGenerica<T> {

    // Clase Nodo: es privada para que nadie de afuera pueda tocar los nodos
    private static class Nodo<T> {
        private T valor;
        private Nodo<T> siguiente;

        Nodo(T valor) {
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public ListaSimplementeEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     * Como tenemos la cola, no hay que recorrer nada, por eso es O(1).
     */
    @Override
    public void agregar(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("No se puede agregar un elemento nulo");
        }

        Nodo<T> nuevo = new Nodo<>(elemento);

        if (estaVacia()) {
            // Si la lista esta vacia, el nuevo nodo es el primero y el ultimo
            cabeza = nuevo;
            cola = nuevo;
        } else {
            // Si no, lo pegamos despues de la cola y ahora el es la nueva cola
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamanio++;
    }

    /**
     * Elimina el primer elemento que sea igual al que nos pasan.
     * Hay que tener cuidado con la cabeza y la cola para que no queden mal.
     * En el peor caso recorre toda la lista, por eso es O(n).
     */
    @Override
    public boolean eliminar(T elemento) {
        // Caso 1: la lista esta vacia, no hay nada que eliminar
        if (estaVacia()) {
            return false;
        }

        // Caso 2: el elemento esta en la cabeza
        if (cabeza.valor.equals(elemento)) {
            cabeza = cabeza.siguiente;
            // Si era el unico, la lista queda vacia y la cola tambien se borra
            if (cabeza == null) {
                cola = null;
            }
            tamanio--;
            return true;
        }

        // Caso 3: buscamos en el resto con dos referencias, anterior y actual
        Nodo<T> anterior = cabeza;
        Nodo<T> actual = cabeza.siguiente;

        while (actual != null) {
            if (actual.valor.equals(elemento)) {
                // Saltamos el nodo actual para sacarlo de la lista
                anterior.siguiente = actual.siguiente;
                // Si el que borramos era la cola, la nueva cola es el anterior
                if (actual == cola) {
                    cola = anterior;
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }

        // Caso 4: recorrimos todo y no estaba
        return false;
    }

    /**
     * Devuelve el elemento de una posicion (desde 0).
     * Hay que avanzar nodo por nodo hasta llegar, por eso es O(n).
     */
    @Override
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException("Posicion invalida: " + posicion
                    + ". La lista tiene " + tamanio + " elementos");
        }

        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    /**
     * Devuelve nuestro propio iterador. Gracias a esto se puede usar
     * el for-each con la lista, sin que el codigo de afuera vea los nodos.
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorLista();
    }

    /**
     * Muestra la lista asi: [A -> B -> C]. Si esta vacia muestra [].
     * Usa el for-each sobre la misma lista, o sea, usa nuestro iterador.
     */
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder("[");
        boolean primero = true;

        for (T elemento : this) {
            if (!primero) {
                texto.append(" -> ");
            }
            texto.append(elemento);
            primero = false;
        }

        texto.append("]");
        return texto.toString();
    }

    /**
     * Iterador de la lista: va pasando nodo por nodo desde la cabeza.
     * No le hicimos remove() porque no lo necesitamos
     * (Java ya lanza UnsupportedOperationException si alguien lo llama).
     */
    private class IteradorLista implements Iterator<T> {

        private Nodo<T> actual = cabeza;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Ya no hay mas elementos en la lista");
            }
            T valor = actual.valor;
            actual = actual.siguiente;
            return valor;
        }
    }
}

package co.edu.uniquindio.estructuras;

/**
 * Esta interfaz es el contrato de nuestras colecciones.
 * Aqui solo decimos que se puede hacer (agregar, eliminar, obtener...),
 * y la clase que la implemente decide como lo hace.
 *
 * La T quiere decir que es generica: sirve para guardar cualquier tipo
 * de dato (String, Integer, Paquete, etc.).
 *
 * Extiende de Iterable para que se pueda recorrer con un for-each.
 */
public interface ColeccionGenerica<T> extends Iterable<T> {

    // Agrega el elemento al final
    void agregar(T elemento);

    // Elimina el primer elemento que sea igual (equals).
    // Si no lo encuentra devuelve false
    boolean eliminar(T elemento);

    // Devuelve el elemento que esta en esa posicion (empieza desde 0)
    T obtener(int posicion);

    // Devuelve cuantos elementos hay
    int tamanio();

    // Devuelve true si no hay ningun elemento
    boolean estaVacia();
}

package co.edu.uniquindio.modelo;

/**
 * Un repartidor que entrega paquetes en una zona.
 * Lo unico que cambia despues de creado es si esta disponible.
 */
public class Repartidor {

    private final String identificacion;
    private final String nombre;
    private final String zona;
    private boolean disponible;

    /** Crea un repartidor disponible. */
    public Repartidor(String identificacion, String nombre, String zona) {
        this(identificacion, nombre, zona, true);
    }

    /** Crea un repartidor indicando si esta disponible; valida que los textos no esten vacios. */
    public Repartidor(String identificacion, String nombre, String zona, boolean disponible) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificacion no puede ser nula ni estar vacia");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni estar vacio");
        }
        if (zona == null || zona.isBlank()) {
            throw new IllegalArgumentException("La zona no puede ser nula ni estar vacia");
        }
        this.identificacion = identificacion.trim();
        this.nombre = nombre.trim();
        this.zona = zona.trim();
        this.disponible = disponible;
    }

    /** Devuelve la identificacion unica del repartidor. */
    public String getIdentificacion() { return identificacion; }

    /** Devuelve el nombre del repartidor. */
    public String getNombre() { return nombre; }

    /** Devuelve la zona donde reparte. */
    public String getZona() { return zona; }

    /** Dice si puede recibir un paquete nuevo. */
    public boolean isDisponible() { return disponible; }

    /** Lo deja ocupado (no disponible). */
    public void marcarOcupado() { disponible = false; }

    /** Lo deja disponible otra vez. */
    public void marcarDisponible() { disponible = true; }

    /** Dos repartidores son iguales si tienen la misma identificacion. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Repartidor)) return false;
        Repartidor otro = (Repartidor) o;
        return identificacion.equals(otro.identificacion);
    }

    /** Se calcula solo con la identificacion, igual que equals. */
    @Override
    public int hashCode() {
        return identificacion.hashCode();
    }

    /** Ejemplo: R1 [Ana, zona Armenia, disponible]. */
    @Override
    public String toString() {
        return identificacion + " [" + nombre + ", zona " + zona
                + (disponible ? ", disponible" : ", ocupado") + "]";
    }
}
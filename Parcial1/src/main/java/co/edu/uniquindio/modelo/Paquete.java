package co.edu.uniquindio.modelo;

/**
 * Un paquete que llega al centro de distribucion.
 * No tiene setters: una vez creado, no cambia.
 */
public class Paquete implements Comparable<Paquete> {

    // PENDIENTE de confirmar con el profesor (0-10 o 1-5). Por ahora 1-5.
    public static final int PRIORIDAD_MINIMA = 1;
    public static final int PRIORIDAD_MAXIMA = 5;
    public static final int PRIORIDAD_POR_DEFECTO = 5;

    private final String codigo;
    private final String destino;
    private final double peso;
    private final int prioridad;
    private final int tiempoEstimado;

    /** Crea un paquete validando todos los datos; si algo esta mal lanza IllegalArgumentException. */
    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El codigo no puede ser nulo ni estar vacio");
        }
        if (destino == null || destino.isBlank()) {
            throw new IllegalArgumentException("El destino no puede ser nulo ni estar vacio");
        }
        if (Double.isNaN(peso) || peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero");
        }
        if (prioridad < PRIORIDAD_MINIMA || prioridad > PRIORIDAD_MAXIMA) {
            throw new IllegalArgumentException("La prioridad debe estar entre "
                    + PRIORIDAD_MINIMA + " y " + PRIORIDAD_MAXIMA);
        }
        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor que cero");
        }
        this.codigo = codigo.trim();
        this.destino = destino.trim();
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    /** Crea un paquete con la prioridad por defecto. */
    public Paquete(String codigo, String destino, double peso, int tiempoEstimado) {
        this(codigo, destino, peso, PRIORIDAD_POR_DEFECTO, tiempoEstimado);
    }

    /** Devuelve el codigo del paquete. */
    public String getCodigo() { return codigo; }

    /** Devuelve el municipio de destino. */
    public String getDestino() { return destino; }

    /** Devuelve el peso en kilogramos. */
    public double getPeso() { return peso; }

    /** Devuelve la prioridad del paquete. */
    public int getPrioridad() { return prioridad; }

    /** Devuelve los minutos estimados de entrega. */
    public int getTiempoEstimado() { return tiempoEstimado; }

    /** Orden natural: solo por codigo, de menor a mayor. */
    @Override
    public int compareTo(Paquete otro) {
        return codigo.compareTo(otro.codigo);
    }

    /** Dos paquetes son iguales si tienen el mismo codigo. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paquete)) return false;
        Paquete otro = (Paquete) o;
        return codigo.equals(otro.codigo);
    }

    /** Se calcula solo con el codigo, igual que equals. */
    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    /** Ejemplo: PQ105 [Salento, 3.0 kg, prioridad 5, 25 min]. */
    @Override
    public String toString() {
        return codigo + " [" + destino + ", " + peso + " kg, prioridad "
                + prioridad + ", " + tiempoEstimado + " min]";
    }
}
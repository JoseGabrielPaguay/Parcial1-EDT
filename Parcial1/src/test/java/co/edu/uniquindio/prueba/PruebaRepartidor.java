package co.edu.uniquindio.prueba;

import co.edu.uniquindio.modelo.Repartidor;
import co.edu.uniquindio.util.Prueba;

/**
 * Pruebas de la clase Repartidor: datos, disponibilidad, equals y validaciones.
 */
public class PruebaRepartidor {

    /** Corre las pruebas y muestra el resumen. */
    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    /** Contiene todos los casos de prueba de Repartidor. */
    public static void ejecutar() {
        Prueba.titulo("Repartidor");

        Repartidor r = new Repartidor("R1", "Ana", "Armenia");
        Prueba.verificar(r.getIdentificacion().equals("R1"), "getIdentificacion devuelve R1");
        Prueba.verificar(r.getNombre().equals("Ana"), "getNombre devuelve Ana");
        Prueba.verificar(r.getZona().equals("Armenia"), "getZona devuelve Armenia");
        Prueba.verificar(r.isDisponible(), "arranca disponible");

        r.marcarOcupado();
        Prueba.verificar(!r.isDisponible(), "despues de marcarOcupado no esta disponible");
        r.marcarDisponible();
        Prueba.verificar(r.isDisponible(), "despues de marcarDisponible esta disponible otra vez");

        Repartidor ocupado = new Repartidor("R2", "Luis", "Salento", false);
        Prueba.verificar(!ocupado.isDisponible(), "el constructor de 4 parametros respeta disponible=false");

        Repartidor conEspacios = new Repartidor(" R3 ", " Marta ", " Armenia ");
        Prueba.verificar(conEspacios.getIdentificacion().equals("R3"), "la identificacion se guarda con trim");
        Prueba.verificar(conEspacios.getNombre().equals("Marta"), "el nombre se guarda con trim");
        Prueba.verificar(conEspacios.getZona().equals("Armenia"), "la zona se guarda con trim");

        Prueba.verificar(new Repartidor("R1", "Otro", "Calarca").equals(r),
                "equals compara solo por identificacion");
        Prueba.verificar(new Repartidor("R1", "Otro", "Calarca").hashCode() == r.hashCode(),
                "hashCode depende solo de la identificacion");

        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Repartidor("R4", "", "Armenia"), "nombre vacio");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Repartidor(null, "Pedro", "Calarca"), "identificacion null");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Repartidor("R5", "Pedro", "   "), "zona en blanco");
    }
}
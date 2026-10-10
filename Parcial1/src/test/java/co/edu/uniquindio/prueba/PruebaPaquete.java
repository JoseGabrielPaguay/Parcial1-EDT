package co.edu.uniquindio.prueba;

import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.util.Prueba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Pruebas de la clase Paquete: validaciones, orden natural, equals y hashCode.
 */
public class PruebaPaquete {

    /** Corre las pruebas y muestra el resumen. */
    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    /** Contiene todos los casos de prueba de Paquete. */
    public static void ejecutar() {
        Prueba.titulo("Paquete");

        // 1. Paquete valido
        Paquete p = new Paquete("PQ105", "Salento", 3.0, 5, 25);
        Prueba.verificar(p.getCodigo().equals("PQ105"), "getCodigo devuelve PQ105");
        Prueba.verificar(p.getDestino().equals("Salento"), "getDestino devuelve Salento");
        Prueba.verificar(p.getPeso() == 3.0, "getPeso devuelve 3.0");
        Prueba.verificar(p.getPrioridad() == 5, "getPrioridad devuelve 5");
        Prueba.verificar(p.getTiempoEstimado() == 25, "getTiempoEstimado devuelve 25");
        Prueba.verificar(p.toString().equals("PQ105 [Salento, 3.0 kg, prioridad 5, 25 min]"),
                "toString tiene el formato esperado");

        // Los textos se guardan sin espacios sobrantes
        Paquete conEspacios = new Paquete("  PQ200 ", " Armenia ", 1.0, 3, 10);
        Prueba.verificar(conEspacios.getCodigo().equals("PQ200"), "el codigo se guarda con trim");
        Prueba.verificar(conEspacios.getDestino().equals("Armenia"), "el destino se guarda con trim");

        // 2. Constructor de 4 parametros
        Paquete sinPrioridad = new Paquete("PQ300", "Calarca", 2.0, 15);
        Prueba.verificar(sinPrioridad.getPrioridad() == Paquete.PRIORIDAD_POR_DEFECTO,
                "el constructor de 4 parametros usa la prioridad por defecto");

        // 3. Datos invalidos
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete("PQ1", "Armenia", 0, 3, 10), "peso 0");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete("PQ1", "Armenia", -1, 3, 10), "peso -1");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete("PQ1", "Armenia", Double.NaN, 3, 10), "peso NaN");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete("PQ1", "Armenia", 1.0, Paquete.PRIORIDAD_MINIMA - 1, 10),
                "prioridad por debajo del minimo");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete("PQ1", "Armenia", 1.0, Paquete.PRIORIDAD_MAXIMA + 1, 10),
                "prioridad por encima del maximo");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete("PQ1", "Armenia", 1.0, 3, 0), "tiempo 0");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete("PQ1", "", 1.0, 3, 10), "destino vacio");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete("PQ1", null, 1.0, 3, 10), "destino null");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete(null, "Armenia", 1.0, 3, 10), "codigo null");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> new Paquete("   ", "Armenia", 1.0, 3, 10), "codigo en blanco");

        // 4. Limites aceptados
        Paquete minima = new Paquete("PQ400", "Armenia", 1.0, Paquete.PRIORIDAD_MINIMA, 10);
        Paquete maxima = new Paquete("PQ401", "Armenia", 1.0, Paquete.PRIORIDAD_MAXIMA, 10);
        Prueba.verificar(minima.getPrioridad() == Paquete.PRIORIDAD_MINIMA, "se acepta la prioridad minima");
        Prueba.verificar(maxima.getPrioridad() == Paquete.PRIORIDAD_MAXIMA, "se acepta la prioridad maxima");

        // 5. Orden natural por codigo
        List<Paquete> lista = new ArrayList<>();
        lista.add(new Paquete("PQ731", "Armenia", 1.0, 3, 10));
        lista.add(new Paquete("PQ105", "Salento", 1.0, 3, 10));
        lista.add(new Paquete("PQ942", "Armenia", 1.0, 3, 10));
        lista.add(new Paquete("PQ318", "Calarca", 1.0, 3, 10));
        lista.add(new Paquete("PQ567", "Salento", 1.0, 3, 10));
        Collections.sort(lista);
        List<String> codigos = new ArrayList<>();
        for (Paquete q : lista) {
            codigos.add(q.getCodigo());
        }
        Prueba.verificar(codigos.equals(Arrays.asList("PQ105", "PQ318", "PQ567", "PQ731", "PQ942")),
                "Collections.sort ordena por codigo");

        // 6. equals y hashCode solo por codigo
        Paquete a = new Paquete("PQ105", "Salento", 3.0, 5, 25);
        Paquete b = new Paquete("PQ105", "Armenia", 9.0, 2, 40);
        Prueba.verificar(a.equals(b), "dos paquetes con el mismo codigo son iguales");
        Prueba.verificar(a.hashCode() == b.hashCode(),
                "dos paquetes con el mismo codigo tienen el mismo hashCode");
        Prueba.verificar(!a.equals(new Paquete("PQ106", "Salento", 3.0, 5, 25)),
                "paquetes con distinto codigo no son iguales");
    }
}
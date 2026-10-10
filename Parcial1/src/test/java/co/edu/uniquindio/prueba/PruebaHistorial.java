package co.edu.uniquindio.prueba;

import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.servicio.HistorialEntregas;
import co.edu.uniquindio.util.Prueba;

/**
 * Pruebas del historial de entregas.
 * Revisamos que se guarde el orden real de las entregas y que no deje
 * registrar repetidos ni nulos.
 */
public class PruebaHistorial {

    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    public static void ejecutar() {
        Prueba.titulo("Historial de entregas");

        Paquete pq105 = new Paquete("PQ105", "Salento", 3.0, 5, 25);
        Paquete pq942 = new Paquete("PQ942", "Armenia", 4.6, 4, 20);
        Paquete pq731 = new Paquete("PQ731", "Calarca", 12.0, 5, 45);

        // 1. Se conserva el orden en que se registran
        HistorialEntregas historial = new HistorialEntregas();
        historial.registrar(pq105);
        historial.registrar(pq942);
        historial.registrar(pq731);
        Prueba.verificar(historial.toString().equals("PQ105 -> PQ942 -> PQ731"),
                "toString muestra PQ105 -> PQ942 -> PQ731");

        // 2. El for-each da el mismo orden, obtener(1) y cantidad()
        String recorrido = "";
        for (Paquete paquete : historial) {
            recorrido = recorrido + paquete.getCodigo() + " ";
        }
        Prueba.verificar(recorrido.equals("PQ105 PQ942 PQ731 "),
                "El for-each recorre en el orden de entrega");
        Prueba.verificar(historial.obtener(1).getCodigo().equals("PQ942"),
                "obtener(1) es PQ942");
        Prueba.verificar(historial.cantidad() == 3, "cantidad() es 3");

        // 3. Repetido y nulo dan error
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> historial.registrar(pq105), "Registrar PQ105 otra vez lanza IllegalStateException");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> historial.registrar(null), "Registrar null lanza IllegalArgumentException");
        Prueba.verificar(historial.cantidad() == 3, "Despues de los errores la cantidad sigue en 3");

        // 4. Historial nuevo
        HistorialEntregas vacio = new HistorialEntregas();
        Prueba.verificar(vacio.estaVacio(), "Un historial nuevo esta vacio");
        Prueba.verificar(vacio.toString().equals("(sin entregas)"),
                "Un historial vacio muestra (sin entregas)");

        // 5. contiene
        Prueba.verificar(historial.contiene("PQ942"), "contiene(PQ942) es true");
        Prueba.verificar(!historial.contiene("PQ000"), "contiene(PQ000) es false");
    }
}
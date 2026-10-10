package co.edu.uniquindio.prueba;

import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.modelo.Repartidor;
import co.edu.uniquindio.servicio.GestorRepartidores;
import co.edu.uniquindio.util.Prueba;

import java.util.List;

/**
 * Pruebas de GestorRepartidores: registro, consulta, asignacion y liberacion.
 */
public class PruebaGestorRepartidores {

    /** Corre las pruebas y muestra el resumen. */
    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    /** Contiene todos los casos de prueba de GestorRepartidores. */
    public static void ejecutar() {
        Prueba.titulo("GestorRepartidores");

        GestorRepartidores gestor = new GestorRepartidores();
        Repartidor r1 = new Repartidor("R1", "Ana", "Armenia");
        Repartidor r2 = new Repartidor("R2", "Luis", "Salento", false);
        Repartidor r3 = new Repartidor("R3", "Marta", "Armenia");
        Repartidor r4 = new Repartidor("R4", "Pedro", "Calarca");
        gestor.registrar(r1);
        gestor.registrar(r2);
        gestor.registrar(r3);
        gestor.registrar(r4);

        // 1. Registro
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> gestor.registrar(new Repartidor("R1", "Otra", "Armenia")),
                "registrar una identificacion repetida");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> gestor.registrar(null), "registrar un repartidor nulo");
        List<Repartidor> lista = gestor.listar();
        Prueba.verificar(lista.size() == 4 && lista.get(0) == r1 && lista.get(3) == r4,
                "listar mantiene el orden de registro");
        lista.clear();
        Prueba.verificar(gestor.listar().size() == 4, "modificar la lista devuelta no afecta al gestor");

        // 2. Consultas
        Prueba.verificar(gestor.buscar("R1") == r1, "buscar R1 devuelve a Ana");
        Prueba.verificar(gestor.buscar("R9") == null, "buscar un id inexistente devuelve null");
        Prueba.verificar(gestor.existe("R3") && !gestor.existe("R9"), "existe responde bien");

        // 3. Disponible por zona
        Prueba.verificar(gestor.buscarDisponiblePorZona("Armenia") == r1,
                "el primer disponible de Armenia es R1");
        Prueba.verificar(gestor.buscarDisponiblePorZona("Salento") == null,
                "en Salento no hay disponibles (R2 esta ocupado)");
        Prueba.verificar(gestor.buscarDisponiblePorZona("Filandia") == null,
                "en una zona sin repartidores devuelve null");

        // 4. Asignacion correcta
        Paquete p1 = new Paquete("PQ101", "Armenia", 4.5, 3, 20);
        gestor.asignar(p1, "R1");
        Prueba.verificar(!r1.isDisponible(), "al asignar, R1 queda no disponible");
        Prueba.verificar(gestor.obtenerAsignado("PQ101") == r1, "obtenerAsignado devuelve a R1");

        // 5. Repartidor no disponible
        Paquete p2 = new Paquete("PQ102", "Armenia", 2.0, 3, 15);
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> gestor.asignar(p2, "R1"), "asignar a un repartidor no disponible");

        // 6. Zona distinta
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> gestor.asignar(p2, "R4"), "asignar a un repartidor de otra zona");
        Prueba.verificar(r4.isDisponible(), "R4 sigue disponible despues del rechazo por zona");
        Prueba.verificar(gestor.obtenerAsignado("PQ102") == null, "el paquete rechazado no queda asignado");

        // 7. Repartidor inexistente y paquete nulo
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> gestor.asignar(p2, "R9"), "asignar a un repartidor inexistente");
        Prueba.esperarExcepcion(IllegalArgumentException.class,
                () -> gestor.asignar(null, "R1"), "asignar un paquete nulo");

        // 8. Paquete ya asignado
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> gestor.asignar(p1, "R3"), "asignar dos veces el mismo paquete");
        Prueba.verificar(r3.isDisponible(), "R3 sigue disponible despues de ese rechazo");

        // 9. Asignacion automatica
        Repartidor elegido = gestor.asignarAutomaticamente(p2);
        Prueba.verificar(elegido == r3, "la asignacion automatica elige a R3 (R1 esta ocupado)");
        Prueba.verificar(gestor.obtenerAsignado("PQ102") == r3, "queda registrada la asignacion de PQ102");
        Paquete p3 = new Paquete("PQ103", "Armenia", 1.0, 3, 10);
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> gestor.asignarAutomaticamente(p3), "asignacion automatica sin repartidores libres");
        Paquete p4 = new Paquete("PQ104", "Salento", 1.0, 3, 10);
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> gestor.asignarAutomaticamente(p4), "asignacion automatica en zona sin disponibles");

        // 10. Liberar
        Repartidor liberado = gestor.liberar("PQ101");
        Prueba.verificar(liberado == r1, "liberar devuelve a R1");
        Prueba.verificar(r1.isDisponible(), "despues de liberar, R1 esta disponible");
        Prueba.verificar(gestor.obtenerAsignado("PQ101") == null, "despues de liberar no hay asignado");
        Prueba.esperarExcepcion(IllegalStateException.class,
                () -> gestor.liberar("PQ101"), "liberar un paquete sin repartidor");
    }
}
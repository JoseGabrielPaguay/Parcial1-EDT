package co.edu.uniquindio.prueba;

import co.edu.uniquindio.datos.DatosIniciales;
import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.modelo.Repartidor;
import co.edu.uniquindio.util.Prueba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Verifica que los datos quemados cumplan los minimos del enunciado. */
public class PruebaDatosIniciales {

    public static void main(String[] args) {
        ejecutar();
        Prueba.resumen();
    }

    public static void ejecutar() {
        Prueba.titulo("DatosIniciales");
        List<Paquete> paquetes = DatosIniciales.crearPaquetes();
        List<Repartidor> repartidores = DatosIniciales.crearRepartidores();

        pruebaCantidades(paquetes, repartidores);
        pruebaCodigos(paquetes);
        pruebaDestinos(paquetes, repartidores);
        pruebaPrioridadesYPesos(paquetes);
        pruebaRepartidores(repartidores);
        pruebaListasIndependientes();
    }

    private static void pruebaCantidades(List<Paquete> paquetes, List<Repartidor> repartidores) {
        Prueba.verificar(paquetes.size() >= 30, "Hay 30 o mas paquetes (hay " + paquetes.size() + ")");
        Prueba.verificar(repartidores.size() >= 6, "Hay 6 o mas repartidores (hay " + repartidores.size() + ")");
    }

    private static void pruebaCodigos(List<Paquete> paquetes) {
        Set<String> codigos = new HashSet<>();
        List<Integer> numeros = new ArrayList<>();
        for (Paquete paquete : paquetes) {
            codigos.add(paquete.getCodigo());
            numeros.add(Integer.parseInt(paquete.getCodigo().substring(2)));
        }
        Prueba.verificar(codigos.size() == paquetes.size(), "Los codigos de paquetes no se repiten");

        // No seguidos: al ordenar los numeros, entre uno y el siguiente hay mas de 1 de diferencia
        List<Integer> ordenados = new ArrayList<>(numeros);
        ordenados.sort(null);
        boolean noSeguidos = true;
        for (int i = 1; i < ordenados.size(); i++) {
            if (ordenados.get(i) - ordenados.get(i - 1) <= 1) {
                noSeguidos = false;
            }
        }
        Prueba.verificar(noSeguidos, "Los codigos no son consecutivos");

        // La lista de llegada no esta ordenada por codigo
        Prueba.verificar(!numeros.equals(ordenados), "El orden de llegada no coincide con el orden por codigo");
    }

    private static void pruebaDestinos(List<Paquete> paquetes, List<Repartidor> repartidores) {
        Map<String, Integer> paquetesPorDestino = new HashMap<>();
        for (Paquete paquete : paquetes) {
            paquetesPorDestino.merge(paquete.getDestino(), 1, Integer::sum);
        }
        Prueba.verificar(paquetesPorDestino.size() >= 5,
                "Hay 5 o mas destinos (hay " + paquetesPorDestino.size() + ")");

        boolean hayDestinoRepetido = false;
        for (int cantidad : paquetesPorDestino.values()) {
            if (cantidad >= 2) {
                hayDestinoRepetido = true;
            }
        }
        Prueba.verificar(hayDestinoRepetido, "Algun destino tiene 2 o mas paquetes");

        // Cada destino tiene al menos un repartidor disponible en su zona
        boolean todosConRepartidor = true;
        for (String destino : paquetesPorDestino.keySet()) {
            boolean hayDisponible = false;
            for (Repartidor repartidor : repartidores) {
                if (repartidor.getZona().equals(destino) && repartidor.isDisponible()) {
                    hayDisponible = true;
                }
            }
            if (!hayDisponible) {
                todosConRepartidor = false;
            }
        }
        Prueba.verificar(todosConRepartidor, "Cada destino tiene un repartidor disponible");
    }

    private static void pruebaPrioridadesYPesos(List<Paquete> paquetes) {
        Set<Integer> prioridades = new HashSet<>();
        Set<Double> pesos = new HashSet<>();
        double pesoMaximo = 0;
        int vecesMaximo = 0;
        int conPrioridadPorDefecto = 0;

        for (Paquete paquete : paquetes) {
            prioridades.add(paquete.getPrioridad());
            pesos.add(paquete.getPeso());
            if (paquete.getPrioridad() == Paquete.PRIORIDAD_POR_DEFECTO) {
                conPrioridadPorDefecto++;
            }
            if (paquete.getPeso() > pesoMaximo) {
                pesoMaximo = paquete.getPeso();
                vecesMaximo = 1;
            } else if (paquete.getPeso() == pesoMaximo) {
                vecesMaximo++;
            }
        }

        boolean todasLasPrioridades = true;
        for (int p = Paquete.PRIORIDAD_MINIMA; p <= Paquete.PRIORIDAD_MAXIMA; p++) {
            if (!prioridades.contains(p)) {
                todasLasPrioridades = false;
            }
        }
        Prueba.verificar(todasLasPrioridades, "Aparecen todas las prioridades del rango");
        Prueba.verificar(conPrioridadPorDefecto >= 1, "Hay paquetes con la prioridad por defecto");
        Prueba.verificar(pesos.size() >= 10, "Hay variedad de pesos (" + pesos.size() + " distintos)");
        Prueba.verificar(vecesMaximo == 1, "El peso maximo aparece una sola vez");

        // Dos paquetes con la misma prioridad y el mismo tiempo estimado
        boolean hayEmpate = false;
        for (int i = 0; i < paquetes.size(); i++) {
            for (int j = i + 1; j < paquetes.size(); j++) {
                Paquete a = paquetes.get(i);
                Paquete b = paquetes.get(j);
                if (a.getPrioridad() == b.getPrioridad() && a.getTiempoEstimado() == b.getTiempoEstimado()) {
                    hayEmpate = true;
                }
            }
        }
        Prueba.verificar(hayEmpate, "Hay dos paquetes con la misma prioridad y el mismo tiempo");
    }

    private static void pruebaRepartidores(List<Repartidor> repartidores) {
        Set<String> ids = new HashSet<>();
        int noDisponibles = 0;
        for (Repartidor repartidor : repartidores) {
            ids.add(repartidor.getIdentificacion());
            if (!repartidor.isDisponible()) {
                noDisponibles++;
            }
        }
        Prueba.verificar(ids.size() == repartidores.size(), "Las identificaciones de repartidores no se repiten");
        Prueba.verificar(noDisponibles >= 1, "Hay al menos un repartidor no disponible");
    }

    private static void pruebaListasIndependientes() {
        List<Paquete> primera = DatosIniciales.crearPaquetes();
        primera.remove(0);
        Prueba.verificar(DatosIniciales.crearPaquetes().size() > primera.size(),
                "Cada llamada a crearPaquetes devuelve una lista nueva");
    }
}
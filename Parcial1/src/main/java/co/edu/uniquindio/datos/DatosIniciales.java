package co.edu.uniquindio.datos;

import co.edu.uniquindio.modelo.Paquete;
import co.edu.uniquindio.modelo.Repartidor;

import java.util.ArrayList;
import java.util.List;

/**
 * Datos quemados para probar el sistema. No se leen archivos ni bases de datos.
 * El orden de la lista de paquetes es el orden en que llegaron al centro.
 */
public final class DatosIniciales {

    // Los destinos se escriben siempre igual (sin tildes) para evitar errores de digitacion
    public static final String ARMENIA = "Armenia";
    public static final String CALARCA = "Calarca";
    public static final String CIRCASIA = "Circasia";
    public static final String MONTENEGRO = "Montenegro";
    public static final String QUIMBAYA = "Quimbaya";
    public static final String SALENTO = "Salento";

    /** Constructor privado: esta clase solo tiene metodos static. */
    private DatosIniciales() {
    }

    /**
     * Crea los 32 paquetes de la jornada, en orden de llegada.
     * Los paquetes creados sin prioridad usan la prioridad por defecto.
     */
    public static List<Paquete> crearPaquetes() {
        List<Paquete> paquetes = new ArrayList<>();

        // codigo, destino, peso (kg), prioridad, tiempo estimado (min)
        paquetes.add(new Paquete("PQ731", ARMENIA, 4.5, 5, 45));
        paquetes.add(new Paquete("PQ105", SALENTO, 3.0, 5, 25));
        paquetes.add(new Paquete("PQ942", ARMENIA, 7.2, 4, 20));
        paquetes.add(new Paquete("PQ318", CALARCA, 2.3, 3, 30));
        paquetes.add(new Paquete("PQ567", SALENTO, 12.0, 2, 50));
        paquetes.add(new Paquete("PQ829", ARMENIA, 5.5, 1, 35));
        paquetes.add(new Paquete("PQ403", CIRCASIA, 8.4, 3, 40));
        paquetes.add(new Paquete("PQ162", MONTENEGRO, 3.2, 4, 28));
        paquetes.add(new Paquete("PQ688", QUIMBAYA, 6.1, 2, 55));
        paquetes.add(new Paquete("PQ247", ARMENIA, 1.8, 5, 15));
        paquetes.add(new Paquete("PQ905", ARMENIA, 9.9, 4, 20)); // misma prioridad y tiempo que PQ942
        paquetes.add(new Paquete("PQ988", SALENTO, 2.6, 38));    // prioridad por defecto
        paquetes.add(new Paquete("PQ131", CALARCA, 14.3, 3, 42));
        paquetes.add(new Paquete("PQ754", MONTENEGRO, 5.8, 2, 33));
        paquetes.add(new Paquete("PQ372", ARMENIA, 6.7, 3, 25));
        paquetes.add(new Paquete("PQ541", QUIMBAYA, 4.1, 5, 22));
        paquetes.add(new Paquete("PQ218", CIRCASIA, 10.5, 1, 60));
        paquetes.add(new Paquete("PQ924", SALENTO, 3.7, 4, 18));
        paquetes.add(new Paquete("PQ468", CALARCA, 7.9, 27));    // prioridad por defecto
        paquetes.add(new Paquete("PQ795", ARMENIA, 2.2, 2, 48));
        paquetes.add(new Paquete("PQ583", MONTENEGRO, 27.4, 3, 52)); // el mas pesado
        paquetes.add(new Paquete("PQ344", QUIMBAYA, 1.5, 4, 12));
        paquetes.add(new Paquete("PQ847", SALENTO, 8.0, 3, 31));
        paquetes.add(new Paquete("PQ275", CIRCASIA, 5.0, 2, 36));
        paquetes.add(new Paquete("PQ662", ARMENIA, 3.4, 44));    // prioridad por defecto
        paquetes.add(new Paquete("PQ436", CALARCA, 6.3, 1, 29));
        paquetes.add(new Paquete("PQ967", MONTENEGRO, 9.1, 5, 14));
        paquetes.add(new Paquete("PQ189", QUIMBAYA, 2.9, 3, 41));
        paquetes.add(new Paquete("PQ709", SALENTO, 4.8, 2, 24));
        paquetes.add(new Paquete("PQ502", CIRCASIA, 11.2, 4, 46));
        paquetes.add(new Paquete("PQ881", CALARCA, 1.2, 5, 10));
        paquetes.add(new Paquete("PQ624", ARMENIA, 3.9, 4, 26));

        return paquetes;
    }

    /**
     * Crea los 8 repartidores: 3 en Armenia (uno no disponible)
     * y 1 en cada uno de los otros 5 destinos.
     */
    public static List<Repartidor> crearRepartidores() {
        List<Repartidor> repartidores = new ArrayList<>();

        // identificacion, nombre, zona, disponible
        repartidores.add(new Repartidor("R01", "Carlos Ramirez", ARMENIA, true));
        repartidores.add(new Repartidor("R02", "Luisa Fernanda Gomez", ARMENIA, false)); // no disponible
        repartidores.add(new Repartidor("R03", "Andres Felipe Torres", ARMENIA, true));
        repartidores.add(new Repartidor("R04", "Marta Londono", CALARCA, true));
        repartidores.add(new Repartidor("R05", "Julian Herrera", CIRCASIA, true));
        repartidores.add(new Repartidor("R06", "Paola Giraldo", MONTENEGRO, true));
        repartidores.add(new Repartidor("R07", "Sebastian Ocampo", QUIMBAYA, true));
        repartidores.add(new Repartidor("R08", "Diana Marcela Arias", SALENTO, true));

        return repartidores;
    }
}

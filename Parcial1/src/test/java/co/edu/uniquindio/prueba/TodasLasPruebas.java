package co.edu.uniquindio.prueba;
import co.edu.uniquindio.util.Prueba;

public class TodasLasPruebas {
    public static void main(String[] args) {
        // Etapa 1
        PruebaListaEnlazada.ejecutar();
        PruebaPaquete.ejecutar();
        PruebaRepartidor.ejecutar();
        PruebaComparadores.ejecutar();
        // Etapa 2
        PruebaHistorial.ejecutar();
        PruebaAnalisisRecursivo.ejecutar();
        PruebaBusquedaBinaria.ejecutar();
        PruebaDivideYVenceras.ejecutar();
        PruebaGestorRepartidores.ejecutar();
        // Etapa 3
        PruebaRegistroPaquetes.ejecutar();
        PruebaGestorDespacho.ejecutar();
        PruebaDatosIniciales.ejecutar();
        // Etapa 4
        PruebaCentroDistribucion.ejecutar();
        PruebaIntegral.ejecutar();
        Prueba.resumen();

    }
}



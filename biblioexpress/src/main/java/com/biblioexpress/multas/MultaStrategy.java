package com.biblioexpress.multas;

/**
 * UNIDAD 3 - PATRON STRATEGY (interfaz Unidad 1 tambien).
 *
 * "Strategy" = tenemos varias formas de hacer lo mismo (calcular una multa)
 * y queremos poder cambiar de forma sin tocar el resto del codigo.
 *
 * Esta interfaz define EL CONTRATO: "dame los dias de atraso y te digo cuanto es la multa".
 * Cada clase que la implementa es una "estrategia" concreta.
 *
 * El PrestamoService guarda una estrategia "activa" y la usa sin saber cual es.
 * Si manana agregan MultaVerano, solo crean una clase nueva: no se toca el service.
 * (Principio Abierto/Cerrado: abierto a extension, cerrado a modificacion.)
 */
public interface MultaStrategy {

    /** Calcula el monto de la multa para esa cantidad de dias de atraso. */
    double calcular(int diasAtraso);

    /** Nombre corto de la estrategia (NORMAL, CAMPANIA, FIN_DE_SEMANA). */
    String nombre();
}

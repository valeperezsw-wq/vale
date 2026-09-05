package com.biblioexpress.dto;

/**
 * UNIDAD 2 - DTO de salida al devolver un material.
 *
 * Muestra el desglose de la multa para que se entienda como se calculo:
 *  - estrategiaAplicada: NORMAL / CAMPANIA / FIN_DE_SEMANA
 *  - multaBase: lo que dio la estrategia
 *  - tipoSocio: REGULAR / PREMIUM
 *  - multaFinal: despues de aplicar el beneficio del socio
 */
public record DevolverResponse(
        String codigoMaterial,
        int diasAtraso,
        String estrategiaAplicada,
        double multaBase,
        String tipoSocio,
        double multaFinal,
        String mensaje
) {
}

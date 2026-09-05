package com.biblioexpress.dto;

import java.time.LocalDate;

/**
 * UNIDAD 2 - DTO de salida al prestar un material.
 */
public record PrestamoResponse(
        String codigoMaterial,
        String tituloMaterial,
        int socioId,
        String nombreSocio,
        LocalDate fechaPrestamo,
        String mensaje
) {
}

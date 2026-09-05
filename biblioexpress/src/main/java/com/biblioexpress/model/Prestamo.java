package com.biblioexpress.model;

import java.time.LocalDate;

/**
 * Representa un prestamo ACTIVO: quien se llevo que material y cuando.
 * Cuando el socio devuelve, este prestamo se cierra (se borra del repositorio).
 */
public class Prestamo {

    private final String codigoMaterial;
    private final int socioId;
    private final LocalDate fechaPrestamo;

    public Prestamo(String codigoMaterial, int socioId, LocalDate fechaPrestamo) {
        this.codigoMaterial = codigoMaterial;
        this.socioId = socioId;
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getCodigoMaterial() {
        return codigoMaterial;
    }

    public int getSocioId() {
        return socioId;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
}

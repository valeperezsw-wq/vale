package com.biblioexpress.exception;

/**
 * Se lanza cuando la operacion no respeta una regla del negocio
 * (ej: prestar un material que ya esta prestado).
 * El manejador global la traduce a HTTP 409 (conflicto).
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}

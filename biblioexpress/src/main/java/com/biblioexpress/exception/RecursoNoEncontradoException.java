package com.biblioexpress.exception;

/**
 * Se lanza cuando se busca algo que no existe (un socio, un material, un prestamo).
 * El manejador global la traduce a HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

package com.biblioexpress.model;

/**
 * UNIDAD 1 - HERENCIA.
 *
 * Revista ES-UN Material y ademas tiene "numeroEdicion".
 */
public class Revista extends Material {

    private int numeroEdicion;

    public Revista(String codigo, String titulo, boolean disponible, int numeroEdicion) {
        super(codigo, titulo, disponible);
        this.numeroEdicion = numeroEdicion;
    }

    @Override
    public String getTipo() {
        return "REVISTA";
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }
}

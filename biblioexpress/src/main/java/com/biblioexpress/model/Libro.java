package com.biblioexpress.model;

/**
 * UNIDAD 1 - HERENCIA.
 *
 * Libro ES-UN Material (extends Material) y ademas tiene "autor".
 * Reutiliza codigo/titulo/disponible de la clase padre y solo agrega lo propio.
 */
public class Libro extends Material {

    private String autor;

    public Libro(String codigo, String titulo, boolean disponible, String autor) {
        super(codigo, titulo, disponible); // llama al constructor de Material
        this.autor = autor;
    }

    @Override
    public String getTipo() {
        return "LIBRO";
    }

    public String getAutor() {
        return autor;
    }
}

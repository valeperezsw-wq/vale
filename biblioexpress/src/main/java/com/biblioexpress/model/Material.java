package com.biblioexpress.model;

/**
 * UNIDAD 1 - HERENCIA.
 *
 * Material es una clase ABSTRACTA: representa "algo que la biblioteca presta",
 * pero nunca se crea un Material "a secas". Siempre es un Libro o una Revista.
 *
 * Pusimos aca los atributos y el comportamiento COMUN a todos los materiales:
 *  - codigo: identificador unico (ej "L-001")
 *  - titulo
 *  - disponible: true si esta en la biblioteca, false si esta prestado
 *
 * Libro y Revista HEREDAN de esta clase (extends) y agregan lo suyo.
 */
public abstract class Material {

    private String codigo;
    private String titulo;
    private boolean disponible;

    protected Material(String codigo, String titulo, boolean disponible) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.disponible = disponible;
    }

    /**
     * Metodo abstracto: cada subclase DEBE decir de que tipo es.
     * Sirve para mostrarlo en la respuesta JSON sin exponer la clase Java.
     */
    public abstract String getTipo();

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}

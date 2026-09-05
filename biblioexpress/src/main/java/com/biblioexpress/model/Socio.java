package com.biblioexpress.model;

/**
 * UNIDAD 1 - HERENCIA + POLIMORFISMO.
 *
 * Socio es abstracta. Todo socio tiene id y nombre.
 *
 * El metodo aplicarBeneficio(monto) es la clave del polimorfismo:
 *  - SocioRegular NO hace descuento -> devuelve el mismo monto.
 *  - SocioPremium hace 50% de descuento -> devuelve monto / 2.
 *
 * El service llama socio.aplicarBeneficio(multa) SIN preguntar de que tipo es.
 * Cada subclase responde a su manera. Eso es polimorfismo.
 */
public abstract class Socio {

    private int id;
    private String nombre;

    protected Socio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Recibe un monto (por ejemplo, una multa) y devuelve cuanto paga
     * realmente este socio despues de aplicar su beneficio.
     */
    public abstract double aplicarBeneficio(double monto);

    /** Texto para mostrar en el JSON sin exponer la clase Java. */
    public abstract String getTipo();

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}

package com.biblioexpress.model;

/**
 * Socio premium: 50% de descuento sobre las multas.
 */
public class SocioPremium extends Socio {

    private static final double DESCUENTO = 0.50;

    public SocioPremium(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    public double aplicarBeneficio(double monto) {
        return monto * (1 - DESCUENTO); // paga la mitad
    }

    @Override
    public String getTipo() {
        return "PREMIUM";
    }
}

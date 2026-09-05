package com.biblioexpress.model;

/**
 * Socio comun: no tiene ningun beneficio, paga la multa completa.
 */
public class SocioRegular extends Socio {

    public SocioRegular(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    public double aplicarBeneficio(double monto) {
        return monto; // sin descuento
    }

    @Override
    public String getTipo() {
        return "REGULAR";
    }
}

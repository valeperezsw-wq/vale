package com.biblioexpress.multas;

import org.springframework.stereotype.Component;

/**
 * Estrategia de campania (promocion): $60 por dia de atraso.
 */
@Component
public class MultaCampania implements MultaStrategy {

    private static final double POR_DIA = 60.0;

    @Override
    public double calcular(int diasAtraso) {
        return diasAtraso * POR_DIA;
    }

    @Override
    public String nombre() {
        return "CAMPANIA";
    }
}

package com.biblioexpress.multas;

import org.springframework.stereotype.Component;

/**
 * Estrategia de fin de semana: ademas de $100 por dia, suma un recargo fijo
 * de $200 al total (interpretacion de "MultaFinDeSemana: suma $200 al total").
 *
 * OJO: esta regla es la que mas dudas genera. Si en el examen el enunciado no
 * aclara, preguntale al profe si $200 es un recargo fijo que se SUMA (como aca)
 * o si es el total plano. Cambiar la formula es una sola linea.
 */
@Component
public class MultaFinDeSemana implements MultaStrategy {

    private static final double POR_DIA = 100.0;
    private static final double RECARGO_FIJO = 200.0;

    @Override
    public double calcular(int diasAtraso) {
        return diasAtraso * POR_DIA + RECARGO_FIJO;
    }

    @Override
    public String nombre() {
        return "FIN_DE_SEMANA";
    }
}
